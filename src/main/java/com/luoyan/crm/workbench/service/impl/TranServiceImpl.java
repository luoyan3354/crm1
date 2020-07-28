package com.luoyan.crm.workbench.service.impl;

import com.luoyan.crm.utils.DateTimeUtil;
import com.luoyan.crm.utils.SqlSessionUtil;
import com.luoyan.crm.utils.UUIDUtil;
import com.luoyan.crm.workbench.dao.CustomerDao;
import com.luoyan.crm.workbench.dao.TranDao;
import com.luoyan.crm.workbench.dao.TranHistoryDao;
import com.luoyan.crm.workbench.domain.Customer;
import com.luoyan.crm.workbench.domain.Tran;
import com.luoyan.crm.workbench.domain.TranHistory;
import com.luoyan.crm.workbench.service.TranService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranServiceImpl implements TranService {

    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

    @Override
    public boolean save(Tran t, String customerName) {

        /*

        交易添加业务：

            在做添加之前，参数t里面就少了一项信息，就是客户的主键：customerId

            1、根据customerName在客户表中进行精确查询
                如果有这个客户，则取出这个客户的id，封装到t对象中
                如果没有这个客户，则在客户表中新建一条客户信息，然后将新建的客户的id取出，封装到t对象中

            2、经过以上的操作，t对象中的信息就全了，需要执行添加交易的操作

            3、添加交易完毕后，需要创建一条交易历史

         */

        boolean flag = true;

        Customer cus = customerDao.getCustomerByName(customerName);

        //如果cus为null，需要创建客户
        if (cus == null) {

            cus = new Customer();
            cus.setId(UUIDUtil.getUUID());
            cus.setName(customerName);
            cus.setCreateBy(t.getCreateBy());
            cus.setCreateTime(DateTimeUtil.getSysTime());
            cus.setContactSummary(t.getContactSummary());
            cus.setNextContactTime(t.getNextContactTime());
            cus.setOwner(t.getOwner());

            //添加客户
            int count1 = customerDao.save(cus);
            if(count1!=1){
                flag = false;
            }

        }

        //通过以上对于客户的处理，不论是查询出来已有的客户，还是以前没有我们新增的客户，总之客户已经有了。客户的id就有了
        //将客户id封装到t对象中
        t.setCustomerId(cus.getId());

        //添加交易
        int count2 = tranDao.save(t);
        if(count2!=1){
            flag = false;
        }

        //添加交易历史
        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setTranId(t.getId());
        th.setStage(t.getStage());
        th.setMoney(t.getMoney());
        th.setExpectedDate(t.getExpectedDate());
        th.setCreateTime(DateTimeUtil.getSysTime());
        th.setCreateBy(t.getCreateBy());
        int count3 = tranHistoryDao.save(th);
        if(count3!=1){
            flag = false;
        }

        return flag;
    }

    @Override
    public Tran detail(String id) {

        Tran t = tranDao.detail(id);

        //System.out.println("交易详细信息页service:"+t.getName());

        return t;
    }

    @Override
    public List<TranHistory> getHistoryListByTranId(String tranId) {

        List<TranHistory> thList = tranHistoryDao.getHistoryListByTranId(tranId);

        return thList;
    }

    @Override
    public boolean changeStage(Tran t) {

        boolean flag = true;

        //改变交易阶段
        int count1 = tranDao.changeStage(t);
        if(count1!=1){
            flag = false;
        }

        //交易阶段改变后，生成一条交易历史
        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setCreateBy(t.getEditBy());//交易历史的创建人是当前所处在登录上的人。也就是修改交易的人
        th.setCreateTime(DateTimeUtil.getSysTime());
        th.setExpectedDate(t.getExpectedDate());
        th.setMoney(t.getMoney());
        th.setTranId(t.getId());
        th.setStage(t.getStage());
        //添加交易历史
        int count2 = tranHistoryDao.save(th);
        if(count2!=1){
            flag = false;
        }

        return flag;
    }

    @Override
    public Map<String, Object> getCharts() {

        //取得total
        int total = tranDao.getTotal();

        //取得dataList（返回一个map，里面是name键值对、value键值对。=>里面有多条map，就返回成多个map，成了数组）
        List<Map<String,Object>> dataList = tranDao.getCharts();

        //打包放入map
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("total",total);
        map.put("dataList",dataList);

        //返回map
        return map;
    }
}
