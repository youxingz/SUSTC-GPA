package com.gpaer.service.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by admin on 2017/3/15.
 */
@Service
@Deprecated
public class HttpUtil {


    @Deprecated
    public String postHttps(String jwxturl, String JID,HashMap<String, String> params) throws IOException {
//        String casurl = "https://cas.sustc.edu.cn/cas/login?service=http://jwxt.sustc.edu.cn/jsxsd/";
        URL url = new URL(jwxturl);
        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, String> param : params.entrySet()) {
            if (postData.length() != 0) {
                postData.append('&');
            }
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setRequestProperty("Cookie", JID + ";path=/cas;Secure;HttpOnly");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.95 Mobile Safari/537.36");
        conn.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch");
        conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6,fr-FR;q=0.4,fr;q=0.2");
        conn.setRequestProperty("Connection", "keep-alive");
        conn.setRequestProperty("Host", "jwxt.sustc.edu.cn");
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);

        // output
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        StringBuilder builder = new StringBuilder();
        for (String line = null; (line = reader.readLine()) != null; ) {
            builder.append(line).append("\n");
        }
        reader.close();
        conn.disconnect();
        return builder.toString();
    }

    public String apachehttpJWXTPage(String jwxturl, String JIDCookie) throws IOException {
        CookieStore httpCookieStore = new BasicCookieStore();
        HttpClient client = HttpClientBuilder.create().setDefaultCookieStore(httpCookieStore).build();
        HttpGet request = new HttpGet(jwxturl);
        if (JIDCookie != null)
            request.setHeader("Cookie", "JSESSIONID=" + JIDCookie);
        request.setHeader("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.95 Mobile Safari/537.36");
        request.setHeader("Accept-Encoding", "gzip, deflate, sdch");
        request.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6,fr-FR;q=0.4,fr;q=0.2");
        request.setHeader("Connection", "keep-alive");
        request.setHeader("Host", "jwxt.sustc.edu.cn");
        HttpResponse response = client.execute(request);

        BufferedReader in = null;
        String result = "";

        in = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));
        String line;
        while ((line = in.readLine()) != null) {
            result += line;
        }
        return result;
    }
}
