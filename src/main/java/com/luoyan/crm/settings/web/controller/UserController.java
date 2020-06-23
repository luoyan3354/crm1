package com.luoyan.crm.settings.web.controller;

import com.luoyan.crm.settings.domain.User;
import com.luoyan.crm.settings.service.UserService;
import com.luoyan.crm.settings.service.impl.UserServiceImpl;
import com.luoyan.crm.utils.MD5Util;
import com.luoyan.crm.utils.PrintJson;
import com.luoyan.crm.utils.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入用户控制台");
        String path = request.getServletPath();//为什么这里还需要再拿到请求路径？
        System.out.println(path);

        if("/settings/user/login.do".equals(path)){
            login(request,response);
        }else if("settings/user/xxx.do".equals(path)){
            //xxx(request,response);
        }

    }

    private void login(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到验证登录操作");

        //controller层从request拿到账户、密码、ip，给service层。返回成功or失败。
        String loginAct = request.getParameter("loginAct");
        String loginPwd = request.getParameter("loginPwd");
        //将密码的明文形式转换成MD5的密文形式
        loginPwd = MD5Util.getMD5(loginPwd);
        //接收浏览器端的ip地址
        String ip = request.getRemoteAddr();
        System.out.println("---ip:"+ip);

        //未来业务层开发，统一使用代理类形态的接口对象
        //这里用的是李四形态的业务层。走了代理，李四将下面的login方法抛出的异常给catch处理了。正确的应该是将异常往上抛。
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        try{
            User user = us.login(loginAct,loginPwd,ip);
            request.getSession().setAttribute("user",user);

            //如果程序执行到此处，说明业务层没有为controller抛出任何异常
            //表示登录成功
            /*
            需要向前端传递的信息就是一个键值对
                {"success":true}
                //不用Jackson，将字符串 {"success":true}发给前端
                String str = "{\"success\":true}";
                response.getWriter().print(str);

             */
            PrintJson.printJsonFlag(response,true);


        }catch (Exception e){
            e.printStackTrace();

            //一旦程序执行了catch块的信息，说明业务层为我们验证登录失败，为controller抛出了异常
            //表示登录失败
            /*为前端提供的信息
            {"success":false,"msg":?}

             */
            String msg = e.getMessage();
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("success",false);
            map.put("msg",msg);
            PrintJson.printJsonObj(response,map);
        }
    }
}
