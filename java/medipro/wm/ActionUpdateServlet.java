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
 * Medipro �d�v�x�ǉ��E�ύX�Ǘ�
 * @author 
 * @version
 */
public class ActionUpdateServlet extends HttpServlet {

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
            // �d�v�x�ǉ��E�ύX��ʗp�Z�b�V�������擾�������ݒ�
            if ( !ActionUpdateController.initSession(request, response) ) {
                return;
            }

            HttpSession session = request.getSession(true);
            String comKey = SysCnst.KEY_COMMON_SESSION;
            Common common = (Common)session.getValue(comKey);
            String subKey = SysCnst.KEY_ACTIONUPDATE_SESSION;
            ActionUpdateSession subSession = (ActionUpdateSession)session.getValue(subKey);

            String url = "wm/WmActionUpdate2/index.html";

            subSession.setStatus(ActionUpdateSession.STATE_NORMAL);

            // �u�m�F�v�ďo��
            if ( request.getParameter("check") != null ) {
                String code = request.getParameter("target_rank");
                if ( code != null ) {
                    ActionUpdateController ctrl = new ActionUpdateController();
                    ActionInfo info = ctrl.createDisplay(request, response, common, code);
                    subSession.setActionInfo(info);
                }
                else {
                    subSession.setActionInfo(null);
                }
            }
            // �u�ۑ��v�{�^������
            else if ( request.getParameter("confirm") != null ) {
                updateTarget(request, response, common, subSession);
                subSession.setStatus(ActionUpdateSession.STATE_CONFIRM);
            }
            // �u�ۑ����܂����H�v�u�͂��v�{�^������
            else if ( request.getParameter("update") != null ) {
                updateTarget(request, response, common, subSession);
                ActionInfo info = subSession.getActionInfo();
                ActionUpdateController ctrl =
                    new ActionUpdateController();
                ctrl.updateDisplay(request, response, common, info);
                subSession.setStatus(ActionUpdateSession.STATE_REPORT);
            }
            // �u���ɖ߂�v�{�^������
            else if ( request.getParameter("back") != null ) {
                subSession.setActionInfo(null);
                url = "wm/WmActionUpdate/index.html";
            }
            else {
                updateTarget(request, response, common, subSession);
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
     * �Ώۂ̏�ԍX�V
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param common ���O�C���҂̏���ς񂾃Z�b�V�������
     * @param sub �d�v�x�̒ǉ��E�X�V��ʏ���ς񂾃Z�b�V�������
     */
    private void updateTarget(HttpServletRequest request,
                              HttpServletResponse response,
                              Common common,
                              ActionUpdateSession sub) {
        ActionInfo info = sub.getActionInfo();
        if ( info == null )        info = new ActionInfo();
        info.setTargetName(Converter.getParameter(request, "target_name"));
        sub.setActionInfo(info);
    }

}
