
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.valueobject;

/**
 * This interface defines the behaviours of any classes that are to be
 * treated as Assets. Implementing classes include WebImage and ResourceLink.
 *
 * @author <a href="mailto:rick_knowles@hotmail.com">Rick Knowles</a>
 * @version $Id: AssetInterface.java,v 1.1.2.1 2001/07/17 03:39:49 rick Exp $
 */
public interface AssetInterface
{

  /**
   * Sets the name of the asset
   */
  public void setName(String name);

  /**
   * Gets the name of the asset
   */
  public String getName();

  /**
   * Sets a description of the asset
   */
  public void setDescription(String description);

  /**
   * Gets a description of the asset
   */
  public String getDescription();
  
}

 