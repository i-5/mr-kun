package jp.ne.sonet.medipro.mr.server.manager;

import java.sql.*;
import java.util.*;
import java.sql.Date;

import jp.ne.sonet.medipro.mr.common.exception.*;
import jp.ne.sonet.medipro.mr.common.util.*;
import jp.ne.sonet.medipro.mr.server.entity.*;


public class EnqueteManager {
	//ENQUETE_ID�擾�pSQL
	static final String ENQUETE_ID_GET_SQL
		= "SELECT"
		+ " url"
		+ ",status"
		+ " FROM enquete_id"
		+ " WHERE enq_id = ?";

	static final String ENQUETE_ID_MAX_SQL
		= "SELECT"
		+ " enq_id"
		+ ",url"
		+ ",status"
		+ " FROM enquete_id"
		+ " WHERE enq_id in (SELECT MAX(enq_id) FROM enquete_id)";

	//ENQUETE_SEND_LOG�}���pSQL
	static final String ENQUETE_SEND_LOG_INSERT_SQL
		= "INSERT INTO enquete_send_log ("
		+ " dr_id"
		+ ",enq_id"
		+ ",message_header_id"
		+ ",send_date"
		+ " ) VALUES ("
		+ " ?"
		+ ",?"
		+ ",?"
		+ ",sysdate"
		+ " )";

	//ENQUETE_SEND_LOG�ύX�pSQL
	static final String ENQUETE_SEND_LOG_UPDATE_SQL
		= "UPDATE enquete_send_log"
		+ " SET status = 1"
		+ ",reply_date = sysdate"
		+ " WHERE dr_id = ?"
		+ " AND enq_id = ?";

	//ENQUETE_SEND_LOG�擾�pSQL
	static final String ENQUETE_SEND_LOG_GET_SQL
		= "SELECT"
		+ " status"
		+ ",message_header_id"
		+ " FROM enquete_send_log"
		+ " WHERE dr_id = ?"
		+ " AND enq_id = ?";

	//ENQUETE_SEND_LOG�����Ă��Ȃ��A���P�[�g
	static final String ENQUETE_NOT_ANSWER
		= "SELECT"
		+ " MIN(enq_id) enq_id"
		+ ",status"
		+ " FROM enquete_send_log"
		+ " WHERE status IS NULL"
		+ " AND dr_id = ?";

	//ENQUETE_ANSWER�}���pSQL(����)
	static final String ENQUETE_ANSWER_INSERT_SQL
		= "INSERT INTO enquete_answer ("
		+ " user_id"
		+ ",enq_id"
		+ ",minor_id"
		+ ",field"
		+ ",answer"
		+ ",answer_date"
		+ ") VALUES ("
		+ " ?"
		+ ",?"
		+ ",?"
		+ ",?"
		+ ",?"
		+ ",sysdate"
		+ ")";

    //��t�A���P�[�g�|�C���g
    static final String DOCTOR_POINT_SQL
	= "UPDATE doctor SET "
	+ "point = point + (select to_number(naiyo1) "
	+                  "from constant_master "
	+                  "where constant_cd = 'ENQUETE_POINT')"
	+ " WHERE dr_id = ?";




    protected Connection connection = null;

    public EnqueteManager(Connection initConnection) {
	this.connection = initConnection;
    }

    /**
     *�@�A���P�[�g��URL����ё��M�ς݃X�e�[�^�X��DB���瓾��
     */
    public void getEnqueteIdTable( EnqueteInfo enqinfo ) throws MrException {
	PreparedStatement pstmt = null;
	try {
	    try {
		pstmt = connection.prepareStatement(ENQUETE_ID_GET_SQL);

		pstmt.setString(1, enqinfo.getEnqId());

		ResultSet rs = pstmt.executeQuery();

		if (rs.next()) {
		    enqinfo.setUrl( rs.getString("url") );
		    enqinfo.setEnqIdStatus( rs.getInt("status") );
		}
	    } finally {
		pstmt.close();
	    }

	} catch (SQLException ex) {
	    throw new MrException(ex);
	}
    }

    /**
     *�@�ŐV�A���P�[�g��ID,URL�����status��DB���瓾��
     */
    public void getEnqueteIdMaxTable( EnqueteInfo enqinfo ) throws MrException {
	Statement stmt = null;
	try {
	    try {
		stmt = connection.createStatement();

		ResultSet rs = stmt.executeQuery(ENQUETE_ID_MAX_SQL);

		if (rs.next()) {
		    enqinfo.setEnqId( rs.getString("enq_id") );
		    enqinfo.setUrl( rs.getString("url") );
		    enqinfo.setEnqIdStatus( rs.getInt("status") );
		}
	    } finally {
		stmt.close();
	    }

	} catch (SQLException ex) {
	    throw new MrException(ex);
	}
    }

