
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.valueobject;

import org.jdom.*;
import jp.ne.sonet.mrkun.framework.valueobject.BaseValueObject;

/**
 * This class models the common attributes and behaviours for users
 * within the MRKun system.
 *
 * @author <a href="mailto:rick_knowles@hotmail.com">Rick Knowles</a>
 * @version $Id: User.java,v 1.1.1.1.2.2 2001/09/12 04:40:15 rick Exp $
 */
public class User extends BaseValueObject
{
  private String  kanaName;
  private String  kanjiName;

  /**
   * Constructor
   */
  public User() {}

  public String getKanaName()
  {
    return this.kanaName;
  }

  public String getKanjiName()
  {
    return this.kanjiName;
  }

  public void setKanjiName(String kanjiName)
  {
    this.kanjiName = kanjiName;
  }

  public void setKanaName(String kanaName)
  {
    this.kanaName = kanaName;
  }

  public Element toXML(String elementName)
  {
    Element masterElement = super.toXML(elementName);
    if (getKanjiName() != null)
      masterElement.addContent(new Element("kanjiName").setText(getKanjiName()));
    if (getKanaName() != null)
      masterElement.addContent(new Element("kanaName").setText(getKanaName()));
    return masterElement;
  }

}

 