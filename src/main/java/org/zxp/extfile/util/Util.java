package org.zxp.extfile.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Util {
    /**
     * 判断是否存在null或者空字符串
     *
     * @param params
     * @return
     */
    public static boolean isNull(String... params) {
        if (params == null)
            return true;

        for (String param : params) {
            if (param == null || "".equals(param.trim()))
                return true;
        }

        return false;
    }

    /**
     * 判断是否是null对象
     *
     * @param params
     * @return
     */
    public static boolean isNull(Object... params) {
        if (params == null)
            return true;

        for (Object param : params) {
            if (param == null)
                return true;
        }

        return false;
    }

    public static String genFullFileName(String fileDir, String fileName) {
        if(fileDir.endsWith(File.separator)) {
            return fileDir+fileName;
        } else {
            return fileDir + File.separator + fileName;
        }
    }

    /**
     * 根据全限定路径获取文件名和路径名
     * @param filePath
     * @return
     */
    public static String[] getFileNameAndPath(String filePath){
        String path = filePath.substring(0,filePath.lastIndexOf(File.separator));
        String name = filePath.substring(filePath.lastIndexOf(File.separator),filePath.length());
        String[] ret = new  String[2];
        ret[0] = path;
        ret[1] = name;
        return ret;
    }

    /**
     * 获取内存消耗信息
     */
    public static long getMemInfo(){
        int kb = 1024;
        // 已使用内存
        long totalMemory = Runtime.getRuntime().totalMemory() / kb/ kb;
        // 剩余内存
        long freeMemory = Runtime.getRuntime().freeMemory() / kb/ kb;
        // 最大可使用内存
//        long maxMemory = Runtime.getRuntime().maxMemory() / kb/ kb;
//        System.out.println("==================================================================");
        System.out.println("已使用总内存:"+(totalMemory)+"M");
        System.out.println("剩余内存:"+freeMemory+"M");
//        System.out.println("最大可使用内存:"+maxMemory+"M");
        return totalMemory;
    }


    public static List<String> lisFile(String path,String type) throws IOException {
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            return null;
        }
        if (!dirFile.isDirectory()) {
            return null;
        }
        String[] fileList = dirFile.list();
        List fileName = new ArrayList<String>();

        for (int i = 0; i < fileList.length; i++) {
            //遍历文件目录
            String string = fileList[i];
            //File("documentName","fileName")是File的另一个构造器
            File file = new File(dirFile.getPath(), string);
            String name = file.getName();
            //如果是一个目录，搜索深度depth++，输出目录名后，进行递归
            if(type.endsWith("."+type) ) {
                fileName.add(name);
            }
        }
        return fileName;
    }
}
