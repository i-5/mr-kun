package jp.ne.sonet.medipro.mr.common.predicate;

import com.objectspace.jgl.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
/**
 * ここで型の記述を挿入してください。
 * 作成日 : (00/06/27 午後 10:20:16)
 * @author: 
 */
public class MrRollCoNameAscendPredicate implements com.objectspace.jgl.BinaryPredicate {
    /**
     * execute メソッド・コメント。
     */
    public boolean execute(Object first, Object second) {
	// ソート条件は昇順
	if (((TantoInfo)first).getMrinfo().getCompanyName() == null)
	    ((TantoInfo)first).getMrinfo().setCompanyName("");
	
	if (((TantoInfo)second).getMrinfo().getCompanyName() == null)
	    ((TantoInfo)second).getMrinfo().setCompanyName("");
	
	if (((TantoInfo)first).getMrinfo().getCompanyName()
	    .compareTo(((TantoInfo)second).getMrinfo().getCompanyName()) < 0 ) {
	    return true;
	} else {
	    return false;
	}
    }
}
