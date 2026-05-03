import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.io.IOException;
import java.util.Map;

public class TelegramBot extends TelegramLongPollingBot {

    // Замените на свой токен от BotFather
    private static final String BOT_TOKEN = "8692090674:AAF4kY-E7g_6ydmBLABtfapP5eNQnIVMISk";
    private static final String BOT_USERNAME = "@BIAMMF_bot"; // Ваш username бота

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (messageText.equals("/start")) {
                sendMessage(chatId, "👋 Привет! Я бот для мониторинга статусов ISO документов.\n\n" +
                        "Команды:\n" +
                        "/status - получить текущие статусы всех документов\n" +
                        "/help - показать эту справку");
            }
            else if (messageText.equals("/status")) {
                sendMessage(chatId, "🔄 Получаю актуальные статусы документов. Пожалуйста, подождите...");

                // Запускаем мониторинг
                String result = runMonitoring();
                sendMessage(chatId, result);
            }
            else if (messageText.equals("/help")) {
                sendMessage(chatId, "📋 Доступные команды:\n" +
                        "/status - получить текущие статусы документов\n" +
                        "/help - показать эту справку");
            }
            else {
                sendMessage(chatId, "❓ Неизвестная команда. Используйте /help для списка команд.");
            }
        }
    }

    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.err.println("Ошибка отправки сообщения: " + e.getMessage());
        }
    }

    private String runMonitoring() {
        try {

            DocParser docParser = new DocParser();
            Map<String, String> docMap = docParser.buildMap("src/main/resources/DocsURLs");

            Selector selector = new Selector();
            Map<String, String> statuses = selector.getStatuses(docMap);

            StringBuilder result = new StringBuilder();
            result.append("📊 *Статусы ISO документов:*\n\n");

            for (Map.Entry<String, String> entry : statuses.entrySet()) {
                String status = entry.getValue();
                String emoji;

                if (status.toLowerCase().contains("published")) {
                    emoji = "✅";
                } else if (status.toLowerCase().contains("withdrawn")) {
                    emoji = "❌";
                } else if (status.toLowerCase().contains("under review")) {
                    emoji = "⏳";
                } else if (status.toLowerCase().contains("Опубликовано")) {
                    emoji = "✅";
                } else {
                    emoji = "📄";
                }

                result.append(emoji).append(" *").append(entry.getKey()).append("*\n");
                result.append("   Статус: ").append(status).append("\n\n");
            }

            return result.toString();

        } catch (IOException e) {
            return "❌ Ошибка при чтении файла с документами: " + e.getMessage();
        } catch (Exception e) {
            return "❌ Ошибка при получении статусов: " + e.getMessage();
        }
    }
}