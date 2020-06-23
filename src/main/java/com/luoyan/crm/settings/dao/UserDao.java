package com.luoyan.crm.settings.dao;

import com.luoyan.crm.settings.domain.User;

import java.util.Map;

public interface UserDao {
    User login(Map<String, String> map);
}
