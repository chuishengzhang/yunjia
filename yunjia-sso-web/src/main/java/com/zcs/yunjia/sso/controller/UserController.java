package com.zcs.yunjia.sso.controller;

import com.zcs.yunjia.common.pojo.LoginResult;
import com.zcs.yunjia.common.pojo.RequestResult;
import com.zcs.yunjia.common.utils.CookieUtils;
import com.zcs.yunjia.common.utils.JsonUtils;
import com.zcs.yunjia.pojo.TbUser;
import com.zcs.yunjia.sso.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    /*展示首页
    * */
    @RequestMapping("/")
    public String showIndex(){
        return "login";
    }

    /**
     * 跳转页面
     * @param page
     */
    @RequestMapping(value = "/{page}",method = RequestMethod.GET)
    public String showPage(@PathVariable("page") String page){
        return page;
    }

    /**
     * 用户登录校验
     * @param username 用户名
     * @param password 密码
     * @return LoginResult
     */
    @RequestMapping(value="/user/login",method = RequestMethod.POST)
    @ResponseBody
    public LoginResult validateUser(HttpServletRequest request, HttpServletResponse response, @RequestParam String username, @RequestParam String password){
        LoginResult result = userService.login(username,password);
        if(result.getMsg() != "") {
            CookieUtils.setCookie(request, response, "userToken", result.getMsg());
        }
        return result;
    }

    @RequestMapping(value="/user/check/{param}/{type}",method = RequestMethod.GET)
    @ResponseBody
    public boolean checkData(@PathVariable("param") String param,@PathVariable("type") String type ){
        return userService.checkData(param,type);
    }

    @RequestMapping("/check/{token}")
    @ResponseBody
    public String checkToken(@PathVariable String token,@RequestParam String callback){
        String json = JsonUtils.objectToJson(userService.checkToken(token));
        String jsonp = callback+"("+json+")";
        return jsonp;
    }

    @RequestMapping("/user/register")
    @ResponseBody
    public RequestResult register(String username,String password,String phone){
        TbUser user = new TbUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setPhone(phone);
        user.setCreated(new Date());
        user.setUpdated(new Date());
        return userService.register(user);
    }
}
