package cc.mrbird.febs.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 时间工具类
 *
 *
 */
public class DateUtil {

    public static final String FULL_TIME_PATTERN = "yyyyMMddHHmmss";

    public static final String FULL_TIME_SPLIT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final String CST_TIME_PATTERN = "EEE MMM dd HH:mm:ss zzz yyyy";

    public static String formatFullTime(LocalDateTime localDateTime) {
        return formatFullTime(localDateTime, FULL_TIME_PATTERN);
    }

    public static String formatFullTime(LocalDateTime localDateTime, String pattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        return localDateTime.format(dateTimeFormatter);
    }

    public static String getDateFormat(Date date, String dateFormatType) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormatType, Locale.CHINA);
        return simpleDateFormat.format(date);
    }

    public static String formatCstTime(String date, String format) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(CST_TIME_PATTERN, Locale.US);
        Date usDate = simpleDateFormat.parse(date);
        return DateUtil.getDateFormat(usDate, format);
    }

    public static String formatInstant(Instant instant, String format) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return localDateTime.format(DateTimeFormatter.ofPattern(format));
    }

    public static Date getDateFormat(String date) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FULL_TIME_SPLIT_PATTERN, Locale.CHINA);
        return simpleDateFormat.parse(date);
    }

    /**
     * 得到几天前的时间
     * @param d
     * @param day
     * @return
     */
    public static Date getDateBefore(Date d,int day){
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE,now.get(Calendar.DATE)-day);
        return now.getTime();
    }

    /**
     * 得到几天后的时间
     * @param d
     * @param day
     * @return
     */
    public static Date getDateAfter(Date d,int day){
        Calendar now =Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE,now.get(Calendar.DATE)+day);
        return now.getTime();
    }

    /**
     * 判断是否超时
     * @param checkData 验证时间
     * @return
     */
    public static boolean isOverTime(Date checkData){
        long time = new Date().getTime();
        long checkTime = checkData.getTime();
        if (checkTime > time){
            return false;
        }
        return true;
    }

    public static void main(String[] args) throws ParseException {
        /*Date nowDate = new Date();
        Date afterDate = getDateAfter(nowDate, 7);
        System.out.println(nowDate);
        System.out.println(afterDate);*/

       Date checkDate = getDateFormat("2020-06-02 16:52:31");
       if (isOverTime(checkDate)){
           System.out.println("超时");
       }else{
           System.out.println("还没超时");
       }
    }
}
