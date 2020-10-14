package jp.ne.sonet.medipro.mr.common.predicate;

import com.objectspace.jgl.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
/**
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/27 �ߌ� 10:20:16)
 * @author: 
 */
public class DrSyokusyuDescendPredicate implements com.objectspace.jgl.BinaryPredicate {
    /**
     * execute ���\�b�h�E�R�����g�B
     */
    public boolean execute(Object first, Object second) {
	// �\�[�g�����͍~��
	if (((TantoInfo)first).getSyokusyu() == null)
	    ((TantoInfo)first).setSyokusyu("");

	if (((TantoInfo)second).getSyokusyu() == null)
	    ((TantoInfo)second).setSyokusyu("");
	
	if (((TantoInfo)first).getSyokusyu()
	    .compareTo(((TantoInfo)second).getSyokusyu()) > 0) {
	    return true;
	} else {
	    return false;
	}
    }
}
