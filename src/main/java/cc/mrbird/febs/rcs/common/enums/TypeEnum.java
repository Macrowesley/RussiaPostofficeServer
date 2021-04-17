package cc.mrbird.febs.rcs.common.enums;


public enum TypeEnum {
    REFILL("REFILL"),
    REFUND("REFUND");

    private final String type;

    TypeEnum(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
