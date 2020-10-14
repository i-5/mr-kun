package jp.ne.sonet.medipro.wm.server.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import jp.ne.sonet.medipro.wm.common.exception.WmException;

/**
 * <strong>営業所情報クラス.</strong>
 * @author  doppe
 * @version 1.00
 */
public class EigyosyoInfoManager {
    /** 営業所リスト取得 */
    protected static final String EIGYOSYO_LIST_SQL
        = "SELECT"
        + " eigyosyo_cd"
        + ",eigyosyo_name"
        + " FROM eigyosyo"
        + " WHERE"
        + " company_cd = ?"
        + " AND shiten_cd = ?"
	+ " AND delete_ymd IS NULL"
	+ " AND eigyosyo_name IS NOT NULL";

    /** コネクションオブジェクト */
    protected Connection connection = null;

    /**
     * このManagerが用いるDBへのコネクションを設定する.
     * @param initConnection 設定するコネクション
     */
    public EigyosyoInfoManager(Connection initConnection) {
        this.connection = initConnection;
    }

    /**
     * 会社コード、支店コードで絞り込んだ営業所リストを取得する.
     * @param companyCd  会社コード
     * @param eigyosyoCd 営業所コード
     * @param (コード、名称)のリスト
     */
    public Vector getEigyosyoList(String companyCd, String shitenCd) {
        Vector list = new Vector();
        //空白の追加
        list.addElement(new String[]{"", ""});

        try {
            PreparedStatement pstmt = connection.prepareStatement(EIGYOSYO_LIST_SQL);
            pstmt.setString(1, companyCd);
            pstmt.setString(2, shitenCd);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String[] pair = {rs.getString("eigyosyo_cd"),
                                 rs.getString("eigyosyo_name")};
                list.addElement(pair);
            }
      
            pstmt.close();
        } catch (SQLException ex) {
            throw new WmException(ex);
        }

        return list;
    }
}
