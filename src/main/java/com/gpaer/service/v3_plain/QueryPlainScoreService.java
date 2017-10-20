package com.gpaer.service.v3_plain;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gpaer.service.TurnBackUtil;
import com.gpaer.service.v3.CASConnector;
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

import java.io.IOException;

/**
 * Created by neo on 20/10/2017.
 * <p>
 * 修复第一版 CAS 登录问题
 */
@Service
public class QueryPlainScoreService {
    private static final String url = "http://jwxt.sustc.edu.cn/jsxsd/xxwcqk/xxwcqkOnkclb.do";

    @Autowired
    CASConnector casConnector;

    @Autowired
    TurnBackUtil turnBackUtil;

    public String queryScore(String username, String password) {
        try {
            JSONObject result = process(username, password);
            if (result == null)
                return turnBackUtil.formIt(1001, "获取成绩失败，请稍后重新尝试", result);
            else
                return turnBackUtil.formIt(1000, "获取成绩成功", result);
        }catch (Exception e){
            return turnBackUtil.formIt(1001, "获取成绩失败，服务器异常，请稍后重新尝试", null);
        }
    }

    private JSONObject process(String username, String password) throws Exception {
        CASConnector.CASResult casResult = casConnector.genLogin(username, password, url);
        String jsessionId = casResult.getJSESSIONID();
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);
        request.setHeader("Cookie", "JSESSIONID=" + jsessionId);
        HttpResponse response = client.execute(request);
        return parseHTML(ReadResponseBodyUtil.readResponse(response));
    }

    private JSONObject parseHTML(String gpadoc) {
        if (gpadoc == null)
            return null;
        Document doc = Jsoup.parse(gpadoc);
        Elements els = doc.select("table.Nsb_r_list");
        Element xuefen = els.get(0).child(0);
        Element chengji = els.get(1).child(0);
        JSONObject jo = parseChengji(chengji);
        return jo;
    }

    private JSONObject parseChengji(Element tbody) {
        Elements trs = tbody.children();
        // add title
        JSONArray title = new JSONArray();
        for (Element th : trs.get(0).children()) {
            String thstr = th.html();
            title.add(thstr);
        }
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
            JSONArray ja = new JSONArray();
            Elements tds = tr.children();
            for (int j = 0; j < tds.size(); j++) {
                if (j == 5) {
                    Element ele = tds.get(j);
                    Elements childfont = ele.getElementsByTag("font");
                    if (childfont == null || childfont.size() == 0) {
                        ja.add(ele.html());
                    } else {
                        ja.add(childfont.html());
                    }
                } else
                    ja.add(tds.get(j).html());
            }
            lines.add(ja);
        }
        JSONObject jo = new JSONObject();
        jo.put("title", title);
        jo.put("data", lines);
        return jo;
    }

}
