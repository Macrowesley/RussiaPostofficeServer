package cc.mrbird.febs.rcs.common.kit;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class DateKit {
    //超时时间
    public static long overTime = 60;
    /**
     * 获取指定日期和当前时间的时间间隔秒数字
     * @param beginDate
     * @return
     */
    public static long betweenNow(Date beginDate){
        return DateUtil.between(beginDate, new Date(), DateUnit.SECOND);
    }

    /**
     * 获取指定日期之后的日期
     * @param date
     * @param offset
     * @return
     */
    public static String offsetDateStr(Date date, int offset) {
        return DateUtil.offset(date, DateField.DAY_OF_MONTH, offset).toDateStr();
    }

    /**
     * 获取指定日期之后的日期
     * @param date
     * @param offset
     * @return
     */
    public static String offsetDateStr(int offset) {
        return DateUtil.offset(new Date(), DateField.DAY_OF_MONTH, offset).toDateStr();
    }

    public static Date offsetDate(int offset) {
        return DateUtil.offset(new Date(), DateField.DAY_OF_MONTH, offset).toSqlDate();
    }

    /**
     * 判断当前时间和指定日期之间间隔是否超时
     * @param beginDate
     * @return
     */
    public static boolean isOverTime(Date beginDate){
        return betweenNow(beginDate) > overTime;
    }

    public static void main(String[] args) {
        log.info("时间: " + offsetDateStr(7));
    }
}
