package api;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class TelegramSender {
    private static final String BOT_TOKEN = "7634287599:AAHa-NGqzREwBdQMlVweRJDofvEjCUuK9DY";
    private static final String CHAT_ID = "292510604";

    public static void sendToTelegram(String message) throws Exception {
        // Правильное кодирование сообщения для URL
        String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8.toString())
                .replace("+", "%20")
                .replace("%2F", "/")
                .replace("%3A", ":");

        String url = "https://api.telegram.org/bot" + BOT_TOKEN +
                "/sendMessage?chat_id=" + CHAT_ID +
                "&text=" + encodedMessage;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Telegram response: " + response.body());
    }
}