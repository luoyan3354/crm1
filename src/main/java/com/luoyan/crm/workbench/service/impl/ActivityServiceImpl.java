package com.luoyan.crm.workbench.service.impl;

import com.luoyan.crm.utils.SqlSessionUtil;
import com.luoyan.crm.vo.PaginationVO;
import com.luoyan.crm.workbench.dao.ActivityDao;
import com.luoyan.crm.workbench.domain.Activity;
import com.luoyan.crm.workbench.service.ActivityService;

import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {

    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);

    @Override
    public boolean save(Activity a) {
        boolean flag = true;

        //dao层发生了异常，是否还会返回值？
        int count = activityDao.save(a);
        if(count!=1){
            flag = false;
        }

        return flag;
    }

    @Override
    public PaginationVO<Activity> pageList(Map<String, Object> map) {

        //取得total
        int total = activityDao.getTotalByCondition(map);

        //取得dataList
        List<Activity> dataList = activityDao.getActivityListByCondition(map);

        //创建一个vo对象，将total和dataList封装到vo中
        PaginationVO<Activity> vo = new PaginationVO<Activity>();
        vo.setTotal(total);
        vo.setDataList(dataList);

        //将vo返回
        return vo;

    }
}
