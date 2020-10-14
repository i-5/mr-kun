package medipro.wm;

import java.io.IOException;
import java.sql.Connection;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;
import com.jspsmart.upload.Request;
import com.jspsmart.upload.Files;
import com.jspsmart.upload.File;

import jp.ne.sonet.medipro.wm.common.SysCnst;
import jp.ne.sonet.medipro.wm.common.util.DBConnect;
import jp.ne.sonet.medipro.wm.common.exception.WmException;
import jp.ne.sonet.medipro.wm.common.exception.DispatManager;
import jp.ne.sonet.medipro.wm.common.servlet.SessionManager;
import jp.ne.sonet.medipro.wm.server.session.Common;
import jp.ne.sonet.medipro.wm.server.session.MrCatchUpdateSession;
import jp.ne.sonet.medipro.wm.server.manager.MrCatchInfoManager;
import jp.ne.sonet.medipro.wm.server.manager.MrInfoManager;


import jp.ne.sonet.medipro.wm.common.util.*;//1025 y-yamada add
import java.io.*;

/**
 * <strong>MR�L���b�`�摜�ǉ��E�X�V��ʑΉ��T�[�u���b�g</strong>.
 * @author  doppe
 * @version 1.00
 */
public class MrCatchUpdateServlet extends HttpServlet {
    /** servlet configuration */
//      private ServletConfig config = null;
    /** multipart/form-data�����I�u�W�F�N�g */
//      private SmartUpload smartUpload = null;
    /** multipart/form-data�Ή��v���I�u�W�F�N�g */
//      private Request request = null;

