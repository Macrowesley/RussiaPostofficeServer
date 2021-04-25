package cc.mrbird.febs.rcs.common.enums;


public enum EventEnum {
    STATUS(1,"STATUS"),
    RATE_TABLE_UPDATE(2,"RATE_TABLE_UPDATE");

    private final int code;
    private final String event;

    EventEnum(int code, String event){
        this.code = code;
        this.event = event;
    }

    public String getEvent() {
        return event;
    }

    public int getCode() {
        return code;
    }

    public static EventEnum getByCode(int code){
        for (EventEnum item: EventEnum.values()){
            if (item.getCode()== code){
                return item;
            }
        }
        return null;
    }
}
