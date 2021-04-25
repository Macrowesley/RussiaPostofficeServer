package cc.mrbird.febs.rcs.dto.manager;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Data
@ToString
@NoArgsConstructor
public class PublicKeyDTO {
    /**
     * PEM格式的公钥
      example:
      "-----BEGIN PUBLIC KEY-----
      MEkwEwYHKoZIzj0CAQYIKoZIzj0DAQEDMgAHxZMuhGUvOwc6GKT6Y9V6+uSQmiLW
      9vCO4A1xy7qquqrNFmPlsQhPMZUZ62HBKDeH
      -----END PUBLIC KEY-----"
     */
    @NotBlank
    String key;

    /**
     * 公钥的版本（序列号）
     * example:
     * 113
     */
    @NonNull
    Integer revision;

    /**
     * 到期时间
     * format: 'date-time'
     */
    @NotBlank
    String expireDate;

    private String alg;

}
