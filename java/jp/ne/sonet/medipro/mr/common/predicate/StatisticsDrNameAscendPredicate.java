package jp.ne.sonet.medipro.mr.common.predicate;

import com.objectspace.jgl.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
/**
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/27 �ߌ� 10:20:16)
 * @author: 
 */
public class StatisticsDrNameAscendPredicate implements com.objectspace.jgl.BinaryPredicate {
    /**
     * execute ���\�b�h�E�R�����g�B
     */
//      public boolean execute(Object first, Object second) {
//  	// �\�[�g�����͏���
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
	// �\�[�g�����͏���
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
