import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello friend!");
        DocParser docParser = new DocParser();
        Map<String, String> docMap= new HashMap<>(docParser.buildMap("src/main/resources/DocsURLs"));
        Selector selector = new Selector();
        selector.checkAllDocs(docMap);
    }
}
