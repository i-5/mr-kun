package jp.ne.sonet.medipro.mr.common.predicate;

import com.objectspace.jgl.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
/**
 * �����Ō^�̋L�q��}�����Ă��������B
 * �쐬�� : (00/06/27 �ߌ� 10:20:16)
 * @author: 
 */
public class DrNameDescendPredicate implements com.objectspace.jgl.BinaryPredicate {
    /**
     * execute ���\�b�h�E�R�����g�B
     */
//      public boolean execute(Object first, Object second) {
//  	// �\�[�g�����͍~��
//  	if (((TantoInfo)first).getDoctorinfo().getNameKana() == null)
//  	    ((TantoInfo)first).getDoctorinfo().setNameKana("");

//  	if (((TantoInfo)second).getDoctorinfo().getNameKana() == null)
//  	    ((TantoInfo)second).getDoctorinfo().setNameKana("");
	
//  	if (((TantoInfo)first).getDoctorinfo().getNameKana()
//  	    .compareTo(((TantoInfo)second).getDoctorinfo().getNameKana()) > 0 ) {
//  	    return true;
//  	} else {
//  	    return false;
//  	}
//      }
    public boolean execute(Object first, Object second) {
	// �\�[�g�����͍~��
	if (((TantoInfo)first).getName() == null)
	    ((TantoInfo)first).setName("");

	if (((TantoInfo)second).getName() == null)
	    ((TantoInfo)second).setName("");
	
	if (((TantoInfo)first).getName()
	    .compareTo(((TantoInfo)second).getName()) > 0 ) {
	    return true;
	} else {
	    return false;
	}
    }
}
