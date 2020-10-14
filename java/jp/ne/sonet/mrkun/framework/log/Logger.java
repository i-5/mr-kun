package jp.ne.sonet.mrkun.framework.log;

/**
 * <P>This is the Logger instance of logging interface--uses MsgLogger.
 * 
 * @author 	Damon Lok
 * @version 	2
 */
public final class Logger
{
	/** Constant representing debug level logging (and all below) */
	public final static int DEBUG_LEVEL		= MsgLogger.DEBUG_LEVEL;
	/** Constant representing informational level logging (and all below) */
	public final static int INFO_LEVEL		= MsgLogger.INFO_LEVEL;
	/** Constant representing warning level logging (and all below) */
	public final static int WARNING_LEVEL	= MsgLogger.WARNING_LEVEL;
	/** Constant representing error level logging (and all below) */
	public final static int ERROR_LEVEL		= MsgLogger.ERROR_LEVEL;
	/** Constant representing exception level logging (this is the minimum) */
	public final static int EXCEPTION_LEVEL	= MsgLogger.EXCEPTION_LEVEL;
	/** Constant property name for log level */
	public final static String LOG_LEVEL = MsgLogger.LOG_LEVEL;
	/** Constant property name for log file name */
	public final static String LOG_FILE = MsgLogger.LOG_FILE;

	/**
	 * Default constructor; private because class is singleton
	 */
	private Logger ()
	{
	}

	/**
	 * Passthrough to MsgLogger's log method
     * @param pMsg The message to log
     * @param pLevel The current logging level
	 */
	public static void log (String pMsg, int pLevel)
	{
		MsgLogger.log (pMsg, pLevel);
	}

	/**
	 * Passthrough to MsgLogger's log method
     * @param pExc The throwable to log
	 */
	public static void log (Throwable pExc)
	{
		MsgLogger.log (pExc);
	}

	/**
	 * Passthrough to MsgLogger's setLogLevel method
     * @param pLevel The level of logging (debug, etc.)
	 */
	public static void setLogLevel (int pLevel)
	{
		MsgLogger.setLogLevel (pLevel);
	}
    
    public static int getLogLevel(){
    	return MsgLogger.getLogLevel();
    }

}
