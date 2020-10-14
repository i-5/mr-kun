package medipro.wm;

import java.io.*;
import java.util.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.wm.common.*;
import jp.ne.sonet.medipro.wm.common.util.*;
import jp.ne.sonet.medipro.wm.common.exception.*;
import jp.ne.sonet.medipro.wm.server.controller.*;
import jp.ne.sonet.medipro.wm.server.session.*;
import jp.ne.sonet.medipro.wm.server.entity.MrInfo;

import java.sql.*;

/**
 * <strong>「コール内容－一覧」画面用サーブレット</strong>
 * @author
 * @version
 */
public class DaikouHaishinServlet extends HttpServlet {
    private static final String PRT_HEADER = "### DaikouHaishinServlet : ";


    /** サブマスター更新（権限取り上げ）リクエスト */
    protected static String MR_BY_ATTRIBUTES_SQL
        = "SELECT "
        + "mr.mr_id, mr.name"
        + " FROM mr mr"
        + " WHERE company_cd = ?"
        + " AND delete_ymd IS NULL ";

    protected static String DR_BY_MR_SQL
        = "SELECT distinct "
        + "DR_ID, MR_ID FROM SENTAKU_TOROKU "
        + "WHERE END_YMD IS NULL AND MR_ID in ( ";

    /**
     * GETメソッド処理。
     * @param req 要求オブジェクト
     * @param res 応答オブジェクト
     */
    public void doGet(HttpServletRequest req, HttpServletResponse res) {
        doPost(req, res);
    }

    /**
     * POSTメソッド処理。
     * @param req 要求オブジェクト
     * @param res 応答オブジェクト
     */
    public void doPost(HttpServletRequest req, HttpServletResponse res) 
    {
	ResultSet rset;
	String sqlstmt;
        Hashtable daikou;
        try {
            // セッションチェック
            if (CallListController.initSession(req, res) == false) {
                return;
            }

            HttpSession ses = req.getSession(true);
            DBConnect dbconn = new DBConnect();
	    Connection connection  = dbconn.getDBConnect();
            daikou = (Hashtable) ses.getValue("daikou");
            Common comSes = (Common)ses.getValue(SysCnst.KEY_COMMON_SESSION);
            CallListSession callSes =
                    (CallListSession)ses.getValue(SysCnst.KEY_CALLLIST_SESSION);
            String target;

            // メッセージとチェック状態のクリア
            callSes.setMessageMode(callSes.MESMODE_NONE);  
            callSes.clearChecked();

	   boolean flag = false;
           target = req.getParameter("execute");
           if (target != null) 
	   {
               if (target.equals("second"))
               {
		StringBuffer additional_sql = new StringBuffer();

                String s = getSelectAttribute(req,"mr_attribute_1");
		
                if(!s.equals("all")) 
		{
                 additional_sql.append(" AND mr.mr_attribute_cd1 = ").append("'").append(s).append("' ");
		 flag=true; 
                }

                s = getSelectAttribute(req,"mr_attribute_2");
		
                if(!s.equals("all")) 
		{
                 additional_sql.append(" AND mr.mr_attribute_cd2 = ").append("'").append(s).append("' ");
		 flag=true; 
                }

                s = getSelectAttribute(req,"shiten");
		
                if(!s.equals("all")) 
		{
                 additional_sql.append(" AND mr.shiten_cd = ").append("'").append(s).append("' ");
		 flag=true; 
                }

                s = getSelectAttribute(req,"eigyosyo");
		
                if(!s.equals("all")) 
		{
                 additional_sql.append(" AND mr.eigyosyo_cd = ").append("'").append(s).append("' ");
		 flag=true; 
                }
          sqlstmt =       MR_BY_ATTRIBUTES_SQL;
	  if(flag)
	  {
 		StringBuffer stmt = new StringBuffer(MR_BY_ATTRIBUTES_SQL);
		sqlstmt = stmt.append(additional_sql.toString()).toString();
	  }
            PreparedStatement pstmt = connection.prepareStatement(sqlstmt);
            pstmt.setString(1, (String) daikou.get("mr_company_cd"));

    	    rset = 	  pstmt.executeQuery();
//               } // target.equals("second")
//	} // req.getParameter("execute")
            Hashtable MrList = new Hashtable();
            
	while(rset.next())
        {
         jp.ne.sonet.medipro.mr.server.entity.MrInfo mr = new jp.ne.sonet.medipro.mr.server.entity.MrInfo();
         String mrid;
         mrid = rset.getString("mr_id"); //explicit assignment to variable for debugging to see values
         mr.setId(mrid);
         String name = rset.getString("name");
         mr.setName(name);
         MrList.put(mrid,mr);   
        }
            daikou.put("mrlist",MrList);
            // Go to the next page
            if ( MrList.size() == 0 )
            {
              res.sendRedirect("/medipro/wm/WmDaikouHaishin/nomatch.jsp");
                
            }
            else
            {
             res.sendRedirect("/medipro/wm/WmDaikouHaishin/second.html");
            }
       } // if target.equals("second")
           else if (target.equals("third"))
           {
               int nrOfDrs;
               if( req.getParameterValues("mr").length > 0 )
               {
                nrOfDrs = selectDoctors(req, connection, daikou);
                if ( nrOfDrs > 0 )
                {
                  res.sendRedirect("/medipro/wm/WmDaikouHaishin/MessageBuild1/index.html");   
                }
                else
                {
                 res.sendRedirect("/medipro/wm/WmDaikouHaishin/nomatch.jsp");
                }
               }
               else
               {
                 res.sendRedirect("/medipro/wm/WmDaikouHaishin/nomatch.jsp");
               }
               
               
           }
      } // if target != null
     } 
     catch (Exception e) 
     {
      log("", e);
      DispatManager dm = new DispatManager();
      dm.distribute(req, res, e);
     }

    }

