package medipro.dr;

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.mr.common.*;
import jp.ne.sonet.medipro.mr.common.exception.*;
import jp.ne.sonet.medipro.mr.common.util.*;
import jp.ne.sonet.medipro.mr.common.servlet.*;
import jp.ne.sonet.medipro.mr.server.manager.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
import jp.ne.sonet.medipro.mr.server.controller.*;

/**
 * Doctor �A���P�[�g
 */
public class UserEnqueteV1 extends HttpServlet {

    // �i�[����Filed��
    static final String FILEDNAME = "company";

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			// �Z�b�V�����`�F�b�N���Z�b�V����������΃��Z�b�g
			SessionManager sm = new SessionManager();
			if (sm.check(request, 0)) {
				HttpSession session = request.getSession(true);
				String action = request.getParameter("action");

				// Save Parameter
				DataSaveParameter(request,response, session);

				//Next Page
				String nextPage = "/medipro/dr/DrEnquete/EnqueteVol1.jsp";

				if( session.getValue("DrEnquete_inputError") == null ){
					EnqueteInfo enqinfo = (EnqueteInfo)session.getValue("UserEnquete");

					String header_id = enqinfo.getMessageHeaderId();
//					log( header_id );
					if( header_id == null ) nextPage = "/medipro.dr.InitMrInput";
				}
				// Go to the next page
				response.sendRedirect( nextPage );
			} else {
				// �Z�b�V�����G���[�̏ꍇ
				DispatManager dm = new DispatManager();
				dm.distSession(request,response);
			}
		} catch (Exception e) {
			log("", e);
			DispatManager dm = new DispatManager();
			dm.distribute(request,response,e);
		}
    }


    /**
     * �����R�[�h�ϊ�
     */
    private String getParameter(HttpServletRequest request, String name) {
		String value = request.getParameter(name);
		try {
			return new String(value.getBytes("8859_1"), "SJIS");
		} catch (Exception e) {
			return "";
		}
    }

    /**
     *�@�A���P�[�g���ʂ�DB�Ɋi�[
     */
    private void DataSaveParameter(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		session.removeValue("DrEnquete_inputError");

		EnqueteInfo enqinfo = (EnqueteInfo)session.getValue("UserEnquete");

		if (enqinfo == null) {
			String drId = (String)session.getValue("com_drid");
			String EnqId = getParameter(request, "EnqId");
			enqinfo = new EnqueteInfo( drId, EnqId );
			session.putValue("UserEnquete", enqinfo);
		}

		EnqueteController controller = new EnqueteController( enqinfo );

		// �A���P�[�g�͉񓚍ς݂�
		if ( controller.isEnqueteReplyStatus( request, response ) ){
			return;
		}

		// �A���P�[�g�ɓ��ӂ��Ȃ��ꍇ
//		String notagree = request.getParameter("notagree");
		if ( request.getParameter("notagree") != null ){
//			log("notagree = '" + notagree + "'");
			controller.updateEnqueteSendLog(request, response);
			return;
		}

		// �A���P�[�g�̉񓚂��i�[����B
		String GroupId = getParameter(request, "GroupId");
		int ii;
		String[] val = request.getParameterValues(FILEDNAME);
		String Answer = null;
		try {
			for( ii=0; ii<val.length; ii++ ){
				Answer = new String(val[ii].getBytes("8859_1"), "SJIS");
				if( Answer.equals( "" ) )	continue;
				EnqueteAnswer ans = new EnqueteAnswer();
				ans.MinorId = GroupId;
				ans.Filed = FILEDNAME;
				ans.Answer = Answer;
				log("'" + ans.Answer + "'");
				enqinfo.addEnqueteAnswer( ans );
			}
		} catch (Exception e) {
		}

		if( enqinfo.getEnqueteAnswer() == null ){
			session.putValue("DrEnquete_inputError","NotSelectError");
		}
		else{
			controller.setEnqueteAnswer(request, response);
		}
		controller.closeDB();

		return;
	}

}
