package com.luoyan.crm.workbench.service;

import com.luoyan.crm.vo.PaginationVO;
import com.luoyan.crm.workbench.domain.Activity;

import java.util.Map;

public interface ActivityService {
    boolean save(Activity a);

    PaginationVO<Activity> pageList(Map<String, Object> map);

    boolean delete(String[] ids);

    Map<String, Object> getUserListAndActivity(String id);

    boolean update(Activity a);
}
