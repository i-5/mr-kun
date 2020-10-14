package medipro.mr;

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.mr.common.util.*;
import jp.ne.sonet.medipro.mr.common.exception.*;
import jp.ne.sonet.medipro.mr.common.servlet.*;
import jp.ne.sonet.medipro.mr.server.manager.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
import com.jspsmart.upload.*;

/**
 * Medipro MR���b�Z�[�W�쐬�i�摜�E�^�C�g���E�����N�̍쐬�j
 */
public class MsgBuildCtl extends HttpServlet {
//      private ServletConfig	config;

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
		String nextpage = null;	//�{���ɂ�����?

		try {
			// �Z�b�V�����`�F�b�N���Z�b�V����������΃��Z�b�g
			SessionManager sm = new SessionManager();

			// multipart request
			SmartUpload mySmartUpload = new SmartUpload();
			mySmartUpload.init(getServletConfig());
			mySmartUpload.service(request, response);
			mySmartUpload.setMaxFileSize(104857600L);
			mySmartUpload.upload();
			Request myRequest = mySmartUpload.getRequest();

			if ( sm.check(request, 1, myRequest) ) {
				HttpSession session = request.getSession(true);

				// session object ��message���N���A
				session.removeValue("sendmail");
				session.removeValue("uploadImageErr");
				session.removeValue("uploadFileErr");
				session.removeValue("upload_image_Name");
				session.removeValue("upload_image_Seq");
				session.removeValue("upload_file_Name");
				session.removeValue("upload_file_Seq");
				session.removeValue("limitdateErr");
				session.removeValue("limitdateTermErr");
				//1222�@�t�@�C�����̍Ōオ"."��������G���[
				session.removeValue("fileNameErr");
				session.removeValue("bccError");

				// ���̉�ʂ��Z�b�g
				String sendlist = null;
				if ( myRequest.getParameter("sendlist") != null ) {
					sendlist = myRequest.getParameter("sendlist");
				} else {
					sendlist = "";
				}
				// ����I����
				if ( sendlist.equals("ON") ) {
					nextpage = "/medipro/mr/MrMessageSendList/index.html";
				}
				// ����I�����玟��
				else if ( sendlist.equals("next") ) {
					String allcheck = myRequest.getParameter("allcheck");
					if ( allcheck.length() > 0 ) {
						nextpage = "/medipro/mr/MrMessageSendList/index.html";
					} else {
						nextpage = myRequest.getParameter("action");
 
						// �I�����ꂽ��t���Z�b�g
						setSelectDoctor(myRequest, session);
					}
				} else {
					String next = myRequest.getParameter("next");

					if ( next.length() == 0 ) {
						next = myRequest.getParameter("next_radio");
						//added by doppe
						if (next.equals("original")) {
							session.putValue("link_type", "original");
						} else {
							session.removeValue("link_type");
						}
					}
					// �I���W�i���쐬��
					if ( next.equals("original") ) {
						//1201 y-yamada add �����N���N���A
						session.removeValue("link_url");
						//1201 y-yamada add �L���N���N���A
						session.removeValue("limit_yy");
						//1201 y-yamada add �L�������N���A
						session.removeValue("limit_mm");
						//1201 y-yamada add �L�������N���A
						session.removeValue("limit_dd");
						nextpage = "/medipro/mr/MrMessageBuild2/index.html";
			
					}
					// ��M���׉�ʂ���I���W�i���쐬��
					if ( next.equals("originalFromMsg") ) {
						//1116 y-yamada add NO.53  �I���W�i�����쐬���鎖��
						session.putValue("link_type", "original");
						//�Z�b�V�����ɓ���Ă���
						//1116�@y-yamada add NO.54 
						//��M���ׂ���I���W�i�������ꍇ�́A
						//�N���A����
						//1116 y-yamada add �{���e�L�X�g���N���A
						session.removeValue("message");
						session.removeValue("plain_message");
						//1116 y-yamada add �����N���N���A
						session.removeValue("link_url");
						//1116 y-yamada add �L���N���N���A
						session.removeValue("limit_yy");
						//1116 y-yamada add �L�������N���A
						session.removeValue("limit_mm");
						//1116 y-yamada add �L�������N���A
						session.removeValue("limit_dd");

						getNewMessage(myRequest, session);
						// �I�����ꂽ��t���Z�b�g
						setSelectDoctor(myRequest, session);
						nextpage = "/medipro/mr/MrMessageBuild2/index.html";
					}
					// ���C�u�����g�p��
					else if ( next.equals("library") ) {
						//1201 y-yamada add �{���e�L�X�g���N���A
						session.removeValue("message");
						session.removeValue("plain_message");
						//1201 y-yamada add �����N���N���A
						session.removeValue("link_url");
						//1201 y-yamada add �L���N���N���A
						session.removeValue("limit_yy");
						//1201 y-yamada add �L�������N���A
						session.removeValue("limit_mm");
						//1201 y-yamada add �L�������N���A
						session.removeValue("limit_dd");
						nextpage = "/medipro/mr/MrMessageBuild3/index.html";
					}
					// ���̑��̃����N��
					else if ( next.equals("other") ) {
						//1201 y-yamada add �{���e�L�X�g���N���A
						session.removeValue("message");
						session.removeValue("plain_message");
						//1201 y-yamada add �����N���N���A
						session.removeValue("link_url");
						//1201 y-yamada add �L���N���N���A
						session.removeValue("limit_yy");
						//1201 y-yamada add �L�������N���A
						session.removeValue("limit_mm");
						//1201 y-yamada add �L�������N���A
						session.removeValue("limit_dd");
						nextpage = "/medipro/mr/MrMessageBuild4/index.html";
					}
					// �A���P�[�g���M�ցi�G���]�[Only�j
					else if ( next.equals("enquete") ) {
						session.removeValue("message");
						session.removeValue("plain_message");
						session.putValue("link_url","/medipro/dr/DrEnquete/mainframe.jsp");
						session.removeValue("limit_yy");
						session.removeValue("limit_mm");
						session.removeValue("limit_dd");
						nextpage = "/medipro/mr/MrMessageBuild4/index.html";
					}
					// �m�F��ʂ�
					else if ( next.equals("confirm") ) {

						boolean next_flg = true;

						if (!checkBccList(myRequest.getParameter("bccList"), session)) {
							next_flg = false;
						}

						// �L�������`�F�b�N
						session.removeValue("limit_date"); // for clear
						if (next_flg &&  checkLimitDate(myRequest,session) ) {
							catchPictureSet(myRequest, session);
							next_flg = true;
						} else { // �G���[�̎��͎����ɖ߂�
							next_flg = false;
						}
						// upload ����
						if ( next_flg ) {
							//	mySmartUpload.upload();
							next_flg = uploadFileCreateCtl(session, mySmartUpload);
						}
						// ���̉�ʂ�ݒ�
						if ( next_flg ) {
							nextpage = "/medipro/mr/MrMessageSend/index.html";
						} else {
							nextpage = myRequest.getParameter("pagename");
						}
					}
					// �����֖߂�i�L���b�`�̔��f�j
					else if ( next.equals("myself") ) {
						nextpage = myRequest.getParameter("pagename");
					}
					// �����N�I����ʂ�
					else if ( next.equals("showlink") ) {
						session.removeValue("start_link_cd");
						session.removeValue("end_link_cd");
						session.removeValue("linklib_action");
						nextpage = "/medipro/mr/MrMessageBuild5/index.html";
					}
					// �����N�I����ʂ���߂�
					else if ( next.equals("back") ) {
						nextpage = (String)session.getValue("pagename");
						setLinkEnv(myRequest, session);
					}
					// �m�F��ʂ���߂�
					else if ( next.equals("confirmback") ) {
						session.removeValue("originalAttachName");
						nextpage = (String)session.getValue("pagename");
					}
					// ��^���I����ʂ� ENICOM
					else if ( next.equals("showteikei") ) {
						// session.removeValue("linklib_action");
						nextpage = "/medipro/mr/MrMessageBuild6/index.html";
					}
				}

				sm.resetMulti(myRequest, session, 1); // �Z�b�V�����ɓo�^ 
				response.sendRedirect(nextpage); // Go to the next page
			} else {
				// �Z�b�V�����G���[�̏ꍇ
				DispatManager dm = new DispatManager();
				dm.distSession(request,response);
			}
		} catch (Exception e) {
			log("", e);
			DispatManager dm = new DispatManager();
			dm.distribute(request,response, e);
		}
    }

    void setSelectDoctor(Request req, HttpSession session) {
		Hashtable hash = new Hashtable();
		String[] drIDs = req.getParameterValues("drID");

        if ( drIDs != null ) {
			for ( int i=0; i < drIDs.length; i++ ) {
				String drid = (String)drIDs[i];
				hash.put(drid, drid);
			}
			session.putValue("drid_hash", hash);
		} else {
			hash.put("EOF", "EOF");
			session.putValue("drid_hash", hash);
		}
    }

    void setLinkEnv(Request req, HttpSession session) {
		if ( req.getParameter("libname") != null ) {
			String libname  = req.getParameter("libname");
			int index  = libname.indexOf('&');
			String liblink_kbn = libname.substring(0, index);
			int start  = index + 1;

			int end  = libname.lastIndexOf('&');//1023 y-yamada add
			String liblink_name = libname.substring(end+1 , libname.length()); //1023 y-yamada add
			String liblink_url = libname.substring(start, end); //1023 y-yamada add

			session.putValue("liblink_kbn" , liblink_kbn);
			session.putValue("liblink_url" , liblink_url);
			session.putValue("liblink_name" , liblink_name);
			//  	    session.putValue("link_url" , liblink_name);
			session.putValue("link_url" , liblink_url);
		} else {
			session.removeValue("liblink_kbn");
			session.removeValue("liblink_url");
			session.removeValue("liblink_name");
			session.removeValue("link_url");
		}
    }

    void catchPictureSet(Request req, HttpSession session) {
        // DB Connection
        DBConnect dbconn = new DBConnect();
        Connection conn  = dbconn.getDBConnect();
		
		try {
			String catch_pictcd  = req.getParameter("mr_gif");
			CatchPctInfoManager manager = new CatchPctInfoManager(conn);
			CatchPctInfo catchinfo  = manager.getCatchPctInfo(catch_pictcd); 
			String catch_picture  = catchinfo.getPicture();
			session.putValue("mr_gif_name", catch_picture);
		} finally {
			dbconn.closeDB(conn);
		}
    }

	boolean checkBccList(String bccList, HttpSession session) {
		if (bccList == null || bccList.trim().equals("")) {
			return true;
		}

		StringTokenizer st = new StringTokenizer(bccList, ";");

		while (st.hasMoreTokens()) {
			String address = st.nextToken();

			if (!checkAddress(address)) {
				session.putValue("bccError", "�A�h���X���s���ł��B(" + address + ")");
				return false;
			}
		}

		return true;
	}

	boolean checkAddress(String address) {
		if (address == null || address.trim().equals("")) {
			return true;
		}
		
		return MailUtil.checkEmailAddress(address);
	}

    boolean checkLimitDate(Request req, HttpSession session) {
		boolean ret  = true;
		int flg  = 0;
		String limit_yy = null;
		String limit_mm = null;
		String limit_dd = null;
		java.util.Date start;
		java.util.Date end;
		java.util.Date now_date = new java.util.Date();

		if ( req.getParameter("limit_yy") != null ) {
			limit_yy = req.getParameter("limit_yy");
			if ( limit_yy.length() > 0 ) {
				flg++;
			}
		}
		if ( req.getParameter("limit_mm") != null ) {
			limit_mm = req.getParameter("limit_mm");
			if ( limit_mm.length() > 0 ) {
				flg++;
			}
		}
		if ( req.getParameter("limit_dd") != null ) {
			limit_dd = req.getParameter("limit_dd");
			if ( limit_dd.length() > 0 ) {
				flg++;
			}
		}
		if ( flg == 0 ) {
			ret = true;
		}
		else if ( flg < 3 ) {
			ret = false;
			session.putValue("limitdateErr", "ON");
		} else {
			int yy = Integer.parseInt(limit_yy.substring(2)) + 100;
			int mm = Integer.parseInt(limit_mm);
			int dd = Integer.parseInt(limit_dd);

			switch (mm) {
			case 2:
			case 4:
			case 6:
			case 9:
			case 11:start = new java.util.Date(yy, mm-1, 1);
				end   = new java.util.Date(yy, mm, 1);
				long ans = Math.abs(end.getTime() - start.getTime()) / 86400000;

				if ( dd > ans ) {
					ret = false;
					session.putValue("limitdateErr", "ON");
				} else {
					ret = true;
				}
				break;
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:ret = true;
				break;
			}
			// �J�����_�[�`�F�b�N��OK�Ȃ�A�ߋ����t�̃`�F�b�N
			if ( ret ) {
				start   = new java.util.Date(yy, mm-1, dd, now_date.getHours(),
											 now_date.getMinutes(),
											 now_date.getSeconds()+1);
				if ( start.before(now_date) ) {
					session.putValue("limitdateTermErr", "ON");
					ret = false;
				} else {
					session.putValue("limit_date", limit_yy+limit_mm+limit_dd);
				}
			}
		}
		return ret;
    }

    void getNewMessage(Request req, HttpSession session) {
		String jikosyokai = null;
		String picture_cd = null;
		String callnaiyo_cd = null;
		String mrID  = (String)session.getValue("com_mrid");
		String drID  = req.getParameter("drID");

        // DB Connection
        DBConnect dbconn = new DBConnect();
        Connection conn  = dbconn.getDBConnect();

		try {
			// ���߂ɑ��������b�Z�[�W���擾���Z�b�g
			MsgManager msgmanager = new MsgManager(conn);
			String  msg_head_id = msgmanager.getNewSendMsg(mrID,drID);
			if ( msg_head_id != null ) {
				MsgInfo      msginfo = msgmanager.getMrSendMessage(msg_head_id);
				MsgBodyInfo  bodyinfo = msginfo.getBody();

				jikosyokai  = bodyinfo.getJikosyokai();
				picture_cd  = bodyinfo.getPictureCD();

				callnaiyo_cd  = bodyinfo.getCallNaiyoCD();
				session.putValue("msg_from", jikosyokai == null ? "" : jikosyokai);
				session.putValue("mr_gif", picture_cd);
				if (callnaiyo_cd != null) {
					session.putValue("callnaiyo", callnaiyo_cd);
				}
			}

			// ����ύX�ׂ̈ɑO�����ă\�[�g�L�[��ݒ肵�Ă���

			session.putValue("nameOrder" , "A");
			session.putValue("senmon1Order" , "A");
			session.putValue("senmon2Order" , "A");
			session.putValue("senmon3Order" , "A");
			session.putValue("kinmuOrder" , "A");
			session.putValue("order" , "A");
			session.putValue("sort"  , "dr_id");
		} finally {
			dbconn.closeDB(conn);
		}
    }

    long getMaxFileSize() {
        // DB Connection
        DBConnect dbconn = new DBConnect();
        Connection conn  = dbconn.getDBConnect();

		try {
			ConstantMasterTableManager  manager = new ConstantMasterTableManager(conn);
			ConstantMasterTable constmanager = manager.getConstantMasterTable("ATTACHMAX");
			Long maxsize;
			long long_primitive = 10L;
			maxsize   = new Long(long_primitive);

			return maxsize.valueOf(constmanager.getNaiyo1().trim()).longValue();
		} finally {
			dbconn.closeDB(conn);
		}
    }

    boolean uploadFileCreateCtl(HttpSession session, SmartUpload mySmartUpload) {
		// UpLoad Object
		UploadFile uploadImage;
		UploadFile uploadFile;
		boolean up_ret = true;

		// �萔�}�X�^�[����Upload��MaxSize���擾
		long maxsize = getMaxFileSize();

		// UpLoad
		UploadFileManager uploadFileManager = new UploadFileManager(mySmartUpload.getFiles());
		// �摜
		uploadImage = uploadFileManager.findUploadFile("img_file");
		// �t�@�C��
		uploadFile = uploadFileManager.findUploadFile("attach_file");


		if ( uploadImage != null ) {
			if (! fileSizeCheck(uploadImage, maxsize) ) {
				up_ret = false;
				session.putValue("uploadImageErr", "ON");
			}
	    
			//1222 �t�@�C�����̍Ō�̕����� "." ��������G���[���b�Z�[�W
			String imageName = uploadImage.getFilePathName();
			int length = imageName.length();
			int index =  imageName.lastIndexOf(".") + 1;
			if( length == index )
				{
					session.putValue("fileNameErr", "ON");
					up_ret = false;

				}

		}
		if ( uploadFile != null ) {
			if (! fileSizeCheck(uploadFile, maxsize) ) {
				up_ret = false;
				session.putValue("uploadFileErr", "ON");
			}
			//1222 �t�@�C�����̍Ō�̕����� "." ��������G���[���b�Z�[�W
			String fileName = uploadFile.getFilePathName();
			int length = fileName.length();
			int index =  fileName.lastIndexOf(".") + 1;
			if( length == index )
				{
					session.putValue("fileNameErr", "ON");
					up_ret = false;

				}


			session.putValue("originalAttachName", uploadFile.getFileName());
		}

		if ( up_ret ) {
			String mrid = (String)session.getValue("com_mrid");
			if ( uploadImage != null ) {
				createUploadFile(session, uploadImage, mrid, "upload_image");
			}
			if ( uploadFile != null ) {
				createUploadFile(session, uploadFile, mrid, "upload_file");
			}
		}

		return up_ret;
    }

    boolean fileSizeCheck(UploadFile upfile, long maxsize) {
		boolean ret = true;

		if ( upfile != null ) {
			if ( upfile.getSize() > maxsize ) {
				ret = false;
			}
		}
		return ret;
    }

    void createUploadFile(HttpSession session,
						  UploadFile upfile,
						  String mrid,
						  String session_name) {
		String session_key = null;

        // DB Connection
        DBConnect dbconn = new DBConnect();
        Connection conn  = dbconn.getDBConnect();

		try {
			if ( upfile != null ) {
				AttachFileInfoManager manager = new AttachFileInfoManager(conn);
				AttachFileInfo attchInfo = manager.getAttachFileInfo(mrid, upfile.getFileName());
				upfile.setServerPath(attchInfo.getInputFullPath());
				upfile.setServerFileName(attchInfo.getInputFile());
				upfile.save();
				session_key = session_name + "_Name";
				session.putValue(session_key, attchInfo.getAttachFullPath());
				session_key = session_name + "_Seq";
				session.putValue(session_key, attchInfo.getSeq());
			}
		} finally {
			dbconn.closeDB(conn);
		}
    } 

//      public final ServletConfig getServletConfig() {
//  		return config;
//      }

//      public final void init(ServletConfig config) throws ServletException {
//  		super.init(config);
//  		this.config = config;
//      }
} 
