import javax.print.attribute.standard.MediaSize;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DocParser {
    public Map<String, String> buildMap (String path) throws IOException {
        Map<String, String> ISO = new HashMap<>();
        List<String> lines = Files.readAllLines(Paths.get(path));
        //игонрирую заголовок
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.trim().isEmpty()) continue;

            // парсер
            String [] pars = line.split(",");
            if (pars.length == 2) {
                String docName = pars[0].trim();
                String docURL = pars[1].trim();
                ISO.put(docName, docURL);
            }
        }
        return ISO;
    }
}
