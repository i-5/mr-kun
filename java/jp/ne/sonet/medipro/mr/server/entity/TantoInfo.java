package jp.ne.sonet.medipro.mr.server.entity;

import java.util.Date;

/**
 * <h3>担当情報</h3>
 * 
 * <br>
 * ここで型の記述を挿入してください。
 * 作成日 : (00/06/21 午後 07:30:55)
 * @author: 
 */
public class TantoInfo {
    protected String drID;
    protected String name;
    protected int recvCount;
    protected int newOpenCount;
    protected int lastOpenDay;
    protected int sendCount;
    protected int sendNoReadDay;
    protected String kinmusaki;
    protected String makerCustID;
    protected String makerShisetsuID;
    protected String targetRank;
    protected String targetName;//1024 y-yamada add
    protected String syokusyu;
    protected String senmon1;
    protected String senmon2;
    protected String senmon3;
    protected String yakusyoku;
    protected String sotsugyoDaigaku;
    protected String sotsugyoYear;
    protected String syumi;
    protected String sonota;
    protected String mrID;
    protected String sentakuKbn;
    protected int DrRecvMsgNoCount;
    protected String drMemo;
    protected DoctorInfo doctorinfo;
    protected MrInfo mrinfo;
    protected Date end_ymd;

    //added by doppe
    protected int action;
    
/**
 * TantoInfo コンストラクター・コメント。
 */
public TantoInfo() {
}
/**
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/25 午後 10:09:07)
 * @return jp.ne.sonet.medipro.mr.server.entity.DoctorInfo
 */
public DoctorInfo getDoctorinfo() {
	return doctorinfo;
}
/**
 * <h3>医師ＩＤの取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/25 午後 02:51:48)
 * @return java.lang.String
 */
public java.lang.String getDrID() {
	return drID;
}
/**
 * <h3>医師メモの取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 07:56:32)
 * @return java.lang.String
 */
public java.lang.String getDrMemo() {
	return drMemo;
}
/**
 * <h3>医師受信ＭＳＧ未読数の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 07:56:32)
 * @return int
 */
public int getDrRecvMsgNoCount() {
	return DrRecvMsgNoCount;
}
/**
 * <h3>所属・勤務先の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 07:56:32)
 * @return java.lang.String
 */
public java.lang.String getKinmusaki() {
	return kinmusaki;
}
/**
 * <h3>前回開封からの日数の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 07:56:32)
 * @return int
 */
public int getLastOpenDay() {
	return lastOpenDay;
}
/**
 * <h3>前回開封からの日数の取得(String)</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (01/06/07 午後 08:50:32) Mizuki
 * @return String
 */
public String getLastOpenDayString() {
	if( lastOpenDay == -1 ){
		return "--";
	}else{
		return Integer.toString( lastOpenDay );
	}
}
/**
 * <h3>メーカー顧客ＩＤの取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 07:56:32)
 * @return java.lang.String
 */
public java.lang.String getMakerCustID() {
	return makerCustID;
}
/**
 * <h3>メーカー施設ＩＤの取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 07:56:32)
 * @return java.lang.String
 */
public java.lang.String getMakerShisetsuID() {
	return makerShisetsuID;
}
/**
 * <h3>ＭＲＩＤの取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/25 午後 02:51:28)
 * @return java.lang.String
 */
public java.lang.String getMrID() {
	return mrID;
}
/**
 * <h3>ＭＲ情報の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/27 午後 11:43:25)
 * @return jp.ne.sonet.medipro.mr.server.entity.MrInfo
 */
public MrInfo getMrinfo() {
	return mrinfo;
}
/**
 * <h3>医師氏名（ＭＲ管理）の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 07:56:32)
 * @return java.lang.String
 */
public java.lang.String getName() {
	return name;
}
/**
 * <h3>新しい開封知らせ数の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 07:56:32)
 * @return int
 */
public int getNewOpenCount() {
	return newOpenCount;
}
/**
 * <h3>未読受信ＭＳＧ数の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 07:56:32)
 * @return int
 */
public int getRecvCount() {
	return recvCount;
}
/**
 * <h3>未読送信ＭＳＧ数の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 07:56:32)
 * @return int
 */
public int getSendCount() {
	return sendCount;
}
/**
 * <h3>最新送信の未読日数の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 07:56:32)
 * @return int
 */
public int getSendNoReadDay() {
	return sendNoReadDay;
}
/**
 * <h3>専門領域１の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 07:56:32)
 * @return java.lang.String
 */
public java.lang.String getSenmon1() {
	return senmon1;
}
/**
 * <h3>専門領域２の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 07:56:32)
 * @return java.lang.String
 */
public java.lang.String getSenmon2() {
	return senmon2;
}
/**
 * <h3>専門領域３の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 07:56:32)
 * @return java.lang.String
 */
public java.lang.String getSenmon3() {
	return senmon3;
}
/**
 * <h3>選択区分の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 07:56:32)
 * @return java.lang.String
 */
public java.lang.String getSentakuKbn() {
	return sentakuKbn;
}
/**
 * <h3>その他の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 07:56:32)
 * @return java.lang.String
 */
public java.lang.String getSonota() {
	return sonota;
}
/**
 * <h3>出身大学の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 07:56:32)
 * @return java.lang.String
 */
public java.lang.String getSotsugyoDaigaku() {
	return sotsugyoDaigaku;
}
/**
 * <h3>卒業年の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 07:56:32)
 * @return java.lang.String
 */
public String getSotsugyoYear() {
	return sotsugyoYear;
}
/**
 * <h3>職種の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 07:56:32)
 * @return java.lang.String
 */
public java.lang.String getSyokusyu() {
	return syokusyu;
}
/**
 * <h3>趣味の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 07:56:32)
 * @return java.lang.String
 */
public java.lang.String getSyumi() {
	return syumi;
}
/**
 * <h3>ターゲットランクの取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 07:56:32)
 * @return java.lang.String
 */
public java.lang.String getTargetRank() {
	return targetRank;
}
/**
 * <h3>ターゲットランク名の取得</h3>
 * 1024 y-yamada add
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 07:56:32)
 * @return java.lang.String
 */
public java.lang.String getTargetName() {
	return targetName;
}
/**
 * <h3>役職の取得</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 07:56:32)
 * @return java.lang.String
 */
public java.lang.String getYakusyoku() {
	return yakusyoku;
}

