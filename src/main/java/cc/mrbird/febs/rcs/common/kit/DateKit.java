package cc.mrbird.febs.rcs.common.kit;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Stream;

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
     * 日期格式转换 yyyy-MM-dd HH:mm:ss  TO yyyy-MM-dd'T'HH:mm:ss.SSSXXX  (yyyy-MM-dd'T'HH:mm:ss.SSSZ)
     * 2020-04-09 23:00:00 TO 2020-04-09T23:00:00.000+08:00
     * @throws ParseException
     */
    public static String parseBeiJingTimeToUtc(String beijinDate) throws ParseException{
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 =  df2.parse(beijinDate);
        return df.format(date1);
    }

    /**
     * 创建俄罗斯时间
     * @return
     */
    public static String createRussiatime(){
        try {
            return parseBeiJingTimeToUtc(formatDateTime(new Date()));
        } catch (ParseException e) {
            return null;
        }
    }

    public static String createRussiatime(Date date){
        try {
            return parseBeiJingTimeToUtc(formatDateTime(date));
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 解析俄罗斯时间
     * @param date
     * @return
     */
    public static Date parseRussiatime(String dateTime){
        dateTime = dateTime.replace(" ","");
        try {
            if (dateTime.contains("Z")){
                dateTime = dateTime.replace("Z", " UTC");
                SimpleDateFormat format = null;
                switch (dateTime.length()){
                    case 23:
                        //2021-10-19T21:00:00Z
                        format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                        break;
                    case 27:
                        //2021-10-01T00:00:00.000Z
                        format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                        break;
                    case 30:
                        //2021-10-19T05:42:48.472647Z
                        format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ");
                        break;
                    default:
                        dateTime = dateTime.substring(0,19) + " UTC";
                        format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                        break;
                }
                Date date = format.parse(dateTime);
                return date;
            }else{
                SimpleDateFormat format = null;
                switch (dateTime.length()){
                    case 28:
                        //2019-03-22T09:11:52.000+0000
                        format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                        break;
                    case 29:
                        //2021-01-01T09:00:00.001+03:00
                        format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                        break;
                    default:
                        dateTime = dateTime.substring(0,23);
                        format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        break;
                }
                Date date = format.parse(dateTime);
                return date;
            }
        }catch (Exception e){
            log.error("解析俄罗斯传递来的时间" + e.getMessage());
            return new Date();
        }
    }
    public static String getNowDateToFileName() {
        return DateUtil.format(new Date(),"YYYY_MM_dd_HH_mm_ss");
    }

    /**
     * 判断applyDate是否可用
     * applyDate比当前时间小，即可用
     * @param firstDate
     * @param secondDate
     * @return
     */
    public static boolean checkApplyDateIsEnable(Date applyDate, Date nowDate){
        return nowDate.compareTo(applyDate) >= 0;
    }

    public static void main(String[] args) {
        testParseRussiaTime();
    }

    private static void testParseRussiaTime() {
        Stream.of(
                "2021-01-01T09:00:00.001+03:00",
                "2019-03-22T09:11:52.000+0000",
                "2021-10-19T21:00:00Z",
                "2021-10-19T21:00:00.1Z",
                "2021-10-01T00:00:00.000Z",
                "2021-10-19T05:42:48.472647Z")
                .forEach(time -> {
                    log.info("开始解析：" + time);
                    Date date = parseRussiatime(time);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    String format = simpleDateFormat.format(date);
                    log.info("结果 = " + format);
                    log.info("");
                });
        log.info(createRussiatime());
    }
}
