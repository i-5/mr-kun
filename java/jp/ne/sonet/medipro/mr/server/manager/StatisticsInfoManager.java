package jp.ne.sonet.medipro.mr.server.manager;

import java.util.*;
import java.sql.*;
import java.net.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
import jp.ne.sonet.medipro.mr.common.util.*; 
import jp.ne.sonet.medipro.mr.common.exception.*; 

/**
 * <h3>���v���͏��Ǘ�</h3>
 * 
 * <br>
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/21 �ߌ� 08:59:33)
 * @author: 
 */
public class StatisticsInfoManager {
    protected Connection conn;

    //�l�q��БS�l��
    protected static final String MR_TOTAL_COUNT_SQL
	= "SELECT count(mr_id) counter FROM mr "
	+ "WHERE SUBSTR(mr_id, 1, 3) = SUBSTR(?, 1, 3) AND delete_ymd is null";

    //���B�R�[�����i�S�Ёj
    protected static final String CO_CALL_COUNT_SQL
	= "SELECT ROUND(count(totatsu_call_time)/?, 0) counter FROM totatsu_call_log "
	+ "WHERE SUBSTR(from_userid, 1, 3) = SUBSTR(?, 1, 3) "
	+ "AND totatsu_call_time >= TO_DATE( ?, 'YYYY/MM/DD') "; 

    //���B�R�[�����i�l�q�j
    protected static final String MR_CALL_COUNT_SQL
	= "SELECT count(totatsu_call_time) counter FROM totatsu_call_log "
	+ "WHERE from_userid = ? "
	+ "AND totatsu_call_time >= TO_DATE( ?, 'YYYY/MM/DD') ";

    //�o�^�ڋq���i�S�Ёj�m �I��o�^ + �I��o�^�����i�o�^�ȊO�j �n
    protected static final String CO_INPUTCUST_COUNT_SQL
	= "SELECT ROUND(count(dr_id)/?, 0) counter FROM sentaku_toroku "
	+ "WHERE SUBSTR(mr_id, 1, 3) = SUBSTR(?, 1, 3) ";
    protected static final String CO_INPUTCUST_HIST_COUNT_SQL
//	= "SELECT ROUND(count(DISTINCT dr_id)/?, 0) counter FROM sentaku_toroku_hist "
	= "SELECT ROUND(count(DISTINCT RPAD(dr_id,30)||RPAD(mr_id,10))/?, 0) counter FROM sentaku_toroku_hist "
	+ "WHERE SUBSTR(mr_id, 1, 3) = SUBSTR(?, 1, 3) "
	+ "AND (dr_id, mr_id) NOT IN( SELECT dr_id, mr_id FROM sentaku_toroku "
	+ "WHERE SUBSTR(mr_id, 1, 3) = SUBSTR(?, 1, 3)) "
	+ "AND end_ymd >= TO_DATE( ?, 'YYYY/MM/DD') ";

    //�o�^�ڋq���i�l�q�j�m �I��o�^ + �I��o�^�����i�o�^�ȊO�j �n
    protected static final String MR_INPUTCUST_COUNT_SQL
	= "SELECT count(dr_id) counter FROM sentaku_toroku "
	+ "WHERE mr_id = ? ";
    protected static final String MR_INPUTCUST_HIST_COUNT_SQL
	= "SELECT count(DISTINCT dr_id) counter FROM sentaku_toroku_hist "
	+ "WHERE mr_id = ? "
	+ "AND dr_id NOT IN( SELECT dr_id FROM sentaku_toroku WHERE mr_id = ?) "
	+ "AND end_ymd >= TO_DATE( ?, 'YYYY/MM/DD') ";

    //���M���i�S�Ёj(�o�^�ڋq�����[���̎��A�[�����Z�ɒ��ӁI)
    protected static final String CO_SENDMSG_COUNT_SQL
//	= "SELECT ROUND(count(message_header_id)/? /? /?, 1) counter, "
	= "SELECT ROUND(count(message_header_id)/? /? , 1) counter, "
	+ "count(message_header_id) total FROM message_header "
	+ "WHERE SUBSTR(from_userid, 1, 3) = SUBSTR(?, 1, 3) "
	//+ "AND message_kbn <> '4'"
//	+ "AND message_kbn < '4'"//1110 y-yamada add NO.47 �ړ����b�Z�[�W��4,5
	+ "AND message_kbn = '1'"//1218 y-yamada add MR�̑��M����
	+ "AND receive_time >= TO_DATE( ?, 'YYYY/MM/DD') ";

