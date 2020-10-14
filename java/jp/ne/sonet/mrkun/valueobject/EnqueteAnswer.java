
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.valueobject;

import jp.ne.sonet.mrkun.framework.valueobject.*;

/**
 *　EnqueteAnswer
 * アンケートの回答を格納するclassです。関係するテーブルはEnquete_answer です。
 * <P>
 * @author M.Mizuki</a>
 */

public class EnqueteAnswer extends BaseValueObject
{
    private String minorId;
    private String filed;
    private String answer;

  /**
   * Constructor
   */
  public EnqueteAnswer() { }

  public String getMinorId()   {return this.minorId;}
  public String getFiled()     {return this.filed;}
  public String getAnswer()    {return this.answer;}

  public void setMinorId(String minorId)       {this.minorId = minorId;}
  public void setFiled(String filed)           {this.filed = filed;}
  public void setAnswer(String answer)         {this.answer = answer;}

}
