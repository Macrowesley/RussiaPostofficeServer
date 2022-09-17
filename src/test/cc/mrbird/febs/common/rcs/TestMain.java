package rcs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestMain {
    //static long count = 250_000_000;
    static long count = 250000000;
    static int threadsize = 1;//2

    static class InsertDb {
        Connection conn = null;
        //private int persize = 200_000;
        private int persize = 2;//2

        public void initConn() throws ClassNotFoundException, SQLException {

            String url = "jdbc:mysql://127.0.0.1:3306/gdpt_russia_server?serverTimezone=GMT";
            String user = "root";
            String password = "root";
            try {
                // 之所以要使用下面这条语句，是因为要使用MySQL的驱动，所以我们要把它驱动起来，
                // 可以通过Class.forName把它加载进去，也可以通过初始化来驱动起来，下面三种形式都可以
                Class.forName("com.mysql.jdbc.Driver");// 动态加载mysql驱动

                System.out.println("成功加载MySQL驱动程序");
                // 一个Connection代表一个数据库连接
                conn = DriverManager.getConnection(url,user,password);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public synchronized void insert() {
            // 开时时间
            Long begin = new Date().getTime();
            // sql前缀
            String prefix = "INSERT INTO gdpt_russia_server.rcs_transaction_msg VALUES";

            try {
                // 保存sql后缀
                StringBuffer suffix = new StringBuffer();
                // 设置事务为非自动提交
                conn.setAutoCommit(false);
                //      Statement pst = conn.createStatement();
                // 比起st，pst会更好些
                //PreparedStatement pst = conn.prepareStatement("");
                // 外层循环，总提交事务次数
                int suffixId=3060000;//3060000起
                for (int i = 1; i <= count / persize / threadsize; i++) {
                    System.out.println("第"+i+"批插入开始");
                    // 第次提交步长
                    for (int j = 1; j <= persize; j++) {
                        // 构建sql后缀
                        //suffix.append("(CONCAT(10000000,");
                        suffix.append("(");
                        suffix.append(suffixId);
                        suffix.append(",'0010000066','PM001000','2100',10,1000,'!45!01NE6431310002803001005000000000500000000000000100001010','2','2022-03-16 09:11:54'),");
                        suffixId++;
                    }
                    // 构建完整sql
                    String sql = prefix + suffix.substring(0, suffix.length() - 1);
                    // 添加执行sql
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.addBatch(sql);
                    // 执行操作
                    pst.executeBatch();
                    // 提交事务
                    conn.commit();
                    // 清空上一次添加的数据
                    suffix = new StringBuffer();
                    pst.close();
                    System.out.println("第"+i+"批插入完成");
                }
                // 头等连接
                //pst.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            // 结束时间
            Long end = new Date().getTime();
            // 耗时
            System.out.println("cast : " + (end - begin) / 1000f + " ms");
        }
    }
    public static void main(String[] args) throws SQLException, ClassNotFoundException, InterruptedException {
        long s = System.currentTimeMillis();
        List<Thread> list = new ArrayList<>();
        for (int i = 0; i < threadsize; i++) {
            Thread th = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        InsertDb db = new InsertDb();
                        db.initConn();
                        db.insert();

                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }
            });
            th.start();
            list.add(th);
        }
        for (Thread th : list) {
            th.join();
        }

        long e = System.currentTimeMillis();
        System.out.println("total耗时" + (e - s) / 1000f + "s");
    }
}