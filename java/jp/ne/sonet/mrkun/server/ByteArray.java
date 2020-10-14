
// Copyright (c) 2001 So-net M3, Inc
package jp.ne.sonet.mrkun.server;

/**
 * A utility class to allow
 * @author <a href="mailto:r-knowles@so-netM3.co.jp">Rick Knowles</a>
 */
public class ByteArray extends Object
{
  private byte bytes[] = null;
  /**
   * Constructor
   */
  public ByteArray(byte input[])
  {
    this.bytes = input;
  }

  public byte[] getBytes()  {return this.bytes;}
}

 