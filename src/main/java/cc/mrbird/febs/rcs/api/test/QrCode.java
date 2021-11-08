package cc.mrbird.febs.rcs.api.test;

import lombok.Data;


@Data
public class QrCode{
    String content;
    String publicKey;
    String sign;
}