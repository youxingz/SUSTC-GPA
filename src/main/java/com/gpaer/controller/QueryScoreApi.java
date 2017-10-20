package com.gpaer.controller;

import com.gpaer.dao.GPALogMapper;
import com.gpaer.model.GPALog;
import com.gpaer.service.QueryScoreService;
import com.gpaer.service.TurnBackUtil;
import com.gpaer.service.util.HandleTermService;
import com.gpaer.service.v3.QueryItemScoreService;
import com.gpaer.service.v3_plain.QueryPlainScoreService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

/**
 * Created by neo on 21/01/2017.
 */
@RestController
@RequestMapping("/")
public class QueryScoreApi {

    @Autowired
    TurnBackUtil turnBackUtil;

    //    @Autowired
//    QueryScoreService queryScoreService;// 第一版 Plain Version
    @Autowired
    QueryPlainScoreService queryPlainScoreService;// 第三版 Plain


    //    @Autowired
//    HandleTermService handleTermService;// 第二版 Material Design
    @Autowired
    QueryItemScoreService queryItemScoreService; // 第三版 Material Design

    @RequestMapping(value = "/mygpa0", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public String queryScore(@RequestParam("username") String username, @RequestParam("password") String password) {
        return queryPlainScoreService.queryScore(username, password);
    }

    @RequestMapping(value = "queryscorebyitem", method = RequestMethod.POST)
    public String queryscorebyitem(@RequestParam("username") String username, @RequestParam("password") String password) {
        String result = queryItemScoreService.process(username, password);
        return result;
    }
}
