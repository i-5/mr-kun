package jp.ne.sonet.medipro.mr.server.manager;

import java.util.*;
import java.sql.*;
import java.net.*;
import com.objectspace.jgl.*;
import jp.ne.sonet.medipro.mr.common.*;
import jp.ne.sonet.medipro.mr.common.predicate.*;
import jp.ne.sonet.medipro.mr.common.exception.*; 
import jp.ne.sonet.medipro.mr.server.entity.*;


/**
 * <h3>担当情報管理</h3>
 * 
 * <br>
 * ここで型の記述を挿入してください。
 * 作成日 : (00/06/21 午後 08:56:24)
 * @author: 
 */
public class TantoInfoManager {
    protected Connection conn;
	
    // 基本SQL
    protected static String DR_MSG_INFO_MAINSQL = 
	"SELECT " +
	"sen.dr_id sen_dr_id, " + 
	"sen.mr_id sen_mr_id, " + 
	"sen.target_rank sen_target_rank, " +
	"sen.name sen_name, " +
	"act.target_name act_target_name " + //1024 y-yamada add
	"FROM " +
	"sentaku_toroku sen, " +
	"doctor dr, " +
	"action act "; //1024 y-yamada add
    protected static String DR_MSG_INFO_OPTIONALSQL =
	"WHERE " +
	"sen.mr_id = ? " +
	"AND sen.dr_id = dr.dr_id "+
	"AND act.company_cd = ? " +//1106 y-yamada add company_cd とtarget_rank の両方で検索する
	"AND sen.target_rank = act.target_rank ";//1024 y-yamada add

    // 未読受信メッセージSQL
    protected static String DR_MSG_UNREAD_RECV_MAINSQL =
	"SELECT " +
	"Count(message_header_id) count " +
	"FROM " +
	"message_header mh, message_body mb ";
    protected static String DR_MSG_UNREAD_RECV_OPTIONALSQL = 
	"WHERE " +
	"from_userid = ? " +
	"AND to_userid = ? " +
	"AND mh.receive_timed IS NULL " +
	"AND mh.send_torikeshi_time IS NULL " +
	"AND mh.message_kbn <> '3' " +
	"AND mh.receive_status <> '3' " + 
	"AND mh.message_id = mb.message_id " +
	"AND ((mb.yuko_kigen IS NULL) OR (SYSDATE <= mb.yuko_kigen )) ";

    // 新しい開封知らせSQL
    protected static String DR_MSG_NEW_OPEN_MAINSQL =
	"SELECT " +
	"Count(message_header_id) count " +
	"FROM " +
	"message_header, mr ";
    protected static String DR_MSG_NEW_OPEN_OPTIONALSQL =
	"WHERE " + 
	"from_userid = ? " +
	"AND to_userid = ? " +
	"AND to_userid = mr_id " +
	"AND receive_time > previous_login_time";

    // 前回開封からのSQL
    protected final static String DR_MSG_LAST_OPEN_MAINSQL =
	"SELECT " +
	"TO_DATE(TO_CHAR(SYSDATE,'YYYYMMDD'),'YYYYMMDD') " +
	"- TO_DATE(TO_CHAR(MAX(receive_timed),'YYYYMMDD'),'YYYYMMDD') lastday " +
	"FROM " +
	"message_header ";
    protected static String DR_MSG_LAST_OPEN_OPTIONALSQL =
	"WHERE " +
	"from_userid = ? " +
	"AND to_userid = ? ";

    // 未読送信メッセージSQL
    protected static String DR_MSG_UNREAD_SEND_MAINSQL =
	"SELECT " +
	"Count(message_header_id) count " +
	"FROM " +
	"message_header mh, message_body mb ";
    protected static String DR_MSG_UNREAD_SEND_OPTIONALSQL =
	"WHERE " +
	"mh.to_userid = ? " +
	"AND mh.from_userid = ? " +
	"AND mh.receive_timed IS NULL " +
	"AND mh.send_torikeshi_time IS NULL " +
	"AND mh.message_kbn <> '3' " +
	"AND mh.receive_status <> '3' " + 
	"AND mh.message_id = mb.message_id " +
	"AND ((mb.yuko_kigen IS NULL) OR (SYSDATE <= mb.yuko_kigen )) ";

    // 最新送信の未読日SQL
    protected static String DR_MSG_SEND_NOREAD_MAINSQL =
	"SELECT " +
	"TO_DATE(TO_CHAR(SYSDATE,'YYYYMMDD'),'YYYYMMDD') " +
	"- TO_DATE(TO_CHAR(MAX(receive_time),'YYYYMMDD'),'YYYYMMDD') lastday " +
	"FROM " +
	"message_header mh, message_body mb ";
    protected static String DR_MSG_SEND_NOREAD_OPTIONALSQL =
	"WHERE " +
	"mh.to_userid = ? " +
	"AND mh.from_userid = ? " +
	"AND mh.receive_timed IS NULL " +
	"AND mh.send_torikeshi_time IS NULL " +
	"AND mh.message_kbn <> '3' " +
	"AND mh.message_id = mb.message_id " +
	"AND mb.yuko_kigen IS NULL ";

    protected static String MR_INFO_MAINSQL
	= "SELECT dr_id, mr_id, sentaku_kbn, dr_memo FROM sentaku_toroku ";
    protected static String MR_INFO_OPTIONALSQL_1
	= "WHERE dr_id = ? ORDER BY dr_id, sentaku_kbn, mr_id";
    protected static String MR_INFO_OPTIONALSQL_2
	= "WHERE dr_id = ? AND mr_id = ? ORDER BY dr_id, sentaku_kbn, mr_id";
	
    protected static String SENTAKU_UPDATE
	= "UPDATE sentaku_toroku set sentaku_kbn = ? ";
    protected static String DR_MEMO_UPDATE
	= "UPDATE sentaku_toroku set dr_memo = ? WHERE dr_id = ? AND mr_id = ? ";

