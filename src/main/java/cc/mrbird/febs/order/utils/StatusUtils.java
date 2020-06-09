package cc.mrbird.febs.order.utils;

import cc.mrbird.febs.common.enums.OrderBtnEnum;
import cc.mrbird.febs.common.enums.OrderStatusEnum;
import cc.mrbird.febs.common.exception.FebsException;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * 状态工具类
 */
@Slf4j
public class StatusUtils {

    /**
     * 获取map形式的按钮列表
     * @param orderStatus
     * @return
     */
    public static List<Map<String, String>> getBtnMapList(String orderStatus) {
        List<Map<String, String>> resList = new ArrayList<>();
        List<OrderBtnEnum> list = getBtnList(orderStatus);
        list.stream().forEach(bean->{
            Map<String, String> map = new HashMap<>();
            map.put("event", bean.getEvent());
            map.put("title", bean.getTitle());
            resList.add(map);
        });
        return resList;
    }

    /**
     * 获取订单状态列表
     * @return
     */
    public static List<Map<String,String>> getOrderStatusList(){
        List<Map<String, String>> resList = new ArrayList<>();
        List<OrderStatusEnum> list = Arrays.asList(OrderStatusEnum.values());
        list.stream().forEach(bean->{
            Map<String, String> map = new HashMap<>();
            map.put("status", bean.getStatus());
            map.put("title", bean.getMsg());
            resList.add(map);
        });
        return resList;
    }


    /**
     * 根据状态获取按钮列表
     * @param orderStatus
     * @return
     */
    public static List<OrderBtnEnum> getBtnList(String orderStatus)  {
//        List<OrderBtnEnum> btnList = Arrays.asList(OrderBtnEnum.values());
        List<OrderBtnEnum> btnList = new ArrayList<>();
        OrderStatusEnum orderStatusEnum = null;
        try {
            orderStatusEnum = OrderStatusEnum.getByStatus(orderStatus);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        switch (orderStatusEnum){
            case createOrder:
                btnList.add(OrderBtnEnum.editBtn);
                btnList.add(OrderBtnEnum.submitInjectionBtn);
                btnList.add(OrderBtnEnum.repealBtn);
                btnList.add(OrderBtnEnum.freezeBtn);
                break;
            case auditIng:
                btnList.add(OrderBtnEnum.repealBtn);
                btnList.add(OrderBtnEnum.freezeBtn);
                break;
            case auditPass:

                break;
            case auditNotPass:
                btnList.add(OrderBtnEnum.editBtn);
                btnList.add(OrderBtnEnum.submitInjectionBtn);
                btnList.add(OrderBtnEnum.submitCloseBtn);
                btnList.add(OrderBtnEnum.repealBtn);
                btnList.add(OrderBtnEnum.freezeBtn);
                break;
            case machineGetData:
                btnList.add(OrderBtnEnum.submitCloseBtn);
                btnList.add(OrderBtnEnum.repealBtn);
                btnList.add(OrderBtnEnum.freezeBtn);
                break;
            case machineInjectionSuccess:
                break;
            case machineInjectionFail:
                btnList.add(OrderBtnEnum.submitCloseBtn);
                btnList.add(OrderBtnEnum.repealBtn);
                btnList.add(OrderBtnEnum.freezeBtn);
                break;
            case orderRepeal:
                btnList.add(OrderBtnEnum.editBtn);
                btnList.add(OrderBtnEnum.submitInjectionBtn);
                btnList.add(OrderBtnEnum.submitCloseBtn);
                btnList.add(OrderBtnEnum.repealBtn);
                btnList.add(OrderBtnEnum.freezeBtn);
                break;
            case orderFreeze:
                btnList.add(OrderBtnEnum.unfreezeBtn);
                break;
            case orderCloseApplyIng:
                btnList.add(OrderBtnEnum.repealBtn);
                btnList.add(OrderBtnEnum.freezeBtn);
                break;
            case orderCloseApplyNotPass:
                btnList.add(OrderBtnEnum.editBtn);
                btnList.add(OrderBtnEnum.submitInjectionBtn);
                btnList.add(OrderBtnEnum.submitCloseBtn);
                btnList.add(OrderBtnEnum.repealBtn);
                btnList.add(OrderBtnEnum.freezeBtn);
                break;
        }
        //统一添加审核详情
        btnList.add(OrderBtnEnum.auditDetail);
        return btnList;
    }


    /**
     * 判断订单是否能执行某个操作
     * @param orderStatusEnum
     * @param orderStatus
     * @return
     */
    public static void checkBtnPermissioin(OrderBtnEnum btnEnum, String orderStatus) {
        if (!getBtnList(orderStatus).contains(btnEnum)){
            throw new FebsException("当前注资状态无法执行该操作");
        }
    }

    public static void main(String[] args) throws Exception {
        long time1 = System.currentTimeMillis();
        boolean res = false;
        /*for (int i = 0; i < 10000; i++) {
            OrderBtnEnum btnEnum = OrderBtnEnum.editBtn;
            checkBtnPermissioin(btnEnum, OrderStatusEnum.orderCloseApplyIng.getStatus());
        }*/
        OrderBtnEnum btnEnum = OrderBtnEnum.unfreezeBtn;
        checkBtnPermissioin(btnEnum, OrderStatusEnum.auditIng.getStatus());

        log.info("结果：" + res + " ，耗时：" + (System.currentTimeMillis() - time1));
    }


}
