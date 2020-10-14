package medipro.ap;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import medipro.ap.util.*;
import medipro.ap.entity.*;
import medipro.ap.request.*;

/**
 * �O��Interface�T�[�r�X�N���X.
 * weblogic.properties�ɓo�^���Ďg��.
 * @author  doppe
 * @version 1.0 (created at 2001/12/04 13:37:04)
 */
public class MessageService extends HttpServlet {
	
    /**
     * ���O�o�͐ݒ�
     * @param  config
     */
    public void init(ServletConfig config) {
        Logger.setContext(config.getServletContext());
        Logger.setMode(Logger.DEBUG);
//          Logger.setMode(Logger.INFO);
    }

    /**
     * �O��Interface����
     * @param  request
     * @param  response
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        Request apRequest = null;
        APException exception = null;

        // �v������
        try {
            Logger.log("�v����͒�...");
            //�v���̉��
            apRequest = Request.makeRequest(request);

        } catch (Exception ex) {
            //�K��O�f�[�^���
            Logger.error("��Q����", ex);
            return;
        }

        Result result = null;

        if (!apRequest.isError()) {
            //�v���̏���
            Logger.log("�v��������...");
            result = apRequest.execute();
        }

        // �ȉ���������
        response.setContentType("application/x-unknown");
        response.setHeader("Content-Disposition",
                           "attachment; filename=" + "result.txt");

        try {
            ServletOutputStream sos = response.getOutputStream();

            if (!apRequest.isError()) {
                result.writeResponse(sos);
            } else {
				//���ڕs��
                apRequest.getError().write(sos);
            }
				
            sos.flush();
            sos.close();
        } catch (Exception e) {
            Logger.error("��Q����", e);
        }
    }

}