    protected static String SENTAKU_TOUROKU_HIST_INSERT
	= "INSERT INTO sentaku_toroku_hist ( " +
	"DR_ID," +
	"MR_ID," +
	"SEQ," +
	"DR_MEMO," +
	"SENTAKU_KBN," +
	"START_YMD," +
	"END_YMD," +
	"NAME," +
	"KINMUSAKI," +
	"MAKER_CUST_ID," +
	"MAKER_SHISETSU_ID," +
	"TARGET_RANK," +
	"SYOKUSYU," +
	"SENMON1," +
	"SENMON2," +
	"SENMON3," +
	"YAKUSYOKU," +
	"SOTSUGYO_DAIGAKU," +
	"SOTSUGYO_YEAR," +
	"SYUMI," +
	"SONOTA, " +
	"UPDATE_TIME " +//1221 アップデートタイムもコピー
	") " +
	"SELECT " +
	"DR_ID," +
	"MR_ID," +
	"SEQ," +
	"DR_MEMO," +
	"SENTAKU_KBN," +
	"START_YMD," +
	"SYSDATE," +
	"NAME," +
	"KINMUSAKI," +
	"MAKER_CUST_ID," +
	"MAKER_SHISETSU_ID," +
	"TARGET_RANK," +
	"SYOKUSYU," +
	"SENMON1," +
	"SENMON2," +
	"SENMON3," +
	"YAKUSYOKU," +
	"SOTSUGYO_DAIGAKU," +
	"SOTSUGYO_YEAR," +
	"SYUMI," +
	"SONOTA, " +
	"UPDATE_TIME " +//1221 アップデートタイムもコピー
	"FROM " +
	"sentaku_toroku " +
	"WHERE " +
	"dr_id = ? " +
	"AND mr_id = ? ";

    protected static String SENTAKU_TOUTOKU_DELETE
	= "DELETE FROM sentaku_toroku WHERE dr_id = ? AND mr_id = ? ";

    protected static String SENTAKU_TOUROKU_INSERT
	= "INSERT INTO sentaku_toroku ( " +
	"DR_ID," +
	"MR_ID," +
	"SEQ," +
	"DR_MEMO," +
	"SENTAKU_KBN," +
	"START_YMD," +
	"END_YMD," +
	"NAME," +
	"KINMUSAKI," +
	"MAKER_CUST_ID," +
	"MAKER_SHISETSU_ID," +
	"TARGET_RANK," +
	"SYOKUSYU," +
	"SENMON1," +
	"SENMON2," +
	"SENMON3," +
	"YAKUSYOKU," +
	"SOTSUGYO_DAIGAKU," +
	"SOTSUGYO_YEAR," +
	"SYUMI," +
	"SONOTA, " +
	"UPDATE_TIME " + //1218 更新時間を取得
	") " +
	"SELECT " +
	"?," + //drID  1
	"?," + //mrID   2
	"?," + //seqCouter   3
	"NULL," + //DR_MEMO
	"?," + //SENTAKU_KBN    4
	"SYSDATE," + //START_YMD
	"NULL," + //END_YMD
	"name," + //NAME
	"kinmusaki_name," + //KINMUSAKI
	"NULL," + //MAKER_CUST_ID
	"NULL," + //MAKER_SHISETSU_ID
//	"NULL," + //TARGET_RANK
	"?," + //TARGET_RANK    5
	"'医師'," + //職種
	"NULL," + //SENMON1
	"NULL," + //SENMON2
	"NULL," + //SENMON3
	"NULL," + //YAKUSYOKU
	"NULL," + //SOTSUGYO_DAIGAKU
	"NULL," + //SOTSUGYO_YEAR
	"NULL," + //SYUMI
	"NULL, " + //SONOTA
	"SYSDATE " + //1218 更新時間を追加
	"FROM doctor, kinmusaki " +
	"WHERE doctor.dr_id = ? " +//drId    6
	"AND doctor.dr_id = kinmusaki.dr_id (+) ";

    protected static String DOCTOR_UPDATE
	= "UPDATE doctor set mrkun_mishiyo_flg = '0' WHERE dr_id = ? ";
	
    protected static String SENTAKU_TOROKU_SEQ_SQL
	= "SELECT TRIM(TO_CHAR(sentaku_toroku_seq.nextval,'000')) counter FROM dual";

    protected static String DR_PROFILE_UPDATE
	= "UPDATE sentaku_toroku set " +
	"name = ?, " +
	"kinmusaki = ?, " +
	"maker_cust_id = ?, " +
	"maker_shisetsu_id = ?, " +
	"target_rank = ?, " +
	"syokusyu = ?, " +
	"senmon1 = ?, " +
	"senmon2 = ?, " +
	"senmon3 = ?, " +
	"yakusyoku = ?, " +
	"sotsugyo_daigaku = ?, " +
	"sotsugyo_year = ?, " +
	"syumi = ?, " +
	"sonota = ?, " +
	"update_time = SYSDATE " + //1218　更新時間を設定
	"WHERE " +
	"dr_id = ? " +
	"AND mr_id = ? ";

    protected static String DR_INFO_LIST_MAINSQL
	= "SELECT " +  
	"sen.dr_id sen_dr_id, " + 
	"sen.mr_id sen_mr_id, " + 
	"sen.target_rank sen_target_rank, " + 
	"sen.syokusyu sen_syokusyu, " + 
	"sen.senmon1 sen_senmon1, " + 
	"sen.senmon2 sen_senmon2, " + 
	"sen.senmon3 sen_senmon3, " +  
	"sen.kinmusaki sen_kinmusaki, " +  
	"sen.name sen_name, " +  
	"act.target_name act_target_name " +//1025 y-yamada add
	"FROM " +  
	"sentaku_toroku sen, " + 
	"action act " + //1025 y-yamada add
	"WHERE " +  
	"sen.mr_id = ? " +
	"AND act.company_cd = ?" +
	"AND sen.target_rank = act.target_rank ";//1025 y-yamada add


    protected static final String DR_MR_RELATION_SQL
	= "select count(*) from sentaku_toroku "
	+ "where dr_id = ? "
	+ "and mr_id = ? ";

// doctor.name -> sentaku_toroku_hist.name
    protected static final String DR_MR_RELATION_END_SQL
	= "select sentaku_toroku_hist.name AS name, sentaku_toroku_hist.end_ymd AS end_ymd "
	+ "from sentaku_toroku_hist,mr,doctor "
	+ "where sentaku_toroku_hist.mr_id = ? "
	+ "and sentaku_toroku_hist.mr_id = mr.mr_id "
	+ "and sentaku_toroku_hist.dr_id = doctor.dr_id "
	+ "and sentaku_toroku_hist.end_ymd > mr.login_time ";

    protected static final String DR_MR_FRONT_COUNT_SQL
	= "SELECT count(*) counter "
	+ "FROM sentaku_toroku "
	+ "WHERE "
	+ "dr_id = ? "
	+ "AND sentaku_kbn IN ('1', '2')";

    protected static final String ACTION_INFO_SQL
	= "SELECT "
	+ " target_rank"
	+ ",threshold"
	+ ",message1"
	+ ",message2"
	+ ",message3"
	+ ",message4"
	+ " FROM action, mr "
	+ " WHERE "
	+ " mr.mr_id = ?"
	+ " AND target_rank = ?"
	+ " AND mr.company_cd = action.company_cd";

