package cc.mrbird.febs.common.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class NettyProperties {
    @Value("${netty.ip}")
    private String ip;
    @Value("${netty.port}")
    private int port;
}
