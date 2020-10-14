package jp.ne.sonet.medipro.wm.common.servlet;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.wm.common.*;
import jp.ne.sonet.medipro.wm.common.exception.*;
import jp.ne.sonet.medipro.wm.server.session.*;


/**
 * <strong>セッション管理</strong>
 * <br>
 * @author: 
 * @version: 
 */
public class SessionManager {

    /**
     * SessionManager オブジェクトを新規に作成する。
     */
    public SessionManager(){}

    /**
     * ログイン情報、権限をチェックする。
     * @param req javax.servlet.http.HttpServletRequest
     * @return 正常／異常
     */
    public static boolean check(HttpServletRequest request) {
        boolean status = false;

        try {
            HttpSession session = request.getSession(true);
            // セッションデータがあるかどうか確認
            if ( session.isNew() != true ) {
                String comKey = SysCnst.KEY_COMMON_SESSION;
                Common comSes = (Common)session.getValue(comKey);
                if ( comSes != null ) {
                    status = true;
                }
            }
        } catch (Exception e) {
//              throw new WmException(e);
        }

        return status;
    }
    
    /**
     * セッション情報をリセット（ログイン情報以外を削除）する。
     * @param req javax.servlet.http.HttpServletRequest
     */
    public static void reset(HttpServletRequest request) {   
        try {
            HttpSession session = request.getSession(true);
            String comKey = SysCnst.KEY_COMMON_SESSION;
            Common comSes = (Common)session.getValue(comKey);
            String[] sessionNames = session.getValueNames();
            if (sessionNames != null) {
                for ( int i = 0; i < sessionNames.length; i++ ) {
                    if ( sessionNames[i].startsWith(comKey) != true ) {
                        session.removeValue(sessionNames[i]);
                    }
                }
            }
        } catch (Exception e) {
            throw new WmException(e);
        }
    }

}
