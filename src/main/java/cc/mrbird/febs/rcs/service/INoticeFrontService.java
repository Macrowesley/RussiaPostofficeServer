package cc.mrbird.febs.rcs.service;

/**
 * 通知前端信息
 */
public interface INoticeFrontService {
    public void notice(int type, String msg, long userId);
}
