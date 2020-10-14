package jp.ne.sonet.mrkun.log;

import java.io.*;
import java.util.*;
import org.jdom.output.*;
import jp.ne.sonet.mrkun.framework.valueobject.*;

import jp.ne.sonet.mrkun.framework.exception.*;

/**
 * This object is used to group together information used in the logging data.
 * The log object can be anything, and if it subclasses BaseValueObject, it
 * is serialized to XML.
 *
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 * @version $Id: ThreadLogger.java,v 1.1.2.1 2001/11/13 08:06:00 rick Exp $
 */
public class ThreadLogger implements Runnable
{
  final static int SLEEP_INTERVAL = 1000;

  private static Thread   writerThread;
  private static List     outItems;
  private static List     outTimes;
  private static boolean  continueFlag;
  private static Object   semaphore;
  private static String   logLocation = "mrkun-timing.log";

  private static void init()
  {
    try
    {
      // Move the old log
      File oldLog = new File(logLocation);
      File archiveName = new File(logLocation + ".old");
      archiveName.delete();
      if (oldLog.exists())
        if (oldLog.renameTo(archiveName))
          oldLog.delete();
    }
    catch (Exception err)
    {
      throw new ApplicationError("Error archiving old log file", err);
    }
    outItems = new Vector();
    outTimes = new Vector();
    semaphore = new Boolean(true);
    continueFlag = true;
    writerThread = new Thread(new ThreadLogger());
    writerThread.start();
  }

  public static void stopThread()
  {
    continueFlag = false;
  }

  public static void setLogLocation(String location)
  {
    logLocation = location;
  }

  public void run()
  {
    while (continueFlag)
    try
    {
      // Write the item
      synchronized (semaphore)
      {
        if (!outItems.isEmpty())
        {
          // Get the file stream
          OutputStream outStr = requestStream();
          Writer out = new OutputStreamWriter(outStr);

          while (!outItems.isEmpty())
          {
            Object item     = outItems.get(0);
            Date   itemDate = (Date) outTimes.get(0);
            outItems.remove(0);
            outTimes.remove(0);
            out.write(itemDate.getTime() + ",");
            if (item instanceof BaseValueObject)
            {
              XMLOutputter outWriter = new XMLOutputter("", false);
              outWriter.output(((BaseValueObject)item).toXML("logItem"), out);
            }
            else
              out.write(item.toString());
            out.write("\r\n");
          }
          out.flush();
          releaseStream(outStr);
        }
      }
      Thread.sleep(SLEEP_INTERVAL);
    }
    catch (Exception err)
    {
      throw new ApplicationError("Error in logging thread", err);
    }
  }

  public static OutputStream requestStream() throws IOException
  {
    File loc = new File(logLocation);
    return new FileOutputStream(logLocation, true);
  }

  public static void releaseStream(OutputStream out) throws IOException
  {
    out.close();
  }

  public static void log(Object item)
  {
    if (semaphore == null) init();
    synchronized (semaphore)
    {
      outTimes.add(new Date());
      outItems.add(item);
    }
  }

/*
  public static void main(String argv[]) throws Exception
  {
    ThreadLogger.log("Whatever I want");
    Thread.sleep(20);
    for (int n=0; n < 50; n++)
      ThreadLogger.log("Log " + n);
    Thread.sleep(1500);
    ThreadLogger.stopThread();

    System.out.println("Finished: in queue = " + outItems.size());
  }
*/
}

