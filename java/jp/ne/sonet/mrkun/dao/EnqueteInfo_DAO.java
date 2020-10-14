
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.dao;

import java.sql.*;
import java.util.*;
import javax.naming.*;
import jp.ne.sonet.mrkun.framework.exception.*;
import jp.ne.sonet.mrkun.valueobject.*;
import jp.ne.sonet.mrkun.server.*;
import jp.ne.sonet.mrkun.framework.log.*;

/**
 * This class implements the persistence mechanism for the Template class.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @author Damon Lok
 * @author M.Mizuki
 * @version $Id: EnqueteInfo_DAO.java,v 1.1.2.5 2001/12/12 11:53:13 mizuki Exp $
 */
public class EnqueteInfo_DAO implements DAO_SQL
{
  final String  INSERT_ENQUETE_LOG_SQL   = "java:comp/env/sql/EnqueteLogInsertSQL";
  final String  UPDATE_ENQUETE_LOG_SQL   = "java:comp/env/sql/EnqueteLogUpdateSQL";
  final String  SEARCH_ENQUETE_LOG_SQL   = "java:comp/env/sql/EnqueteLogSearchSQL";
  final String  SEARCH_ENQUETE_MIN_SQL   = "java:comp/env/sql/EnqueteMinSearchSQL";
  final String  SEARCH_GROUPBYDRID_SQL   = "java:comp/env/sql/GroupByDrIdSearchSQL";
  final String  SEARCH_QUESTION_LIST_SQL = "java:comp/env/sql/QuestionListSearchSQL";
  final String  INSERT_ANSWER_SQL        = "java:comp/env/sql/EnqueteAnswerInsertSQL";
  final String  ADD_ENQUETE_POINT        = "java:comp/env/sql/AddEnquetePointUpdateSQL";
  final String  ADD_ENQUETE_POINT2        = "java:comp/env/sql/AddEnquetePoint2UpdateSQL";
  final String  LOAD_PROFILE             = "java:comp/env/sql/DRLoadProfileSQL";

  final String  SHIKAKU                  = "0000000015";

  final String  DOCTOR                   = "0001";

  /**
   * This method is used for populating the resultant object when
   * a retrieval query is required. The resultset contains all the
   * attributes of the retrieved object (as stored in the database),
   * and so they are read and used to create in instance of the
   * persisted object.
   * @param attributes The resultset with the retrieved object in it.
   */
  private Object populateObject(Connection conn, ResultSet rs)
    throws NoObjectFoundException, SQLException, NamingException
  {
    // Check the state of the resultset
    if (rs == null)
//      throw new NoObjectFoundException("Resultset was null");
      return null;
    else if (!rs.next())
//      throw new NoObjectFoundException("Resultset contained no rows");
      return null;
    else
    {
      EnqueteInfo  enquete   = new EnqueteInfo();
      enquete.setEnqId(rs.getString("enq_id"));
      enquete.setUrl(rs.getString("url"));
      enquete.setEnqIdStatus(rs.getInt("enqstatus"));
      enquete.setDrId(rs.getString("dr_id"));
      enquete.setReplyStatus(rs.getInt("status"));
      enquete.setMessageHeaderId(rs.getString("message_header_id"));

      if( enquete.getEnqId().equals("0001") )
        enquete.setGroupId( getGroupIdsql(conn, enquete.getDrId()) );

      return enquete;
    }

  }

  /**
   * get User Group ID
   **/
  private String getGroupIdsql(Connection conn,String drId)
  throws SQLException, NamingException
  {
    String GroupId = null;

    if( getProfileValue(conn, SHIKAKU, drId).equals(DOCTOR) )
    {
      PreparedStatement selectQuery = conn.prepareStatement(getSQLStatement(SEARCH_GROUPBYDRID_SQL));
      selectQuery.setObject(1, drId);

      ResultSet rs  = selectQuery.executeQuery();
      if (rs.next())
        GroupId = rs.getString("shinryoka_group");
    }
    else
      GroupId = "0010";

    return GroupId;
  }

