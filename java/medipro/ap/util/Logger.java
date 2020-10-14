package medipro.ap.util;

import javax.servlet.*;

/**
 * ログ出力
 */
public class Logger {
    /** DEBUGモード */
    public static final int DEBUG = 0;
    /** INFOモード */
    public static final int INFO = 1;

    /** ログ出力モード(Default = DEBUG) */
    private static int mode = DEBUG;
    /** */
    private static ServletContext context;

    /**
     * ログ出力モードを設定する
     */
    public static void setMode(int initMode) {
        mode = initMode;
    }

    /**
     * 
     */
    public static void setContext(ServletContext initContext) {
        context = initContext;
    }

    /**
     * ログを出力する。(DEBUG時のみ)
     */
    public static void log(String str) {
        if (mode == DEBUG) {
            if (context != null) {
                context.log("[AP] " + str);
            } else {
                System.err.println("[AP] " + str);
            }
        }
    }

    /**
     * エラー情報と例外のスタックトレースを出力する。(DEBUG時のみ)
     */
    public static void log(String str, Throwable e) {
        if (mode == DEBUG) {
            if (context != null) {
                context.log("[AP] " + str, e);
            } else {
                System.err.println("[AP] " + str);
                e.printStackTrace();
            }
        }
    }

    /**
     * エラー情報を出力する。
     */
    public static void error(String str) {
        if (context != null) {
            context.log("[AP] " + str);
        } else {
            System.err.println("[AP] " + str);
        }
    }

    /**
     * エラー情報と例外のスタックトレースを出力する。
     */
    public static void error(String str, Throwable e) {
        if (context != null) {
            context.log("[AP] " + str, e);
        } else {
            System.err.println("[AP] " + str);
            e.printStackTrace();
        }
    }
}
