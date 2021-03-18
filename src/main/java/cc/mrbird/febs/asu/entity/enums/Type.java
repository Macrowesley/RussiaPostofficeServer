package cc.mrbird.febs.asu.entity.enums;


public enum Type {
    REFILL("REFILL"),
    REFUND("REFUND");

    private final String type;

    Type(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
