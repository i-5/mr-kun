package jp.ne.sonet.medipro.wm.server.manager;

import java.util.*;
import java.sql.*;
import java.net.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.wm.common.*;
import jp.ne.sonet.medipro.wm.common.exception.*; 
import jp.ne.sonet.medipro.wm.server.entity.*;
import jp.ne.sonet.medipro.wm.server.session.*;


/**
 * <strong>�x�X�E�c�Ə����Ǘ�Manager�N���X</strong>
 * <br>�x�X�E�c�Ə�����DB����擾�܂���DB�X�V����.
 * @author: 
 * @version:
 */
public class BranchInfoManager {
    /////////////////////////////////////////////
    //constants
    //
    /** ��{SQL */
    private static final String BRANCH_INFO_MAINSQL = 
	"SELECT shiten.shiten_cd as s_shiten_cd, shiten_name, " +
	"eigyosyo_cd, eigyosyo_name " +
	"FROM shiten, eigyosyo " +
	"WHERE (shiten.delete_ymd is null AND " + 
	"eigyosyo.delete_ymd(+) is null) AND " + 
	"(shiten.shiten_cd = eigyosyo.shiten_cd(+) " +
	"AND shiten.company_cd = eigyosyo.company_cd(+)) " +
	"AND shiten.company_cd = ";

    /** �x�XSQL */
    private static final String BRANCH_INFO_SHITENSQL = 
	"select shiten.shiten_cd as s_shiten_cd, " + 
	"shiten_name, eigyosyo_cd, eigyosyo_name " + 
	"from shiten, eigyosyo " + 
	"where (shiten.delete_ymd is null and " + 
	"eigyosyo.delete_ymd(+) is null) and " + 
	"(shiten.shiten_cd = eigyosyo.shiten_cd(+)) and " +
	"shiten.shiten_cd = ";

    /** �c�Ə�SQL */
    private static final String BRANCH_INFO_EIGYOSYOSQL = 
	"select shiten.shiten_cd as s_shiten_cd, " + 
	"shiten_name, eigyosyo_cd, eigyosyo_name " + 
	"from shiten, eigyosyo " + 
	"where (shiten.delete_ymd is null and " + 
	"eigyosyo.delete_ymd is null) and " + 
	"(shiten.company_cd = eigyosyo.company_cd and " +
	"shiten.shiten_cd = eigyosyo.shiten_cd ) and " +
	"eigyosyo_cd = ";

    /** �l�q�̏�������x�X�E�c�Ə��擾SQL */
    private static final String BELONGING_MR = 
	"select shiten_cd, eigyosyo_cd " +
	"from mr " +
	"where delete_ymd is null and company_cd = ";

	/** �x�X�擾SQL(��ЃR�[�h�w��) */
    private static final String BRANCH_LIST_SQL = 
	"SELECT shiten_cd as s_shiten_cd, shiten_name " + 
	"FROM shiten " + 
	"WHERE delete_ymd is null and company_cd = ? " + 
	"ORDER BY shiten_cd";

    /** �x�X�擾SQL(�x�X�R�[�h�w��) */
    private static final String BRANCH_SQL = 
	"SELECT shiten_cd as s_shiten_cd, shiten_name " + 
	"FROM shiten " + 
	"WHERE delete_ymd is null and shiten_cd = ? " + 
	"ORDER BY shiten_cd";

    /** �x�X�擾SQL(�c�Ə��R�[�h�w��) */
    private static final String BRANCH_OFFICESQL = 
	"SELECT shiten.shiten_cd as s_shiten_cd, shiten_name " + 
	"FROM shiten, eigyosyo " + 
	"WHERE (shiten.delete_ymd is null and " +
	"eigyosyo.delete_ymd is null) and " +
	"(shiten.company_cd = eigyosyo.company_cd and " +
	"shiten.shiten_cd = eigyosyo.shiten_cd) and eigyosyo_cd = ? ";
	
    /** �c�Ə��擾SQL(��ЃR�[�h�w��) */
    private static final String OFFICE_LIST_SQL = 
	"SELECT shiten_cd, eigyosyo_cd, eigyosyo_name " + 
	"FROM eigyosyo " + 
	"WHERE delete_ymd is null and company_cd = ? " + 
	"ORDER BY shiten_cd, eigyosyo_cd";

