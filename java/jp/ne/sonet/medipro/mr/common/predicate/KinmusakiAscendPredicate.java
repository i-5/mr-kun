package jp.ne.sonet.medipro.mr.common.predicate;

import com.objectspace.jgl.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
/**
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/27 �ߌ� 10:20:16)
 * @author: 
 */
public class KinmusakiAscendPredicate implements com.objectspace.jgl.BinaryPredicate {
    /**
     * execute ���\�b�h�E�R�����g�B
     */
    public boolean execute(Object first, Object second) {
	// �\�[�g�����͏���
	if (((TantoInfo)first).getKinmusaki() == null)
	    ((TantoInfo)first).setKinmusaki("");

	if (((TantoInfo)second).getKinmusaki() == null)
	    ((TantoInfo)second).setKinmusaki("");
	
	if (((TantoInfo)first).getKinmusaki()
	    .compareTo(((TantoInfo)second).getKinmusaki()) < 0) {
	    return true;
	} else {
	    return false;
	}
    }
}
