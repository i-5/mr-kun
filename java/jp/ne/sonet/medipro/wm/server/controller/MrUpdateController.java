package jp.ne.sonet.medipro.wm.server.controller;

import java.sql.Connection;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jp.ne.sonet.medipro.wm.common.SysCnst;
import jp.ne.sonet.medipro.wm.common.util.DBConnect;
import jp.ne.sonet.medipro.wm.common.exception.WmException;
import jp.ne.sonet.medipro.wm.common.exception.DispatManager;
import jp.ne.sonet.medipro.wm.server.entity.MrInfo;
import jp.ne.sonet.medipro.wm.server.manager.MrInfoManager;
import jp.ne.sonet.medipro.wm.server.manager.MrAttributeInfoManager;
import jp.ne.sonet.medipro.wm.server.manager.ShitenInfoManager;
import jp.ne.sonet.medipro.wm.server.manager.EigyosyoInfoManager;
import jp.ne.sonet.medipro.wm.server.session.MrUpdateSession;
import jp.ne.sonet.medipro.wm.server.session.Common;

/**
 * <strong>MR�ǉ��E�X�VController�N���X.</strong>
 * @author  doppe
 * @version 1.00
 */
public class MrUpdateController {

    /**
     * �R�[�h�Ɩ��̂̃y�A����Ȃ�Vector���R�[�h��key�Ƃ���Hashtable�ɕϊ�����.
     * @param list �R�[�h�Ɩ��̂̃y�A�ō\�����ꂽ�z��
     * @return �R�[�h��key�Ƃ���Hashtable
     */
    private Hashtable toHashtable(Vector list) {
	Hashtable table = new Hashtable();

	Enumeration e = list.elements();
	while (e.hasMoreElements()) {
	    String[] pair = (String[])e.nextElement();
	    table.put(pair[1], pair[0]);
	}
	return table;
    }

    /**
     * Common�ɐݒ肳�ꂽ��ЃR�[�h����ɁA�x�X�R�[�h�A�x�X���̃��X�g�𐶐����܂��B
     * @param req     �v���I�u�W�F�N�g
     * @param res     �����I�u�W�F�N�g
     * @param session �Z�b�V�����I�u�W�F�N�g
     * @return �R�[�h�Ɩ��̂̃y�A����Ȃ�z��
     */
    public Vector getShitenList(HttpServletRequest req, HttpServletResponse res, HttpSession session) {
	Vector list = null;
	MrUpdateSession mrUpdateSession = (MrUpdateSession)session.getValue(SysCnst.KEY_MRUPDATE_SESSION);
	Common common = (Common)session.getValue(SysCnst.KEY_COMMON_SESSION);

	try {
	    DBConnect dbConnect = new DBConnect();
	    Connection con = dbConnect.getDBConnect();
	    ShitenInfoManager manager = new ShitenInfoManager(con);
	    list = manager.getShitenList(common.getCompanyCd());

	    dbConnect.closeDB(con);
	} catch (Exception ex) {
	    ex.printStackTrace();
	    new DispatManager().distribute(req, res);
	}

	mrUpdateSession.setShitenList(toHashtable(list));
	
	return list;
    }

    /**
     * Common�ɐݒ肳�ꂽ��ЃR�[�h�A�x�X�R�[�h�����ɉc�Ə����X�g�𐶐����܂��B
     * @param req     �v���I�u�W�F�N�g
     * @param res     �����I�u�W�F�N�g
     * @param session �Z�b�V�����I�u�W�F�N�g
     * @return �R�[�h�Ɩ��̂̃y�A����Ȃ�z��
     */
    public Vector getEigyosyoList(HttpServletRequest req, HttpServletResponse res, HttpSession session) {
	Vector list = null;
	MrUpdateSession mrUpdateSession = (MrUpdateSession)session.getValue(SysCnst.KEY_MRUPDATE_SESSION);
	Common common = (Common)session.getValue(SysCnst.KEY_COMMON_SESSION);

	try {
	    DBConnect dbConnect = new DBConnect();
	    Connection con = dbConnect.getDBConnect();
	    EigyosyoInfoManager manager = new EigyosyoInfoManager(con);

	    list = manager.getEigyosyoList(common.getCompanyCd(), mrUpdateSession.getShitenCd());

	    dbConnect.closeDB(con);
	} catch (Exception ex) {
	    ex.printStackTrace();
	    new DispatManager().distribute(req, res);
	}

	mrUpdateSession.setEigyosyoList(toHashtable(list));

	return list;
    }

