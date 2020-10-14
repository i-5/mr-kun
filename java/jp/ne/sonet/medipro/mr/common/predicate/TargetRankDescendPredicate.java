package jp.ne.sonet.medipro.mr.common.predicate;

import com.objectspace.jgl.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
/**
 * ここで型の記述を挿入してください。
 * 作成日 : (00/06/27 午後 10:20:16)
 * @author: 
 */

    /**
     * execute メソッド・コメント。
     */

public class TargetRankDescendPredicate implements com.objectspace.jgl.BinaryPredicate {
/*     
    public boolean execute(Object first, Object second) {
	// ソート条件は降順
	if (((MsgInfo)first).getHeader().getTargetRank() == null)
	    ((MsgInfo)first).getHeader().setTargetRank("");

	if (((MsgInfo)second).getHeader().getTargetRank() == null)
	    ((MsgInfo)second).getHeader().setTargetRank("");
	
	if (((MsgInfo)first).getHeader().getTargetRank()
	    .compareTo(((MsgInfo)second).getHeader().getTargetRank()) > 0) {
	    return true;
	} else {
	    return false;
	}
    }
*/    
    
    public boolean execute(Object first, Object second) {
	// ソート条件は降順
//System.out.println("送信一覧で降順");
	if (((MsgInfo)first).getHeader().getTargetName() == null)
	    ((MsgInfo)first).getHeader().setTargetName("");

	if (((MsgInfo)second).getHeader().getTargetName() == null)
	    ((MsgInfo)second).getHeader().setTargetName("");
	
	if (((MsgInfo)first).getHeader().getTargetName()
	    .compareTo(((MsgInfo)second).getHeader().getTargetName()) > 0) {
	    return true;
	} else {
	    return false;
	}
    }
    
    
}
