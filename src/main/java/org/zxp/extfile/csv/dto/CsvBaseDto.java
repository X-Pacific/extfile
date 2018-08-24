package org.zxp.extfile.csv.dto;

import org.zxp.extfile.csv.CsvHead;

public class CsvBaseDto {
    public String getIndexnum() {
        return indexnum;
    }

    public void setIndexnum(String indexnum) {
        this.indexnum = indexnum;
    }

    @CsvHead("序号")
    private String indexnum;
}
