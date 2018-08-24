package org.zxp.extfile.split.splitter.normal;

import org.zxp.extfile.util.Constant;
import org.zxp.extfile.util.Util;

import java.io.*;
import java.util.List;

/**
 * 普通方式拆分的线程池任务
 */
public class NormalFileSplitWriteTask extends Thread{
    /**
     * 文件所在路径
     */
    private String fileDir;
    /**
     * 文件名称
     */
    private String fileName;

    public int getSumLine() {
        return sumLine;
    }

    public void setSumLine(int sumLine) {
        this.sumLine = sumLine;
    }

    private int sumLine;

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

    public List<String> getFileContent() {
        return fileContent;
    }

    public void setFileContent(List<String> fileContent) {
        this.fileContent = fileContent;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public int getFileWritenSize() {
        return fileWritenSize;
    }

    public void setFileWritenSize(int fileWritenSize) {
        this.fileWritenSize = fileWritenSize;
    }

    /**
     * 文件内容
     */
    private List<String> fileContent;

    /**
     * 文件大小
     */
    private int fileSize;

    /**
     * 文件实际写入的大小
     */
    private int fileWritenSize;

    public NormalFileSplitWriteTask(String fileDir, String fileName, List<String> fileContent, int fileSize) {
        this.fileName = fileName;
        this.fileContent = fileContent;
        this.fileDir = fileDir;
        this.fileSize = fileSize;
        this.fileWritenSize = 0;
        this.sumLine = 0;
    }

    @Override
    public void run() {
        File desFile = new File(this.fileDir);
        if(!desFile.isDirectory()){
            desFile.mkdir();
        }
        File file = new File(Util.genFullFileName(this.fileDir, this.fileName));
        BufferedWriter bw = null;
        try {
//			FileWriter fw = new FileWriter(file);
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,false), Constant.FILECHARSET));
            for(String lineContent : fileContent) {
                bw.write(lineContent);
                bw.newLine();
                bw.flush();
                fileWritenSize += lineContent.length();
                sumLine++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                }
            }
            this.fileContent = null;
        }
    }
}
