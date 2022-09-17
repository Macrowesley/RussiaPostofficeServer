package rcs;//package cc.mrbird.febs.rcs.controller;
//
//import cc.mrbird.febs.rcs.entity.TransactionMsg;
//
//import java.util.List;
//
//public class JdbcTest {
//    public static void main(String[] args) {
//        try {
//            //准白sql语句
//            String sql = "INSERT INTO table1(NAME,gender,age,qq) VALUES(?,?,?,?)";
//            Connection con = null;
//            //加载驱动
//            Class.forName("com.mysql.jdbc.Driver");
//            //获取到远程服务器的连接
//            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Jnshu1?useUnicode=true&"+
//                            "characterEncoding=utf-8&useSSL=false&rewriteBatchedStatements=true",
//                    "root","root");
//            //设置非自动提交事务
//            con.setAutoCommit(false);
//
//            PreparedStatement pstat = con.prepareStatement(sql);
//
//            //获得模拟数据
//            list2 = getTransactionMsgWithArg();
//            System.out.println(list2.size());
//
//            long start = System.currentTimeMillis();
//
//            for (int i=0; i<list2.size(); i++) {
//
//                TransactionMsg transactionMsg = list2.get(i);
//                pstat.setString(1, transactionMsg.getNAME());
//                pstat.setString(2, transactionMsg.getGender());
//                pstat.setString(3, transactionMsg.getAge());
//                pstat.setString(4, transactionMsg.getQq());
//
//                //10w提交一次
//                pstat.addBatch();
//                if(i % 100000 == 0){
//                    pstat.executeBatch();
//                    pstat.clearBatch();
//                }
//            }
//            pstat.executeBatch(); //执行批处理
//            pstat.clearBatch();  //清空批处理
//            con.commit();
//            long end = System.currentTimeMillis();
//
//            pstat.close();
//            con.close();
//            System.out.print((end-start)/1000+"秒。");
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//
//    // 模拟数据
////1000万数据执行后会内存溢出
//    public List<TransactionMsg> getTransactionMsgWithArg(){
//
//        //由一个StringBuffer来代替之前4个String的反复字符串操作。
//        StringBuffer sb = new StringBuffer("");// String = "";
//        for (int r = 0; r < 1000000 ; r++) {
//            TransactionMsg transactionMsg = new TransactionMsg();
//            //随机产生姓名
//            for (int i = 0; i < 3; i++) {
//                sb.append((char) (0x4e00 + (Math.random() * (0x9fa5 - 0x4e00 + 1))));
//            }
//            transactionMsg.setNAME(sb.toString());
//            //用完后清楚里面的内容，以供下一次使用
//            sb.delete(0,sb.length());
//
//            //随机产生性别
//            int sexf = (int)(Math.random() * 2);//[0,2)
//            if(sexf==0){
//                sb.append("男");
//            }else if( sexf == 1){
//                sb.append("女");
//            }else {
//                sb.append("Unknow");
//            }
//            transactionMsg.setGender(sb.toString());
//            sb.delete(0,sb.length());
//            //随机产生年龄
//            int d;
//            for (int i = 0; i < 2; i++) {
//                d = (int)(Math.random()*10);
//                sb.append(d);
//            }
//            TransactionMsg.setAge(sb.toString());
//            sb.delete(0,sb.length());
//
//            // 随机产生qq
//            int j;
//            for (int i = 0; i < 10; i++) {
//                d = (int)(Math.random()*10);
//                sb.append(d);
//            }
//            TransactionMsg.setQq(sb.toString());
//            sb.delete(0,sb.length());
//
//            list.add(transactionMsg);
//        }
//        return list;
//    }
//}
