package jp.ne.sonet.medipro.mr.common.predicate;

import com.objectspace.jgl.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
/**
 * ここで型の記述を挿入してください。
 * 作成日 : (00/06/27 午後 10:20:16)
 * @author: 
 */
public class StatisticsTargetRankAscendPredicate implements com.objectspace.jgl.BinaryPredicate {
    /**
     * execute メソッド・コメント。
     */
  /*   
    public boolean execute(Object first, Object second) {
	// ソート条件は昇順
	if (((StatisticsDrInfo)first).getTargetRank() == null)
	    ((StatisticsDrInfo)first).setTargetRank("");

	if (((StatisticsDrInfo)second).getTargetRank() == null)
	    ((StatisticsDrInfo)second).setTargetRank("");
	
	if (((StatisticsDrInfo)first).getTargetRank()
	    .compareTo(((StatisticsDrInfo)second).getTargetRank()) < 0) {
	    return true;
	} else {
	    return false;
	}
    }

*/
    public boolean execute(Object first, Object second) {
	// ソート条件は昇順
//System.out.println("統計で昇順");
	if (((StatisticsDrInfo)first).getTargetName() == null)
	    ((StatisticsDrInfo)first).setTargetName("");

	if (((StatisticsDrInfo)second).getTargetName() == null)
	    ((StatisticsDrInfo)second).setTargetName("");
	
	if (((StatisticsDrInfo)first).getTargetName()
	    .compareTo(((StatisticsDrInfo)second).getTargetName()) < 0) {
	    return true;
	} else {
	    return false;
	}
    }

}
