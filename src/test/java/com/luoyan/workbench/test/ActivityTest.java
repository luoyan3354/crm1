package com.luoyan.workbench.test;

import com.luoyan.crm.utils.ServiceFactory;
import com.luoyan.crm.utils.UUIDUtil;
import com.luoyan.crm.workbench.domain.Activity;
import com.luoyan.crm.workbench.service.ActivityService;
import com.luoyan.crm.workbench.service.impl.ActivityServiceImpl;
import org.junit.Assert;
import org.junit.Test;

//测试类的类名为domain+Test
public class ActivityTest {

    @Test//测试方法的方法名一般设置为test+功能名
    public void testSave(){

        Activity a = new Activity();
        a.setId(UUIDUtil.getUUID());
        a.setName("宣传推广会");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.save(a);

        //断言。假设：左侧是运行的真实结果，右边是我预判的结果。结果一致，则是绿√
        Assert.assertEquals(flag,true);

    }


}
