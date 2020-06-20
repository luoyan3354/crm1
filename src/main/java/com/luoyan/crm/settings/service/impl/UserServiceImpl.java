package com.luoyan.crm.settings.service.impl;

import com.luoyan.crm.settings.dao.UserDao;
import com.luoyan.crm.settings.service.UserService;
import com.luoyan.crm.utils.SqlSessionUtil;

public class UserServiceImpl implements UserService {

    private UserDao usedao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

}
