package com.luoyan.settings.test;

import com.luoyan.crm.utils.DateTimeUtil;
import com.luoyan.crm.utils.MD5Util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Test1 {
    public static void main(String[] args) {
        //让当前时间变成自己想要的格式（最后是以String的形式）
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = sdf.format(date);

        //验证时间是否有效
        String expireTime = "2023-06-18 07:07:07";//假设是从数据库里获取的失效时间
        String currentTime = DateTimeUtil.getSysTime();//从工具类中获取的当前系统时间
        int count = expireTime.compareTo(currentTime);//用数据库里的时间调compareTo(),若大于0，则有效，否则无效。

        //验证ip是否有效，使用数据库里的多条ip连成的字符串.contains()

        //判断账号是否锁定，用0.equals(数据库里的lockState值),o代表锁头，1代表钥匙。

        //密码通过MD5算法加密（数据库取出来的密码暗文.equals)
        String pwd = "123";
        pwd = MD5Util.getMD5(pwd);
        System.out.println(pwd);
    }
}
