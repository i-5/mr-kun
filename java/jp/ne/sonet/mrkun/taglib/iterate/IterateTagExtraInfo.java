/*
 * IterateTagExtraInfo.java
 *
 */

package jp.ne.sonet.mrkun.taglib.iterate;


import javax.servlet.jsp.tagext.*;


public class IterateTagExtraInfo extends TagExtraInfo
{
  public VariableInfo[] getVariableInfo(TagData data)
  {
    VariableInfo[] variableInfoArray = null;
    
    String variable = data.getAttributeString("variable");
    String type = data.getAttributeString("type");
    VariableInfo vi1 = new VariableInfo(variable, type, true, VariableInfo.NESTED);

    String counterVariable = data.getAttributeString("counterVariable");
    if (counterVariable != null)
    {
      VariableInfo vi2 = new VariableInfo(counterVariable, "Integer", true, VariableInfo.NESTED);
      variableInfoArray = new VariableInfo[] {vi1,vi2};
    }
    else
    {
      variableInfoArray = new VariableInfo[] {vi1};
    }
    
    return variableInfoArray;
  }
}