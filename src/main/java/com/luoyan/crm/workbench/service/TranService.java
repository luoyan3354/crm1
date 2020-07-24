package com.luoyan.crm.workbench.service;

import com.luoyan.crm.workbench.domain.Tran;

public interface TranService {
    boolean save(Tran t, String customerName);
}
