import org.junit.Test;

public class TestFileSplit {

    @Test
    public void testSplit() throws Exception {
//        FileSplitter fs = FileSplitter.getInstance();
//        String path = "D:\\eeee\\gencsv\\e2.csv";
//        fs.split(path,2,"D:\\eeee\\gencsv\\new\\");
    }

    @Test
    public void testIOSplit() throws Exception {
//        FileSplitter fs = FileSplitter.getInstance(FileSplitType.IOSP);
//        String path = "D:\\eeee\\a2.csv";
//        fs.split(path,2,"D:\\eeee\\new\\");
    }


    @Test
    public void test(){
//        final String fileurl = "D://a2.xlsx";//这是excel的路径
//        InputStream inputStream = null;
//        try {
//            inputStream = new FileInputStream(new File(fileurl));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        try {
//            ExcelReader reader = new ExcelReader(inputStream, ExcelTypeEnum.XLSX, null,
//                    new AnalysisEventListener<List<String>>() {
//                        StringBuffer sb = new StringBuffer();
//                        String newName = fileurl.substring(0, fileurl.lastIndexOf(".")) + ".csv";
//                        private int index = 0;
//
//                        @Override
//                        public void invoke(List<String> object, AnalysisContext context) {
//                            ++index;
//                        }
//
//                        @Override
//                        public void doAfterAllAnalysed(AnalysisContext context) {
//                        }
//                    });
//            reader.read();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                inputStream.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }
}