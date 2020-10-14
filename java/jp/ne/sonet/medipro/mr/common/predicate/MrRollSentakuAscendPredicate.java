package jp.ne.sonet.medipro.mr.common.predicate;

import com.objectspace.jgl.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
/**
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/27 �ߌ� 10:20:16)
 * @author: 
 */
public class MrRollSentakuAscendPredicate implements com.objectspace.jgl.BinaryPredicate {
    /**
     * execute ���\�b�h�E�R�����g�B
     */
    public boolean execute(Object first, Object second) {
	// �\�[�g�����͏���
	if (((TantoInfo)first).getSentakuKbn() == null)
	    ((TantoInfo)first).setSentakuKbn("");

	if (((TantoInfo)second).getSentakuKbn() == null)
	    ((TantoInfo)second).setSentakuKbn("");
	
	if (((TantoInfo)first).getSentakuKbn()
	    .compareTo(((TantoInfo)second).getSentakuKbn()) < 0) {
	    return true;
	} else {
	    return false;
	}
    }
}
