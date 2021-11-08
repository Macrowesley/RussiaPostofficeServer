package cc.mrbird.febs.common.configure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@ConfigurationProperties(prefix="customer")
@Component
public class IpConfig {
    private List<String> whiteIps = new ArrayList<String>();
}
