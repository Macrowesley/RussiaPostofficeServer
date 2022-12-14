package cc.mrbird.febs.common.entity;

/**
 * 常量
 *
 *
 */
public interface FebsConstant {

    /**
     * 注册用户角色ID
     */
    Long REGISTER_ROLE_ID = 2L;

    /**
     * 排序规则：降序
     */
    String ORDER_DESC = "desc";

    /**
     * 排序规则：升序
     */
    String ORDER_ASC = "asc";

    /**
     * 前端页面路径前缀
     */
    String VIEW_PREFIX = "febs/views/";

    /**
     * 验证码 Session Key
     */
    String CODE_PREFIX = "febs_captcha_";

    /**
     * 登录失败统计 Session Key
     */
    String LOGIN_ERROR = "login_error:";

    /**
     * 允许下载的文件类型，根据需求自己添加（小写）
     */
    String[] VALID_FILE_TYPE = {"xlsx", "zip"};

    /**
     * 异步线程池名称
     */
    String ASYNC_POOL = "febsAsyncThreadPool";

    /**
     * RCS异步线程池名称
     */
    String NETTY_ASYNC_POOL = "NettyAsyncThreadPool";
    /**
     * 开发环境
     */
    String DEVELOP = "dev";

    /**
     * Windows 操作系统
     */
    String SYSTEM_WINDOWS = "windows";

    /**
     * 机器版本
     */
    String FmVersion1 = "001";

    /**
     * 判断是否测试netty
     * 需要做如下操作：
     * - 临时秘钥写成123456
     * - 心跳包的时候就直接放到登录缓存
     */
    boolean IS_TEST_NETTY = false;
    String TEMP_KEY = "2dc1f4d99e7fcadc";
}
