package cc.mrbird.febs.common.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class MoneyUtils {
    /**
     * 元转分，确保price保留两位有效数字
     *
     * @return
     */
    public static Long changeY2F(double price) {
        /*DecimalFormat df = new DecimalFormat("#.00");
        price = Double.valueOf(df.format(price));
        int money = (int) (price * 100);
        return money;*/

        String amount = String.valueOf(price);
        NumberFormat format = NumberFormat.getInstance();
        try {
            Number number = format.parse(amount);
            double temp = number.doubleValue() * 100.0;
            format.setGroupingUsed(false);
            // 设置返回数的小数部分所允许的最大位数
            format.setMaximumFractionDigits(0);
            amount = format.format(temp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Long.valueOf(amount);
    }

    /**
     * 分转元，转换为bigDecimal在toString
     *
     * @return
     */
    public static String changeF2Y(long price) {
//        return BigDecimal.valueOf(Long.valueOf(price)).divide(new BigDecimal(100)).toString();
        String amount = String.valueOf(price);
        NumberFormat format = NumberFormat.getInstance();
        try {
            Number number = format.parse(amount);
            double temp = number.doubleValue() / 100.0;
            format.setGroupingUsed(false);
            // 设置返回的小数部分所允许的最大位数
            format.setMaximumFractionDigits(2);
            amount = format.format(temp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return amount;
    }

    /**
     * 金额保留2位小数
     * @param money
     * @return
     */
    public static String moneySaveTwoDecimal(String money){
        DecimalFormat format = new DecimalFormat("0.00");
        String abc ="100.456";
        String a = format.format(new BigDecimal(money));
        return a;
    }

    public static void main(String[] args) {
//        double price = 34.8;
//        System.out.println(yuanToFen("34.8"));
//        System.out.println(fenToYuan(yuanToFen("34.8")));
        for (int i = 0; i < 100; i++) {
            for (int k = 0; k < 1000; k++) {
                String old = i + "." + k;
                String money = moneySaveTwoDecimal(old);
                System.out.println("old = " + old + " yuan = " + money);

                /*int fen = changeY2F(Double.valueOf(old));
                String yuan = changeF2Y(fen);
                System.out.println("old = " + old + " yuan = " + yuan + " fen = " + fen);*/
            }
        }
    }
}
