package jp.ne.sonet.medipro.mr.common.predicate;

import com.objectspace.jgl.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
/**
 * ここで型の記述を挿入してください。
 * 作成日 : (00/06/27 午後 10:20:16)
 * @author: 
 */
public class LinkLibCDAscendPredicate implements com.objectspace.jgl.BinaryPredicate {
    /**
     * execute メソッド・コメント。
     */
    public boolean execute(Object first, Object second) {
	// ソート条件は昇順
	if (((LinkLibInfo)first).getLinkCD() == null)
	    ((LinkLibInfo)first).setLinkCD("");

	if (((LinkLibInfo)second).getLinkCD() == null)
	    ((LinkLibInfo)second).setLinkCD("");
	
	if (((LinkLibInfo)first).getLinkCD()
	    .compareTo(((LinkLibInfo)second).getLinkCD()) < 0) {
	    return true;
	} else {
	    return false;
	}
    }
}
