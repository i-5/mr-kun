package jp.ne.sonet.medipro.wm.common.util;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import jp.ne.sonet.medipro.wm.common.exception.WmException;
import jp.ne.sonet.medipro.wm.common.SysCnst;
import jp.ne.sonet.mrkun.dao.DAOFacade;

/**
 * <strong>DB接続Utilityクラス.</strong>
 * @author
 * @version
 */
public class DBConnect {

    /**
     * DBConnect コンストラクター・コメント。
     */
    public DBConnect() {
    }

    /**
     * 指定したConnectionの開放.
     * @param connection 対象となるConnectionオブジェクト
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
     * DBとのConnectionを取得する.
     * @return アプリケーションで使用するDBへのConnection
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
     * アプリケーションで使用するDBとConnectionが張れるかチェックします.
     * @return DBと更新可能ならtrue
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
