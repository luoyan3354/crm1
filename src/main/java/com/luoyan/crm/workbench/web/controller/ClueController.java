package com.luoyan.crm.workbench.web.controller;

import com.luoyan.crm.settings.domain.User;
import com.luoyan.crm.settings.service.UserService;
import com.luoyan.crm.settings.service.impl.UserServiceImpl;
import com.luoyan.crm.utils.PrintJson;
import com.luoyan.crm.utils.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ClueController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入线索控制台");
        String path = request.getServletPath();//为什么这里还需要再拿到请求路径？
        System.out.println(path);

        if("/workbench/clue/getUserList.do".equals(path)){
            getUserList(request,response);
        }else if("/workbench/clue/.do".equals(path)){
            //save(request,response);
        }

    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("clueController下的取用户信息列表");

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> ulist = us.getUserList();
        PrintJson.printJsonObj(response,ulist);

    }


}
