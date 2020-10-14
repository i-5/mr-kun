package jp.ne.sonet.medipro.mr.common.predicate;

import com.objectspace.jgl.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
/**
 * ここで型の記述を挿入してください。
 * 作成日 : (00/06/27 午後 10:20:16)
 * @author: 
 */
public class DrSenmon1DescendPredicate implements com.objectspace.jgl.BinaryPredicate {
    /**
     * execute メソッド・コメント。
     */
    public boolean execute(Object first, Object second) {
	// ソート条件は降順
	if (((TantoInfo)first).getSenmon1() == null)
	    ((TantoInfo)first).setSenmon1("");

	if (((TantoInfo)second).getSenmon1() == null)
	    ((TantoInfo)second).setSenmon1("");
	
	if (((TantoInfo)first).getSenmon1()
	    .compareTo(((TantoInfo)second).getSenmon1()) > 0) {
	    return true;
	} else {
	    return false;
	}
    }
}
