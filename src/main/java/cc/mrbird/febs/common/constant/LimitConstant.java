package cc.mrbird.febs.common.constant;

import cc.mrbird.febs.common.i18n.MessageUtils;

public class LimitConstant {

    /**
     * 严格模式
     * 数据的增删改
     */
    public static class Strict{
        public static final int period = 30;
        public static final int count = 6;
    }

    /**
     * 宽松模式
     * view 数据的查
     */
    public static class Loose{
        public static final int period = 30;
        public static final int count = 12;
    }
}
