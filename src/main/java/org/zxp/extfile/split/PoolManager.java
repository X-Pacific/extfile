package org.zxp.extfile.split;

import org.zxp.extfile.util.Constant;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 该类暂时不用
 */
public class PoolManager {
    private static ExecutorService fileWritePool = null;

    static{
        fileWritePool = Executors.newFixedThreadPool(Constant.NORMAL_FIXEDTHREADPOOL);
    }

    public static synchronized ExecutorService getFileWritePool() {
        if(fileWritePool == null){
            fileWritePool = Executors.newFixedThreadPool(Constant.NORMAL_FIXEDTHREADPOOL);
        }
        return fileWritePool;
    }

}
