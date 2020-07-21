package com.luoyan.crm.workbench.dao;

import com.luoyan.crm.workbench.domain.Clue;
import com.luoyan.crm.workbench.service.ClueService;

public interface ClueDao {


    int save(Clue c);

    Clue detail(String id);

    Clue getById(String clueId);
}
