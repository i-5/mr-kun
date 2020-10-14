package jp.ne.sonet.medipro.mr.common.predicate;

import com.objectspace.jgl.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
/**
 * ここで型の記述を挿入してください。
 * 作成日 : (00/06/27 午後 10:20:16)
 * @author: 
 */
public class DrSenmon2AscendPredicate implements com.objectspace.jgl.BinaryPredicate {
    /**
     * execute メソッド・コメント。
     */
    public boolean execute(Object first, Object second) {
	// ソート条件は昇順
	if (((TantoInfo)first).getSenmon2() == null)
	    ((TantoInfo)first).setSenmon2("");
	
	if (((TantoInfo)second).getSenmon2() == null)
	    ((TantoInfo)second).setSenmon2("");
	
	if (((TantoInfo)first).getSenmon2()
	    .compareTo(((TantoInfo)second).getSenmon2()) < 0) {
	    return true;
	} else {
	    return false;
	}
    }
}