    /** �c�Ə��擾SQL(�x�X�R�[�h�w��) */
    private static final String OFFICE_LIST_BRANCHSQL = 
	"SELECT shiten_cd, eigyosyo_cd, eigyosyo_name " + 
	"FROM eigyosyo " + 
	"WHERE delete_ymd is null and shiten_cd = ? " + 
	"ORDER BY eigyosyo_cd";

    /** �c�Ə��擾SQL(�c�Ə��R�[�h�w��) */
    private static final String OFFICE_SQL = 
	"SELECT shiten_cd, eigyosyo_cd, eigyosyo_name " + 
	"FROM eigyosyo " + 
	"WHERE delete_ymd is null and eigyosyo_cd = ? ";
		
	/** �x�X�X�VSQL */
    private static final String BRANCH_UPDATE = 
	"UPDATE shiten set shiten_name = ?, update_userid = ?, " +
	"update_time = sysdate " +
	"WHERE shiten_cd = ? ";
    /** �c�Ə��X�VSQL */
    private static final String OFFICE_UPDATE = 
	"UPDATE eigyosyo set eigyosyo_name = ?, update_userid = ?, " +
	"update_time = sysdate " +
	"WHERE eigyosyo_cd = ? ";

    /** �x�X�ǉ�SQL */
    private static final String SHITEN_INSERT = 
	"INSERT INTO shiten VALUES(?, ?, ?, ?, SYSDATE, null) ";
		
    /** �c�Ə��ǉ�SQL */
    private static final String EIGYOSYO_INSERT = 
	"INSERT INTO eigyosyo VALUES(?, ?, ?, ?, ?, SYSDATE, null) ";
	
	/** �x�X�폜SQL */
    private static final String SHITEN_DELETE = 
	"UPDATE shiten set delete_ymd = sysdate " +
	"WHERE shiten_cd IN ";

    /** �c�Ə��폜 */
    private static final String EIGYOSYO_DELETE = 
	"UPDATE eigyosyo set delete_ymd = sysdate " +
	"WHERE eigyosyo_cd IN ";

    /** �x�X�V�[�P���X�擾 */
    private static final String BRANCH_SEQUENCE = 
	"SELECT to_char(shiten_cd.nextval, '0000000000') " +
	"as shiten_cd FROM dual";

    /** �c�Ə��V�[�P���X�擾 */
    private static final String OFFICE_SEQUENCE = 
	"SELECT to_char(eigyosyo_cd.nextval, '0000000000') " +
	"as eigyosyo_cd FROM dual";

    /////////////////////////////////////////////
    //class variables
    //
    protected Connection conn;
    private Common common;
    private BranchListSession brses;
    private BranchUpdateSession buses;
    private String comCD;				// ��ЃR�[�h
    private String mrID;				// MRID
    private String branchCD;			// �x�X�R�[�h
    private String officeCD;			// �c�Ə��R�[�h
    private String masterFlg;			// �}�X�^�[�t���O
    private String authorityOrg;		// �}�X�^�[����(�g�D)

    /////////////////////////////////////////////
    //constructors
    //
    /**
     * �R���X�g���N�^.
     * @param conn Connection
     * @param session HttpSession
     */
    public BranchInfoManager(Connection conn, HttpSession session) {
	this.conn = conn;
	// �Z�b�V�����擾
	common = (Common) session.getValue(SysCnst.KEY_COMMON_SESSION);
	brses = (BranchListSession) session.getValue(SysCnst.KEY_BRANCHLIST_SESSION);
	buses = (BranchUpdateSession) session.getValue(SysCnst.KEY_BRANCHUPDATE_SESSION);
	// ��ЃR�[�h�擾
	comCD = common.getCompanyCd();
	// MRID�擾
	mrID = common.getMrId();
	// �x�XCD�擾
	branchCD = common.getShitenCd();
	// �c�Ə�CD�擾
	officeCD = common.getEigyosyoCd();
	// �}�X�^�[�t���O�̎擾
	masterFlg = common.getMasterFlg();
	// �}�X�^�[����(�g�D)�̎擾
	authorityOrg = common.getMasterKengenSoshiki();

    }

