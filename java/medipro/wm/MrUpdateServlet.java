package medipro.wm;

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;



import java.io.IOException;
import java.sql.Connection;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.ne.sonet.medipro.wm.common.SysCnst;
import jp.ne.sonet.medipro.wm.common.util.DBConnect;
import jp.ne.sonet.medipro.wm.common.util.Converter;
import jp.ne.sonet.medipro.wm.common.exception.DispatManager;
import jp.ne.sonet.medipro.wm.common.exception.WmException;
import jp.ne.sonet.medipro.wm.common.servlet.SessionManager;
import jp.ne.sonet.medipro.wm.server.session.Common;
import jp.ne.sonet.medipro.wm.server.session.MrUpdateSession;
import jp.ne.sonet.medipro.wm.server.manager.MrInfoManager;
import jp.ne.sonet.medipro.wm.server.entity.MrInfo;

/**
 * <strong>MR�Ǘ� - MR�̒ǉ��E�ύX��ʑΉ�Servlet�N���X.</strong>
 * @author
 * @version
 */
public class MrUpdateServlet extends HttpServlet {

    /**
     * �T�[�r�X��`.
     * @param req �v���I�u�W�F�N�g
     * @param res �ԓ��I�u�W�F�N�g
     */
    public void service(HttpServletRequest req, HttpServletResponse res) {
		if (SysCnst.DEBUG) {
			log("MrUpdateServlet called!");
		}

		//�Z�b�V�����`�F�b�N
		if (! new SessionManager().check(req)) {
			new DispatManager().distSession(req, res);
			return;
		}

		Common common = (Common)req.getSession(true).getValue(SysCnst.KEY_COMMON_SESSION);

	//�����`�F�b�N
		if (!common.getMasterFlg().equals(SysCnst.FLG_MASTER_WEB) &&
			!common.getMasterFlg().equals(SysCnst.FLG_MASTER_SUB)) {
			new DispatManager().distAuthority(req, res);
			return;
		}
	

		//��ʃp�����[�^�̎擾
		String action = req.getParameter("action");		//���얼
		String mrId = req.getParameter("id");			//MR ID�N���b�N��
		String back = req.getParameter("back");			//�߂�{�^��
		String save = req.getParameter("save");			//�ۑ��{�^��
		String updateOk = req.getParameter("updateOk");		//�ۑ����s�{�^��
		String updateCancel = req.getParameter("updateCancel");	//�ۑ����~�{�^��
		String inputOk = req.getParameter("inputOk");		//���͕s���{�^��
		String shitenNew = Converter.getParameter(req, "shiten");//�x�X��

	//MR��ǉ��E�ύX�Z�b�V�����I�u�W�F�N�g�̎擾
	//������ΐV���ɓo�^
		MrUpdateSession session = (MrUpdateSession)req.getSession(true).getValue(SysCnst.KEY_MRUPDATE_SESSION);
		if (session == null) {
			session = new MrUpdateSession();
			req.getSession(true).putValue(SysCnst.KEY_MRUPDATE_SESSION, session);
		}
		String shitenOld = session.getShitenCd();		//�ύX�O�̎x�X�R�[�h

//  	session.setStatus(MrUpdateSession.NORMAL);
		String nextPage = SysCnst.HTML_ENTRY_POINT + "wm/WmMrUpdate/index.html";

		try {
			//�Ƃ肠������ʍ��ڂ��Z�b�V�����ɕێ�
			setMrInfo(session, req);

			if (back != null) {
				//MR�ꗗ�ɖ߂�
                req.getSession(true).removeValue(SysCnst.KEY_MRUPDATE_SESSION);
				nextPage = SysCnst.HTML_ENTRY_POINT + "wm/WmMrList/index.html";
			} else if (save != null) {
		
				//�X�V�m�F
				if (action.equals("change")) {
					//�x�X��ύX�����̂łƂ肠�����c�Ə����N���A
					session.setEigyosyoCd("");
				} else {
					if (checkAllItem(req)) {
						//  			session.setStatus(MrUpdateSession.SAVE_CONFIRM);
						executeUpdate(req, res);
					}
				}
				//  	    } else if (updateOk != null) {
				//  		//�X�V���s
				//  		executeUpdate(req, res);
			} else if (inputOk != null) {
				//���͕s��OK�{�^��
				session.setStatus(MrUpdateSession.NORMAL);
				//  	    } else if (updateCancel != null) {
				//  		//�X�VCancel�{�^��
				//  		session.setStatus(MrUpdateSession.NORMAL);
			} else if (action.equals("update")) {
				//�X�V���
				session.clear();
				session.setLoadFlag(true);
			} else {
				//MR�ꗗ�ɖ߂�
				nextPage = SysCnst.HTML_ENTRY_POINT + "wm/WmMrList/index.html";
				session.setStatus(MrUpdateSession.NORMAL);
			}

			res.sendRedirect(nextPage);
		} catch (Exception ex) {
			log("", ex);
			new DispatManager().distribute(req, res, ex);
		}
    }

