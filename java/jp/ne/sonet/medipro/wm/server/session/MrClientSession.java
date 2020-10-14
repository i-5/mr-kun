package jp.ne.sonet.medipro.wm.server.session;

import java.util.Vector;
import java.util.Enumeration;
import java.util.Hashtable;

import jp.ne.sonet.medipro.wm.common.SysCnst;
import jp.ne.sonet.medipro.wm.server.entity.MrInfo;
import jp.ne.sonet.medipro.wm.server.entity.SentakuTorokuInfo;

/**
 * <strong>MR管理担当顧客変更画面用セッションクラス.</strong>
 * @author  doppe
 * @version 1.00
 */
public class MrClientSession {
    ////////////////////////////////////////////////////////////////////////////////
    // constants
    /** 正常 */
    public static final int NORMAL = 0;
    /** 保存確認 */
    public static final int SAVE_CONFIRM = 1;
    /** 保存完了 */
    public static final int SAVE_DONE = 2;
    /** MRID非存在エラー */
    public static final int NO_MR_EXIST_ERROR = 3;
    /** 同一MRIDエラー */
    public static final int SAME_MRID_ERROR = 4;
    /** 権限エラー */
    public static final int AUTHORITY_ERROR = 5;

    ////////////////////////////////////////////////////////////////////////////////
    // instance variables
    /** 左側MR情報 */
    private Mr leftMr = null;
    /** 右側MR情報 */
    private Mr rightMr = null;
    /** 左側更新フラグ */
    private boolean leftLoadFlag = false;
    /** 右側更新フラグ */
    private boolean rightLoadFlag = false;
    /** 現在のstatus */
    private int status = NORMAL;
    
    ////////////////////////////////////////////////////////////////////////////////
    // constructors
    public MrClientSession() {
        leftMr = new Mr();
        rightMr = new Mr();

        if (SysCnst.DEBUG) {
            System.err.println("MrClientSession is created");
        }
    }

    ////////////////////////////////////////////////////////////////////////////////
    // instance methods
    /**
     * 現在のstatusを取得する。
     * @return statusコードのどれか
     */
    public int getStatus() {
        return status;
    }

    /**
     * 現在のstatusを設定する。
     * @param status statusコードのどれか
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * 左側のMR情報を取得する。
     * @return Mrオブジェクト
     */
    public Mr getLeftMr() {
        return leftMr;
    }

    /**
     * 右側のMR情報を取得する。
     * @return Mrオブジェクト
     */
    public Mr getRightMr() {
        return rightMr;
    }

    /**
     * 左側のMR情報とDRリストを設定する。
     * @param info   MR情報
     * @param drList DRリスト
     */
    public void setLeftMrInfo(MrInfo info, Vector drList) {
        leftMr.setMrId(info.getMrId());
        leftMr.setName(info.getName());
        leftMr.setShitenName(info.getShitenName());
        leftMr.setEigyosyoName(info.getEigyosyoName());
        leftMr.setAttributeName1(info.getMrAttributeName1());
        leftMr.setAttributeName2(info.getMrAttributeName2());
        leftMr.setDrList(drList);
        leftLoadFlag = false;
    }

    /**
     * 右側のMR情報とDRリストを設定する。
     * @param info   MR情報
     * @param drList DRリスト
     */
    public void setRightMrInfo(MrInfo info, Vector drList) {
        rightMr.setMrId(info.getMrId());
        rightMr.setName(info.getName());
        rightMr.setShitenName(info.getShitenName());
        rightMr.setEigyosyoName(info.getEigyosyoName());
        rightMr.setAttributeName1(info.getMrAttributeName1());
        rightMr.setAttributeName2(info.getMrAttributeName2());
        rightMr.setDrList(drList);
        rightLoadFlag = false;
    }

    /**
     * 左側のMR-IDを取得する。
     * @return MR-ID
     */
    public String getLeftMrId() {
        return leftMr.getMrId();
    }

    /**
     * 右側のMR-IDを取得する。
     * @return MR-ID
     */
    public String getRightMrId() {
        return rightMr.getMrId();
    }

    /**
     * 左側のMR-IDを設定する。
     * @param mrID 設定するMR-ID
     */
    public void setLeftMrId(String mrId) {
        if (mrId == null || mrId.equals("")) {
            leftMr = new Mr();
        } else {
            leftMr.setMrId(mrId);
            leftLoadFlag = true;
        }
    }

