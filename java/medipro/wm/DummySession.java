package medipro.wm;

import java.io.*;
import java.util.*;

public class DummySession {
 private Hashtable hash;

 public DummySession()
 {
  hash = new Hashtable();
 }

 public Object getValue(String key)
 {
   return hash.get(key);
 }
 
 public void putValue(String key, Object value)
 {
   hash.put(key,value);
 }
}
