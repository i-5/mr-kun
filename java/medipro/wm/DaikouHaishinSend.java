package medipro.wm;

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.wm.common.util.*;
import jp.ne.sonet.medipro.mr.common.exception.*;
import jp.ne.sonet.medipro.mr.common.servlet.*;
import jp.ne.sonet.medipro.mr.server.controller.*;
import jp.ne.sonet.medipro.mr.server.manager.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
import java.util.*;

/**
 * Medipro MRメッセージの作成（プレビュー）
 */
public class DaikouHaishinSend extends HttpServlet {

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
				if ( daikouHaishinSend(request, response, session) ) {
					session.putValue("sendmail", "OK");
				} else {
					session.putValue("sendmail", "DRerror");
				} 

				// Go to the next page
				response.sendRedirect("/medipro/wm/WmDaikouHaishin/MessageSend/index.html");
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

    boolean daikouHaishinSend(HttpServletRequest req,
						  HttpServletResponse res,
						  HttpSession session)
    {
        
       DBConnect conn = new DBConnect(); 
       Connection connection =  conn.getDBConnect();
       Hashtable daikou = (Hashtable) session.getValue("daikou");   
       Vector mrs = (Vector) daikou.get("chosen_mrs");
       Hashtable drsByMr = (Hashtable) daikou.get("drsByMr");
        Enumeration list = mrs.elements();
       
       	jp.ne.sonet.medipro.mr.server.manager.MrInfoManager manager = new jp.ne.sonet.medipro.mr.server.manager.MrInfoManager(connection);
       
       while(list.hasMoreElements())
       {
        jp.ne.sonet.medipro.mr.server.entity.MrInfo mrinfo = (jp.ne.sonet.medipro.mr.server.entity.MrInfo) list.nextElement();
        String mrid = (String) mrinfo.getId();   
        Vector drs = (Vector) drsByMr.get(mrid);
        session.putValue("drid_hash",drs);
   	mrinfo = manager.getMrLoginInfo(mrid);
        String JikoSyokai = mrinfo.getJikosyokai();
        if ( JikoSyokai != null )
        {
            session.putValue("msg_from",mrinfo.getJikosyokai());
        
        }
        else
        {
            session.putValue("msg_from","");
        }
        session.putValue("from_mrid",mrid);
	if ( mrinfo.getPictureCd() != null )
	{
        	session.putValue("mr_gif", mrinfo.getPictureCd());
	}
        
        sendHaishinGo(req,res,session);
        session.removeValue("mr_gif");
        session.removeValue("from_mrid");
        session.removeValue("drid_hash");
        session.removeValue("msg_from");

       }
       return true; 
        
    }
    
    boolean sendHaishinGo(HttpServletRequest req,
						  HttpServletResponse res,
						  HttpSession session) {
		// DB Connection
		DBConnect dbconn = new DBConnect();
		Connection conn  = dbconn.getDBConnect();

			MessageTableManager manager = new MessageTableManager(conn);
			MessageTable msgtbl  = new MessageTable();
			MessageHeaderTable msgtblhead = new MessageHeaderTable();
			MessageBodyTable msgtblbody = new MessageBodyTable();

			// 宛先のチェック＆セット
			String      drbuff = null;
			Vector      dr  = new Vector();
			Vector   dr_hash = (Vector) session.getValue("drid_hash");
			if ( dr_hash.size() == 0 )
			{
			  try
			  {
				conn.close();
		          }
			  catch (SQLException e) {}
			 return(false);
			}
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
			msgtblhead.setFromUserID((String)session.getValue("from_mrid"));
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
                        else
                        {
                            msgtblbody.setTitle("No Title");
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

			//String bccList = (String)session.getValue("bccList");

			// Message送信
			String msgID = manager.insert(dr.elements(), msgtbl, null, plainMessage);
//  			String msgID = manager.insert(dr.elements(), msgtbl);

			// Message送信時刻を取得
			MsgManager msgmanager = new MsgManager(conn);
			MsgInfo  msginfo  = msgmanager.getMrSendMessage(msgID);
			MsgHeaderInfo headinfo = msginfo.getHeader();
			String  sendtime = headinfo.getReceiveTime();

			session.putValue("send_time", sendtime); //送信時刻をセット
		        try
			{
				conn.close();
		        }
			catch (SQLException e) {}

		return true;
    }

}
