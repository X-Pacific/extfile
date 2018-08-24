package org.zxp.extfile.csv;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.context.AnalysisContext;
import com.alibaba.excel.read.event.AnalysisEventListener;
import com.alibaba.excel.support.ExcelTypeEnum;
import org.zxp.extfile.csv.readCsv.CsvReader;
import org.zxp.extfile.util.Constant;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;

public class CsvHandler {

    /**
     * excel转化为csv
     *
     * @throws Exception
     */
    public static int excelCover2csv(String filepath, String filename) throws Exception {
        if (filepath == null || filename == null) {
            throw new NullPointerException();
        }
        ExcelTypeEnum ee = null;
        if (filename.toLowerCase().endsWith("xls")) {
            ee = ExcelTypeEnum.XLS;
        }else if (filename.toLowerCase().endsWith("xlsx")){
            ee = ExcelTypeEnum.XLSX;
        }
        return excelCover2csv(filepath,filename,ee);
    }


    /**
     * excel转化为csv
     *
     * @throws Exception
     */
    public static int excelCover2csv(String filepath, String filename, ExcelTypeEnum ee) throws Exception {
        if (filepath == null || filename == null || ee == null) {
            throw new NullPointerException();
        }
        if (ee == ExcelTypeEnum.XLS) {
            if (!filename.toLowerCase().endsWith("xls")) {
                throw new Exception("文件不匹配");
            }
        }
        if (ee == ExcelTypeEnum.XLSX) {
            if (!filename.toLowerCase().endsWith("xlsx")) {
                throw new Exception("文件不匹配");
            }
        }



        final String fileurl = filepath + File.separator + filename;

        File csvFile = new File(fileurl.substring(0, fileurl.lastIndexOf(".")) + ".csv");
        csvFile.delete();
        final CsvCounter csvCounter = new CsvCounter();

        InputStream inputStream = new FileInputStream(new File(fileurl));
        try {
            ExcelReader reader = new ExcelReader(inputStream, ee, csvCounter,
                    new AnalysisEventListener<List<String>>() {
                        StringBuffer sb = new StringBuffer();
                        String newName = fileurl.substring(0, fileurl.lastIndexOf(".")) + ".csv";

                        @Override
                        public void invoke(List<String> object, AnalysisContext context) {
                            csvCounter.increase();
                            if(csvCounter.sumcount == 1){
                                String header = object.toString().replaceAll(", ",",");
                                if(Constant.GENINDEX) {
                                    sb.append("序号,");
                                }
                                sb.append(header.substring(1, header.length() - 1).replaceAll(String.valueOf((char) 10), "").replaceAll(String.valueOf((char) 11), "").replaceAll(String.valueOf((char) 12), "").replaceAll(String.valueOf((char) 13), "")).append("\n");
                            }else {
                                String content = object.toString();
                                if(Constant.GENINDEX) {
                                    sb.append(csvCounter.sumcount).append(",");
                                }
                                sb.append("`").append(content.substring(1, content.length() - 1).replaceAll(", ", ",`").replaceAll(String.valueOf((char) 10), "").replaceAll(String.valueOf((char) 11), "").replaceAll(String.valueOf((char) 12), "").replaceAll(String.valueOf((char) 13), "").replaceAll("`null","`")).append("\n");
                            }
                            /**判断追加还是写入文件*/
                            sb = addStringBuffer(sb);
                        }

                        @Override
                        public void doAfterAllAnalysed(AnalysisContext context) {
                            /**最后一次要把小于3000的内容写入文件*/
                            writeFile(newName, sb);
                        }

                        private StringBuffer addStringBuffer(StringBuffer content) {
                            /**小于3000则不写入文件*/
                            if (content.length() / 1024 <= 3000) {
                                return content;
                            }
                            writeFile(newName, content);
                            content = null;
                            return new StringBuffer();
                        }

                        /**
                         * 写入文件
                         *
                         * @param filepath
                         * @param content
                         */
                        private void writeFile(String filepath, StringBuffer content) {
                            BufferedWriter out = null;
                            try {
                                out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filepath, true), Constant.FILECHARSET));
                                out.write(content.toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                try {
                                    if (out != null) {
                                        out.close();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
            reader.read();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return csvCounter.sumcount;
    }

    /**
     * 读取CSV并处理
     *
     * @throws Exception
     */
    public static Object dealCsv(String filepath, CsvReaderCallBack csvReaderCallBack) throws Exception {
        CsvReader r = new CsvReader(filepath, ',', Charset.forName(Constant.FILECHARSET));
        try {
            if (csvReaderCallBack == null || filepath == null || "".equals(filepath)) {
                throw new NullPointerException();
            }
            r.readHeaders();
            return csvReaderCallBack.handle(r);
        }finally {
            r.close();
        }
    }
}

class CsvCounter{
    int sumcount = 0;

    public int increase(){
        return ++sumcount;
    }
}