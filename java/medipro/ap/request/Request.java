package medipro.ap.request;

import javax.servlet.http.HttpServletRequest;

import medipro.ap.util.Logger;
import medipro.ap.util.Utility;
import medipro.ap.util.APException;
import medipro.ap.entity.Result;
import medipro.ap.entity.Response;

/**
 * ���ۗv���N���X
 */
public abstract class Request {
    /** �p�����[�^�� */
    protected String[] parameterNames;
    /** �p�����[�^�l */
    protected String[] parameterValues;
    /** �p�����[�^���� */
    protected int[] parameterSizes;
    /** �K�{ */
    protected boolean[] required;
    /** �G���[�t���O */
    public boolean error = false;

    /** �v������ */
    public abstract Result execute();
    /** �G���[�v�� */
    public abstract Response getError();

    /**
     * HTTP�v���̃f�[�^�^�C�v�����đ�������v���I�u�W�F�N�g�𐶐����܂��B
     * @throws Exception   �K��O�̃f�[�^���
     */
    public static Request makeRequest(HttpServletRequest request)
        throws Exception {
        String value = request.getParameter("VALUE");
        byte[] bytes = Utility.toBytes(value);

        Logger.log("�o�C�g��= " + bytes.length);

        Request ret;

        if (value.startsWith("1")) {
            ret = new SendRequest();
        } else if (value.startsWith("3") || value.startsWith("4")) {
            ret = new GetsRequest();
        } else if (value.startsWith("A") || value.startsWith("B")) {
            ret = new GetsRequest2();
        } else if (value.startsWith("E")) {
            ret = new CancelRequest();
        } else {
            throw new Exception("�ΏۊO�̃f�[�^���");
        }

        try {
            ret.parseInput(bytes);
        } catch (APException ex) {
            ret.error = true;
            Logger.log("���ڕs��", ex);
        }

        return ret;
    }

    public boolean isError() {
        return error;
    }

    /**
     * �e�p�����[�^�̎擾�ƌ����`�F�b�N
     * �o�C�g�񂩂�t�H�[�}�b�g�ɏ]���ăp�����[�^��؂�o���܂��B
     * @throws APException ���ڕs��
     */
    private void parseInput(byte[] bytes) throws APException {
        int begin = 0;//�؂�o���J�n�ʒu

        try {
            for (int i = 0; i < parameterSizes.length; i++) {
                byte[] param = new byte[parameterSizes[i]];
                Logger.log("index = " + begin + "-" + (begin + param.length));
                System.arraycopy(bytes, begin, param, 0, param.length);
                begin += parameterSizes[i];
                parameterValues[i] = Utility.toString(param).trim();
				//�K�{
                if (required[i] && Utility.isNull(parameterValues[i])) {
                    throw new Exception();
                }

                Logger.log(parameterNames[i] + " = \"" + parameterValues[i] + "\"");
            }
        } catch (Exception ex) {
            Logger.error("", ex);
            throw new APException("���ڕs��");
        }
    }

    /**
     * �w��index�̃p�����[�^�l���擾���܂��B
     * @param  index
     * @return �l(String)
     */
    public String getParameter(int index) {
        return parameterValues[index];
    }

    /**
     * �w��index�̃p�����[�^�l��ݒ肵�܂��B
     * @param index index
     * @param value �ݒ�l(String)
     */
    public void setParameter(int index, String value) {
        parameterValues[index] = value;
    }

}
