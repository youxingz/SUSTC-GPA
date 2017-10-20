package com.gpaer.service.v3;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gpaer.service.v3.util.ReadResponseBodyUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by neo on 20/10/2017.
 * <p>
 * 获取已修读完成的科目
 */
@Service
public class ProcessHtml2JSONFinishService {
    static final String url = "http://jwxt.sustc.edu.cn/jsxsd/kscj/cjcx_list";

    public JSONArray process2JSON(String JESSIONID) {
        List<String> myTerms = getTermNames();
        JSONArray myDatas = new JSONArray();
        for (String termOne : myTerms) {
            JSONObject jo = getOneTermData(termOne, JESSIONID);
            if (jo != null) {
                myDatas.add(jo);
            }
        }
        return myDatas;
    }

    private List getTermNames() {
        ArrayList<String> myTerms = new ArrayList<>();
        Calendar now = Calendar.getInstance();
        int current_year = now.get(Calendar.YEAR);
        int start_year = 2010;
        for (int i = start_year; i <= current_year; i++) {
            myTerms.add(i + "-" + (i + 1) + "-" + 1);
            myTerms.add(i + "-" + (i + 1) + "-" + 2);
            myTerms.add(i + "-" + (i + 1) + "-" + 3);
        }
//        myTerms.add("2016-2017-1");
        return myTerms;
    }


    private JSONObject getOneTermData(String term, String JESSIONID) {
        HashMap<String, String> params = new HashMap<>();
        params.put("kksj", term);
        params.put("kckz", null);
        params.put("kcmz", null);
        params.put("xsfs", "all");
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(url + "?kksj=" + term + "&xsfs=all");

            request.setHeader("Cookie", "JSESSIONID=" + JESSIONID);
            HttpResponse response = client.execute(request);
            JSONArray jaData = parseHTML(ReadResponseBodyUtil.readResponse(response));
            if (jaData == null || jaData.size() == 0) return null;
            else {
                JSONObject jo = new JSONObject();
                jo.put("term", term);
                jo.put("data", jaData);
                return jo;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param gpadoc
     * @return data from one page
     */
    private JSONArray parseHTML(String gpadoc) {
        if (gpadoc == null)
            return null;
        Document doc = Jsoup.parse(gpadoc);
        Elements els = doc.select("#dataList");
        Element tbody = els.get(0).getElementsByTag("tbody").get(0);
        JSONArray jo = parseChengji(tbody);
        return jo;
    }

    private JSONArray parseChengji(Element tbody) {
        Elements trs = tbody.children();

        JSONArray databody = new JSONArray();
        for (int i = 1; i < trs.size(); i++) {
            Element tr = trs.get(i);
            if (tr.children().size() <= 1) {
                // 是小标题或空元素
                continue;
            }
            if (tr.children().get(0).html().equals("&nbsp;")) {
                // 空值
                continue;
            }
            JSONObject joline = new JSONObject();
            Elements tds = tr.children();
            joline.put("id", tds.get(0).html());//序號
            joline.put("time", tds.get(1).html());//開學時間
            joline.put("courseid", tds.get(2).html());//課程編號
            joline.put("coursename", tds.get(3).html());//課程名稱
            Element a = tds.get(4).getElementsByTag("a").get(0);
            joline.put("score", getScore(a));//成績
            joline.put("comment", a.html());//评价
            joline.put("credit", tds.get(5).html());//學分
            joline.put("period", tds.get(6).html());//縂學時
            joline.put("kaohe", tds.get(7).html());//考核方式
            joline.put("shuxing", tds.get(8).html());//課程屬性
            joline.put("xingzhi", tds.get(9).html());//課程性質

            databody.add(joline);
        }

        return databody;
    }

    private String getScore(Element hrefstr) {
        try {
            String href = hrefstr.attr("href");
            String[] hh = href.split("zcj=");
            String[] rr = hh[1].split("'");
            return rr[0];
        } catch (Exception e) {
        }
        return 0 + "";
    }
}
