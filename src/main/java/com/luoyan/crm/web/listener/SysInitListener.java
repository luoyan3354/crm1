package com.luoyan.crm.web.listener;

import com.luoyan.crm.settings.domain.DicValue;
import com.luoyan.crm.settings.service.DicService;
import com.luoyan.crm.settings.service.impl.DicServiceImpl;
import com.luoyan.crm.utils.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SysInitListener implements ServletContextListener{

    @Override//application域一旦创建完成，就执行下面方法中的代码
    public void contextInitialized(ServletContextEvent event) {

        //System.out.println("上下文域对象创建了");  //测试
        System.out.println("服务器缓存处理数据字典开始");

        //从event参数中取出application域对象
        ServletContext application = event.getServletContext();

        //这里的SysInitListener类  就当做controller，也可以操作service层
        DicService ds = (DicService) ServiceFactory.getService(new DicServiceImpl());


       /* 管业务层要7个List（7代表的是数据字典的类型）
          =>打包成一个map，key为类型的名称，value为List集合的dic_value

        存入application域中时，不直接存map。
        为了前端好拿，直接存成键值对。*/


        Map<String, List<DicValue>> map = ds.getAll();

        Set<String> set = map.keySet();
        for(String key:set){

            application.setAttribute(key,map.get(key));

        }
        System.out.println("服务器缓存处理数据字典结束");

    }
}