    /////////////////////////////////////////////
    //class methods
    //
    /**
     * �x�X�E�c�Ə����(�\�[�g�w��)�̎擾.
     * @return Vector
     */
    public Vector getBranchInfo() {

	ResultSet	rs;
	Statement	stmt;
	Vector		branchList;
	String		sql;
	int			cnt;
	int			srowNum;	// �y�[�W�J�n�s
	int			maxRow;		// �y�[�W�ő�s��
		
	Vector mrBranchList = new Vector();	// MR�����x�X���X�g
	Vector mrOfficeList = new Vector();	// MR�����c�Ə����X�g
		
	try {
	    // MR�����x�X�E�c�Ə��擾SQL��
	    sql = BELONGING_MR;
	    sql += "'" + comCD + "'";
	    if (SysCnst.DEBUG) {
		System.out.println(sql);
	    }
	    stmt = conn.createStatement();
	    try {
		rs = stmt.executeQuery(sql);
		while (rs.next()) {
		    mrBranchList.add(rs.getString("shiten_cd"));
		    mrOfficeList.add(rs.getString("eigyosyo_cd"));
		}
	    }
	    catch (SQLException e) {
		throw new WmException(e);
	    }
	    finally {
		stmt.close();
	    }
	}
	catch (SQLException e) {
	    throw new WmException(e);
	}
	// �擪�s�ԍ��擾
	srowNum = brses.getCurrentRow();
	// �y�[�W�ő�s���擾
	maxRow = common.getShitenLine();
		
	branchList = new Vector();
		
		// �\�[�g�L�[�̎擾
	String sortKey = brses.getSortKey();
	// �\�[�g���̎擾
	String order = brses.getOrder();
	//System.out.println("masterflg:"+masterFlg);
	if (masterFlg.equals(SysCnst.FLG_MASTER_WEB)) {
	    // �E�F�u�}�X�^
	    sql = BRANCH_INFO_MAINSQL;
	    sql += "'" + comCD + "'";
	    sql += " ORDER BY ";
	    sql += sortKey + order;
	}
	else if (authorityOrg.equals(SysCnst.FLG_AUTHORITY_BRANCH)) {
	    // �T�u�}�X�^(�x�X)
	    sql = BRANCH_INFO_SHITENSQL;
	    sql += "'" + branchCD + "'";
	    sql += " ORDER BY ";
	    sql += sortKey + order;
	}
	else if (authorityOrg.equals(SysCnst.FLG_AUTHORITY_OFFICE)) {
	    // �T�u�}�X�^(�c�Ə�)
	    sql = BRANCH_INFO_EIGYOSYOSQL;
	    sql += "'" + officeCD + "'";
	}
	else {
	    // ��L�ȊO
	    return branchList;
	}
	try {
	    stmt = conn.createStatement();
	    try {
				// SQL ���s
		if (SysCnst.DEBUG) {
		    System.err.println(sql);
		}
		rs   = stmt.executeQuery(sql);
		cnt = 0;
		while (rs.next()) {
		    BranchInfo branch = new BranchInfo();
		    String shitenCD = rs.getString("s_shiten_cd");
		    String eigyosyoCD = rs.getString("eigyosyo_cd");
		    branch.setShitenCD(shitenCD);
		    branch.setShitenName(rs.getString("shiten_name"));
		    String eigyosyoName = rs.getString("eigyosyo_name");
		    branch.setMrFlg(false);
		    if (eigyosyoName == null) {
			// �x�X�ɑ�����c�Ə����Ȃ��ꍇ
			eigyosyoName = "-";
			eigyosyoCD = "nooffice";
			branch.setShitenFlg(true);
			for (int i = 0; i < mrBranchList.size(); i++) {
			    if (mrBranchList.elementAt(i) != null) {
				if (mrBranchList.elementAt(i).equals(shitenCD)) {
				    // ���̎x�X�ɂl�q���������Ă���΃t���O�Z�b�g
				    branch.setMrFlg(true);
				    break;
				}
			    }
			}
		    }
		    else {
			branch.setShitenFlg(false);
			for (int j = 0; j < mrOfficeList.size(); j++) {
			    if (mrOfficeList.elementAt(j) != null) {
				if (mrOfficeList.elementAt(j).equals(eigyosyoCD)) {
				    // ���̉c�Ə��ɂl�q���������Ă���΃t���O�Z�b�g
				    branch.setMrFlg(true);
				    break;
				}
			    }
			}
		    }
		    branch.setEigyosyoCD(eigyosyoCD);
		    branch.setEigyosyoName(eigyosyoName);
		    // �y�[�W�J�n�s����y�[�W�\���s���̂݊i�[����
		    if (cnt >= srowNum - 1 && 
			cnt < srowNum + maxRow - 1) {
			//System.out.println("*** Add BranchList! cnt = " + cnt);
			branchList.add(branch);
		    }
		    cnt++;
		}
				// �ő�s���̃Z�b�g
		brses.setMaxRow(cnt);
	    }
	    catch (SQLException e) {
		throw new WmException(e);
	    }
	    finally {
		stmt.close();
	    }
	}
	catch (SQLException e) 
	    {
		throw new WmException(e);
	    }
	return branchList;
    }

