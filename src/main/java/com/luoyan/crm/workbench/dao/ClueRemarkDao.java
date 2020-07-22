package com.luoyan.crm.workbench.dao;

import com.luoyan.crm.workbench.domain.Activity;
import com.luoyan.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {

    List<ClueRemark> getListByClueId(String clueId);

    int delete(ClueRemark clueRemark);
}
