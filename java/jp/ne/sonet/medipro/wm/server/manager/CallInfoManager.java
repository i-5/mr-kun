package jp.ne.sonet.medipro.wm.server.manager;

import java.util.*;
import java.sql.*;
import java.net.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.wm.common.*;
import jp.ne.sonet.medipro.wm.common.exception.*; 
import jp.ne.sonet.medipro.wm.server.entity.*;
import jp.ne.sonet.medipro.wm.server.session.*;

/**
 * <strong>�R�[�����e���Ǘ�</strong>
 * @author
 * @version
 */
public class CallInfoManager {
    protected Connection conn;

    /** ��{SQL */
    protected static final String CALL_INFO_MAINSQL =
	"SELECT call_naiyo_cd, call_naiyo " +
	"FROM call_naiyo " +
	"WHERE delete_ymd IS NULL AND company_cd = ";

    /** �R�[�����e�X�VSQL */
    protected static final String CALL_UPDATE_SQL =
	"UPDATE call_naiyo SET call_naiyo = ? " +
	"WHERE call_naiyo_cd = ? AND company_cd = ?";

    /** �R�[�����e�ǉ�SQL */
    protected static final String CALL_INSERT_SQL =
	"INSERT INTO call_naiyo VALUES(?, ?, ?, ?, SYSDATE, NULL)";

    /** �R�[�����e�폜SQL */
    protected static final String CALL_DELETE_SQL =
	"UPDATE call_naiyo SET delete_ymd = SYSDATE";

    /** �R�[�����e�d������SQL */
    protected static final String CALL_OVERLAP_SQL =
	"SELECT call_naiyo FROM call_naiyo " +
	"WHERE call_naiyo_cd = ? AND company_cd = ?";

    /** �R�[�����e����SQL */
    protected static final String CALL_SEARCH_SQL =
	"SELECT call_naiyo FROM call_naiyo " +
	"WHERE call_naiyo_cd = ? AND company_cd = ?";

    /** �R�[�����e�R�[�h���g�p���Ă��郁�b�Z�[�WID���擾����SQL */
    protected static final String MESSAGE_SEARCH_SQL =
	"SELECT message_id " +
	"FROM message_body";

    /**
     * CallInfoManager �I�u�W�F�N�g��V�K�ɍ쐬����B
     * @param conn �R�l�N�V�����I�u�W�F�N�g
     */
    public CallInfoManager(Connection conn) {
        this.conn = conn;
    }

    /**
     * �R�[�����e���(�\�[�g�w��)�̎擾
     * @param ses �Z�b�V�����I�u�W�F�N�g
     * @return �R�[�����e���(�e�v�f��CallInfo�I�u�W�F�N�g)
     */
    public Vector getCallInfo(HttpSession ses) {
        ResultSet rs;
        Statement stmt;
        CallInfo  callInfo = null;
        Vector    callList;
        String    sql;
        int       cnt;
        int       curRow;
        int       pageRow;

        callList = new Vector();
        Common comSes = (Common)ses.getValue(SysCnst.KEY_COMMON_SESSION);
        CallListSession callSes = (CallListSession)ses.getValue(SysCnst.KEY_CALLLIST_SESSION);

        try {
            // SQL ����
            sql = CALL_INFO_MAINSQL;
            sql += "'" + comSes.getCompanyCd() + "'";
            sql += " ORDER BY " + callSes.getSortKey() + " " + callSes.getOrder();

            stmt = conn.createStatement();
            try {
                // SQL ���s
		if (SysCnst.DEBUG) {
		    System.err.println("### CallInfoManager : sql = " + sql);
		}
                rs      = stmt.executeQuery(sql);
                curRow  = callSes.getCurrentRow();
                pageRow = comSes.getCallLine();
                cnt     = 0;
                while (rs.next()) {
                    callInfo = new CallInfo();
                    callInfo.setCallCD(rs.getString("call_naiyo_cd"));
                    callInfo.setCallNaiyo(rs.getString("call_naiyo"));

                    // �y�[�W�J�n�s����y�[�W�\���s���̂݊i�[����
                    if (cnt >= (curRow - 1) && cnt < (curRow + pageRow - 1)) {
                        callList.add(callInfo);
                    }
                    cnt++;
                }

                // �O�y�[�W/���y�[�W�̗L����ݒ�
                callSes.setPrev(curRow != 1);
                callSes.setNext(cnt - curRow >= pageRow);
            }
            catch (SQLException e) {
                throw new WmException(e);
            }
            finally {
                stmt.close();
            }
        }
        catch (SQLException e) {
            throw new WmException(e);
        }
        return callList;
    }