    //���M���i�l�q�j(�o�^�ڋq�����[���̎��A�[�����Z�ɒ��ӁI)
    protected static final String MR_SENDMSG_COUNT_SQL
	= "SELECT ROUND(count(message_header_id)/?, 1) counter, "
	+ "count(message_header_id) total FROM message_header "
	+ "WHERE from_userid = ? "
	//+ "AND message_kbn <> '4'"
//	+ "AND message_kbn < '4'"//1110 y-yamada add NO.47 �ړ����b�Z�[�W��4,5
	+ "AND message_kbn = '1'"//1218 y-yamada add MR�̑��M����
	+ "AND receive_time >= TO_DATE( ?, 'YYYY/MM/DD') ";

    protected String sDate180;
    protected String sDate30;
    protected int coTotalCount180 = 0;
    protected int coTotalCount30 = 0;
    protected int mrTotalCount180 = 0;
    protected int mrTotalCount30 = 0;
	
    /**
     * StatisticsInfoManager �R���X�g���N�^�[�E�R�����g�B
     */
    public StatisticsInfoManager(Connection conn) {
	this.conn = conn;
    }

    /**
     * <h3>���v���͑S�̏��̎擾�i���B�R�[�����j</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/07/04 �ߌ� 07:30:54)
     * @param statisticsinfo jp.ne.sonet.medipro.mr.server.entity.StatisticsInfo
     * @param mrID java.lang.String
     * @param mrTotalCount int
     */
    private void getCallCount(StatisticsInfo statisticsinfo,
			      String mrID,
			      int mrTotalCount) {
	ResultSet rs;
	PreparedStatement pstmt;
	String sqltxt;
		
	try {
	    //���B�R�[�����i�S�Ёj
	    sqltxt = CO_CALL_COUNT_SQL;			
	    // SQL ����ݒ�
	    pstmt = conn.prepareStatement(sqltxt);
	    // �p�����[�^��ݒ�
	    pstmt.setInt(1, mrTotalCount);
	    pstmt.setString(2, mrID);
	    pstmt.setString(3, sDate180);
	    // SQL ���s
	    rs   = pstmt.executeQuery();
	    try {
		while ( rs.next() ) {
		    statisticsinfo.setCoCallCount180(rs.getInt("counter"));
//  System.out.println("���B�R�[�����i�S�Ёj180:" + rs.getInt("counter"));
		}
	    } finally {
		pstmt.close();
	    }
	    // SQL ����ݒ�
	    pstmt = conn.prepareStatement(sqltxt);
	    // �p�����[�^��ݒ�
	    pstmt.setInt(1, mrTotalCount);
	    pstmt.setString(2, mrID);
	    pstmt.setString(3, sDate30);
	    // SQL ���s
	    rs   = pstmt.executeQuery();
	    try {
		while ( rs.next() ) {
		    statisticsinfo.setCoCallCount30(rs.getInt("counter"));
//  System.out.println("���B�R�[�����i�S�Ёj30:" + rs.getInt("counter"));
		}
	    } finally {
		pstmt.close();
	    }

	    //���B�R�[�����i�l�q�j
	    sqltxt = MR_CALL_COUNT_SQL;			
	    // SQL ����ݒ�
	    pstmt = conn.prepareStatement(sqltxt);
	    // �p�����[�^��ݒ�
	    pstmt.setString(1, mrID);
	    pstmt.setString(2, sDate180);
	    // SQL ���s
	    rs   = pstmt.executeQuery();
	    try {
		while ( rs.next() ) {
		    statisticsinfo.setMrCallCount180(rs.getInt("counter"));
		}
	    } finally {
		pstmt.close();
	    }
	    // SQL ����ݒ�
	    pstmt = conn.prepareStatement(sqltxt);
	    // �p�����[�^��ݒ�
	    pstmt.setString(1, mrID);
	    pstmt.setString(2, sDate30);
	    // SQL ���s
	    rs   = pstmt.executeQuery();
	    try {
		while ( rs.next() ) {
		    statisticsinfo.setMrCallCount30(rs.getInt("counter"));
		}
	    } finally {
		pstmt.close();
	    }

	} catch (SQLException e) {
	    throw new MrException(e);
	}
    }

