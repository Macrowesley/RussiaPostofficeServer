package cc.mrbird.febs.rcs.service;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.netty.protocol.dto.ForeseenFmReqDTO;
import cc.mrbird.febs.common.utils.AESUtils;
import cc.mrbird.febs.rcs.dto.machine.PrintProgressInfo;
import cc.mrbird.febs.rcs.entity.Contract;
import cc.mrbird.febs.rcs.entity.PrintJob;
import io.netty.channel.ChannelHandlerContext;

/**
 * foreseen流程模板
 */
public abstract class AbstractForeseenProcessTemplate {

    /**
     * 获取返回给fm的foreseen数据
     * @return
     */
    public byte[] foreseenProcess(ForeseenFmReqDTO foreseenFmReqDTO, PrintJob dbPrintJob, ChannelHandlerContext ctx, PrintProgressInfo pcPrintProgressInfo){
        Contract dbContract = sendForeseenToRussia(foreseenFmReqDTO, dbPrintJob, ctx, pcPrintProgressInfo);
        String foreseenId =  createForeseenId(foreseenFmReqDTO);
        return buildForeseenFmRespData(dbPrintJob, ctx, foreseenId, dbContract, pcPrintProgressInfo);
    }

    protected String createForeseenId(ForeseenFmReqDTO foreseenFmReqDTO){
        String foreseenId = "";
        if (FebsConstant.IS_TEST_NETTY) {
            foreseenId = foreseenFmReqDTO.getId();
        }else{
            foreseenId = AESUtils.createUUID();
            foreseenFmReqDTO.setId(foreseenId);
        }
        return foreseenId;
    }


    /**
     * 发送数据给俄罗斯
     */
    public abstract Contract sendForeseenToRussia(ForeseenFmReqDTO foreseenFmReqDTO, PrintJob dbPrintJob, ChannelHandlerContext ctx, PrintProgressInfo pcPrintProgressInfo);

    /**
     * 构建返回给机器的数据
     * @return
     */
    public abstract byte[] buildForeseenFmRespData(PrintJob dbPrintJob, ChannelHandlerContext ctx, String foreseenId, Contract dbContract, PrintProgressInfo pcPrintProgressInfo);
}
