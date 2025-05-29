package api;

import util.ConfigLoader;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class TelegramSender implements MessageSender {
    private final String botToken;
    private final String chatId;

    public TelegramSender() {
        this.botToken = ConfigLoader.getTelegramBotToken();
        this.chatId = ConfigLoader.getTelegramChatId();

        if (botToken == null || botToken.isEmpty() || chatId == null || chatId.isEmpty()) {
            throw new IllegalStateException("Telegram credentials не настроены в config.properties");
        }
    }

    @Override
    public void send(String message) throws Exception {
        String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8.toString())
                .replace("+", "%20")
                .replace("%2F", "/")
                .replace("%3A", ":");

        String url = String.format(
                "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s",
                botToken,
                chatId,
                encodedMessage
        );

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Telegram response: " + response.body());
    }
}