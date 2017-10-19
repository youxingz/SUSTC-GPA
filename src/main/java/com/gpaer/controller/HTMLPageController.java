package com.gpaer.controller;

import com.gpaer.dao.GPALogMapper;
import com.gpaer.model.GPALog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * Created by neo on 20/01/2017.
 */
@Controller
@RequestMapping("/")
public class HTMLPageController {

    //    @Autowired
//    HandleHTMLService handleHTMLService;
//
//    // 返回 json 成绩信息，本地计算 GPA
//    @RequestMapping("/mygpas")
//    @ResponseBody
//    public String toMainPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        return handleHTMLService.getMyScore(request,response);
//    }
    @Autowired
    GPALogMapper gpaLogMapper;

    @RequestMapping("/")
    public String toLoginPage(HttpServletRequest request) {
        log(request);
        return "md_login";
    }

    @RequestMapping("/gpa")
    public String toGPAPage(HttpServletRequest request) {

        log(request);
        return "md_mygpa";
    }

    @RequestMapping("/oldlogin")
    public String toOLDLoginPage(HttpServletRequest request) {
        log(request);
        return "login";
    }

    @RequestMapping("/mygpa")
    public String toOLDGPAPage(HttpServletRequest request) {
        log(request);
        return "index";
    }

    /* 记录日志信息 */
    private void log(HttpServletRequest request) {
        try {
            String username = "HTML Page";
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                if ("username".equals(cookie.getName())) {
                    username = cookie.getValue();
                    break;
                }
            }
            String ipAddress = request.getHeader("X-FORWARDED-FOR");
            if (ipAddress == null) {
                ipAddress = request.getRemoteAddr();
            }
            GPALog log = new GPALog();
            log.setSid(username);
            log.setIp(ipAddress);
            log.setServer("sustcGPA server 172.18.4.52 gpa.sustc.edu.cn");
//            log.setServer("localhost server 127.0.0.1 debug");
            log.setDelaytime(-1);
            log.setTime(new Date());
            log.setUrl(request.getRequestURI());
            gpaLogMapper.insert(log);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
