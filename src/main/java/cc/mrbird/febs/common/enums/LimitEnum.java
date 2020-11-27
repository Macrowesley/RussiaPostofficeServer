package cc.mrbird.febs.common.enums;

import cc.mrbird.febs.common.i18n.MessageUtils;

public enum LimitEnum {
    strict(1,60, 10, MessageUtils.getMessage("limitEnum.strict")),
    loose(2,60, 30, MessageUtils.getMessage("limitEnum.loose"));
    private int type;
    private int period;
    private int count;
    private String message;

    LimitEnum(int type, int period, int count, String message) {
        this.type = type;
        this.period = period;
        this.count = count;
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static LimitEnum getByType(int type) throws Exception {
        for (LimitEnum item : LimitEnum.values()) {
            if (item.getType() == type) {
                return item;
            }
        }
        throw new Exception(MessageUtils.getMessage("audit.BtnEnum.noType"));
    }
}
