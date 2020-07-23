package com.luoyan.crm.web.listener;

import com.luoyan.crm.settings.domain.DicValue;
import com.luoyan.crm.settings.service.DicService;
import com.luoyan.crm.settings.service.impl.DicServiceImpl;
import com.luoyan.crm.utils.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

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

        //---------------------------------------------------------------------------------------------------
        //处理Stage2Possibility.properties文件
        /*
            处理步骤
                1.解析这个文件，将文件中的键值对关系处理成为Java中的键值对关系（map）
                2.pMap保存值之后，放在服务器缓存中
         */

        Map<String,String> pMap = new HashMap<String,String>();

        ResourceBundle rb = ResourceBundle.getBundle("Stage2Possibility");//相当于已经转成了Java中的map

        Enumeration<String> e = rb.getKeys();

        while(e.hasMoreElements()){

            //取阶段
            String key = e.nextElement();
            //取可能性
            String value = rb.getString(key);

            pMap.put(key,value);

        }

        //将pMap保存到服务器缓存中
        application.setAttribute("pMap",pMap);

    }
}
