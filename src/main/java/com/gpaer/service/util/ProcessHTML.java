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
 * Created by admin on 2017/3/15.
 * <p>
 * helper for web html file : http://jwxt.sustc.edu.cn/jsxsd/kscj/cjcx_list
 */
@Service
@Deprecated
public class ProcessHTML {

    /**
     * This doc must be : http://jwxt.sustc.edu.cn/jsxsd/kscj/cjcx_list
     *
     * @param htmldoc
     * @return a json object.
     * <p>
     * {
     * term:2016-2017-1
     * [
     * {
     * course_key:CS204,
     * course_name:数字媒体与创意编程,
     * score:86,
     * xuefen:3,
     * shuxing:選修,
     * xingzhi:專業選修,
     * },
     * {
     * key:value,
     * }
     * ]
     * }
     */
    public JSONArray processHTML(String htmldoc) {
        try{
            return parseHTML(htmldoc);
        }catch (Exception e){
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
//        Element xuefen = els.get(0).child(0);
//        Element chengji = els.get(1).child(0);
//        System.out.println(xuefen.html());
//        System.out.println(chengji.html());
//        parseXueFen(xuefen);
        JSONArray jo = parseChengji(tbody);
//        String json = JSONObject.toJSONString(jo);
//        System.out.print(json);
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
//            JSONArray ja = new JSONArray();
            JSONObject joline = new JSONObject();
            Elements tds = tr.children();
            joline.put("id", tds.get(0).html());//序號
            joline.put("time", tds.get(1).html());//開學時間
            joline.put("courseid", tds.get(2).html());//課程編號
            joline.put("coursename", tds.get(3).html());//課程名稱
            Element a = tds.get(4).getElementsByTag("a").get(0);
            joline.put("score", getScore(a));//成績
            joline.put("comment",a.html());//评价
            joline.put("credit", tds.get(5).html());//學分
            joline.put("period", tds.get(6).html());//縂學時
            joline.put("kaohe", tds.get(7).html());//考核方式
            joline.put("shuxing", tds.get(8).html());//課程屬性
            joline.put("xingzhi", tds.get(9).html());//課程性質

            databody.add(joline);
//            for (int j = 0; j < tds.size(); j++) {
//                if (j == 4) {// 分數 取自 a 標簽
//                    Element ele = tds.get(j);
//                    Elements childfont = ele.getElementsByTag("font");
//                    if (childfont == null || childfont.size() == 0) {
//                        ja.add(ele.html());
//                    } else {
//                        ja.add(childfont.html());
//                    }
//                } else
//                    joline.put("", "");
//                ja.add(tds.get(j).html());
//            }
//            databody.add(ja);
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
            //
        }
        return 0 + "";
    }
}
