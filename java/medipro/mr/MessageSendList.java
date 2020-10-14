package medipro.mr;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.mr.common.exception.*;
import jp.ne.sonet.medipro.mr.common.servlet.*;

/**
 * Medipro MR����I��
 */
public class MessageSendList extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
		String sort = "dr_id";
		String order = "A";
		String nameOrder = "A";
		String senmon1Order = "A";
		String senmon2Order = "A";
		String senmon3Order = "A";
		String kinmuOrder = "A";

		try {
			// �Z�b�V�����`�F�b�N���Z�b�V����������΃��Z�b�g
			SessionManager sm = new SessionManager();
			if	( sm.check(request, 1) ) {
				HttpSession session = request.getSession(true);
				Enumeration keys = request.getParameterNames();

				//�Z�b�V�������̃��Z�b�g
				if (request.getParameter("init") != null) {
					session.removeValue("sort");
					session.removeValue("nameOrder");
					session.removeValue("senmon1Order");
					session.removeValue("senmon2Order");
					session.removeValue("senmon3Order");
					session.removeValue("kinmuOrder");
					session.removeValue("order");
					session.removeValue("allcheck");
					session.removeValue("drid_hash");
					session.removeValue("callnaiyo");
					session.removeValue("mr_gif");
					session.removeValue("msg_from");
					session.removeValue("msg_title");
					session.removeValue("mr_gif_name");
					session.removeValue("mr_pictureType");
					session.removeValue("message");
					session.removeValue("link_url");
					session.removeValue("next_radio");
					session.removeValue("liblink_name");
					session.removeValue("expressionCd");
					session.removeValue("limit_yy");
					session.removeValue("limit_mm");
					session.removeValue("limit_dd");
					session.removeValue("sendmail");
					session.removeValue("send_time");
					session.removeValue("upload_file_Name");
					session.removeValue("msg_to");
					session.removeValue("upload_image_Name");
					session.removeValue("link_type");
					session.removeValue("liblink_kbn");
					session.removeValue("liblink_url");
					session.removeValue("limit_date");
					session.removeValue("pagename");
					session.removeValue("originalAttachName");
					session.removeValue("bccList");//MR1.5
					session.removeValue("plain_message");//MR1.5
				}

//  				if ( keys.hasMoreElements() ) {
					sort	= (String)session.getValue("sort");
					nameOrder = (String)session.getValue("nameOrder");
					senmon1Order = (String)session.getValue("senmon1Order");
					senmon2Order = (String)session.getValue("senmon2Order");
					senmon3Order = (String)session.getValue("senmon3Order");
					kinmuOrder = (String)session.getValue("kinmuOrder");

					sort = sort == null ? "dr_id" : sort;
					nameOrder = nameOrder == null ? "A" : nameOrder;
					senmon1Order = senmon1Order == null ? "A" : senmon1Order;
					senmon2Order = senmon2Order == null ? "A" : senmon2Order;
					senmon3Order = senmon3Order == null ? "A" : senmon3Order;
					kinmuOrder = kinmuOrder == null ? "A" : kinmuOrder;
//  					if ( session.getValue("sort") == null ) {
//  						sort	= "dr_id";
//  					} else {
//  						sort	= (String)session.getValue("sort");
//  					}

//  					if	( session.getValue("nameOrder") == null ) {
//  						nameOrder = "A";
//  					} else {
//  						nameOrder = (String)session.getValue("nameOrder");
//  					}

//  					if	( session.getValue("senmon1Order") == null ) {
//  						senmon1Order = "A";
//  					} else {
//  						senmon1Order = (String)session.getValue("senmon1Order");
//  					}

//  					if	( session.getValue("senmon2Order") == null ) {
//  						senmon2Order = "A";
//  					} else {
//  						senmon2Order = (String)session.getValue("senmon2Order");
//  					}

//  					if	( session.getValue("senmon3Order") == null ) {
//  						senmon3Order = "A";
//  					} else {
//  						senmon3Order = (String)session.getValue("senmon3Order");
//  					}

//  					if	( session.getValue("kinmuOrder") == null ) {
//  						kinmuOrder = "A";
//  					} else {
//  						kinmuOrder = (String)session.getValue("kinmuOrder");
//  					}
//  				}

				// reset of session object
				sm.reset(request,0);

				// �\�[�g�̏����Z�b�g
				setSort(request,
						session,
						sort,
						order,
						nameOrder,
						senmon1Order,
						senmon2Order,
						senmon3Order,
						kinmuOrder);

				// Go to the next page
				response.sendRedirect("/medipro/mr/MrMessageSendList/index.html");
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

    void setSort(HttpServletRequest req,
				 HttpSession session,
				 String sort,
				 String order,
				 String nameOrder,
				 String senmon1Order,
				 String senmon2Order,
				 String senmon3Order,
				 String kinmuOrder) {

		if	( req.getParameter("sort") != null ) {
			sort = req.getParameter("sort");

			// ����
			if	( sort.equals("dr_id") ) {
				nameOrder	= (nameOrder.equals("A"))? "D":"A";
				order		= nameOrder;
			}
			// ���̈�1
			else if	( sort.equals("senmon1") ) {
				senmon1Order	= (senmon1Order.equals("A"))? "D":"A";
				order		= senmon1Order;
			}
			// ���̈�2
			else if	( sort.equals("senmon2") ) {
				senmon2Order	= (senmon2Order.equals("A"))? "D":"A";
				order		= senmon2Order;
			}
			// ���̈�3
			else if	( sort.equals("senmon3") ) {
				senmon3Order	= (senmon3Order.equals("A"))? "D":"A";
				order		= senmon3Order;
			}
			// �����E�Ζ���
			else if	( sort.equals("kinmusaki") ) {
				kinmuOrder	= (kinmuOrder.equals("A"))? "D":"A";
				order		= kinmuOrder;
			}
		}

		session.putValue("nameOrder"	, nameOrder);
		session.putValue("senmon1Order"	, senmon1Order);
		session.putValue("senmon2Order"	, senmon2Order);
		session.putValue("senmon3Order"	, senmon3Order);
		session.putValue("kinmuOrder"	, kinmuOrder);
		session.putValue("order"	, order);
		session.putValue("sort"		, sort);
    }

}
