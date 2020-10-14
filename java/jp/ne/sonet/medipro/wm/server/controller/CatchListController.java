package jp.ne.sonet.medipro.wm.server.controller;

import java.util.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.wm.common.*;
import jp.ne.sonet.medipro.wm.common.util.*; 
import jp.ne.sonet.medipro.wm.common.exception.*;
import jp.ne.sonet.medipro.wm.common.servlet.*;
import jp.ne.sonet.medipro.wm.server.manager.*;
import jp.ne.sonet.medipro.wm.server.entity.*;
import jp.ne.sonet.medipro.wm.server.controller.*;
import jp.ne.sonet.medipro.wm.server.session.*;

/**
 * <strong>��ЃL���b�`�摜�ꗗController�N���X.</strong>
 * <br>
 * @author: 
 * @version: 
 */
public class CatchListController {
    /////////////////////////////////////////////
    //class variables
    //
    protected Connection conn;
    protected DBConnect dbconn;
    private HttpServletRequest request;
    private HttpServletResponse response;
	
    /////////////////////////////////////////////
    //constructors
    //
    /**
     * �R���X�g���N�^.
     */
    public CatchListController(HttpServletRequest req, HttpServletResponse res) {
	request = req;
	response = res;
//		dbconn = new DBConnect();
//		conn = dbconn.getDBConnect();
    }
	
    /////////////////////////////////////////////
    //class methods
    //

    /**
     * ��Љ摜�ꗗ�\��.
     * @return Vector
     * @param session HttpSession
     */
    public Vector createDisplay(HttpSession session) {

	Vector catchList = null;
		
	CatchListSession cases = (CatchListSession) session.getValue
	    (SysCnst.KEY_CATCH_SESSION);
	if (cases == null) {
	    System.out.println("CatchListSession is null(controller)!");
	    cases = new CatchListSession();
	    session.putValue(SysCnst.KEY_CATCH_SESSION, cases);
	}
		
	try{
	    dbconn = new DBConnect();
	    conn = dbconn.getDBConnect();

	    CatchInfoManager catchmanager = new CatchInfoManager(conn, session);
	    catchList = catchmanager.getCatchInfo();
	}
	catch (WmException e) {
	    DispatManager dm = new DispatManager();
	    dm.distribute(request,response);
	    throw new WmException(e);
	}
	finally {
	    dbconn.closeDB(conn);
	}

	return catchList;
    }

    /**
     * ��Љ摜�X�V��ʕ\��.
     * @return jp.ne.sonet.medipro.wm.server.entity.CatchInfo
     * @param session HttpSession
     */
    public CatchInfo createDisplayUpdate(HttpSession session) {

	CatchInfo caInfo = null;
		
	try{
	    dbconn = new DBConnect();
	    conn = dbconn.getDBConnect();

	    CatchInfoManager catchmanager = new CatchInfoManager(conn, session);
	    caInfo = catchmanager.getCatch();
	}
	catch (WmException e) {
	    DispatManager dm = new DispatManager();
	    dm.distribute(request,response);
	    throw new WmException(e);
	}
	finally {
	    dbconn.closeDB(conn);
	}

	return caInfo;
    }

    /**
     * �f�t�H���g�摜�X�V.
     * @param session javax.servlet.http.HttpSession
     * @param pictureCD java.lang.String
     */
    public void updatePicture(HttpSession session, String pictureCD) {

	try{
	    dbconn = new DBConnect();
	    conn = dbconn.getDBConnect();

	    CatchInfoManager catchmanager = new CatchInfoManager(conn, session);
	    catchmanager.changeDefault(pictureCD);
	}
	catch (WmException e) {
	    DispatManager dm = new DispatManager();
	    dm.distribute(request,response);
	    throw new WmException(e);
	}
	finally {
	    dbconn.closeDB(conn);
	}
    }

    /**
     * �f�t�H���g�摜�폜.
     * @param session javax.servlet.http.HttpSession
     */
    public void deletePicture(HttpSession session) {

	try{
	    dbconn = new DBConnect();
	    conn = dbconn.getDBConnect();

	    CatchInfoManager catchmanager = new CatchInfoManager(conn, session);
	    catchmanager.deleteCatch();
	}
	catch (WmException e) {
	    DispatManager dm = new DispatManager();
	    dm.distribute(request,response);
	    throw new WmException(e);
	}
	finally {
	    dbconn.closeDB(conn);
	}
    }

