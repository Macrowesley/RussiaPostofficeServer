package cc.mrbird.febs.rcs.common.enums;


public enum EventEnum {
    STATUS(1,"STATUS"),
    RATE_TABLE_UPDATE(2,"RATE_TABLE_UPDATE");

    private final int type;
    private final String event;

    EventEnum(int type, String event){
        this.type = type;
        this.event = event;
    }

    public String getEvent() {
        return event;
    }

    public int getType() {
        return type;
    }

    public static EventEnum getEventByType(int eventType){
        for (EventEnum item: EventEnum.values()){
            if (item.getType()== eventType){
                return item;
            }
        }
        return null;
    }
}
