package medipro.mr;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.mr.common.exception.*;
import jp.ne.sonet.medipro.mr.common.servlet.*;
import jp.ne.sonet.medipro.mr.common.util.*;

/**
 * Medipro MR顧客管理
 */
public class DrList extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
		String sort = "dr.name";//初期ソート項目
		String order = "A";		//初期ソート方向
		String LankOrder = "D";
		String NoRcvOrder = "A";
		String DaysOrder = "A";
		String name_order = "A"; /* y-yamada add 1012 名前でのソートに対応 */
		String SendOrder = "A";
		String NoReadOrder = "A";
		String ActionOrder = "A";

		try {
			// セッションチェック＆セッションがあればリセット
			SessionManager sm = new SessionManager();

			if ( sm.check(request, 1) ) {
				HttpSession session = request.getSession(true);

				//初期化
				if (request.getParameter("init") != null) {
					//1016  y-yamada add start
					session.removeValue("sort");
					session.removeValue("order");
					session.removeValue("LankOrder");
					session.removeValue("NoRcvOrder");
					session.removeValue("DaysOrder");
					session.removeValue("SendOrder");
					session.removeValue("NoReadOrder");
					session.removeValue("ActionOrder");
					session.removeValue("name_order");
					//1016 y-yamada add end
				}

				//セッションパラメータの取得
				Enumeration keys = request.getParameterNames();

//  				if ( keys.hasMoreElements()) {
					sort = (String)session.getValue("sort");
					LankOrder = (String)session.getValue("LankOrder");
					NoRcvOrder = (String)session.getValue("NoRcvOrder");
					ActionOrder = (String)session.getValue("ActionOrder");
					DaysOrder = (String)session.getValue("DaysOrder");
					SendOrder = (String)session.getValue("SendOrder");
					NoReadOrder = (String)session.getValue("NoReadOrder");

					sort = sort == null ? "dr.name" : sort;
					LankOrder = LankOrder == null ? "D" : LankOrder;
					NoRcvOrder = NoRcvOrder == null ? "A" : NoRcvOrder;
					ActionOrder = ActionOrder == null ? "A" : ActionOrder;
					DaysOrder = DaysOrder == null ? "A" : DaysOrder;
					SendOrder = SendOrder == null ? "A" : SendOrder;
					NoReadOrder = NoReadOrder == null ? "A" : NoReadOrder;
					
//  					if (session.getValue("sort") == null) {
//  						sort = "dr.name";//y-yamada add 1016
//  					} else {
//  						sort = (String)session.getValue("sort");
//  					}
//  					if ( session.getValue("LankOrder") == null ) {
//  						LankOrder = "D";
//  					} else {
//  						LankOrder = (String)session.getValue("LankOrder");
//  					}
//  					if ( session.getValue("NoRcvOrder") == null ) {
//  						NoRcvOrder = "A";
//  					} else {
//  						NoRcvOrder = (String)session.getValue("NoRcvOrder");
//  					}
//  					if ( session.getValue("ActionOrder") == null ) {
//  						ActionOrder = "A";
//  					} else {
//  						ActionOrder = (String)session.getValue("ActionOrder");
//  					}
//  					if ( session.getValue("DaysOrder") == null ) {
//  						DaysOrder = "A";
//  					} else {
//  						DaysOrder = (String)session.getValue("DaysOrder");
//  					}
//  					if ( session.getValue("SendOrder") == null ) {
//  						SendOrder = "A";
//  					} else {
//  						SendOrder = (String)session.getValue("SendOrder");
//  					}
					if ( session.getValue("NoReadOrder") == null ) {
//  						NoReadOrder = "A";
					} else {
//  						NoReadOrder = (String)session.getValue("NoReadOrder");
						// y-yamada add 1012 start 
						if ( session.getValue("name_order") == null ) {
							name_order = "A";
						} else {
							name_order = (String)session.getValue("name_order");
						}			
						// y-yamada add 1012 end
					}
//  				}

				// reset of session object
				sm.reset(request,0); 

				// ソートの情報をセット
				setSort(request,
						session,
						sort,
						order,
						LankOrder,
						NoRcvOrder,
						DaysOrder,
						name_order,
						SendOrder,
						NoReadOrder,
						ActionOrder);

				// Go to the next page
				response.sendRedirect("/medipro/mr/MrDrList/index.html");
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
				 String LankOrder,
				 String NoRcvOrder,
				 String DaysOrder,
				 String name_order,
				 String SendOrder,
				 String NoReadOrder,
				 String ActionOrder) {
		if ( req.getParameter("sort") != null ) {
			sort = req.getParameter("sort");

			// ターゲットランク
			if ( sort.equals("sen.target_rank") ) {
				LankOrder = (LankOrder.equals("A"))? "D":"A";
				order  = LankOrder;
			}
			// 未読受信メッセージ
			else if ( sort.equals("recvCount") ) {
				NoRcvOrder = (NoRcvOrder.equals("A"))? "D":"A";
				order  = NoRcvOrder;
			}
			else if ( sort.equals("action") ) {
				ActionOrder = (ActionOrder.equals("A"))? "D":"A";
				order  = ActionOrder;
			}
			// 前回開封からの日数
			else if ( sort.equals("lastOpenDay") ) {
				DaysOrder = (DaysOrder.equals("A"))? "D":"A";
				order  = DaysOrder;
			}
			// 未読送信メッセージ
			else if ( sort.equals("sendCount") ) {
				SendOrder = (SendOrder.equals("A"))? "D":"A";
				order  = SendOrder;
			}
			// 最新送信の未読日数
			else if ( sort.equals("sendNoReadDay") ) {
				NoReadOrder = (NoReadOrder.equals("A"))? "D":"A";
				order  = NoReadOrder;
			}
			// y-yamada add 1012 start
			// 名前
			else if ( sort.equals("dr.name") ) {
				name_order = (name_order.equals("A"))? "D":"A";
				order  = name_order;
			}
			// y-yamada add 1012 end
		}

		session.putValue("LankOrder" , LankOrder);
		session.putValue("NoRcvOrder" , NoRcvOrder);
		session.putValue("ActionOrder" , ActionOrder);
		session.putValue("DaysOrder" , DaysOrder);
		session.putValue("SendOrder" , SendOrder);
		session.putValue("NoReadOrder" , NoReadOrder);
		session.putValue("name_order" , name_order); // y-yamada add 1012
		session.putValue("order" , order);
		session.putValue("sort"  , sort);
		session.putValue("gamen" , "M18");
	
    }

}
