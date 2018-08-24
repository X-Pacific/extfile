package org.zxp.extfile.split.splitter.iosplit;

import org.zxp.extfile.split.FileSplitter;
import org.zxp.extfile.util.Constant;

import java.io.*;

public class IOFileSplitter extends FileSplitter {
    public void split(String filePath, int subFileSizeLimit, String desFilePath) throws Exception {
        File srcFile = new File(filePath);//源文件

        long srcSize = srcFile.length();//源文件的大小
        long destSize = Constant.SUB_FILE_UNIT*subFileSizeLimit;//目标文件的大小（分割后每个文件的大小）

        int number = (int)(srcSize/destSize);
        number = srcSize%destSize==0?number:number+1;//分割后文件的数目

        String fileName = filePath.substring(filePath.lastIndexOf("\\"));//源文件名

        InputStream in = null;//输入字节流
        BufferedInputStream bis = null;//输入缓冲流
        byte[] bytes = new byte[1024*1024];//每次读取文件的大小为1MB
        int len = -1;//每次读取的长度值
        String dest = desFilePath;
        File desFile = new File(dest);
        if(!desFile.isDirectory()){
            desFile.mkdir();
        }
        try {
            in = new FileInputStream(srcFile);
            bis = new BufferedInputStream(in);
            for(int i=0;i<number;i++){

                String destName = dest+File.separator+fileName+"-"+i+".dat";
                OutputStream out = new FileOutputStream(destName);
                BufferedOutputStream bos = new BufferedOutputStream(out);
                int count = 0;
                while((len = bis.read(bytes))!=-1){
                    bos.write(bytes, 0, len);//把字节数据写入目标文件中
                    count+=len;
                    if(count>=destSize){
                        break;
                    }
                }
                bos.flush();//刷新
                bos.close();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }finally{
            try {
                if(bis!=null)bis.close();
                if(in!=null)in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void split(String filePath) throws Exception {
        split(filePath,Constant.NORMAL_FILESIZELIMIT,filePath);
    }

    public void split(String filePath, String desFilePath) throws Exception {
        split(filePath,Constant.NORMAL_FILESIZELIMIT,desFilePath);
    }
}
