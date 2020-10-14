
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.valueobject;

import jp.ne.sonet.mrkun.framework.valueobject.*;

/**
 * Models an email address
 *
 * @author <a href="mailto:rick_knowles@hotmail.com">Rick Knowles</a>
 * @version $Id: EmailContact.java,v 1.1.1.1.2.1 2001/07/17 03:39:49 rick Exp $
 */
public class EmailContact extends BaseValueObject
{
  private String  emailContactId;
  private String  name;
  private String  emailAddress;

  /**
   * Constructor
   */
  public EmailContact()
  {
  }

  public String getName()
  {
    return this.name;
  }

  public String getEmailAddress()
  {
    return this.emailAddress;
  }

  public String getEmailContactId()
  {
    return this.emailContactId;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public void setEmailAddress(String emailAddress)
  {
    this.emailAddress = emailAddress;
  }

  public void setEmailContactId(String emailContactId)
  {
    this.emailContactId = emailContactId;
  }
}

