package jp.ne.sonet.medipro.wm.common.util;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import jp.ne.sonet.medipro.wm.common.exception.WmException;
import jp.ne.sonet.medipro.wm.common.SysCnst;
import jp.ne.sonet.mrkun.dao.DAOFacade;

/**
 * <strong>DB�ڑ�Utility�N���X.</strong>
 * @author
 * @version
 */
public class DBConnect {

    /**
     * DBConnect �R���X�g���N�^�[�E�R�����g�B
     */
    public DBConnect() {
    }

    /**
     * �w�肵��Connection�̊J��.
     * @param connection �ΏۂƂȂ�Connection�I�u�W�F�N�g
     */
    public void closeDB(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            throw new WmException(e);
        }
    }

    /**
     * DB�Ƃ�Connection���擾����.
     * @return �A�v���P�[�V�����Ŏg�p����DB�ւ�Connection
     */
    public Connection getDBConnect() {
        Connection conn;
        try {
	    	conn = DAOFacade.getConnection();
        } catch (SQLException e) {
            throw new WmException(e);
        }
        return(conn);  
    }

    /**
     * �A�v���P�[�V�����Ŏg�p����DB��Connection������邩�`�F�b�N���܂�.
     * @return DB�ƍX�V�\�Ȃ�true
     */
    public static boolean isConnectable() {
	DBConnect dbConnect = new DBConnect();
	Connection connection = null;
	
	try {
	    connection = dbConnect.getDBConnect();

	    if (connection == null) {
		return false;
	    }

	    Statement stmt = connection.createStatement();
	    stmt.close();
	} catch (Exception ex) {
	    return false;
	} finally {
	    dbConnect.closeDB(connection);
	}

	return true;
    }
}
