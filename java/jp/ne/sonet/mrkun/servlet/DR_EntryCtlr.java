package jp.ne.sonet.mrkun.servlet;

import java.io.*;
import java.rmi.*;
import java.text.*;
import java.util.*;
import javax.ejb.*;
import javax.naming.*;
import javax.rmi.*;
import javax.servlet.*;
import javax.servlet.http.*;

import jp.ne.sonet.mrkun.framework.servlet.BaseServlet;
import jp.ne.sonet.mrkun.framework.exception.*;
import jp.ne.sonet.mrkun.sessionEJB.*;
import jp.ne.sonet.mrkun.server.*;
import jp.ne.sonet.mrkun.valueobject.*;

/**
 * Doctor registration screen Servlet class. 
 * New registration of a doctor is performed. If there is DR_ID of DR class, 
 * a screen will be displayed and it will carry out to the registration 
 * processing to DB.
 * @author M.Mizuki
 */
public class DR_EntryCtlr extends BaseServlet
{
  final String  ERROR_TEMPLATE        = HttpConstant.Dr12_View;
  final String  ENTRY_TEMPLATE        = HttpConstant.Dr00_1_View;
  final String  SUCCESS_TEMPLATE      = HttpConstant.Dr00_2_Ctlr;

  // JSP Parameters
  final String  REQUEST_PAGE_DR       = "pageDR";
  final String  INPUT_ERROR           = "DrEntry_inputError";
  final String  ENTRY_CONFIRM         = "DrEntry_confirm";

  // Control Parameters
  final String  ENTRY_CHECK           = "DrEntry_submit";
  final String  ENTRY_SAVE            = "DrEntry_save";
  final String  ENTRY_CANCEL          = "DrEntry_cancel";

  // Parameters
  final String  DR_NAME               = "DrEntry_name";
  final String  DR_KANANAME           = "DrEntry_nameKana";
  final String  DR_HOSPITAL           = "DrEntry_kinmusaki";
  final String  DR_PUBLICOFFICIAL     = "DrEntry_koumuin";
  final String  DR_EMAIL              = "DrEntry_email";
  final String  DR_RE_EMAIL           = "DrEntry_reEmail";

  int  MAX_NAME               = 20;
  int  MAX_KANANAME           = 20;
  int  MAX_HOSPITAL           = 32;
  int  MAX_EMAIL              = 64;


/**
 * Entry Display Control
 **/
  public void doAction(HttpServletRequest request, HttpServletResponse response) {
    DR sessionDR = null;
    String sessionId = getWLCookie(request);

      sessionDR = (DR) checkSession(request);

      if( sessionDR.getDrId()==null )
        throw new SessionFailedException("Error user ID");

      request.setAttribute(REQUEST_PAGE_DR, sessionDR);

      String nextPage = ENTRY_TEMPLATE;

      if( request.getParameter(ENTRY_CHECK) != null) {
        // 確認ボタン
        if ( RegistryData(sessionDR, request) ) {
          request.setAttribute(ENTRY_CONFIRM, "ON");
          nextPage = ENTRY_TEMPLATE;
        }

      } else if (request.getParameter(ENTRY_SAVE) != null) {
        // 登録ボタン
        if ( RegistryData(sessionDR, request) ) {
          if( executeSave(sessionDR, sessionId) ){
            nextPage = SUCCESS_TEMPLATE;

            M3SecurityManager security = new M3SecurityManager();
            DR userDR = security.loginDR(sessionDR.getDrId(), sessionId);
            SessionManager session = new SessionManager(request, new Boolean(true));
            session.setUserItem(userDR);
          }else{
            //The user is already registered.
            request.setAttribute(INPUT_ERROR, SystemMessages.get("dr_entryAlreadyRegistered"));
            nextPage = ENTRY_TEMPLATE;
          }
        }

      }

      // Send to template
      super.forwardToTemplate(nextPage, request, response);

  }


