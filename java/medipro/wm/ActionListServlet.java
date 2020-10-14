package medipro.wm;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.wm.server.session.*;
import jp.ne.sonet.medipro.wm.server.controller.*;
import jp.ne.sonet.medipro.wm.common.exception.*;
import jp.ne.sonet.medipro.wm.common.servlet.*;
import jp.ne.sonet.medipro.wm.common.*;
import jp.ne.sonet.medipro.wm.common.util.*;

/**
 * MR kun Action
 * @author
 * @version
 */
public class ActionListServlet extends HttpServlet {

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
            // �d�v�x�ꗗ��ʗp�Z�b�V�������擾�������ݒ�
            if ( ! ActionListController.initSession(request, response) ) {
                return;
            }

            HttpSession session = request.getSession(true);
            String comKey = SysCnst.KEY_COMMON_SESSION;
            Common common = (Common)session.getValue(comKey);
            String subKey = SysCnst.KEY_ACTION_SESSION;
            ActionListSession subSession = (ActionListSession)session.getValue(subKey);

            String url = "wm/WmActionUpdate/index.html";
            String target = null;
            subSession.setStatus(ActionListSession.STATE_NORMAL);

            // �\�[�g����
            if ( (target = request.getParameter("sort")) != null ) {
                subSession.setSortKey(target);
                String order = subSession.getOrder();
                order = order.equals("ASC") ? "DESC" : "ASC";
                subSession.setOrder(order);
                subSession.clearCheck();
            }
            // ���s����
            else if ( (target = request.getParameter("execute")) != null ) {
                Vector v = getTeikeibunCd(request, subSession);
                // �m�F
                if ( target.equals("confirm") == true ) {
                    if ( v.size() > 0 ) {
                        subSession.setStatus(ActionListSession.STATE_CONFIRM);
                    }
                    else {
                        subSession.setStatus(ActionListSession.STATE_ALERT);
                    }
                }
                // �폜
                else if ( target.equals("delete") == true ) {
                    if ( request.getParameter("yes") != null ) {
                        if ( v.size() > 0 ) {
                            ActionListController ctrl =
                                new ActionListController();
                            ctrl.deleteDisplay(request, response,
                                               common, subSession, v);
                            subSession.clearCheck();
                            subSession.setStatus(ActionListSession.STATE_REPORT);
                        }
                        else { // �K�v���H
                            subSession.setStatus(ActionListSession.STATE_ALERT);
                        }
                    }
                }
            }
            // Go to the next page
            response.sendRedirect(SysCnst.HTML_ENTRY_POINT + url);
        } catch (Exception e) {
		log("", e);
            DispatManager dm = new DispatManager();
            dm.distribute(request,response, e);
        }
    }

    /**
     * ��^���R�[�h�̎��o���ƃ`�F�b�N��Ԃ̍X�V
     * @param request HttpServletRequest
     * @param session ActionListSession
     * @return ��^���R�[�h
     */
    private Vector getTeikeibunCd(HttpServletRequest request,
                                  ActionListSession session) {
        Vector v = new Vector();
        session.clearCheck();
        String s[] = request.getParameterValues("checkbox");
        if ( s != null ) {
            for ( int i = 0; i < s.length; i++ ) {
                v.addElement(s[i]);
                session.setCheck(s[i], "checked");
            }
        }
        return v;
    }
}