    /**
     * �x�X���X�g�̎擾.
     * @return Vector
     */
    public Vector getBranchSet() {

	ResultSet	rs;
	String		sql;
	PreparedStatement	prest;
	Vector branchList = new Vector();
		
	try {	
	    if (masterFlg.equals(SysCnst.FLG_MASTER_WEB)) {
				// �E�F�u�}�X�^
		sql = BRANCH_LIST_SQL;
		prest = conn.prepareStatement(sql);
		prest.setString(1, comCD);
				
	    } else if (authorityOrg.equals(SysCnst.FLG_AUTHORITY_BRANCH)) {
				// �T�u�}�X�^(�x�X)
		sql = BRANCH_SQL;
		prest = conn.prepareStatement(sql);
		prest.setString(1, branchCD);
				
	    } else if (authorityOrg.equals(SysCnst.FLG_AUTHORITY_OFFICE)) {
				// �T�u�}�X�^(�c�Ə�)
		sql = BRANCH_OFFICESQL;
		prest = conn.prepareStatement(sql);
		prest.setString(1, officeCD);
		if (SysCnst.DEBUG) {
		    System.out.println(sql);
		    System.out.println("officeCD:"+officeCD);
		}
	    } else {
		return branchList;
	    }
	    try {
		rs = prest.executeQuery();
		while (rs.next()) {
		    BranchInfo branch = new BranchInfo();
		    branch.setShitenCD(rs.getString("s_shiten_cd"));
		    branch.setShitenName(rs.getString("shiten_name"));
		    branchList.add(branch);
		    if (masterFlg.equals(SysCnst.FLG_MASTER_SUB)) {
			buses.setBranchCD(branch.getShitenCD());
		    }
		    //					if (masterFlg.equals(SysCnst.FLG_MASTER_SUB)) {
		    //						buses.setBranchName(branch.getShitenName());
		    //					}
		}
	    }
	    catch (SQLException e) {
		throw new WmException(e);
	    }
	    finally {
		prest.close();
	    }
	}
	catch (SQLException e) {
	    throw new WmException(e);
	}
	return branchList;
    }

    /**
     * �c�Ə����X�g�̎擾.
     * @return Vector
     * @param branchCD String
     */
    public Vector getOfficeSet(String shitenCD) {

	ResultSet	rs;
	Vector		officeList = new Vector();
	String		sql;
	PreparedStatement	prest;
		
	try {
	    if (masterFlg.equals(SysCnst.FLG_MASTER_WEB)) {
				// �E�F�u�}�X�^
		sql = OFFICE_LIST_BRANCHSQL;
		prest = conn.prepareStatement(sql);
		prest.setString(1, shitenCD);
				
	    } else if (authorityOrg.equals(SysCnst.FLG_AUTHORITY_BRANCH)) {
				// �x�X�̃T�u�}�X�^
		if (shitenCD == null) {
		    shitenCD = branchCD;
		}
		sql = OFFICE_LIST_BRANCHSQL;
		prest = conn.prepareStatement(sql);
		prest.setString(1, shitenCD);
	    } else if (authorityOrg.equals(SysCnst.FLG_AUTHORITY_OFFICE)) {
				// �T�u�}�X�^(�c�Ə�)
		sql = OFFICE_SQL;
		prest = conn.prepareStatement(sql);
		prest.setString(1, officeCD);
				
	    } else {
		return officeList;
	    }
	    try {
		rs = prest.executeQuery();
		while (rs.next()) {
		    BranchInfo branch = new BranchInfo();
		    branch.setShitenCD(rs.getString("shiten_cd"));
		    branch.setEigyosyoCD(rs.getString("eigyosyo_cd"));
		    branch.setEigyosyoName(rs.getString("eigyosyo_name"));
		    officeList.add(branch);
		}
	    }
	    catch (SQLException e) {
		throw new WmException(e);
	    }
	    finally {
		prest.close();
	    }
	}
	catch (SQLException e) {
	    throw new WmException(e);
	}
	return officeList;
    }

