package com.seeu.config;


import com.gpaer.dao.GPALogMapper;
import com.gpaer.model.GPALog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * Created by neo on 23/02/2017.
 */
public class HtmlPageInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    GPALogMapper gpaLogMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handle) throws IOException {
        request.setAttribute("str", System.currentTimeMillis());// set start time

        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type", "text/json;charset=UTF-8");//这句话是解决乱码的
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
            throws Exception {
        // TODO Auto-generated method stub
    }

    /**
     * 每访问一次都会有 3 次 /errer，暂时不做拦截
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // TODO Auto-generated method stub
        try {
            String username = "HTML Page";
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                if ("username".equals(cookie.getName())) {
                    username = cookie.getValue();
                    break;
                }
            }
            long str = Long.parseLong(request.getAttribute("str").toString());
            String ipAddress = request.getHeader("X-FORWARDED-FOR");
            if (ipAddress == null) {
                ipAddress = request.getRemoteAddr();
            }
            GPALog log = new GPALog();
            log.setSid(username);
            log.setIp(ipAddress);
            log.setServer("sustcGPA server 172.18.4.52 gpa.sustc.edu.cn");
//            log.setServer("localhost server 127.0.0.1 debug");
            log.setDelaytime((int) (System.currentTimeMillis() - str));
            log.setTime(new Date());
            log.setUrl(request.getRequestURI());
            gpaLogMapper.insert(log);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
