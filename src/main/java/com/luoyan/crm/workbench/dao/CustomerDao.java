package com.luoyan.crm.workbench.dao;

import com.luoyan.crm.workbench.domain.Customer;

public interface CustomerDao {

    Customer getCustomerByName(String company);

    int save(Customer cus);
}
