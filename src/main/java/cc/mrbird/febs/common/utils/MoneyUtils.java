package cc.mrbird.febs.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
@Slf4j
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

    public static double changeF2Y(String fen) {
        return BigDecimal.valueOf(Long.valueOf(fen)).divide(new BigDecimal(100)).doubleValue();
        /*String amount = String.valueOf(price);
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
        return amount;*/
    }

    /**
     * 金额保留2位小数
     * @param money
     * @return
     */
    public static String moneySaveTwoDecimal(String money){
        if (StringUtils.isEmpty(money)){
            money = "0";
        }
        DecimalFormat format = new DecimalFormat("0.00");
        String a = format.format(new BigDecimal(money));
        return a;
    }

    /**
     * 判断2个金额是否相同
     * @param m1
     * @param m2
     * @return
     */
    public static boolean moneyIsSame(String m1, String m2){
        return moneySaveTwoDecimal(m1).equals(moneySaveTwoDecimal(m2));
    }

    public static void main(String[] args) {
//        test1();
//        test2();
        test3();
    }

    private static void test3() {
        String fen =  "130";
        double yuan = changeF2Y(fen);
        log.info("fen={},yuan={}", fen, yuan);
    }

    private static void test2() {
        System.out.println(moneyIsSame("126", "126.00"));
    }

    private static void test1() {
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
