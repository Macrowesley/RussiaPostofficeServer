package cc.mrbird.febs.rcs.service;

public interface IMsgService {
    String createWebsocketKey(int type, int printJobId, int userId);
    void sendMsg(int type, int printJobId, String value);
    void receviceMsg(int type,int printJobId, String res, int userId);
    void overtimeMsg(String key);
}