    protected static final String DR_MR_HAD_RELATION_SQL
	= "SELECT count(*) AS counter "
	+ "FROM sentaku_toroku sen1,sentaku_toroku_hist sen2 "
	+ "WHERE "
	+ "(sen1.dr_id = ? AND sen1.mr_id = ?) "
	+ "OR (sen2.dr_id = ? AND sen2.mr_id = ?) ";

    //フロント設定アンケートMR取得SQL
    protected static final String ENQUETE_MR_SQL
	= "SELECT"
	+ " dr_id"
	+ ",mr_id"
	+ ",sentaku_kbn"
	+ " FROM sentaku_toroku"
	+ " WHERE"
	+ " dr_id = ?"
	+ " AND mr_id = (SELECT naiyo1 "
	+               "FROM constant_master "
	+               "WHERE constant_cd = 'ENQUETE_MR_ID')"
	+ " AND sentaku_kbn in ('1', '2')";

    //選択区分更新SQL
    protected static final String ENQUETE_MR_REPLACE_SQL
	= "UPDATE sentaku_toroku SET"
	+ " sentaku_kbn = ?"
	+ " WHERE"
	+ " dr_id = ?"
	+ " AND mr_id = ?";

    //左シフト
//      protected static final String SHIFT_SQL
//  	= "UPDATE sentaku_toroku SET"
//  	+ " sentaku_kbn = '1'"
//  	+ " WHERE"
//  	+ " dr_id = ?"
//  	+ " AND sentaku_kbn = '2'";

    /**
     * TantoInfoManager コンストラクター・コメント。
     */
    public TantoInfoManager(Connection conn) {
	this.conn = conn;
    }

    /**
     * <h3>担当医師情報の取得</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/22 午後 12:37:31)
     * @return jp.ne.sonet.medipro.mr.server.entity.TantoInfo
     * @param mrID java.lang.String
     * @param drID java.lang.String
     */
    public TantoInfo getDrInfo(String mrID, String drID) {
		//このクラスには getDrInfo メソッドが２個あるので注意
	TantoInfo tan = null;

	try {
	    String sqltxt = "SELECT "; 
	    sqltxt = sqltxt + "sen.dr_id sen_dr_id, ";
	    sqltxt = sqltxt + "sen.mr_id sen_mr_id, ";
	    sqltxt = sqltxt + "sen.target_rank sen_target_rank, ";
	    sqltxt = sqltxt + "sen.syokusyu sen_syokusyu, ";
	    sqltxt = sqltxt + "sen.senmon1 sen_senmon1, ";
	    sqltxt = sqltxt + "sen.senmon2 sen_senmon2, ";
	    sqltxt = sqltxt + "sen.senmon3 sen_senmon3, "; 
	    sqltxt = sqltxt + "sen.name sen_name, ";
	    sqltxt = sqltxt + "sen.kinmusaki sen_kinmusaki, ";
	    sqltxt = sqltxt + "sen.maker_cust_id sen_maker_cust_id, "; 
	    sqltxt = sqltxt + "sen.maker_shisetsu_id sen_maker_shisetsu_id, "; 
	    sqltxt = sqltxt + "sen.yakusyoku sen_yakusyoku, "; 
	    sqltxt = sqltxt + "sen.sotsugyo_daigaku sen_sotsugyo_daigaku, "; 
	    sqltxt = sqltxt + "sen.sotsugyo_year sen_sotsugyo_year, "; 
	    sqltxt = sqltxt + "sen.syumi sen_syumi, "; 
	    sqltxt = sqltxt + "sen.sonota sonota "; 
	    sqltxt = sqltxt + "FROM "; 
	    sqltxt = sqltxt + "sentaku_toroku sen ";
	    sqltxt = sqltxt + "WHERE "; 
	    sqltxt = sqltxt + "sen.mr_id = '" + mrID + "' ";
	    sqltxt = sqltxt + "AND sen.dr_id = '" + drID + "' ";
		
	    Statement stmt = conn.createStatement();

	    try {
		ResultSet rs = stmt.executeQuery(sqltxt);
		
		while (rs.next()) {
		    tan = new TantoInfo();
		    tan.setDrID(rs.getString("sen_dr_id"));
		    tan.setMrID(rs.getString("sen_mr_id"));
		    tan.setTargetRank(rs.getString("sen_target_rank"));
		    tan.setSyokusyu(rs.getString("sen_syokusyu"));
		    tan.setSenmon1(rs.getString("sen_senmon1"));
		    tan.setSenmon2(rs.getString("sen_senmon2"));
		    tan.setSenmon3(rs.getString("sen_senmon3"));
		    tan.setName(rs.getString("sen_name"));
		    tan.setKinmusaki(rs.getString("sen_kinmusaki"));
		    tan.setMakerCustID(rs.getString("sen_maker_cust_id"));
		    tan.setMakerShisetsuID(rs.getString("sen_maker_shisetsu_id"));
		    tan.setYakusyoku(rs.getString("sen_yakusyoku"));
		    tan.setSotsugyoDaigaku(rs.getString("sen_sotsugyo_daigaku"));
		    tan.setSotsugyoYear(rs.getString("sen_sotsugyo_year"));
		    tan.setSyumi(rs.getString("sen_syumi"));
		    tan.setSonota(rs.getString("sonota"));
				
		    DoctorInfoManager doctor = new DoctorInfoManager(conn);
//  		    tan.setDoctorinfo(doctor.getDoctorInfo(rs.getString("sen_dr_id")));
		    tan.setDoctorinfo(doctor.getDoctorInfo(tan.getDrID()));
		}
	    } finally {
		stmt.close();
	    }
	} catch (SQLException e) {
	    throw new MrException(e);
	}

	return tan;
    }

