package com.zcs.yunjia.sso.service.impl;

import com.zcs.yunjia.common.pojo.LoginResult;
import com.zcs.yunjia.common.pojo.RequestResult;
import com.zcs.yunjia.common.utils.JsonUtils;
import com.zcs.yunjia.mapper.TbUserMapper;
import com.zcs.yunjia.pojo.TbUser;
import com.zcs.yunjia.pojo.TbUserExample;
import com.zcs.yunjia.sso.redis.RedisClient;
import com.zcs.yunjia.sso.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private TbUserMapper userMapper;
    @Autowired
   private RedisClient redisClient;

    /**
     * 根据用户名查找用户
     * @param username 同户名
     * @return 根据用户名查找到的用户 未查询到返回空
     */
    public TbUser getUserByName(String username){
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<TbUser> users = userMapper.selectByExample(example);
        return users.size() == 0 ? null : users.get(0);
    }

    /**
     * 登录校验
     * @param username 用户名
     * @param password 密码
     * @return LoginResult
     */
    public LoginResult login(String username, String password) {
        LoginResult result = new LoginResult();
        TbUser user = getUserByName(username);
        if (user != null) {
            if (user.getPassword().equals(password)) {
                //登录成功 生成token并写入redis中
                String token = UUID.randomUUID().toString();
                redisClient.set(token+":token", JsonUtils.objectToJson(user));
                //设置过期时间
                redisClient.expire(token+":token",15*60);
                result.setState(1);
                result.setMsg(token);
            } else {
                result.setState(0);
                result.setMsg("密码错误");
            }
        } else {
            result.setState(0);
            result.setMsg("用户名不存在");
        }
        return result;
    }

    /**
     *
     * @param param 要检验的数据
     * @param type  检验数据类型 1=用户名 2=电话 3=邮箱
     * @return true数据可用 false数据不可用
     */
    public boolean checkData(String param,String type){
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria =  example.createCriteria();
        //设置条件
        switch(type){
            case "1" :
                criteria.andUsernameEqualTo(param);
                break;
            case "2" :
                criteria.andPhoneEqualTo(param);
                break;
            case "3" :
                criteria.andEmailEqualTo(param);
                break;
        }
        return userMapper.selectByExample(example).size() != 0 ? false : true;
    }

    /**
     * 根据token检验用户是否登录
     * @param token
     * @return true已登录  false未登录
     */
    public RequestResult checkToken(String token){
        RequestResult result = new RequestResult();
        //查询token是否存在
        String json = redisClient.get(token+":token");
        if(json == "" || json == null){
            System.out.println("token过期啦");
            result.setStatus(400);
            return result;
        }else{
            //校验成功重新设置过期时间
            redisClient.expire(token+":token",15*60);
            result.setStatus(200);
            result.setData(JsonUtils.jsonToPojo(json,TbUser.class));
            return result;
        }
    }

    /**
     * 用户注册
     * @param user 前段封装的用户数据
     * @return 注册结结果
     */
    public RequestResult register(TbUser user){
        int i = userMapper.insert(user);
        RequestResult result = new RequestResult();
        if(i == 1){
            result.setStatus(200);
            result.setData(JsonUtils.objectToJson(user));
        }else{
            result.setStatus(400);
            result.setData("注册失败");
        }
        return result;
    }
    public RequestResult logout(String token){
        RequestResult result = new RequestResult();
        //删除redis中的token
        Long id = redisClient.del(token+":token");
        int states = id == null ? 400 : 200;
        result.setStatus(states);
        return result;
    }
}
