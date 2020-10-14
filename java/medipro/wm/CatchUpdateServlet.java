package medipro.wm;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import jp.ne.sonet.medipro.wm.common.*;
import jp.ne.sonet.medipro.wm.common.util.*;
import jp.ne.sonet.medipro.wm.common.exception.*;
import jp.ne.sonet.medipro.wm.common.servlet.*;
import jp.ne.sonet.medipro.wm.server.session.*;
import jp.ne.sonet.medipro.wm.server.manager.*;
import jp.ne.sonet.medipro.wm.server.controller.*;

//import com.jspsmart.upload.*;
import jp.ne.sonet.mrkun.server.MultipartHandler;

/**
 * <strong>��ЃL���b�`�摜�ǉ��E�ύX�Ή�Servlet�N���X</strong>
 * <br>�L���b�`�摜�̒ǉ��E�ύX����щ摜�t�@�C�����M�������s��.
 * @author
 * @version
 */
public class CatchUpdateServlet extends HttpServlet implements SingleThreadModel {
    ////////////////////////////////////////////////////
    //constants
    //
    /**��ЃL���b�`�摜�ꗗhtml�t�@�C�����΃p�X*/
    private static final String CATCH_LIST_PATH = "wm/WmCatchList/index.html";
    /**��ЃL���b�`�摜�ǉ��E�ύXhtml�t�@�C�����΃p�X*/
    private static final String CATCH_UPDATE_PATH = "wm/WmCatchUpdate/index.html";

    ////////////////////////////////////////////////////
    //class variable
    //
//      private HttpSession session = null;
//      private ServletConfig config = null;
//      private SmartUpload smartUpload = null;
//      private Request request = null;
//      private Common common;				// �R�����Z�b�V����
//      private CatchUpdateSession cuses;	// �L���b�`�摜�ǉ��E�ύX�Z�b�V����
//      private String mode;			// �������[�h
//      private String title;			// �摜��
//      private String radio;			// �摜�`��
//      private String path;			// ���_�C���N�g��
//      private String check;

    ////////////////////////////////////////////////////
    //class method
    //

