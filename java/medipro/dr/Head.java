package medipro.dr;

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
 * 
 */
public class Head extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try	{
					
			String  linkURL = getCookieID(request);
			log("linkURL 確認 ="+linkURL);
				
			// Go to the next page
			response.sendRedirect(linkURL);
		} catch (Exception e) {
			log("", e);
		    DispatManager dm = new DispatManager();
		    dm.distribute(request,response, e);
		}
	}
	
    private String getCookieID(HttpServletRequest request) {
		// initialization
		String id = null;
		Cookie thisCookie = null;
		// cookieの持つデータをすべてとってくる
		Cookie[] cookies = request.getCookies();

		for(int i=0; i < cookies.length; i++) {
			thisCookie = cookies[i];
			if (thisCookie.getName().equals("bvtomrurl")) {
				return thisCookie.getValue();
			}
		}

		return null;
    }

}
