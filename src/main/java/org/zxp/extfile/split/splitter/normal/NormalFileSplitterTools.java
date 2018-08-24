package org.zxp.extfile.split.splitter.normal;

import org.zxp.extfile.util.Constant;
import org.zxp.extfile.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * 普通方式拆分的工具方法
 */
public class NormalFileSplitterTools {
    /**
     * 拆分出来的子文件所在路径
     */
    private String fileDir;

    /**
     * 拆分出来的子文件的大小上限值
     */
    private int subFileSizeLimit;

    /**
     * 当前cache文件内容，大小小于subFileSizeLimit
     */
    private List<String> fileCache;

    /**
     * 当前cache的大小值，小于subFileSizeLimit
     */
    private int fileCacheSize;

    /**
     * 已拆分的子文件个数
     * 鉴于是单线程操作，故这里未使用原子计数器
     */
    private int subFileCounter;

    /**
     * 待生成的子文件全名称模板
     */
    private String fileNameTemplate;

    /**
     * 小于每个拆分文件大小的剩余文件cache文件内容
     */
    private List<String> fileCacheLast;

    public List<String> getFileCacheLast() {
        return fileCacheLast;
    }

    public String getFileDir() {
        return fileDir;
    }

    public void setFileDir(String fileDir) {
        this.fileDir = fileDir;
    }

    public int getSubFileSizeLimit() {
        return subFileSizeLimit;
    }

    public void setSubFileSizeLimit(int subFileSizeLimit) {
        this.subFileSizeLimit = subFileSizeLimit;
    }

    public List<String> getFileCache() {
        return fileCache;
    }

    public void setFileCache(List<String> fileCache) {
        this.fileCache = fileCache;
    }

    public int getFileCacheSize() {
        return fileCacheSize;
    }

    public void setFileCacheSize(int fileCacheSize) {
        this.fileCacheSize = fileCacheSize;
    }

    public int getSubFileCounter() {
        return subFileCounter;
    }

    public void setSubFileCounter(int subFileCounter) {
        this.subFileCounter = subFileCounter;
    }

    public String getFileNameTemplate() {
        return fileNameTemplate;
    }

    public void setFileNameTemplate(String fileNameTemplate) {
        this.fileNameTemplate = fileNameTemplate;
    }

    public void setFileCacheLast(List<String> fileCacheLast) {
        this.fileCacheLast = fileCacheLast;
    }


    public void setProp(int subFileSizeLimit, String fileDir, String fileNameTemplate) {
        this.fileDir = fileDir;
        this.fileNameTemplate = fileNameTemplate;
        this.subFileSizeLimit = subFileSizeLimit * Constant.SUB_FILE_UNIT;
        this.fileCacheSize = 0;
        this.fileCache = new ArrayList<String>();
        this.fileCacheLast = new ArrayList<String>();
    }

    public NormalFileSplitWriteTask spilt(String lineContent,String firstContent) {
        int totalSize = this.fileCacheSize + lineContent.length();
        //当前行加入后，缓存的文件内容大于上限值，则生成一个新的Task
        if (totalSize >= subFileSizeLimit) {
            /**先将本次的add进去*/
            this.fileCache.add(lineContent);
            this.fileCacheLast.add(lineContent);
            this.fileCacheSize += lineContent.length();
            this.subFileCounter++;
            String subFileName = genSubFileName();
            List<String> fileCacheCopy = new ArrayList<String>();
            fileCacheCopy.addAll(this.fileCache);
            NormalFileSplitWriteTask fileWriteTask = new NormalFileSplitWriteTask(this.fileDir, subFileName, fileCacheCopy, this.fileCacheSize);
            //重置文件缓存和大小
            this.fileCache.clear();
            this.fileCacheLast.clear();
            this.fileCacheSize = 0;
            if(Constant.TITLE_APPEND){
                //当新建文件时，把头信息优先加入
                this.fileCache.add(firstContent);
                this.fileCacheLast.add(firstContent);
                this.fileCacheSize = 1;
            }
            return fileWriteTask;
        } else {
            this.fileCache.add(lineContent);
            this.fileCacheLast.add(lineContent);
            this.fileCacheSize += lineContent.length();
            return null;
        }
    }


    /**
     * 添加被spilt方法抛弃的内容到线程池
     *
     * @return
     */
    public NormalFileSplitWriteTask addEndContent() {
        if (this.fileCacheLast.size() > 0) {
            int totalSize = this.fileCacheSize;
            this.subFileCounter++;
            String subFileName = genSubFileName();
            List<String> fileCacheCopy = new ArrayList<String>();
            fileCacheCopy.addAll(this.fileCacheLast);
            NormalFileSplitWriteTask fileWriteTask = new NormalFileSplitWriteTask(this.fileDir, subFileName, fileCacheCopy, this.fileCacheSize);
            this.fileCacheLast.clear();
            this.fileCacheSize = 0;
            return fileWriteTask;
        } else {
            return null;
        }
    }

    /**
     * 生成子文件的名称
     *
     * @return 子文件名称
     */
    private String genSubFileName() {
        String fileName = "";
        String[] fileNameItems = this.fileNameTemplate.split(Constant.FILENAME_SEPARATOR);
        if (fileNameItems.length == 1) {
            fileName = fileNameItems[0] + Constant.SUB_FILE_SPLITER + this.subFileCounter;
        } else {
            StringBuffer sb = new StringBuffer();
            sb.append(fileNameItems[0]).append(Constant.SUB_FILE_SPLITER).append(this.subFileCounter)
                    .append(".").append(fileNameItems[1]);
            fileName = sb.toString();
        }
        return fileName;
    }

    /**
     * 生成子文件的名称
     *
     * @param subFileNo —— 子文件编号
     * @return 子文件名称
     */
    public String genSubFileFullName(int subFileNo) {
        String fileName = "";
        String[] fileNameItems = this.fileNameTemplate.split(Constant.FILENAME_SEPARATOR);
        if (fileNameItems.length == 1) {
            fileName = fileNameItems[0] + subFileNo;
        } else {
            StringBuffer sb = new StringBuffer();
            sb.append(fileNameItems[0]).append(subFileNo)
                    .append(".").append(fileNameItems[1]);
            fileName = sb.toString();
        }
        return Util.genFullFileName(this.fileDir, fileName);
    }
}
