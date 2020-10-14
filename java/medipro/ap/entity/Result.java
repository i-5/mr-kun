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
 * 要求処理結果格納クラス
 */
public class Result {
    /** 結果一覧 */
    private Vector results;

    /**
     * Creates a new <code>Result</code> instance.
     *
     */
    public Result() {
        results = new Vector();
    }

    /**
     * 結果を追加
     * @param  response
     */
    public void addResponse(Response response) {
        results.add(response);
    }

    /**
     * 結果一覧をストリームに書き出す
     * @param  os
     * @exception IOException
     */
    public void writeResponse(OutputStream os) throws IOException {
        Logger.log("返す応答数 = " + results.size());

        Iterator i = results.iterator();
        while (i.hasNext()) {
            ((Response)i.next()).write(os);
        }

        if (Boolean.getBoolean("medipro.ap.debug")) {
            writeResponse();
        }
    }

    /**
     * 結果一覧をストリームに書き出す
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
