package medipro.dr;

import java.io.*;
import java.util.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.mr.common.*;
import jp.ne.sonet.medipro.mr.common.exception.*;
import jp.ne.sonet.medipro.mr.common.util.*;
import jp.ne.sonet.medipro.mr.common.servlet.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
import jp.ne.sonet.medipro.mr.server.manager.*;
import jp.ne.sonet.medipro.mr.server.controller.*;

/**
 * Medipro ����DR&MR�o�^
 */
public class InitMrInput extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			// �Z�b�V�����`�F�b�N���Z�b�V����������΃��Z�b�g
			SessionManager sm = new SessionManager();
			if (sm.check(request, 0)) {
				HttpSession session = request.getSession(true);

				// �{�^�������session���N���A
				session.removeValue("msg");
				session.removeValue("Confirm");
				session.removeValue("insert_action");
				session.removeValue("insert_cancel");
				session.removeValue("show_input");

				String nextPage = "/medipro/dr/DrInitMrInput/mainframe.jsp";

				// �u�{�l���m�F����v�������ꂽ�Ƃ�
				// ��Ђ�I�������ꂽ�Ƃ�
				if (request.getParameter("showPrefix") == null)	{
					nextPage = "/medipro/dr/DrInitMrInput/mainframe.jsp";
					response.sendRedirect(nextPage); 
					return;
				}
		    
				if (request.getParameter("showPrefix").equals("ON")) {
					setPrefix(request, session);
				}

				// �uMR��o�^���܂����H�v�̊m�F�{�^���������ꂽ�Ƃ�
				else if (request.getParameter("insert_action") != null) {
					setSentakuToroku(session);
					nextPage = "/medipro/dr/DrInitMrInput/mainframe.jsp"; 
				} else if (request.getParameter("insert_cancel") != null) {
					nextPage = "/medipro.dr.MrRollList?";
				} else {
					loadMrInfo(request,response,session);
					nextPage = "/medipro/dr/DrInitMrInput/mainframe.jsp"; 
				}

				response.sendRedirect(nextPage); 
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

    /**
     * ���Prefix�̎擾
     */
    void setPrefix(HttpServletRequest req, HttpSession session) {
		String companycd = req.getParameter("companycd");
		Hashtable hash = (Hashtable)session.getValue("prefixTbl");
		String prefixCD = (String)hash.get(companycd);
		if (prefixCD == null) {
			prefixCD = "";
			companycd = "";
		}
	
		session.putValue("companycd" , companycd);
		session.putValue("input_mrid", prefixCD);
    }

    /**
     * �w�肵��MR�̏���ǂݍ���
     */
    void loadMrInfo(HttpServletRequest req,
					HttpServletResponse res,
					HttpSession session) {
		// DBConnection
		DBConnect dbconn = new DBConnect();
		Connection conn  = dbconn.getDBConnect();

		String drid = (String)session.getValue("com_drid");
		String mrid = req.getParameter("input_mrid");
		session.putValue("input_mrid", mrid);

		try {
			// MRID�ɊY������MR�̏�񂪃e�[�u���ɑ��݂��邩�ǂ����̊m�F
			MrInfoManager mim = new MrInfoManager(conn);
			MrInfo mrinfo = mim.getMrInfoCheck(mrid);

			if (mrinfo != null) {
				// MR�����ɓo�^����Ă��邩�ǂ����̊m�F     
				TantoInfoManager tim = new TantoInfoManager(conn);
				if (tim.getMrInfo(drid,mrid) != null) {
					session.putValue("msg","TOROKUZUMI");
				} else {
					CatchPctInfo catchinfo = mrinfo.getMrCatchpctinfo();
					/////////////////////////////////////////////////////////////
					//log("�摜�@="+catchinfo.getPicture());
					////////////////////////////////////////////////////////////
					if( catchinfo != null )
						{//�l�q���摜�o�^���Ă���
							session.putValue("showPicture",  catchinfo.getPicture());
						}
					session.putValue("msg","OK");
					session.putValue("showCompanyName", mrinfo.getCompanyName());
					session.putValue("showMrName",  mrinfo.getName());
				}
			} else {
				log(mrid);
				session.putValue("msg","NG");
			}
		} finally {
			dbconn.closeDB(conn);
		}
    }

    /**
     * MR��o�^����
     */
    void setSentakuToroku(HttpSession session) {
		String drid = (String)session.getValue("com_drid");
		String mrid = (String)session.getValue("input_mrid");

		// DB connection
		DBConnect dbconn = new DBConnect();
		Connection conn = dbconn.getDBConnect();

		try {
			// MRID�ɊY������MR�̏�񂪃e�[�u���ɑ��݂��邩�ǂ����̊m�F
			MrInfoManager mim = new MrInfoManager(conn);
			MrInfo mrinfo = mim.getMrInfoCheck(mrid);
			if(mrinfo == null)
				{
					log(mrid);
					session.putValue("msg","NG");
					return;
				}
		
			TantoInfoManager tantoinfomanager = new TantoInfoManager(conn);

			/*y-yamada add 1017 start �o�^�{�^����x�����ɂ��
			  ��d�o�^���~�߂�	*/

			// MR�����ɓo�^����Ă��邩�ǂ����̊m�F     
			if ( tantoinfomanager.getMrInfo(drid,mrid) != null ) {
				//���ɓo�^����Ă����甲����
				return;
			}
			/*y-yamada add 1017 end*/

			//�ߋ��Ɋ֌W�����������Ƃ��Ȃ��ꍇ�̂ݗݐ�MR�����A�b�v
			if (!tantoinfomanager.hadRelation(drid, mrid)) {
				DoctorInfoManager docManager = new DoctorInfoManager(conn);
				//		docManager.updateMrInput(drid);
				docManager.updateMrInput(drid , mrid);// y-yamada add 1016
			}

			tantoinfomanager.insertSentakuTouroku(drid, mrid);

			session.putValue("msg", "SAVE_OK");
// Ver1.6 M.Mizuki			sendChargedMessage(drid, mrid);
			sendChargedMessage(drid, mrid, conn);
		} finally {
			dbconn.closeDB(conn);
		}
    }

    /**
     * MR�ɓo�^�ʒm�𑗕t����.
     * @param drId DR-ID
     * @param mrId MR-ID
     */
// Ver1.6 M.Mizuki    void sendChargedMessage(String drId, String mrId) {
    void sendChargedMessage(String drId, String mrId, Connection connection) {
// Ver1.6 M.Mizuki		DBConnect dbConnect = new DBConnect();
// Ver1.6 M.Mizuki		Connection connection = dbConnect.getDBConnect();

		MessageTableManager manager = new MessageTableManager(connection);
		MessageTable message = new MessageTable();
		MessageHeaderTable header = new MessageHeaderTable();
		MessageBodyTable body = new MessageBodyTable();

		try {
			DoctorInfoManager docManager = new DoctorInfoManager(connection);
			DoctorInfo docInfo = docManager.getDoctorInfo(drId);
			String drName = docInfo.getName() == null ? "" : docInfo.getName();
			String kinmusaki
				= docInfo.getKinmusakiName() == null ? "" : docInfo.getKinmusakiName();
			//Ver1.6 M.Mizuki
			String drSysCD = docInfo.getSysDrCD() == null ? "" : docInfo.getSysDrCD();

			header.setMessageKbn(SysCnst.MESSAGE_KBN_TO_OTHER);
			header.setFromUserID(drId);
			body.setTitle("MR�V�K�o�^�ʒm:" + drName + " " + kinmusaki);
			body.setMessageHonbun("�V�K��MR�o�^����܂����B\n"
					+ "�ڋq:" + drName + " " + kinmusaki + "\n" + drSysCD);
// Ver1.6 M.Mizuki				  + "�ڋq:" + drName + " " + kinmusaki);

			message.setMsgHTable(header);
			message.setMsgBTable(body);

			Vector toList = new Vector();
			toList.addElement(mrId);

			//-- �Y�t�E�����N�t�@�C���Z�b�g�i���e�J���j
			Vector attach = new Vector();
			message.setAttachFTable(attach.elements());
			message.setAttachLTable(attach.elements());

			manager.insert(toList.elements(), message);
		} finally {
// Ver1.6 M.Mizuki			dbConnect.closeDB(connection);
		}
    }

}