    /**
     *�@Enquete Message ���M�L�^
     */
    public void setEnqueteSendLogTable( EnqueteInfo enqinfo ) throws MrException {
	PreparedStatement pstmt = null;

	try {
	    try {
		connection.setAutoCommit(false);
		pstmt = connection.prepareStatement(ENQUETE_SEND_LOG_INSERT_SQL);

		pstmt.setString(1, enqinfo.getUserId());
		pstmt.setString(2, enqinfo.getEnqId());
		pstmt.setString(3, enqinfo.getMessageHeaderId());

		pstmt.executeUpdate();
	    } finally {
		pstmt.close();
	    }
	    connection.commit();
	} catch (SQLException ex) {
	    try {
		connection.rollback();
	    } catch (SQLException e) { }
	    throw new MrException(ex);
	}
    }

    /**
     *�@Enquete ���񓚂�����Ԃɂ���
     */
    public void updateEnqueteSendLogTable( EnqueteInfo enqinfo ) throws MrException {
	PreparedStatement pstmt = null;

	try {
	    try {
		connection.setAutoCommit(false);
		pstmt = connection.prepareStatement(ENQUETE_SEND_LOG_UPDATE_SQL);

		pstmt.setString(1, enqinfo.getUserId());
		pstmt.setString(2, enqinfo.getEnqId());

		pstmt.executeUpdate();
	    } finally {
		pstmt.close();
	    }
	    connection.commit();
	} catch (SQLException ex) {
	    try {
		connection.rollback();
	    } catch (SQLException e) { }
	    throw new MrException(ex);
	}
    }

    /**
     *�@Enquete �̉�Status��DB���瓾��
     */
    public void getEnqueteReplyStatus( EnqueteInfo enqinfo ) throws MrException {
	PreparedStatement pstmt = null;

	try {
	    try {
		pstmt = connection.prepareStatement(ENQUETE_SEND_LOG_GET_SQL);

		pstmt.setString(1, enqinfo.getUserId());
		pstmt.setString(2, enqinfo.getEnqId());

		ResultSet rs = pstmt.executeQuery();

		if (rs.next()) {
		    enqinfo.setReplyStatus( rs.getInt("status") );
		    enqinfo.setMessageHeaderId( rs.getString("message_header_id") );
		}
	    } finally {
		pstmt.close();
	    }
	} catch (SQLException ex) {
	    throw new MrException(ex);
	}
    }

    /**
     *�@�����Ă��Ȃ��A���P�[�g��ID��DB���瓾��
     */
    public void getNotAnswerEnqueteID( EnqueteInfo enqinfo ) throws MrException {
	PreparedStatement pstmt = null;
	try {
	    try {
		pstmt = connection.prepareStatement(ENQUETE_NOT_ANSWER);

		pstmt.setString(1, enqinfo.getUserId());

		ResultSet rs = pstmt.executeQuery();

		if (rs.next()) {
		    String enq_id = rs.getString("enq_id");
		    if( enq_id != null ){
			enqinfo.setEnqId( enq_id );
		    }
		}
	    } finally {
		pstmt.close();
	    }

	} catch (SQLException ex) {
	    throw new MrException(ex);
	}
    }

    /**
     *�@Enquete �̉񓚂�DB�Ɋi�[����
     */

    public void setEnqueteAnswer( EnqueteInfo enqinfo ) throws MrException {
	PreparedStatement pstmt = null;

	Vector list = enqinfo.getEnqueteAnswer();
	Enumeration e = list.elements();

	try {
	    connection.setAutoCommit(false);
	    try {
		pstmt = connection.prepareStatement( ENQUETE_ANSWER_INSERT_SQL );

		while (e.hasMoreElements()) {
		    EnqueteAnswer info = (EnqueteAnswer)e.nextElement();
		    pstmt.setString(1, enqinfo.getUserId());
		    pstmt.setString(2, enqinfo.getEnqId());
		    pstmt.setString(3, info.MinorId);
		    pstmt.setString(4, info.Filed);
		    pstmt.setString(5, info.Answer);

		    pstmt.executeUpdate();
		}
	    } finally {
		pstmt.close();
	    }

	// �X�e�[�^�X���񓚂�����Ԃɂ���B
	    try {
		pstmt = connection.prepareStatement(ENQUETE_SEND_LOG_UPDATE_SQL);

		pstmt.setString(1, enqinfo.getUserId());
		pstmt.setString(2, enqinfo.getEnqId());

		pstmt.executeUpdate();
	    } finally {
		pstmt.close();
	    }
	// �A���P�[�g�񓚃|�C���g
	    try {
		pstmt = connection.prepareStatement(DOCTOR_POINT_SQL);

		pstmt.setString(1, enqinfo.getUserId());

		pstmt.executeUpdate();
	    } finally {
		pstmt.close();
	    }

	    connection.commit();
	} catch (SQLException ex) {
	    try {
		connection.rollback();
	    } catch (SQLException exc) { }
	    throw new MrException(ex);
	}
    }

}

