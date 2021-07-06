package cc.mrbird.febs.rcs.dto.machine;

import cc.mrbird.febs.rcs.dto.manager.FrankDTO;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DmMsgDetail {
    //当前任务已经打印的总数量
    int actualCount = 0;
    //当前任务已经打印的总金额 单位是分
    String actualAmount = "0";
    FrankDTO[] franks;
}
