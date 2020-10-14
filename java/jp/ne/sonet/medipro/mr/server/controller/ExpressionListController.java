package jp.ne.sonet.medipro.mr.server.controller;

import java.util.Vector;
import java.sql.Connection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.ne.sonet.medipro.mr.common.util.DBConnect;
import jp.ne.sonet.medipro.mr.common.exception.DispatManager; 
import jp.ne.sonet.medipro.mr.common.exception.MrException;
import jp.ne.sonet.medipro.mr.server.manager.ExpressionLibInfoManager;
import jp.ne.sonet.medipro.mr.server.entity.ExpressionLibInfo;

/**
 * <strong>��^���ꗗController</strong>
 * <br>
 * @author: 
 * @version: 
 */
public class ExpressionListController {

    /**
     * ExpressionListController �I�u�W�F�N�g��V�K�ɍ쐬����B
     */
    public ExpressionListController() {
    }

    /**
     * ��^���ꗗ�����擾����B
     * @param request   javax.servlet.http.HttpServletRequest
     * @param response  javax.servlet.http.HttpServletResponse
     * @param companyCd ��ЃR�[�h
     * @return ��^���ꗗ���
     */
    public Vector getExpressionList(HttpServletRequest request,
				    HttpServletResponse response,
				    String companyCd) {
	DBConnect dbconn = new DBConnect();
	Connection conn = dbconn.getDBConnect();
	Vector list = new Vector();

	try {

	    ExpressionLibInfoManager manager = new ExpressionLibInfoManager(conn);
	    list = manager.getExpressionLibList(companyCd);
	} catch (MrException e) {
            new DispatManager().distribute(request, response);
        } finally {
	    dbconn.closeDB(conn);
	}

        return list;
    }

    /**
     * ��^�������擾����B
     * @param request      javax.servlet.http.HttpServletRequest
     * @param response     javax.servlet.http.HttpServletResponse
     * @param expressionCd ��^���R�[�h
     * @return ��^�����
     */
    public ExpressionLibInfo getExpressionInfo(HttpServletRequest request,
					       HttpServletResponse response,
					       String expressionCd) {
	DBConnect dbconn = new DBConnect();
	Connection conn = dbconn.getDBConnect();
	ExpressionLibInfo info = null;

	try {
	    ExpressionLibInfoManager manager = new ExpressionLibInfoManager(conn);
	    info = manager.getExpressionLibInfo(expressionCd);
	} catch (MrException e) {
            new DispatManager().distribute(request, response);
        } finally {
	    dbconn.closeDB(conn);
	}

        return info;
    }

}
