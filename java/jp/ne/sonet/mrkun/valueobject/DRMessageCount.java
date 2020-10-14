
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.valueobject;

/**
 * A Class class.
 * <P>
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 */
public class DRMessageCount extends Object
{
  private String  drId;
  private int     newEDetails; // number of ...
  private int     newContacts; // number of ...
  private String  newEDetailId; // iff only one message from MR->DR this holds the relevant Message_HEADER_ID
  private String  newContactId; // iff only one message from DR->MR this holds the relevant Message_HEADER_ID
  private int     actionValue;
  
  /**
   * Constructor
   */
  public DRMessageCount()  {}

  public String   getDrId()           {return this.drId;}
  public int      getNewEDetails()    {return this.newEDetails;}
  public int      getNewContacts()    {return this.newContacts;}
  public String   getNewEDetailId()   {return this.newEDetailId;}
  public String   getNewContactId()   {return this.newContactId;}
  public int      getActionValue()    {return this.actionValue;}

  public void setDrId(String drId)                  {this.drId = drId;}
  public void setNewEDetails(int newEDetails)       {this.newEDetails = newEDetails;}
  public void setNewContacts(int newContacts)       {this.newContacts = newContacts;}
  public void setNewEDetailId(String newEDetailId)  {this.newEDetailId = newEDetailId;}
  public void setNewContactId(String newContactId)  {this.newContactId = newContactId;}
  public void setActionValue(int actionValue)       {this.actionValue = actionValue;}

}

 