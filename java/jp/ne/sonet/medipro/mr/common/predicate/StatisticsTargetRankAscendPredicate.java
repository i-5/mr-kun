package jp.ne.sonet.medipro.mr.common.predicate;

import com.objectspace.jgl.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
/**
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/27 �ߌ� 10:20:16)
 * @author: 
 */
public class StatisticsTargetRankAscendPredicate implements com.objectspace.jgl.BinaryPredicate {
    /**
     * execute ���\�b�h�E�R�����g�B
     */
  /*   
    public boolean execute(Object first, Object second) {
	// �\�[�g�����͏���
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
	// �\�[�g�����͏���
//System.out.println("���v�ŏ���");
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
