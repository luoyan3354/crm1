package com.luoyan.crm.workbench.service;

import com.luoyan.crm.workbench.domain.Tran;
import com.luoyan.crm.workbench.domain.TranHistory;

import java.util.List;

public interface TranService {
    boolean save(Tran t, String customerName);

    Tran detail(String id);

    List<TranHistory> getHistoryListByTranId(String tranId);
}
