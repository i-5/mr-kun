package jp.ne.sonet.medipro.mr.server.manager;

import java.sql.*;
import java.util.*;
import java.sql.Date;

import jp.ne.sonet.medipro.mr.common.exception.*;
import jp.ne.sonet.medipro.mr.common.util.*;
import jp.ne.sonet.medipro.mr.server.entity.*;

public class DrProfileInfoManager {
    static final String PLACE = "0000000001";
    static final String NETWORK = "0000000002";
    static final String WORK_TYPE = "0000000003";
    static final String CLINIC = "0000000004";
    static final String SPECIAL = "0000000005";
    static final String ACADEMIC = "0000000006";
    static final String COLLEGE = "0000000007";
    static final String FACULTY = "0000000008";
    static final String YEAR = "0000000009";
    static final String PREFECTURE = "0000000010";
    static final String REGION = "0000000011";
    static final String MEDIPRO_MEMBER = "0000000012";
    static final String SONET_MEMBER = "0000000013";
    static final String NEED_NOTIFY = "0000000014";
    static final String SHIKAKU = "0000000015";
    static final String ADDRESS = "0000000016";
    static final String TEL = "0000000017";
    static final String OFFICE_ADDRESS = "0000000018";
    static final String OFFICE_TEL = "0000000019";

    //挿入用SQL
    static final String PROFILE_INSERT_SQL
	= "INSERT INTO doctor_profile ("
	+ " dr_id"
	+ ",item_cd"
	+ ",seq"
	+ ",item"
	+ ",start_ymd"
	+ ") values ("
	+ " ?"
	+ ",?"
	+ ",?"
	+ ",?"
	+ ",SYSDATE"
	+ ")";

    //取得用SQL
    static final String PROFILE_GET_SQL
	= "SELECT"
	+ " item"
	+ " FROM doctor_profile"
	+ " WHERE"
	+ " dr_id = ?"
	+ " AND item_cd = ?";

    //削除用SQL
    static final String PROFILE_DELETE_SQL
	= "DELETE FROM doctor_profile"
	+ " WHERE"
	+ " dr_id = ?"
	+ " AND item_cd = ?";

    //医師情報更新SQL
    static final String DOCTOR_UPDATE_SQL
	= "UPDATE doctor SET"
	+ " name = ?"
	+ ",name_kana = ?"
	+ ",birthday = ?"
	+ ",email = ?"
	+ ",koumuin = ?"
	+ ",update_time = SYSDATE"
	+ " WHERE dr_id = ?";

    //医師アンケートポイント
    static final String DOCTOR_POINT_SQL
	= "UPDATE doctor SET "
	+ "point = point + (select to_number(naiyo1) "
	+                  "from constant_master "
	+                  "where constant_cd = 'ENQUETE_POINT')"
	+ " WHERE dr_id = ?";

    //医師情報取得SQL
    static final String DOCTOR_GET_SQL
	= "SELECT"
	+ " name"
	+ ",name_kana"
	+ ",to_char(birthday, 'YYYY') year"
	+ ",to_char(birthday, 'MM') month"
	+ ",to_char(birthday, 'DD') day"
	+ ",email"
	+ ",koumuin"
	+ " FROM doctor"
	+ " WHERE"
	+ " dr_id = ?";

    //医師情報追加SQL
    static final String DOCTOR_ADD_SQL
	= "INSERT INTO doctor ("
	+ " dr_id"
	+ ",system_cd"
	+ ",password"
	+ ",name"
	+ ",name_kana"
	+ ",birthday"
	+ ",email"
	+ ",point"
	+ ",mrkun_mishiyo_flg"
	+ ",koumuin"
	+ ",toroku_ymd"
	+ ",update_time"
	+ ",lastyear_point"
	+ ",ruiseki_mr"
	+ ") values ("
	+ " ?"//dr_id
	+ ",?"//system_cd
	+ ",NULL"//password 1106 y-yamada add 
//	+ ",?"//password
	+ ",?"//name
	+ ",?"//name_kana
	+ ",?"//birthday
	+ ",?"//email
	+ ",(select to_number(naiyo1) "
	+   "from constant_master "
	+   "where constant_cd = 'ENTRY_POINT')"//point
	+ ",0"//mrkun_mishiyo_flg
	+ ",?"//koumuin
	+ ",SYSDATE"//toroku_ymd
	+ ",SYSDATE"//update_time
	+ ",0"//lastyear_point
	+ ",0"//ruiseki_mr
	+ ")";