    /**
     * doGet.
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    public void doGet(HttpServletRequest req, HttpServletResponse res) {
		doPost(req, res);
    }

    /**
     * doPost.
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    public void doPost(HttpServletRequest req, HttpServletResponse res) {
		if (SysCnst.DEBUG) {
			log("CatchUpdateServlet is called");
		}

		//SmartUpload smartUpload = null;
		MultipartHandler mph = null;
		HttpSession session = null;
		String mode = null;
		String title = null;
		String radio = null;
		String path = null;
		String check = null;
		CatchUpdateSession cuses = null;

		boolean sessionFlg = true;
		if (new SessionManager().check(req) != true) {
			sessionFlg = false;
			path = CATCH_UPDATE_PATH;
			new DispatManager().distSession(req, res);
		}
		else {
			session = req.getSession(true);
			//�Z�b�V�����I�u�W�F�N�g�̎擾
			Common common = (Common) session.getValue(SysCnst.KEY_COMMON_SESSION);
			cuses = (CatchUpdateSession) req.getSession(true).getValue
				(SysCnst.KEY_CATCHUPDATE_SESSION);
			if (cuses == null) {
				cuses = new CatchUpdateSession();
				session.putValue(SysCnst.KEY_CATCHUPDATE_SESSION, cuses);
			}

			DispatManager dm = new DispatManager();
			// �����`�F�b�N
			if (!common.getMasterFlg().equals(SysCnst.FLG_MASTER_WEB)) {
				// �E�F�u�}�X�^�ȊO�̏ꍇ
				dm.distAuthority(req, res);
			}
			
			//smartUpload = new SmartUpload();
			try {
				mph = new MultipartHandler(req);
			} catch (IOException errIO) {
				log ("IOException during process of multipart handler");
			}
			//try {
			//	smartUpload.init(getServletConfig());
			//	smartUpload.service(req, res);
			//	try {
			//		smartUpload.upload();
			//		request = smartUpload.getRequest();
			//	} catch (Exception e) {
			//		request = null;
			//	}
			//} catch (Exception ex) {
			//	log("", ex);
			//}

			//��ʃp�����[�^�̎擾
			mode = getParameter(req, "mode", mph);
			title = getParameter(req, "title", mph);
			radio = getParameter(req, "radiobutton", mph);
			if (getParameter(req, "default", mph) != null) {
				cuses.check();
			} else {
				cuses.unCheck();
			}

			if (SysCnst.DEBUG) {
				log("mode:"+mode);
				log("title:"+title);
				log("radio:"+radio);
			}
		}
		
		try {
			if (sessionFlg == true) {
				if (mode.equals("updatemessage")){
					// ��ۑ���{�^������������
					// ���b�Z�[�WID���Z�b�g
					cuses.setMessageState(SysCnst.CATCH_UPDATE_MSG_CONFIRM);
					// ���W�I�{�^����ԏ��Z�b�g
					cuses.setPictureType(radio);
					// �^�C�g�����Z�b�V�����ɃZ�b�g
					cuses.setPictureName(title);
					
					cuses.setFirstFlg(false);
					// �摜�t�@�C�����ꎞ�]���ۑ�
					makeTemporaryFile(req, res, mph, session, cuses);
					
					// �ꎞ�t�@�C���\���t���O�Z�b�g
					if (cuses.getPath() != null) {
						cuses.setTempFlg(true);
					}
					else {
						cuses.setTempFlg(false);
					}
					path = CATCH_UPDATE_PATH;
				} else if (mode.equals("list")) {
					// ����ɖ߂飉���������
					// �^�C�g���Z�b�V�����N���A
					cuses.crearPictureName();
					// �摜�`���Z�b�V�����N���A
					cuses.crearPictureType();
					// ���b�Z�[�WID�Z�b�g
					cuses.setMessageState(SysCnst.CATCH_UPDATE_MSG_NONE);
					// �ꎞ�t�@�C���\���t���O�Z�b�g
					cuses.setTempFlg(false);
					cuses.setPath(null);
					cuses.setFirstFlg(true);
					path = CATCH_LIST_PATH;
				} else if (mode.equals("update")) {
					// ��ۑ����܂�����Ţ�͂������������
					// ���W�I�{�^����ԏ��Z�b�g
					cuses.setPictureType(radio);
					// �^�C�g�����Z�b�V�����ɃZ�b�g
					cuses.setPictureName(title);
					if (cuses.isUpdate()) {
						// �X�V������
						callUpdateCatch(session, req, res);
					} else {
						// �V�K�ǉ�������
						callInsertCatch(session, req, res);
						cuses.setUpdateFlg(true);
					}
					// �摜�t�@�C���ۑ�����
					makeTemporaryFile(req, res, mph, session, cuses);
					executeSave(req, res, session, cuses);
					// ���b�Z�[�WID�Z�b�g
					cuses.setMessageState(SysCnst.CATCH_UPDATE_MSG_DONE);
					cuses.setFirstFlg(true);
					// �ꎞ�t�@�C���\���t���O�Z�b�g
					cuses.setTempFlg(false);
					cuses.setPath(null);
					path = CATCH_UPDATE_PATH;
				} else if (mode.equals("noupdate")) {
					// ���b�Z�[�WID���Z�b�g
					cuses.setMessageState(SysCnst.CATCH_UPDATE_MSG_NONE);
					cuses.setTempFlg(false);
					cuses.setPath(null);
					cuses.setFirstFlg(true);
					path = CATCH_UPDATE_PATH;
				} else if (mode.equals("") || mode == null) {
					// ���W�I�{�^����ԏ����N���A
					cuses.crearPictureType();
					// ���b�Z�[�WID���Z�b�g
					cuses.setMessageState(SysCnst.CATCH_UPDATE_MSG_NONE);
					// �^�C�g���N���A
					cuses.setPath(null);
					cuses.setTempFlg(false);
					cuses.setFirstFlg(true);
					path = CATCH_UPDATE_PATH;

				}
			}
			res.sendRedirect(SysCnst.HTML_ENTRY_POINT + path);
		} catch (Exception e) {
			log("", e);
			new DispatManager().distribute(req, res, e);
		}
    }

    /**
     * getParameter.
     * @return String
     * @param req HttpServletRequest
     * @param name String
     */
    private String getParameter(HttpServletRequest req,
				String name,
//				Request request) 
				MultipartHandler mph) 
    {
//		name = Converter.convert(name); //y-yamada add
//		if (request == null)      
		if (mph == null)        
		  return Converter.enToSjis(req.getParameter(name));     
		else
//		  return Converter.enToSjis(request.getParameter(name));         
		  return Converter.enToSjis((String)mph.getParameter(name));         
    }

    /**
     * executeSave.
     * <dl>�ꎞ�ۑ��摜�t�@�C�����摜�ۑ��f�B���N�g���ɕۑ�����</dl>
     * @param req HttpServletRequest
     * @param res HttpServletResponse
     */
    private void executeSave(HttpServletRequest req,
							 HttpServletResponse res,
							 HttpSession session,
							 CatchUpdateSession cuses) {
		if (SysCnst.DEBUG) {
			log("getPath:"+cuses.getPath());
			log("getPicture:"+cuses.getPicture());
		}

		Common common = (Common) session.getValue(SysCnst.KEY_COMMON_SESSION);

		try {
			if (cuses.getPath() != null && !cuses.getPath().equals("")) {
				//�摜���Z�b�g����Ă�����...
				//�m�肵���t�@�C���p�X�̃t�@�C���Ɉړ�
				java.io.File file = new java.io.File(common.getDocumentRoot() + cuses.getPath());
				java.io.File file2 = new java.io.File
					(common.getDocumentRoot() + cuses.getPicture());
				file2.delete();
				file2.getParentFile().mkdirs();
				file.renameTo(file2);
				cuses.setPath(null);
			}
		} catch (WmException we) {
			log("", we);
			new DispatManager().distIO(req, res);
		}
    }

