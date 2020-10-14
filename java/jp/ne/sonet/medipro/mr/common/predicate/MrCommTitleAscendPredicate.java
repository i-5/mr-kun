package jp.ne.sonet.medipro.mr.common.predicate;

import com.objectspace.jgl.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
/**
 * ここで型の記述を挿入してください。
 * 作成日 : (00/06/27 午後 10:20:16)
 * @author: 
 */
public class MrCommTitleAscendPredicate implements com.objectspace.jgl.BinaryPredicate {
    /**
     * execute メソッド・コメント。
     */
    public boolean execute(Object first, Object second) {
	// ソート条件は昇順
	if (((MsgInfo)first).getBody().getTitle() == null)
	    ((MsgInfo)first).getBody().setTitle("");

	if (((MsgInfo)second).getBody().getTitle() == null)
	    ((MsgInfo)second).getBody().setTitle("");
	
	if (((MsgInfo)first).getBody().getTitle()
	    .compareTo(((MsgInfo)second).getBody().getTitle()) < 0) {
	    return true;
	} else {
	    return false;
	}
    }
}
