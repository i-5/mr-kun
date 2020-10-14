package jp.ne.sonet.mrkun.framework.log;

import java.io.*;
import java.util.Date;

/**
 * <P>Debug/info/error logger class.  Defines levels, writes to
 *				stdout and stderr depending upon filter; also writes to file.
 * 
 * @author 	Damon Lok
 * @version 	2
 */
final class MsgLogger
{
	/** Constant representing debug level logging (and all below) */
	public final static int DEBUG_LEVEL		= 4000;
	/** Constant representing informational level logging (and all below) */
	public final static int INFO_LEVEL		= 3000;
	/** Constant representing warning level logging (and all below) */
	public final static int WARNING_LEVEL	= 2000;
	/** Constant representing error level logging (and all below) */
	public final static int ERROR_LEVEL		= 1000;
	/** Constant representing exception level logging (this is the minimum) */
	public final static int EXCEPTION_LEVEL	= 0;
	/** Constant printed when log writes debug message to stream */
	private final static String DEBUG_LEVEL_STR		= "DEBUG:\t";
	/** Constant printed when log writes information message to stream */
	private final static String INFO_LEVEL_STR		= "INFO:\t";
	/** Constant printed when log writes warning message to stream */
	private final static String WARNING_LEVEL_STR	= "WARNING:\t";
	/** Constant printed when log writes error message to stream */
	private final static String ERROR_LEVEL_STR		= "ERROR:\t";
	/** Constant printed when log writes exception to stream */
	private final static String EXCEPTION_LEVEL_STR	= "EXCEPTION:\t";
	/** Constant printed when log writes an undefined level to stream */
	private final static String UND_LEVEL_STR		= "Undef Level:\t";
	/** Constant property name for log level */
	public final static String LOG_LEVEL = "log.level";
	/** Constant property name for log file name */
	public final static String LOG_FILE = "log.file";

	/** Current log level allowed */
	private static int currLevel = ERROR_LEVEL;   // default to app errors
	/** Instance of message logger object */
	private static MsgLogger instance = null;
	/** File writer when log file is used */
	private static FileWriter filestr = null;



	/**
	 * Default constructor; invisible because class is singleton
	 */
	private MsgLogger ()
	{
    	String level = System.getProperties().getProperty (LOG_LEVEL);
        if (level!=null)
        	if (level.equalsIgnoreCase ("WARNING"))
            	this.currLevel = WARNING_LEVEL;
            else if (level.equalsIgnoreCase ("INFO"))
            	this.currLevel = INFO_LEVEL;
            else if  (level.equalsIgnoreCase ("DEBUG"))
            	this.currLevel = DEBUG_LEVEL;    
	}

	/**
	 * Get an instance of logging object
	 * @return Instance of logger
	 */
	public MsgLogger getInstance ()
	{
		if (this.instance==null)
			this.instance = new MsgLogger ();
		return this.instance;
	}

	/**
	 * Set filtering to write the specified log level and below
	 * @param pLevel Integer value of logging level
	 *					(DEBUG_LEVEL,INFO_LEVEL,WARNING_LEVEL,
	 *					 ERROR_LEVEL,EXCEPTION_LEVEL)
	 */
	public static void setLogLevel (int pLevel)
	{
		if (pLevel>=EXCEPTION_LEVEL)
			MsgLogger.currLevel = pLevel;
		else
			System.err.println ("Attempting to set log level outside of acceptable range");
	}
    
	public static int getLogLevel(){
		return MsgLogger.currLevel;
	}
    

	/**
	 * Accept request to log exception
	 * @param pExc Throwable to log
	 */
	public static void log (Throwable pExc)
	{
		String msg = null;
			// if debug, print stack trace
		if (MsgLogger.currLevel >= MsgLogger.DEBUG_LEVEL)
			msg = MsgLogger.getStackTrace (pExc);
		else  // otherwise (not debug), just get exception message
			msg = pExc.toString();
		MsgLogger.log (msg, EXCEPTION_LEVEL); //log the message
	}

	/**
	 * Write the log message to a stream
	 * @param pStream PrintStream to send log message to
	 * @param pMsg String with log message
	 * @param pLevel String specifying log level
	 */
	protected static void writeLogMessage (PrintStream pStream, String pMsg, String pLevel)
	{
		String logFile = System.getProperty (LOG_FILE);
		StringBuffer logLine = new StringBuffer (new Date().toString());

		logLine.append (" | ").append (pLevel).append (pMsg);
			// if applicable write to log file
		if (logFile!=null && !logFile.equals(""))
		{
			try {
				if (filestr==null)
					filestr = new FileWriter (logFile, true);
				filestr.write (logLine.toString());
				filestr.write ("\r\n");
				filestr.flush ();
			} catch (IOException exc) {
				exc.printStackTrace();
			}
		}
		pStream.println (logLine.toString());
		pStream.flush ();
	}

	/**
	 * Accept request to log message with level
	 * @param pMsg String with log message
	 * @param pLevel Integer specifying log level
	 */
	public static void log (String pMsg, int pLevel)
	{
		if (MsgLogger.currLevel >= pLevel) {
			switch (pLevel)
			{
				case DEBUG_LEVEL:
					MsgLogger.writeLogMessage (System.err, pMsg, DEBUG_LEVEL_STR);
					break;
				case INFO_LEVEL:
					MsgLogger.writeLogMessage (System.out, pMsg, INFO_LEVEL_STR);
					break;
				case WARNING_LEVEL:
					MsgLogger.writeLogMessage (System.out, pMsg, WARNING_LEVEL_STR);
					break;
				case ERROR_LEVEL:
					MsgLogger.writeLogMessage (System.err, pMsg, ERROR_LEVEL_STR);
					break;
				case EXCEPTION_LEVEL:
					MsgLogger.writeLogMessage (System.err, pMsg, EXCEPTION_LEVEL_STR);
					break;
				default:
					MsgLogger.writeLogMessage (System.out, pMsg, UND_LEVEL_STR);
			}
		}
	}

	private static String getStackTrace (Throwable anException)
	{
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream ();
		PrintWriter writer = new PrintWriter (outputStream);
		anException.printStackTrace (writer);
		writer.flush ();
		return outputStream.toString ();
	}
}
