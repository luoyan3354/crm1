package com.luoyan.crm.workbench.web.controller;

import com.luoyan.crm.settings.domain.User;
import com.luoyan.crm.settings.service.UserService;
import com.luoyan.crm.settings.service.impl.UserServiceImpl;
import com.luoyan.crm.utils.DateTimeUtil;
import com.luoyan.crm.utils.PrintJson;
import com.luoyan.crm.utils.ServiceFactory;
import com.luoyan.crm.utils.UUIDUtil;
import com.luoyan.crm.vo.PaginationVO;
import com.luoyan.crm.workbench.domain.Activity;
import com.luoyan.crm.workbench.service.ActivityService;
import com.luoyan.crm.workbench.service.impl.ActivityServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入市场活动控制台");
        String path = request.getServletPath();//为什么这里还需要再拿到请求路径？
        System.out.println(path);

        if("/workbench/activity/getUserList.do".equals(path)){
            getUserList(request,response);
        }else if("/workbench/activity/save.do".equals(path)){
            save(request,response);
        }else if("/workbench/activity/pageList.do".equals(path)){
            pageList(request,response);
        }

    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("补市场活动数据给前端空白");

        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String pageNoStr = request.getParameter("pageNo");
        String pageSizeStr = request.getParameter("pageSize");

        //sql语句后面LIMIT两个参数（略过的前几个数，取出几个数）
        //取出的个数
        int pageSize = Integer.valueOf(pageSizeStr);
        //略过的记录数
        int pageNo = Integer.valueOf(pageNoStr);
        int skipCount = (pageNo-1)*pageSize;

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        //返回一个封装着List（市场活动信息）和总记录数的vo对象（前端需要的）
        PaginationVO<Activity> vo = as.pageList(map);
        //vo-->前端
        PrintJson.printJsonObj(response,vo);



    }

    private void save(HttpServletRequest request, HttpServletResponse response) {
         System.out.println("执行市场活动提交表单到后台");

         //从前端传递过来的参数获取数据
        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        //创建时间：当前系统时间
        String createTime = DateTimeUtil.getSysTime();
        //创建人：当前登录用户
        String createBy = ((User)request.getSession().getAttribute("user")).getName();

        Activity a = new Activity();
        a.setId(id);
        a.setOwner(owner);
        a.setName(name);
        a.setStartDate(startDate);
        a.setEndDate(endDate);
        a.setCost(cost);
        a.setDescription(description);

        ActivityService as = (ActivityService)ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = as.save(a);
        PrintJson.printJsonFlag(response,flag);


    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("取得用户信息列表");
        //这里会用到tbl_user数据表，所以用的service层是UserService
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> ulist = us.getUserList();
        PrintJson.printJsonObj(response,ulist);

    }


}
