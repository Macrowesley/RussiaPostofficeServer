package cc.mrbird.febs.rcs.dto.machine;

import cc.mrbird.febs.rcs.dto.manager.ForeseenProductFmDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PrintProgressInfo {
    //当前任务已经打印的总数量
    int actualCount = 0;
    //当前任务已经打印的总金额 单位是分
    String actualAmount = "0";
    ForeseenProductFmDto[] productArr;
}
