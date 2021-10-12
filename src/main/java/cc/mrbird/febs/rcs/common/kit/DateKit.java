package cc.mrbird.febs.rcs.common.kit;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

    /**
     * 日期格式转换yyyy-MM-dd'T'HH:mm:ss.SSSXXX  (yyyy-MM-dd'T'HH:mm:ss.SSSZ) TO  yyyy-MM-dd HH:mm:ss
     * 2020-04-09T23:00:00.000+08:00 TO 2020-04-09 23:00:00
     * @throws ParseException
     */
    public static String dealDateFormatToStr(String oldDateStr) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");  //yyyy-MM-dd'T'HH:mm:ss.SSSZ
        Date  date = df.parse(oldDateStr);
        SimpleDateFormat df1 = new SimpleDateFormat ("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
        Date date1 =  df1.parse(date.toString());
        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df2.format(date1);
    }

    /**
     * 解析指定格式日期，转date
     * @param oldDateStr
     * @return
     * @throws ParseException
     */
    public static Date dealDateFormatToDate(String oldDateStr) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");  //yyyy-MM-dd'T'HH:mm:ss.SSSZ
        Date  date = df.parse(oldDateStr.trim());
        SimpleDateFormat df1 = new SimpleDateFormat ("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
        Date date1 =  df1.parse(date.toString());
        return date1;
    }

    public static Date dealDateFormatToDate2(String oldDateStr) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");  //yyyy-MM-dd'T'HH:mm:ss.SSSZ
        Date  date = df.parse(oldDateStr.trim());
        SimpleDateFormat df1 = new SimpleDateFormat ("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
        Date date1 =  df1.parse(date.toString());
        return date1;
    }

    /**
     * 日期格式转换 yyyy-MM-dd HH:mm:ss  TO yyyy-MM-dd'T'HH:mm:ss.SSSXXX  (yyyy-MM-dd'T'HH:mm:ss.SSSZ)
     * 2020-04-09 23:00:00 TO 2020-04-09T23:00:00.000+08:00
     * @throws ParseException
     */
    public static String dealDateFormatReverse(String oldDateStr) throws ParseException{
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 =  df2.parse(oldDateStr);
        return df.format(date1);
    }

    /**
     * 创建俄罗斯时间
     * @return
     */
    public static String createRussiatime(){
        try {
            return dealDateFormatReverse(formatDateTime(new Date()));
        } catch (ParseException e) {
            return null;
        }
    }

    public static String createRussiatime(Date date){
        try {
            return dealDateFormatReverse(formatDateTime(date));
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 解析俄罗斯时间
     * @param date
     * @return
     */
    public static Date parseRussiatime(String date){
        try {
            if (StringUtils.isEmpty(date)){
                return new Date();
            }

            for (int i = 0; i < date.length(); i++) {
                date = date.replaceAll(" ", "");
                //if(rTime.length()==20&&rTime.charAt(i)=='Z'){
                if (date.length() > 19) {
                    date = date.substring(0, 19);
                    date += ".001+03:00";
                    //rTime = rTime.replace("Z",".001+03: 00");
                } else {
                    log.info("时间长度不够");
                    return new Date();
                }
            }

            return dealDateFormatToDate(date);
        } catch (ParseException e) {
            log.info(e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) throws ParseException {
//        log.info("时间: " + formatDateTime(new Date()));
//        log.info(offsetMinuteToDate(120));
//        log.info(offsetMinuteToDateTime(120));
//        log.info(getNowDateToFileName());
        /*String russiatime = createRussiatime();
        log.info(russiatime);
        log.info(parseRussiatime(russiatime).toString());*/
        //             "2021-01-01T09:00:00.001+03:00"
        String rTime = "2021-01-01T09:00:00.001+03:00";
        log.info(parseRussiatime(rTime).toString());

        String rTime2 = "2021-01-01T09: 00: 00.001+03: 00";
        log.info(parseRussiatime(rTime2).toString());

        String rTime3 = "2021-10-12T05:15:33.327019Z";
        log.info(parseRussiatime(rTime3).toString());

    }

    public static String getNowDateToFileName() {
        return DateUtil.format(new Date(),"YYYY_MM_dd_HH_mm_ss");
    }
}
