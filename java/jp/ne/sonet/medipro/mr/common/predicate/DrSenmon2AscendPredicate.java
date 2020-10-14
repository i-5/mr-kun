package jp.ne.sonet.medipro.mr.common.predicate;

import com.objectspace.jgl.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
/**
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/27 �ߌ� 10:20:16)
 * @author: 
 */
public class DrSenmon2AscendPredicate implements com.objectspace.jgl.BinaryPredicate {
    /**
     * execute ���\�b�h�E�R�����g�B
     */
    public boolean execute(Object first, Object second) {
	// �\�[�g�����͏���
	if (((TantoInfo)first).getSenmon2() == null)
	    ((TantoInfo)first).setSenmon2("");
	
	if (((TantoInfo)second).getSenmon2() == null)
	    ((TantoInfo)second).setSenmon2("");
	
	if (((TantoInfo)first).getSenmon2()
	    .compareTo(((TantoInfo)second).getSenmon2()) < 0) {
	    return true;
	} else {
	    return false;
	}
    }
}