    /**
     * makeTemporaryFile.
     * <dl>�摜���e���|�����f�B���N�g���Ɉꎞ�ۑ�����</dl>
     * @param req HttpServletRequest
     * @param res HttpServletResponse
     */
    private void makeTemporaryFile(HttpServletRequest req, 
				   HttpServletResponse res,
//				   SmartUpload smartUpload,
				   MultipartHandler mph,
				   HttpSession session,
				   CatchUpdateSession cuses) {
		//Files files = smartUpload.getFiles();
		Common common = (Common) session.getValue(SysCnst.KEY_COMMON_SESSION);

		try {
			//File file = files.getFile(0);
			byte inFile[] = mph.getRawParameter("path");
			String inFileName = mph.getFileName("path");
			if ((inFile != null) && (inFile.length > 0) &&
			    (inFileName != null) && !inFileName.equals(""))
			{
			  //if (file.getFileName() == null || file.getFileName().equals("")) {
			  //	//�t�@�C���w�肪���������Ƃ�
			  //	return;
			  //}
			  String inputFileCanonicalName = extractCanonicalName(inFileName);
			  String tempFile = common.getTempDir() + 
				"/" + common.getMrId() + 
				inputFileCanonicalName; //file.getFileName();
			  String fileName = common.getDocumentRoot() + tempFile;
			  if (SysCnst.DEBUG) {
			 	log("file:"+fileName);
			  }
			  //file.saveAs(fileName);
			  File outFile = new File(fileName);
			  FileOutputStream outStr = new FileOutputStream(outFile);
			  outStr.write(inFile);
			  outStr.close();
			  cuses.setPath(tempFile);
			  int lastDot = inputFileCanonicalName.lastIndexOf('.');
			  String fileExt = ( lastDot == -1  ? "" : inputFileCanonicalName.substring( lastDot + 1 ) );
			  cuses.setType(fileExt);// file.getFileExt());
			}
		//} catch (SmartUploadException  ex) {
		//	log("", ex);
		//	throw new WmException(ex);
		} catch (IOException ex) {
			log("", ex);
			throw new WmException(ex);
		}
    }

    /**
     * init.
     * @param config ServletConfig
     */
//      public void init(ServletConfig config) {
//  		this.config = config;
//      }
    
    /**
     * callUpdateCatch.
     * <dl>�L���b�`�摜�X�V�������R�[������</dl>
     * @param session HttpSession
     */
    public void callUpdateCatch(HttpSession session, 
								HttpServletRequest request,
								HttpServletResponse response) {
		CatchListController cctrl = new CatchListController(request, response);
		// �摜�X�V�����R�[��
		cctrl.updateCatch(session);
    }
	
    /**
     * callInsertCatch.
     * <dl>�L���b�`�摜�ǉ��������R�[������</dl>
     * @param session HttpSession
     */
    public void callInsertCatch(HttpSession session, 
								HttpServletRequest request,
								HttpServletResponse response) {
		CatchListController cctrl = new CatchListController(request, response);
		// �摜�ǉ������R�[��
		cctrl.addCatch(session);
    }

  /**
   * Retrieves the file name only from a full file path. This method is more
   * sophisticated than the default java.io.File method, because it has to
   * cater to the client browser submission of a file name (ie the slashes can
   * be supplied to point either direction).
   */
  private String extractCanonicalName(String fullPath)
  {
    StringBuffer  result = new StringBuffer(fullPath);
    int           nLength = fullPath.length();

    // Check for back slashes (ie windows paths)
    if (fullPath.lastIndexOf('\\') != -1)
      if (nLength > fullPath.lastIndexOf('\\'))
        result.delete(0, fullPath.lastIndexOf('\\') + 1);
      else
        result.delete(0, nLength);

    nLength = result.length();
    String searchString = result.toString();
    
    // Check for forward slashes (ie other paths)
    if (searchString.lastIndexOf('/') != -1)
      if (nLength > searchString.lastIndexOf('/'))
        result.delete(0, searchString.lastIndexOf('/') + 1);
      else
        result.delete(0, nLength);

    return result.toString();
  }

}
