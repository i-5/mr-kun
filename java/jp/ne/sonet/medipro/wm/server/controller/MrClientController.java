package jp.ne.sonet.medipro.wm.server.controller;

import java.sql.Connection;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jp.ne.sonet.medipro.wm.common.SysCnst;
import jp.ne.sonet.medipro.wm.common.util.DBConnect;
import jp.ne.sonet.medipro.wm.common.exception.DispatManager;
import jp.ne.sonet.medipro.wm.server.entity.MrInfo;
import jp.ne.sonet.medipro.wm.server.manager.SentakuTorokuInfoManager;
import jp.ne.sonet.medipro.wm.server.manager.MrInfoManager;
import jp.ne.sonet.medipro.wm.server.session.MrClientSession;
import jp.ne.sonet.medipro.wm.server.session.Common;

/**
 * <strong>MR�Ǘ�-�S���ڋq�ύX���Controller�N���X.</strong>
 * @author  doppe
 * @version 1.00
 */
public class MrClientController {

    ////////////////////////////////////////////////////////////////////////////////
    // instance methods
    /**
     * �K�v�ȃf�[�^��DB����ǂݍ��݃Z�b�V�����ɃZ�b�V�����ɃZ�b�g���܂��B
     * @param req     �v���I�u�W�F�N�g
     * @param res     �����I�u�W�F�N�g
     * @param session �Z�b�V�����I�u�W�F�N�g
     */
    public MrClientSession getMrClientSession(HttpServletRequest req,
					      HttpServletResponse res,
					      HttpSession session) {
	MrClientSession mrClientSession
	    = (MrClientSession)session.getValue(SysCnst.KEY_MRCLIENT_SESSION);

	//�Z�b�V�����������Ƃ��͐������ēo�^
	if (mrClientSession == null) {
	    mrClientSession = new MrClientSession();
	    session.putValue(SysCnst.KEY_MRCLIENT_SESSION, mrClientSession);
	}

	//�����̏����擾
	if (mrClientSession.isNeedToLeftMrLoad()) {
	    DBConnect dbConnect = new DBConnect();
	    Connection connection = null;

	    try {
		connection = dbConnect.getDBConnect();
		MrInfoManager mrManager = new MrInfoManager(connection);
		MrInfo mrInfo = mrManager.getMrInfo(mrClientSession.getLeftMr().getMrId());

		if (mrInfo == null) {
		    mrInfo = new MrInfo();
		    mrInfo.setMrId(mrClientSession.getLeftMr().getMrId());
		    mrClientSession.setStatus(MrClientSession.NO_MR_EXIST_ERROR);
		    mrClientSession.setLeftMrInfo(mrInfo, null);
		} else {
		    if (!hasUpdatePermission(session, mrInfo)) {
			//�����������Ă��Ȃ�������MR-ID�������ăN���A
			mrInfo = new MrInfo();
			mrInfo.setMrId(mrClientSession.getLeftMr().getMrId());
			mrClientSession.setLeftMrInfo(mrInfo, null);
			mrClientSession.setStatus(MrClientSession.AUTHORITY_ERROR);
		    } else {
			SentakuTorokuInfoManager senManager
			    = new SentakuTorokuInfoManager(connection);
			Vector drList
			    = senManager.getDrList(mrClientSession.getLeftMr().getMrId());

			mrClientSession.setLeftMrInfo(mrInfo, drList);
		    }
		}

	    } catch (Exception ex) {
		if (SysCnst.DEBUG) {
		    ex.printStackTrace();
		}
		new DispatManager().distribute(req, res);
	    } finally {
		dbConnect.closeDB(connection);
	    }
	}

	//�E���̏����擾
	if (mrClientSession.isNeedToRightMrLoad()) {
	    DBConnect dbConnect = new DBConnect();
	    Connection connection = null;

	    try {
		connection = dbConnect.getDBConnect();
		MrInfoManager mrManager = new MrInfoManager(connection);
		MrInfo mrInfo
		    = mrManager.getMrInfo(mrClientSession.getRightMr().getMrId());

		if (mrInfo == null) {
		    mrInfo = new MrInfo();
		    mrInfo.setMrId(mrClientSession.getRightMr().getMrId());
		    mrClientSession.setStatus(MrClientSession.NO_MR_EXIST_ERROR);
		    mrClientSession.setRightMrInfo(mrInfo, null);
		} else {
		    if (!hasUpdatePermission(session, mrInfo)) {
			//�����������Ă��Ȃ�������MR-ID�������ăN���A
			mrInfo = new MrInfo();
			mrInfo.setMrId(mrClientSession.getRightMr().getMrId());
			mrClientSession.setRightMrInfo(mrInfo, null);
			mrClientSession.setStatus(MrClientSession.AUTHORITY_ERROR);
		    } else {
			SentakuTorokuInfoManager
			    senManager = new SentakuTorokuInfoManager(connection);
			Vector drList
			    = senManager.getDrList(mrClientSession.getRightMr().getMrId());

			mrClientSession.setRightMrInfo(mrInfo, drList);
		    }
		}

	    } catch (Exception ex) {
		if (SysCnst.DEBUG) {
		    ex.printStackTrace();
		}
		new DispatManager().distribute(req, res);
	    } finally {
		dbConnect.closeDB(connection);
	    }
	}

	return mrClientSession;
    }

