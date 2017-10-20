package com.gpaer.service.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by admin on 2017/3/15.
 */
@Service
@Deprecated
public class ProcessJID {

    /**
     * 獲取 JESSIONID
     * @param username
     * @param password
     * @return
     * @throws IOException
     */
    @Deprecated
    public String getJESSIONID(String username, String password) throws IOException {
        HashMap<String, String> params = new HashMap<>();
        String result = post1(null, null);

        Document doc = Jsoup.parse(result);
        Element masthead = doc.select("section.btn-row").first();
//        System.out.println(masthead.html());
        Elements inputs = masthead.children();
        String lt = inputs.get(0).attr("value");
        String execution = inputs.get(1).attr("value");
//        System.out.println("lt="+lt);
//        System.out.println("ex="+execution);
        Elements eles = doc.select("link[href]");
//        System.out.println(eles.get(0).attr("href"));
        String jsessionid = eles.get(0).attr("href").split("=")[1];
        String JID = "JSESSIONID=" + jsessionid;
//        System.out.print(jsessionid);

        params.put("username", username);
        params.put("password", password);
        params.put("lt", lt);
        params.put("execution", execution);
        params.put("_eventId", "submit");
        String ticjeturl = post2(params, JID);
        String jwxtid = apachehttpsTransJID("http://jwxt.sustc.edu.cn/jsxsd/xxwcqk/xxwcqkOnkclb.do", null);//域名内任意地址都可以的
        String finalJID = apachehttpsTransJID(ticjeturl, jwxtid);// final JID
        return finalJID;
    }

    private String post2(HashMap<String, String> params, String JID) throws IOException {
        String casurl = "https://cas.sustc.edu.cn/cas/login?service=http://jwxt.sustc.edu.cn/jsxsd/";
        URL url = new URL(casurl);
        Set set = params.entrySet();
//        Iterator i = set.iterator();
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
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);

        // 此时已经能拿到 TGC 了
//        System.out.println(">><<<<<<<<<<<<<<<<<" + conn.getHeaderField("Set-Cookie"));
//        System.out.println(">><<<<<<<<<<<<<<<<<" + conn.getHeaderField("Location"));

        String result = conn.getHeaderField("Location");
        conn.disconnect();
//        System.out.println(">>>INSTAGRAM token returned: " + builder.toString());
        return result;
    }


    private String post1(String username, String password) throws IOException {
        String casurl = "https://cas.sustc.edu.cn/cas/login?service=http://jwxt.sustc.edu.cn/jsxsd/";
        URL url = new URL(casurl);
        HashMap<String, String> params = new HashMap<String, String>();
        // 发现第一次获取 JID 的时候不带个人信息也是可以的
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
//        conn.getOutputStream().write(postDataBytes);
//        System.out.println(">><<<<<<<<<<<<<<<<<" + conn.getHeaderField("Set-Cookie"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        StringBuilder builder = new StringBuilder();
        for (String line = null; (line = reader.readLine()) != null; ) {
            builder.append(line).append("\n");
        }
        reader.close();
        conn.disconnect();
//        System.out.println("INSTAGRAM token returned: " + builder.toString());
        return builder.toString();
    }

    private String apachehttpsTransJID(String jwxturl, String JIDCookie) throws IOException {
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
        List<Cookie> coos = httpCookieStore.getCookies();
        for (Cookie coo : coos) {
            if (coo.getName().equals("JSESSIONID"))
                return coo.getValue();
        }
        return null;
    }

    @Deprecated
    private String apachehttpsGPA(String jwxturl, String JIDCookie) throws IOException {
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
