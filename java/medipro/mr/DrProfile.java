package medipro.mr;

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.mr.common.util.*;
import jp.ne.sonet.medipro.mr.common.exception.*;
import jp.ne.sonet.medipro.mr.common.servlet.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
import jp.ne.sonet.medipro.mr.server.manager.*;

/**
 * Medipro MR�ڋq�v���t�B�[��
 */
public class DrProfile extends HttpServlet {

   public void doGet(HttpServletRequest request, HttpServletResponse response) {
       doPost(request, response);
   }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			// �Z�b�V�����`�F�b�N���Z�b�V����������΃��Z�b�g
			SessionManager sm = new SessionManager();
			if ( sm.check(request, 1) ) {
				sm.reset(request,0);

				HttpSession session = request.getSession(true);

				if (request.getParameter("MrDrProfile_update_action") != null) {
					//�X�V
					profileUpdate(request, session);
					session.putValue("MrDrProfile_update_yes", "OK");
				} else if (request.getParameter("MrDrProfile_delete_yes") != null) {
					//�X�V�m�F
					profileDelete(request, session);
					session.putValue("MrDrProfile_delete_yes", "OK");
					response.sendRedirect("/medipro.mr.DrRollList");
					return;
				} else if (request.getParameter("MrDrProfile_nodr") != null) {
					//��t�ꗗ�ɖ߂�
					response.sendRedirect("/medipro.mr.DrRollList");
					return;
				} else if (request.getParameter("init") != null) {
					//������
					session.removeValue("MrDrProfile_update_yes");
					session.removeValue("MrDrProfile_delete_action");
					session.removeValue("MrDrProfile_delete_cancel");
					session.removeValue("MrDrProfile_delete_yes");
				}

				// Go to the next page
				response.sendRedirect("/medipro/mr/MrDrProfile/index.html");
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

    void profileUpdate(HttpServletRequest request, HttpSession session) {
		// DB Connection
		DBConnect dbconn = new DBConnect();
		Connection conn  = dbconn.getDBConnect();

		try {
			String drID = (String)session.getValue("m37_drid");
			String mrID = (String)session.getValue("com_mrid");

			TantoInfoManager  manager = new TantoInfoManager(conn);
			TantoInfo         tantoinfo = new TantoInfo();

			tantoinfo.setName(  (String)session.getValue("drName") );
			tantoinfo.setKinmusaki(  (String)session.getValue("kinmusaki") );
			tantoinfo.setMakerCustID( (String)session.getValue("custID") );
			tantoinfo.setMakerShisetsuID( (String)session.getValue("shisetuID") );
			tantoinfo.setTargetRank( (String)session.getValue("targetRank") );
			tantoinfo.setSyokusyu(  (String)session.getValue("syokusyu") );
			tantoinfo.setSenmon1(  (String)session.getValue("senmon1") );
			tantoinfo.setSenmon2(  (String)session.getValue("senmon2") );
			tantoinfo.setSenmon3(  (String)session.getValue("senmon3") );
			tantoinfo.setYakusyoku(  (String)session.getValue("yakusyoku") );
			tantoinfo.setSotsugyoDaigaku( (String)session.getValue("daigaku") );
			tantoinfo.setSotsugyoYear( (String)session.getValue("sotsugyoYear") );
			tantoinfo.setSyumi(  (String)session.getValue("syumi") );
			tantoinfo.setSonota(  (String)session.getValue("sonota") );

			manager.updateDrProfile(tantoinfo, drID, mrID);
		} finally {
			// DB Connection close
			dbconn.closeDB(conn);
		}
    }

    void profileDelete(HttpServletRequest request, HttpSession session) {
		// DB Connection
		DBConnect dbconn = new DBConnect();
		Connection conn  = dbconn.getDBConnect();

		try {
			String drID = (String)session.getValue("m37_drid");
			String mrID = (String)session.getValue("com_mrid");

			DoctorInfoManager docManager = new DoctorInfoManager(conn);
			DoctorInfo docInfo = docManager.getDoctorInfo(drID);//DR �̏����擾

			TantoInfoManager  manager = new TantoInfoManager(conn);
			TantoInfo info = new TantoInfo();
			info.setName(docInfo.getName());//DR�̖��O��ݒ�
			//�ڋq�����N���A�{�^�����������Ƃ��E��Ɉ�t������
			info.setSyokusyu("��t");	// y-yamada add 1013 

			//�f�t�H���g�̋Ζ�����擾���� 1027 y-yamada add
			String kinmusaki = docManager.getKinmusaki(drID);//1027 y-yamada add
			info.setKinmusaki( kinmusaki ); //1027 y-yamada add

			//1024 y-yamada company �e�[�u������TargetRank���擾
			String companyCD = docManager.getCompanyCD(mrID);
			String targetRank = docManager.getTargetRank(companyCD);
			info.setTargetRank(targetRank);//1023 y-yamada �^�[�Q�b�g�����N�̐ݒ�
	
			manager.updateDrProfile(info, drID, mrID);
			//   manager.insertSentakuTourokuHist(drID, mrID);
		} finally {
			// DB Connection close
			dbconn.closeDB(conn);
		}
    }
}
