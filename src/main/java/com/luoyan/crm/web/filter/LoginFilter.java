package com.luoyan.crm.web.filter;

import com.luoyan.crm.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        System.out.println("进入到验证有没有登录的过滤器");

        //从session域中拿user对象，若有的话说明已经登录，没有的话就是没有登录
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        String path = request.getServletPath();

        //不应该被拦截的资源，自动放行请求（登录页和验证登录页跳转的小uri）
        if("/login.jsp".equals(path) || "/settings/user/login.do".equals(path)){
            chain.doFilter(req,resp);

        }else{
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");

            //如果user不为null,说明登录过
            if(user!=null){

                chain.doFilter(req,resp);

                //说明没有登录过
            }else{
                //重定向到登录页，并且项目的名称从request域中拿
                response.sendRedirect(request.getContextPath()+"/login.jsp");
            }
        }




    }
}
