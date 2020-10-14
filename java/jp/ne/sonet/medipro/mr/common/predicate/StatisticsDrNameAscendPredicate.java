package jp.ne.sonet.medipro.mr.common.predicate;

import com.objectspace.jgl.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
/**
 * ここで型の記述を挿入してください。
 * 作成日 : (00/06/27 午後 10:20:16)
 * @author: 
 */
public class StatisticsDrNameAscendPredicate implements com.objectspace.jgl.BinaryPredicate {
    /**
     * execute メソッド・コメント。
     */
//      public boolean execute(Object first, Object second) {
//  	// ソート条件は昇順
//  	if (((StatisticsDrInfo)first).getDrNameKana() == null)
//  	    ((StatisticsDrInfo)first).setDrNameKana("");

//  	if (((StatisticsDrInfo)second).getDrNameKana() == null)
//  	    ((StatisticsDrInfo)second).setDrNameKana("");
	
//  	if (((StatisticsDrInfo)first).getDrNameKana()
//  	    .compareTo(((StatisticsDrInfo)second).getDrNameKana()) < 0) {
//  	    return true;
//  	} else {
//  	    return false;
//  	}
//      }
    public boolean execute(Object first, Object second) {
	// ソート条件は昇順
	if (((StatisticsDrInfo)first).getDrName() == null)
	    ((StatisticsDrInfo)first).setDrName("");

	if (((StatisticsDrInfo)second).getDrName() == null)
	    ((StatisticsDrInfo)second).setDrName("");
	
	if (((StatisticsDrInfo)first).getDrName()
	    .compareTo(((StatisticsDrInfo)second).getDrName()) < 0) {
	    return true;
	} else {
	    return false;
	}
    }
}
