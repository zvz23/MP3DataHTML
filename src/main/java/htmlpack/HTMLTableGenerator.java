package htmlpack;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public final class HTMLTableGenerator {

    private String htmlPath;
    private List<String> headers;
    private List<String[]> tableData;

    public HTMLTableGenerator(List<String> newTableHeaders){
        this.headers = newTableHeaders;
    }

    public String getHtmlPath() {
        return htmlPath;
    }

    public void setHtmlPath(String htmlPath) {
        File tempFile = new File(htmlPath);
        if((tempFile.isDirectory() && tempFile.exists())){
            throw new IllegalArgumentException("File not found");
        }
        this.htmlPath = htmlPath;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        if(headers == null){
            throw new IllegalArgumentException("Empty header");
        }
        this.headers = headers;
    }

    public List<String[]> getTableData() {
        return tableData;
    }

    public void setTableData(List<String[]> tableData) {
        tableData.forEach(i -> {
            if(i.length > headers.size()){
                throw new IllegalArgumentException("Size of table data does match the header");
            }
        });
        this.tableData = tableData;
    }

    public void generateTable() throws IOException {
        String format = String.format("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<header><title>Mp3Table</title>%s</header>\n" +
                "<body>\n" +
                "<table>\n" +
                "<tr>\n" +
                "%s" +
                "</tr>\n" +
                "%s" +
                "</table>\n" +
                "</body>\n" +
                "</html>\n",getTableCSS(), generateHeader(), generateRow());

        try (FileOutputStream os = new FileOutputStream("C:\\Users\\ADMIN\\Desktop\\index.html")) {
            for (char i : format.toCharArray()) {
                os.write(i);
            }
        }
    }

    private String getTableCSS(){
        return "<style>table,th,td{border: solid black;}table{margin: 0 auto;}</style>";
    }

    public String generateHeader(){
        StringBuilder builder = new StringBuilder();
        for(String header : headers){
            builder.append(String.format("\t<th>%s</th>\n", header));
        }
        return builder.toString();
    }

    public String generateRow(){
        StringBuilder builder = new StringBuilder();
        for(String[] row : tableData){
            builder.append("<tr>\n");
            for(String data: row){
                builder.append(String.format("\t<td>%s</td>\n", data));
            }
            builder.append("</tr>\n");
        }
        return builder.toString();
    }


}