    /**
     * <h3>���v���͑S�̏��̎擾�i�o�^�ڋq���j</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/07/04 �ߌ� 07:30:54)
     * @param statisticsinfo jp.ne.sonet.medipro.mr.server.entity.StatisticsInfo
     * @param mrID java.lang.String
     * @param mrTotalCount int
     */
    private void getInputCust(StatisticsInfo statisticsinfo,
			      String mrID,
			      int mrTotalCount) {

	ResultSet rs;
	PreparedStatement pstmt;
	String sqltxt;
		
	try {
	    ////�o�^�ڋq���i�S�Ёj
	    //�I��o�^�r�p�k
	    sqltxt = CO_INPUTCUST_COUNT_SQL;			
	    // SQL ����ݒ�
	    pstmt = conn.prepareStatement(sqltxt);
	    // �p�����[�^��ݒ�
	    pstmt.setInt(1, mrTotalCount);
	    pstmt.setString(2, mrID);
	    // SQL ���s
	    rs   = pstmt.executeQuery();
	    int custcount = 0;
	    try {
		while ( rs.next() ) {
		    custcount = rs.getInt("counter");
//  System.out.println("�o�^�ڋq���i�S�Ёj:" + rs.getInt("counter"));
		}
	    } finally {
		pstmt.close();
	    }
		
	    //�I��o�^�����r�p�k
	    sqltxt = CO_INPUTCUST_HIST_COUNT_SQL;
	    // SQL ����ݒ�
	    pstmt = conn.prepareStatement(sqltxt);
	    // �p�����[�^��ݒ�
	    pstmt.setInt(1, mrTotalCount);
	    pstmt.setString(2, mrID);
	    pstmt.setString(3, mrID);
	    pstmt.setString(4, sDate180);
	    // SQL ���s
	    rs   = pstmt.executeQuery();
	    try {
		while ( rs.next() ) {
		    statisticsinfo.setCoInsertCustCount180(rs.getInt("counter") + custcount);
		}
	    } finally {
		pstmt.close();
	    }
	    // SQL ����ݒ�
	    pstmt = conn.prepareStatement(sqltxt);
	    // �p�����[�^��ݒ�
	    pstmt.setInt(1, mrTotalCount);
	    pstmt.setString(2, mrID);
	    pstmt.setString(3, mrID);
	    pstmt.setString(4, sDate30);
	    // SQL ���s
	    rs   = pstmt.executeQuery();
	    try {
		while ( rs.next() ) {
		    statisticsinfo.setCoInsertCustCount30(rs.getInt("counter") + custcount);
		}
	    } finally {
		pstmt.close();
	    }

	    //�o�^�ڋq���i�l�q�j
	    //�I��o�^�r�p�k
	    sqltxt = MR_INPUTCUST_COUNT_SQL;			
	    // SQL ����ݒ�
	    pstmt = conn.prepareStatement(sqltxt);
	    // �p�����[�^��ݒ�
	    pstmt.setString(1, mrID);
	    // SQL ���s
	    rs   = pstmt.executeQuery();
	    custcount = 0;
	    try {
		while ( rs.next() ) {
		    custcount = rs.getInt("counter");
//  System.out.println("�o�^�ڋq���iMR�j:" + rs.getInt("counter"));
		}
	    } finally {
		pstmt.close();
	    }
		
	    //�I��o�^�����r�p�k
	    sqltxt = MR_INPUTCUST_HIST_COUNT_SQL;
	    // SQL ����ݒ�
	    pstmt = conn.prepareStatement(sqltxt);
	    // �p�����[�^��ݒ�
	    pstmt.setString(1, mrID);
	    pstmt.setString(2, mrID);
	    pstmt.setString(3, sDate180);
	    // SQL ���s
	    rs   = pstmt.executeQuery();
	    try {
		while ( rs.next() ) {
		    statisticsinfo.setMrInsertCustCount180(rs.getInt("counter") + custcount);
		}
	    }
	    finally {
		pstmt.close();
	    }
	    // SQL ����ݒ�
	    pstmt = conn.prepareStatement(sqltxt);
	    // �p�����[�^��ݒ�
	    pstmt.setString(1, mrID);
	    pstmt.setString(2, mrID);
	    pstmt.setString(3, sDate30);
	    // SQL ���s
	    rs   = pstmt.executeQuery();
	    try {
		while ( rs.next() ) {
		    statisticsinfo.setMrInsertCustCount30(rs.getInt("counter") + custcount);
		}
	    } finally {
		pstmt.close();
	    }

	} catch (SQLException e) {
	    throw new MrException(e);
	}
    }

