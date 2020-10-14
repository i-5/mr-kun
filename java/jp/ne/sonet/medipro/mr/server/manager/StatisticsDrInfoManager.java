package jp.ne.sonet.medipro.mr.server.manager;

import java.util.*;
import java.sql.*;
import java.net.*;
import com.objectspace.jgl.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
import jp.ne.sonet.medipro.mr.common.*;
import jp.ne.sonet.medipro.mr.common.util.*;
import jp.ne.sonet.medipro.mr.common.exception.*;
import jp.ne.sonet.medipro.mr.common.predicate.*; 

/**
 * <h3>���v���͌ڋq�ʏ��Ǘ�</h3>
 * 
 * <br>
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/21 �ߌ� 08:58:54)
 * @author: 
 */
public class StatisticsDrInfoManager {
    protected Connection conn;
	
    //�I��o�^
    protected static final String SENTAKU_TOROKU_SQL
	//= "SELECT sen.dr_id sen_di_id, sen.name doc_name, doc.name_kana doc_name_kana, sen.target_rank sen_target_rank "
	= "SELECT sen.dr_id sen_di_id, sen.name doc_name, doc.name_kana doc_name_kana, sen.target_rank sen_target_rank, act.target_name act_target_name "
	//+ "FROM sentaku_toroku sen, doctor doc "
	+ "FROM sentaku_toroku sen, doctor doc, action act "
	//+ "WHERE mr_id = ? AND sen.dr_id = doc.dr_id ";
//	+ "WHERE mr_id = ? AND sen.dr_id = doc.dr_id AND sen.target_rank = act.target_rank";//1025 y-yamada add 
	+ "WHERE mr_id = ? AND sen.dr_id = doc.dr_id AND sen.target_rank = act.target_rank AND act.company_cd = ?";//1106 y-yamada add 

    //�I��o�^����
    protected static final String SENTAKU_TOROKU_HIST_SQL
	= "SELECT DISTINCT sen.dr_id sen_di_id, sen.name doc_name, doc.name_kana doc_name_kana, sen.target_rank sen_target_rank "
	+ "FROM sentaku_toroku_hist sen, doctor doc "
	+ "WHERE mr_id = ? "
	+ "AND sen.dr_id = doc.dr_id "
	+ "AND sen.dr_id NOT IN( SELECT dr_id FROM sentaku_toroku WHERE mr_id = ?) ";

    //���M��
    protected static final String SENDMSG_COUNT_SQL
	= "SELECT count(message_header_id) counter FROM message_header "
	+ "WHERE from_userid = ? "
	+ "AND to_userid = ? "
	//+ "AND message_kbn <> '4' "
	+ "AND message_kbn < '4' "//1110 y-yamada add NO.47 �ړ����b�Z�[�W��4,5
	+ "AND receive_time >= TO_DATE( ?, 'YYYY/MM/DD') ";

    //���B�R�[����
    protected static final String CALL_COUNT_SQL
	= "SELECT count(totatsu_call_time) counter "
	+ "FROM totatsu_call_log "
	+ "WHERE from_userid = ? "
	+ "AND to_userid = ? "
	+ "AND totatsu_call_time >= TO_DATE( ?, 'YYYY/MM/DD') ";

    protected String sDate180;
    protected String sDate30;
	
