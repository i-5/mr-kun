package medipro.ap.util;

import javax.servlet.*;

/**
 * ���O�o��
 */
public class Logger {
    /** DEBUG���[�h */
    public static final int DEBUG = 0;
    /** INFO���[�h */
    public static final int INFO = 1;

    /** ���O�o�̓��[�h(Default = DEBUG) */
    private static int mode = DEBUG;
    /** */
    private static ServletContext context;

    /**
     * ���O�o�̓��[�h��ݒ肷��
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
     * ���O���o�͂���B(DEBUG���̂�)
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
     * �G���[���Ɨ�O�̃X�^�b�N�g���[�X���o�͂���B(DEBUG���̂�)
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
     * �G���[�����o�͂���B
     */
    public static void error(String str) {
        if (context != null) {
            context.log("[AP] " + str);
        } else {
            System.err.println("[AP] " + str);
        }
    }

    /**
     * �G���[���Ɨ�O�̃X�^�b�N�g���[�X���o�͂���B
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
