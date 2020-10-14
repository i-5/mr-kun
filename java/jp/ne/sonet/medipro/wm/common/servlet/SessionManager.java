package jp.ne.sonet.medipro.wm.common.servlet;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import jp.ne.sonet.medipro.wm.common.*;
import jp.ne.sonet.medipro.wm.common.exception.*;
import jp.ne.sonet.medipro.wm.server.session.*;


/**
 * <strong>�Z�b�V�����Ǘ�</strong>
 * <br>
 * @author: 
 * @version: 
 */
public class SessionManager {

    /**
     * SessionManager �I�u�W�F�N�g��V�K�ɍ쐬����B
     */
    public SessionManager(){}

    /**
     * ���O�C�����A�������`�F�b�N����B
     * @param req javax.servlet.http.HttpServletRequest
     * @return ����^�ُ�
     */
    public static boolean check(HttpServletRequest request) {
        boolean status = false;

        try {
            HttpSession session = request.getSession(true);
            // �Z�b�V�����f�[�^�����邩�ǂ����m�F
            if ( session.isNew() != true ) {
                String comKey = SysCnst.KEY_COMMON_SESSION;
                Common comSes = (Common)session.getValue(comKey);
                if ( comSes != null ) {
                    status = true;
                }
            }
        } catch (Exception e) {
//              throw new WmException(e);
        }

        return status;
    }
    
    /**
     * �Z�b�V�����������Z�b�g�i���O�C�����ȊO���폜�j����B
     * @param req javax.servlet.http.HttpServletRequest
     */
    public static void reset(HttpServletRequest request) {   
        try {
            HttpSession session = request.getSession(true);
            String comKey = SysCnst.KEY_COMMON_SESSION;
            Common comSes = (Common)session.getValue(comKey);
            String[] sessionNames = session.getValueNames();
            if (sessionNames != null) {
                for ( int i = 0; i < sessionNames.length; i++ ) {
                    if ( sessionNames[i].startsWith(comKey) != true ) {
                        session.removeValue(sessionNames[i]);
                    }
                }
            }
        } catch (Exception e) {
            throw new WmException(e);
        }
    }

}