    /**
     * 終了日の取得
     */
    public Date getEndYmd() {
	return end_ymd;
    }
/**
 * <h3>医師情報のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/25 午後 10:09:07)
 * @param newDoctorinfo jp.ne.sonet.medipro.mr.server.entity.DoctorInfo
 */
public void setDoctorinfo(DoctorInfo newDoctorinfo) {
	doctorinfo = newDoctorinfo;
}
/**
 * <h3>医師ＩＤのセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/25 午後 02:51:48)
 * @param newDrID java.lang.String
 */
public void setDrID(java.lang.String newDrID) {
	drID = newDrID;
}
/**
 * <h3>医師メモのセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 07:56:32)
 * @param newDrMemo java.lang.String
 */
public void setDrMemo(java.lang.String newDrMemo) {
	drMemo = newDrMemo;
}
/**
 * <h3>医師受信ＭＳＧ未読数のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 07:56:32)
 * @param newDrRecvMsgNoCount int
 */
public void setDrRecvMsgNoCount(int newDrRecvMsgNoCount) {
	DrRecvMsgNoCount = newDrRecvMsgNoCount;
}
/**
 * <h3>所属・勤務先のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 07:56:32)
 * @param newKinmusaki java.lang.String
 */
public void setKinmusaki(java.lang.String newKinmusaki) {
	kinmusaki = newKinmusaki;
}
/**
 * <h3>前回開封からの日数のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 07:56:32)
 * @param newLastOpenDay int
 */
public void setLastOpenDay(int newLastOpenDay) {
	lastOpenDay = newLastOpenDay;
}
/**
 * <h3>メーカー顧客ＩＤのセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 07:56:32)
 * @param newMakerCustID java.lang.String
 */
public void setMakerCustID(java.lang.String newMakerCustID) {
	makerCustID = newMakerCustID;
}
/**
 * <h3>メーカー施設ＩＤのセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 07:56:32)
 * @param newMakerShisetsuID java.lang.String
 */
public void setMakerShisetsuID(java.lang.String newMakerShisetsuID) {
	makerShisetsuID = newMakerShisetsuID;
}
/**
 * <h3>ＭＲＩＤのセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/25 午後 02:51:28)
 * @param newMrID java.lang.String
 */
public void setMrID(java.lang.String newMrID) {
	mrID = newMrID;
}
/**
 * <h3>ＭＲ情報のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/27 午後 11:43:25)
 * @param newMrinfo jp.ne.sonet.medipro.mr.server.entity.MrInfo
 */
public void setMrinfo(MrInfo newMrinfo) {
	mrinfo = newMrinfo;
}
/**
 * <h3>医師氏名（ＭＲ管理）のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 07:56:32)
 * @param newName java.lang.String
 */
public void setName(java.lang.String newName) {
	name = newName;
}
/**
 * <h3>新しい開封知らせ数のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 07:56:32)
 * @param newNewOpenCount int
 */
public void setNewOpenCount(int newNewOpenCount) {
	newOpenCount = newNewOpenCount;
}
/**
 * <h3>未読受信ＭＳＧ数のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 07:56:32)
 * @param newRecvCount int
 */
public void setRecvCount(int newRecvCount) {
	recvCount = newRecvCount;
}
/**
 * <h3>未読送信ＭＳＧ数のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 07:56:32)
 * @param newSendCount int
 */
public void setSendCount(int newSendCount) {
	sendCount = newSendCount;
}
/**
 * <h3>最新送信の未読日数のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 07:56:32)
 * @param newSendNoReadDay int
 */
public void setSendNoReadDay(int newSendNoReadDay) {
	sendNoReadDay = newSendNoReadDay;
}
/**
 * <h3>専門領域１のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 07:56:32)
 * @param newSenmon1 java.lang.String
 */
public void setSenmon1(java.lang.String newSenmon1) {
	senmon1 = newSenmon1;
}
/**
 * <h3>専門領域２のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 07:56:32)
 * @param newSenmon2 java.lang.String
 */
public void setSenmon2(java.lang.String newSenmon2) {
	senmon2 = newSenmon2;
}
/**
 * <h3>専門領域３のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 07:56:32)
 * @param newSenmon3 java.lang.String
 */
public void setSenmon3(java.lang.String newSenmon3) {
	senmon3 = newSenmon3;
}
/**
 * <h3>選択区分のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 07:56:32)
 * @param newSentakuKbn java.lang.String
 */
public void setSentakuKbn(java.lang.String newSentakuKbn) {
	sentakuKbn = newSentakuKbn;
}
/**
 * <h3>その他のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 07:56:32)
 * @param newSonota java.lang.String
 */
public void setSonota(java.lang.String newSonota) {
	sonota = newSonota;
}
/**
 * <h3>出身大学のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 07:56:32)
 * @param newSotsugyoDaigaku java.lang.String
 */
public void setSotsugyoDaigaku(java.lang.String newSotsugyoDaigaku) {
	sotsugyoDaigaku = newSotsugyoDaigaku;
}
/**
 * <h3>卒業年のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 07:56:32)
 * @param newSotsugyoYear java.lang.String
 */
public void setSotsugyoYear(String newSotsugyoYear) {
	sotsugyoYear = newSotsugyoYear;
}
/**
 * <h3>職種のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 07:56:32)
 * @param newSyokusyu java.lang.String
 */
public void setSyokusyu(java.lang.String newSyokusyu) {
	syokusyu = newSyokusyu;
}
/**
 * <h3>趣味のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 07:56:32)
 * @param newSyumi java.lang.String
 */
public void setSyumi(java.lang.String newSyumi) {
	syumi = newSyumi;
}
/**
 * <h3>ターゲットランクのセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 07:56:32)
 * @param newTargetRank java.lang.String
 */
public void setTargetRank(java.lang.String newTargetRank) {
	targetRank = newTargetRank;
}
/**
 * <h3>ターゲットランクのセット</h3>
 * 1024 y-yamada add
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 07:56:32)
 * @param newTargetRank java.lang.String
 */
public void setTargetName(java.lang.String newTargetName) {
	targetName = newTargetName;
}
/**
 * <h3>役職のセット</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/21 午後 07:56:32)
 * @param newYakusyoku java.lang.String
 */
public void setYakusyoku(java.lang.String newYakusyoku) {
	yakusyoku = newYakusyoku;
}
    /**
     * 終了日の取得
     */
    public void setEndYmd(Date date) {
	end_ymd = date;
    }


