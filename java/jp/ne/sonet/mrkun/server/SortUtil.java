// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.server;

import java.util.*;
import jp.ne.sonet.mrkun.framework.event.*;
import jp.ne.sonet.mrkun.valueobject.*;
import jp.ne.sonet.mrkun.framework.exception.*;

/**
 * This is the MR statistics event handler class for dr received contact event.  
 * It encapsulates logic for handling the received contact event read by the dr.  
 * When event is triggered, this will perform in-memory data (received contact count) 
 * update of the usage statistics (MR6 page). 
 *
 * @author Damon Lok
 * @version $Id$
 */
 
public class SortUtil 
{
  // The data structure that holds the values to compare with
  private Map dataStructure;
  
  // This will indicates what is to compare in the dataStructure
  private String sortCase;

  // Default constructor
  public SortUtil() {}
  
  // Constructor for sorting the ranking list   
  public SortUtil(Map dataStructure, String sortCase) 
  { 
    this.dataStructure = dataStructure;    
    this.sortCase = sortCase;  
  }
  
  void bubbleSort(List sortList)
  {
    boolean sorted = false;
    
    Object elements[] = sortList.toArray();
    
    for (int pass = 1; (pass < size) && (!sorted); pass++) 
    {
      sorted = true;      
      for (int index = 0; index < size - pass; index++)
      {
        int nextIndex = ++index;
        if (elements[index] > elements[nextIndex])
        {
          swap(elements[index], elements[nextIndex]);
          sorted = false;
        }
      }
    }
  }
  
  void swap(Object elementTobeSwap, Object elementToSwapFor)
  {
    Object temp;
    temp = elementTobeSwap;
    elementTobeSwap = elementToSwapFor;
    elementToSwapFor = temp;
  }
}   