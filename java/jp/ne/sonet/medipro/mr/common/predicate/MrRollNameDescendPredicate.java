package jp.ne.sonet.medipro.mr.common.predicate;

import com.objectspace.jgl.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
/**
 * ここで型の記述を挿入してください。
 * 作成日 : (00/06/27 午後 10:20:16)
 * @author: 
 */
public class MrRollNameDescendPredicate implements com.objectspace.jgl.BinaryPredicate {
    /**
     * execute メソッド・コメント。
     */
    public boolean execute(Object first, Object second) {
	// ソート条件は降順
	if (((TantoInfo)first).getMrinfo().getNameKana() == null)
	    ((TantoInfo)first).getMrinfo().setNameKana("");

	if (((TantoInfo)second).getMrinfo().getNameKana() == null)
	    ((TantoInfo)second).getMrinfo().setNameKana("");
	
	if (((TantoInfo)first).getMrinfo().getNameKana()
	    .compareTo(((TantoInfo)second).getMrinfo().getNameKana()) > 0) {
	    return true;
	} else {
	    return false;
	}
    }
}