    /**
     * <h3>担当医師情報［集合］の取得</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/22 午後 12:37:31)
     * @return java.util.Enumeration
     * @param mrID java.lang.String
     * @param sortKey java.lang.String
     * @param rowType java.lang.String
     */
//    public Enumeration getDrInfo(String mrID, String sortKey, String rowType) {
    public Enumeration getDrInfo(String mrID, String sortKey, String rowType , String companyCD) {
		//このクラスには getDrInfo メソッドが２個あるので注意
	BinaryPredicate pred = getDrInfoSortPredicate(sortKey, rowType);
	OrderedSet tantoInfoList = new OrderedSet(pred,true);
	DoctorInfoManager manager = new DoctorInfoManager(conn);
	
	try {
	    //ＳＱＬ文
	    String sqltxt = DR_INFO_LIST_MAINSQL;
	    // SQL 文を設定
	    PreparedStatement pstmt = conn.prepareStatement(sqltxt);
	    try {
		// パラメータを設定
		pstmt.setString(1, mrID);
		pstmt.setString(2, companyCD);// 1106 y-yamada add
		// SQL 実行
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
		    TantoInfo tanInfo = new TantoInfo();
		    tanInfo.setDrID(rs.getString("sen_dr_id"));
		    tanInfo.setMrID(rs.getString("sen_mr_id"));
		    tanInfo.setTargetRank(rs.getString("sen_target_rank"));
		    tanInfo.setTargetName(rs.getString("act_target_name"));// 1025 y-yamada add
		    tanInfo.setSyokusyu(rs.getString("sen_syokusyu"));
		    tanInfo.setSenmon1(rs.getString("sen_senmon1"));
		    tanInfo.setSenmon2(rs.getString("sen_senmon2"));
		    tanInfo.setSenmon3(rs.getString("sen_senmon3"));
		    tanInfo.setKinmusaki(rs.getString("sen_kinmusaki"));
		    // added masato 09/02
		    tanInfo.setName(rs.getString("sen_name"));
//  		    tanInfo.setDoctorinfo(manager.getDoctorInfo(rs.getString("sen_dr_id")));
		    tanInfo.setDoctorinfo(manager.getDoctorInfo(tanInfo.getDrID()));
		    tantoInfoList.add(tanInfo);
		}
	    } finally {
		//rs.close();
		pstmt.close();
	    }
	} catch (SQLException e) {
	    throw new MrException(e);
	}

	return tantoInfoList.elements();
    }

    /**
     * <h3>担当医師情報ソート条件の取得 （補助）</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/28 午前 01:22:00)
     * @return com.objectspace.jgl.BinaryPredicate
     * @param sortKey java.lang.String
     * @param rowType java.lang.String
     */
    private BinaryPredicate getDrInfoSortPredicate(String sortKey, String rowType) {

	// 9/6 added by doi
	if (sortKey == null) {
	    sortKey = SysCnst.SORTKEY_DR_KINMUSAKI;
	}
	if (rowType == null) {
	    rowType = "A";
	}

	BinaryPredicate pred = new KinmusakiAscendPredicate();
	
	if (sortKey.equals(SysCnst.SORTKEY_DR_KINMUSAKI)) {
	    if (rowType.equals("A")) {
		pred = new KinmusakiAscendPredicate();
	    } else {
		pred = new KinmusakiDescendPredicate();
	    }
	} else if (sortKey.equals(SysCnst.SORTKEY_DR_DR_ID)) {
	    if (rowType.equals("A")) {
		pred = new DrNameAscendPredicate();
	    } else {
		pred = new DrNameDescendPredicate();
	    }
	} else if (sortKey.equals(SysCnst.SORTKEY_DR_TARGET_RANK)) {
	    if (rowType.equals("A")) {
		pred = new DrTargetRankAscendPredicate();
	    } else {
		pred = new DrTargetRankDescendPredicate();
	    }
	} else if (sortKey.equals(SysCnst.SORTKEY_DR_SYOKUSYU)) {
	    if (rowType.equals("A")) {
		pred = new DrSyokusyuAscendPredicate();
	    } else {
		pred = new DrSyokusyuDescendPredicate();
	    }
	} else if (sortKey.equals(SysCnst.SORTKEY_DR_SENMON1)) {
	    if (rowType.equals("A")) {
		pred = new DrSenmon1AscendPredicate();
	    } else {
		pred = new DrSenmon1DescendPredicate();
	    }
	} else if (sortKey.equals(SysCnst.SORTKEY_DR_SENMON2)) {
	    if (rowType.equals("A")) {
		pred = new DrSenmon2AscendPredicate();
	    } else {
		pred = new DrSenmon2DescendPredicate();
	    }
	} else if (sortKey.equals(SysCnst.SORTKEY_DR_SENMON3)) {
	    if (rowType.equals("A")) {
		pred = new DrSenmon3AscendPredicate();
	    } else {
		pred = new DrSenmon3DescendPredicate();
	    }
	}
	return pred;
    }

    /**
     * <h3>担当医師ＭＳＧ情報［集合］の取得</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/22 午後 12:28:09)
     * @return java.util.Enumeration
     * @param mrID java.lang.String
     * @param sortKey java.lang.String
     * @param rowType java.lang.String
     */