    /**
     * �x�X�E�c�Ə��폜.
     * @version:
     */
    public void deleteOffice() {

	ResultSet	rs;
	String		sql;
	Statement	stmt;
	Vector		branchList;
	Vector		officeList;
	int			i;
		
	// �폜����x�X�R�[�h�擾
	branchList = (Vector) (brses.getDeleteBranch().clone());
	// �폜����c�Ə��R�[�h�擾
	officeList = (Vector) (brses.getDeleteOffice().clone());
	// �`�F�b�N�{�b�N�X�̏�ԃ��X�g�N���A
	brses.crearCheckValue();
	if (SysCnst.DEBUG) {
	    for (int j = 0; j < branchList.size(); j++){
		System.out.println("delete Branch :"+ branchList.elementAt(j));
	    }
	    for ( int k = 0; k < officeList.size(); k++){
		System.out.println("delete Office :"+ officeList.elementAt(k));
	    }
	}
	try {
	    if (branchList.size() != 0) {
				// �x�X�폜SQL��
		sql = SHITEN_DELETE;
		sql += "(";
		for (i = 0; i < branchList.size(); i++) {
		    if (i != 0) {
			sql += ",";
		    }
		    sql += "'" + branchList.elementAt(i) + "'";
		}
		sql += ")";
		if (SysCnst.DEBUG) {
		    System.out.println(sql);
		}
		stmt = conn.createStatement();
			
		try {
		    conn.setAutoCommit(false);
		    stmt.executeUpdate(sql);
		} 
		catch (SQLException e) {
		    conn.rollback();
		    throw new WmException(e);
		}
		finally {
		    conn.commit();
		    stmt.close();
		}
	    }
	    if (officeList.size() != 0) {
				// �c�Ə��폜SQL��
		sql = EIGYOSYO_DELETE;
		sql += "(";
		for (i = 0; i < officeList.size(); i++) {
		    if (i != 0) {
			sql += ",";
		    }
		    sql += "'" + officeList.elementAt(i) + "'";
		}
		sql += ")";
		if (SysCnst.DEBUG) {
		    System.out.println(sql);
		}
		stmt = conn.createStatement();
			
		try {
		    conn.setAutoCommit(false);
		    stmt.executeUpdate(sql);
		} 
		catch (SQLException e) {
		    conn.rollback();
		    throw new WmException(e);
		}
		finally {
		    conn.commit();
		    stmt.close();
		}
	    }
	}
	catch (SQLException e) 
	    {
		throw new WmException(e);
	    }
    }

    /**
     * �x�X�ǉ�.
     */
    public void insertBranch() {

	String		sql;
	PreparedStatement	prest;
	Statement	stmt;
	ResultSet	rs;
		
	String branchName = buses.getBranchName();
	String branchCD = null;
		
	// �V�K�ǉ��x�X�R�[�h�擾����
	try {
	    sql = BRANCH_SEQUENCE;
	    stmt = conn.createStatement();
	    rs = stmt.executeQuery(sql);
	    while (rs.next()) {
		branchCD = (rs.getString("shiten_cd")).trim();
	    }
	}
	catch (SQLException e) {
	    throw new WmException(e);
	}
		
	buses.setBranchCD(branchCD);
		
	if (branchCD != null) {
	    try {
		sql = SHITEN_INSERT;
		prest = conn.prepareStatement(sql);
		if (SysCnst.DEBUG) {
		    System.out.println(sql);
		    System.out.println("branchCD:"+branchCD);
		    System.out.println("comCD:"+comCD);
		    System.out.println("branchName:"+branchName);
		    System.out.println("mrID:"+mrID);
		}
		try {
		    prest.setString(1, branchCD);
		    prest.setString(2, comCD);
		    prest.setString(3, branchName);
		    prest.setString(4, mrID);
					
		    conn.setAutoCommit(false);
		    prest.executeUpdate();
		}
		catch (SQLException e) {
		    conn.rollback();
		    throw new WmException(e);
		}
		finally {
		    conn.commit();
		    prest.close();
		}
	    }
	    catch (SQLException e) {
		throw new WmException(e);
	    }
	}
    }