    private int selectDoctors(HttpServletRequest request, Connection connection, Hashtable daikou) throws SQLException
    {
      String[] selections = request.getParameterValues("mr");
      StringBuffer insql = new StringBuffer();
      Hashtable drsByMr = new Hashtable();
      boolean notempty = false;
      int i;
      int nrOfDrs = 0;
      
       if (selections.length > 0)
       {
          notempty = true;
       }
       if(notempty)
       {
           Vector drList = new Vector();
           Vector MrList = new Vector();
           Hashtable AllMrs = (Hashtable) daikou.get("mrlist");
           drsByMr.put(selections[0],drList);
           insql.append("'").append(selections[0]).append("'");
          jp.ne.sonet.medipro.mr.server.entity.MrInfo mr = (jp.ne.sonet.medipro.mr.server.entity.MrInfo) AllMrs.get(selections[0]);
         MrList.add(mr);   

       for (i = 1; i < selections.length; i++)
       {
           drList = new Vector();
           drsByMr.put(selections[i],drList);
           mr = new jp.ne.sonet.medipro.mr.server.entity.MrInfo();
         mr.setId(selections[i]);
         MrList.add(mr);   
  
          insql.append(",'").append(selections[i]).append("'");
       }
           daikou.put("chosen_mrs",MrList);
       insql.append(" )"); 
        StringBuffer stmt = new StringBuffer(DR_BY_MR_SQL);
        String sqlstmt = stmt.append(insql.toString()).toString();
        PreparedStatement pstmt = connection.prepareStatement(sqlstmt);
        ResultSet rset = pstmt.executeQuery();
        
        while(rset.next())
        {
           String drId, mrId;
           
           drId = rset.getString("DR_ID");
           mrId = rset.getString("MR_ID");
           ((Vector) drsByMr.get(mrId)).add(drId);
           nrOfDrs++;
        } // while rset.next()
       daikou.put("drsByMr",drsByMr);
      } // if(notempty) 
      return(nrOfDrs);
      
    }
    private String getSelectAttribute(HttpServletRequest req, String attribute) 
    {
	String ret;
        Vector v   = new Vector();
        String s[] = req.getParameterValues(attribute);
        if (s != null) 
	{
	 ret = encode(s[0]);
         return ret;
	}
	else
	{
	 return(null);
	}
    }

    /**
     *
     */
    private String encode(String value) {
        if (value != null) {
            try {
                value = new String(value.getBytes("8859_1"), "SJIS");
            }
            catch (UnsupportedEncodingException e) {
                log("", e);
            }
        }
        return value;
    }
}
