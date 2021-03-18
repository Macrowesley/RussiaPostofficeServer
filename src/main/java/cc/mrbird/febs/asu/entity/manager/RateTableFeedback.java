package cc.mrbird.febs.asu.entity.manager;

import lombok.Data;
import lombok.ToString;

/**
 * 资费表加载结果
 */
@Data
@ToString
public class RateTableFeedback {
    /**
     * 原始（RP）版本资费
     * RP.202001-1
     */
    String taxVersion;

    /**
     * 是否成功处理关税表
     */
    boolean status;

    /**
     * 映射的本地（RCS）资费版本
     * example:
     * - A0042015A
     * - B0042015A
     * - C0042015A
     * - D0042015A
     * - E0042015A
     */
    String[] rcsVersions;
}
