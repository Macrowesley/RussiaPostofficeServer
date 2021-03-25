package cc.mrbird.febs.asu.entity.enums;


public enum Event {
    STATUS(1,"STATUS"),
    RATE_TABLE_UPDATE(2,"RATE_TABLE_UPDATE");

    private final int type;
    private final String event;

    Event(int type, String event){
        this.type = type;
        this.event = event;
    }

    public String getEvent() {
        return event;
    }

    public int getType() {
        return type;
    }

    public static Event getEventByType(int eventType){
        switch (eventType){
            case 1:
                return STATUS;
            case 2:
                return RATE_TABLE_UPDATE;
            default:
                return null;
        }
    }
}
