package cc.mrbird.febs.rcs.common.kit;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DatePattern;
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
     * @param offsetDay
     * @return
     */
    public static String offsetDayDateStr(Date date, int offsetDay) {
        return DateUtil.offset(date, DateField.DAY_OF_MONTH, offsetDay).toDateStr();
    }

    /**
     * 获取指定日期之后的日期
     * @param date
     * @param offsetDay
     * @return
     */
    public static String offsetDayDateStr(int offsetDay) {
        return DateUtil.offset(new Date(), DateField.DAY_OF_MONTH, offsetDay).toDateStr();
    }

    public static Date offsetDayDate(int offsetDay) {
        return DateUtil.offset(new Date(), DateField.DAY_OF_MONTH, offsetDay).toSqlDate();
    }

    /**
     * 得到N分钟后的时间，格式为yyyy-MM-dd HH:mm:ss
     * @param offsetMinute
     * @return
     */
    public static String offsetMinuteToDate(int offsetMinute) {
        return DateUtil.offset(new Date(), DateField.MINUTE, offsetMinute).toString();
    }

    /**
     * 得到N分钟后的时间，格式为 yyyy-MM-dd
     * @param offsetMinute
     * @return
     */
    public static String offsetMinuteToDateTime(int offsetMinute) {
        return DateUtil.offset(new Date(), DateField.MINUTE, offsetMinute).toString(DatePattern.NORM_DATE_PATTERN);
    }


    /**
     * 判断当前时间和指定日期之间间隔是否超时
     * @param beginDate
     * @return
     */
    public static boolean isOverTime(Date beginDate){
        return betweenNow(beginDate) > overTime;
    }

    /**
     * 格式为yyyy-MM-dd HH:mm:ss
     * @param date
     * @return
     */
    public static String formatDateTime(Date date) {
        return DatePattern.NORM_DATETIME_FORMAT.format(date);
    }

    /**
     * 格式为yyyy-MM-dd
     * @param date
     * @return
     */
    public static String formatDate(Date date) {
        return DatePattern.NORM_DATE_FORMAT.format(date);
    }

    public static void main(String[] args) {
        log.info("时间: " + formatDateTime(new Date()));
        log.info(offsetMinuteToDate(120));
        log.info(offsetMinuteToDateTime(120));
    }
}
