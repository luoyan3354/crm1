package com.luoyan.crm.workbench.service;

import com.luoyan.crm.workbench.domain.Clue;
import com.luoyan.crm.workbench.domain.Tran;

public interface ClueService {


    boolean save(Clue c);

    Clue detail(String id);

    boolean unbond(String id);

    boolean bund(String cid, String[] aids);

    boolean convert(String clueId, Tran t, String createBy);
}
