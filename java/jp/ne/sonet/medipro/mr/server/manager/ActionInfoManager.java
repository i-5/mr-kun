package jp.ne.sonet.medipro.mr.server.manager;

import java.sql.*;
import java.util.*;

import jp.ne.sonet.medipro.mr.server.entity.*;
import jp.ne.sonet.medipro.mr.common.exception.*;

public class ActionInfoManager {
    /** ��Ўw��^�[�Q�b�g�����N�ꗗ�擾 */
    protected static final String ACTION_LIST_SQL
	= "SELECT "
	+ " target_rank"
	+ ",threshold"
	+ ",message1"
	+ ",message2"
	+ ",message3"
	+ ",message4"
	+ ",target_name"//1020 y-yamada add
	+ " FROM action, mr "
	+ " WHERE "
	+ " mr.mr_id = ?"
	+ " AND mr.company_cd = action.company_cd"
	+ " order by target_name";//1020 y-yamada add�^�[�Q�b�g�l�[���Ń\�[�g

    
    protected Connection connection = null;

    public ActionInfoManager(Connection initConnection) {
	connection = initConnection;
    }

    /**
     * �w�肵����Ђ̃^�[�Q�b�g�����N���̃A�N�V�������ꗗ���擾����.
     * @param  companyCd ��ЃR�[�h
     * @return �A�N�V�������ꗗ
     */
    public Vector getActionList(String mrId) {
	Vector list = new Vector();

	try {
	    PreparedStatement pstmt = connection.prepareStatement(ACTION_LIST_SQL);
	    pstmt.setString(1, mrId);

	    ResultSet rs = pstmt.executeQuery();

	    while (rs.next()) {
		ActionInfo info = new ActionInfo();
		info.setTargetRank(rs.getString("target_rank"));
		info.setThreshold(rs.getInt("threshold"));
		info.setMessage1(rs.getString("message1"));
		info.setMessage2(rs.getString("message2"));
		info.setMessage3(rs.getString("message3"));
		info.setMessage4(rs.getString("message4"));
		info.setTargetName(rs.getString("target_name"));//1020 y-yamada add

		list.addElement(info);
	    }
	    
	    pstmt.close();
	} catch (SQLException e) {
	    throw new MrException(e);
	}

	return list;
    }
}
