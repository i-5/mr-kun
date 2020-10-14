package jp.ne.sonet.medipro.wm.server.entity;

/**
 * <strong>選択登録テーブル情報クラス.</strong>
 * @author  doppe
 * @version 1.00
 */
public class SentakuTorokuInfo {
    ////////////////////////////////////////////////////////////////////////////////
    // instance variables
    //
    /** DR-ID */
    protected String drId = null;
    /** MR-ID */
    protected String mrId = null;
    /** シーケンス */
    protected int seq = 0;
    /** 医師氏名 */
    protected String name = null;
    /** 勤務先 */
    protected String kinmusaki = null;
    /** メーカー施設ID */
    protected String makerShisetsuId = null;
    /** DRテーブルにおける医師氏名 */
    protected String drName = null;

    ////////////////////////////////////////////////////////////////////////////////
    // instance methods
    //
    /**
     * DR-IDを取得する.
     * @return DR-ID
     */
    public String getDrId() {
        return drId;
    }

    /**
     * DR-IDを設定する.
     * @param drId 設定するDR-ID
     */
    public void setDrId(String drId) {
        this.drId = drId;
    }

    /**
     * MR-IDを取得する.
     * @return MR-ID
     */
    public String getMrId() {
        return mrId;
    }

    /**
     * MR-IDを設定する.
     * @param mrId 設定するMR-ID
     */
    public void setMrId(String mrId) {
        this.mrId = mrId;
    }

    /**
     * シーケンスを取得する.
     * @return シーケンス
     */
    public int getSeq() {
        return seq;
    }

    /**
     * シーケンスを設定する.
     * @param val シーケンス
     */
    public void setSeq(int val) {
        seq = val;
    }

    /**
     * 医師氏名を取得する.
     * @return 選択登録テーブルの医師氏名
     */
    public String getName() {
        return name;
    }

    /**
     * 医師氏名を設定する.
     * @param name 設定する医師氏名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 勤務先を取得する.
     * @return 勤務先
     */
    public String getKinmusaki() {
        return kinmusaki;
    }

    /**
     * 勤務先を設定する.
     * @param kinmusaki 設定する勤務先
     */
    public void setKinmusaki(String kinmusaki) {
        this.kinmusaki = kinmusaki;
    }

    /**
     * メーカー施設IDを取得する.
     * @return メーカー施設ID
     */
    public String getMakerShisetsuId() {
        return makerShisetsuId;
    }

    /**
     * メーカー施設IDを設定する.
     * @param makerShisetsuId 設定するメーカー施設ID
     */
    public void setMakerShisetsuId(String makerShisetsuId) {
        this.makerShisetsuId = makerShisetsuId;
    }

    /**
     * 医師氏名を取得する.
     * @return 医師テーブルの医師氏名
     */
    public String getDrName() {
        return drName;
    }

    /**
     * 医師氏名を設定する.
     * @param drName 設定する医師氏名
     */
    public void setDrName(String drName) {
        this.drName = drName;
    }

    /**
     * このオブジェクトの文字列表現を取得する.
     * @return パラメータの配列表現文字列
     */
    public String toString() {
        return "("
            + drId + ","
            + mrId + ","
            + seq + ","
            + name + ","
            + kinmusaki + ","
            + makerShisetsuId + ","
            + drName + ")";
    }

}
