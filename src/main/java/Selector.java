import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Map;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
public class Selector {
    public static final String SELECTOR = "document.querySelector(\"#publicationStatus span\")?.textContent";

    public void checkAllDocs(Map<String, String> docs) {
        System.out.println("Start...");
        int delay = 3000;
        for (Map.Entry<String,String> entry : docs.entrySet()) {
            String docName = entry.getKey();
            String docUrl = entry.getValue();

            String status = getStatus(docUrl);

            System.out.println("Документ: " + docName);
            System.out.println("  URL: " + docUrl);
            System.out.println("  Статус: " + status);
            System.out.println();
        }
        try {
            Thread.sleep(delay); // ждём перед следующим запросом
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public String getStatus(String url) {
        try {
            Document document = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                    .header("Accept-Language", "en-US,en;q=0.5")
                    .header("Accept-Encoding", "gzip, deflate, br")
                    .header("Connection", "keep-alive")
                    .header("Upgrade-Insecure-Requests", "1")
                    .header("Sec-Fetch-Dest", "document")
                    .header("Sec-Fetch-Mode", "navigate")
                    .header("Sec-Fetch-Site", "none")
                    .header("Sec-Fetch-User", "?1")
                    .timeout(10000)
                    .get();

            String status = document.select(SELECTOR).first().text();
            return status;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
