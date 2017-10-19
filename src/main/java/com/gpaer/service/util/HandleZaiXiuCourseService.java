package com.gpaer.service.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by neo on 16/03/2017.
 *
 * 提供在修读课程信息
 *
 * 以及个人昵称
 */
@Service
public class HandleZaiXiuCourseService {
    static final String url = "http://jwxt.sustc.edu.cn/jsxsd/xxwcqk/xxwcqkOnkclb.do";

    @Autowired
    HttpUtil httpUtil;


    public JSONArray process(String JESSIONID) {
        try {
            String doc = httpUtil.apachehttpJWXTPage(url, JESSIONID);
            return parseHTML(doc);
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
        // add title
//        JSONArray title = new JSONArray();
//        for (Element th : trs.get(0).children()) {
//            String thstr = th.html();
//            title.add(thstr);
////            System.out.println(">>>>"+thstr);
//        }
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
                jo.put("courseid",tds.get(0).html());
                jo.put("coursename",tds.get(1).html());
                jo.put("xuefen",tds.get(2).html());
                jo.put("shuxing",tds.get(3).html());
                lines.add(jo);
            }
        }
//        JSONObject jo = new JSONObject();
//        jo.put("title", title);
//        jo.put("data", lines);
        return lines;
    }
}