    /**
     * �T�[�r�X��`.
     * @param req �v���I�u�W�F�N�g
     * @param res �ԓ��I�u�W�F�N�g
     */
    public void service(HttpServletRequest req, HttpServletResponse res) {
        if (SysCnst.DEBUG) {
            log("MrCatchUpdateServlet is called");
        }
 
        //�Z�b�V�����`�F�b�N
        if (!new SessionManager().check(req)) {
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

		if (!DBConnect.isConnectable()) {
            new DispatManager().distribute(req, res);
			return;
		}

		SmartUpload smartUpload = new SmartUpload();
		Request request = null;
        try {
            smartUpload.init(getServletConfig());
            smartUpload.service(req, res);
            try {
                smartUpload.upload();
                request = smartUpload.getRequest();
            } catch (Exception e) {
            }
        } catch (Exception ex) {
			if (SysCnst.DEBUG) {
				log("", ex);
			}
            new DispatManager().distribute(req, res);
			return;
        }

        //��ʃp�����[�^�̎擾
        String save = getParameter(req, request, "save");
        String back = getParameter(req, request, "back");
        String saveOk = getParameter(req, request, "saveOk");
        String saveCancel = getParameter(req, request, "saveCancel");
        String mrId = getParameter(req, request, "mrId");
        String pictureName = getParameter(req, request, "pictureName");

        //�Z�b�V�����I�u�W�F�N�g�̎擾�A������ΐV���ɓo�^
        MrCatchUpdateSession session = (MrCatchUpdateSession)req.getSession(true).getValue(SysCnst.KEY_MRCATCHUPDATE_SESSION);
        if (session == null) {
            session = new MrCatchUpdateSession();
            req.getSession(true).putValue(SysCnst.KEY_MRCATCHUPDATE_SESSION, session);
        }

		if (getParameter(req, request, "default") != null) {
			session.setCheck(true);
		} else {
			session.setCheck(false);
		}

        //status���N���A
        session.setStatus(MrCatchUpdateSession.NORMAL);
        session.setPictureName(pictureName);
		session.setMrId(mrId);

        String nextPage = SysCnst.HTML_ENTRY_POINT + "wm/WmMrMngCatchUpdate/index.html";

        try {
            if (save != null) {
                session.setStatus(MrCatchUpdateSession.SAVE_CONFIRM);
                makeTemporaryFile(req, res, smartUpload);
                executeSave(req, res);
            } else if (back != null) {
                req.getSession(true).removeValue(SysCnst.KEY_MRCATCHUPDATE_SESSION);
                nextPage = SysCnst.SERVLET_ENTRY_POINT + "medipro.wm.MrCatchListServlet?mrId=" + mrId;
            } else if (mrId != null) {
                String pictureCd = getParameter(req, request, "pictureCd");

                if (pictureCd != null) {
                    //�ύX
                    session.setLoadFlag(true);
                    session.setPictureCd(pictureCd);
                } else {
                    //�V�K
                    session = new MrCatchUpdateSession();
                    session.setMrId(mrId);
                    req.getSession(true).putValue(SysCnst.KEY_MRCATCHUPDATE_SESSION, session);
                }
            }

            res.sendRedirect(nextPage);
        } catch (Exception ex) {
			if (SysCnst.DEBUG) {
				log("", ex);
			}
            new DispatManager().distribute(req, res, ex);
        }
    }

    /**
     * �p�����[�^�擾��̑I�����s���A�K�؂�query�p�����[�^���擾����.
     * @param req  �v���I�u�W�F�N�g
     * @param name �p�����[�^��
     * @return �p�����[�^�l
     */
    private String getParameter(HttpServletRequest req,
								Request request,
								String name) {
        String value = "";
	//	name = Converter.convert(name); //y-yamada add
        if (request == null) {         
          //return Converter.convert(req.getParameter(name));        
          value = req.getParameter(name);
        }                                    
        else
          value = request.getParameter(name);

        //return Converter.convert(request.getParameter(name));       
        if (value == null)
	  return null;
  	try
        {
	  value = new String(value.getBytes("8859_1"), "SJIS");
	}
        catch (UnsupportedEncodingException ex)
        {
	  throw new RuntimeException("Error decoding parameter" + ex.toString());
	}
  	return value;
    }

    /**
     * �ۑ����s(DB�ւ̏������݂�TEMP�t�@�C���̈ړ�)
     * @param req �v���I�u�W�F�N�g
     * @param res �����I�u�W�F�N�g
     */
    private void executeSave(HttpServletRequest req, HttpServletResponse res) {
        MrCatchUpdateSession session
			= (MrCatchUpdateSession)req.getSession(true).getValue(SysCnst.KEY_MRCATCHUPDATE_SESSION);
        Common common = (Common)req.getSession(true).getValue(SysCnst.KEY_COMMON_SESSION);
        
        DBConnect dbConnect = new DBConnect();
        Connection connection = null;

		try {
			connection = dbConnect.getDBConnect();
			MrCatchInfoManager manager = new MrCatchInfoManager(connection);

	    //�摜�R�[�h���w�肳��Ă�����ǉ��A�����łȂ���΍X�V
			if (session.getPictureCd() == null || session.getPictureCd().equals("")) {
				manager.insertMrCatchInfo(req.getSession(true));
			} else {
				manager.updateMrCatchInfo(req.getSession(true));
			}
	    
			if (session.isChecked()) {
				MrInfoManager mrManager = new MrInfoManager(connection);
				int c = mrManager.setDefaultPictureCd(common,
													  session.getPictureCd(),
													  session.getMrId());
			}
		} catch (Exception ex) {
			throw new WmException(ex);
		} finally {
			dbConnect.closeDB(connection);
		}

        //temporary�摜���ݒ肳��Ă�����A�w��̏ꏊ�Ɉړ�����.
        if (session.getTempPicture() != null && !session.getTempPicture().equals("")) {
            java.io.File file = new java.io.File(common.getDocumentRoot() + session.getTempPicture());
            java.io.File file2 = new java.io.File(common.getDocumentRoot() + session.getCatchInfo().getPicture());

            //�ړ����O�̂��ߍ폜
            file2.delete();
            //�ړ���directory�̍쐬
            file2.getParentFile().mkdirs();
            //�ړ�
            file.renameTo(file2);
            //�N���A
            session.setTempPicture(null);
        }

        session.setStatus(MrCatchUpdateSession.SAVE_DONE);
    }

    /**
     * �ۑ��m�F�̍ۂ�temporary�摜�t�@�C����ۑ�����.
     * @param req �v���I�u�W�F�N�g
     * @param res �����I�u�W�F�N�g
     */
    private void makeTemporaryFile(HttpServletRequest req,
								   HttpServletResponse res,
								   SmartUpload smartUpload) {
        MrCatchUpdateSession session = (MrCatchUpdateSession)req.getSession(true).getValue(SysCnst.KEY_MRCATCHUPDATE_SESSION);
        Common common = (Common)req.getSession(true).getValue(SysCnst.KEY_COMMON_SESSION);
        Files files = smartUpload.getFiles();

        File file = files.getFile(0);

        //�t�@�C���w�肪���������Ƃ��͉������Ȃ�
        if (file.getFileName() == null || file.getFileName().equals("")) {
            return;
        }

        String fileName = common.getTempDir() + SysCnst.SEPARATOR + common.getMrId() + file.getFileName();
        
        if (SysCnst.DEBUG) {
            log("temporaty file = " + common.getDocumentRoot() + fileName);
        }

        try {
            //�ۑ�
            System.err.println("Saving to filename: " + common.getDocumentRoot() + fileName);
            file.saveAs(common.getDocumentRoot() + fileName);
            //temporary�摜���̃Z�b�g
            session.setTempPicture(fileName);
            //�摜�t�@�C���g���q�̃Z�b�g
            session.setExtension(file.getFileExt());
        } catch (SmartUploadException  ex) {
			if (SysCnst.DEBUG) {
				log("", ex);
			}
            throw new WmException(ex);
        } catch (Exception ex) {
			if (SysCnst.DEBUG) {
				log("", ex);
			}
            throw new WmException(ex);
        }
    }

    /**
     * �T�[�u���b�g���������s��.
     * @param config configuration�I�u�W�F�N�g
     */
//      public void init(ServletConfig config) {
//          this.config = config;
//      }

}
