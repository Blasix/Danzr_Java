package logic;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Version {
    public static final String VERSION = "1.1.0";

    public static String getLatestVersion() {
        try {
            URL url = new URL("https://api.github.com/repos/Blasix/Danzr_Java/releases/latest");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuilder result = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();

            return result.toString().split("\"tag_name\":\"")[1].split("\"")[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isNewerVersion() {
        String[] currentComponents = VERSION.split("\\.");
        String[] newComponents = getLatestVersion().split("\\.");

        int length = Math.max(currentComponents.length, newComponents.length);

        for (int i = 0; i < length; i++) {
            int currentComponent = i < currentComponents.length ? Integer.parseInt(currentComponents[i]) : 0;
            int newComponent = i < newComponents.length ? Integer.parseInt(newComponents[i]) : 0;

            if (newComponent > currentComponent) {
                return true;
            } else if (newComponent < currentComponent) {
                return false;
            }
        }

        // If we've made it this far, the version numbers are equal
        return false;
    }

}
