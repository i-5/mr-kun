
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.sessionEJB;

import jp.ne.sonet.mrkun.framework.sessionEJB.*;
import jp.ne.sonet.mrkun.valueobject.*;
import java.util.*;
import java.rmi.*;

/**
 * Remote interface for the AssetManager EJB.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @author M.Mizuki
 * @version $Id: AssetManagerRemoteIntf.java,v 1.1.2.10 2001/12/20 08:59:09 rick Exp $
 */
public interface AssetManagerRemoteIntf extends BaseSessionEJBRemoteIntf
{
  /**
   * Retrieves a template from persistent storage.
   */
  public Template getByTemplateId(String templateId, String sessionId) throws RemoteException;

  /**
   * Retrieves all templates from persistent storage.
   */
  public Collection getTemplateList(Company company, String sessionId) throws RemoteException;

  /**
   * Retrieves a link from persistent storage.
   */
  public ResourceLink getByLinkId(String linkId, String sessionId) throws RemoteException;

  /**
   * Retrieves a link from persistent storage.
   */
  public Collection getResourceList(Company company, String sessionId) throws RemoteException;

  /**
   * Retrieve a constant value
   */
  public DatabaseConstant getConstantById(String constantId, String sessionId) throws RemoteException;

  /**
   * Retrieve an edetail object by its Id
   */
  public EDetailCategory getEDetailCategoryById (String categoryId, String companyId, String sessionId) throws RemoteException;

  /**
   * Retrieve a list of edetail categories by company id
   */
  public Collection getEDetailCategories(String companyId, String sessionId) throws RemoteException;

  /**
   * Retrieve a list of medical certifications
   */
  public Collection getCertificationList(String sessionId) throws RemoteException;
}

 