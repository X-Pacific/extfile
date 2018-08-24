package org.zxp.extfile.split.splitter.normal;

import org.zxp.extfile.split.FileSplitter;
import org.zxp.extfile.util.Constant;
import org.zxp.extfile.util.Util;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;

/**
 * 普通方式拆分非二进制文件（文本文件）的实现类入口
 */
public class NormalFileSplitter extends FileSplitter {

    private boolean isExcute;

    private ExecutorService fileWritePool;

    private NormalFileSplitterTools fileSplitterTools = new NormalFileSplitterTools();

    public NormalFileSplitter() {
        isExcute = false;
        this.fileWritePool = Executors.newFixedThreadPool(Constant.NORMAL_FIXEDTHREADPOOL);
    }

    public void split(String filePath) throws Exception {
        split(filePath, Constant.NORMAL_FILESIZELIMIT,filePath);
    }

    public void split(String filePath,String desFilePath) throws Exception {
        split(filePath, Constant.NORMAL_FILESIZELIMIT,desFilePath);
    }

    public void split(String filePath, int subFileSizeLimit,String desFilePath) throws Exception {

        long beginMem = Util.getMemInfo();

        if(isExcute){
            throw new Exception("线程池已经回收，请不要重复执行");
        }else{
            isExcute  = true;
        }

        String fileDir = Util.getFileNameAndPath(filePath)[0];
        String fileName = Util.getFileNameAndPath(filePath)[1];
        this.setFileDir(fileDir);
        this.setFileName(fileName);
        super.setProp(fileDir, fileName, subFileSizeLimit);
        fileSplitterTools.setProp(subFileSizeLimit, desFilePath, fileName);
        long startTime = System.currentTimeMillis();
        File file = new File(Util.genFullFileName(this.getFileDir(), this.getFileName()));
        BufferedReader reader = null;
        List<Future> futureList = new ArrayList<Future>();
        int sumSize = 0;
        String firstLine = "";//首行内容
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file), Constant.FILECHARSET);
            reader = new BufferedReader(isr);
            String lineContent = "";
            int index = 0;
            while (reader.ready() && (lineContent = reader.readLine()) != null) {
                if(++index == 1){
                    firstLine = lineContent;
                }
                NormalFileSplitWriteTask fileWriteTask = fileSplitterTools.spilt(lineContent,firstLine);
                if (fileWriteTask != null) {
                    //将任务提交pool处理
                    Future future = this.fileWritePool.submit(fileWriteTask, fileWriteTask);
                    futureList.add(future);
                    sumSize += fileWriteTask.getFileSize();
                }
            }
            //如果完事了再检查一下剩余还有没有
            NormalFileSplitWriteTask fileWriteTask = fileSplitterTools.addEndContent();
            if (fileWriteTask != null) {
                //将任务提交pool处理
                Future future = this.fileWritePool.submit(fileWriteTask, fileWriteTask);
                futureList.add(future);
                sumSize += fileWriteTask.getFileSize();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new Exception("文件未找到");
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("文件IO异常");
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        /**如果同步模式才走入下面的逻辑*/
        /**同步模式无法输出信息*/
        Future futureTemp = null;
        int totalFileWritenSize = 0;
        int sumLine = 0;
        while (true) {
            for (Iterator<Future> it = futureList.iterator(); it.hasNext(); ) {
                futureTemp = it.next();
                if (futureTemp.isDone()) {
                    try {
                        totalFileWritenSize += ((NormalFileSplitWriteTask) futureTemp.get()).getFileWritenSize();
                        sumLine += ((NormalFileSplitWriteTask) futureTemp.get()).getSumLine();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        throw new Exception("获取线程执行结果失败");
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                        throw new Exception("获取线程执行结果失败");
                    }
                    it.remove();
                }
            }
            if (futureList == null || futureList.size() == 0) {
                break;
            }
        }
        this.fileWritePool.shutdown();
        long endTime = System.currentTimeMillis();
        long endMem = Util.getMemInfo();
        if (Constant.PRINTMSG) {
            System.out.println("内存消耗："+(endMem-beginMem)+"（Mb） 总耗时：" + (endTime - startTime)+"（ms） 共计写入："+sumLine+"行数据");
            //检查源文件大小和最终写入文件大小和是否相等。
//            if (sumSize == totalFileWritenSize) {
//                System.out.println("文件拆分成功！源文件大小为：" + sumSize + ", 拆分后的子文件大小之后为：" + totalFileWritenSize);
//            } else {
//                System.out.println("文件拆分失败！源文件大小为：" + sumSize + ", 拆分后的子文件大小之后为：" + totalFileWritenSize);
//            }
        }
    }

}