    /**
     * �c�Ə��ǉ�.
     */
    public void insertOffice() {

	String		sql;
	PreparedStatement	prest;
	Statement	stmt;
	ResultSet	rs;
		
	String branchCD = buses.getBranchCD();
	String officeName = buses.getOfficeName();
	String officeCD  = null ;
		
	// �V�K�ǉ��c�Ə��R�[�h�擾����
	try {
	    sql = OFFICE_SEQUENCE;
	    stmt = conn.createStatement();
	    rs = stmt.executeQuery(sql);
	    while (rs.next()) {
		officeCD = (rs.getString("eigyosyo_cd")).trim();
	    }
	}
	catch (SQLException e) {
	    throw new WmException(e);
	}
		
	buses.setOfficeCD(officeCD);
	if (officeCD != null) {
	    try {
		sql = EIGYOSYO_INSERT;
		prest = conn.prepareStatement(sql);
		if (SysCnst.DEBUG) {
		    System.out.println(sql);
		    System.out.println("officeCD:"+officeCD);
		    System.out.println("comCD:"+comCD);
		    System.out.println("branchCD:"+branchCD);
		    System.out.println("officeName:"+officeName);
		    System.out.println("mrID:"+mrID);
		}
		try {
		    prest.setString(1, officeCD);
		    prest.setString(2, comCD);
		    prest.setString(3, branchCD);
		    prest.setString(4, officeName);
		    prest.setString(5, mrID);
					
		    conn.setAutoCommit(false);
		    prest.executeUpdate();
		}
		catch (SQLException e) {
		    conn.rollback();
		    throw new WmException(e);
		}
		finally {
		    conn.commit();
		    prest.close();
		}
	    }
	    catch (SQLException e) {
		throw new WmException(e);
	    }
	}
    }

    /**
     * �x�X�X�V.
     */
    public void updateShiten() {

	String		sql;
	PreparedStatement	prest;
	String branchName = buses.getBranchName();
	String branchCD = buses.getBranchCD();
		
	try {
	    sql = BRANCH_UPDATE;
	    prest = conn.prepareStatement(sql);
	    if (SysCnst.DEBUG) {
		System.out.println(sql);
		System.out.println("branchName:"+branchName);
		System.out.println("mrID:"+mrID);
		System.out.println("branchCD:"+branchCD);
	    }
	    try {
		prest.setString(1, branchName);
		prest.setString(2, mrID);
		prest.setString(3, branchCD);
				
		conn.setAutoCommit(false);
		prest.executeUpdate();
	    }
	    catch (SQLException e) {
		conn.rollback();
		throw new WmException(e);
	    }
	    finally {
		conn.commit();
		prest.close();
	    }
	}
	catch (SQLException e) {
	    throw new WmException(e);
	}
    }

    /**
     * �c�Ə��X�V.
     */
    public void updateEigyosyo() {

	String		sql;
	PreparedStatement	prest;
		
	String officeName = buses.getOfficeName();
	String officeCD = buses.getOfficeCD();
		
	try {
	    sql = OFFICE_UPDATE;
	    prest = conn.prepareStatement(sql);
	    if (SysCnst.DEBUG) {
		System.out.println(sql);
		System.out.println("officeName:"+officeName);
		System.out.println("mrID:"+mrID);
		System.out.println("officeCD:"+officeCD);
	    }
	    try {
		prest.setString(1, officeName);
		prest.setString(2, mrID);
		prest.setString(3, officeCD);
				
		conn.setAutoCommit(false);
		prest.executeUpdate();
	    }
	    catch (SQLException e) {
		conn.rollback();
		throw new WmException(e);
	    }
	    finally {
		conn.commit();
		prest.close();
	    }
	}
	catch (SQLException e) {
	    throw new WmException(e);
	}
    }
}
