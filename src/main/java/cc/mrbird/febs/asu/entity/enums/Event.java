package cc.mrbird.febs.asu.entity.enums;


public enum Event {
    STATUS("STATUS"),
    RATE_TABLE_UPDATE("RATE_TABLE_UPDATE");

    private final String event;

    Event(String event){
        this.event = event;
    }

    public String getEvent() {
        return event;
    }
}
