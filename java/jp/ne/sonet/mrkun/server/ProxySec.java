package jp.ne.sonet.mrkun.server;

import java.util.*;

/**
 * <h3>セキュリティリンク変換ユーティリティ</h3>
 * 
 * <br>
 * ここで型の記述を挿入してください。
 * 作成日 : (01/01/16 19:32:00)
 * @author: 
 */
 public final class ProxySec {
    protected static int PreTime = 30;	// n分前から
    protected static String MakerServer[] = {	// 参照先URL
//del 2001.11.02 M.Mizuki		"iyaku.kyowa.co.jp",
		"www.so-net.ne.jp/medipro/mm/shionogi",
		"www.so-net.ne.jp/medipro/mm/roche",
		"www.medical.nipponroche.co.jp/mr-kun/",
		"www.so-net.ne.jp/medipro/mm/pfizer"
					};
    protected static String MakerName[] = {	// 識別名
//del 2001.11.02 M.Mizuki		"kyowa",
		"shionogi",
		"roche",
		"mr-roche",
		"pfizer"
					};
    protected static int MakerMin[] = {	// 有効時間(分)
//del 2001.11.02 M.Mizuki		60,
		60,
		60,
		60,
		60
					};

// test
//    public static void main( String[] args ) {
//
//	System.out.println( getURL( "http://iyaku.kyowa.co.jp/test/index.html" ) );
//	System.out.println( getURL( "http://www.iyaku.kyowa.co.jp/test/index.html" ) );
//	System.out.println( getURL( "http://iyaku.kyowa.co.jp" ) );
//	System.out.println( getURL( "http://www.kyowa.co.jp/test/index.html" ) );
//
//    }
//

    /**
     * <h3>セキュリティリンク変換</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (01/01/16 20:00:00)
     * @return java.lang.String
     */
    public static String getURL(String url) {
        String ProxyServer = new String( ".medical.mymedipro.net" );
	String TimeKey = new String();
	int ii,idx,len;

	if( url == null ){
	    return url;
	}

	StringBuffer urlwork = new StringBuffer( url );
	for( ii=0; ii<MakerServer.length; ii++ ){

	    // 指定URLが含まれるかの確認
	    idx = url.indexOf( "://" + MakerServer[ii] );
	    if( idx != -1 ){

		// server名とdir名を分離位置
		len = MakerServer[ii].indexOf("/");
		if( len == -1 ){
		    len = MakerServer[ii].length();
		}

		// 有効期間キーを得る
		TimeKey = getTimeKey( MakerMin[ii] );

		// 書き換え
		urlwork.replace( idx+3, len+idx+3, 
			TimeKey + "." + MakerName[ii] + ProxyServer );
		break;
	    }
	}

	return urlwork.toString();
    }

    /**
     * <h3>有効期間を暗号化する</h3>
     * 
     * <br>
     * ここでメソッドの記述を挿入してください。
     * 作成日 : (01/01/16 20:00:00)
     * @return java.lang.String
     */
    protected static String getTimeKey( int makermin ){
	int chr_key_str[] = {	// XOR 変換キー
	//	1,2,3,4, 5,6,7,8, 9101112 13141516 17181920
		1,1,0,1, 0,1,0,0, 1,0,1,1, 0,1,1,1, 1,1,0,1,
		0,1,0,0, 0,0,1,0, 1,0,1,0, 1,1,1,1, 1,0,0,0,
		1,1,0,1, 1,1,0,1, 0,1,1,1, 0,0,1,0, 0,1,0,1,
		0,1,0,0, 0,1,1,1, 0,1,1,0 };
	String al[] = {	// 暗号化定数
		"x", "g", "k", "r", "o", "f", "t", "m"
		};
	String timekey = new String();
	int temp1[] = new int[72];
	int temp2[] = new int[72];
	int tmp;
	int ii,jj,kk;

	// 現在時刻からｎ分前の時刻を設定する
	Calendar datetime = Calendar.getInstance();
	datetime.add( Calendar.MINUTE, 0-PreTime );

//System.out.println( "datetime = " + datetime );

	// 年
	tmp = datetime.get( Calendar.YEAR );
	tmp %= 100;
	temp1[0] = tmp / 10;
	temp1[1] = tmp % 10;
	// 月
	tmp = datetime.get( Calendar.MONTH );
	tmp += 1;
	temp1[2] = tmp / 10;
	temp1[3] = tmp % 10;
	// 日
	tmp = datetime.get( Calendar.DATE );
	temp1[4] = tmp / 10;
	temp1[5] = tmp % 10;
	// 時
	tmp = datetime.get( Calendar.HOUR_OF_DAY );
	temp1[6] = tmp / 10;
	temp1[7] = tmp % 10;
	// 分
	tmp = datetime.get( Calendar.MINUTE );
	temp1[8] = tmp / 10;
	temp1[9] = tmp % 10;
	// 秒
	tmp = datetime.get( Calendar.SECOND );
	temp1[10] = tmp / 10;
	temp1[11] = tmp % 10;

	// 有効時間
	makermin += PreTime;
	if( makermin > 99999 ){
	    makermin = 99999;
	}
	temp1[12] = makermin / 10000;
	temp1[13] = makermin % 10000 / 1000;
	temp1[14] = makermin % 1000 / 100;
	temp1[15] = makermin % 100 / 10;
	temp1[16] = makermin % 10;


	// CDの計算およびBit化
	temp1[17] = 0;	// CD
	jj=0;
	for( ii=0; ii < temp1.length/4; ii++ ){
//System.out.println( "temp[" + ii + "] = " + temp1[ii] );
	    if( ii < temp1.length/4-1 ){
		temp1[17] += temp1[ii];	// CD
	    }else{
		temp1[17] %= 10;
	    }
	    for( kk=8; kk>0; kk>>=1, jj++ ){
		if( (temp1[ii] & kk) != 0 ){
			temp2[jj] = 1;
		}else{
			temp2[jj] = 0;
		}
//System.out.println( temp2[jj] );
	    }
	}
//System.out.println( "temp2 = " );
//System.out.println( temp2 );

	// 3Bitづつ前後に入れ替え
	for( ii=0; ii < temp2.length/3; ii++ ){
	    if( (ii%2) == 0 ){
		kk = ii / 2 * 3;	// 偶数のとき前から
	    }else{
		kk = temp2.length - (ii+1) / 2 * 3;	// 奇数のとき後ろから
	    }

	    for( jj=0; jj<3; jj++, kk++ ){
		temp1[kk] = temp2[ ii*3+jj ];
	    }
	}
//System.out.println( "temp1 = " );
//System.out.println( temp1 );

	// さらに2Bitづつ前後に入れ替え
	for( ii=0; ii < temp1.length/2; ii++ ){
	    if( (ii%2) == 0 ){
		kk = ii / 2 * 2;	// 偶数のとき前から
	    }else{
		kk = temp1.length - (ii+1) / 2 * 2;	// 奇数のとき後ろから
	    }

	    for( jj=0; jj<2; jj++, kk++ ){
		temp2[kk] = temp1[ ii*2+jj ];
	    }
	}
//System.out.println( "temp2 = " );
//System.out.println( temp2 );

	// XORをKeyと行う
	for( ii=0; ii < temp2.length; ii++ ){
	    temp2[ii] ^= chr_key_str[ii];
//System.out.println( ii + " = " + temp2[ii] );
	}

	// 英数字（暗号化定数）に置き換え
	for( ii=0; ii < temp2.length; ii+=3 ){
	    tmp = temp2[ii] << 2;
	    tmp += temp2[ii+1] << 1;
	    tmp += temp2[ii+2];
//System.out.println( tmp );
	    timekey += al[tmp];
	} 
//System.out.println( timekey );
	return timekey;
    }

}
