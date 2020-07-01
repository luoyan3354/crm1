package com.luoyan.crm.settings.service.impl;

import com.luoyan.crm.exception.LoginException;
import com.luoyan.crm.settings.dao.UserDao;
import com.luoyan.crm.settings.domain.User;
import com.luoyan.crm.settings.service.UserService;
import com.luoyan.crm.utils.DateTimeUtil;
import com.luoyan.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {

    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    @Override
    public User login(String loginAct, String loginPwd, String ip) throws LoginException {
        Map<String,String> map = new HashMap<String,String>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);

        User user = userDao.login(map);

        if(user==null){
            throw new LoginException("账号密码错误");
        }

        //如果程序能够成功地执行到该行，说明账号密码正确
        //需要继续向下验证其他3项信息

        //验证失效时间
        String expireTime = user.getExpireTime();
        String currentTime = DateTimeUtil.getSysTime();
        if(expireTime.compareTo(currentTime)<0){
            throw new LoginException("账号时间已过期");
        }

        //判断锁定状态
        String lockState = user.getLockState();
        if("0".equals(lockState)){
            throw new LoginException("账号已锁定");
        }

        //判断ip地址
        String allowIp = user.getAllowIps();
        System.out.println("数据库里的ip是"+allowIp);
        if("".equals(allowIp)){
            System.out.println("数据库里的ip是空串"+allowIp);
        }
        if(allowIp!=null&&!("".equals(allowIp))){//如果数据库里的ip地址为空，也就是说不需要判断来访主机的ip
            if(!allowIp.contains(ip)){
                System.out.println("进入了ip地址受限的地方");
                throw new LoginException("ip地址受限");
            }
        }

        return user;
    }

    @Override
    public List<User> getUserList() {
        List<User> ulist = userDao.getUserList();
        return ulist;
    }
}
