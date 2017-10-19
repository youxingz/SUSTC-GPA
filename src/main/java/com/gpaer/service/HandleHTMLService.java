package com.gpaer.service;


import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by neo on 20/01/2017.
 *
 * 弃用
 */
@Deprecated
@Service
public class HandleHTMLService {
    @Autowired
    TurnBackUtil turnBackUtil;

    public String getMyScore(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        String token = getToken(request);
//        String token = "0C70AC0D42600B02D91A1E7AD3CEFD7A";
//        String urll = "http://jwxt.sustc.edu.cn/jsxsd/framework/xsMain.jsp?ticket=";
//        if (token == null) return turnBackUtil.formIt(1001, "cookie过期，请重新登录", null);
//        String url = "http://jwxt.sustc.edu.cn/jsxsd/xxwcqk/xxwcqkOnkcxz.do";
//        String JSESSIONID = token;
//        String result = Http.sendPost(url, "", JSESSIONID);
//        System.out.println(result);
        String result = "";
        String JSESSIONID = handleJWXT2GetJSESSIONID();
        if (JSESSIONID != null) {
            response.addCookie(new Cookie("JSESSIONID", JSESSIONID));
            Map map = getTokenAndTicket(request);// 从用户本地获取的
            if (map != null) {
                String ticket = map.get("ticket").toString();
                result = handleJWXT2CheckAuth(ticket,JSESSIONID);
                if (result != null) {
                    System.out.println(">>>>>JSESSIONID>>>>>" + result);
                }
            }
        }

        return result;
    }

    private String handleJWXT2GetJSESSIONID() throws IOException {
//        String ticket = "ST-17931-e45cPM2W5GTLBEjx0Cev-cas52";
        CookieStore httpCookieStore = new BasicCookieStore();
        HttpClient client = HttpClientBuilder.create().setDefaultCookieStore(httpCookieStore).build();
        HttpGet request = new HttpGet("http://jwxt.sustc.edu.cn/jsxsd/framework/xsMain.jsp");
//        request.setHeader("Cookie", "JSESSIONID=F921125B05BD62FF180C5B3997DE11B3");
        HttpResponse response = client.execute(request);
        BufferedReader in = null;

        String result = "";
        in = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));
        String line;
        while ((line = in.readLine()) != null) {
            result += line;
        }
        System.out.print("||>>>>>>>>>>>>>>\n\n" + result);


        List<org.apache.http.cookie.Cookie> coos = httpCookieStore.getCookies();
        for (org.apache.http.cookie.Cookie coo : coos) {
            if (coo.getName().equals("JSESSIONID")) {
                System.out.println(">>>>>>>>>>>1//JSESSIONID\\\\" + coo.getValue());
                return coo.getValue();
            }
        }
        return null;
    }

    private String handleJWXT2CheckAuth(String ticket,String JSESSIONID) throws IOException {
//        String ticket = "ST-17931-e45cPM2W5GTLBEjx0Cev-cas52";
        CookieStore httpCookieStore = new BasicCookieStore();
        HttpClient client = HttpClientBuilder.create().setDefaultCookieStore(httpCookieStore).build();
        HttpGet request = new HttpGet("http://jwxt.sustc.edu.cn/jsxsd/framework/xsMain.jsp?ticket=" + ticket);
        request.setHeader("Cookie", "JSESSIONID="+JSESSIONID);
        HttpResponse response = client.execute(request);


        BufferedReader in = null;
        String result = "";
//        Header[] headers = response.getAllHeaders();
//        for (Header header : headers) {
//            System.out.println("Key : " + header.getName()
//                    + " ,Value : " + header.getValue());
//        }

        in = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));
        String line;
        while ((line = in.readLine()) != null) {
            result += line;
        }
        System.out.print(">>>>>>>>>>>>>>\n\n" + result);


        List<org.apache.http.cookie.Cookie> coos = httpCookieStore.getCookies();
        for (org.apache.http.cookie.Cookie coo : coos) {
            if (coo.getName().equals("JSESSIONID")) {
                System.out.println(">>>>>>>>>>>>2//JSESSIONID\\\\" + coo.getValue());
                return coo.getValue();
            }
        }
        return result;
    }


    private Map getTokenAndTicket(HttpServletRequest request) {
//        String tokenBeyond = turnBackUtil.formIt(1001, "cookie过期，请重新登录", null);
        String ticket = "";
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return null;
        }
        for (int i = 0; i < cookies.length; i++) {
            String name = cookies[i].getName();
            if (name.equals("ticket")) {
                ticket = cookies[i].getValue();
            }
        }
        if (ticket.equals("") || ticket == null) {
            return null;
        } else {
            Map<String, String> map = new HashMap<>();
            map.put("ticket", ticket);
            return map;
        }

    }

    private void https() throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet("http://mkyong.com");
        HttpResponse response = client.execute(request);

//get all headers
        Header[] headers = response.getAllHeaders();
        for (Header header : headers) {
            System.out.println("Key : " + header.getName()
                    + " ,Value : " + header.getValue());
        }

//get header by 'key'
        String server = response.getFirstHeader("Server").getValue();
    }
}

