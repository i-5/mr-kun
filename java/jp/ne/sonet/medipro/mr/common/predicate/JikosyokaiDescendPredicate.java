package jp.ne.sonet.medipro.mr.common.predicate;

import com.objectspace.jgl.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
/**
 * ここで型の記述を挿入してください。
 * 作成日 : (00/06/27 午後 10:20:16)
 * @author: 
 */
public class JikosyokaiDescendPredicate implements com.objectspace.jgl.BinaryPredicate {
    /**
     * execute メソッド・コメント。
     */
    public boolean execute(Object first, Object second) {
	// ソート条件は降順
	if (((MsgInfo)first).getBody().getJikosyokai() == null)
	    ((MsgInfo)first).getBody().setJikosyokai("");
	
	if (((MsgInfo)second).getBody().getJikosyokai() == null)
	    ((MsgInfo)second).getBody().setJikosyokai("");
	
	if (((MsgInfo)first).getBody().getJikosyokai()
	    .compareTo(((MsgInfo)second).getBody().getJikosyokai()) > 0) {
	    return true;
	} else {
	    return false;
	}
    }
}
