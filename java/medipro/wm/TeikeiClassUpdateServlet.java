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
 * Medipro �����N���ޒǉ��E�ύX�Ǘ�
 * @author
 * @version
 */
public class TeikeiClassUpdateServlet extends HttpServlet {

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
            // �����N���ޒǉ��E�ύX��ʗp�Z�b�V�������擾�������ݒ�
            if ( ! TeikeiClassUpdateController.initSession(request, response) ) {
                return;
            }

            HttpSession session = request.getSession(true);
            String comKey = SysCnst.KEY_COMMON_SESSION;
            Common common = (Common)session.getValue(comKey);
            String subKey = SysCnst.KEY_TEIKEICLASSUPDATE_SESSION;
            TeikeiClassUpdateSession subSession =        (TeikeiClassUpdateSession)session.getValue(subKey);

            String url = "wm/WmExpressionClassUpdate/index.html";

            subSession.setStatus(TeikeiClassUpdateSession.STATE_NORMAL);

                        // �u�m�F�v�ďo��
            if ( request.getParameter("check") != null ) {
                String lcd = request.getParameter("teikeibun_bunrui_cd");
                if ( lcd != null ) {
                    TeikeiClassUpdateController ctrl = new TeikeiClassUpdateController();
                    TeikeiClassInfo info = ctrl.createDisplay(request, response, common, lcd);
                    subSession.setTeikeiClassInfo(info);
                }
                else {
                    subSession.setTeikeiClassInfo(null);
                }
            }
            // �u�ۑ��v�{�^������
            else if ( request.getParameter("confirm") != null ) {
                updateTarget(request, response, common, subSession);
                subSession.setStatus(TeikeiClassUpdateSession.STATE_CONFIRM);
            }
            // �u�ۑ����܂����H�v�u�͂��v�{�^������
            else if ( request.getParameter("update") != null ) {
                updateTarget(request, response, common, subSession);
                TeikeiClassInfo info = subSession.getTeikeiClassInfo();
                TeikeiClassUpdateController ctrl =
                    new TeikeiClassUpdateController();
                ctrl.updateDisplay(request, response, common, info);
                subSession.setStatus(TeikeiClassUpdateSession.STATE_REPORT);
            }
            // �u���ɖ߂�v�{�^������
            else if ( request.getParameter("back") != null ) {
                subSession.setTeikeiClassInfo(null);
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
     * @param sub �����N���ނ̒ǉ��E�X�V��ʏ���ς񂾃Z�b�V�������
     */
    private void updateTarget(HttpServletRequest request,
                              HttpServletResponse response,
                              Common common,
                              TeikeiClassUpdateSession sub) {
        TeikeiClassInfo info = sub.getTeikeiClassInfo();
        if ( info == null )        info = new TeikeiClassInfo();
        String code = Converter.getParameter(request, "code");
        int idx = code.indexOf(',');
        if ( idx > 0 )        code = code.substring(0, idx);
        info.setBunruiCode(code);
        info.setBunruiName(Converter.getParameter(request, "name"));
        sub.setTeikeiClassInfo(info);
    }

}