    /**
     * 右側のMR-IDを設定する。
     * @param mrID 設定するMR-ID
     */
    public void setRightMrId(String mrId) {
        if (mrId == null || mrId.equals("")) {
            rightMr = new Mr();
        } else {
            rightMr.setMrId(mrId);
            rightLoadFlag = true;
        }
    }

    /**
     * 左側の指定したKeyのDRを右に移す。
     * @param key 移動するDRのKey
     */
    public void moveToRight(String key) {
        if (getLeftMrId().equals("") || getRightMrId().equals("")) {
            return;
        }
        rightMr.addDr(leftMr.getDr(key));
        leftMr.remove(key);
    }

    /**
     * 右側の指定したKeyのDRを左に移す。
     * @param key 移動するDRのKey
     */
    public void moveToLeft(String key) {
        if (getLeftMrId().equals("") || getRightMrId().equals("")) {
            return;
        }
        leftMr.addDr(rightMr.getDr(key));
        rightMr.remove(key);
    }

    /**
     * 左側の情報をDBから読み込む必要があることを示すフラグ。
     * @return 必要だったらtrue
     */
    public boolean isNeedToLeftMrLoad() {
        return leftLoadFlag;
    }

    /**
     * 右側の情報をDBから読み込む必要があることを示すフラグ。
     * @return 必要だったらtrue
     */
    public boolean isNeedToRightMrLoad() {
        return rightLoadFlag;
    }

    /**
     * <strong>簡易MR情報クラス.</strong>
     * MrInfoのラッピングとDRリストの保持機能を持つ.
     */
    public class Mr {
        /** MRID*/
        private String mrId = new String();
        /** 氏名 */
        private String name = new String();
        /** 支店名称 */
        private String shitenName = new String();
        /** 営業所名称 */
        private String eigyosyoName = new String();
        /** MR属性1 */
        private String attributeName1 = new String();
        /** MR属性2 */
        private String attributeName2 = new String();
        /** 医師氏名 */
        private Hashtable drList = null;

        /**
         * 空のDRリストを生成する.
         */
        public Mr() {
            drList = new Hashtable();
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
         * @param val 設定するMR-ID
         */
        public void setMrId(String val) {
            mrId = val;
        }

        /**
         * MR氏名を取得する.
         * @return 氏名
         */
        public String getName() {
            return name;
        }

        /**
         * MR氏名を設定する.
         * @param 設定するMR氏名
         */
        public void setName(String val) {
            if (val == null) {
                name = "";
            } else {
                name = val;
            }
        }
        
        /**
         * MRが属する支店名を取得する.
         * @return 支店名
         */
        public String getShitenName() {
            return shitenName;
        }

        /**
         * MRが属する支店名を設定する.
         * @param val 設定する支店名
         */
        public void setShitenName(String val) {
            if (val == null) {
                shitenName = "";
            } else {
                shitenName = val;
            }
        }

        /**
         * MRが属する営業所名を取得する.
         * @return 営業所名
         */
        public String getEigyosyoName() {
            return eigyosyoName;
        }

        /**
         * MRが属する営業所名を設定する.
         * @param val 設定する営業所名
         */
        public void setEigyosyoName(String val) {
            if (val == null) {
                eigyosyoName = "";
            } else {
                eigyosyoName = val;
            }
        }
        
        /**
         * MRが属する属性名1を取得する.
         * @return 属性名1
         */
        public String getAttributeName1() {
            return attributeName1;
        }

        /**
         * MRが属する属性名1を設定する.
         * @param val 設定する属性名1
         */
        public void setAttributeName1(String val) {
            if (val == null) {
                attributeName1 = "";
            } else {
                attributeName1 = val;
            }
        }
        
        /**
         * MRが属する属性名2を取得する.
         * @return 属性名2
         */
        public String getAttributeName2() {
            return attributeName2;
        }

        /**
         * MRが属する属性名2を設定する.
         * @param val 設定する属性名2
         */
        public void setAttributeName2(String val) {
            if (val == null) {
                attributeName2 = "";
            } else {
                attributeName2 = val;
            }
        }

        /**
         * MRが担当するDR一覧を取得する.
         * @return Drインスタンスの一覧
         */
        public Enumeration getDrList() {
            return drList.elements();
        }

        /**
         * 現在保持するDR一覧から新たに担当になったDR-ID一覧を取得する.
         * @return DR-IDリスト
         */
        public Enumeration getMovedDrList() {
            Enumeration e = getDrList();
            Vector list = new Vector();

            while (e.hasMoreElements()) {
                Dr dr = (Dr)e.nextElement();
                if (!getMrId().equals(dr.getMrId())) {
                    list.addElement(dr);
                }
            }

            return list.elements();
        }

        /**
         * DRを一人追加します.
         * @param dr 追加するDR情報
         */
        public void addDr(Dr dr) {
            drList.put(dr.getKey(), dr);
        }

        /**
         * DR一覧を設定します(既存のDRは破棄).
         * @param list 設定するDR情報のリスト
         */
        public void setDrList(Vector list) {
            drList.clear();
            
            if (list == null) {
                return;
            }

            Enumeration e = list.elements();
            while (e.hasMoreElements()) {
                SentakuTorokuInfo info = (SentakuTorokuInfo)e.nextElement();
                drList.put(info.getDrId() + info.getSeq(), new Dr(info));
            }
        }

        /**
         * 指定したIDのDR情報を取得します.
         * @param drId 取得するDrのDR-ID
         * @return DR情報
         */
        public Dr getDr(String key) {
            return (Dr)drList.get(key);
        }

        /**
         * 指定したIDのDRを一覧から削除します.
         * @param drId 削除するDrのDR-ID
         */
        public void remove(String key) {
            drList.remove(key);
        }

    }

