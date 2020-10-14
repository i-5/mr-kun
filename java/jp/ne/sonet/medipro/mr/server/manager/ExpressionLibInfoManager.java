package jp.ne.sonet.medipro.mr.server.manager;

import java.util.Vector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jp.ne.sonet.medipro.mr.server.entity.ExpressionLibInfo;
import jp.ne.sonet.medipro.mr.common.exception.MrException; 

/**
 * <strong>��^�����C�u�������Ǘ�</strong>
 * <br>
 * @author: 
 * @version: 
 */
public class ExpressionLibInfoManager {
    /** ��^�����ꗗ�擾 */
    protected static final String EXPRESSION_LIST_SQL
	= "SELECT"
	+ " teikeibun_cd"
	+ ",title"
	+ " FROM teikeibun_lib"
	+ " WHERE"
	+ " company_cd = ?"
	+ " AND delete_ymd IS NULL";

    /** ��^���{���擾 */
    protected static final String EXPRESSION_INFO_SQL
	= "SELECT"
	+ " honbun"
	+ " FROM teikeibun_lib"
	+ " WHERE"
	+ " teikeibun_cd = ?";
                                    
    /** �R�l�N�V�����E�I�u�W�F�N�g */
    protected Connection connection = null;

    /**
     * ExpressionLibInfoManager �I�u�W�F�N�g��V�K�ɍ쐬����B
     * @param conn �R�l�N�V�����E�I�u�W�F�N�g
     */
    public ExpressionLibInfoManager(Connection initConnection) {
        this.connection = initConnection;
    }

    /**
     * ��^���ꗗ�����擾����B
     * @param  campanyCd ��ЃR�[�h
     * @return ��^���ꗗ���
     */
    public Vector getExpressionLibList(String companyCd) {
        Vector list = new Vector();
        
        try {
	    PreparedStatement pstmt = connection.prepareStatement(EXPRESSION_LIST_SQL);
	    pstmt.setString(1, companyCd);

	    ResultSet rs = pstmt.executeQuery();

	    while (rs.next()) {
		ExpressionLibInfo info = new ExpressionLibInfo();
		info.setTeikeibunCd(rs.getString("teikeibun_cd"));
		info.setTitle(rs.getString("title"));
		
		list.addElement(info);
	    }

	    pstmt.close();
        } catch (SQLException e) {
            throw new MrException(e);
        }
	
        return list;
    }

    /**
     * ��^�������擾����B
     * @param  expressionCd ��^���R�[�h
     * @return ��^�����
     */
    public ExpressionLibInfo getExpressionLibInfo(String expressionCd) {
	ExpressionLibInfo info = null;

        try {
	    PreparedStatement pstmt = connection.prepareStatement(EXPRESSION_INFO_SQL);
	    pstmt.setString(1, expressionCd);

	    ResultSet rs = pstmt.executeQuery();

	    if (rs.next()) {
		info = new ExpressionLibInfo();
		info.setHonbun(rs.getString("honbun"));
	    }
	    
	    pstmt.close();
        } catch (SQLException e) {
            throw new MrException(e);
        }
	
	return info;
    }

}