    /**
     * �摜�X�V.
     * @param session javax.servlet.session.HttpSession
     */
    public void updateCatch(HttpSession session) {

	try{
	    dbconn = new DBConnect();
	    conn = dbconn.getDBConnect();

	    CatchInfoManager catchmanager = new CatchInfoManager(conn, session);
	    catchmanager.changeCatch();

	} catch (WmException e) {
	    DispatManager dm = new DispatManager();
	    dm.distribute(request,response);
	    throw new WmException(e);
	} finally {
	    dbconn.closeDB(conn);
	}
    }

    /**
     * �摜�ǉ�.
     * @param session javax.servlet.session.HttpSession
     */
    public void addCatch(HttpSession session) {

	try{
	    dbconn = new DBConnect();
	    conn = dbconn.getDBConnect();

	    CatchInfoManager catchmanager = new CatchInfoManager(conn, session);
	    catchmanager.insertCatch();
	}
	catch (WmException e) {
	    DispatManager dm = new DispatManager();
	    dm.distribute(request,response);
	    throw new WmException(e);
	}
	finally {
	    dbconn.closeDB(conn);
	}
    }

    /**
     * �Z�b�V�������i��ЃL���b�`�摜�ꗗ�Ǘ����j������������.
     * @param req HttpServletRequest
     * @param res HttpServletResponse
     * @return boolean
     */
    public boolean initSession(HttpServletRequest request,
			       HttpServletResponse response) {
	boolean bContinue = true;
	DispatManager dm = new DispatManager();
	try {
	    // ���O�C�����̃`�F�b�N�i�Z�b�V�������̎擾�j
	    if (SessionManager.check(request) != true) {
		System.out.println("session err!");
		dm.distSession(request, response);
		return false;
	    }
	    HttpSession session = request.getSession(true);
	    String comKey = SysCnst.KEY_COMMON_SESSION;
	    Common common = (Common) session.getValue(comKey);
	    if ( common == null ) {
		dm.distSession(request, response);
		return false;
	    }
	    // �����`�F�b�N
	    if (!common.getMasterFlg().equals(SysCnst.FLG_MASTER_WEB)) {
				// �E�F�u�}�X�^�ȊO�̏ꍇ
		dm.distAuthority(request, response);
		return false;
	    }
			
	    // DB�R�l�N�g�`�F�b�N
	    if (new DBConnect().isConnectable() == false) {
		dm.distribute(request, response);
		return false;
	    }
	    // ��ЃL���b�`�摜�ꗗ��ʗp�Z�b�V�������擾�������ݒ�
	    String catchKey = SysCnst.KEY_CATCH_SESSION;
	    CatchListSession cuses =
		(CatchListSession) session.getValue(catchKey);
	    if (cuses == null) {
		cuses = new CatchListSession();
		session.putValue(catchKey, cuses);
	    }
	}
	catch (Exception e) {
	    dm.distribute(request, response);
	}

	return bContinue;
    }

    /**
     * �Z�b�V�������i��ЃL���b�`�摜�ǉ��E�ύX�Ǘ����j������������.
     * @param req HttpServletRequest
     * @param res HttpServletResponse
     * @return boolean
     */
    public boolean initUpdateSession(HttpServletRequest request,
				     HttpServletResponse response) {

	boolean bContinue = true;
	DispatManager dm = new DispatManager();
	try {
	    // ���O�C�����̃`�F�b�N�i�Z�b�V�������̎擾�j
	    if (SessionManager.check(request) != true) {
		dm.distSession(request, response);
		return false;
	    }
	    HttpSession session = request.getSession(true);
	    String comKey = SysCnst.KEY_COMMON_SESSION;
	    Common common = (Common) session.getValue(comKey);
	    if (common == null) {
		dm.distSession(request, response);
		return false;
	    }
	    // �����`�F�b�N
	    if (!common.getMasterFlg().equals(SysCnst.FLG_MASTER_WEB)) {
				// �E�F�u�}�X�^�ȊO�̏ꍇ
		dm.distAuthority(request, response);
		return false;
	    }
			
	    // DB�R�l�N�g�`�F�b�N
	    if (new DBConnect().isConnectable() == false) {
		dm.distribute(request, response);
		return false;
	    }
	    // ��ЃL���b�`�摜�ǉ��E�ύX��ʗp�Z�b�V�������擾�������ݒ�
	    String catchKey = SysCnst.KEY_CATCHUPDATE_SESSION;
	    CatchUpdateSession cuses =
		(CatchUpdateSession) session.getValue(catchKey);
	    if (cuses == null) {
		cuses = new CatchUpdateSession();
		session.putValue(catchKey, cuses);
	    }
	}
	catch (Exception e) {
	    dm.distribute(request, response);
	}

	return bContinue;
    }
}