//    public Enumeration getDrMsgInfo(String mrID, String sortKey, String rowType) {
    public Enumeration getDrMsgInfo(String mrID, String sortKey, String rowType , String companyCD) { //1106 y-yamada
	Enumeration tantoInfoList;
	OrderedSet tantoInfoList1 = new OrderedSet();
	Vector tantoInfoList2 = new Vector();
	BinaryPredicate pred = getDrSortPredicate(sortKey, rowType);
	//ＳＱＬ文
	String sqltxt = DR_MSG_INFO_MAINSQL + DR_MSG_INFO_OPTIONALSQL;
	if (pred != null) { 	
	    tantoInfoList1 = new OrderedSet(pred,true);
	} else {
	    sqltxt = sqltxt + "ORDER BY " + sortKey;
	    if ( rowType.equals("D") ) {
		sqltxt = sqltxt + " DESC";
	    } else {
		sqltxt = sqltxt + " ASC";
	    }		
	}

	try {
	    // SQL 文を設定
	    PreparedStatement pstmt = conn.prepareStatement(sqltxt);
	    try {
		// パラメータを設定
		pstmt.setString(1, mrID);
		pstmt.setString(2, companyCD);//1106 y-yamada add
		// SQL 実行
		ResultSet rs = pstmt.executeQuery();

		while (rs.next()) {
		    TantoInfo taninfo = new TantoInfo();
		    taninfo.setDrID(rs.getString("sen_dr_id"));
		    taninfo.setMrID(rs.getString("sen_mr_id"));
		    taninfo.setTargetRank(rs.getString("sen_target_rank"));
		    taninfo.setTargetName(rs.getString("act_target_name"));//1024 y-yamada add
		    // added by masato 09/02
		    taninfo.setName(rs.getString("sen_name"));

		    getDrMsgInfoSub(taninfo);

		    DoctorInfoManager doctor = new DoctorInfoManager(conn);
//  		    taninfo.setDoctorinfo(doctor.getDoctorInfo(rs.getString("sen_dr_id")));
		    taninfo.setDoctorinfo(doctor.getDoctorInfo(taninfo.getDrID()));

		    if ( pred != null ) {
			tantoInfoList1.add(taninfo);
		    } else {
			tantoInfoList2.addElement(taninfo);
		    }
		}

		if ( pred != null ) {
		    tantoInfoList = tantoInfoList1.elements();
		} else {
		    tantoInfoList = tantoInfoList2.elements();
		}		
			
	    } finally {
		pstmt.close();
	    }
	} catch (SQLException e) {
	    throw new MrException(e);
	}

	return tantoInfoList;
    }

    /**
     * <h3>担当医師ＭＳＧ情報［集合］の取得（補助）</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/22 午後 12:28:09)
     * @param mrID java.lang.String
     * @param sortKey java.lang.String
     * @param rowType java.lang.String
     */
    private void getDrMsgInfoSub(TantoInfo tan) {
	String sqltxt;
//20010607 Mizuki	int lastOpenDay = 0;
	int lastOpenDay = -1;	//20010607 Mizuki
	int sendCount = 0;

	try {	
	    //未読受信メッセージＳＱＬ文
	    sqltxt = DR_MSG_UNREAD_RECV_MAINSQL + DR_MSG_UNREAD_RECV_OPTIONALSQL;
			
	    // SQL 文を設定
	    PreparedStatement pstmt = conn.prepareStatement(sqltxt);
	    try {
		// パラメータを設定
		pstmt.setString(1, tan.getDrID());
		pstmt.setString(2, tan.getMrID());
		// SQL 実行
		ResultSet rs = pstmt.executeQuery();
		tan.setRecvCount(0);

		while ( rs.next() ) {
		    tan.setRecvCount(rs.getInt("count"));
		}
	    } finally {
		pstmt.close();
	    }

			
	    //新しい開封知らせＳＱＬ文
	    sqltxt = DR_MSG_NEW_OPEN_MAINSQL + DR_MSG_NEW_OPEN_OPTIONALSQL;			
	    // SQL 文を設定
	    pstmt = conn.prepareStatement(sqltxt);
	    try {
		// パラメータを設定
		pstmt.setString(1, tan.getDrID());
		pstmt.setString(2, tan.getMrID());
		// SQL 実行
		ResultSet rs = pstmt.executeQuery();
		tan.setNewOpenCount(0);

		while ( rs.next() ) {
		    tan.setNewOpenCount(rs.getInt("count"));
		}
	    } finally {
		pstmt.close();
	    }

	    //前回開封からのＳＱＬ文
	    sqltxt = DR_MSG_LAST_OPEN_MAINSQL + DR_MSG_LAST_OPEN_OPTIONALSQL;
	    // SQL 文を設定
	    pstmt = conn.prepareStatement(sqltxt);
	    try {
		// パラメータを設定
		pstmt.setString(1, tan.getMrID());
		pstmt.setString(2, tan.getDrID());
		// SQL 実行
		ResultSet rs = pstmt.executeQuery();
		tan.setLastOpenDay(0);

		while ( rs.next() ) {
		    lastOpenDay = rs.getInt("lastday");
		    tan.setLastOpenDay(lastOpenDay);
		    if( rs.wasNull() ){		//20010607 Mizuki
			tan.setLastOpenDay(-1);	//20010607 Mizuki
		    }
		}
	    } finally{
		pstmt.close();
	    }

	    //未読送信メッセージＳＱＬ文
	    sqltxt = DR_MSG_UNREAD_SEND_MAINSQL + DR_MSG_UNREAD_SEND_OPTIONALSQL;
	    // SQL 文を設定
	    pstmt = conn.prepareStatement(sqltxt);
	    try {
		// パラメータを設定
		pstmt.setString(1, tan.getDrID());
		pstmt.setString(2, tan.getMrID());
		// SQL 実行
		ResultSet rs = pstmt.executeQuery();
		tan.setSendCount(0);

		while ( rs.next() ) {
		    sendCount = rs.getInt("count");
		    tan.setSendCount(sendCount);
		}
	    } finally {
		pstmt.close();
	    }

	    //最新送信の未読日ＳＱＬ文
	    sqltxt = DR_MSG_SEND_NOREAD_MAINSQL + DR_MSG_SEND_NOREAD_OPTIONALSQL;
	    // SQL 文を設定
	    pstmt = conn.prepareStatement(sqltxt);
	    try {
		// パラメータを設定
		pstmt.setString(1, tan.getDrID());
		pstmt.setString(2, tan.getMrID());
		// SQL 実行
		ResultSet rs   = pstmt.executeQuery();
		tan.setSendNoReadDay(0);

		while ( rs.next() ) {
		    tan.setSendNoReadDay(rs.getInt("lastday"));
		}
	    } finally {
		pstmt.close();
	    }

	    pstmt = conn.prepareStatement(ACTION_INFO_SQL);
	    try {
		pstmt.setString(1, tan.getMrID());
		pstmt.setString(2, tan.getTargetRank());
		ResultSet rs = pstmt.executeQuery();
		tan.setAction(0);
		if (rs.next()) {
		    int threshold = rs.getInt("threshold");
		    //アクションの判定
		    int action = determineAction(lastOpenDay, sendCount, threshold);
		    tan.setAction(action);
		}
	    } finally {
		pstmt.close();
	    }
	} catch (SQLException e) {
	    throw new MrException(e);
	}
    }

    /**
     *　アクションの判定
     */
    private int determineAction(int lastOpenDay, int sendCount, int threshold) {
	int action = 0;

	if (lastOpenDay >= threshold && sendCount == 0) {
	    action = 1;
	} else if (lastOpenDay == -1 && sendCount == 0) {	//20010607 Mizuki
	    action = 1;
	} else if (lastOpenDay >= threshold && sendCount > 0) {
	    action = 2;
	} else if (lastOpenDay == -1 && sendCount > 0) {	//20010607 Mizuki
	    action = 2;
	} else if (lastOpenDay < threshold && sendCount == 0) {
	    action = 3;
	} else if (lastOpenDay < threshold && sendCount > 0) {
	    action = 4;
	}

	return action;
    }

    /**
     * <h3>担当医師ＭＳＧ情報［集合］の取得 （補助）</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/28 午前 01:22:00)
     * @return com.objectspace.jgl.BinaryPredicate
     * @param sortKey java.lang.String
     * @param rowType java.lang.String
     */
    private BinaryPredicate getDrSortPredicate(String sortKey, String rowType) {

	// 9/6 added by doi
	if (sortKey == null) {
	    sortKey = SysCnst.SORTKEY_RECV_COUNT;
	}
	if (rowType == null) {
	    rowType = "A";
	}

	BinaryPredicate pred = new RecvCountAscendPredicate();
	
	if (sortKey.equals(SysCnst.SORTKEY_DR_TARGET_RANK)) {
	    if (rowType.equals("A")) {
		pred = new DrTargetRankAscendPredicate();
	    } else {
		pred = new DrTargetRankDescendPredicate();
	    }
	} else if (sortKey.equals(SysCnst.SORTKEY_RECV_COUNT)) {
	    if (rowType.equals("A")) {
		pred = new RecvCountAscendPredicate();
	    } else {
		pred = new RecvCountDescendPredicate();
	    }
	} else if (sortKey.equals(SysCnst.SORTKEY_ACTION)) {
	    if (rowType.equals("A")) {
		pred = new ActionAscendPredicate();
	    } else {
		pred = new ActionDescendPredicate();
	    }
	} else if (sortKey.equals(SysCnst.SORTKEY_NEW_OPEN_COUNT)) {
	    if (rowType.equals("A")) {
		pred = new NewOpenCountAscendPredicate();
	    } else {
		pred = new NewOpenCountDescendPredicate();
	    }
	} else if (sortKey.equals(SysCnst.SORTKEY_LAST_OPEN_DAY)) {
	    if (rowType.equals("A")) {
		pred = new LastOpenDayAscendPredicate();
	    } else {
		pred = new LastOpenDayDescendPredicate();
	    }
	} else if (sortKey.equals(SysCnst.SORTKEY_SEND_COUNT)) {
	    if (rowType.equals("A")) {
		pred = new SendCountAscendPredicate();
	    } else {
		pred = new SendCountDescendPredicate();
	    }
	} else if (sortKey.equals(SysCnst.SORTKEY_SEND_NO_READ_DAY)) {
	    if (rowType.equals("A")) {
		pred = new SendNoReadDayAscendPredicate();
	    } else {
		pred = new SendNoReadDayDescendPredicate();
	    }
	    /*　氏名でのソートを追加する　　y-yamada add */
	} else if (sortKey.equals(SysCnst.SORTKEY_DR_NAME)) {
	    if (rowType.equals("A")) {
		pred = new DrNameAscendPredicate();
	    } else {
		pred = new DrNameDescendPredicate();
	    }
	}
	
	return pred;
    }

    /**
     * <h3>担当医師統計情報の取得</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/22 午後 12:56:12)
     * @return java.util.Enumeration
     * @param mrID java.lang.String
     */
    public Enumeration getDrStatisticsInfo(String mrID) {
	return null;
    }

    /**
     * <h3>担当ＭＲ情報の取得</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/22 午後 12:53:33)
     * @return jp.ne.sonet.medipro.mr.server.entity.TantoInfo
     * @param drID java.lang.String
     * @param mrID java.lang.String
     */
    public TantoInfo getMrInfo(String drID, String mrID) {
	TantoInfo tantoinfo = null;
	
	//ＳＱＬ文
	try {
	    String sqltxt = MR_INFO_MAINSQL + MR_INFO_OPTIONALSQL_2;
		
	    // SQL 文を設定
	    PreparedStatement pstmt = conn.prepareStatement(sqltxt);
	    try {
		// パラメータを設定
		pstmt.setString(1, drID);
		pstmt.setString(2, mrID);
		// SQL 実行
		ResultSet rs = pstmt.executeQuery();
		while ( rs.next() ) {
		    tantoinfo = new TantoInfo();
		    tantoinfo.setDrID(rs.getString("dr_id"));
		    tantoinfo.setMrID(rs.getString("mr_id"));
		    tantoinfo.setDrMemo(rs.getString("dr_memo"));

		    MrInfoManager mrinfomanager = new MrInfoManager(conn);
//  		    tantoinfo.setMrinfo(mrinfomanager.getMrInfo(rs.getString("mr_id")));
		    tantoinfo.setMrinfo(mrinfomanager.getMrInfo(tantoinfo.getMrID()));
		}
	    } finally {
		pstmt.close();
	    }
	} catch (SQLException e) {
	    throw new MrException(e);
	}

	return tantoinfo;
    }

    /**
     * <h3>担当ＭＲ情報[集合]の取得</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/22 午後 12:48:08)
     * @return java.util.Enumeration
     * @param drID java.lang.String
     * @param sortKey java.lang.String
     * @param rowType java.lang.String
     */
    public Enumeration getMrInfo(String drID, String sortKey, String rowType) {
	BinaryPredicate pred = getMrSortPredicate(sortKey, rowType);
	OrderedSet tantoInfoList = new OrderedSet(pred,true);
	
	//ＳＱＬ文
	try {
	    String sqltxt = MR_INFO_MAINSQL + MR_INFO_OPTIONALSQL_1;
		
	    // SQL 文を設定
	    PreparedStatement pstmt = conn.prepareStatement(sqltxt);
	    try {
		// パラメータを設定
		pstmt.setString(1, drID);
		// SQL 実行
		ResultSet rs = pstmt.executeQuery();
		while ( rs.next() ) {
		    TantoInfo tantoinfo = new TantoInfo();
		    tantoinfo.setDrID(rs.getString("dr_id"));
		    tantoinfo.setMrID(rs.getString("mr_id"));
		    tantoinfo.setSentakuKbn(rs.getString("sentaku_kbn"));

		    DoctorInfoManager doctor = new DoctorInfoManager(conn);
//  		    tantoinfo.setDoctorinfo(doctor.getDoctorInfo(rs.getString("dr_id")));
		    tantoinfo.setDoctorinfo(doctor.getDoctorInfo(tantoinfo.getDrID()));

		    MrInfoManager mrinfomanager = new MrInfoManager(conn);
		    tantoinfo.setMrinfo(mrinfomanager.getMrInfo(tantoinfo.getMrID()));

		    tantoInfoList.add(tantoinfo);
		}	
	    } finally {
		pstmt.close();
	    }
	} catch (SQLException e) {
	    throw new MrException(e);
	}

	return tantoInfoList.elements();
    }

    /**
     * <h3>担当医師ＭＳＧ情報［集合］の取得 （補助）</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/06/28 午前 01:22:00)
     * @return com.objectspace.jgl.BinaryPredicate
     * @param sortKey java.lang.String
     * @param rowType java.lang.String
     */
    private BinaryPredicate getMrSortPredicate(String sortKey, String rowType) {

	// 9/6 added by doi
	if (sortKey == null) {
	    sortKey = SysCnst.SORTKEY_SENTAKU_KBN;
	}
	if (rowType == null) {
	    rowType = "A";
	}

	BinaryPredicate pred = new MrRollSentakuAscendPredicate();
	
	if (sortKey.equals(SysCnst.SORTKEY_SENTAKU_KBN)) {
	    if (rowType.equals("A")) {
		pred = new MrRollSentakuAscendPredicate();
	    } else {
		pred = new MrRollSentakuDescendPredicate();
	    }
	} else if (sortKey.equals(SysCnst.SORTKEY_NAME)) {
	    if (rowType.equals("A")) {
		pred = new MrRollNameAscendPredicate();
	    } else {
		pred = new MrRollNameDescendPredicate();
	    }
	} else if (sortKey.equals(SysCnst.SORTKEY_COMPANY_NAME)) {
	    if (rowType.equals("A")) {
		pred = new MrRollCoNameAscendPredicate();
	    } else {
		pred = new MrRollCoNameDescendPredicate();
	    }
	}
	
	return pred;
    }

    /**
     * <h3>選択登録の書出し</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/07/01 午後 03:50:50)
     * @param drID java.lang.String
     * @param mrID java.lang.String
     */
    public void insertSentakuTouroku(String drID, String mrID) {
	String updsql;
	int counter;
	String seqCounter = null;
	String sentakuKbn = "3";
		
	try {
	    conn.setAutoCommit(false);
		
	    //SEQの取得
	    Statement stmt = conn.createStatement();
	    try {
		ResultSet rs = stmt.executeQuery(SENTAKU_TOROKU_SEQ_SQL);
		while (rs.next()) {
		    seqCounter = rs.getString("counter");
		}
	    } finally {
		stmt.close();
	    }

	    //フロント設定MRをカウント
	    int mrCount = 0;
	    PreparedStatement pstmt = conn.prepareStatement(DR_MR_FRONT_COUNT_SQL);

	    try {
		pstmt.setString(1, drID);

		ResultSet rs = pstmt.executeQuery();

		if (rs.next()) {
		    mrCount = rs.getInt("counter");
		}
	    } finally {
		pstmt.close();
	    }

	    if (mrCount == 0) {
		sentakuKbn = "1";
	    } else if (mrCount == 1) {
		sentakuKbn = replaceSentakuKbn(drID, 1);
	    } else if (mrCount == 2) {
		sentakuKbn = replaceSentakuKbn(drID, 2);
	    }

	    //選択登録の書出しＳＱＬ分	
	    pstmt = conn.prepareStatement(SENTAKU_TOUROKU_INSERT);

	    try {
		DoctorInfoManager docManager = new DoctorInfoManager(conn);//1106 y-yamada add 
		String companyCD = docManager.getCompanyCD(mrID);//1106 y-yamada add カンパニーコードを取得
		String targetRank = docManager.getTargetRank(companyCD);//1106 y-yamada add ターゲットランクの取得
		
		pstmt.setString(1, drID);
		pstmt.setString(2, mrID);
		pstmt.setString(3, seqCounter);
		pstmt.setString(4, sentakuKbn);
		pstmt.setString(5, targetRank);//1106 y-yamada add
//		pstmt.setString(5, drID);
		pstmt.setString(6, drID); //1106 y-yamada add
		pstmt.execute();
	    } finally {
		pstmt.close();
	    }

	    //医師情報のＳＱＬ文
	    pstmt = conn.prepareStatement(DOCTOR_UPDATE);
	    try {
		pstmt.setString(1, drID);
		pstmt.executeUpdate();
	    } finally {
		pstmt.close();
	    }

	    conn.commit();
	} catch (SQLException e) {
	    System.err.println("DR-ID = " + drID);
	    System.err.println("MR-ID = " + mrID);
	    throw new MrException(e);
	}
    }

    /**
     *　アンケートMRが存在すればシフトする
     */
    private String replaceSentakuKbn(String drId,
				     int count) throws SQLException {
	TantoInfo info = null;
	String sentakuKbn = null;
	PreparedStatement pstmt = conn.prepareStatement(ENQUETE_MR_SQL);

	//アンケートMRの存在チェック
	try {
	    pstmt.setString(1, drId);
	    
	    ResultSet rs = pstmt.executeQuery();

	    if (rs.next()) {
		info = new TantoInfo();
		info.setDrID(rs.getString("dr_id"));
		info.setMrID(rs.getString("mr_id"));
		info.setSentakuKbn(rs.getString("sentaku_kbn"));
	    }
	} finally {
	    pstmt.close();
	}

	//フロントに設定されている場合ははずす
	if (info != null) {
	    pstmt = conn.prepareStatement(ENQUETE_MR_REPLACE_SQL);

	    try {
		if (count == 2) {
		    //二人の場合ははずす
		    pstmt.setString(1, "3");
		    sentakuKbn = "2";
		} else {
		    //一人の場合は右へ
		    pstmt.setString(1, "2");
		    sentakuKbn = "1";
		}
		pstmt.setString(2, info.getDrID());
		pstmt.setString(3, info.getMrID());

		int count1 = pstmt.executeUpdate();
	    } finally {
		pstmt.close();
	    }
	} else {
	    sentakuKbn = "3";
	}

	return sentakuKbn;
    }

    /**
     * <h3>選択登録履歴の書出し</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/07/01 午後 03:50:50)
     * @param drID java.lang.String
     * @param mrID java.lang.String
     */
    public void insertSentakuTourokuHist(String drID, String mrID) {
	try {
		
	    conn.setAutoCommit(false);
		
	    //選択登録履歴の書出しＳＱＬ分	
	    PreparedStatement pstmt = conn.prepareStatement(SENTAKU_TOUROKU_HIST_INSERT);
	    try {
		pstmt.setString(1, drID);
		pstmt.setString(2, mrID);
		pstmt.execute();
	    } finally {
		//rs.close();
		pstmt.close();
	    }


	    //選択登録の削除ＳＱＬ文
	    String delsql = SENTAKU_TOUTOKU_DELETE;
	    pstmt = conn.prepareStatement(delsql);
	    try {
		pstmt.setString(1, drID);
		pstmt.setString(2, mrID);
		pstmt.executeUpdate();
	    } finally {
		//rs.close();
		pstmt.close();
	    }

	    conn.commit();
		
	} catch (SQLException e) {
	    throw new MrException(e);
	}
    }

    /**
     * <h3>医師メモの更新</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/07/01 午後 03:00:38)
     * @param drID java.lang.String
     * @param mrID java.lang.String
     * @param drMemo java.lang.String
     */
    public void updateDrMemo(String drID, String mrID, String drMemo) {
	try {
	    conn.setAutoCommit(false);
		
	    //メモ更新	
	    String updsql = DR_MEMO_UPDATE;
	    PreparedStatement pstmt = conn.prepareStatement(updsql);
	    try {
		pstmt.setString(1, drMemo);
		pstmt.setString(2, drID);
		pstmt.setString(3, mrID);
		pstmt.executeUpdate();
	    } finally {
		pstmt.close();
	    }
		
	    conn.commit();
	} catch (SQLException e) {
	    throw new MrException(e);
	}
    }

    /**
     * <h3>医師プロフィールの更新</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/07/01 午前 12:32:14)
     * @param tantoinfo jp.ne.sonet.medipro.mr.server.entity.TantoInfo
     * @param drID java.lang.String
     * @param mrID java.lang.String
     */
    public void updateDrProfile(TantoInfo tantoinfo, String drID, String mrID) {
	try {
	    conn.setAutoCommit(false);
		
	    //医師プロフィール更新	
	    String updsql = DR_PROFILE_UPDATE;
	    PreparedStatement pstmt = conn.prepareStatement(updsql);
	    try {
		pstmt.setString(1, tantoinfo.getName());
		pstmt.setString(2, tantoinfo.getKinmusaki());
		pstmt.setString(3, tantoinfo.getMakerCustID());
		pstmt.setString(4, tantoinfo.getMakerShisetsuID());
		pstmt.setString(5, tantoinfo.getTargetRank());
		pstmt.setString(6, tantoinfo.getSyokusyu());
		pstmt.setString(7, tantoinfo.getSenmon1());
		pstmt.setString(8, tantoinfo.getSenmon2());
		pstmt.setString(9, tantoinfo.getSenmon3());
		pstmt.setString(10, tantoinfo.getYakusyoku());
		pstmt.setString(11, tantoinfo.getSotsugyoDaigaku());
		pstmt.setString(12, tantoinfo.getSotsugyoYear());
		pstmt.setString(13, tantoinfo.getSyumi());
		pstmt.setString(14, tantoinfo.getSonota());
		pstmt.setString(15, drID);
		pstmt.setString(16, mrID);
		pstmt.executeUpdate();
	    } finally {
		pstmt.close();
	    }

	    conn.commit();
	} catch (SQLException e) {
	    throw new MrException(e);
	}
    }

    /**
     * <h3>フロント最上段の更新</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (00/07/01 午前 12:32:14)
     * @param drID java.lang.String
     * @param liftmrID java.lang.String
     * @param RightmrID java.lang.String
     */
    public void updateSentaku(String drID, String liftmrID, String RightmrID) {
	try {
	    conn.setAutoCommit(false);
		
	    //全ＭＲ更新	
	    String updsql = SENTAKU_UPDATE + "WHERE dr_id = ? ";
	    PreparedStatement pstmt = conn.prepareStatement(updsql);
	    try {
		pstmt.setString(1, "3");
		pstmt.setString(2, drID);
		pstmt.executeUpdate();
	    } finally {
		pstmt.close();
	    }

	    //ＭＲ左更新
	    updsql = SENTAKU_UPDATE + "WHERE dr_id = ? AND mr_id = ? ";
	    pstmt = conn.prepareStatement(updsql);
	    try {
		pstmt.setString(1, "1");
		pstmt.setString(2, drID);
		pstmt.setString(3, liftmrID);
		pstmt.executeUpdate();
	    } finally {
		pstmt.close();
	    }

	    //ＭＲ右更新
	    updsql = SENTAKU_UPDATE + "WHERE dr_id = ? AND mr_id = ? ";
	    pstmt = conn.prepareStatement(updsql);
	    try {
		pstmt.setString(1, "2");
		pstmt.setString(2, drID);
		pstmt.setString(3, RightmrID);
		pstmt.executeUpdate();
	    } finally {
		pstmt.close();
	    }
		
	    conn.commit();
	} catch (SQLException e) {
	    throw new MrException(e);
	}
    }

    /**
     * 指定したDRとMRに担当関係があるか検査する.
     * @param  mrId 対象とするMRID
     * @param  drId 対象とするDRID
     * @return 選択登録テーブルにレコードが存在したらtrue
     */
    public boolean hasRelation(String drId, String mrId) {
	int count = 0;
	
	try {
	    PreparedStatement pstmt = conn.prepareStatement(DR_MR_RELATION_SQL);

	    try {
		pstmt.setString(1, drId);
		pstmt.setString(2, mrId);

		ResultSet rs = pstmt.executeQuery();

		if (rs.next()) {
		    count = rs.getInt(1);
		}
	    } finally {
		pstmt.close();
	    }
	} catch (SQLException e) {
	    throw new MrException(e);
	}

	if (count > 0) {
	    return true;
	}

	return false;
    }

    /**
     * 指定したDRとMRに担当関係があるか検査する.
     * @param  mrId 対象とするMRID
     * @param  drId 対象とするDRID
     * @return 選択登録テーブルにレコードが存在したらtrue
     */
    public boolean hadRelation(String drId, String mrId) {
	int count = 0;
	
	try {
	    PreparedStatement pstmt = conn.prepareStatement(DR_MR_HAD_RELATION_SQL);
	    
	    try {
		pstmt.setString(1, drId);
		pstmt.setString(2, mrId);
		pstmt.setString(3, drId);
		pstmt.setString(4, mrId);

		ResultSet rs = pstmt.executeQuery();

		if (rs.next()) {
		    count = rs.getInt(1);
		}
	    } finally {
		pstmt.close();
	    }
	} catch (SQLException e) {
	    throw new MrException(e);
	}

	if (count > 0) {
	    return true;
	}

	return false;
    }

    /**
     * 前回ログインから関係が終了した医師一覧の名称を取得する.
     * @param  mrId 対象とするMRID
     * @return 選択登録履歴テーブルにレコードが存在したらtrue
     */
    public Vector getEndRelationList(String mrId) {
	Vector list = new Vector();
	
	try {
	    PreparedStatement pstmt = conn.prepareStatement(DR_MR_RELATION_END_SQL);
	    
	    try {
		pstmt.setString(1, mrId);

		ResultSet rs = pstmt.executeQuery();

		while (rs.next()) {
		    TantoInfo info = new TantoInfo();
		    info.setName(rs.getString("name"));
		    info.setEndYmd(rs.getDate("end_ymd"));
		    list.addElement(info);
		}
	    } finally {
		pstmt.close();
	    }
	} catch (SQLException e) {
	    throw new MrException(e);
	}

	return list;
    }
    
    
    
    /*****************************************
	mrId と drId から target_name を取得する
	1122 y-yamada add NO.58 (by sugiyama)
	参考文献 MrInfoManager getMrSpInfo
	******************************************/
   public String getTargetName(String targetrank,  String companyCD) {

	ResultSet rs;
	PreparedStatement pstmt;
	String targetName = "";
	try {
	    //ＳＱＬ文
	    String sqltxt = "SELECT target_name "
						+ "FROM   action  "
						+ "WHERE  target_rank = ?  "
						+ "AND    company_cd = ? ";
	
	    // SQL 文を設定
	    pstmt = conn.prepareStatement(sqltxt);
	    try {
			// パラメータを設定
			pstmt.setString(1, targetrank);
			pstmt.setString(2, companyCD);
			// SQL 実行
			rs   = pstmt.executeQuery();
			while( rs.next() )
			{
			 	targetName = rs.getString("target_name");
			}	
	    } finally {
		//rs.close();
		pstmt.close();
	    }
	} catch (SQLException e) {
	    throw new MrException(e);
	}

	return targetName;

    }


    
    
    
    
    
    
}