    /**
     * StatisticsDrInfoManager �R���X�g���N�^�[�E�R�����g�B
     */
    public StatisticsDrInfoManager(Connection conn) {
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
    private void getCallCount(StatisticsDrInfo statisticsdrinfo, String mrID) {

	ResultSet rs;
	PreparedStatement pstmt;
	String sqltxt;
		
	try {
	    //���B�R�[����
	    sqltxt = CALL_COUNT_SQL;
	    // SQL ����ݒ�
	    pstmt = conn.prepareStatement(sqltxt);
	    // �p�����[�^��ݒ�
	    pstmt.setString(1, mrID);
	    pstmt.setString(2, statisticsdrinfo.getDrID());
	    pstmt.setString(3, sDate180);
	    // SQL ���s
	    rs   = pstmt.executeQuery();
	    try {
		while ( rs.next() ) {
		    statisticsdrinfo.setClickCount180(rs.getInt("counter"));
		}
	    } finally {
		pstmt.close();
	    }
	    // SQL ����ݒ�
	    pstmt = conn.prepareStatement(sqltxt);
	    // �p�����[�^��ݒ�
	    pstmt.setString(1, mrID);
	    pstmt.setString(2, statisticsdrinfo.getDrID());
	    pstmt.setString(3, sDate180);
	    // SQL ���s
	    rs   = pstmt.executeQuery();
	    try {
		while ( rs.next() ) {
		    statisticsdrinfo.setClickCount30(rs.getInt("counter"));
		}
	    } finally {
		pstmt.close();
	    }
	} catch (SQLException e) {
	    throw new MrException(e);
	}

    }

    /**
     * <h3>���v���͌ڋq���̎擾</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/22 �ߌ� 12:18:43)
     * @return java.util.Enumeration (StatisticsDrInfo)
     * @param mrID java.lang.String
     * @param sortKey java.lang.String
     * @param rowType java.lang.String
     */
//   public Enumeration getSatisticsDrInfo(String mrID, String sortKey, String rowType) {
    public Enumeration getSatisticsDrInfo(String mrID, String sortKey, String rowType ,String companyCD) {//1106 y-yamada add
	ResultSet rs;
	PreparedStatement pstmt;
	String sqltxt;
	TantoInfo taninfo;
	OrderedSet statisticsdrinfoList = new OrderedSet();
	BinaryPredicate pred;

	//���t�̎擾�i30���O�A180���O�j
	java.util.Date today = new java.util.Date();
	DateUtil dateutil = new DateUtil();
	java.util.Date date30 = dateutil.addDays(today,-30);
	java.util.Date date180 = dateutil.addDays(today,-180);
	sDate30 = dateutil.toStrDate(dateutil.toStr(date30));
	sDate180 = dateutil.toStrDate(dateutil.toStr(date180));
	
	pred = getSortPredicate(sortKey, rowType);
	statisticsdrinfoList = new OrderedSet(pred,true);
	
	try {
	    //�I��o�^
	    //�r�p�k��
	    sqltxt = SENTAKU_TOROKU_SQL;
		
	    // SQL ����ݒ�
	    pstmt = conn.prepareStatement(sqltxt);
	    try {
		// �p�����[�^��ݒ�
		pstmt.setString(1, mrID);
		pstmt.setString(2, companyCD);//1106 y-yamada add
		// SQL ���s
		rs   = pstmt.executeQuery();
		while ( rs.next() ) {

		    StatisticsDrInfo statisticsdrinfo = new StatisticsDrInfo();
		    statisticsdrinfo.setDrID(rs.getString("sen_di_id"));
		    statisticsdrinfo.setDrName(rs.getString("doc_name"));
		    statisticsdrinfo.setDrNameKana(rs.getString("doc_name_kana"));
		    statisticsdrinfo.setTargetRank(rs.getString("sen_target_rank"));
		    statisticsdrinfo.setTargetName(rs.getString("act_target_name"));//1025 y-yamada add
				
		    getCallCount(statisticsdrinfo, mrID);
		    getSendCount(statisticsdrinfo, mrID);

      // 2000/10/04 ����i���M���j��0�̏ꍇ�ɃN���b�N����0�Ƃ���
      // ���M����Ă��Ȃ������[�J�[�̃f�t�H���g�\�����N���b�N���Ă�������ꍇ
      if( statisticsdrinfo.getSendCount180() == 0 ) {
		      statisticsdrinfo.setClickRate180(0);
      } else {
		      statisticsdrinfo.setClickRate180((int)((float)statisticsdrinfo.getClickCount180() / statisticsdrinfo.getSendCount180() * 100));
      }
      if( statisticsdrinfo.getSendCount30() == 0 ) {
		      statisticsdrinfo.setClickRate30(0);
      } else {
		      statisticsdrinfo.setClickRate30((int)((float)statisticsdrinfo.getClickCount30() / statisticsdrinfo.getSendCount30() * 100));
      }
				
		    statisticsdrinfoList.add(statisticsdrinfo);
		}
	    } finally {
		//rs.close();
		pstmt.close();
	    }

	    //�I��o�^����
	    //�r�p�k��
	    //  		sqltxt = SENTAKU_TOROKU_HIST_SQL;
		
	    //  		// SQL ����ݒ�
	    //  		pstmt = conn.prepareStatement(sqltxt);
	    //  		try {
	    //  			// �p�����[�^��ݒ�
	    //  			pstmt.setString(1, mrID);
	    //  			pstmt.setString(2, mrID);
	    //  			// SQL ���s
	    //  			rs   = pstmt.executeQuery();
	    //  			while ( rs.next() ) {

	    //  				StatisticsDrInfo statisticsdrinfo = new StatisticsDrInfo();
	    //  				statisticsdrinfo.setDrID(rs.getString("sen_di_id"));
	    //  				statisticsdrinfo.setDrName(rs.getString("doc_name"));
	    //  				statisticsdrinfo.setDrNameKana(rs.getString("doc_name_kana"));
	    //  				statisticsdrinfo.setTargetRank(rs.getString("sen_target_rank"));
				
	    //  				getCallCount(statisticsdrinfo, mrID);
	    //  				getSendCount(statisticsdrinfo, mrID);

	    //  				statisticsdrinfo.setClickRate180((int)((float)statisticsdrinfo.getClickCount180() / statisticsdrinfo.getSendCount180() * 100));
	    //  				statisticsdrinfo.setClickRate30((int)((float)statisticsdrinfo.getClickCount30() / statisticsdrinfo.getSendCount30() * 100));
				
	    //  				statisticsdrinfoList.add(statisticsdrinfo);
	    //  			}
	    //  		}
	    //  		finally {
	    //  			//rs.close();
	    //  			pstmt.close();
	    //  		}

	} catch (SQLException e) {
	    throw new MrException(e);
	}
	return statisticsdrinfoList.elements();
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
    private void getSendCount(StatisticsDrInfo statisticsdrinfo, String mrID) {

	ResultSet rs;
	PreparedStatement pstmt;
	String sqltxt;
		
	try {
	    //���M��
	    sqltxt = SENDMSG_COUNT_SQL;			
	    // SQL ����ݒ�
	    pstmt = conn.prepareStatement(sqltxt);
	    // �p�����[�^��ݒ�
	    pstmt.setString(1, mrID);
	    pstmt.setString(2, statisticsdrinfo.getDrID());
	    pstmt.setString(3, sDate180);
	    // SQL ���s
	    rs   = pstmt.executeQuery();
	    try {
		while ( rs.next() ) {
		    statisticsdrinfo.setSendCount180(rs.getInt("counter"));
		}
	    }
	    finally {
		pstmt.close();
	    }
	    // SQL ����ݒ�
	    pstmt = conn.prepareStatement(sqltxt);
	    // �p�����[�^��ݒ�
	    pstmt.setString(1, mrID);
	    pstmt.setString(2, statisticsdrinfo.getDrID());
	    pstmt.setString(3, sDate30);
	    // SQL ���s
	    rs   = pstmt.executeQuery();
	    try {
		while ( rs.next() ) {
		    statisticsdrinfo.setSendCount30(rs.getInt("counter"));
		}
	    } finally {
		pstmt.close();
	    }

	} catch (SQLException e) {
	    throw new MrException(e);
	}

    }

    /**
     * <h3>�S����t�l�r�f���m�W���n�̎擾 �i�⏕�j</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/28 �ߑO 01:22:00)
     * @return com.objectspace.jgl.BinaryPredicate
     * @param sortKey java.lang.String
     * @param rowType java.lang.String
     */
    private BinaryPredicate getSortPredicate(String sortKey, String rowType) {

	// 9/6 added by doi
	if (sortKey == null) {
	    sortKey = SysCnst.SORTKEY_STAT_NAME;
	}
	if (rowType == null) {
	    rowType = "A";
	}

	BinaryPredicate pred = new StatisticsDrNameAscendPredicate();
	
	if (sortKey.equals(SysCnst.SORTKEY_STAT_NAME)) {
	    if (rowType.equals("A")) {
		pred = new StatisticsDrNameAscendPredicate();
	    }
	    else {
		pred = new StatisticsDrNameDescendPredicate();
	    }
							
	}

	if (sortKey.equals(SysCnst.SORTKEY_STAT_TARGET_RANK)) {
	    if (rowType.equals("A")) {
		pred = new StatisticsTargetRankAscendPredicate();
	    }
	    else {
		pred = new StatisticsTargetRankDescendPredicate();
	    }
							
	}

	if (sortKey.equals(SysCnst.SORTKEY_STAT_SEND_COUNT30)) {
	    if (rowType.equals("A")) {
		pred = new StatisticsSendCount30AscendPredicate();
	    }
	    else {
		pred = new StatisticsSendCount30DescendPredicate();
	    }
							
	}

	if (sortKey.equals(SysCnst.SORTKEY_STAT_CKICK_COUNT30)) {
	    if (rowType.equals("A")) {
		pred = new StatisticsClickCount30AscendPredicate();
	    }
	    else {
		pred = new StatisticsClickCount30DescendPredicate();
	    }
							
	}

	if (sortKey.equals(SysCnst.SORTKEY_STAT_CKICK_RATE30)) {
	    if (rowType.equals("A")) {
		pred = new StatisticsClickRate30AscendPredicate();
	    }
	    else {
		pred = new StatisticsClickRate30DescendPredicate();
	    }
							
	}

	if (sortKey.equals(SysCnst.SORTKEY_STAT_SEND_COUNT180)) {
	    if (rowType.equals("A")) {
		pred = new StatisticsSendCount180AscendPredicate();
	    }
	    else {
		pred = new StatisticsSendCount180DescendPredicate();
	    }
							
	}

	if (sortKey.equals(SysCnst.SORTKEY_STAT_CKICK_COUNT180)) {
	    if (rowType.equals("A")) {
		pred = new StatisticsClickCount180AscendPredicate();
	    }
	    else {
		pred = new StatisticsClickCount180DescendPredicate();
	    }
							
	}

	if (sortKey.equals(SysCnst.SORTKEY_STAT_CKICK_RATE180)) {
	    if (rowType.equals("A")) {
		pred = new StatisticsClickRate180AscendPredicate();
	    }
	    else {
		pred = new StatisticsClickRate180DescendPredicate();
	    }
							
	}
	
	return pred;
    }
}
