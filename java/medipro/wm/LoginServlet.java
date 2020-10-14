package medipro.wm;

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.wm.common.*;
import jp.ne.sonet.medipro.wm.common.util.*;
import jp.ne.sonet.medipro.mr.common.util.HtmlTagUtil;
import jp.ne.sonet.medipro.wm.common.exception.*;
import jp.ne.sonet.medipro.wm.common.servlet.*;
import jp.ne.sonet.medipro.wm.server.entity.*;
import jp.ne.sonet.medipro.wm.server.manager.*;
import jp.ne.sonet.medipro.wm.server.session.*;

/**
 * <strong>Medipro Webmaster ���O�C��.</strong>
 * @author
 * @version
 */
public class LoginServlet extends HttpServlet {

    /**
     * ���O�C���T�[�r�X�̒�`.
     * @param request  �v���I�u�W�F�N�g
     * @param response �����I�u�W�F�N�g
     */
    public void service(HttpServletRequest request, HttpServletResponse response) {
	if (SysCnst.DEBUG) {
	    log("LoginServlet called!");
	}

	// DB Connection
	DBConnect dbconn = new DBConnect();
	Connection connection  = null;
	
	try {
	    connection  = dbconn.getDBConnect();

	    //�Z�b�V�����̐V�K�쐬
	    HttpSession session = request.getSession(true);

            
            /* <hb010716
             * this - as in medipro.mr.Login
             * does not seem necessary and creates problems with Tomcat
             * sessions
             * hb010716>
             
	    if (!session.isNew()) {
		//�Z�b�V�������̃��Z�b�g
		session.invalidate();
		session = request.getSession(true);
	    }
            */
	    String nextPage = SysCnst.HTML_ENTRY_POINT + "wm/WmMrList/index.html";
	    String submit  = request.getParameter("submitaction");
	    session.removeValue("login_err");
	    session.removeValue("pwdchg_err");
	    session.removeValue("login_wmid");

	    if (submit.equals("login")) {
		//���O�C���{�^��
		if (!loginCheck(request, connection)) {
		    session.putValue("login_err", "pwderror");
		    nextPage = null;
		}
	    } else if (submit.equals("passchg")) {
		if (!passwdChange(request, connection)) {
		    session.putValue("pwdchg_err", "pwderror");
		    nextPage = null;
		}
	    } else {
		//����ȊO�͂Ƃ肠�����G���[...
		throw new Exception("��������Ă܂���!");
	    }

            /*
             * <hb010801
             * this should actually be done for all the SessionManager.check() calls
            String sessionId = HtmlTagUtil.getRandomId();
            session.putValue(SysCnst.MR_SESSION_ID, sessionId);
             * hb010801>
             */

	    if (nextPage == null) {
		session.putValue("login_wmid", request.getParameter("com_mrid"));
		nextPage = SysCnst.HTML_ENTRY_POINT + "wm/WmLogin/index.jsp";
	    } else {
		//���ʕϐ��̎擾
		Common common = (Common)session.getValue(SysCnst.KEY_COMMON_SESSION);

		//�E�F�u�}�X�^�[�ƃT�u�}�X�^�[�ȊO�̓G���[��ʂ�
		if (!common.getMasterFlg().equals(SysCnst.FLG_MASTER_WEB) &&
		    !common.getMasterFlg().equals(SysCnst.FLG_MASTER_SUB)) {
		    new DispatManager().distAuthority(request, response);
		    return;
		}

		//�萔�̎擾
		ConstantMasterInfoManager manager = new ConstantMasterInfoManager(connection);
		manager.refreshCommon(common);
		
		
		/**************************************************************
		MR��user-agent��loginmr�e�[�u���ɕۑ����� 1212 y-yamada add
		***************************************************************/
		String mrId = request.getParameter("com_mrid");
		if( mrId != null )
		{	
			session.putValue("wmId" , mrId );
			new AccessWmAnalyzer(mrId).analyze(request);
		}

		//�Z�b�V�����^�C���A�E�g�̐ݒ�
		session.setMaxInactiveInterval(common.getTimeout());

		Cookie cookie = new Cookie("Mrid", mrId);
		cookie.setMaxAge(864000);
		cookie.setPath("/");
		response.addCookie(cookie);
	    }

	    // Go to the next page
	    response.sendRedirect(nextPage);
	} catch (Exception e) {
	    //�G���[��ʂɑJ��
		log("", e);
	    new DispatManager().distribute(request,response, e);
	} finally {
	    // DB Connection close
	    dbconn.closeDB(connection);
	}
    }

    /**
     * info�̓��e��Common�ɔ��f����B
     */
    private void refreshCommon(MrInfo info, HttpSession session) {
	Common common = (Common)session.getValue(SysCnst.KEY_COMMON_SESSION);
	if (common == null) {
	    common = new Common();
	}

	common.setMrId(info.getMrId());
	common.setCompanyCd(info.getCompanyCd());
	common.setShitenCd(info.getShitenCd());
	common.setEigyosyoCd(info.getEigyosyoCd());
	common.setMrAttributeCd1(info.getMrAttributeCd1());
	common.setMrAttributeCd2(info.getMrAttributeCd2());
	common.setMasterFlg(info.getMasterFlg());
	common.setMasterKengenSoshiki(info.getMasterKengenSoshiki());
	common.setMasterKengenAttribute(info.getMasterKengenAttribute());

	session.putValue(SysCnst.KEY_COMMON_SESSION, common);
    }

    /**
     * MR��ID��password��DB�ɓo�^����Ă��邩�`�F�b�N����.
        */
    private boolean loginCheck(HttpServletRequest req,
			       Connection connection) throws Exception {
	String  mrId = req.getParameter("com_mrid");	//id
	String  password = req.getParameter("passwd");	//password

	//MR���̎擾
	MrInfoManager manager = new MrInfoManager(connection);
   	MrInfo  mrInfo = manager.getMrLoginInfo(mrId);

	if (mrInfo == null) {
	    return false;
	} else if (password.equals(mrInfo.getPassword())) {
	    refreshCommon(mrInfo, req.getSession());
            // hb010717
            HttpSession session = req.getSession();
            session.putValue("daikou",mrInfo.getDaikou());
            session.putValue("connection",((Hashtable) mrInfo.getDaikou()).get("connection"));
 	    session.putValue("com_mrid", req.getParameter("com_mrid"));
 	    session.putValue("com_mrname", mrInfo.getName());

	    return true;
	}

	return false;
    }

    /**
     * �p�X���[�h�ύX.
     */
    private boolean passwdChange(HttpServletRequest req,
				 Connection connection) throws Exception {
	String  mrid = req.getParameter("com_mrid");
	String  passwd1 = req.getParameter("newPasswd1");

	//���O�C���`�F�b�N
	if (loginCheck(req, connection)) {
	    MrInfoManager manager = new MrInfoManager(connection);
	    MrInfo  mrinfo = manager.getMrLoginInfo(mrid);
	    mrinfo.setPassword(passwd1);
	    manager.updatePassword(mrinfo);
	    return true;
	}

	return false;
    }
}