  /**
   * Loads a normalised field from the DoctorProfile table
   */
  private String getProfileValue(Connection conn, String itemCode, String drId)
    throws SQLException, NamingException
  {
    if (itemCode == null) return null;
    
    // Look up this item code and DR in the doctor_profile table
    PreparedStatement profileQry = conn.prepareStatement(getSQLStatement(LOAD_PROFILE));
    profileQry.setString(1, drId);
    profileQry.setString(2, itemCode);
    String item = null;
    ResultSet rstProfile = profileQry.executeQuery();
    if (rstProfile.next())
      item = rstProfile.getString("item");
    rstProfile.close();
    profileQry.close();
    return item;
  }

  /**
   * Get a sql string from the environment variables in the deployment
   * descriptor
   */
  private String getSQLStatement(String statementLocation) throws NamingException
  {
    InitialContext ctx = new InitialContext();
    String sqlStatement = (String) ctx.lookup(statementLocation);
    return sqlStatement;
  }

  /**
   * Create new instances in persistent storage
   */
  public boolean createRecord(Connection  conn,
                              Object      addObject)
    throws SQLException, NamingException
  {
    EnqueteInfo enqinfo = (EnqueteInfo)addObject;
    try
    {
      conn.setAutoCommit(false);
      PreparedStatement pstmt = conn.prepareStatement(getSQLStatement(INSERT_ENQUETE_LOG_SQL));

      pstmt.setString(1, enqinfo.getDrId());
      pstmt.setString(2, enqinfo.getEnqId());
      pstmt.setString(3, enqinfo.getMessageHeaderId());

      pstmt.executeUpdate();
      pstmt.close();

      conn.commit();
    }
    catch (SQLException ex)
    {
      conn.rollback();
      throw new ApplicationError("EnqueteInfo_DAO.class: createRecord(): SQLException", ex );
    }
    finally
    {
      conn.setAutoCommit(true);
    }
    return true;
  }

  /**
   * Select existing instances in persistent storage
   */
  public Object searchRecord(Connection  conn,
                             Object      searchObject,
                             String      filter)
    throws SQLException, NamingException
  {
    if (filter.equals("drId"))
    {
      try
      {
        // Get the search parameters out of the search Template Id
        String        drId    = (String) searchObject;

        //Get Enquete
        PreparedStatement selectQuery = conn.prepareStatement(getSQLStatement(SEARCH_ENQUETE_MIN_SQL));
        selectQuery.setObject(1, drId);

        ResultSet rs  = selectQuery.executeQuery();
        Object found = populateObject(conn,rs);

        rs.close();
        selectQuery.close();

        // Template Object return
        return found;
      }
      catch(NoObjectFoundException ex)
      {
        return null;
      }
    }
    else if (filter.equals("enqueteInfo"))
    {
      try
      {
        // Get the search parameters out of the search Template Id
        EnqueteInfo        enqinfo    = (EnqueteInfo) searchObject;

        // Get Enquete Status by database
        PreparedStatement selectQuery = conn.prepareStatement(getSQLStatement(SEARCH_ENQUETE_LOG_SQL));
        selectQuery.setObject(1, enqinfo.getDrId());
        selectQuery.setObject(2, enqinfo.getEnqId());

        ResultSet rs  = selectQuery.executeQuery();
        Object found = populateObject(conn,rs);

        rs.close();
        selectQuery.close();

        if( found == null ){
          this.createRecord(conn,searchObject);
          return searchObject;
        }

        // Template Object return
        return found;
      }
      catch(NoObjectFoundException ex)
//      catch(Exception ex)
      {
//System.out.println("Exception: " + ex.toString() );
        this.createRecord(conn,searchObject);
        return searchObject;
      }
    }
    else
      throw new RuntimeException("Invalid query filter type");
  }

