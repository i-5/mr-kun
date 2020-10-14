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
 * Medipro �l�q�����ǉ��E�ύX�Ǘ�
 * @author
 * @version
 */
public class AttributeUpdateServlet extends HttpServlet {

    /**
     * doGet
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response){
        doPost(request, response);
    }

    /**
     * doPost
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            // �l�q�����ǉ��E�ύX��ʗp�Z�b�V�������擾�������ݒ�
            if ( ! AttributeUpdateController.initSession(request, response) ) {
                return;
            }

            HttpSession session = request.getSession(true);
            String comKey = SysCnst.KEY_COMMON_SESSION;
            Common common = (Common)session.getValue(comKey);
            String subKey = SysCnst.KEY_ATTRIBUTEUPDATE_SESSION;
            AttributeUpdateSession subSession = (AttributeUpdateSession)session.getValue(subKey);

            String url = "wm/WmAttributeUpdate/index.html";

            subSession.setStatus(AttributeUpdateSession.STATE_NORMAL);

            // �u�m�F�v�ďo��
            if ( request.getParameter("check") != null ) {
                String code = request.getParameter("mr_attribute_cd");
                if ( code != null ) {
                    AttributeUpdateController ctrl = new AttributeUpdateController();
                    AttributeInfo info = ctrl.createDisplay(request, response, common, code);
                    subSession.setAttributeInfo(info);
                }
                else {
                    subSession.setAttributeInfo(null);
                }
            }
            // �u�ۑ��v�{�^������
            else if ( request.getParameter("confirm") != null ) {
                updateTarget(request, response, common, subSession);
                subSession.setStatus(AttributeUpdateSession.STATE_CONFIRM);
            }
            // �u�ۑ����܂����H�v�u�͂��v�{�^������
            else if ( request.getParameter("update") != null ) {
                updateTarget(request, response, common, subSession);
                AttributeInfo info = subSession.getAttributeInfo();
                AttributeUpdateController ctrl = new AttributeUpdateController();
                ctrl.updateDisplay(request, response, common, info);
                subSession.setStatus(AttributeUpdateSession.STATE_REPORT);
            }
            // �u���ɖ߂�v�{�^������
            else if ( request.getParameter("back") != null ) {
                subSession.setAttributeInfo(null);
                url = "wm/WmAttributeList/index.html";
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
     * �Ώۂl�q�����̏�ԍX�V
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param common ���O�C���҂̏���ς񂾃Z�b�V�������
     * @param sub �l�q�����ǉ��E�X�V��ʏ���ς񂾃Z�b�V�������
     */
    private void updateTarget(HttpServletRequest request,
                              HttpServletResponse response,
                              Common common,
                              AttributeUpdateSession sub) {
        AttributeInfo info = sub.getAttributeInfo();
        if ( info == null )        info = new AttributeInfo();
        String code = Converter.getParameter(request, "code");
        int idx = code.indexOf(',');
        if ( idx > 0 )        code = code.substring(0, idx);
        info.setMrAttributeCd(code);
        info.setMrAttributeName(Converter.getParameter(request, "name"));
        sub.setAttributeInfo(info);
    }

}
