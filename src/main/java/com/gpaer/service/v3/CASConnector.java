package com.gpaer.service.v3;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by neo on 19/10/2017.
 * <p>
 * 模拟 CAS 登录目标客户端服务，返回对应的会话 ID 和 CAS TGC 票据
 * <p>
 * <不依赖任何自有类，独立文件>
 */
@Service
public class CASConnector {


    /**
     * @param username
     * @param password
     * @param redirectUrl 目标地址（可以为空）
     * @return
     * @throws Exception
     */
    public CASResult genLogin(String username, String password, String redirectUrl) throws Exception {
        // 创建客户端
        CookieStore httpCookieStore = new BasicCookieStore();
        CloseableHttpClient client = createHttpClientWithNoSsl(httpCookieStore);

        /* 第一次请求[GET] 拉取流水号信息 */
        HttpGet request = new HttpGet("https://cas.sustc.edu.cn/cas/login?service=" + redirectUrl);
        HttpResponse response = client.execute(request);

        Document htmlPage = Jsoup.parse(readResponse(response));
        Element form = htmlPage.select("#fm1").first();
        String execution = form.select("[name=execution]").first().val();
        String _eventId = "submit";
        String submit = "登录";

        /* 第二次请求[POST] 发送表单验证信息 */
        HttpPost request2 = new HttpPost("https://cas.sustc.edu.cn/cas/login");
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("execution", execution));
        params.add(new BasicNameValuePair("_eventId", _eventId));
        params.add(new BasicNameValuePair("submit", submit));
        request2.setEntity(new UrlEncodedFormEntity(params));
        HttpResponse response2 = client.execute(request2);

        Header headerSetCookie = response2.getFirstHeader("Set-Cookie");
        String TGC = headerSetCookie == null ? null : headerSetCookie.getValue().substring(4, headerSetCookie.getValue().indexOf(";"));  // TGC
        Header headerLocation = response2.getFirstHeader("Location");
        String location = headerLocation == null ? null : headerLocation.getValue();

        /* 第三次请求[GET]，前往 CAS 客户端进行验证获取会话 */
        HttpGet request3 = new HttpGet(location);
        HttpResponse response3 = client.execute(request3);

        CASResult casResult = new CASResult();
        List<Cookie> cookies = httpCookieStore.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie != null && cookie.getName().equals("TGC")) {
                casResult.setTGC(cookie.getValue());
                continue;
            }
            if (cookie != null && cookie.getName().equals("JSESSIONID")) {
                casResult.setJSESSIONID(cookie.getValue());
                continue;
            }
        }
        // set Uri (scheme, host, path, query..)
        casResult.setUri(request3.getURI());
        /* 第四次请求[GET]，获取目标页面内容 */
        /* 该获取移交由用户自行操作，不在此做多余请求 */

        return casResult;
    }

    /* 读取 response body 内容为字符串 */
    private String readResponse(HttpResponse response) throws IOException {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));
        String result = new String();
        String line;
        while ((line = in.readLine()) != null) {
            result += line;
        }
        return result;
    }


    /**
     * 创建模拟客户端（针对 https 客户端禁用 SSL 验证）
     *
     * @param cookieStore 缓存的 Cookies 信息
     * @return
     * @throws Exception
     */
    private CloseableHttpClient createHttpClientWithNoSsl(CookieStore cookieStore) throws Exception {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        // don't check
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        // don't check
                    }
                }
        };

        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(null, trustAllCerts, null);
        LayeredConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(ctx);
        return HttpClients.custom()
                .setSSLSocketFactory(sslSocketFactory)
                .setDefaultCookieStore(cookieStore == null ? new BasicCookieStore() : cookieStore)
                .build();
    }

    /**
     * 最终返回结果集
     */
    public class CASResult {
        /* 认证通过则返回 TGC，否则为空 */
        private String TGC;
        private String JSESSIONID;  // 会话 ID
        private URI uri;     // 包含 (scheme, host, path, query..)

        public String getTGC() {
            return TGC;
        }

        public void setTGC(String TGC) {
            this.TGC = TGC;
        }

        public String getJSESSIONID() {
            return JSESSIONID;
        }

        public void setJSESSIONID(String JSESSIONID) {
            this.JSESSIONID = JSESSIONID;
        }

        public URI getUri() {
            return uri;
        }

        public void setUri(URI uri) {
            this.uri = uri;
        }
    }

}
