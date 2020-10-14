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
 * <strong>Medipro �l�q�����ꗗ�Ǘ�</strong>
 * @author
 * @version
 */
public class AttributeListServlet extends HttpServlet {

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
            // �l�q�����ꗗ��ʗp�Z�b�V�������擾�������ݒ�
            if ( !AttributeListController.initSession(request, response) ) {
                return;
            }

            HttpSession session = request.getSession(true);
            String comKey = SysCnst.KEY_COMMON_SESSION;
            Common common = (Common)session.getValue(comKey);
            String subKey = SysCnst.KEY_ATTRIBUTELIST_SESSION;
            AttributeListSession subSession = (AttributeListSession)session.getValue(subKey);

            String url = "wm/WmAttributeList/index.html";
            String target = null;
            subSession.setStatus(AttributeListSession.STATE_NORMAL);

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
                Vector v = getAttributeCd(request, subSession);
                                // �m�F
                if ( target.equals("confirm") == true ) {
                    // �폜
                    if ( v.size() > 0 ) {
                        AttributeListController ctrl =
                            new AttributeListController();
                        boolean bDelete = 
                            isDelete(request, response, common,
                                     subSession, ctrl, v);
                        subSession.setStatus(bDelete ? AttributeListSession.STATE_CONFIRM_1
                                             : AttributeListSession.STATE_ALERT_2
                                             );
                    }
                    else {
                        subSession.setStatus(AttributeListSession.STATE_ALERT_1);
                    }
                }
                                // �폜
                else if ( target.equals("delete") == true ) {
                    if ( request.getParameter("yes") != null ) {
                        if ( v.size() > 0 ) {
                            AttributeListController ctrl = new AttributeListController();
                            boolean bDelete = isDelete(request, response, common, subSession, ctrl, v);
                            if ( bDelete ) {
                                ctrl.deleteDisplay(request, response, common, subSession, v);
                                subSession.clearCheck();
                                subSession.setStatus(AttributeListSession.STATE_REPORT_1);
                            }
                            else {
                                subSession.setStatus(AttributeListSession.STATE_ALERT_2);
                            }
                        }
                        else { // �K�v���H
                            subSession.setStatus(AttributeListSession.STATE_ALERT_1);
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
     * �l�q�����R�[�h�̎��o���ƃ`�F�b�N��Ԃ̍X�V
     * @param request HttpServletRequest
     * @param session AttributeListSession
     * @return �l�q�����R�[�h
     */
    private Vector getAttributeCd(HttpServletRequest request, AttributeListSession session) {
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
     * @param ses �l�q�����ꗗ��ʗp�Z�b�V�������
     * @param ctrl �l�q�����ꗗ��ʐ���I�u�W�F�N�g
     * @param list �폜�Ώۂ̃����N�R�[�h���X�g
     * @return �����N�R�[�h
     */
    private boolean isDelete(HttpServletRequest req, HttpServletResponse res,
                             Common com, AttributeListSession ses,
                             AttributeListController ctrl, Vector list) {
        boolean bDelete = false;
        bDelete = ctrl.isDeleteDisplay(req, res, com, ses, list);
        return bDelete;
    }
}
