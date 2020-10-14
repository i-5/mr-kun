package medipro.ap.entity;

import java.io.PrintWriter;
import java.io.IOException;
import java.io.OutputStream;

/**
 * ���ۉ����N���X
 */
public abstract class Response {
    /** ����I�� or �f�[�^���� */
    public final static String NORMAL = "00";
    /** ���ڕs�� */
    public final static String LACK = "01";
    /** ���o�^MR */
    public final static String NO_MR = "02";
    /** ���o�^DR */
    public final static String NO_DR = "03";
    /** ���o�^�摜 */
    public final static String NO_PICTURE = "04";
    /** �s�ǗL�������� */
    public final static String ILLEGAL_APPLIED_DATE = "05";
    /** ���o�^��� */
    public final static String NO_COMPANY = "06";
    /** ���̑��s�� */
    public final static String UNKNOWN_ERROR = "99";
    /** �p�����[�^���z�� */
    protected String[] parameterNames;
    /** �p�����[�^�l�z�� */
    protected String[] parameterValues;
    /** �p�����[�^�����z�� */
    protected int[] parameterSizes;

    /**
     * �X�g���[���Ɏ����̕ێ�������e����������
     */
    public void write(OutputStream os) throws IOException {
        for (int i = 0; i < parameterSizes.length; i++) {
            byte[] val = null;
			
            if (parameterValues[i] == null) {
                val = new byte[0];
            } else {
                val = parameterValues[i].getBytes("SJIS");
            }
            byte[] out = new byte[parameterSizes[i]];

            if (val.length < parameterSizes[i]) {
                System.arraycopy(val, 0, out, 0, val.length);
            } else {
                System.arraycopy(val, 0, out, 0, out.length);
            }
            os.write(out);
        }
    }

    public void write(PrintWriter pw) throws IOException {
        for (int i = 0; i < parameterSizes.length; i++) {
            if (i != 0) {
                pw.print(",'");
            } else {
                pw.print("'");
            }
            if (parameterValues[i] != null) {
                pw.print(parameterValues[i].replace('\n',' ').replace('\r',' '));
            }
        }
        pw.println();
    }

}