  /**
   * Update existing instances in persistent storage
   */
  public boolean updateRecord(Connection  conn,
                              Object      saveContact)
    throws SQLException, NamingException
  {
    throw new NotImplementedException();
  }

  /**
   * Delete existing instances in persistent storage
   */
  public boolean deleteRecord(Connection  conn,
                              Object      deleteContact)
    throws SQLException, NamingException
  {
    throw new NotImplementedException();
  }

  /**
   * Create new instances in persistent storage
   */
  public boolean createMultiple(Connection conn, Object modifyingObject)
    throws SQLException, NamingException
  {
    EnqueteInfo enqinfo = (EnqueteInfo)modifyingObject;
    try
    {
      conn.setAutoCommit(false);
      PreparedStatement pstmt = conn.prepareStatement(getSQLStatement(INSERT_ANSWER_SQL));

      List answer = (List)enqinfo.getEnqueteAnswer();

      for(int ii=0; ii<answer.size(); ii++){
        EnqueteAnswer ans = (EnqueteAnswer)answer.get(ii);
        pstmt.setString(1, enqinfo.getDrId());
        pstmt.setString(2, enqinfo.getEnqId());
        pstmt.setString(3, ans.getMinorId());
        pstmt.setString(4, ans.getFiled());
        pstmt.setString(5, ans.getAnswer());

        pstmt.executeUpdate();
      }
      pstmt.close();

      // It changes into the state where status was answered.
      pstmt = conn.prepareStatement(getSQLStatement(UPDATE_ENQUETE_LOG_SQL));

      pstmt.setString(1, enqinfo.getDrId());
      pstmt.setString(2, enqinfo.getEnqId());

      pstmt.executeUpdate();
      pstmt.close();

      // Add enquete answer Point
      if( enqinfo.getEnqId().equals("0002") )
        pstmt = conn.prepareStatement(getSQLStatement(ADD_ENQUETE_POINT2)); // 50 Point
      else
        pstmt = conn.prepareStatement(getSQLStatement(ADD_ENQUETE_POINT)); // 100 Point

      pstmt.setString(1, enqinfo.getDrId());

      pstmt.executeUpdate();
      pstmt.close();

      conn.commit();
    }
    catch (SQLException ex)
    {
      conn.rollback();
      throw new ApplicationError("EnqueteInfo_DAO.class: createMultiple(): SQLException", ex );
    }
    finally
    {
      conn.setAutoCommit(true);
    }
    return true;
  }


  /**
   * Retrieves existing instances from persistent storage
   */
  public Object searchMultiple(Connection conn, Object searchObject, String filter)
    throws SQLException, NamingException
  {
    if (filter.equals("Question"))
    {
      // Get the search parameters out of the search object TemplateCategory
      EnqueteInfo  enqinfo     = (EnqueteInfo) searchObject;

      // Get Enquete  by database
      PreparedStatement selectQuery = conn.prepareStatement(getSQLStatement(SEARCH_QUESTION_LIST_SQL));
      selectQuery.setObject(1, enqinfo.getEnqId());
      selectQuery.setObject(2, enqinfo.getGroupId());

      ResultSet rs  = selectQuery.executeQuery();
      Collection returnSet = new ArrayList();
      try
      {
        while (rs.next())
          returnSet.add(rs.getString("question_name"));
      }
      catch (NoObjectFoundException NOF)
      { /* Don't panic - this is the end of the list */}
      rs.close();
      selectQuery.close();
      return returnSet;
    }
    else
      throw new RuntimeException("Invalid query filter type");
  }

  /**
   * Saves existing instances in persistent storage
   */
  public boolean updateMultiple(Connection conn, Object modifyingObject)
    throws SQLException, NamingException
  {
    throw new NotImplementedException();
  }

  /**
   * Deletes existing instances from persistent storage
   */
  public boolean deleteMultiple(Connection conn, Object modifyingObject)
    throws SQLException, NamingException
  {
    throw new NotImplementedException();
  }

}
