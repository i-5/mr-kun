package jp.ne.sonet.medipro.mr.common.predicate;

import com.objectspace.jgl.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
/**
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/27 �ߌ� 10:20:16)
 * @author: 
 */
public class DrSenmon1DescendPredicate implements com.objectspace.jgl.BinaryPredicate {
    /**
     * execute ���\�b�h�E�R�����g�B
     */
    public boolean execute(Object first, Object second) {
	// �\�[�g�����͍~��
	if (((TantoInfo)first).getSenmon1() == null)
	    ((TantoInfo)first).setSenmon1("");

	if (((TantoInfo)second).getSenmon1() == null)
	    ((TantoInfo)second).setSenmon1("");
	
	if (((TantoInfo)first).getSenmon1()
	    .compareTo(((TantoInfo)second).getSenmon1()) > 0) {
	    return true;
	} else {
	    return false;
	}
    }
}
