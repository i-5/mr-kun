package jp.ne.sonet.medipro.mr.common.util;

import java.util.*;
import java.sql.*;
import java.net.*;
import jp.ne.sonet.medipro.mr.common.exception.*;
import jp.ne.sonet.mrkun.dao.DAOFacade;

/**
 * <h3>�c�a�R�l�N�g���[�e�B���e�B</h3>
 * 
 * <br>
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/25 �ߑO 02:10:24)
 * @author: 
 */
public class DBConnect {
    /**
     * DbConnect �R���X�g���N�^�[�E�R�����g�B
     */
    public DBConnect() {
    }
    /**
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/25 �ߑO 02:57:09)
     * @param conn com.sun.rmi.iiop.Connection
     * @exception jp.ne.sonet.medipro.mr.common.exception.MrException ��O�L�q
     */
    public void closeDB(Connection conn) {

	try {
	    if ( conn != null ) {
		conn.close();
	    }
	} catch (Exception e) {
	    throw new MrException(e);
	}
		
    }
    /**
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/25 �ߑO 02:11:35)
     * @return com.sun.rmi.iiop.Connection
     * @exception jp.ne.sonet.medipro.mr.common.exception.MrException ��O�L�q
     */
    public Connection getDBConnect() {

	Connection conn = null;
	
	try {
	    	conn = DAOFacade.getConnection();
	} catch (java.sql.SQLException e) {
	        throw new MrException(e);
	} 
	return(conn);
    }
}