    /**
     * <h3>���v���͑S�̏��̎擾�i���M���j</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/07/04 �ߌ� 07:30:54)
     * @param statisticsinfo jp.ne.sonet.medipro.mr.server.entity.StatisticsInfo
     * @param mrID java.lang.String
     * @param mrTotalCount int
     */
    private void getSendCount(StatisticsInfo statisticsinfo,
			      String mrID,
			      int mrTotalCount) {
	ResultSet rs;
	PreparedStatement pstmt;
	String sqltxt;
		
	try {
	    //���M���i�S�Ёj
	    sqltxt = CO_SENDMSG_COUNT_SQL;			
	    // SQL ����ݒ�
	    pstmt = conn.prepareStatement(sqltxt);
	    // �p�����[�^��ݒ�
	    if ( statisticsinfo.getCoInsertCustCount180() == 0 ) {
		pstmt.setInt(1, 1);
	    }
	    else {
		pstmt.setInt(1, statisticsinfo.getCoInsertCustCount180());
	    }
	    pstmt.setInt(2, mrTotalCount);
	    pstmt.setString(3, mrID);//1218 y-yamada add
	    pstmt.setString(4, sDate180);//1218 y-yamada add
//	    pstmt.setInt(3, mrTotalCount);//1218 y-yamada del
//	    pstmt.setString(4, mrID);//1218 y-yamada del
//	    pstmt.setString(5, sDate180);//1218 y-yamada del
	    // SQL ���s
	    rs   = pstmt.executeQuery();
	    try {
		while ( rs.next() ) {
		    statisticsinfo.setCoSendCount180(rs.getFloat("counter"));
		    coTotalCount180 = rs.getInt("total");
//  System.out.println("���M���i�S�Ёj180:" + rs.getInt("counter"));
		}
	    } finally {
		pstmt.close();
	    }
	    // SQL ����ݒ�
	    pstmt = conn.prepareStatement(sqltxt);
	    // �p�����[�^��ݒ�
	    if ( statisticsinfo.getCoInsertCustCount30() == 0 ) {
		pstmt.setInt(1, 1);
	    } else {
		pstmt.setInt(1, statisticsinfo.getCoInsertCustCount30());
	    }
	    pstmt.setInt(2, mrTotalCount);
	    pstmt.setString(3, mrID);//1218 y-yamada add
	    pstmt.setString(4, sDate30);//1218 y-yamada add
//	    pstmt.setInt(3, mrTotalCount);//1218 y-yamada del
//	    pstmt.setString(4, mrID);//1218 y-yamada del
//	    pstmt.setString(5, sDate30);//1218 y-yamada del
	    // SQL ���s
	    rs   = pstmt.executeQuery();
	    try {
		while ( rs.next() ) {
		    statisticsinfo.setCoSendCount30(rs.getFloat("counter"));
		    coTotalCount30 = rs.getInt("total");
//  System.out.println("���M���i�S�Ёj30:" + rs.getInt("total"));
		}
	    } finally {
		pstmt.close();
	    }

	    //���M���i�l�q�j
	    sqltxt = MR_SENDMSG_COUNT_SQL;			
	    // SQL ����ݒ�
	    pstmt = conn.prepareStatement(sqltxt);
	    // �p�����[�^��ݒ�
	    if ( statisticsinfo.getMrInsertCustCount180() == 0 ) {
		pstmt.setInt(1, 1);
	    } else {
		pstmt.setInt(1, statisticsinfo.getMrInsertCustCount180());
	    }
	    pstmt.setString(2, mrID);
	    pstmt.setString(3, sDate180);
	    // SQL ���s
	    rs   = pstmt.executeQuery();
	    try {
		while ( rs.next() ) {
		    statisticsinfo.setMrSendCount180(rs.getFloat("counter"));
		    mrTotalCount180 = rs.getInt("total");
//  System.out.println("���M���iMR�j180:" + rs.getInt("total"));
		}
	    } finally {
		pstmt.close();
	    }
	    // SQL ����ݒ�
	    pstmt = conn.prepareStatement(sqltxt);
	    // �p�����[�^��ݒ�
	    if ( statisticsinfo.getMrInsertCustCount30() == 0 ) {
		pstmt.setInt(1, 1);
	    } else {
		pstmt.setInt(1, statisticsinfo.getMrInsertCustCount30());
	    }
	    pstmt.setString(2, mrID);
	    pstmt.setString(3, sDate30);
	    // SQL ���s
	    rs   = pstmt.executeQuery();
	    try {
		while ( rs.next() ) {
		    statisticsinfo.setMrSendCount30(rs.getFloat("counter"));
		    mrTotalCount30 = rs.getInt("total");
		}
	    } finally {
		pstmt.close();
	    }

	} catch (SQLException e) {
	    throw new MrException(e);
	}
    }

