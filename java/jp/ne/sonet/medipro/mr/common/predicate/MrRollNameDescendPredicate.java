package jp.ne.sonet.medipro.mr.common.predicate;

import com.objectspace.jgl.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
/**
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/27 �ߌ� 10:20:16)
 * @author: 
 */
public class MrRollNameDescendPredicate implements com.objectspace.jgl.BinaryPredicate {
    /**
     * execute ���\�b�h�E�R�����g�B
     */
    public boolean execute(Object first, Object second) {
	// �\�[�g�����͍~��
	if (((TantoInfo)first).getMrinfo().getNameKana() == null)
	    ((TantoInfo)first).getMrinfo().setNameKana("");

	if (((TantoInfo)second).getMrinfo().getNameKana() == null)
	    ((TantoInfo)second).getMrinfo().setNameKana("");
	
	if (((TantoInfo)first).getMrinfo().getNameKana()
	    .compareTo(((TantoInfo)second).getMrinfo().getNameKana()) > 0) {
	    return true;
	} else {
	    return false;
	}
    }
}
