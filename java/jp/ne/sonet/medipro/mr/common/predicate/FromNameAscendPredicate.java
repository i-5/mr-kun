package jp.ne.sonet.medipro.mr.common.predicate;

import com.objectspace.jgl.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
/**
 * ここで型の記述を挿入してください。
 * 作成日 : (00/06/27 午後 10:20:16)
 * @author: 
 */
public class FromNameAscendPredicate implements com.objectspace.jgl.BinaryPredicate {
    /**
     * execute メソッド・コメント。
     */
    public boolean execute(Object first, Object second) {
	// ソート条件は昇順
	if (((MsgInfo)first).getHeader().getFromNameKana() == null)
	    ((MsgInfo)first).getHeader().setFromNameKana("");

	if (((MsgInfo)second).getHeader().getFromNameKana() == null)
	    ((MsgInfo)second).getHeader().setFromNameKana("");
	
	if (((MsgInfo)first).getHeader().getFromNameKana()
	    .compareTo(((MsgInfo)second).getHeader().getFromNameKana()) < 0) {
	    return true;
	} else {
	    return false;
	}
    }
}
