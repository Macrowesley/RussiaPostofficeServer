package cc.mrbird.febs.common.constant;

import cc.mrbird.febs.common.i18n.MessageUtils;

public class LimitConstant {

    /**
     * 严格模式
     */
    public static class Strict{
        public static final int period = 60;
        public static final int count = 15;
    }

    /**
     * 宽松模式
     */
    public static class Loose{
        public static final int period = 60;
        public static final int count = 25;
    }
}
