
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.servlet;

import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.util.*;
import java.text.*;
import java.rmi.*;
import javax.naming.*;
import javax.rmi.PortableRemoteObject;
import javax.ejb.*;

import jp.ne.sonet.mrkun.sessionEJB.*;
import jp.ne.sonet.mrkun.server.*;
import jp.ne.sonet.mrkun.valueobject.*;
import jp.ne.sonet.mrkun.framework.exception.*;
import jp.ne.sonet.mrkun.framework.servlet.*;

public class DR_RetreivePointTestCtlr extends BaseServlet
{
  public void doAction(HttpServletRequest request, HttpServletResponse response)
  {
    String companyId = request.getParameter("COMPANY_CD");
    String storeId = request.getParameter("STORE_CD");
    String drId = request.getParameter("MEMBER_ID");
    String system_cd = request.getParameter("MEMBER_PWD");
    String point = request.getParameter("POINT_QT");
    String drName = request.getParameter("UJI_KJ");
    
    System.out.println("Company Id: " + companyId);
    System.out.println("Store Id: " + storeId);
    System.out.println("DR Id: " + drId);
    System.out.println("System_cd: " + system_cd);
    System.out.println("Point: " + point);
    System.out.println("DR name: " + drName);
  }
}