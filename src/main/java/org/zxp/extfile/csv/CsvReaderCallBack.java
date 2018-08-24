package org.zxp.extfile.csv;

import org.zxp.extfile.csv.readCsv.CsvReader;

import java.io.IOException;

public interface CsvReaderCallBack {
    public Object handle(CsvReader csvReader) throws IOException;
}
