package com.gpaer.controller;

import com.gpaer.dao.GPALogMapper;
import com.gpaer.model.GPALog;
import com.gpaer.service.QueryScoreService;
import com.gpaer.service.TurnBackUtil;
import com.gpaer.service.util.HandleTermService;
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
public class QueryScoreController {
    Logger logger = Logger.getLogger(QueryScoreController.class);

    @Autowired
    QueryScoreService queryScoreService;
    @Autowired
    TurnBackUtil turnBackUtil;

    @Autowired
    GPALogMapper gpaLogMapper;


    @Autowired
    HandleTermService handleTermService;

    @RequestMapping(value = "/mygpa0", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public String queryScore(@RequestParam("username") String username, @RequestParam("password") String password) {
//        long time = System.currentTimeMillis();
        // return a json.
        String result = null;
        try {
            result = queryScoreService.queryScore(username, password);
//            logger.info(">>>user leaving : " + username + "\ttime cost : " + (System.currentTimeMillis() - time) + " ms");
            return result;
        } catch (IOException e) {
            logger.error(e.getMessage());
            return turnBackUtil.formIt(1001, "获取成绩失败，服务器异常，请稍后重新尝试", null);
        }
    }

//    @RequestMapping("test/{username}/{password}")
//    public String querysScore(@PathVariable("username") String username, @PathVariable("password") String password) {
//        return handleTermService.process(username, password);
//    }

    @RequestMapping(value = "queryscorebyitem", method = RequestMethod.POST)
    public String queryscorebyitem(HttpServletRequest request, @RequestParam("username") String username, @RequestParam("password") String password) {
//        long time = System.currentTimeMillis();
        String result = handleTermService.process(username, password);
//        try {
//            String ipAddress = request.getHeader("X-FORWARDED-FOR");
//            if (ipAddress == null) {
//                ipAddress = request.getRemoteAddr();
//            }
//            GPALog log = new GPALog();
//            log.setSid(username);
//            log.setIp(ipAddress);
////            log.setServer("sustcGPA server 172.18.4.52 gpa.sustc.edu.cn");
//            log.setServer("localhost server 127.0.0.1 debug");
//            log.setDelaytime((int) (System.currentTimeMillis() - time));
//            log.setTime(new Date());
//            gpaLogMapper.insert(log);
//        } catch (Exception e) {
//
//        }
        return result;
    }
}