    /**
     * <h3>���v���͑S�̏��̎擾</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/22 �ߌ� 12:23:08)
     * @return jp.ne.sonet.medipro.mr.server.entity.StatisticsInfo
     * @param companyCD java.lang.String
     */
    public StatisticsInfo getStatisticsInfo(String mrID) {

	//���t�̎擾�i30���O�A180���O�j
	java.util.Date today = new java.util.Date();
	DateUtil dateutil = new DateUtil();
	java.util.Date date30 = dateutil.addDays(today,-30);
	java.util.Date date180 = dateutil.addDays(today,-180);
	sDate30 = dateutil.toStrDate(dateutil.toStr(date30));
	sDate180 = dateutil.toStrDate(dateutil.toStr(date180));
	
	StatisticsInfo statisticsinfo = new StatisticsInfo();
	
	MrInfoManager mrinfomanager = new MrInfoManager(conn);
	statisticsinfo.setMrinfo(mrinfomanager.getMrInfo(mrID));

	getStatisticsInfoSub(statisticsinfo, mrID);
	
	return statisticsinfo;
	
    }

    /**
     * <h3>���v���͑S�̏��̎擾�i�⏕�j</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/22 �ߌ� 12:28:09)
     * @param mrID java.lang.String
     * @param statisticsinfo jp.ne.sonet.medipro.mr.server.entity.StatisticsInfo
     */
    private void getStatisticsInfoSub(StatisticsInfo statisticsinfo, String mrID) {
	ResultSet rs;
	PreparedStatement pstmt;
	String sqltxt;

	try {	
		
	    //�l�q��БS�l��
	    sqltxt = MR_TOTAL_COUNT_SQL;
	    // SQL ����ݒ�
	    pstmt = conn.prepareStatement(sqltxt);
	    // �p�����[�^��ݒ�
	    pstmt.setString(1, mrID);
	    // SQL ���s
	    rs   = pstmt.executeQuery();
	    int mrTotalCount = 0;
	    try {
		while ( rs.next() ) {
		    mrTotalCount = rs.getInt("counter");
//  System.out.println("MR�l��:" + rs.getInt("counter"));
		}
	    } finally {
		pstmt.close();
	    }

	    getCallCount(statisticsinfo, mrID, mrTotalCount);
	    getInputCust(statisticsinfo, mrID, mrTotalCount);
	    getSendCount(statisticsinfo, mrID, mrTotalCount);

      // 2000/10/02 ����i���M���j��0�̏ꍇ�ɃN���b�N����0�Ƃ���
      // ���M����Ă��Ȃ������[�J�[�̃f�t�H���g�\�����N���b�N���Ă�������ꍇ
//      if ( coTotalCount180 == 0 ) {
      if (( statisticsinfo.getCoSendCount180() * (float)statisticsinfo.getCoInsertCustCount180()) == 0 ) {
	     statisticsinfo.setCoClickRate180(0);
      } else {
//	     statisticsinfo.setCoClickRate180((int)((float)statisticsinfo.getCoCallCount180() / coTotalCount180 * 100));
//	     statisticsinfo.setCoClickRate180((int)(((float)statisticsinfo.getCoCallCount180() / (coTotalCount180 / mrTotalCount) )* 100));//1218
	     statisticsinfo.setCoClickRate180((int)(((float)statisticsinfo.getCoCallCount180()/ (statisticsinfo.getCoSendCount180() * (float)statisticsinfo.getCoInsertCustCount180()) * 100)+0.5));//1221

      }
//      if ( coTotalCount30 == 0 ) {
      if ( (statisticsinfo.getCoSendCount30() * (float)statisticsinfo.getCoInsertCustCount30())  == 0 ) {
	     statisticsinfo.setCoClickRate30(0);
      } else {
//	     statisticsinfo.setCoClickRate30((int)((float)statisticsinfo.getCoCallCount30() / coTotalCount30 * 100));
//	     statisticsinfo.setCoClickRate30((int)(((float)statisticsinfo.getCoCallCount30() / (coTotalCount30 / mrTotalCount)) * 100));//1218
	     statisticsinfo.setCoClickRate30((int)(((float)statisticsinfo.getCoCallCount30()/ (statisticsinfo.getCoSendCount30() * (float)statisticsinfo.getCoInsertCustCount30()) * 100)+0.5));//1221
      }
//	    if ( mrTotalCount180 == 0 ) {
	    if ( (statisticsinfo.getMrSendCount180() * (float)statisticsinfo.getMrInsertCustCount180()) == 0 ) {
       statisticsinfo.setMrClickRate180(0);
      } else {
//	     statisticsinfo.setMrClickRate180((int)((float)statisticsinfo.getMrCallCount180() / mrTotalCount180 * 100));
	     statisticsinfo.setMrClickRate180((int)(((float)statisticsinfo.getMrCallCount180()/ (statisticsinfo.getMrSendCount180() * (float)statisticsinfo.getMrInsertCustCount180()) * 100)+0.5));//1221
      }
//      if ( mrTotalCount30 == 0 ) {
      if ( (statisticsinfo.getMrSendCount30() * (float)statisticsinfo.getMrInsertCustCount30()) == 0 ) {
	     statisticsinfo.setMrClickRate30(0);
      } else {
//	     statisticsinfo.setMrClickRate30((int)((float)statisticsinfo.getMrCallCount30() / mrTotalCount30 * 100));
	     statisticsinfo.setMrClickRate30((int)(((float)statisticsinfo.getMrCallCount30()/ (statisticsinfo.getMrSendCount30() * (float)statisticsinfo.getMrInsertCustCount30()) * 100)+0.5));//1221
      }
				
//  System.out.println(
//  	"CoCallCnt180/CoTotalCount180 = "
//  	+ statisticsinfo.getCoCallCount180() + "/" + coTotalCount180);
//  System.out.println(
//  	"CoCallCnt30/CoTotalCount30 = "
//  	+ statisticsinfo.getCoCallCount30() + "/" + coTotalCount30);
//  System.out.println(
//  	"MrCallCnt180/MrTotalCount180 = "
//  	+ statisticsinfo.getMrCallCount180() + "/" + mrTotalCount180);
//  System.out.println(
//  	"MrCallCnt30/MrTotalCount30 = "
//  	+ statisticsinfo.getMrCallCount30() + "/" + mrTotalCount30);
//  System.out.println("------------------------");

	} catch (SQLException e) {
	    throw new MrException(e);
	}
    }
}