    /**
     * ���O�C�����[�U�[�̌����Ɠ��͂���MR-ID�̌����Ƃ��r�`�F�b�N����B
     * @param session �Z�b�V�������
     * @param info    ���͂���MR���
     * @return        �ύX��������������true
     */
    private boolean hasUpdatePermission(HttpSession session, MrInfo info) {
	Common common = (Common)session.getValue(SysCnst.KEY_COMMON_SESSION);

	if (!common.getCompanyCd().equals(info.getCompanyCd())) {
	    //�Ⴄ���
	    return false;
	} else if (common.getMasterFlg().equals(SysCnst.FLG_MASTER_WEB)) {
	    //�E�F�u�}�X�^�[�Ȃ�OK

	    return true;
	} else if (common.getMasterFlg().equals(SysCnst.FLG_MASTER_SUB)) {
	    //�T�u�}�X�^�[

//  	    if (info.getMasterFlg() != null && info.getMasterFlg().equals(SysCnst.FLG_MASTER_WEB)) {
//  		//�E�F�u�}�X�^��������s��
//  		return false;
//  	    }

	    if (common.getMasterKengenSoshiki().equals(SysCnst.FLG_AUTHORITY_BRANCH)) {
		//�x�X�̃T�u�}�X�^�[

		if (common.getMasterKengenAttribute().equals(SysCnst.FLG_AUTHORITY_ATTRIBUTE1) ||
		    common.getMasterKengenAttribute().equals(SysCnst.FLG_AUTHORITY_ATTRIBUTE2)) {
		    //���C���Ă�ꍇ��
		    return true;
		} else if (common.getShitenCd().equals(info.getShitenCd())) {
		    //�����łȂ��Ă��x�X�R�[�h��������������
		    return true;
		}

		return false;
	    } else if (common.getMasterKengenSoshiki().equals(SysCnst.FLG_AUTHORITY_OFFICE)) {
		//�c�Ə��̃T�u�}�X�^�[

		if (common.getMasterKengenAttribute().equals(SysCnst.FLG_AUTHORITY_ATTRIBUTE1) ||
		    common.getMasterKengenAttribute().equals(SysCnst.FLG_AUTHORITY_ATTRIBUTE2)) {
		    //���C���Ă�ꍇ��
		    return true;
//  		} else if (info.getMasterKengenSoshiki() != null &&
//  			   info.getMasterKengenSoshiki().equals(SysCnst.FLG_AUTHORITY_BRANCH)) {
		    //�x�X�̃T�u�}�X�^�͕s��
//  		    return false;
		} else if (common.getShitenCd().equals(info.getShitenCd()) &&
			   common.getEigyosyoCd().equals(info.getEigyosyoCd())) {
		    return true;
		}

		return false;
	    } else if (common.getMasterKengenAttribute().equals(SysCnst.FLG_AUTHORITY_ATTRIBUTE1)) {
		//����1�̃T�u�}�X�^�[

		if (common.getMasterKengenSoshiki().equals(SysCnst.FLG_AUTHORITY_BRANCH) ||
		    common.getMasterKengenSoshiki().equals(SysCnst.FLG_AUTHORITY_OFFICE)) {
		    //���C���Ă�ꍇ��
		    return true;
		} else if (common.getMrAttributeCd1().equals(info.getMrAttributeCd1()) ||
			   common.getMrAttributeCd1().equals(info.getMrAttributeCd2())) {
		    //�����łȂ��Ă�����1 or 2�R�[�h��������������
		    return true;
		}

		return false;
	    } else if (common.getMasterKengenAttribute().equals(SysCnst.FLG_AUTHORITY_ATTRIBUTE2)) {
		//����2�̃T�u�}�X�^�[

		if (common.getMasterKengenSoshiki().equals(SysCnst.FLG_AUTHORITY_BRANCH) ||
		    common.getMasterKengenSoshiki().equals(SysCnst.FLG_AUTHORITY_OFFICE)) {
		    //���C���Ă�ꍇ��
		    return true;
		} else if (common.getMrAttributeCd2().equals(info.getMrAttributeCd1()) ||
			   common.getMrAttributeCd2().equals(info.getMrAttributeCd2())) {
		    //�����łȂ��Ă�����1 or 2�R�[�h��������������
		    return true;
		}

		return false;
	    }
	}

	return false;
    }
}
