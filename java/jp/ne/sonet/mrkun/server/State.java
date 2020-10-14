
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.server;


import java.util.*;
import java.lang.*;

public class State extends Object
{

  private String name;
  private List stack = null;;
  private int SP = -1;
  private Map data;

  /**
   * Constructor
   */
  public State(String n)
  {
	name = new String(n);
	data = new Hashtable();
	data.put("daddy",this);
    stack = new ArrayList();
    SP = -1;
  }

  public void push(String executor, String message, Map args)
  {
		Map m = new Hashtable();
        Map ex = new Hashtable();
        Map out = new Hashtable();
        ex.put("class",executor);
        ex.put("method", message);
        m.put("executor",ex);
        m.put("args", args);
		m.put("out", out);
		stack.add(m);
        SP = stack.size() - 1;
  }

  public void setReturnCode(String ret)
  {
   peek(SP).put("status",ret);	
  }
  
  public String getReturnCode()
  {
   return((String) peek(SP).get("status"));	
  }
  public Map pop()
  {
  	Map ret = (Map) stack.get(SP);
    stack.remove(SP);
    return(ret);
    
  }
  
  
  public Map peek(int sp)
  {
  	Map entry = (Map) stack.get(sp);
    return(entry);
  	
  }
  
  public int getSP()
  {
  	return this.SP;
  }
  
  public String getName() 
  {
  	 return this.name;
     
  }
  
  public void setName(String n)
  {
  	this.name = n;
    data.put("name",n);
  }
  
  public Map getData()
  {
  	return this.data;
  }
  
  public void setData(Map d)
  {
  	this.data = d;
  }
  
  public Object getField(String key)
  {
  	return data.get(key);
  }
  
  public void setField(String key, Object value)
  {
  	data.put(key,value);
  }
  
  
} // class State

 