  /**
   *　Parameter Save DR Classes
   */
  private boolean RegistryData(DR userDR, HttpServletRequest request)
  {
    try {
      String testName              = StringUtils.getParameter(request, DR_NAME );
      String testKanaName          = StringUtils.getParameter(request, DR_KANANAME );
      String testHospital          = StringUtils.getParameter(request, DR_HOSPITAL );
      String testPublicOfficial    = StringUtils.getParameter(request, DR_PUBLICOFFICIAL );
      String testEmail             = StringUtils.getParameter(request, DR_EMAIL );
      String testReEmail           = StringUtils.getParameter(request, DR_RE_EMAIL );

      userDR.setKanjiName(testName);
      userDR.setKanaName(testKanaName);
      userDR.setHospital(testHospital);
      userDR.setPublicOfficial(new Boolean(testPublicOfficial));
      userDR.setEmail(testEmail);
      userDR.setReEmail(testReEmail);

      if ( testName == null ||
           testName.equals("") )
        throw new ValidationException(SystemMessages.get("dr_entryNameEmpty"));//"The name field was left empty.");
//      if ( testName.length() > MAX_NAME )
//        throw new ValidationException(SystemMessages.get("dr_entryNameOver"));//"The name field was over length.");
      if ( testKanaName == null ||
           testKanaName.equals("") )
        throw new ValidationException(SystemMessages.get("dr_entryKanaNameEmpty"));//"The kana name field was left empty.");
//      if ( testKanaName.length() > MAX_KANANAME )
//        throw new ValidationException(SystemMessages.get("dr_entryKanaNameOver"));//"The kana name field was over length.");
      if ( testHospital == null ||
           testHospital.equals("") )
        throw new ValidationException(SystemMessages.get("dr_entryHospitalEmpty"));//"The hospital field was left empty.");
      if ( testHospital.length() > MAX_HOSPITAL )
        throw new ValidationException(SystemMessages.get("dr_entryHospitalOver"));//"The hospital field was over length.");
//      if ( testPublicOfficial == null )
//        throw new ValidationException("The Public Official field was left empty.");
      if ( testEmail == null ||
           testEmail.equals("") )
        throw new ValidationException(SystemMessages.get("dr_entryEmailEmpty"));//"The email field was left empty.");
      if ( testEmail.length() > MAX_EMAIL )
        throw new ValidationException(SystemMessages.get("dr_entryEmailOver"));//"The email field was over length.");
      if ( testReEmail == null ||
           testReEmail.equals("") )
        throw new ValidationException(SystemMessages.get("dr_entryRemailEmpty"));//"The email field was left empty.");
      if ( !checkEmail(testEmail,testReEmail) )
        throw new ValidationException(SystemMessages.get("dr_entryEmailRemailDifferent"));//"Email and ReEmail are not in agreement.");

    }
    catch (ValidationException errValid)
    {
      request.setAttribute(INPUT_ERROR, errValid.getMessage());
      return false;
    }
    return true;
  }


  /**
   *　registration execute
   */

  private boolean executeSave(DR userDR, String sessionId) {

    // 医師テーブルに登録
    // コンスタントテーブルからデフォルトMRを取得
    // 選択登録テーブルに登録
    // アンケート送信記録
    try
    {
      // Get MRManager for storing the memo field
      DRManagerRemoteIntf drManager = (DRManagerRemoteIntf) new EJBUtil().getManager(HttpConstant.DRMANAGER_HOME);
      drManager.addDR(userDR, sessionId);
    }
    catch (RemoteException remoteErr)
    {
      return false;
    }
    catch (Exception e)
    {
      // Throw ApplicationError if remote network problem happens
      throw new ApplicationError(this.getClass().getName() + 
      ": Problem on setting the doctor field in Entry.", e );
    }
    return true;
  }




/**
		// アンケート送信記録 MRK Ver1.6 2001.07 M.Mizuki
			EnqueteInfo enqinfo = new EnqueteInfo( prop.getDrId(), "0000" );
			session.putValue("UserEnquete", enqinfo);

			EnqueteController enqcont = new EnqueteController( enqinfo, con );
			enqcont.getMaxEnqueteId( request, response );
			enqinfo.setMessageHeaderId( "" );
			enqcont.setEnqueteSendLog( request, response );

		} catch (Exception ex) {
			throw new MrException(ex);
		} finally {
			dbConnect.closeDB(con);
		}

		return count;
    }

    String getConstTable(String keyword, int colno) {
		String value = null;

        // DB Connection
        DBConnect dbconn = new DBConnect();
        Connection conn  = dbconn.getDBConnect();

		try {
			ConstantMasterTableManager  manager = new ConstantMasterTableManager(conn);
			ConstantMasterTable constmanager = manager.getConstantMasterTable(keyword);

			if ( colno == 1 ) {
				value = constmanager.getNaiyo1().trim();
			} else {
				value = constmanager.getNaiyo2().trim();
			}
		} finally {
			dbconn.closeDB(conn);
		}

		return value;
    }
**/
    /**
     *　メールアドレスの論理チェック
     */
    boolean checkEmail(String email, String reEmail) {
		if (email == null || reEmail == null || !email.equals(reEmail)) {
			return false;
		}
	
		int aCount = 0;
		// 半角英数字であることの確認
		for (int i = 0; i < email.length(); i++) {
			char c = email.charAt(i);
			if ((c >= 'a' && c <= 'z') || 
				(c >= 'A' && c <= 'Z') ||
				(c >= '0' && c <= '9') ||
				(c == '.') || (c == '_') || (c == '-') || (c == '/') ||
				(c == '@')) {
				if (c =='@') aCount++;
			} else {
				return false;
			}
		}
		if (aCount == 1) return true;
		return false;
    }
}
