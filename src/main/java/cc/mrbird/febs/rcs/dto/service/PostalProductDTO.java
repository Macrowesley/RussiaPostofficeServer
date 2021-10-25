package cc.mrbird.febs.rcs.dto.service;

import com.alibaba.fastjson.annotation.JSONType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JSONType(orders={"code","name","mailType","mailCtg","maxWeight","transType","regionType","regionZone","distanceType","contractName",
        "numdiff","labelRu","isPostalMarketOnly","modified"})
public class PostalProductDTO {
    /*"code": "6101",
            "name": "Почтовая карточка",
            "mailType": 6,
            "mailCtg": 1,
            "maxWeight": 20,
            "transType": "ANY",
            "regionType": "DOMESTIC",
            "regionZone": 1,
            "distanceType": "AFTER_2000",
            "labelRu": "Карточка заказная внутренняя",
            "isPostalMarketOnly": false,
            "modified": "2021-01-01T09:00:00.001+03:00"*/
    String code;
    String name;
    Integer mailType;
    Integer mailCtg;
    Integer maxWeight;
    //SURFACE, AIR, ANY
    String transType;
    String regionType;
    Integer regionZone;
    //AFTER_2000, UP_2000
    String distanceType;
    String contractName;
    Integer numdiff;
    String labelRu;
    boolean isPostalMarketOnly;
    String modified;

    @JsonProperty(value = "isPostalMarketOnly")
    public boolean getIsPostalMarketOnly() {
        return isPostalMarketOnly;
    }

    public void setIsPostalMarketOnly(boolean isPostalMarketOnly) {
        isPostalMarketOnly = isPostalMarketOnly;
    }
}
