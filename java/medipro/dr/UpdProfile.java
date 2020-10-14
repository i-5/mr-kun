package medipro.dr;

import java.io.*;
import java.util.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.mr.common.exception.*;
import jp.ne.sonet.medipro.mr.common.util.*;
import jp.ne.sonet.medipro.mr.common.servlet.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
import jp.ne.sonet.medipro.mr.server.manager.*;
import jp.ne.sonet.medipro.mr.server.controller.*;

/**
 * Medipro DR�v���t�B�[���ύX
 */
public class UpdProfile extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			// �Z�b�V�����`�F�b�N���Z�b�V����������΃��Z�b�g
			SessionManager sm = new SessionManager();
			if ( sm.check(request, 0) ) {
				HttpSession session = request.getSession(true);

				// reset of session
				sm.reset(request,0);

				// ��t�̃v���t�B�[���X�V
				msgDRUpdate(request,response,session);

				// Go to the next page
				response.sendRedirect("/medipro/dr/DrUpdProfile/end.html");  
			} else {
				// �Z�b�V�����G���[�̏ꍇ
				DispatManager dm = new DispatManager();
				dm.distSession(request,response);
			}
		} catch (Exception e) {
			log("", e);
			DispatManager dm = new DispatManager();
			dm.distribute(request,response, e);
		}
    }

    void msgDRUpdate(HttpServletRequest req,
					 HttpServletResponse res,
					 HttpSession session) {
		// DR�̏����X�V����
		String drid  = (String)session.getValue("com_drid");
		String dname  = (String)session.getValue("d_name");
		String dkana  = (String)session.getValue("d_kana");
		String dkinmusaki = (String)session.getValue("d_kinmusaki");
		String djob  = (String)session.getValue("d_job");
 
		// DB connection
		DBConnect dbconn = new DBConnect();
		Connection conn = dbconn.getDBConnect();

		try {
			// Manager���
			DoctorInfoManager doctorinfomanager = new DoctorInfoManager(conn);
			DoctorInfo docinfo = new DoctorInfo();
			docinfo.setDrID(drid);
			docinfo.setName(dname);
			docinfo.setNameKana(dkana);
			docinfo.setKinmusakiName(dkinmusaki);
			docinfo.setKoumuin(djob);
			doctorinfomanager.update(docinfo);
		} finally { 
			// DB close
			dbconn.closeDB(conn);
		}
    }

}


