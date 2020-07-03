package com.luoyan.crm.workbench.service.impl;

import com.luoyan.crm.utils.SqlSessionUtil;
import com.luoyan.crm.vo.PaginationVO;
import com.luoyan.crm.workbench.dao.ActivityDao;
import com.luoyan.crm.workbench.dao.ActivityRemarkDao;
import com.luoyan.crm.workbench.domain.Activity;
import com.luoyan.crm.workbench.service.ActivityService;

import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {

    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private ActivityRemarkDao activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);

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

    @Override
    public boolean delete(String[] ids) {
        //市场活动备注表=>学生    市场活动=>班级

        boolean flag = true;

        //查询出需要删除的市场活动备注表的数量
        int count1 = activityRemarkDao.getCountByAids(ids);

        //删除市场活动备注表（实际删除的数量）
        int count2 = activityRemarkDao.deleteByAids(ids);

        //比对一下，看有没有删干净市场活动备注表
        if(count1 != count2){
            flag = false;
        }

        //删除市场活动表
        int count3 = activityDao.delete(ids);
        if(count3 != ids.length){
            flag = false;
        }

        return flag;

    }
}
