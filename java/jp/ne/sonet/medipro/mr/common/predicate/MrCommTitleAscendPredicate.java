package jp.ne.sonet.medipro.mr.common.predicate;

import com.objectspace.jgl.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
/**
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/27 �ߌ� 10:20:16)
 * @author: 
 */
public class MrCommTitleAscendPredicate implements com.objectspace.jgl.BinaryPredicate {
    /**
     * execute ���\�b�h�E�R�����g�B
     */
    public boolean execute(Object first, Object second) {
	// �\�[�g�����͏���
	if (((MsgInfo)first).getBody().getTitle() == null)
	    ((MsgInfo)first).getBody().setTitle("");

	if (((MsgInfo)second).getBody().getTitle() == null)
	    ((MsgInfo)second).getBody().setTitle("");
	
	if (((MsgInfo)first).getBody().getTitle()
	    .compareTo(((MsgInfo)second).getBody().getTitle()) < 0) {
	    return true;
	} else {
	    return false;
	}
    }
}
