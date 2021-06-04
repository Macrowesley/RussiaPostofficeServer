package cc.mrbird.febs.common.netty.protocol.machine.publickey;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.netty.protocol.base.MachineToServiceProtocol;
import cc.mrbird.febs.common.service.RedisService;
import cc.mrbird.febs.common.utils.BaseTypeUtils;
import cc.mrbird.febs.rcs.api.ServiceInvokeManager;
import cc.mrbird.febs.rcs.common.enums.FMResultEnum;
import cc.mrbird.febs.rcs.common.enums.FlowDetailEnum;
import cc.mrbird.febs.rcs.common.enums.FlowEnum;
import cc.mrbird.febs.rcs.common.enums.ResultEnum;
import cc.mrbird.febs.rcs.common.exception.FmException;
import cc.mrbird.febs.rcs.common.kit.DateKit;
import cc.mrbird.febs.rcs.dto.manager.ApiResponse;
import cc.mrbird.febs.rcs.dto.manager.PublicKeyDTO;
import cc.mrbird.febs.rcs.entity.PublicKey;
import cc.mrbird.febs.rcs.service.IPublicKeyService;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 机器收到了秘钥，告知服务器，服务器把最新秘钥通知俄罗斯，俄罗斯得到最新秘钥后，数据库更新
 */
@Slf4j
@Component
public class UpdatePrivateKeyResultPortocol extends MachineToServiceProtocol {
    @Autowired
    RedisService redisService;

    @Autowired
    ServiceInvokeManager serviceInvokeManager;

    @Autowired
    IPublicKeyService publicKeyService;

    public static final byte PROTOCOL_TYPE = (byte) 0xB9;

    //表头号长度
    private static final int REQ_ACNUM_LEN = 6;

    private static final String OPERATION_NAME = "UpdatePrivateKeyResultPortocol";

    /**
     * 获取协议类型
     * @return
     */
    @Override
    public byte getProtocolType() {
        return PROTOCOL_TYPE;
    }

