import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args) {
        System.out.println("🤖 Запуск Telegram бота для мониторинга ISO документов...");

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new TelegramBot());

            System.out.println("✅ Бот успешно запущен!");
            System.out.println("👉 Найдите бота в Telegram и отправьте команду /status");

        } catch (TelegramApiException e) {
            System.err.println("❌ Ошибка при запуске бота: " + e.getMessage());
            e.printStackTrace();
        }
    }
}