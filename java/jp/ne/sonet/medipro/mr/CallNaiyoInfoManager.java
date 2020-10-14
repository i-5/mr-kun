package jp.ne.sonet.medipro.mr;

import java.util.*;
import java.sql.*;
import java.net.*;
import jp.ne.sonet.mrkun.framework.exception.*;

/**
 * <h3>�R�[�����e���Ǘ�</h3>
 * 
 * <br>
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/21 �ߌ� 09:00:50)
 * @author: 
 */
public class CallNaiyoInfoManager {
    protected Connection conn;

    /**
     * CallNaiyoInfoManager �R���X�g���N�^�[�E�R�����g�B
     */
    public CallNaiyoInfoManager(Connection conn) {
	this.conn = conn;
    }

    /**
     * @return java.util.Enumeration (CallNaiyoInfo)
     * @param companyCD java.lang.String
     */
    public Enumeration getCallNaiyou(String companyCD) {
	CallNaiyoInfo call;
	Vector enum = new Vector();
	String sqltxt = null;

	try {
	    //�r�p�k��
	    sqltxt = "SELECT ";
	    sqltxt = sqltxt +  "call_naiyo_cd, ";
	    sqltxt = sqltxt +  "call_naiyo ";
	    sqltxt = sqltxt +  "FROM ";
	    sqltxt = sqltxt +  "call_naiyo ";
	    sqltxt = sqltxt +  "WHERE ";
	    sqltxt = sqltxt +  "company_cd = '" + companyCD + "' ";
	    sqltxt = sqltxt +  "AND delete_ymd is NULL ";
	    sqltxt = sqltxt +  "ORDER BY ";
	    sqltxt = sqltxt +  "call_naiyo_cd";
		
	    ResultSet rs;
	    Statement stmt = conn.createStatement();
	    try {
		rs   = stmt.executeQuery(sqltxt);
		    
		while ( rs.next() ) {
		    call = new CallNaiyoInfo(rs.getString("call_naiyo_cd"));
		    call.setCallNaiyo(rs.getString("call_naiyo"));
		    enum.addElement(call);
		}
	    } finally {
		stmt.close();
	    }
	} catch (SQLException e) {
	    throw new ApplicationError("CallNaiyouInfoManager.getCallNaiyou: SQLException",e);
	}
	
	return enum.elements();
    }
}
