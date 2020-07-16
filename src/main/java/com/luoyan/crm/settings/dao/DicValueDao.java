package com.luoyan.crm.settings.dao;

import com.luoyan.crm.settings.domain.DicValue;

import java.util.List;

public interface DicValueDao {
    List<DicValue> getListByCode(String code);
}