    /**
     * <strong>SentakuTorokuInfoのラッパクラス.</strong>
     */
    public class Dr {
        /** DR名の表示文字列長 */
        static final int NAME_LENGTH = 14;
        /** 勤務先の表示文字列長 */
        static final int KINMUSAKI_LENGTH = 20;
        /** メーカー施設IDの表示文字列長 */
        static final int MAKER_ID_LENGTH = 10;
        /** 選択登録情報 */
        private SentakuTorokuInfo info = null;

        /**
         * 簡易DR情報を生成します.
         * @param info DR情報
         */
        public Dr(SentakuTorokuInfo info) {
            this.info = info;
        }

        /**
         * このDRを一意に判別するためのKeyを取得します.
         * @return Keyとなる文字列
         */
        public String getKey() {
            return info.getDrId() + info.getSeq();
        }

        /**
         * DR-IDを取得します.
         * @return MR-ID
         */
        public String getDrId() {
            return info.getDrId();
        }

        /**
         * このDRを担当するMRのIDを取得します.
         * @return MR-ID
         */
        public String getMrId() {
            return info.getMrId();
        }

        /**
         * シーケンスを取得します.
         * @return シーケンス
         */
        public int getSeq() {
            return info.getSeq();
        }

        /**
         * 医師氏名を取得します.
         * 選択登録テーブルに設定されていなければ、医師テーブルから取得します.
         * @return 医師氏名
         */
        public String getName() {
            String name = info.getName() == null ? info.getDrName() : info.getName();

            return format(name, NAME_LENGTH);
        }

        /**
         * 勤務先を取得します.
         * @return 勤務先
         */
        public String getKinmusaki() {
            return format(info.getKinmusaki(), KINMUSAKI_LENGTH);
        }

        /**
         * メーカー施設IDを取得します.
         * @return メーカー施設ID
         */
        public String getMakerId() {
            return format(info.getMakerShisetsuId(), MAKER_ID_LENGTH);
        }

        /**
         * 指定した長さにフォーマットした文字列を取得する.
         * @param str           フォーマット対象
         * @param displayLength フォーマットする長さ
         */
        private String format(String str, int displayLength) {
            int length = str == null ? 0 : str.getBytes().length;

            if (length < displayLength) {
                StringBuffer buff = new StringBuffer();
                if (str != null) {
                    buff.append(str);
                }

                for (int i = 0; i < displayLength - length; i += 2) {
                    buff.append("　");
                }
                return buff.toString();
            }

            return new String(str.getBytes(), 0, displayLength);
        }

        /**
         * このオブジェクトの文字列表現を取得します.
         * @return 文字列表現
         */
        public String toString() {
            return getName() + " " + getKinmusaki() + " "+ getMakerId();
        }

    }

}
