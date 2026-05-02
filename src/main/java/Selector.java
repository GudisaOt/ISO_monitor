import com.microsoft.playwright.*;
import java.util.Map;

public class Selector {
    private static final String CSS_SELECTOR = "#publicationStatus span";
    private static final int TIMEOUT_SECONDS = 15;
    private static final int DELAY_BETWEEN_MS = 3000;

    public void checkAllDocs(Map<String, String> docs) {
        System.out.println("Start...");

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                    .setHeadless(false)
                    .setSlowMo(100));

            BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                    .setViewportSize(1920, 1080)
                    .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36"));

            context.addInitScript("Object.defineProperty(navigator, 'webdriver', {get: () => undefined});");

            Page page = context.newPage();

            for (Map.Entry<String, String> entry : docs.entrySet()) {
                String docName = entry.getKey();
                String docUrl = entry.getValue();

                System.out.println("Document: " + docName);
                System.out.println("  URL: " + docUrl);

                String status = getStatusWithPlaywright(page, docUrl);
                System.out.println("  Status: " + status);
                System.out.println();

                page.waitForTimeout(DELAY_BETWEEN_MS);
            }

        } catch (Exception e) {
            System.err.println("Ошибка Playwright: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String getStatusWithPlaywright(Page page, String url) {
        try {
            page.navigate(url);
            page.waitForSelector(CSS_SELECTOR, new Page.WaitForSelectorOptions()
                    .setTimeout(TIMEOUT_SECONDS * 1000));

            String status = page.locator(CSS_SELECTOR).textContent();
            return (status == null || status.isEmpty()) ? "Статус не найден" : status;

        } catch (Exception e) {
            return "Ошибка: " + e.getMessage();
        }
    }
}