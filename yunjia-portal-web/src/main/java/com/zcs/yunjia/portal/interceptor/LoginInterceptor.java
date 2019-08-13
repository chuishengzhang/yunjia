package com.zcs.yunjia.portal.interceptor;

import com.zcs.yunjia.common.pojo.RequestResult;
import com.zcs.yunjia.common.utils.CookieUtils;
import com.zcs.yunjia.pojo.TbUser;
import com.zcs.yunjia.sso.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;
    @Value("${SSO_URL}")
    private String ssoUrl;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //判断用户是否登录
        String tokenJson = CookieUtils.getCookieValue(httpServletRequest,"userToken");
        String token = tokenJson == null ? "" : tokenJson.split(":")[0];
        RequestResult result = userService.checkToken(token);
        if(result.getStatus() != 200 && result.getData() == null){
            httpServletResponse.sendRedirect(ssoUrl+"?redirect="+httpServletRequest.getRequestURL());
            return false;
        }
        //已登录
        TbUser user = (TbUser)result.getData();
        httpServletRequest.setAttribute("user",user);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
