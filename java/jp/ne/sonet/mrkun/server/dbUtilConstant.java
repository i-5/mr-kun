// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.server; 

/**
 *
 * This captures the JDBC connection pool properties constants that 
 * used by non-DAO and non-sessionEJB classes for getting the database connection.
 *
 * @author Damon Lok
 * @version $Id: dbUtilConstant.java,v 1.1.2.1 2001/10/15 05:26:40 damon Exp $
 *
 */
public class dbUtilConstant
{
 // properties for JDBC connection pool
 public final static String JDBC_LOOKUP             = "jdbc:weblogic:pool:mrkun";
 public final static String JDBC_USER               = "mrkunUser";
 public final static String JDBC_PASS               = "mummamia";
}