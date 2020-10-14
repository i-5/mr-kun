
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.dao;

import java.sql.*;

/**
 * A temporary class used to simulate oracle sequences
 * 
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id: Sequence.java,v 1.1.2.2 2001/08/07 02:03:31 rick Exp $
 */
public class Sequence
{
  public static synchronized int getNext(Connection conn, String name) throws SQLException
  {
    // Get value
    PreparedStatement seqQuery = conn.prepareStatement("SELECT currval FROM Sequence WHERE name = ?");
    seqQuery.setString(1, name);
    ResultSet results = seqQuery.executeQuery();
    int currval = -1;
    while (results.next())
      currval = results.getInt(1);
    results.close();
    seqQuery.close();

    // Update value
    seqQuery = conn.prepareStatement("UPDATE Sequence SET currval = ? WHERE name = ?");
    seqQuery.setInt(1, currval + 1);
    seqQuery.setString(2, name);
    seqQuery.executeUpdate();
    seqQuery.close();

    return currval + 1;
  }
}

