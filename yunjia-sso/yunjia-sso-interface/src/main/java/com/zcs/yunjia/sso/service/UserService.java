package com.zcs.yunjia.sso.service;

import com.zcs.yunjia.common.pojo.LoginResult;
import com.zcs.yunjia.pojo.TbUser;

/**
 * 用户接口
 */
public interface UserService {

    public TbUser getUserByName(String username);

    public LoginResult login(String username,String password);

    public boolean checkData(String param,String type);

}
