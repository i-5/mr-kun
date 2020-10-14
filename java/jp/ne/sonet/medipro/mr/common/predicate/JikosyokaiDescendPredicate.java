package jp.ne.sonet.medipro.mr.common.predicate;

import com.objectspace.jgl.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
/**
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/27 �ߌ� 10:20:16)
 * @author: 
 */
public class JikosyokaiDescendPredicate implements com.objectspace.jgl.BinaryPredicate {
    /**
     * execute ���\�b�h�E�R�����g�B
     */
    public boolean execute(Object first, Object second) {
	// �\�[�g�����͍~��
	if (((MsgInfo)first).getBody().getJikosyokai() == null)
	    ((MsgInfo)first).getBody().setJikosyokai("");
	
	if (((MsgInfo)second).getBody().getJikosyokai() == null)
	    ((MsgInfo)second).getBody().setJikosyokai("");
	
	if (((MsgInfo)first).getBody().getJikosyokai()
	    .compareTo(((MsgInfo)second).getBody().getJikosyokai()) > 0) {
	    return true;
	} else {
	    return false;
	}
    }
}
