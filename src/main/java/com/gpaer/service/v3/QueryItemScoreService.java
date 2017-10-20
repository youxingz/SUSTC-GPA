package com.gpaer.service.v3;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gpaer.service.TurnBackUtil;
import com.gpaer.service.util.HandleUserInfoService;
import com.gpaer.service.util.HandleZaiXiuCourseService;
import com.gpaer.service.util.HttpUtil;
import com.gpaer.service.util.ProcessHTML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by neo on 20/10/2017.
 * <p>
 * 集合新旧系统的版本，返回对应 json
 */
@Service
public class QueryItemScoreService {
    static final String url = "http://jwxt.sustc.edu.cn/jsxsd/kscj/cjcx_list";


    @Autowired
    CASConnector casConnector;
    @Autowired
    ProcessHtml2JSONUserService processHtml2JSONUserService;
    @Autowired
    ProcessHtml2JSONFinishService processHtml2JSONFinishService;
    @Autowired
    ProcessHtml2JSONStudyingService processHtml2JSONStudyingService;

    @Autowired
    TurnBackUtil turnBackUtil;

    public String process(String username, String password) {
        try {
            CASConnector casConnector = new CASConnector();
            CASConnector.CASResult casResult = casConnector.genLogin(username, password, url);
            String JESSIONID = casResult.getJSESSIONID();
            if (JESSIONID != null) {
                JSONObject userinfo = processHtml2JSONUserService.process2JSON(JESSIONID);//昵称等信息
                JSONArray ja1 = processHtml2JSONFinishService.process2JSON(JESSIONID);//已修读科目
                JSONArray ja2 = processHtml2JSONStudyingService.process2JSON(JESSIONID);// 在修读科目


                JSONObject jo = new JSONObject();
                jo.put("info", userinfo);
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

}
