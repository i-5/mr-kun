/*
 * LoopTagExtraInfo.java
 *
 */

package jp.ne.sonet.mrkun.taglib.iterate;


import javax.servlet.jsp.tagext.*;


public class LoopTagExtraInfo extends TagExtraInfo
{
 public VariableInfo[] getVariableInfo(TagData data)
  {
    VariableInfo[] variableInfoArray = null;
    
    String counterVariable = data.getAttributeString("counterVariable");
    if (counterVariable != null)
    {
      VariableInfo vi1 = new VariableInfo(counterVariable, "Integer", true, VariableInfo.NESTED);
      variableInfoArray = new VariableInfo[] {vi1};
    }
    
    return variableInfoArray;
  }
}