    /**
     * Common�ɐݒ肳�ꂽ��ЃR�[�h����ɁA�����R�[�h�A�������̃��X�g�𐶐����܂�.
     * @param req     �v���I�u�W�F�N�g
     * @param res     �����I�u�W�F�N�g
     * @param session �Z�b�V�����I�u�W�F�N�g
     * @return �R�[�h�Ɩ��̂̃y�A����Ȃ�z��
     */
    public Vector getMrAttributeList(HttpServletRequest req, HttpServletResponse res, HttpSession session) {
	Vector list = null;
	MrUpdateSession mrUpdateSession = (MrUpdateSession)session.getValue(SysCnst.KEY_MRUPDATE_SESSION);
	Common common = (Common)session.getValue(SysCnst.KEY_COMMON_SESSION);

	try {
	    DBConnect dbConnect = new DBConnect();
	    Connection con = dbConnect.getDBConnect();
	    MrAttributeInfoManager manager = new MrAttributeInfoManager(con);
	    list = manager.getMrAttributeList(common.getCompanyCd());

	    dbConnect.closeDB(con);
	} catch (Exception ex) {
	    ex.printStackTrace();
	    new DispatManager().distribute(req, res);
	}

	mrUpdateSession.setAttributeList(toHashtable(list));

	return list;
    }

    /**
     * �K�v��MR�����Z�b�V�����ɐς�ŁA���̃Z�b�V�������擾���܂�.
     * @param req     �v���I�u�W�F�N�g
     * @param res     �����I�u�W�F�N�g
     * @param session �Z�b�V�����I�u�W�F�N�g
     */
    public MrUpdateSession getMrUpdateSession(HttpServletRequest req, HttpServletResponse res, HttpSession session) {
	MrInfo info = null;
	MrUpdateSession mrUpdateSession = (MrUpdateSession)session.getValue(SysCnst.KEY_MRUPDATE_SESSION);
	Common common = (Common)session.getValue(SysCnst.KEY_COMMON_SESSION);
	Connection con = null;

	//�Z�b�V�����������Ƃ��͐������ēo�^
	if (mrUpdateSession == null) {
	    mrUpdateSession = new MrUpdateSession();
	    session.putValue(SysCnst.KEY_MRUPDATE_SESSION, mrUpdateSession);
	}

	if (mrUpdateSession.isNeedToLoad()) {
	    //�ύX��
	    try {
		DBConnect dbConnect = new DBConnect();
		con = dbConnect.getDBConnect();
      
		MrInfoManager manager = new MrInfoManager(con);
		info = manager.getMrInfo(mrUpdateSession.getMrId());

		dbConnect.closeDB(con);
	    } catch (Exception ex) {
		ex.printStackTrace();
		new DispatManager().distribute(req, res);
	    }

	    mrUpdateSession.setMrInfo(info);
	    mrUpdateSession.setLoadFlag(false);
	    mrUpdateSession.setAdding(false);
	} else {
	    //�ǉ���
	    //�����ɂ���ăf�t�H���g�l��ݒ�

	    //�x�X or �c�Ə��̃T�u�}�X�^�[�ő����̃T�u�}�X�^�[�����C���Ă��Ȃ�
	    if ((common.getMasterKengenSoshiki().equals(SysCnst.FLG_AUTHORITY_BRANCH) ||
		 common.getMasterKengenSoshiki().equals(SysCnst.FLG_AUTHORITY_OFFICE)) &&
		!common.getMasterKengenAttribute().equals(SysCnst.FLG_AUTHORITY_ATTRIBUTE1) &&
		!common.getMasterKengenAttribute().equals(SysCnst.FLG_AUTHORITY_ATTRIBUTE2)) {
		//�x�X���̃f�t�H���g�l��ݒ�
		mrUpdateSession.setShitenCd(common.getShitenCd());

		//����ɉc�Ə��̃T�u�}�X�^�[�������ꍇ�͉c�Ə����̃f�t�H���g�l��ݒ�
		if (common.getMasterKengenSoshiki().equals(SysCnst.FLG_AUTHORITY_OFFICE)) {
		    mrUpdateSession.setEigyosyoCd(common.getEigyosyoCd());
		}
	    }

	    //����1�̃T�u�}�X�^�[�Ŏx�X�E�c�Ə��̃T�u�}�X�^�[�����C���Ă��Ȃ�
	    if (common.getMasterKengenAttribute().equals(SysCnst.FLG_AUTHORITY_ATTRIBUTE1) &&
		!common.getMasterKengenSoshiki().equals(SysCnst.FLG_AUTHORITY_BRANCH) &&
		!common.getMasterKengenSoshiki().equals(SysCnst.FLG_AUTHORITY_OFFICE)) {
		//����1�̃f�t�H���g�l��ݒ�
		mrUpdateSession.setMrAttributeCd1(common.getMrAttributeCd1());
	    }

	    //����2�̃T�u�}�X�^�[�Ŏx�X�E�c�Ə��̃T�u�}�X�^�[�����C���Ă��Ȃ�
	    if (common.getMasterKengenAttribute().equals(SysCnst.FLG_AUTHORITY_ATTRIBUTE2) &&
		!common.getMasterKengenSoshiki().equals(SysCnst.FLG_AUTHORITY_BRANCH) &&
		!common.getMasterKengenSoshiki().equals(SysCnst.FLG_AUTHORITY_OFFICE)) {
		//����2�̃f�t�H���g�l��ݒ�
		mrUpdateSession.setMrAttributeCd2(common.getMrAttributeCd2());
	    }

	}

	return mrUpdateSession;
    }

}
