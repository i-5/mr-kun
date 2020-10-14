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
 * <h3>�����N���C�u�������Ǘ�</h3>
 * 
 * <br>
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/21 �ߌ� 08:57:47)
 * @author: 
 */
public class LinkLibInfoManager {
    protected Connection conn;

    protected static final String LINK_MAINSQL
	= "SELECT link_cd, url, honbun_text, picture, niagai_link_kbn, bun.link_bunrui_cd bun_link_bunrui_cd, bunrui_name FROM link_lib lib, link_bunrui bun ";

    protected static final String LINK_NEXT_OPTIONALSQL
	= "WHERE "
	+ "lib.company_cd = ? "
	+ "AND bun.link_bunrui_cd || LIb.link_cd > ? "
	+ "AND lib.link_bunrui_cd = bun.link_bunrui_cd "
	+ "AND lib.delete_ymd is null "
	+ "ORDER BY bun.link_bunrui_cd , lib.link_cd ";
//	+ "ORDER BY bun.link_bunrui_cd, lib.link_cd ";

    protected static final String LINK_LAST_OPTIONALSQL
	= "WHERE "
	+ "lib.company_cd = ? "
	+ "AND bun.link_bunrui_cd || LIb.link_cd < ? "
	+ "AND lib.link_bunrui_cd = bun.link_bunrui_cd "
	+ "AND lib.delete_ymd is null "
	+ "ORDER BY bun.link_bunrui_cd DESC , lib.link_cd DESC ";
//	+ "ORDER BY bun.link_bunrui_cd DESC, lib.link_cd DESC";

    /**
     * LinkLibInfoManager �R���X�g���N�^�[�E�R�����g�B
     */
    public LinkLibInfoManager(Connection conn) {
	this.conn = conn;
    }

    /**
     * <h3>�����N���C�u�������̎擾</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/22 �ߑO 11:59:34)
     * @return jp.ne.sonet.medipro.mr.server.entity.LinkLibInfo
     * @param linkCD java.lang.String
     */
    public LinkLibInfo getLinkLibInfo(String linkCD) {

	LinkLibInfo linklibinfo = null;
	String sqltxt = null;

	try {
	
	    //�r�p�k��
	    sqltxt = LINK_MAINSQL;
	    sqltxt = sqltxt +  "WHERE ";
	    sqltxt = sqltxt +  "lib.link_cd = '" + linkCD + "' ";
	    sqltxt = sqltxt +  "AND lib.link_bunrui_cd = bun.link_bunrui_cd ";

	
	    ResultSet rs;
	    Statement stmt = conn.createStatement();
	    try {
		rs   = stmt.executeQuery(sqltxt);
		while ( rs.next() ) {
		    linklibinfo = new LinkLibInfo();
		    linklibinfo.setLinkCD(rs.getString("link_cd"));
		    linklibinfo.setUrl(rs.getString("url"));
		    linklibinfo.setHonbunText(rs.getString("honbun_text"));
		    linklibinfo.setBunruiName(rs.getString("bunrui_name"));
		    linklibinfo.setNiagaiLinkKbn(rs.getString("niagai_link_kbn"));
		    linklibinfo.setPicture(rs.getString("picture"));
		}
	    } finally {
		//rs.close();
		stmt.close();
	    }
	} catch (SQLException e) {
	    throw new MrException(e);
	}

	return linklibinfo;
    }

    /**
     * <h3>�����N���C�u�������̂Q�O���̎擾</h3>
     * 
     * <br>
     * �����Ń��\�b�h�̋L�q��}�����Ă��������B
     * �쐬�� : (00/06/22 �ߌ� 12:02:11)
     * @return java.util.Enumeration (LinkLibInfo)
     * @param linkCD java.lang.String
     * @param companyCD java.lang.String
     * @param lastnextType java.lang.String
     */
    public Enumeration getLinkLibLastNext(String linkCD,
					  String companyCD,
					  String lastnextType )  {
	ResultSet rs;
	PreparedStatement pstmt = null;
	OrderedSet linklibinfolist = new OrderedSet();
	String sqltxt;

	BinaryPredicate pred = new LinkLibCDAscendPredicate();
	linklibinfolist = new OrderedSet(pred,true);

		/*
		�\���������������̂�Vector�ɕύX
		*/
		Vector list = new Vector();//1211 y-yamada 
	
	//�����N���C�u�������̎擾
	try {
	    //�����N���C�u�������Q�O�r�p�k��
	    if ( lastnextType.equals("1") ) {
		sqltxt = LINK_MAINSQL + LINK_LAST_OPTIONALSQL;
	    } else {
		sqltxt = LINK_MAINSQL + LINK_NEXT_OPTIONALSQL;
	    }
///////////////////////////////
//System.err.println("companyCD�@="+companyCD);
//System.err.println("linkCD�@="+linkCD);
//int count = linkCD.length();
//System.err.println("count�@="+count);
///////////////////////////////

	    pstmt = conn.prepareStatement(sqltxt);
	    try {
		// �p�����[�^��ݒ�
		pstmt.setString(1, companyCD);
		pstmt.setString(2, linkCD);
		rs   = pstmt.executeQuery();
		for (int i = 1; i <= 20; i++) {				
		    if ( rs.next() == true ) { 
			LinkLibInfo linklibinfo = new LinkLibInfo();
			linklibinfo.setLinkCD(rs.getString("link_cd"));
			linklibinfo.setUrl(rs.getString("url"));
			linklibinfo.setHonbunText(rs.getString("honbun_text"));
			linklibinfo.setLinkBunruiCD(rs.getString("bun_link_bunrui_cd"));
			linklibinfo.setBunruiName(rs.getString("bunrui_name"));
			
/////////////////////////////////////////////////////////////////////////////////
//System.err.println("bunrui_name�@="+(String)rs.getString("bun_link_bunrui_cd"));
/////////////////////////////////////////////////////////////////////////////////

			linklibinfo.setNiagaiLinkKbn(rs.getString("niagai_link_kbn"));
			linklibinfo.setPicture(rs.getString("picture"));

			if ( lastnextType.equals("1") ) {	//0309 M.Mizuki add
			    list.insertElementAt(linklibinfo, 0);//0309 M.Mizuki add
			}else{
			    list.addElement(linklibinfo);//1211 y-yamada add
			}

			//linklibinfolist.add(linklibinfo);
		    }
		    else {
			break;
		    }
		}	
	    } finally { 
		//rs.close();
		pstmt.close();
	    }
	} catch (SQLException e) {
	    throw new MrException(e);
	}	

	Enumeration  enum	= null;//1211 y-yamada add
	enum = list.elements();//1211 y-yamada add
	return enum;//1211 y-yamada add
	//return linklibinfolist.elements();
    }
}
