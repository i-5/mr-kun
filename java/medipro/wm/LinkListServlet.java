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
 * Medipro �����N�ꗗ�Ǘ�
 * @author
 * @version
 */
public class LinkListServlet extends HttpServlet {

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
            // �����N�ꗗ��ʗp�Z�b�V�������擾�������ݒ�
            if ( LinkListController.initSession(request, response) != true ) {
                return;
            }

            HttpSession session = request.getSession(true);
            String comKey = SysCnst.KEY_COMMON_SESSION;
            Common common = (Common)session.getValue(comKey);
            String subKey = SysCnst.KEY_LINKLIST_SESSION;
            LinkListSession subSession = (LinkListSession)session.getValue(subKey);

            String url = "wm/WmLinkList/index.html";
            String target = null;
            subSession.setStatus(LinkListSession.STATE_NORMAL);

            String defLinkCd = request.getParameter("radiobutton");
            subSession.setDefaultLinkCd(defLinkCd);

            // �\�[�g����
            if ( (target = request.getParameter("sort")) != null ) {
                subSession.setSortKey(target);
                String order = subSession.getOrder();
                order = order.equals("ASC") ? "DESC" : "ASC";
                subSession.setOrder(order);
                subSession.setPage(1);
                subSession.clearCheck();
            }
            // �y�[�W�؂�ւ�����
            else if ( (target = request.getParameter("page")) != null ) {
                int page = subSession.getPage();
                if ( target.equals("prev") && subSession.hasPrevPage() ) {
                    page--;
                }
                else if ( target.equals("next") && subSession.hasNextPage() ) {
                    page++;
                }
                subSession.setPage(page);
                subSession.clearCheck();
            }
            // ���s����
            else if ( (target = request.getParameter("execute")) != null ) {
                Vector v = getLinkCd(request, subSession);
                                // �m�F
                if ( target.equals("confirm") == true ) {
                    // �ۑ�
                    if ( request.getParameter("update") != null ) {
                        subSession.setStatus(LinkListSession.STATE_CONFIRM_2);
                    }
                    // �폜
                    else {
                        if ( v.size() > 0 ) {
                            LinkListController ctrl = new LinkListController();
                            boolean bDelete = isDelete(request, response, common, subSession, ctrl, v);
                            subSession.setStatus(bDelete ? LinkListSession.STATE_CONFIRM_1
                                                 : LinkListSession.STATE_ALERT_2
                                                 );
                        }
                        else {
                            subSession.setStatus(LinkListSession.STATE_ALERT_1);
                        }
                    }
                }
                                // �ۑ�
                if ( target.equals("update") == true ) {
                    if ( request.getParameter("yes") != null ) {
                        LinkListController ctrl = new LinkListController();
                        ctrl.updateDisplay(request, response,
                                           common, subSession,
                                           subSession.getDefaultLinkCd());
                        subSession.setStatus(LinkListSession.STATE_REPORT_2);
                    }
                }
                                // �폜
                if ( target.equals("delete") == true ) {
                    if ( request.getParameter("yes") != null ) {
                        if ( v.size() > 0 ) {
                            LinkListController ctrl = new LinkListController();
                            boolean bDelete = isDelete(request, response, common, subSession, ctrl, v);
                            if ( bDelete ) {
                                ctrl.deleteDisplay(request, response,
                                                   common, subSession, v);
                                subSession.clearCheck();
                                subSession.setStatus(LinkListSession.STATE_REPORT_1);
                            }
                            else {
                                subSession.setStatus(LinkListSession.STATE_ALERT_2);
                            }
                        }
                        else { // �K�v���H
                            subSession.setStatus(LinkListSession.STATE_ALERT_1);
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
     * �����N�R�[�h�̎��o���ƃ`�F�b�N��Ԃ̍X�V
     * @param request HttpServletRequest
     * @param session LinkListSession
     * @return �����N�R�[�h
     */
    private Vector getLinkCd(HttpServletRequest request,
                             LinkListSession session) {
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

    /**
     * �폜���s�m�F
     * @param req HttpServletRequest
     * @param res HttpServletResponse
     * @param com ���p�Z�b�V�������
     * @param ses �����N�ꗗ��ʗp�Z�b�V�������
     * @param ctrl �����N�ꗗ��ʐ���I�u�W�F�N�g
     * @param list �폜�Ώۂ̃����N�R�[�h���X�g
     * @return �����N�R�[�h
     */
    private boolean isDelete(HttpServletRequest req, HttpServletResponse res,
                             Common com, LinkListSession ses,
                             LinkListController ctrl, Vector list) {
        boolean bDelete = true;
        String code = ctrl.createDisplaySub(req, res, com, ses);
        if ( code != null ) {
            for ( int i = 0; i < list.size() ; i ++ ) {
                if ( code.equals((String)list.elementAt(i)) == true ) {
                    bDelete = false;
                    break;
                }
            }
        }
        return bDelete;
    }
}
