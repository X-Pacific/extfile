package org.zxp.extfile.split;

import org.zxp.extfile.split.splitter.iosplit.IOFileSplitter;
import org.zxp.extfile.split.splitter.normal.NormalFileSplitter;
import org.zxp.extfile.util.Constant;

public abstract class FileSplitter {
    private String fileDir;

    private String fileName;

    private int subFileSizeLimit;


    public void setProp(String fileDir, String fileName, int subFileSizeLimit) {
        this.fileDir = fileDir;
        this.fileName = fileName;
        this.subFileSizeLimit = subFileSizeLimit * Constant.SUB_FILE_UNIT;
    }

    /**
     * 获取普通拆分工具类
     * @return
     */
    public static FileSplitter getInstance() {
        return new NormalFileSplitter();
    }

    /**
     * 根据入参确定获取那种拆分工具
     * @return
     */
    public static FileSplitter getInstance(FileSplitType type) throws Exception {
        if (type == FileSplitType.NORMAL) {
            return new NormalFileSplitter();
        } else if (type == FileSplitType.OBSERVE) {
            throw new Exception("暂未支持");
        } else if (type == FileSplitType.IOSP) {
            return new IOFileSplitter();
        }
        throw new Exception("暂未支持");
    }


    /**
     * 其他初始化操作
     */
    public void init() {

    }

    /**
     * 业务逻辑执行
     */
    public abstract void split(String filePath, int subFileSizeLimit,String desFilePath) throws Exception;

    /**
     * 业务逻辑执行
     */
    public abstract void split(String filePath) throws Exception;


    public abstract void split(String filePath,String desFilePath) throws Exception;

    public String getFileDir() {
        return fileDir;
    }

    public void setFileDir(String fileDir) {
        this.fileDir = fileDir;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getSubFileSizeLimit() {
        return subFileSizeLimit;
    }

    public void setSubFileSizeLimit(int subFileSizeLimit) {
        this.subFileSizeLimit = subFileSizeLimit;
    }

}
