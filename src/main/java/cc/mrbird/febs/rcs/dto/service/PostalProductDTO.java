package cc.mrbird.febs.rcs.dto.service;

import com.alibaba.fastjson.annotation.JSONType;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JSONType(orders={"code","name","mailType","mailCtg","maxWeight","transType","regionType","regionZone","distanceType","contractName",
        "numdiff","labelRu","isPostalMarketOnly","modified"})
public class PostalProductDTO {
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
}
