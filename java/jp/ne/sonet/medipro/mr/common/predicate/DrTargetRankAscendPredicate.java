package jp.ne.sonet.medipro.mr.common.predicate;

import com.objectspace.jgl.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
/**
 * ここで型の記述を挿入してください。
 * 作成日 : (00/06/27 午後 10:20:16)
 * @author: 
 */
public class DrTargetRankAscendPredicate implements com.objectspace.jgl.BinaryPredicate {
    /**
 * execute メソッド・コメント。
 */
 /*
    public boolean execute(Object first, Object second) {
	// ソート条件は昇順
	if (((TantoInfo)first).getTargetRank() == null)
	    ((TantoInfo)first).setTargetRank("");
	if (((TantoInfo)second).getTargetRank() == null)
	    ((TantoInfo)second).setTargetRank("");
	
	if (((TantoInfo)first).getTargetRank()
	    .compareTo(((TantoInfo)second).getTargetRank()) < 0) {
	    return true;
	} else {
	    return false;
	}
    }
    
 */   
    //ターゲットランクではなくターゲットランク名でソート
     public boolean execute(Object first, Object second) {
	// ソート条件は昇順
//System.out.println("顧客管理は昇順");
	if (((TantoInfo)first).getTargetName() == null)
	    ((TantoInfo)first).setTargetName("");
	if (((TantoInfo)second).getTargetName() == null)
	    ((TantoInfo)second).setTargetName("");
	
	if (((TantoInfo)first).getTargetName()
	    .compareTo(((TantoInfo)second).getTargetName()) < 0) {
	    return true;
	} else {
	    return false;
	}
    }
    
    
}