    /**
     * �R�[�����e�R�[�h���폜����
     * @param ses      �Z�b�V�����I�u�W�F�N�g
     * @param callList �R�[�����e�R�[�h���X�g(�e�v�f��String�I�u�W�F�N�g)
     * @return 1:�폜�ł��� 0:�폜�ł��Ȃ�����
     */
    public int deleteCallInfo(HttpSession ses, Vector callList) {
        Common comSes = (Common)ses.getValue(SysCnst.KEY_COMMON_SESSION);
        try {
            String    sql;
            ResultSet rs;
            Statement stmt;

            // ���b�Z�[�W�{�̂ŃR�[�����e�R�[�h���g�p���Ă�����̂��������m�F
            sql = MESSAGE_SEARCH_SQL;
            sql += " WHERE call_naiyo_cd = '" + callList.elementAt(0) + "'";
            for (int i = 1; i < callList.size(); i++) {
                sql += " OR call_naiyo_cd = '" + callList.elementAt(i) + "'";
            }
	    if (SysCnst.DEBUG) {
		System.out.println("### CallInfoManager : sql = " + sql);
	    }
            stmt = conn.createStatement();
            try {
                rs = stmt.executeQuery(sql);
                if (rs.next() == true) {
                    // �폜�s��
                    return 0;
                }
            }
            finally {
                stmt.close();
            }

            // �폜����
            sql = CALL_DELETE_SQL;
            sql += " WHERE call_naiyo_cd = '" + callList.elementAt(0) + "'";
            for (int i = 1; i < callList.size(); i++) {
                sql += " OR call_naiyo_cd = '" + callList.elementAt(i) + "'";
            }
	    if (SysCnst.DEBUG) {
		System.out.println("### CallInfoManager : sql = " + sql);
	    }
            stmt = conn.createStatement();
            try {
                int cnt = stmt.executeUpdate(sql);
                return (cnt != 0) ? 1 : 0;
            }
            finally {
                stmt.close();
                conn.commit();
            }
        }
        catch (SQLException e) {
            throw new WmException(e);
        }
    }

    /**
     * �R�[�����e��ǉ�����
     * @param ses         �Z�b�V�����I�u�W�F�N�g
     * @param callNaiyoCd �R�[�����e�R�[�h
     * @param callNaiyo   �R�[�����e
     * @return �ǉ�������
     */
    public int insertCallInfo(HttpSession ses, String callNaiyoCd, String callNaiyo) {
        Common comSes = (Common)ses.getValue(SysCnst.KEY_COMMON_SESSION);
        PreparedStatement pstmt;
        int rc;

        try {
            pstmt = conn.prepareStatement(CALL_INSERT_SQL);
            try {
                // �R�[�����e�ǉ�
                pstmt.setString(1, callNaiyoCd);
                pstmt.setString(2, comSes.getCompanyCd());
                pstmt.setString(3, callNaiyo);
                pstmt.setString(4, comSes.getMrId());
                rc = pstmt.executeUpdate();
                return rc;
            }
            finally {
                pstmt.close();
                conn.commit();
            }
        }
        catch (SQLException e) {
            throw new WmException(e);
        }
    }

    /**
     * �R�[�����e��ύX����
     * @param ses         �Z�b�V�����I�u�W�F�N�g
     * @param callNaiyoCd �R�[�����e�R�[�h
     * @param callNaiyo   �R�[�����e
     * @return �ύX������
     */
    public int updateCallInfo(HttpSession ses, String callNaiyoCd, String callNaiyo) {
        Common comSes = (Common)ses.getValue(SysCnst.KEY_COMMON_SESSION);
        PreparedStatement pstmt;
        int rc;

        try {
            pstmt = conn.prepareStatement(CALL_UPDATE_SQL);
            try {
                // �R�[�����e�ύX
                pstmt.setString(1, callNaiyo);
                pstmt.setString(2, callNaiyoCd);
                pstmt.setString(3, comSes.getCompanyCd());
                rc = pstmt.executeUpdate();
                return rc;
            }
            finally {
                pstmt.close();
                conn.commit();
            }
        }
        catch (SQLException e) {
            throw new WmException(e);
        }
    }

    /**
     * �R�[�����e�̏d������
     * @param ses         �Z�b�V�����I�u�W�F�N�g
     * @param callNaiyoCd �R�[�����e�R�[�h
     * @return �d�����Ă����ꍇ��true
     */
    public boolean isOverlap(HttpSession ses, String callNaiyoCd) {
        Common comSes = (Common)ses.getValue(SysCnst.KEY_COMMON_SESSION);
        PreparedStatement pstmt;
        ResultSet rs;

        try {
            pstmt = conn.prepareStatement(CALL_OVERLAP_SQL);
            try {
                // �d������
                pstmt.setString(1, callNaiyoCd);
                pstmt.setString(2, comSes.getCompanyCd());
                rs = pstmt.executeQuery();
                return rs.next();
            }
            finally {
                pstmt.close();
            }
        }
        catch (SQLException e) {
            throw new WmException(e);
        }
    }

    /**
     * �R�[�����e�R�[�h����R�[�����e����������
     * @param ses �Z�b�V�����I�u�W�F�N�g
     * @param callNaiyoCd �R�[�����e�R�[�h
     * @return �R�[�����e
     */
    public String searchCallNaiyo(HttpSession ses, String callNaiyoCd) {
        Common comSes = (Common)ses.getValue(SysCnst.KEY_COMMON_SESSION);
        PreparedStatement pstmt;
        ResultSet rs;
        String callNaiyo = null;

        try {
            pstmt = conn.prepareStatement(CALL_SEARCH_SQL);
            try {
                // ����
                pstmt.setString(1, callNaiyoCd);
                pstmt.setString(2, comSes.getCompanyCd());
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    callNaiyo = rs.getString("call_naiyo");
                }
                return callNaiyo;
            }
            finally {
                pstmt.close();
            }
        }
        catch (SQLException e) {
            throw new WmException(e);
        }
    }
}
