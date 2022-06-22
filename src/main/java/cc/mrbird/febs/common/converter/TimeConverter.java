package cc.mrbird.febs.common.converter;

import lombok.extern.slf4j.Slf4j;

/**
 * Execl导出时间类型字段格式化
 *
 *
 */
@Slf4j
public class TimeConverter {

}
/*@Slf4j
public class TimeConverter implements WriteConverter {
    @Override
    public String convert(Object value) {
        if (value == null) {
            return StringUtils.EMPTY;
        } else {
            try {
                return DateUtil.formatCstTime(value.toString(), DateUtil.FULL_TIME_SPLIT_PATTERN);
            } catch (ParseException e) {
                String message = "时间转换异常";
                log.error(message, e);
                throw new ExcelKitWriteConverterException(message);
            }
        }
    }
}*/
