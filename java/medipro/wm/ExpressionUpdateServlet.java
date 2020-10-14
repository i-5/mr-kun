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
 * Medipro ��^���ǉ��E�ύX�Ǘ�
 * @author 
 * @version
 */
public class ExpressionUpdateServlet extends HttpServlet {

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
            // ��^���ǉ��E�ύX��ʗp�Z�b�V�������擾�������ݒ�
            if ( !ExpressionUpdateController.initSession(request, response) ) {
                return;
            }

            HttpSession session = request.getSession(true);
            String comKey = SysCnst.KEY_COMMON_SESSION;
            Common common = (Common)session.getValue(comKey);
            String subKey = SysCnst.KEY_EXPRESSIONUPDATE_SESSION;
            ExpressionUpdateSession subSession = (ExpressionUpdateSession)session.getValue(subKey);

            String url = "wm/WmExpressionUpdate/index.html";

            subSession.setStatus(ExpressionUpdateSession.STATE_NORMAL);

            // �u�m�F�v�ďo��
            if ( request.getParameter("check") != null ) {
                String code = request.getParameter("teikeibun_cd");
                if ( code != null ) {
                    ExpressionUpdateController ctrl = new ExpressionUpdateController();
                    ExpressionLibInfo info = ctrl.createDisplay(request, response, common, code);
                    subSession.setExpressionLibInfo(info);
                }
                else {
                    subSession.setExpressionLibInfo(null);
                }
            }
            // �u�ۑ��v�{�^������
            else if ( request.getParameter("confirm") != null ) {
                updateTarget(request, response, common, subSession);
                subSession.setStatus(ExpressionUpdateSession.STATE_CONFIRM);
            }
            // �u�ۑ����܂����H�v�u�͂��v�{�^������
            else if ( request.getParameter("update") != null ) {
                updateTarget(request, response, common, subSession);
                ExpressionLibInfo info = subSession.getExpressionLibInfo();
                ExpressionUpdateController ctrl =
                    new ExpressionUpdateController();
                ctrl.updateDisplay(request, response, common, info);
                subSession.setStatus(ExpressionUpdateSession.STATE_REPORT);
            }
            // �u���ɖ߂�v�{�^������
            else if ( request.getParameter("back") != null ) {
                subSession.setExpressionLibInfo(null);
                url = "wm/WmExpressionList/index.html";
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
     * @param sub ��^���̒ǉ��E�X�V��ʏ���ς񂾃Z�b�V�������
     */
    private void updateTarget(HttpServletRequest request,
                              HttpServletResponse response,
                              Common common,
                              ExpressionUpdateSession sub) {
        ExpressionLibInfo info = sub.getExpressionLibInfo();
        if ( info == null )        info = new ExpressionLibInfo();
        info.setTitle(Converter.getParameter(request, "title"));
        info.setDescription(Converter.getParameter(request, "description"));
        info.setBunruiCode(Converter.getParameter(request, "bunrui"));
        info.setHonbun(Converter.getParameter(request, "honbun"));
        sub.setExpressionLibInfo(info);
    }

}