    /**
     * 解析并返回结果流
     *
     * @param bytes
     * @param ctx
     * @return
     */
    @Override
    public synchronized byte[] parseContentAndRspone(byte[] bytes, ChannelHandlerContext ctx) throws Exception {
        String version = null;
        try {
        /*
            typedef  struct{
                unsigned char head;				    //0xAA
                unsigned char length[2];				//
                unsigned char type;					//0xB9
                unsigned char acnum[6];             //机器表头号
                unsigned char version[3];           //版本号
                unsigned char content[?];			//加密后内容: result(1成功 0 失败) + frankMachineId
                unsigned char check;				//校验位
                unsigned char tail;					//0xD0
            }__attribute__((packed))privateKeyRes, *privateRes;
         */
            //防止频繁操作 需要时间，暂时假设一次闭环需要1分钟，成功或者失败都返回结果
            String key = ctx.channel().id().toString() + "_" + OPERATION_NAME;
            if (redisService.hasKey(key)) {
                return getOverTimeResult(version, ctx, key, FMResultEnum.Overtime.getCode());
            } else {
                log.info("channelId={}的操作记录放入redis", key);
                redisService.set(key, "wait", WAIT_TIME);
            }
            log.info("机器开始 ForeseensCancel");

            int pos = TYPE_LEN;

            //表头号
            String acnum = BaseTypeUtils.byteToString(bytes, pos, REQ_ACNUM_LEN, BaseTypeUtils.UTF8);
            pos += REQ_ACNUM_LEN;

            //版本号
            version = BaseTypeUtils.byteToString(bytes, pos, VERSION_LEN, BaseTypeUtils.UTF8);
            pos += VERSION_LEN;

            switch (version) {
                case FebsConstant.FmVersion1:
                    String dectryptContent = getDecryptContent(bytes, ctx, pos, REQ_ACNUM_LEN).trim();
                    String fmRes = dectryptContent.substring(0, 1);
                    String frankMachineId = dectryptContent.substring(1, dectryptContent.length());

                    /**
                     typedef  struct{
                     unsigned char length[2];				 //2个字节
                     unsigned char head;				 	     //0xB9
                     unsigned char content;				     //加密内容: result(长度为2 0 失败 1 成功) + version
                     unsigned char check;				     //校验位
                     unsigned char tail;					     //0xD0
                     }__attribute__((packed))privateKeyRes, *privateKeyRes;
                     */
                    if (fmRes != "1"){
                        //如果机器没有成功更新私钥，让机器重新更新，数据库不改变
                        return getErrorResult(ctx, version, OPERATION_NAME, FMResultEnum.PrivateKeyNeedUpdate.getCode());
                    }
                    //如果机器privateKey更新成功，而且publickey没有闭环，那就通知俄罗斯更新，俄罗斯更新成功后，更新数据库状态
                    PublicKey dbPubliceKey = publicKeyService.findByFrankMachineId(frankMachineId);

                    if (dbPubliceKey.getFlow() != FlowEnum.FlowEnd.getCode()) {
                        //返回给俄罗斯
                        PublicKeyDTO publicKeyDTO = new PublicKeyDTO();
                        publicKeyDTO.setKey("-----BEGIN PUBLIC KEY----- " + dbPubliceKey.getPublicKey() + " -----END PUBLIC KEY-----");
                        publicKeyDTO.setRevision(dbPubliceKey.getRevision());
                        publicKeyDTO.setAlg(dbPubliceKey.getAlg());
                        publicKeyDTO.setExpireDate(DateKit.createRussiatime(dbPubliceKey.getExpireTime()));

                        ApiResponse publickeyResponse = serviceInvokeManager.publicKey(frankMachineId, publicKeyDTO);

                        if (!publickeyResponse.isOK()) {
                            if (publickeyResponse.getCode() == ResultEnum.UNKNOW_ERROR.getCode()) {
                                //未接收到俄罗斯返回,返回失败信息给机器，保存进度
                                publicKeyService.changeFlowInfo(dbPubliceKey,FlowDetailEnum.PublicKeyErrorFailUnKnow);
                                log.info("服务器收到了设备{}发送的auth协议，发送了消息给俄罗斯，然后发送了publickey给俄罗斯，但是没有收到返回", frankMachineId);
                                throw new FmException(FMResultEnum.VisitRussiaTimedOut.getCode(), "auth.isOK() false ");
                            } else {
                                //收到了俄罗斯返回，但是俄罗斯不同意，返回失败信息给机器
                                publicKeyService.changeFlowInfo(dbPubliceKey,FlowDetailEnum.PublicKeyErrorFail4xxError);
                                log.info("服务器收到了设备{}发送的auth协议，发送了消息给俄罗斯，然后发送了publickey给俄罗斯，但是俄罗斯不同意，返回失败信息给机器", frankMachineId);
                                throw new FmException(FMResultEnum.RussiaServerRefused.getCode(), "auth.isOK() false ");
                            }
                        }
                        publicKeyService.changeFlowInfo(dbPubliceKey,FlowDetailEnum.PublicKeyEndSuccess);
                        log.info("服务器收到了设备{}发送的auth协议，发送了消息给俄罗斯，然后发送了publickey给俄罗斯，收到了俄罗斯返回", frankMachineId);
                    }else{
                        log.error("publickey 已经闭环了，无序操作");
                        throw new FmException(FMResultEnum.DonotAgain.getCode(), "publickey 已经闭环了，无序操作 ");
                    }

                default:
                    return getErrorResult(ctx, version, OPERATION_NAME, FMResultEnum.VersionError.getCode());
            }

        } catch (Exception e) {
            log.error(OPERATION_NAME + "error info = " + e.getMessage());
            return getErrorResult(ctx, version, OPERATION_NAME);
        } finally {
            log.info("机器结束 ForeseensCancelPortocol");
        }
    }
}
