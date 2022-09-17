//package rcs;
//
//import cc.mrbird.febs.common.netty.protocol.dto.TransactionMsgFMDTO;
//import cc.mrbird.febs.common.utils.AESUtils;
//import cc.mrbird.febs.rcs.entity.TransactionMsg;
//import cc.mrbird.febs.rcs.service.ITransactionMsgService;
//import cc.mrbird.febs.rcs.service.impl.TransactionMsgServiceImpl;
//import cn.hutool.core.date.DateTime;
//
//import java.text.NumberFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Random;
//
//
//public class TransactionMsgController{
//    public static void main(String[] args) throws ParseException {
//
//        System.out.println("插入前时间："+new Date().toString());
//       // for (int i = 0; i < 500 * 3600 * 24 * 30 * 6 / 5; i++) {
//            for (int i = 2; i < 5; i++) {
//            TransactionMsg transactionMsg = new TransactionMsg();
//            ITransactionMsgService transactionMsgService = new TransactionMsgServiceImpl();
//                NumberFormat nf = NumberFormat.getInstance();
//                //设置是否使用分组
//                nf.setGroupingUsed(false);
//                //设置最大整数位数
//                nf.setMaximumIntegerDigits(6);
//                //设置最小整数位数
//                nf.setMinimumIntegerDigits(6);
//                String uuid = nf.format(i);
//
//                //transactionMsg.setId(uuid);
//            transactionMsg.setId((long)i);
//            transactionMsg.setTransactionId(uuid);
//            transactionMsg.setFrankMachineId("PM001000");
//            transactionMsg.setCode("2100");
//            transactionMsg.setCount(10L);
//            transactionMsg.setAmount(1000L);
//            transactionMsg.setDmMsg("!45!01NE6431310002803001005000000000500000000000000100001010");
//            transactionMsg.setStatus("2");
//
//
//            Date date = new DateTime();
//            String dateStr = date.toString().replace("Z", " UTC");//注意是空格+UTC
//            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");//注意格式化的表达式
//            Date d = format.parse(dateStr);
//
//            transactionMsg.setCreatedTime(d);
//
//            transactionMsgService.createTransactionMsg(transactionMsg);//NullPointerException
//        }
//        }
//}
