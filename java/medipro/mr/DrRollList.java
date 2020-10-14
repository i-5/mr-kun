package medipro.mr;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.mr.common.exception.*;
import jp.ne.sonet.medipro.mr.common.servlet.*;
import jp.ne.sonet.medipro.mr.server.controller.*;
import jp.ne.sonet.medipro.mr.server.manager.*;
import jp.ne.sonet.medipro.mr.server.entity.*;

/**
 * Medipro MR顧客名簿
 */
public class DrRollList extends HttpServlet {
    
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
	}

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
		String sort = "sen.target_rank";//初期ソート項目
		String order = "D";				//初期ソート方向
		String target_order = "D";
		String kinmusaki_order = "A";
		String name_order = "A";
		String syokusyu_order = "A";
		String senmon1_order = "A";
		String senmon2_order = "A";
		String senmon3_order = "A";

		try {
			// セッションチェック＆セッションがあればリセット
			SessionManager sm = new SessionManager();
			if ( sm.check(request, 1) ) {
				HttpSession session = request.getSession(true);
				Enumeration keys = request.getParameterNames();

				//消されると困るのでとっておく
//  				if ( keys.hasMoreElements() ) {
					sort = (String)session.getValue("sort");
					kinmusaki_order	= (String)session.getValue("kinmusaki_order");
					name_order	= (String)session.getValue("name_order");
					target_order	= (String)session.getValue("target_order");
					syokusyu_order	= (String)session.getValue("syokusyu_order");
					senmon1_order	= (String)session.getValue("senmon1_order");
					senmon2_order	= (String)session.getValue("senmon2_order");
					senmon3_order	= (String)session.getValue("senmon3_order");

					sort = sort == null ? "sen.target_rank" : sort;
					kinmusaki_order = kinmusaki_order == null ? "D" : kinmusaki_order;
					name_order = name_order == null ? "D" : name_order;
					target_order = target_order == null ? "D" : target_order;
					syokusyu_order = syokusyu_order == null ? "D" : syokusyu_order;
					senmon1_order = senmon1_order == null ? "D" : senmon1_order;
					senmon2_order = senmon2_order == null ? "D" : senmon2_order;
					senmon3_order = senmon3_order == null ? "D" : senmon3_order;
//  					if ( session.getValue("sort") == null ) {
//  						sort    = "sen.target_rank";
//  					} else {
//  						sort    = (String)session.getValue("sort");
//  					}
//  					if ( session.getValue("kinmusaki_order") == null ) {
//  						kinmusaki_order	= "D";
//  					} else {
//  						kinmusaki_order	= (String)session.getValue("kinmusaki_order");
//  					}
//  					if ( session.getValue("name_order") == null ) {
//  						name_order	= "D";
//  					} else {
//  						name_order	= (String)session.getValue("name_order");
//  					}
//  					if ( session.getValue("target_order") == null ) {
//  						target_order	= "D";
//  					} else {
//  						target_order	= (String)session.getValue("target_order");
//  					}
//  					if ( session.getValue("syokusyu_order") == null ) {
//  						syokusyu_order	= "D";
//  					} else {
//  						syokusyu_order	= (String)session.getValue("syokusyu_order");
//  					}
//  					if ( session.getValue("senmon1_order") == null ) {
//  						senmon1_order	= "D";
//  					} else {
//  						senmon1_order	= (String)session.getValue("senmon1_order");
//  					}
//  					if ( session.getValue("senmon2_order") == null ) {
//  						senmon2_order	= "D";
//  					} else {
//  						senmon2_order	= (String)session.getValue("senmon2_order");
//  					}
//  					if ( session.getValue("senmon3_order") == null ) {
//  						senmon3_order	= "D";
//  					} else {
//  						senmon3_order	= (String)session.getValue("senmon3_order");
//  					}
//  				}

				// reset of session object
				sm.reset(request,0);
						
				// ソートの情報をセット
				setSort(request,
						session,
						sort,
						order,
						target_order,
						kinmusaki_order,
						name_order,
						syokusyu_order,
						senmon1_order,
						senmon2_order,
						senmon3_order);

				// Go to the next page
				response.sendRedirect("/medipro/mr/MrDrRollList/index.html");
			} else {
				// セッションエラーの場合
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
				 String target_order,
				 String kinmusaki_order,
				 String name_order,
				 String syokusyu_order,
				 String senmon1_order,
				 String senmon2_order,
				 String senmon3_order) {
        if ( req.getParameter("sort") != null ) {
			sort = req.getParameter("sort");
		
			// 所属先・勤務先
			if	( sort.equals("kinmusaki") ) {
				kinmusaki_order	= (kinmusaki_order.equals("A"))? "D":"A";
				order		= kinmusaki_order;
			}

			// 氏名
			else if	( sort.equals("dr_id") ) {
				name_order	= (name_order.equals("A"))? "D":"A";
				order		= name_order;
			}

			// ターゲットランク
			else if	( sort.equals("sen.target_rank") ) {
				target_order	= (target_order.equals("A"))? "D":"A";
				order		= target_order;
			}

			// 職種
			else if	( sort.equals("syokusyu") ) {
				syokusyu_order	= (syokusyu_order.equals("A"))? "D":"A";
				order		= syokusyu_order;
			}

			// 専門領域1
			else if	( sort.equals("senmon1") ) {
				senmon1_order	= (senmon1_order.equals("A"))? "D":"A";
				order		= senmon1_order;
			}

			// 専門領域2
			else if	( sort.equals("senmon2") ) {
				senmon2_order	= (senmon2_order.equals("A"))? "D":"A";
				order		= senmon2_order;
			}
			// 専門領域3
			else if	( sort.equals("senmon3") ) {
				senmon3_order	= (senmon3_order.equals("A"))? "D":"A";
				order		= senmon3_order;
			}

		}

		session.putValue("kinmusaki_order", kinmusaki_order);
		session.putValue("name_order", name_order);
		session.putValue("target_order", target_order);
		session.putValue("syokusyu_order", syokusyu_order);
		session.putValue("senmon1_order", senmon1_order);
		session.putValue("senmon2_order", senmon2_order);
		session.putValue("senmon3_order", senmon3_order);
		session.putValue("order"     , order);
		session.putValue("sort"      , sort);
    }

}
