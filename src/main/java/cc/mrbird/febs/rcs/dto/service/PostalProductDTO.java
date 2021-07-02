package cc.mrbird.febs.rcs.dto.service;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@ToString
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
