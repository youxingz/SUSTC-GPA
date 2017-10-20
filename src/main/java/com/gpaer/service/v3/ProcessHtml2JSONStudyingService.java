package com.gpaer.service.v3;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gpaer.service.util.HttpUtil;
import com.gpaer.service.v3.util.ReadResponseBodyUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by neo on 20/10/2017.
 * <p>
 * 提供在修读课程信息
 * <p>
 * 以及个人昵称
 */
@Service
public class ProcessHtml2JSONStudyingService {
    static final String url = "http://jwxt.sustc.edu.cn/jsxsd/xxwcqk/xxwcqkOnkclb.do";

    @Autowired
    HttpUtil httpUtil;


    public JSONArray process2JSON(String JESSIONID) {
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(url);
            request.setHeader("Cookie", "JSESSIONID=" + JESSIONID);
            HttpResponse response = client.execute(request);
            return parseHTML(ReadResponseBodyUtil.readResponse(response));
        } catch (Exception e) {
            return null;
        }
    }


    private JSONArray parseHTML(String gpadoc) {
        if (gpadoc == null)
            return null;
        Document doc = Jsoup.parse(gpadoc);
        Elements els = doc.select("table.Nsb_r_list");
        Element xuefen = els.get(0).child(0);
        Element chengji = els.get(1).child(0);

        JSONArray jo = parseChengji(chengji);
        return jo;
    }


    private JSONArray parseChengji(Element tbody) {
        Elements trs = tbody.children();
        JSONArray lines = new JSONArray();
        for (int i = 1; i < trs.size(); i++) {
            Element tr = trs.get(i);
            if (tr.children().size() == 1) {
                // 是小标题
                continue;
            }
            if (tr.children().get(0).html().equals("&nbsp;")) {
                // 空值
                continue;
            }
            Elements tds = tr.children();
            Element ele = tds.get(5);
            Elements childfont = ele.getElementsByTag("font");
            if (childfont != null && childfont.html().contains("修读中")) {
                JSONObject jo = new JSONObject();
                jo.put("courseid", tds.get(0).html());
                jo.put("coursename", tds.get(1).html());
                jo.put("xuefen", tds.get(2).html());
                jo.put("shuxing", tds.get(3).html());
                lines.add(jo);
            }
        }
        return lines;
    }
}
