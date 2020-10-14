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
			// �Z�b�V�����f�[�^�����邩�ǂ����m�F
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

				// 2001.04.24 �ǉ� -->
				// �ɂ߂ċH��������ɃZ�b�V�������擾�ł��Ȃ������ꍇ
				// �������A�p�����[�^��ID���Z�b�g����Ă���Ƃ��̂ݏƍ�
				
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
	 * jspSmartUpload��Request�g�p��
	 */
    public boolean check(HttpServletRequest req, int dr_mr_flg, Request myRequest) {
		HttpSession session = req.getSession(true);

		try {
			// �Z�b�V�����f�[�^�����邩�ǂ����m�F
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

				// 2001.04.24 �ǉ� -->
				// �ɂ߂ċH��������ɃZ�b�V�������擾�ł��Ȃ������ꍇ
				// �������A�p�����[�^��ID���Z�b�g����Ă���Ƃ��̂ݏƍ�
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
			// �Z�b�V�����f�[�^�����邩�ǂ����m�F
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

				// 2001.04.24 �ǉ� -->
				// �ɂ߂ċH��������ɃZ�b�V�������擾�ł��Ȃ������ꍇ
				// �������A�p�����[�^��ID���Z�b�g����Ă���Ƃ��̂ݏƍ�
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

			// �Z�b�V�����f�[�^���ēo�^����
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
		// �Z�b�V�����f�[�^���ēo�^����
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
		// �Z�b�V�����f�[�^���ēo�^����
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
