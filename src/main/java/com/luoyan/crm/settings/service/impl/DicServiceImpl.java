package com.luoyan.crm.settings.service.impl;

import com.luoyan.crm.settings.dao.DicTypeDao;
import com.luoyan.crm.settings.dao.DicValueDao;
import com.luoyan.crm.settings.domain.DicType;
import com.luoyan.crm.settings.domain.DicValue;
import com.luoyan.crm.settings.service.DicService;
import com.luoyan.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicServiceImpl implements DicService {

    private DicTypeDao dicTypeDao = SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
    private DicValueDao dicValueDao = SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);

    @Override
    public Map<String, List<DicValue>> getAll() {

        Map<String,List<DicValue>> map = new HashMap<String,List<DicValue>>();

        //将数据字典的 类型 取出
        List<DicType> dtList = dicTypeDao.getTypeList();

        //将字典类型列表遍历 =>拿到每一种type，通过类型拿到List<value>，存入map。
        for(DicType dt:dtList){

            //取得每一种类型
            String code = dt.getCode();

            //根据每一种类型取得字典value列表
            List<DicValue> dvList = dicValueDao.getListByCode(code);

            //存入map
            map.put(code+"List",dvList);

        }

        return map;
    }
}
