package cc.mrbird.febs.order.utils;

import cc.mrbird.febs.common.enums.AuditBtnEnum;
import cc.mrbird.febs.common.enums.AuditStatusEnum;
import cc.mrbird.febs.common.enums.OrderBtnEnum;
import cc.mrbird.febs.common.enums.OrderStatusEnum;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.i18n.MessageUtils;
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
    public static List<Map<String, String>> getOrderBtnMapList(String orderStatus) {
        List<Map<String, String>> resList = new ArrayList<>();
        List<OrderBtnEnum> list = getOrderBtnList(orderStatus);
        list.stream().forEach(bean->{
            Map<String, String> map = new HashMap<>();
            map.put("event", bean.getEvent());
            map.put("title", bean.getTitle());
            resList.add(map);
        });
        return resList;
    }

    /**
     * 获取map形式的按钮列表
     * @param auditStatus
     * @return
     */
    public static List<Map<String, String>> getAuditBtnMapList(String auditStatus) {
        List<Map<String, String>> resList = new ArrayList<>();
        List<AuditBtnEnum> list = getAuditBtnList(auditStatus);
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
     * 获取审核状态列表
     * @return
     */
    public static List<Map<String,String>> getAuditStatusList(){
        List<Map<String, String>> resList = new ArrayList<>();
        List<AuditStatusEnum> list = Arrays.asList(AuditStatusEnum.values());
        list.stream().forEach(bean->{
            Map<String, String> map = new HashMap<>();
            map.put("status", bean.getStatus());
            map.put("title", bean.getMsg());
            resList.add(map);
        });
        return resList;
    }


    /**
     * 根据状态获取订单页面的按钮列表
     * @param orderStatus
     * @return
     */
    public static List<OrderBtnEnum> getOrderBtnList(String orderStatus)  {
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
                btnList.add(OrderBtnEnum.cancelBtn);
                btnList.add(OrderBtnEnum.freezeBtn);
                break;
            case auditIng:
                btnList.add(OrderBtnEnum.cancelBtn);
                btnList.add(OrderBtnEnum.freezeBtn);
                break;
            case auditPass:
                btnList.add(OrderBtnEnum.submitCloseBtn);
                btnList.add(OrderBtnEnum.cancelBtn);
                btnList.add(OrderBtnEnum.freezeBtn);
                break;
            case auditNotPass:
                btnList.add(OrderBtnEnum.editBtn);
                btnList.add(OrderBtnEnum.submitInjectionBtn);
                btnList.add(OrderBtnEnum.submitCloseBtn);
                btnList.add(OrderBtnEnum.cancelBtn);
                btnList.add(OrderBtnEnum.freezeBtn);
                break;
            case machineGetData:
                btnList.add(OrderBtnEnum.submitCloseBtn);
                btnList.add(OrderBtnEnum.cancelBtn);
                btnList.add(OrderBtnEnum.freezeBtn);
                break;
            case machineInjectionSuccess:
                break;
            case machineInjectionFail:
                btnList.add(OrderBtnEnum.submitCloseBtn);
                btnList.add(OrderBtnEnum.cancelBtn);
                btnList.add(OrderBtnEnum.freezeBtn);
                break;
            case orderRepeal:

                break;
            case orderFreeze:
                btnList.add(OrderBtnEnum.unfreezeBtn);
                break;
            case orderCloseApplyIng:
                btnList.add(OrderBtnEnum.cancelBtn);
                btnList.add(OrderBtnEnum.freezeBtn);
                break;
            case orderCloseApplyNotPass:
                btnList.add(OrderBtnEnum.editBtn);
                btnList.add(OrderBtnEnum.submitInjectionBtn);
                btnList.add(OrderBtnEnum.submitCloseBtn);
                btnList.add(OrderBtnEnum.cancelBtn);
                btnList.add(OrderBtnEnum.freezeBtn);
                break;
        }
        //统一添加审核详情
        btnList.add(OrderBtnEnum.auditDetail);
        return btnList;
    }

    private static List<AuditBtnEnum> getAuditBtnList(String auditStatus) {
        List<AuditBtnEnum> btnList = new ArrayList<>();
        AuditStatusEnum statusEnum = null;
        try {
            statusEnum = AuditStatusEnum.getByStatus(auditStatus);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        /*notBegin("0","未审核"),
                success("1","审核通过"),
                notPass("2","审核不通过"),
                orderFreezeing("3","冻结中"),
                orderRepeal("4","已注销");*/
        switch (statusEnum){
            case notBegin:
                btnList.add(AuditBtnEnum.passBtn);
                btnList.add(AuditBtnEnum.noPassBtn);
                break;
            case success:
                break;
            case notPass:
                break;
            case orderFreezeing:
                break;
            case orderRepeal:
                break;
        }
        return btnList;
    }


    /**
     * 判断订单是否能执行某个操作
     * @param orderStatusEnum
     * @param orderStatus
     * @return
     */
    public static void checkOrderBtnPermissioin(OrderBtnEnum btnEnum, String orderStatus) {
        if (!getOrderBtnList(orderStatus).contains(btnEnum)){
            throw new FebsException(MessageUtils.getMessage("order.operation.orderStatusDisable"));
        }
    }

    /**
     * 判断审核是否可以执行某个操作
     * @param btnEnum
     * @param status
     */
    public static void checkAuditBtnPermissioin(AuditBtnEnum btnEnum, String status) {
        if (!getAuditBtnList(status).contains(btnEnum)){
            throw new FebsException(MessageUtils.getMessage("order.operation.auditStatusDisable"));
        }

        if (status.equals(AuditStatusEnum.orderFreezeing) || status.equals(AuditStatusEnum.orderRepeal)){
            throw new FebsException(MessageUtils.getMessage("order.operation.orderIsFreezeOrRepeal"));
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
        checkOrderBtnPermissioin(btnEnum, OrderStatusEnum.auditIng.getStatus());

        log.info("结果：" + res + " ，耗时：" + (System.currentTimeMillis() - time1));
    }


}
