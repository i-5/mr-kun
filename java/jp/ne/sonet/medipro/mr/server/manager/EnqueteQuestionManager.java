package jp.ne.sonet.medipro.mr.server.manager;

import java.sql.*;
import java.util.*;
import java.sql.Date;

import jp.ne.sonet.medipro.mr.common.exception.*;
import jp.ne.sonet.medipro.mr.common.util.*;
import jp.ne.sonet.medipro.mr.server.entity.*;


public class EnqueteQuestionManager {

	//ENQUETE_QUESTION取得用SQL
	static final String ENQUETE_QUESTION_GET_SQL
		= "SELECT question_name"
		+ " FROM enquete_question"
		+ " WHERE enq_id = ?"
		+ " AND group_id = ?"
		+ " AND minor_id = ?";

    protected Connection connection = null;

    public EnqueteQuestionManager(Connection initConnection) {
	this.connection = initConnection;
    }

    /**
     *　問題の選択肢を取得します。
     */
    public void getQuestion(EnqueteQuestionInfo info) throws MrException {
	PreparedStatement pstmt = null;

	try {
	    try {
		pstmt = connection.prepareStatement(ENQUETE_QUESTION_GET_SQL);

		pstmt.setString(1, info.getEnqId());
		pstmt.setString(2, info.getGroupId());
		pstmt.setString(3, info.getMinorId());

		ResultSet rs = pstmt.executeQuery();

		while (rs.next()) {
		    info.addQuestion(rs.getString("question_name"));
		}
	    } finally {
		pstmt.close();
	    }
	} catch (SQLException ex) {
	    throw new MrException(ex);
	}

    }

}
