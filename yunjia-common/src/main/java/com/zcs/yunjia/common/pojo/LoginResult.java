package com.zcs.yunjia.common.pojo;

import java.io.Serializable;

/**
 * 登录返回结果
 * state 登录状态 1为校验成功 0为校验失败
 * msg  登录相关信息
 */
public class LoginResult implements Serializable {
    private int state = 0;
    private String msg;

    public int getState() {
        return state;
    }

    public String getMsg() {
        return msg;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "LoginResult{" +
                "state=" + state +
                ", msg='" + msg + '\'' +
                '}';
    }
}
