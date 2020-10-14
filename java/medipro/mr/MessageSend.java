package medipro.mr;

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.mr.common.util.*;
import jp.ne.sonet.medipro.mr.common.exception.*;
import jp.ne.sonet.medipro.mr.common.servlet.*;
import jp.ne.sonet.medipro.mr.server.controller.*;
import jp.ne.sonet.medipro.mr.server.manager.*;
import jp.ne.sonet.medipro.mr.server.entity.*;

/**
 * Medipro MRメッセージの作成（プレビュー）
 */
public class MessageSend extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
	}

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			// セッションチェック＆セッションがあればリセット
			SessionManager sm = new SessionManager();
			if ( sm.check(request, 1) ) {
				sm.reset(request, 1);
				HttpSession session     = request.getSession(true);

				// メッセージ送信
				if ( sendMessageGo(request, response, session) ) {
					session.putValue("sendmail", "OK");
				} else {
					session.putValue("sendmail", "DRerror");
				} 

				// Go to the next page
				response.sendRedirect("/medipro/mr/MrMessageSend/index.html");
			}
			else {
				// セッションエラーの場合
				DispatManager dm = new DispatManager();
				dm.distSession(request,response);
			}
		} catch (Exception e) {
			log("", e);
			DispatManager dm = new DispatManager();
			dm.distribute(request,response, e);
		}
    }

    boolean sendMessageGo(HttpServletRequest req,
						  HttpServletResponse res,
						  HttpSession session) {
		// DB Connection
		DBConnect dbconn = new DBConnect();
		Connection conn  = dbconn.getDBConnect();

		try {
			MessageTableManager manager = new MessageTableManager(conn);
			MessageTable msgtbl  = new MessageTable();
			MessageHeaderTable msgtblhead = new MessageHeaderTable();
			MessageBodyTable msgtblbody = new MessageBodyTable();

			// 宛先のチェック＆セット
			String      drbuff = null;
			Vector      dr  = new Vector();
			Hashtable   dr_hash = (Hashtable)session.getValue("drid_hash");
			Enumeration enum = dr_hash.elements();
			while ( enum.hasMoreElements() ) {
				drbuff = (String)enum.nextElement();
				dr.addElement(drbuff);
			}
			if ( drbuff.equals("EOF") ) {
				return false;
			}

			//MRからDRへ
			msgtblhead.setMessageKbn("1");
			msgtblhead.setFromUserID((String)session.getValue("com_mrid"));
			msgtblhead.setCcFlg("0");

			if (session.getValue("CompanyCD") != null) {
				msgtblbody.setCompanyCD((String)session.getValue("CompanyCD"));
			}

			if (session.getValue("callnaiyo") != null) {
				msgtblbody.setCallNaiyoCD((String)session.getValue("callnaiyo"));
			}

			if (session.getValue("msg_from") != null) {
				msgtblbody.setJikosyokai((String)session.getValue("msg_from"));
			}

			if (session.getValue("msg_title") != null) {
				msgtblbody.setTitle((String)session.getValue("msg_title"));
			}

			String plainMessage = null;

			if (session.getValue("message") != null) {
				String str = (String)session.getValue("message");
				System.err.println("html message is [" + str + "]");
				plainMessage = (String)session.getValue("plain_message");
				System.err.println("plain message is [" + plainMessage + "]");

				if (plainMessage == null) {
					plainMessage = str;
				}

				msgtblbody.setMessageHonbun(str);
//  				msgtblbody.setMessageHonbun((String)session.getValue("message"));
			}

			if (session.getValue("limit_date") != null) {
				msgtblbody.setYukoKigen((String)session.getValue("limit_date")); 
			}

			if (session.getValue("mr_gif") != null) {
				msgtblbody.setPictureCD((String)session.getValue("mr_gif")); 
			}

			// 直接リンクかライブラリ
			String link_url = null;
			if (session.getValue("link_url") != null) {
				link_url = (String)session.getValue("link_url");

				//originalメッセージ以外
				if (session.getValue("link_type") == null) {
					msgtblbody.setUrl((String)session.getValue("link_url")); 
				}
			}

			msgtbl.setMsgHTable(msgtblhead);
			msgtbl.setMsgBTable(msgtblbody);
 
			//-- 添付ファイルセット
			AttachFileTable  atfiletbl;
			atfiletbl  = new AttachFileTable();
			Vector filetbl_work = new Vector();

			// 画像
			if ( session.getValue("upload_image_Name") != null ) {
				String img_file = (String)session.getValue("upload_image_Name");
				if ( img_file.length() > 0 ) {
					atfiletbl.setAttachFile(img_file);
					atfiletbl.setSeq((String)session.getValue("upload_image_Seq"));
					atfiletbl.setFileKbn("1");
					filetbl_work.addElement(atfiletbl);
				}
			}

			//-- 添付ファイルセット
			atfiletbl  = new AttachFileTable();
			// ファイル
			if ( session.getValue("upload_file_Name") != null ) {
				String attach_file = (String)session.getValue("upload_file_Name");
				if ( attach_file.length() > 0 ) {
					atfiletbl.setAttachFile(attach_file);
					atfiletbl.setSeq((String)session.getValue("upload_file_Seq"));
					atfiletbl.setFileKbn("0");
					filetbl_work.addElement(atfiletbl);
				}
			}
			msgtbl.setAttachFTable(filetbl_work.elements());

			// リンクセット
			AttachLinkTable  atlinktbl = new AttachLinkTable();
			Vector linktbl_work  = new Vector();

			//added by doppe
			//for original message
			if (session.getValue("link_type") != null) {
				String linkUrl = (String)session.getValue("link_url");

				if (linkUrl != null) {
					atlinktbl.setUrl(linkUrl);
					atlinktbl.setHonbuText(linkUrl);
					linktbl_work.addElement(atlinktbl);
				}
			}

			msgtbl.setAttachLTable(linktbl_work.elements());

			String bccList = (String)session.getValue("bccList");

			// Ver1.6 2001.07 M.Mizuki
			// アンケート送信のときにメッセージ送信Logを取る。もし、
			// 送信済みのアンケートであればエラーとする。
			String enqueteId = null;
			String SelectRadio = (String)session.getValue("next_radio");
			if( SelectRadio != null && SelectRadio.equals("enquete") ){
				EnqueteInfo enqinfo = new EnqueteInfo();
				EnqueteManager enqmanager = new EnqueteManager( conn );
				enqmanager.getEnqueteIdMaxTable( enqinfo );
				if( enqinfo.getEnqIdStatus() ){	return false;}
				enqueteId = enqinfo.getEnqId();
			}
			// Message送信
			String msgID = manager.insert(dr.elements(),
							 msgtbl,
							 bccList,
							 plainMessage,
							 enqueteId );
//			String msgID = manager.insert(dr.elements(), msgtbl, bccList, plainMessage );
//			String msgID = manager.insert(dr.elements(), msgtbl);
			// Message送信時刻を取得
			MsgManager msgmanager = new MsgManager(conn);
			MsgInfo  msginfo  = msgmanager.getMrSendMessage(msgID);
			MsgHeaderInfo headinfo = msginfo.getHeader();
			String  sendtime = headinfo.getReceiveTime();

			session.putValue("send_time", sendtime); //送信時刻をセット
		} finally {
			dbconn.closeDB(conn);
		}

		return true;
    }

}
