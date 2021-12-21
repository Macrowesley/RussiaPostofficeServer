package cc.mrbird.febs.rcs.service;

public interface IMsgService {
    String createWebsocketKey(int type, int id);
    void sendMsg(String key, String value);
    void receviceMsg(int type,int printJobId, String res);
    void overtimeMsg(String key);
}
