package com.zcs.yunjia.order.exception;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GlobalExceptionResolver  implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("message","系统出错了...请稍后再试。");
        System.out.println("-------------------------------------------------------------");
        System.out.print("order-web-exception:");
        e.printStackTrace();
        System.out.println("-------------------------------------------------------------");
        mav.setViewName("/error/exception");
        return mav;
    }
}
