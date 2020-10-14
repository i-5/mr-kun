package jp.ne.sonet.medipro.mr.common.predicate;

import com.objectspace.jgl.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
/**
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/27 �ߌ� 10:20:16)
 * @author: 
 */
public class ToNameDescendPredicate implements com.objectspace.jgl.BinaryPredicate {
    /**
     * execute ���\�b�h�E�R�����g�B
     */
    public boolean execute(Object first, Object second) {
	// �\�[�g�����͍~��
	if (((MsgInfo)first).getHeader().getToNameKana() == null)
	    ((MsgInfo)first).getHeader().setToNameKana("");
	
	if (((MsgInfo)second).getHeader().getToNameKana() == null)
	    ((MsgInfo)second).getHeader().setToNameKana("");
	
	if (((MsgInfo)first).getHeader().getToNameKana()
	    .compareTo(((MsgInfo)second).getHeader().getToNameKana()) > 0) {
	    return true;
	} else {
	    return false;
	}
    }
}
