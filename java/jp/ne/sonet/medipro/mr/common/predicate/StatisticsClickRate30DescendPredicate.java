package jp.ne.sonet.medipro.mr.common.predicate;

import com.objectspace.jgl.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
/**
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/27 �ߌ� 10:20:16)
 * @author: 
 */
public class StatisticsClickRate30DescendPredicate implements com.objectspace.jgl.BinaryPredicate {
    /**
     * execute ���\�b�h�E�R�����g�B
     */
    public boolean execute(Object first, Object second) {
	// �\�[�g�����͍~��
	return ((StatisticsDrInfo)first).getClickRate30() > 
	    ((StatisticsDrInfo)second).getClickRate30();
    }
}
