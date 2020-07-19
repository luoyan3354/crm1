package com.luoyan.crm.workbench.service.impl;

import com.luoyan.crm.utils.SqlSessionUtil;
import com.luoyan.crm.utils.UUIDUtil;
import com.luoyan.crm.workbench.dao.ClueActivityRelationDao;
import com.luoyan.crm.workbench.dao.ClueDao;
import com.luoyan.crm.workbench.domain.Clue;
import com.luoyan.crm.workbench.domain.ClueActivityRelation;
import com.luoyan.crm.workbench.service.ClueService;

public class ClueServiceImpl implements ClueService {

    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private ClueActivityRelationDao clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);


    @Override
    public boolean save(Clue c) {

        boolean flag = true;

        int count = clueDao.save(c);

        if(count!=1) flag=false;

        return flag;
    }

    @Override
    public Clue detail(String id) {

        Clue c = clueDao.detail(id);
        return c;
    }

    @Override
    public boolean unbond(String id) {

        boolean flag = true;

        int count = clueActivityRelationDao.unbund(id);

        if(count!=1) flag=false;

        return flag;
    }

    @Override
    public boolean bund(String cid, String[] aids) {

        boolean flag = true;

        for(String aid:aids){

            //取得每一个aid和cid做关联
            ClueActivityRelation car = new ClueActivityRelation();
            car.setId(UUIDUtil.getUUID());
            car.setClueId(cid);
            car.setActivityId(aid);

            //添加关联关系表中的记录
            int count = clueActivityRelationDao.bund(car);
            if(count!=1){
                flag = false;
            }

        }

        return flag;
    }
}