    //勤務先情報取得
    static final String KINMUSAKI_GET_SQL
	= "SELECT"
	+ " kinmusaki_name"
	+ " FROM kinmusaki"
	+ " WHERE dr_id = ?"
	+ " AND seq = '1'";
    
    //勤務先更新SQL
    static final String KINMUSAKI_UPDATE_SQL
	= "UPDATE kinmusaki SET"
	+ " kinmusaki_name = ?"
	+ " WHERE dr_id = ?"
	+ " AND seq = '1'";

    //勤務先追加SQL
    static final String KINMUSAKI_ADD_SQL
	= "INSERT INTO kinmusaki ("
	+ " dr_id"
	+ ",seq"
	+ ",kinmusaki_name"
	+ ",start_ymd"
	+ ",update_time"
	+ ") values ("
	+ " ?"
	+ ",1"
	+ ",?"
	+ ",SYSDATE"
	+ ",SYSDATE"
	+ ")";

    static final String GENGO_LIST_SQL
	= "SELECT"
	+ " gengo_cd"
	+ ",start_year"
	+ ",end_year"
	+ " FROM gengo";

    static final String NEXT_SYSTEM_CD_SQL
	= "SELECT TO_CHAR(system_cd.nextval ,'0000000000') next_seq FROM dual";

    static final String ENQUETE_END_SQL
	= "SELECT count(*)"
	+ " FROM doctor_profile"
	+ " WHERE"
	+ " dr_id = ?"
//	+ " AND item_cd = '" + SONET_MEMBER + "'";
	+ " AND item_cd = '" + SHIKAKU + "'";

    protected Connection connection = null;

    public DrProfileInfoManager(Connection initConnection) {
	this.connection = initConnection;
    }

    /**
     *　指定したアイテムを削除し、変わりの値を挿入します.
     */
    private int set(String drId,
		    String itemCd,
		    String seq,
		    String item) throws SQLException {
	PreparedStatement pstmt = null;

	try {
	    pstmt = connection.prepareStatement(PROFILE_DELETE_SQL);

	    pstmt.setString(1, drId);
	    pstmt.setString(2, itemCd);

	    pstmt.executeUpdate();
	} finally {
	    pstmt.close();
	}

	try {
	    pstmt = connection.prepareStatement(PROFILE_INSERT_SQL);
	    
	    pstmt.setString(1, drId);
	    pstmt.setString(2, itemCd);
	    pstmt.setString(3, seq);
	    pstmt.setString(4, item);

	    return pstmt.executeUpdate();
	} finally {
	    pstmt.close();
	}
    }

    /**
     *　指定したアイテムを削除し、変わりの値を挿入します(複数).
     */
    private int sets(String drId,
		     String itemCd,
		     Vector itemList) throws SQLException {
	PreparedStatement pstmt = null;

	try {
	    pstmt = connection.prepareStatement(PROFILE_DELETE_SQL);

	    pstmt.setString(1, drId);
	    pstmt.setString(2, itemCd);

	    pstmt.executeUpdate();
	} finally {
	    pstmt.close();
	}

	int count = 0;

	try {
	    pstmt = connection.prepareStatement(PROFILE_INSERT_SQL);
	    
	    for (int i = 0; i < itemList.size(); i++) {
		pstmt.setString(1, drId);
		pstmt.setString(2, itemCd);
		pstmt.setString(3, new Integer(i+1).toString());
		pstmt.setString(4, (String)itemList.elementAt(i));

		count += pstmt.executeUpdate();
	    }
	} finally {
	    pstmt.close();
	}

	return count;
    }

