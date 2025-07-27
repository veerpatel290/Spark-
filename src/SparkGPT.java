import java.io.*;
import java.net.*;
import org.json.*;
import io.github.cdimascio.dotenv.Dotenv;

public class SparkGPT {
  private static final Dotenv dotenv = Dotenv.load();
private static final String API_KEY = dotenv.get("OPENAI_API_KEY");

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public static void main(String[] args) throws IOException {
        String userMessage = "Hello, what's your name?";
        String response = getChatGPTResponse(userMessage);
        System.out.println("SparkGPT: " + response);
    }

    public static String getChatGPTResponse(String message) throws IOException {
        URL url = new URL(API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + "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");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("model", "gpt-3.5-turbo");

        JSONArray messages = new JSONArray();
        JSONObject userMsg = new JSONObject();
        userMsg.put("role", "user");
        userMsg.put("content", message);
        messages.put(userMsg);

        jsonBody.put("messages", messages);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonBody.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), "utf-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line.trim());
            }
        }

        JSONObject jsonResponse = new JSONObject(response.toString());
        return jsonResponse
            .getJSONArray("choices")
            .getJSONObject(0)
            .getJSONObject("message")
            .getString("content");
    }
}
