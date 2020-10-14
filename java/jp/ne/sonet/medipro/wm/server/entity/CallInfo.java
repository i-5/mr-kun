package jp.ne.sonet.medipro.wm.server.entity;

import java.util.*;

/**
 * <strong>コール内容情報</strong>
 * <br>
 * @author
 * @version
 */
public class CallInfo {
    protected String callCD;			// コール内容コード
    protected String callNaiyo;			// コール内容

    /**
     * CallInfo コンストラクタ
     */
    public CallInfo() {
    }

    /**
     * コール内容コードの取得
     * @return コール内容コード
     */
    public String getCallCD() {
        return callCD;
    }

    /**
     * コール内容の取得
     * @return コール内容
     */
    public String getCallNaiyo() {
        return callNaiyo;
    }

    /**
     * コール内容コードのセット
     * @param newCallCD コール内容コード
     */
    public void setCallCD(String newCallCD) {
        callCD = newCallCD;
    }

    /**
     * コール内容のセット
     * @param newCallNaiyo コール内容
     */
    public void setCallNaiyo(String newCallNaiyo) {
        callNaiyo = newCallNaiyo;
    }

    /**
     * 文字列化する
     * @return 文字列
     */
    public String toString() {
        StringBuffer me = new StringBuffer();
        me.append(callCD + "\n");
        me.append(callNaiyo + "\n");

        return me.toString();
    }
}