    /**
     * 医師情報の設定
     */
    public void set(DoctorProperty prop) {
	String drId = prop.getDrId();
	String seq = "1";
	    
	try {
	    connection.setAutoCommit(false);

	    PreparedStatement pstmt = null;

	    ////////////////////////////////////////10/02
	    //医師テーブル更新
	    try {
		pstmt = connection.prepareStatement(DOCTOR_UPDATE_SQL);
		pstmt.setString(1, prop.getName());
		pstmt.setString(2, prop.getNameKana());
		pstmt.setDate(3, getBirthDate(prop));
		pstmt.setString(4, prop.getEmail());
		pstmt.setString(5, prop.getKoumuin().equals("") ? null : "1");
		pstmt.setString(6, prop.getDrId());

		pstmt.executeUpdate();
	    } finally {
		pstmt.close();
	    }

	    //勤務先更新
	    try {
		pstmt = connection.prepareStatement(KINMUSAKI_UPDATE_SQL);
		pstmt.setString(1, prop.getKinmusaki());
		pstmt.setString(2, prop.getDrId());

		pstmt.executeUpdate();
	    } finally {
		pstmt.close();
	    }
	    ////////////////////////////////////////10/02

	    //Profileの置き換え
	    this.set(drId, PLACE, seq, prop.getPlace());
	    this.set(drId, NETWORK, seq, prop.getNetwork());
	    this.set(drId, WORK_TYPE, seq, prop.getWorkType());
	    this.set(drId, CLINIC, seq, prop.getClinic());
	    this.set(drId, SHIKAKU, seq, prop.getShikaku());
	    this.sets(drId, SPECIAL, prop.getSpecialList());
	    this.sets(drId, ACADEMIC, prop.getAcademicList());
	    this.set(drId, COLLEGE, seq, prop.getCollege());
	    this.set(drId, FACULTY, seq, prop.getFaculty());
	    this.set(drId, YEAR, seq, prop.getYearAD());
	    this.set(drId, PREFECTURE, seq, prop.getPrefecture());
	    this.set(drId, REGION, seq, prop.getRegion());
	    this.set(drId, NEED_NOTIFY, seq, prop.getNeedNotify());

	    connection.commit();
	} catch (SQLException ex) {
	    try {
		connection.rollback();
	    } catch (SQLException e) { }
	    throw new MrException(ex);
	} finally {
	    try {
		connection.setAutoCommit(false);
	    } catch (SQLException e) { }
	}
    }

