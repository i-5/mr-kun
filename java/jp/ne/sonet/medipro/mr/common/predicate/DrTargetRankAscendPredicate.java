package jp.ne.sonet.medipro.mr.common.predicate;

import com.objectspace.jgl.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
/**
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/27 �ߌ� 10:20:16)
 * @author: 
 */
public class DrTargetRankAscendPredicate implements com.objectspace.jgl.BinaryPredicate {
    /**
 * execute ���\�b�h�E�R�����g�B
 */
 /*
    public boolean execute(Object first, Object second) {
	// �\�[�g�����͏���
	if (((TantoInfo)first).getTargetRank() == null)
	    ((TantoInfo)first).setTargetRank("");
	if (((TantoInfo)second).getTargetRank() == null)
	    ((TantoInfo)second).setTargetRank("");
	
	if (((TantoInfo)first).getTargetRank()
	    .compareTo(((TantoInfo)second).getTargetRank()) < 0) {
	    return true;
	} else {
	    return false;
	}
    }
    
 */   
    //�^�[�Q�b�g�����N�ł͂Ȃ��^�[�Q�b�g�����N���Ń\�[�g
     public boolean execute(Object first, Object second) {
	// �\�[�g�����͏���
//System.out.println("�ڋq�Ǘ��͏���");
	if (((TantoInfo)first).getTargetName() == null)
	    ((TantoInfo)first).setTargetName("");
	if (((TantoInfo)second).getTargetName() == null)
	    ((TantoInfo)second).setTargetName("");
	
	if (((TantoInfo)first).getTargetName()
	    .compareTo(((TantoInfo)second).getTargetName()) < 0) {
	    return true;
	} else {
	    return false;
	}
    }
    
    
}
