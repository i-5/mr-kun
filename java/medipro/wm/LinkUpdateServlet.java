package medipro.wm;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.wm.server.entity.*;
import jp.ne.sonet.medipro.wm.server.session.*;
import jp.ne.sonet.medipro.wm.server.controller.*;
import jp.ne.sonet.medipro.wm.common.exception.*;
import jp.ne.sonet.medipro.wm.common.servlet.*;
import jp.ne.sonet.medipro.wm.common.util.*;
import jp.ne.sonet.medipro.wm.common.*;


/**
 * Medipro リンク追加・変更管理
 * @author
 * @version
 */
public class LinkUpdateServlet extends HttpServlet {

    /**
     * doGet
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        doPost(request, response);
    }

    /**
     * doPost
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            // リンク追加・変更画面用セッション情報取得＆初期設定
            if ( LinkUpdateController.initSession(request, response) != true ) {
                return;
            }

            HttpSession session = request.getSession(true);
            String comKey = SysCnst.KEY_COMMON_SESSION;
            Common common = (Common)session.getValue(comKey);
            String subKey = SysCnst.KEY_LINKUPDATE_SESSION;
            LinkUpdateSession subSession = (LinkUpdateSession)session.getValue(subKey);

            String url = "wm/WmLinkUpdate/index.html";

            subSession.setStatus(LinkUpdateSession.STATE_NORMAL);

            /*1023　y-yamada & があるかチェック */
			String string = (String)Converter.getParameter(request, "title");
log("string="+string);
			int check = -1;
			if (string != null)
			{
				check = string.indexOf("&");
			}
			/*1023 y-yamada　チェックここまで　*/

                        // 「確認」呼出し
            if ( request.getParameter("check") != null ) {
                String lcd = request.getParameter("link_cd");
                if ( lcd != null ) {
                    LinkUpdateController ctrl = new LinkUpdateController();
                    LinkLibInfo info = ctrl.createDisplay(request, response, common, lcd);
                    subSession.setLinkLibInfo(info);
                }
                else {
                    subSession.setLinkLibInfo(null);
                }
            }
            // 「保存」ボタン押下
            else if ( request.getParameter("confirm") != null ) {
				if (check == -1)
				{//1023 y-yamada "&"がなければ保存
                	updateTarget(request, response, common, subSession);
                	subSession.setStatus(LinkUpdateSession.STATE_CONFIRM);
                }
                else
                {//1023　y-yamada "&"があった場合
		            subSession.setStatus(LinkUpdateSession.TEXT_ERR);
				}
            }
            // 「保存しますか？」「はい」ボタン押下
            else if ( request.getParameter("update") != null ) {
				if (check == -1)
				{//1023 y-yamada "&"がなければ保存
	                updateTarget(request, response, common, subSession);
	                LinkLibInfo info = subSession.getLinkLibInfo();
	                LinkUpdateController ctrl = new LinkUpdateController();
	                ctrl.updateDisplay(request, response, common, info);
	                subSession.setStatus(LinkUpdateSession.STATE_REPORT);
	            }
                else
                {//1023　y-yamada "&"があった場合
		            subSession.setStatus(LinkUpdateSession.TEXT_ERR);
				}
            }
            // 「元に戻る」ボタン押下
            else if ( request.getParameter("back") != null ) {
                subSession.setLinkLibInfo(null);
                url = "wm/WmLinkList/index.html";
            }
            else {
				if(check == -1)
				{//1023 y-yamada "&"がなければ保存
                	updateTarget(request, response, common, subSession);
                }
                else
                {//1023　y-yamada "&"があった場合
		            subSession.setStatus(LinkUpdateSession.TEXT_ERR);
				}
            }
            if ( SysCnst.DEBUG ) {
                log("URL="+url);
            }
            response.sendRedirect(SysCnst.HTML_ENTRY_POINT+url);
        } 
        catch (Exception e) {
		log("", e);
            DispatManager dm = new DispatManager();
            dm.distribute(request, response, e);
        }
    }

    /**
     * 対象の状態更新
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param common ログイン者の情報を積んだセッション情報
     * @param sub リンクの追加・更新画面情報を積んだセッション情報
     */
    private void updateTarget(HttpServletRequest request,
                              HttpServletResponse response,
                              Common common,
                              LinkUpdateSession sub) {
        LinkLibInfo info = sub.getLinkLibInfo();
        if ( info == null )
                info = new LinkLibInfo();



        info.setLinkBunruiCd(Converter.getParameter(request, "bunrui"));
        info.setUrl(Converter.getParameter(request, "link"));
        info.setHonbunText(Converter.getParameter(request, "title"));
        info.setDescription(Converter.getParameter(request, "description"));
        sub.setLinkLibInfo(info);
    }

}