    /**
     * query�p�����[�^��session�p�����[�^�ɃR�s�[
     * @param session �Z�b�V�����I�u�W�F�N�g
     * @param req     �v���I�u�W�F�N�g
     */
    private void setMrInfo(MrUpdateSession session, HttpServletRequest req) {
		session.setMrId(req.getParameter("mrId"));
		session.setNameKana(Converter.getParameter(req, "nameKana"));
		session.setName(Converter.getParameter(req, "name"));
		if (Converter.getParameter(req, "shiten") != null &&
			!Converter.getParameter(req, "shiten").equals(session.getShitenCd())) {
			session.setSoshikiDeprivation(true);
		}
		session.setShitenCd(Converter.getParameter(req, "shiten"));
		if (Converter.getParameter(req, "eigyosyo") != null &&
			!Converter.getParameter(req, "eigyosyo").equals(session.getEigyosyoCd())) {
			session.setSoshikiDeprivation(true);
		}
		session.setEigyosyoCd(Converter.getParameter(req, "eigyosyo"));
		if (Converter.getParameter(req, "attribute1") != null &&
			!Converter.getParameter(req, "attribute1").equals(session.getMrAttributeCd1())) {
			session.setAttributeDeprivation(true);
		}
		session.setMrAttributeCd1(Converter.getParameter(req, "attribute1"));
		if (Converter.getParameter(req, "attribute2") != null &&
			!Converter.getParameter(req, "attribute2").equals(session.getMrAttributeCd2())) {
			session.setAttributeDeprivation(true);
		}
		session.setMrAttributeCd2(Converter.getParameter(req, "attribute2"));
		session.setPassword(req.getParameter("password"));
		session.setPictureCd(req.getParameter("pictureCd"));
		session.setNyusyaYear(Converter.getParameter(req, "nyusyaYear"));
    }

    private boolean checkAllItem(HttpServletRequest req) {
		MrUpdateSession session = (MrUpdateSession)req.getSession(true).getValue(SysCnst.KEY_MRUPDATE_SESSION);

	// 2001/03/09 Y.Yama ��ƈ˗��� MRK00005�ɑΉ� add
	// MR ID�ɋ󔒂��܂܂�Ă��邩�`�F�b�N
	// ���̍ۖ����̋󔒂��`�F�b�N�ΏۂƂ��Ăق�����M3���邳����v������
	// �Ȃ̂�trim�͂��Ȃ�
		String reqMrId = req.getParameter("mrId");
		if (reqMrId != null){
			//�󔒂��܂�MR ID�̏ꍇ�̓Z�b�V�����ɋ󔒃G���[���Z�b�g
			if (reqMrId.indexOf(" ") >= 0){
				session.setStatus(MrUpdateSession.INNER_SPACE_ERROR);
				return false;
			}else if(reqMrId.indexOf("\t") >= 0){
				session.setStatus(MrUpdateSession.INNER_TAB_ERROR);
				return false;
			}
			// add end
			// change (MRK00005�ɔ���if -> else if�ɕύX)
			//	if (req.getParameter("nameKana") == null || req.getParameter("nameKana").equals("")) {
		} else if (req.getParameter("nameKana") == null || req.getParameter("nameKana").equals("")) {
			session.setStatus(MrUpdateSession.NAMEKANA_NOINPUT);
			return false;
		} else if (req.getParameter("name") == null || req.getParameter("name").equals("")) {
			session.setStatus(MrUpdateSession.NAME_NOINPUT);
			return false;
		} else if (req.getParameter("password") == null || req.getParameter("password").equals("")) {
			session.setStatus(MrUpdateSession.PASSWORD_NOINPUT);
			return false;
		} else {
			String val = Converter.getParameter(req, "nyusyaYear");
			if (val == null || val.equals("")) {
			} else {
				try {
					new Integer(val);
				} catch (NumberFormatException ex) {
					session.setStatus(MrUpdateSession.NUMBER_FORMAT_ERROR);
					return false;
				}
			}
		}
	
		return true;
    }

    /**
     * �X�V���s.�K�{���̓`�F�b�N���s���܂�.
     * @param req �v���I�u�W�F�N�g
     * @param res �ԓ��I�u�W�F�N�g
     */
    private void executeUpdate(HttpServletRequest req, HttpServletResponse res) {
		MrUpdateSession session = (MrUpdateSession)req.getSession(true).getValue(SysCnst.KEY_MRUPDATE_SESSION);
		Common common = (Common)req.getSession(true).getValue(SysCnst.KEY_COMMON_SESSION);

		if (!checkAllItem(req)) {
			return;
		}

		MrInfo info = session.getMrInfo();
		DBConnect dbConnect = new DBConnect();
		Connection connection = null;

		try {
		
			HttpSession httpsession = req.getSession(true);
			String wmId = (String)httpsession.getValue("wmId");//1213 y-yamada add�@�o�^����wm��Id��n��
		
			connection = dbConnect.getDBConnect();
			MrInfoManager manager = new MrInfoManager(connection);
			String mrId = info.getMrId();
			//MRID���Z�b�g����Ă���΍X�V�A�łȂ���Βǉ�
			//  	    if (mrId == null || mrId.equals("")) {
			if (session.isAdding()) {
				//manager.insertMr(info, common);
				manager.insertMr(info, common, wmId);//1213 y-yamada add�@�o�^����wm��Id��n��
				session.setAdding(false);
			} else {
				if (info.getMasterFlg() != null &&
					info.getMasterFlg().equals(SysCnst.FLG_MASTER_WEB)) {
				} else {
					//�x�X�E�c�Ə����ύX���ꂽ��null���Z�b�g
					if (session.isSoshikiDeprivation()) {
						info.setMasterKengenSoshiki(null);
						info.setTelNo(null);
						info.setFaxNo(null);
					}
		
					//����1�E����2���ύX���ꂽ��null���Z�b�g
					if (session.isAttributeDeprivation()) {
						info.setMasterKengenAttribute(null);
					}

					//�T�u�}�X�^�[�łȂ�����
					if (info.getMasterKengenSoshiki() == null &&
						info.getMasterKengenAttribute() == null) {
						info.setMasterFlg(null);
					}
				}
		
				manager.updateMr(info, common);
			}
		} catch (Exception ex) {
			throw new WmException(ex);
		} finally {
			dbConnect.closeDB(connection);
		}

		session.setStatus(MrUpdateSession.SAVE_DONE);
    }

}
