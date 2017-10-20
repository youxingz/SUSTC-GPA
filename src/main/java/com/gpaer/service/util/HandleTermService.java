package com.gpaer.service.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gpaer.service.TurnBackUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by admin on 2017/3/15.
 * <p>
 * 新版系统 v2.0 使用，可获取分学期查询结果
 */
@Deprecated
@Service
public class HandleTermService {
    static final String url = "http://jwxt.sustc.edu.cn/jsxsd/kscj/cjcx_list";
    @Autowired
    ProcessJID processJID;
    @Autowired
    ProcessHTML processHTML;
    @Autowired
    HttpUtil httpUtil;

    @Autowired
    HandleUserInfoService handleUserInfoService;
    @Autowired
    HandleZaiXiuCourseService handleZaiXiuCourseService;


    @Autowired
    TurnBackUtil turnBackUtil;

    public String process(String username, String password) {
        try {
            String JESSIONID = processJID.getJESSIONID(username, password);
            if (JESSIONID != null) {
                JSONObject info = handleUserInfoService.process(JESSIONID);//昵称等信息
                JSONArray ja1 = getMyList(JESSIONID);//已修读科目
                JSONArray ja2 = handleZaiXiuCourseService.process(JESSIONID);// 在修读科目


                JSONObject jo = new JSONObject();
                jo.put("info", info);
                jo.put("ed", ja1);
                jo.put("ing", ja2);
                return turnBackUtil.formIt(1000, "獲取數據成功", jo);
            } else {
                return turnBackUtil.formIt(1002, "請輸入正確的用戶名密碼", null);
            }
        } catch (Exception e) {
            return turnBackUtil.formIt(1001, "服務器異常", null);
        }
    }

    private JSONArray getMyList(String JESSIONID) {
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

    private JSONObject getOneTermData(String term, String JESSIOND) {
        HashMap<String, String> params = new HashMap<>();
        params.put("kksj", term);
        params.put("kckz", null);
        params.put("kcmz", null);
        params.put("xsfs", "all");
        try {
            String resultHTML = httpUtil.apachehttpJWXTPage(url + "?kksj=" + term + "&xsfs=all", JESSIOND);
            JSONArray jaData = processHTML.processHTML(resultHTML);
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
}
