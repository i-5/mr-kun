package jp.ne.sonet.medipro.mr.common.predicate;

import com.objectspace.jgl.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
/**
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/27 �ߌ� 10:20:16)
 * @author: 
 */

    /**
     * execute ���\�b�h�E�R�����g�B
     */

public class TargetRankDescendPredicate implements com.objectspace.jgl.BinaryPredicate {
/*     
    public boolean execute(Object first, Object second) {
	// �\�[�g�����͍~��
	if (((MsgInfo)first).getHeader().getTargetRank() == null)
	    ((MsgInfo)first).getHeader().setTargetRank("");

	if (((MsgInfo)second).getHeader().getTargetRank() == null)
	    ((MsgInfo)second).getHeader().setTargetRank("");
	
	if (((MsgInfo)first).getHeader().getTargetRank()
	    .compareTo(((MsgInfo)second).getHeader().getTargetRank()) > 0) {
	    return true;
	} else {
	    return false;
	}
    }
*/    
    
    public boolean execute(Object first, Object second) {
	// �\�[�g�����͍~��
//System.out.println("���M�ꗗ�ō~��");
	if (((MsgInfo)first).getHeader().getTargetName() == null)
	    ((MsgInfo)first).getHeader().setTargetName("");

	if (((MsgInfo)second).getHeader().getTargetName() == null)
	    ((MsgInfo)second).getHeader().setTargetName("");
	
	if (((MsgInfo)first).getHeader().getTargetName()
	    .compareTo(((MsgInfo)second).getHeader().getTargetName()) > 0) {
	    return true;
	} else {
	    return false;
	}
    }
    
    
}
