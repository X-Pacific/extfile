import org.zxp.extfile.csv.FastCsvHandler;
import org.junit.Test;

import java.util.List;

public class TestCvsHandler {


    /**
     * 问题1：数量不一致
     * 问题2：内存消耗：1458（Mb）
     *
     * @throws Exception
     */
    @Test
    public void genCsv() throws Exception {
//        long beginMem = Util.getMemInfo();
//        long startTime = System.currentTimeMillis();
//
//        int a = CsvHandler.excelCover2csv("D:\\eeee\\gencsv", "e2.xlsx", ExcelTypeEnum.XLSX);
//
//        long endTime = System.currentTimeMillis();
//        long endMem = Util.getMemInfo();
////        System.out.println("内存消耗：" + (endMem - beginMem) + "（Mb） 总耗时：" + (endTime - startTime) + "（ms）");
//
//        System.out.println(a);
////        Thread.sleep(20000000);
    }

    @Test
    public void testMname() throws Exception {
//        CsvHandler.dealCsv("D://qqq.csv", new CsvReaderCallBack() {
//            public Object handle(CsvReader csvReader) throws IOException {
//                while (csvReader.readRecord()) {
//                    System.out.println(csvReader.get("testName5"));
//                }
//                return "";
//            };
//        });
    }


    @Test
    public void tesTfASTCSV() throws Exception {
        FastCsvHandler<CsvDemo> fhandle = FastCsvHandler.getInstance();
        List<CsvDemo>  list = fhandle.dealCsv("D://eeee//gencsv//new//e2_7.csv",CsvDemo.class,"",null);
        for(int i = 0;i < 10;i++){
            System.out.println(list.get(i));
        }
    }
}
