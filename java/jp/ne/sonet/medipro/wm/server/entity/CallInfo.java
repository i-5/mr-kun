package jp.ne.sonet.medipro.wm.server.entity;

import java.util.*;

/**
 * <strong>�R�[�����e���</strong>
 * <br>
 * @author
 * @version
 */
public class CallInfo {
    protected String callCD;			// �R�[�����e�R�[�h
    protected String callNaiyo;			// �R�[�����e

    /**
     * CallInfo �R���X�g���N�^
     */
    public CallInfo() {
    }

    /**
     * �R�[�����e�R�[�h�̎擾
     * @return �R�[�����e�R�[�h
     */
    public String getCallCD() {
        return callCD;
    }

    /**
     * �R�[�����e�̎擾
     * @return �R�[�����e
     */
    public String getCallNaiyo() {
        return callNaiyo;
    }

    /**
     * �R�[�����e�R�[�h�̃Z�b�g
     * @param newCallCD �R�[�����e�R�[�h
     */
    public void setCallCD(String newCallCD) {
        callCD = newCallCD;
    }

    /**
     * �R�[�����e�̃Z�b�g
     * @param newCallNaiyo �R�[�����e
     */
    public void setCallNaiyo(String newCallNaiyo) {
        callNaiyo = newCallNaiyo;
    }

    /**
     * �����񉻂���
     * @return ������
     */
    public String toString() {
        StringBuffer me = new StringBuffer();
        me.append(callCD + "\n");
        me.append(callNaiyo + "\n");

        return me.toString();
    }
}
