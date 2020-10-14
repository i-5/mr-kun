package medipro.ap.entity;

import java.io.OutputStream;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.util.Iterator;
import java.io.IOException;
import medipro.ap.util.Logger;
import java.util.Vector;

/**
 * �v���������ʊi�[�N���X
 */
public class Result {
    /** ���ʈꗗ */
    private Vector results;

    /**
     * Creates a new <code>Result</code> instance.
     *
     */
    public Result() {
        results = new Vector();
    }

    /**
     * ���ʂ�ǉ�
     * @param  response
     */
    public void addResponse(Response response) {
        results.add(response);
    }

    /**
     * ���ʈꗗ���X�g���[���ɏ����o��
     * @param  os
     * @exception IOException
     */
    public void writeResponse(OutputStream os) throws IOException {
        Logger.log("�Ԃ������� = " + results.size());

        Iterator i = results.iterator();
        while (i.hasNext()) {
            ((Response)i.next()).write(os);
        }

        if (Boolean.getBoolean("medipro.ap.debug")) {
            writeResponse();
        }
    }

    /**
     * ���ʈꗗ���X�g���[���ɏ����o��
     * @exception IOException
     */
    public void writeResponse() throws IOException {
        FileWriter fw = new FileWriter("d:/workplace/mr/hoehoe.csv");
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter pw = new PrintWriter(bw);

        Iterator i = results.iterator();
        while (i.hasNext()) {
            ((Response)i.next()).write(pw);
        }

        pw.flush();
        fw.close();
    }

}
