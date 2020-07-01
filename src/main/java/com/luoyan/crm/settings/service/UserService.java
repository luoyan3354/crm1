package com.luoyan.crm.settings.service;

import com.luoyan.crm.exception.LoginException;
import com.luoyan.crm.settings.domain.User;

import java.util.List;

public interface UserService {
    User login(String loginAct, String loginPwd, String ip) throws LoginException;

    List<User> getUserList();
}
