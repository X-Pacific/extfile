package org.zxp.extfile.csv;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
/***
 * 这个配置的是csv头部的列名(如果csv头部与dto字段不一致)
 */
public @interface CsvHead {
    String value();
}
