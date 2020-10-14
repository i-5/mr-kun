package medipro.mr;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.mr.common.exception.*;
import jp.ne.sonet.medipro.mr.common.servlet.*;
import jp.ne.sonet.medipro.mr.common.util.*;

/**
 * Medipro MR統計分析(顧客別）
 */
public class StatisticsDr extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
		String sort	= "dr_id";
		String order = "A";
		String NameOrder = "A";
		String TargetOrder = "A";
		String Send30Order = "A";
		String clickCnt30Order = "A";
		String clickRitu30Order	= "A";
		String Send180Order = "A";
		String clickCnt180Order	= "A";
		String clickRitu180Order = "A";

		try {
			// セッションチェック＆セッションがあればリセット
			SessionManager sm = new SessionManager();
			if	( sm.check(request, 1) ) {
				HttpSession session = request.getSession(true);
				Enumeration keys = request.getParameterNames();

//  				if ( keys.hasMoreElements() ) {
				sort	= (String)session.getValue("sort");
				NameOrder = (String)session.getValue("NameOrder");
				TargetOrder = (String)session.getValue("TargetOrder");
				Send30Order = (String)session.getValue("Send30Order");
				clickCnt30Order = (String)session.getValue("clickCnt30Order");
				clickRitu30Order = (String)session.getValue("clickRitu30Order");
				Send180Order = (String)session.getValue("Send180Order");
				clickCnt180Order = (String)session.getValue("clickCnt180Order");
				clickRitu180Order = (String)session.getValue("clickRitu180Order");
				
				sort = sort == null ? "dr_id" : sort;
				NameOrder = NameOrder == null ? "A" : NameOrder;
				TargetOrder = TargetOrder == null ? "A" : TargetOrder;
				Send30Order = Send30Order == null ? "A" : Send30Order;
				clickCnt30Order = clickCnt30Order == null ? "A" : clickCnt30Order;
				clickRitu30Order = clickRitu30Order == null ? "A" : clickRitu30Order;
				Send180Order = Send180Order == null ? "A" : Send180Order;
				clickCnt180Order = clickCnt180Order == null ? "A" : clickCnt180Order;
				clickRitu180Order = clickRitu180Order == null ? "A" : clickRitu180Order;
//  					if	( session.getValue("sort") == null ) {
//  						sort	= "dr_id";
//  					} else {
//  						sort	= (String)session.getValue("sort");
//  					}
//  					if	( session.getValue("NameOrder") == null ) {
//  						NameOrder = "A";
//  					} else {
//  						NameOrder = (String)session.getValue("NameOrder");
//  					}
//  					if	( session.getValue("TargetOrder") == null ) {
//  						TargetOrder = "A";
//  					} else {
//  						TargetOrder = (String)session.getValue("TargetOrder");
//  					}
//  					if	( session.getValue("Send30Order") == null ) {
//  						Send30Order = "A";
//  					} else {
//  						Send30Order = (String)session.getValue("Send30Order");
//  					}
//  					if	( session.getValue("clickCnt30Order") == null ) {
//  						clickCnt30Order = "A";
//  					} else {
//  						clickCnt30Order = (String)session.getValue("clickCnt30Order");
//  					}
//  					if	( session.getValue("clickRitu30Order") == null ) {
//  						clickRitu30Order = "A";
//  					} else {
//  						clickRitu30Order = (String)session.getValue("clickRitu30Order");
//  					}
//  					if	( session.getValue("Send180Order") == null ) {
//  						Send180Order = "A";
//  					} else {
//  						Send180Order = (String)session.getValue("Send180Order");
//  					}
//  					if	( session.getValue("clickCnt180Order") == null ) {
//  						clickCnt180Order = "A";
//  					} else {
//  						clickCnt180Order = (String)session.getValue("clickCnt180Order");
//  					}
//  					if	( session.getValue("clickRitu180Order") == null ) {
//  						clickRitu180Order = "A";
//  					} else {
//  						clickRitu180Order = (String)session.getValue("clickRitu180Order");
//  					}
//  				} else {
//  				}
				sm.reset(request,0);

				// ソートの情報をセット
				setSort(request,
						session,
						sort,
						order,
						NameOrder,
						TargetOrder,
						Send30Order,
						clickCnt30Order,
						clickRitu30Order,
						Send180Order,
						clickCnt180Order,
						clickRitu180Order);

				// Go to the next page
				response.sendRedirect("/medipro/mr/MrStatisticsDr/index.html");
			}
			else {
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
				 String NameOrder,
				 String TargetOrder,
				 String Send30Order,
				 String clickCnt30Order,
				 String clickRitu30Order,
				 String Send180Order,
				 String clickCnt180Order,
				 String clickRitu180Order) {

		if	( req.getParameter("sort") != null ) {
			sort = req.getParameter("sort");

			// 氏名
			if	( sort.equals("dr_id") ) {
				NameOrder       = (NameOrder.equals("A"))? "D":"A";
				order		= NameOrder;
			}
			// ターゲットランク
			else if	( sort.equals("target_rank") ) {
				TargetOrder     = (TargetOrder.equals("A"))? "D":"A";
				order		= TargetOrder;
			}
			// 過去30日送信数
			else if	( sort.equals("send_count30") ) {
				Send30Order     = (Send30Order.equals("A"))? "D":"A";
				order		= Send30Order;
			}
			// 過去30日クリック数
			else if	( sort.equals("click_count30") ) {
				clickCnt30Order = (clickCnt30Order.equals("A"))? "D":"A";
				order		= clickCnt30Order;
			}
			// 過去30日クリック率
			else if	( sort.equals("click_rate30") ) {
				clickRitu30Order = (clickRitu30Order.equals("A"))? "D":"A";
				order		 = clickRitu30Order;
			}
			// 過去180日送信数
			else if	( sort.equals("send_count180") ) {
				Send180Order     = (Send180Order.equals("A"))? "D":"A";
				order		= Send180Order;
			}
			// 過去180日クリック数
			else if	( sort.equals("click_count180") ) {
				clickCnt180Order = (clickCnt180Order.equals("A"))? "D":"A";
				order		= clickCnt180Order;
			}
			// 過去180日クリック率
			else if	( sort.equals("click_rate180") ) {
				clickRitu180Order = (clickRitu180Order.equals("A"))? "D":"A";
				order		 = clickRitu180Order;
			}
		}

		session.putValue("sort"			, sort);
		session.putValue("order"		, order);
		session.putValue("NameOrder"		, NameOrder);
		session.putValue("TargetOrder"		, TargetOrder);
		session.putValue("Send30Order"		, Send30Order);
		session.putValue("clickCnt30Order"	, clickCnt30Order);
		session.putValue("clickRitu30Order"	, clickRitu30Order);
		session.putValue("Send180Order"		, Send180Order);
		session.putValue("clickCnt180Order"	, clickCnt180Order);
		session.putValue("clickRitu180Order"	, clickRitu180Order);
    }

}
