package cc.mrbird.febs.rcs.service.impl;

import cc.mrbird.febs.common.netty.protocol.dto.ForeseenFmReqDTO;
import cc.mrbird.febs.rcs.dto.machine.PrintProgressInfo;
import cc.mrbird.febs.rcs.entity.Contract;
import cc.mrbird.febs.rcs.entity.PrintJob;
import cc.mrbird.febs.rcs.service.AbstractForeseenProcessTemplate;
import io.netty.channel.ChannelHandlerContext;

/**
 * PC的foreseen流程
 */
public class ForeseenPcProcessTemplate extends AbstractForeseenProcessTemplate {
    /**
     * 发送数据给俄罗斯
     *
     * @param foreseenFmReqDTO
     * @param dbPrintJob
     * @param ctx
     * @param pcPrintProgressInfo
     */
    @Override
    public Contract sendForeseenToRussia(ForeseenFmReqDTO foreseenFmReqDTO, PrintJob dbPrintJob, ChannelHandlerContext ctx, PrintProgressInfo pcPrintProgressInfo) {
        return null;
    }

    /**
     * 构建返回给机器的数据
     *
     * @param dbPrintJob
     * @param ctx
     * @param foreseenId
     * @param dbContract
     * @param pcPrintProgressInfo
     * @return
     */
    @Override
    public byte[] buildForeseenFmRespData(PrintJob dbPrintJob, ChannelHandlerContext ctx, String foreseenId, Contract dbContract, PrintProgressInfo pcPrintProgressInfo) {
        return new byte[0];
    }
}
