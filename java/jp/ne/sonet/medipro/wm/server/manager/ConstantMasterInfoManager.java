package jp.ne.sonet.medipro.wm.server.manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import jp.ne.sonet.medipro.wm.common.exception.WmException;
import jp.ne.sonet.medipro.wm.common.util.DBConnect;
import jp.ne.sonet.medipro.wm.server.session.Common;

/**
 * <strong>�萔�}�X�^�擾.</strong>
 * @author
 * @version
 */
public class ConstantMasterInfoManager {
    ////////////////////////////////////////////////////////////////////////////////
    // constants
    //
    /** �萔�擾�p */
    protected static final String MAIN_SQL
        = "select"
        + " constant_cd"
        + ",naiyo1"
        + ",naiyo2"
        + ",naiyo3"
        + " from constant_master";

    ////////////////////////////////////////////////////////////////////////////////
    // instace variables
    //
    /** DB�Ƃ̃R�l�N�V���� */
    protected Connection connection = null;

    ////////////////////////////////////////////////////////////////////////////////
    // constructors
    //
    /**
     * �R�l�N�V�����̃Z�b�g.
     * @param initConnection ���̃I�u�W�F�N�g�ɗ��p������R�l�N�V����
     */
    public ConstantMasterInfoManager(Connection initConnection) {
        this.connection = initConnection;
    }

    ////////////////////////////////////////////////////////////////////////////////
    // instance methods
    //
    /**
     * �萔�}�X�^�̒l��Common�ɃZ�b�g����.
     * @param common �ݒ�Ώ�
     */
    public void refreshCommon(Common common) {
        try {
            Statement stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery(MAIN_SQL);
          
            while (rs.next()) {
                String key = rs.getString("constant_cd");
                String val = rs.getString("naiyo1");
                String val2 = rs.getString("naiyo2");
                String val3 = rs.getString("naiyo3");

                if (key == null || val == null) {
                    continue;
                }

                if (key.equals("MRLINE")) {
                    common.setMrLine(new Integer(val).intValue());
                } else if (key.equals("MRCATCHLINE")) {
                    common.setMrCatchLine(new Integer(val).intValue());
                } else if (key.equals("SHITENLINE")) {
                    common.setShitenLine(new Integer(val).intValue());
                } else if (key.equals("ATTRIBUTELINE")) {
                    common.setAttributeLine(new Integer(val).intValue());
                } else if (key.equals("SUBLINE")) {
                    common.setSubLine(new Integer(val).intValue());
                } else if (key.equals("CATCHLINE")) {
                    common.setCatchLine(new Integer(val).intValue());
                } else if (key.equals("LINKLINE")) {
                    common.setLinkLine(new Integer(val).intValue());
                } else if (key.equals("BUNLINE")) {
                    common.setBunLine(new Integer(val).intValue());
                } else if (key.equals("CALLLINE")) {
                    common.setCallLine(new Integer(val).intValue());
                } else if (key.equals("CATCHHOME")) {
                    common.setCatchHome(val);
                    common.setTempDir(val2);
                    common.setDocumentRoot(val3);
                } else if (key.equals("TIMEOUT")) {
                    common.setTimeout(new Integer(val2).intValue());
                }
            }

            stmt.close();
        } catch (SQLException ex) {
            throw new WmException(ex);
        }
    }

    public static void main(String[] args) {
        DBConnect dbConnect = new DBConnect();
        Connection con = dbConnect.getDBConnect();
        ConstantMasterInfoManager manager = new ConstantMasterInfoManager(con);
        Common common = new Common();
        manager.refreshCommon(common);
        System.err.println(common.getMrLine());
        
        dbConnect.closeDB(con);
    }
}
