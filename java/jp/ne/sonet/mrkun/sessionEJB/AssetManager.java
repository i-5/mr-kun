
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.sessionEJB;

import jp.ne.sonet.mrkun.framework.exception.*;
import jp.ne.sonet.mrkun.framework.sessionEJB.*;
import jp.ne.sonet.mrkun.valueobject.*;
import jp.ne.sonet.mrkun.server.*;
import jp.ne.sonet.mrkun.dao.*;
import java.util.*;
import java.rmi.*;


/**
 * The implementation class for the AssetManager bean.
 * This bean is stateless in nature, and uses DAO support classes to
 * write persistent information to the database.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @author Damon Lok
 * @author M.Mizuki
 *
 * @version $Id: AssetManager.java,v 1.1.2.11 2001/12/20 08:59:09 rick Exp $
 */
public class AssetManager extends BaseSessionEJB
{
  /**
   * Retrieves a template from persistent storage.
   */
  public Template getByTemplateId(String templateId, String sessionId) throws RemoteException
  {
    DAOFacade daoResource = new DAOFacade(Template.class);
    return (Template) daoResource.searchRecord(templateId, "templateId", sessionId);
  }

  /**
   * Retrieves a link from persistent storage.
   */
  public ResourceLink getByLinkId(String linkId, String sessionId) throws RemoteException
  {
    DAOFacade daoResource = new DAOFacade(ResourceLink.class);
    return (ResourceLink) daoResource.searchRecord(linkId, "linkId", sessionId);
  }

  /**
   * Retrieves a link from persistent storage.
   */
  public Collection getResourceList(Company company, String sessionId)
  {
    // Get a DAO and perform the save query
    DAOFacade resourceLinkDAO = new DAOFacade(ResourceCat.class);
    try
    {
      return (Collection) resourceLinkDAO.searchMultiple(company,"companyId", sessionId);
    }
    catch (NoObjectFoundException errNOF)
    {
      return new ArrayList();
    }
  }

  /**
   * Retrieves a link from persistent storage.
   */
  public Collection getTemplateList(Company company, String sessionId)
  {
    // Get a DAO and perform the save query
    DAOFacade templateDAO = new DAOFacade(TemplateCategory.class);
    try
    {
      return (Collection) templateDAO.searchMultiple(company, "templateCategory", sessionId);
    }
    catch (NoObjectFoundException errNOF)
    {
      return new ArrayList();
    }
  }

  /**
   * Retrieve a constant value
   */
  public DatabaseConstant getConstantById(String constantId, String sessionId)
  {
    // Get a DAO and perform the save query
    DAOFacade constDAO = new DAOFacade(DatabaseConstant.class);
    try
    {
      return (DatabaseConstant) constDAO.searchRecord(constantId, "constantId", sessionId);
    }
    catch (NoObjectFoundException errNOF)
    {
      return null;
    }
  }

  /**
   * Retrieve a list of edetail categories by company id
   */
  public Collection getEDetailCategories(String companyId, String sessionId)
  {
    DAOFacade daoCategory = new DAOFacade(EDetailCategory.class);
    try
    {
      return (Collection) daoCategory.searchMultiple(companyId, "companyId", sessionId);
    }
    catch (NoObjectFoundException errNOF)
    {
      return new ArrayList();
    }
  }

  /**
   * Retrieve an edetail object by its Id
   */
  public EDetailCategory getEDetailCategoryById (String categoryId, String companyId, String sessionId)
  {
    DAOFacade daoCategory = new DAOFacade(EDetailCategory.class);
    try
    {
      Map params = new Hashtable();
      params.put("categoryId", categoryId);
      params.put("companyId", companyId);
      return (EDetailCategory) daoCategory.searchRecord(params, "edetailCategoryId", sessionId);
    }
    catch (NoObjectFoundException errNOF)
    {
      return null;
    }
  }

  /**
   * Retrieve a list of medical certifications
   */
  public Collection getCertificationList(String sessionId)
  {
    DAOFacade daoCertification = new DAOFacade(MedicalCertification.class);
    try
    {
      return (Collection) daoCertification.searchMultiple(null, "all", sessionId);
    }
    catch (NoObjectFoundException errNOF)
    {
      return new ArrayList();
    }
  }

}

