package org.zxp.extfile.util;

public class Constant {
    /**
     * 拆分文件大小单位 默认为M
     */
    public static final int SUB_FILE_UNIT = 1024 * 1024;

    /**
     * 默认的普通文件拆分线程池大小
     */
    public static final int NORMAL_FIXEDTHREADPOOL = 5;

    /**
     * 默认的普通文件拆分大小为10M
     */
    public static final int NORMAL_FILESIZELIMIT = 2;

    /**
     * 默认的文件字符集
     */
    public static final String FILECHARSET = "GBK";

    /**
     * 默认的文是否打印拆分信息
     */
    public static final boolean PRINTMSG = true;

    /**
     * 分隔符
     */
    public final static String FILENAME_SEPARATOR = "\\.";


    public final  static  char COMMA = ',';

    public final static String SUB_FILE_SPLITER = "_";

    /**
     * 是否将第一行加入
     */
    public final static boolean TITLE_APPEND = true;
    /**
     * 生成csv是否包含序号
     */
    public final static boolean GENINDEX = true;

}