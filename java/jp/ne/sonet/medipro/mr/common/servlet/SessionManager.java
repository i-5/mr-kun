package jp.ne.sonet.medipro.mr.common.servlet;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.mr.common.exception.*;
import jp.ne.sonet.medipro.mr.common.*;
import jp.ne.sonet.medipro.wm.common.util.Converter;
import com.jspsmart.upload.*;
import jp.ne.sonet.mrkun.server.MultipartHandler;

/**
 * Session Check or Reset 
 */
public class SessionManager {
 
    public SessionManager() {}

    public boolean check(HttpServletRequest req, int dr_mr_flg) {
		HttpSession session = req.getSession(true);

		try {
			// セッションデータがあるかどうか確認
			if (session.isNew()){
				return false;
			} else {  
				String  id  = null;

				if (dr_mr_flg == 0) {
					// Doctor check
					id = (String)session.getValue("com_drid");
				} else {
					// MR check
					id = (String)session.getValue("com_mrid");
				}

				if (id == null || id.length() == 0) {
					return false;
				}

				// 2001.04.24 追加 -->
				// 極めて稀だが正常にセッションを取得できなかった場合
				// ただし、パラメータにIDがセットされているときのみ照合
				
                                String sessionID = (String)session.getValue(SysCnst.MR_SESSION_ID);

                                /* <hb010703 why is this needed and where is it set?? 
                                 
                                String requestID = req.getParameter(SysCnst.MR_SESSION_ID);
				
				if (requestID != null) {
					if (!sessionID.equals(requestID)) {
						System.err.println("Caution! illegal state occured.");
						System.err.println("requestID = " + requestID);
						System.err.println("Date      = " + new Date());
						session.putValue("IllegalStateError", "aValue");
						return false;
					}
				} else {
					System.err.println("Parameter ID is not set.");
				}
				// <-- 2001.04.24
                                 * hb010703>
                                 */
			}
		} catch (Exception e) {
			return false;
		}

		return true;
    }

	/**
	 * jspSmartUploadのRequest使用時
	 */
    public boolean check(HttpServletRequest req, int dr_mr_flg, Request myRequest) {
		HttpSession session = req.getSession(true);

		try {
			// セッションデータがあるかどうか確認
			if (session.isNew()){
				return false;
			} else {  
				String  id  = null;
				if (dr_mr_flg == 0) {
					// Doctor check
					id = (String)session.getValue("com_drid");
				} else {
					// MR check
					id = (String)session.getValue("com_mrid");
				}

				if (id == null || id.length() == 0) {
					return false;
				}

				// 2001.04.24 追加 -->
				// 極めて稀だが正常にセッションを取得できなかった場合
				// ただし、パラメータにIDがセットされているときのみ照合
				String requestID = myRequest.getParameter(SysCnst.MR_SESSION_ID);
				String sessionID = (String)session.getValue(SysCnst.MR_SESSION_ID);

				if (requestID != null) {
					if (!sessionID.equals(requestID)) {
						System.err.println("Caution! illegal state occured.");
						System.err.println("requestID = " + requestID);
						System.err.println("Date      = " + new Date());
						session.putValue("IllegalStateError", "aValue");
						return false;
					}
				} else {
					System.err.println("Parameter ID is not set.");
				}
				// <-- 2001.04.24
			}
		} catch (Exception e) {
			return false;
		}

		return true;
    }

    public boolean check(HttpServletRequest req, int dr_mr_flg, MultipartHandler mph) {
		HttpSession session = req.getSession(true);

		try {
			// セッションデータがあるかどうか確認
			if (session.isNew()){
				return false;
			} else {  
				String  id  = null;
				if (dr_mr_flg == 0) {
					// Doctor check
					id = (String)session.getValue("com_drid");
				} else {
					// MR check
					id = (String)session.getValue("com_mrid");
				}

				if (id == null || id.length() == 0) {
					return false;
				}

				// 2001.04.24 追加 -->
				// 極めて稀だが正常にセッションを取得できなかった場合
				// ただし、パラメータにIDがセットされているときのみ照合
				String requestID = (String) mph.getParameter(SysCnst.MR_SESSION_ID);
				String sessionID = (String)session.getValue(SysCnst.MR_SESSION_ID);

				if (requestID != null) {
					if (!sessionID.equals(requestID)) {
						System.err.println("Caution! illegal state occured.");
						System.err.println("requestID = " + requestID);
						System.err.println("Date      = " + new Date());
						session.putValue("IllegalStateError", "aValue");
						return false;
					}
				} else {
					System.err.println("Parameter ID is not set.");
				}
				// <-- 2001.04.24
			}
		} catch (Exception e) {
			return false;
		}

		return true;
    }
    public void reset(HttpServletRequest req, int flg) { 

			HttpSession session = req.getSession(true);

			// セッションデータを再登録する
			Enumeration e = req.getParameterNames();
			while( e.hasMoreElements()) {
				String key = (String)e.nextElement();
				if (!key.equals(SysCnst.MR_SESSION_ID)) {
					String[] value = req.getParameterValues(key);
					session.putValue(key, req.getParameterValues(key));
				}
			}
    }

    public void resetMulti(Request req, HttpSession session, int flg) { 
		// セッションデータを再登録する
		Enumeration e = req.getParameterNames();
		while( e.hasMoreElements()) {
			String key = (String)e.nextElement();
			if (!key.equals(SysCnst.MR_SESSION_ID)) {
				String value = Converter.enToSjis(req.getParameter(key));
				session.putValue(key, value);
			}
		}
    }

    public void resetMulti(MultipartHandler mph, HttpSession session, int flg) { 
		// セッションデータを再登録する
		Collection keys = mph.getParameters().keySet();
		for ( Iterator e = keys.iterator(); e.hasNext(); )
		{
			String key = (String)e.next();
			if (!key.equals(SysCnst.MR_SESSION_ID)) {
				Object param = mph.getParameter(key);
				if (param instanceof String)
				  session.putValue(key, Converter.enToSjis((String) param));
				else
				  session.putValue(key, param);
			}
		}
    }
}