    public int insert(DoctorProperty prop) {
	int count = 0;
	try {
	    connection.setAutoCommit(false);

	    Statement stmt = connection.createStatement();
	    String nextSystemCd = null;

	    try {
		ResultSet rs = stmt.executeQuery(NEXT_SYSTEM_CD_SQL);
		if (rs.next()) {
		    nextSystemCd = rs.getString("next_seq").trim();
		}
	    } finally {
		stmt.close();
	    }

	    PreparedStatement pstmt = null;

	    //医師テーブル更新
	    try {
		pstmt = connection.prepareStatement(DOCTOR_ADD_SQL);
		pstmt.setString(1, prop.getDrId());
		pstmt.setString(2, nextSystemCd);
		pstmt.setString(3, prop.getName());//1107 y-yamada ココから変更
		pstmt.setString(4, prop.getNameKana());
		pstmt.setDate(5, getBirthDate(prop));
		pstmt.setString(6, prop.getEmail());
		pstmt.setString(7, prop.getKoumuin().equals("") ? null : "1");
		/*	1107 y-yamada del passwordに drIdを入れない	
		pstmt.setString(3, prop.getDrId()); 
		pstmt.setString(4, prop.getName());
		pstmt.setString(5, prop.getNameKana());
		pstmt.setDate(6, getBirthDate(prop));
		pstmt.setString(7, prop.getEmail());
		pstmt.setString(8, prop.getKoumuin().equals("") ? null : "1");
		 */

		count = pstmt.executeUpdate();
	    } finally {
		pstmt.close();
	    }

	    //勤務先更新
	    try {
		pstmt = connection.prepareStatement(KINMUSAKI_ADD_SQL);
		pstmt.setString(1, prop.getDrId());
		pstmt.setString(2, prop.getKinmusaki());

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
	} finally {
	    try {
		connection.setAutoCommit(false);
	    } catch (SQLException e) { }
	}

	return count;
    }

    public void setEnquete(DoctorProperty prop) {
	String drId = prop.getDrId();
	String seq = "1";
	    
	try {
	    connection.setAutoCommit(false);

	    PreparedStatement pstmt = null;

	    //医師テーブルポイントアップ
//	    try {
//		pstmt = connection.prepareStatement(DOCTOR_POINT_SQL);
//		pstmt.setString(1, prop.getDrId());
//
//		pstmt.executeUpdate();
//	    } finally {
//		pstmt.close();
//	    }

	    //Profileの置き換え
//	    this.set(drId, PLACE, seq, prop.getPlace());
//	    this.set(drId, NETWORK, seq, prop.getNetwork());
	    this.set(drId, WORK_TYPE, seq, prop.getWorkType());
	    this.set(drId, CLINIC, seq, prop.getClinic());
	    this.set(drId, SHIKAKU, seq, prop.getShikaku());
	    this.sets(drId, SPECIAL, prop.getSpecialList());
//	    this.sets(drId, ACADEMIC, prop.getAcademicList());
//	    this.set(drId, COLLEGE, seq, prop.getCollege());
//	    this.set(drId, FACULTY, seq, prop.getFaculty());
//	    this.set(drId, YEAR, seq, prop.getYearAD());
//	    this.set(drId, PREFECTURE, seq, prop.getPrefecture());
//	    this.set(drId, REGION, seq, prop.getRegion());
//	    this.set(drId, NEED_NOTIFY, seq, prop.getNeedNotify());
//	    this.set(drId, MEDIPRO_MEMBER, seq, prop.getMediproMember());
//	    this.set(drId, SONET_MEMBER, seq, prop.getSonetMember());
//	    this.set(drId, ADDRESS, seq, prop.getAddress());
//	    this.set(drId, TEL, seq, prop.getTel());
//	    this.set(drId, OFFICE_ADDRESS, seq, prop.getOfficeAddress());
//	    this.set(drId, OFFICE_TEL, seq, prop.getOfficeTel());

	    connection.commit();
	} catch (SQLException ex) {
	    try {
		connection.rollback();
	    } catch (SQLException e) { }
	    throw new MrException(ex);
	} finally {
	    try {
		connection.setAutoCommit(false);
	    } catch (SQLException e) { }
	}
    }

    /**
     *　指定したアイテム取得します.
     */
    private String get(String drId,
		       String itemCd,
		       String seq) throws SQLException {
	PreparedStatement pstmt = null;

	try {
	    pstmt = connection.prepareStatement(PROFILE_GET_SQL);
	    
	    pstmt.setString(1, drId);
	    pstmt.setString(2, itemCd);

	    ResultSet rs = pstmt.executeQuery();

	    if (rs.next()) {
		return rs.getString("item");
	    }

	} finally {
	    pstmt.close();
	}

	return "";
    }

    /**
     *　指定したアイテム取得します.
     */
    private Vector gets(String drId,
			String itemCd) throws SQLException {
	PreparedStatement pstmt = null;
	Vector list = new Vector();

	try {
	    pstmt = connection.prepareStatement(PROFILE_GET_SQL);
	    
	    pstmt.setString(1, drId);
	    pstmt.setString(2, itemCd);

	    ResultSet rs = pstmt.executeQuery();

	    while (rs.next()) {
		list.addElement(rs.getString("item"));
	    }

	} finally {
	    pstmt.close();
	}

	return list;
    }

    /**
     * 医師プロフィール情報の取得
     */
    public DoctorProperty getDoctorProperty(String drId) {
	DoctorProperty prop = new DoctorProperty(drId);

	try {
	    connection.setAutoCommit(false);
	    
	    PreparedStatement pstmt = null;

	    //医師テーブル
	    try {
		pstmt = connection.prepareStatement(DOCTOR_GET_SQL);
		pstmt.setString(1, drId);

		ResultSet rs = pstmt.executeQuery();

		if (rs.next()) {
		    prop.setName(rs.getString("name"));
		    prop.setNameKana(rs.getString("name_kana"));
		    try {
			prop.setBirthYear(rs.getString("year"));
			prop.setBirthMonth(new Integer(rs.getString("month")).toString());
			prop.setBirthDay(new Integer(rs.getString("day")).toString());
		    } catch (NumberFormatException e) {
			prop.setBirthYear("");
			prop.setBirthMonth("");
			prop.setBirthDay("");
		    }
		    prop.setEmail(rs.getString("email"));
		    prop.setKoumuin(rs.getString("koumuin"));
		}
	    } finally {
		pstmt.close();
	    }

	    //勤務先
	    try {
		pstmt = connection.prepareStatement(KINMUSAKI_GET_SQL);
		pstmt.setString(1, drId);

		ResultSet rs = pstmt.executeQuery();

		if (rs.next()) {
		    prop.setKinmusaki(rs.getString("kinmusaki_name"));
		}

	    } finally {
		pstmt.close();
	    }

	    String seq = "1";

	    Vector list = getGengoList();

	    //Profile
	    prop.setPlace(this.get(drId, PLACE, seq));
	    prop.setNetwork(this.get(drId, NETWORK, seq));
	    prop.setWorkType(this.get(drId, WORK_TYPE, seq));
	    prop.setClinic(this.get(drId, CLINIC, seq));
	    prop.setShikaku(this.get(drId, SHIKAKU, seq));
	    prop.setSpecialList(this.gets(drId, SPECIAL));
	    prop.setAcademicList(this.gets(drId, ACADEMIC));
	    prop.setCollege(this.get(drId, COLLEGE, seq));
	    prop.setFaculty(this.get(drId, FACULTY, seq));

	    String year = this.get(drId, YEAR, seq);
	    prop.setGengo(toGengo(year, list));
	    prop.setYear(toGengoYear(year, list));
	    prop.setYearAD(year);

	    prop.setPrefecture(this.get(drId, PREFECTURE, seq));
	    prop.setRegion(this.get(drId, REGION, seq));
	    prop.setNeedNotify(this.get(drId, NEED_NOTIFY, seq));

	    connection.commit();
	} catch (SQLException ex) {
	    try {
		connection.rollback();
	    } catch (SQLException e) { }
	    throw new MrException(ex);
	} finally {
	    try {
		connection.setAutoCommit(false);
	    } catch (SQLException e) { }
	}

	return prop;
    }

    /**
     * 生年月日の変換
     */
    private Date getBirthDate(DoctorProperty prop) {
	Calendar cal = Calendar.getInstance();
	cal.set(Integer.parseInt(prop.getBirthYear()),
		Integer.parseInt(prop.getBirthMonth())-1,
		Integer.parseInt(prop.getBirthDay()));
	
	return new Date(cal.getTime().getTime());
    }

    private String toGengo(String year, Vector list) {
	Enumeration e = list.elements();

	try {
	    int value = new Integer(year).intValue();

	    while (e.hasMoreElements()) {
		GengoInfo info = (GengoInfo)e.nextElement();
		if (info.startYear < value &&
		    info.endYear >= value) {
		    return info.gengoCd;
		}
	    }
	} catch (NumberFormatException nfe) {
	}

	return "";
    }

    private String toGengoYear(String year, Vector list) {
	Enumeration e = list.elements();

	try {
	    int value = new Integer(year).intValue();

	    while (e.hasMoreElements()) {
		GengoInfo info = (GengoInfo)e.nextElement();

		if (info.startYear < value &&
		    info.endYear >= value) {
		    return new Integer(value - info.startYear + 1).toString();
		}
	    }
	} catch (NumberFormatException nfe) {
	}

	return "";
    }

    private Vector getGengoList() throws SQLException {
	Statement stmt = null;
	Vector list = new Vector();

	try {
	    stmt = connection.createStatement();
	    
	    ResultSet rs = stmt.executeQuery(GENGO_LIST_SQL);

	    while (rs.next()) {
		GengoInfo info = new GengoInfo();
		info.gengoCd = rs.getString("gengo_cd");
		info.startYear = rs.getInt("start_year");
		info.endYear = rs.getInt("end_year");

		list.addElement(info);
	    }

	} finally {
	    stmt.close();
	}

	return list;
	
    }


    public boolean isEnqueteEnd(String drId) {
	int count = 0;

	try {
	    PreparedStatement pstmt = connection.prepareStatement(ENQUETE_END_SQL);
	    pstmt.setString(1, drId);
	    
	    ResultSet rs = pstmt.executeQuery();
	    
	    if (rs.next()) {
		count = rs.getInt(1);
	    }
	} catch (SQLException ex) {
	    throw new MrException(ex);
	}

	if (count > 0) {
	    return true;
	}

	return false;
    }
}

