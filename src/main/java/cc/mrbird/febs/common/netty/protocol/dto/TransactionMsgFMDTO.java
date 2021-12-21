package cc.mrbird.febs.common.netty.protocol.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TransactionMsgFMDTO {
    //第一批，id是ForeseensId,idType=1  以后的发送id是TransactionId,idType=2
    String id;
    //id类型
    int idType;
    //产品code
    String code;
    //累加总数量
    String totalCount;
    //累加总金额，单位是分
    String totalAmount;
    //二维码数据矩阵 开机时机器发送的二维码为空
    String dmMsg;
    //1开始 2结束 0开机：需要服务器自己判断：上一个为2，不存入数据库，上一个为1，服务器储存这条信息，status改成2
    String status;
    String frankMachineId;
    //测试专用，代表transactionId，生产环境不会用这个id
    String testId;
}
