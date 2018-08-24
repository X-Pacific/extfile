package org.zxp.extfile.csv.autocheck;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface CheckService<T> {
    /**
     * 根据对象注解进行简单校验
     * @param obj
     * @return
     * @throws Exception
     */
    public String checkField(Object obj) throws Exception;

    public void addCheckInfo(T t);


    public List<T> getCheckInfoList();

    public void setCheckInfoList(List<T> checkInfoList);
}
