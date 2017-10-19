//package com.seeu.config;
//
//import com.TP;
//import com.TurnBackUtil;
//import com.seeu.userOAuth.model.LoginUser;
//import com.seeu.userOAuth.service.UserFromToken;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.ModelAndView;
//import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
///**
// * Created by neo on 17/01/2017.
// */
//@Component
//public class OAuthHandleInterceptor extends HandlerInterceptorAdapter {
//
//    @Autowired
//    UserFromToken userFromToken;
//
//    @Autowired
//    TurnBackUtil turnBackUtil;
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handle) throws IOException {
//        response.setCharacterEncoding("UTF-8");
//        response.setHeader("Content-Type", "text/json;charset=UTF-8");//这句话是解决乱码的
//        String token = (String) request.getParameter("token");
//        LoginUser user = userFromToken.parseToken(token);
//        if (user == null) {
//            String result = turnBackUtil.formIt(TP.RESCODE_NOAUTH, "无权访问", null);
//            response.getOutputStream().write(result.getBytes());
//            response.setStatus(HttpStatus.OK.value());
//            return false;
//        } else {
//            request.setAttribute("UID", user.getUID());
//        }
//        return true;
//    }
//
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
//            throws Exception {
//        // TODO Auto-generated method stub
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
//            throws Exception {
//        // TODO Auto-generated method stub
//    }
//}
