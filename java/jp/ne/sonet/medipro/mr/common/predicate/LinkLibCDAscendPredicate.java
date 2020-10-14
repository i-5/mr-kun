package jp.ne.sonet.medipro.mr.common.predicate;

import com.objectspace.jgl.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
/**
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/27 �ߌ� 10:20:16)
 * @author: 
 */
public class LinkLibCDAscendPredicate implements com.objectspace.jgl.BinaryPredicate {
    /**
     * execute ���\�b�h�E�R�����g�B
     */
    public boolean execute(Object first, Object second) {
	// �\�[�g�����͏���
	if (((LinkLibInfo)first).getLinkCD() == null)
	    ((LinkLibInfo)first).setLinkCD("");

	if (((LinkLibInfo)second).getLinkCD() == null)
	    ((LinkLibInfo)second).setLinkCD("");
	
	if (((LinkLibInfo)first).getLinkCD()
	    .compareTo(((LinkLibInfo)second).getLinkCD()) < 0) {
	    return true;
	} else {
	    return false;
	}
    }
}
