package jp.ne.sonet.medipro.mr.common.predicate;

import com.objectspace.jgl.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
/**
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/27 �ߌ� 10:20:16)
 * @author: 
 */
public class FromNameDescendPredicate implements com.objectspace.jgl.BinaryPredicate {
    /**
     * execute ���\�b�h�E�R�����g�B
     */
    public boolean execute(Object first, Object second) {
	// �\�[�g�����͍~��
	if (((MsgInfo)first).getHeader().getFromNameKana() == null)
	    ((MsgInfo)first).getHeader().setFromNameKana("");

	if (((MsgInfo)second).getHeader().getFromNameKana() == null)
	    ((MsgInfo)second).getHeader().setFromNameKana("");
	
	if (((MsgInfo)first).getHeader().getFromNameKana()
	    .compareTo(((MsgInfo)second).getHeader().getFromNameKana()) > 0) {
	    return true;
	} else {
	    return false;
	}
    }
}