package com.gpaer.service.v3;

import com.alibaba.fastjson.JSONObject;
import com.gpaer.service.util.HttpUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by neo on 16/03/2017.
 * <p>
 * 提供在修读课程信息
 * <p>
 * 以及个人昵称
 */
@Service
public class ProcessHtml2JSONUserService {
    static final String url = "http://jwxt.sustc.edu.cn/jsxsd/xxwcqk/xxwcqkOnkclb.do";

    @Autowired
    HttpUtil httpUtil;


    public JSONObject process2JSON(String JESSIONID) {
        try {
            String doc = httpUtil.apachehttpJWXTPage(url, JESSIONID);
            return parseHTML(doc);
        } catch (Exception e) {
            return null;
        }
    }


    private JSONObject parseHTML(String gpadoc) {
        if (gpadoc == null)
            return null;
        Document doc = Jsoup.parse(gpadoc);
        Element els = doc.select("#Top1_divLoginName").first();
        String name = els.html();
        // 对昵称规整化一下
        if (name == null) {
            name = "佚名(11510000)";
        }
        int length = name.length();
        String nickname = name.substring(0, length - 10);
        String sid = name.substring(length - 9, length - 1);

        JSONObject jo = new JSONObject();
        jo.put("name", nickname);
        jo.put("sid", sid);
        return jo;
    }

}
