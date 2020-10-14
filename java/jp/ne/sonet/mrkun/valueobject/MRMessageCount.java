
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.valueobject;

/**
 * A Class class.
 * <P>
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 */
public class MRMessageCount extends Object
{
  private String  mrId;
  private int     newEDetails; // number of ...
  private int     newContacts; // number of ...
  private String  newEDetailId; // iff only one message from MR->DR this holds the relevant Message_HEADER_ID
  private String  newContactId; // iff only one message from DR->MR this holds the relevant Message_HEADER_ID
  
  /**
   * Constructor
   */
  public MRMessageCount()  {}

  public String   getMrId()           {return this.mrId;}
  public int      getNewEDetails()    {return this.newEDetails;}
  public int      getNewContacts()    {return this.newContacts;}
  public String   getNewEDetailId()   {return this.newEDetailId;}
  public String   getNewContactId()   {return this.newContactId;}

  public void setMrId(String mrId)                  {this.mrId = mrId;}
  public void setNewEDetails(int newEDetails)       {this.newEDetails = newEDetails;}
  public void setNewContacts(int newContacts)       {this.newContacts = newContacts;}
  public void setNewEDetailId(String newEDetailId)  {this.newEDetailId = newEDetailId;}
  public void setNewContactId(String newContactId)  {this.newContactId = newContactId;}

}

 