    public int getAction() {
	return action;
    }

    public void setAction(int action) {
	this.action = action;
    }

/**
 * <h3>文字列化する</h3>
 * 
 * <br>
 * ここでメソッドの記述を挿入してください。
 * 作成日 : (00/06/19 23:51:35)
 * @return java.lang.String
 */
public String toString() {
	StringBuffer me = new StringBuffer();
	me.append(drID + "\n");
	me.append(mrID + "\n");
	me.append(drMemo + "\n");
	me.append(targetRank + "\n");
	me.append(targetName + "\n");//1024 y-yamada add
	me.append(kinmusaki + "\n");
	me.append(recvCount + "\n");
	me.append(newOpenCount + "\n");
	me.append(lastOpenDay + "\n");
	me.append(sendCount + "\n");
	me.append(sendNoReadDay + "\n");
	me.append(syokusyu + "\n");
	me.append(senmon1 + "\n");
	me.append(senmon2 + "\n");
	me.append(senmon3 + "\n");
	me.append(name + "\n");
	me.append(makerCustID + "\n");
	me.append(makerShisetsuID + "\n");
	me.append(yakusyoku + "\n");
	me.append(sotsugyoDaigaku + "\n");
	me.append(sotsugyoYear + "\n");
	me.append(syumi + "\n");
	me.append(sonota + "\n");
		
	me.append(doctorinfo + "\n");
	me.append(mrinfo + "\n");
	
	return me.toString();
}
}
