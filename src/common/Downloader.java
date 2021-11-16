package common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fanglingxiao
 * @version 1.0
 * @description TODO
 * @date 2021/11/8 10:12 下午
 **/
public class Downloader {
    public static List<String> download() throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL("https://www.baidu.com")
                .openConnection();
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                             new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }
}
