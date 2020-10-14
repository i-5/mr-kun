package medipro.ap;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import medipro.ap.util.*;
import medipro.ap.entity.*;
import medipro.ap.request.*;

/**
 * 外部Interfaceサービスクラス.
 * weblogic.propertiesに登録して使う.
 * @author  doppe
 * @version 1.0 (created at 2001/12/04 13:37:04)
 */
public class MessageService extends HttpServlet {
	
    /**
     * ログ出力設定
     * @param  config
     */
    public void init(ServletConfig config) {
        Logger.setContext(config.getServletContext());
        Logger.setMode(Logger.DEBUG);
//          Logger.setMode(Logger.INFO);
    }

    /**
     * 外部Interface入口
     * @param  request
     * @param  response
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        Request apRequest = null;
        APException exception = null;

        // 要求処理
        try {
            Logger.log("要求解析中...");
            //要求の解析
            apRequest = Request.makeRequest(request);

        } catch (Exception ex) {
            //規定外データ種別
            Logger.error("障害発生", ex);
            return;
        }

        Result result = null;

        if (!apRequest.isError()) {
            //要求の処理
            Logger.log("要求処理中...");
            result = apRequest.execute();
        }

        // 以下応答処理
        response.setContentType("application/x-unknown");
        response.setHeader("Content-Disposition",
                           "attachment; filename=" + "result.txt");

        try {
            ServletOutputStream sos = response.getOutputStream();

            if (!apRequest.isError()) {
                result.writeResponse(sos);
            } else {
				//項目不足
                apRequest.getError().write(sos);
            }
				
            sos.flush();
            sos.close();
        } catch (Exception e) {
            Logger.error("障害発生", e);
        }
    }

}
