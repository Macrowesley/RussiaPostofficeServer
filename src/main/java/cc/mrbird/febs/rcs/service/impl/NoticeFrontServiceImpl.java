package cc.mrbird.febs.rcs.service.impl;

import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.websocket.WebSocketServer;
import cc.mrbird.febs.rcs.service.INoticeFrontService;
import org.springframework.stereotype.Service;

@Service
public class NoticeFrontServiceImpl implements INoticeFrontService {

    @Override
    public void notice(int type, String msg) {
        WebSocketServer.sendInfo(type, msg,String.valueOf(FebsUtil.getCurrentUser().getUserId()));
    }
}
