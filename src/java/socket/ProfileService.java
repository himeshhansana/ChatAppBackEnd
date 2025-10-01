package socket;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class ProfileService {

    public static String getProfileUrl(int userId) {
        try {

            URL url = new URI("http://localhost:8080/ChatApp/profile-images/" + userId + "/profile1.png").toURL(); // java.net.URL
            HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // java.net.HttpURLConnection
            conn.setRequestMethod("HEAD");
            int responseCode = conn.getResponseCode();
            conn.connect();

            String profile;

            if (responseCode == HttpURLConnection.HTTP_OK) {
                profile = ChatService.URL + "/ChatApp/profile-images/" + userId + "/profile1.png";
            } else {
                profile = "";
            }
            return profile;
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return "";
        }
    }
}
