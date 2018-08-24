package org.zxp.extfile.csv;

import org.zxp.extfile.csv.autocheck.CheckService;
import org.zxp.extfile.csv.dto.CsvBaseDto;
import org.zxp.extfile.csv.readCsv.CsvReader;
import org.zxp.extfile.util.Constant;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class FastCsvHandler<T> {
    private static FastCsvHandler handler = null;
    private FastCsvHandler(){

    }

    public synchronized static FastCsvHandler getInstance(){
       if (handler == null) {
           handler = new FastCsvHandler();
       }
       return handler;
    }

    /**
     * 读取CSV并返回相应pojo的结果list
     * @param filepath
     * @param clazz
     * @param prefix
     * @param checkService 校验服务
     * @return
     * @throws Exception
     */
    public List<T> dealCsv(String filepath, Class<T> clazz, String prefix, CheckService checkService) throws Exception {
        if(clazz == null || "".equals(filepath)){
            throw new NullPointerException();
        }
        /***从clazz中获取与csv的抬头字段 begin*/
        Field[] fields = clazz.getDeclaredFields();
        //把父类的序号搞进来
        if(clazz.getSuperclass() == CsvBaseDto.class){
            Field[] fieldstmp = new Field[fields.length+1];
            for(int i = 0;i < fields.length;i++){
                fieldstmp[i] = fields[i];
            }
            fieldstmp[fields.length] = clazz.getSuperclass().getDeclaredField("indexnum");
            fields = fieldstmp;
        }
        String[] columeNames = new String[fields.length];
        for(int i = 0;i < columeNames.length;i++){
            String columName = "";
            if(fields[i].getAnnotation(CsvHead.class) != null){
                if( fields[i].getAnnotation(CsvHead.class).value() != null){
                    columName =  fields[i].getAnnotation(CsvHead.class).value();
                }
            }
            if(columName.equals("") || columName == null){
                columName =  fields[i].getName();
            }
            columeNames[i] = columName;
        }
        /***从clazz中获取与csv的抬头字段 end*/
        CsvReader r = new CsvReader(filepath, ',', Charset.forName(Constant.FILECHARSET));
        List<T> list = new ArrayList<T>();
        try {
            r.readHeaders();
            while (r.readRecord()) {
                T tt = clazz.newInstance();
                for (int j = 0; j < fields.length; j++) {
                    String name = fields[j].getName();
                    if (fields[j].getAnnotation(CsvHead.class) != null) {
                        name = fields[j].getAnnotation(CsvHead.class).value();
                    }
                    if (name.equals(columeNames[j])) {
                        setValue(tt, fields[j], r.get(prefix + columeNames[j]));
                    }
                }
                //校验
                if (checkService != null) {
                    checkService.checkField(tt);
                }
                list.add(tt);
            }
        }finally {
            r.close();
        }
        return list;
    }


    /**
     * 给tt对象的field字段设置值
     * @param tt
     * @param field
     * @param value
     * @throws Exception
     */
    private void setValue(T tt,Field field,String value) throws Exception {
        PropertyDescriptor pd = new PropertyDescriptor(field.getName(), tt.getClass());
        Method wM = pd.getWriteMethod();//获得写方法
        if(value != null && value.startsWith("`")){
            value = value.substring(1,value.length());
        }
        wM.invoke(tt, value);
    }
}
