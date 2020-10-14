package jp.ne.sonet.medipro.mr.common.predicate;

import com.objectspace.jgl.*;
import jp.ne.sonet.medipro.mr.server.entity.*;
/**
 * ‚±‚±‚ÅŒ^‚Ì‹Lq‚ğ‘}“ü‚µ‚Ä‚­‚¾‚³‚¢B
 * ì¬“ú : (00/06/27 ŒßŒã 10:20:16)
 * @author: 
 */
public class DrTargetRankDescendPredicate implements com.objectspace.jgl.BinaryPredicate {
    /**
     * execute ƒƒ\ƒbƒhEƒRƒƒ“ƒgB
     */
     
 /*    
    public boolean execute(Object first, Object second) {
	// ƒ\[ƒgğŒ‚Í~‡
	if (((TantoInfo)first).getTargetRank() == null)
	    ((TantoInfo)first).setTargetRank("");

	if (((TantoInfo)second).getTargetRank() == null)
	    ((TantoInfo)second).setTargetRank("");
	
	if (((TantoInfo)first).getTargetRank()
	    .compareTo(((TantoInfo)second).getTargetRank()) > 0) {
	    return true;
	} else {
	    return false;
	}
    }
*/    

    public boolean execute(Object first, Object second) {
	// ƒ\[ƒgğŒ‚Í~‡
//System.out.println("ŒÚ‹qŠÇ—‚Í~‡");	
	if (((TantoInfo)first).getTargetName() == null)
	    ((TantoInfo)first).setTargetName("");

	if (((TantoInfo)second).getTargetName() == null)
	    ((TantoInfo)second).setTargetName("");
	
	if (((TantoInfo)first).getTargetName()
	    .compareTo(((TantoInfo)second).getTargetName()) > 0) {
	    return true;
	} else {
	    return false;
	}
    }
    
}
