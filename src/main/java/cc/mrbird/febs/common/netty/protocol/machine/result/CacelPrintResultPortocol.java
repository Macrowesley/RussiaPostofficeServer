package cc.mrbird.febs.common.netty.protocol.machine.result;

import cc.mrbird.febs.common.netty.protocol.base.BaseProtocol;
import cc.mrbird.febs.common.netty.protocol.base.MachineToServiceProtocol;
import cc.mrbird.febs.common.netty.protocol.dto.CancelPrintResDto;
import cc.mrbird.febs.common.utils.BaseTypeUtils;
import cc.mrbird.febs.rcs.common.enums.FlowDetailEnum;
import cc.mrbird.febs.rcs.entity.PrintJob;
import cc.mrbird.febs.rcs.service.INoticeFrontService;
import cc.mrbird.febs.rcs.service.IPrintJobService;
import io.netty.channel.ChannelHandlerContext;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@NoArgsConstructor
@Component
public class CacelPrintResultPortocol extends MachineToServiceProtocol {
    @Autowired
    IPrintJobService printJobService;

    @Autowired
    INoticeFrontService noticeFrontService;

    public static final byte PROTOCOL_TYPE = (byte) 0xC8;

    //表头号长度
    private static final int REQ_ACNUM_LEN = 6;

    //SSH结果长度
    private static final int REQ_SSH_RES_LEN = 1;

    public static CacelPrintResultPortocol protocol;

    @PostConstruct
    public void init(){
        protocol = this;
    }

    @Override
    public BaseProtocol getOperator() {
        return protocol;
    }


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
        /*
        typedef  struct{
            unsigned char head;				    //0xAA
            unsigned char length[4];
            unsigned char type;					//0xC8
            unsigned char operateID[2];
            unsigned char acnum[6];             //机器表头号
            unsigned char version[3];           //版本号
            unsigned char result;				//CancelPrintResDto的json
            unsigned char check;				//校验位
            unsigned char tail;					//0xD0
        }__attribute__((packed))pcPrintJobCancelRes, *pcPrintJobCancelRes;

         */
        log.info("机器返回点击 取消 打印事件结果");
        int pos = getBeginPos();

        //表头号
        String acnum = BaseTypeUtils.byteToString(bytes, pos, REQ_ACNUM_LEN, BaseTypeUtils.UTF8);
        pos += REQ_ACNUM_LEN;

        //版本号
        String version = BaseTypeUtils.byteToString(bytes, pos, VERSION_LEN, BaseTypeUtils.UTF8);
        pos += VERSION_LEN;

        CancelPrintResDto resDto = parseEnctryptToObject(bytes, ctx, pos, REQ_ACNUM_LEN, CancelPrintResDto.class);


        PrintJob printJob = new PrintJob();
        printJob.setId(Integer.valueOf(resDto.getPrintJobId()));
        if ("1".equals(resDto.getRes())){
            //如果结果ok
            printJob.setFlowDetail(FlowDetailEnum.JobingPcCancelResOk.getCode());
        }else{
            printJob.setFlowDetail(FlowDetailEnum.JobingPcCancleResFail.getCode());
        }
        protocol.printJobService.updatePrintJob(printJob);

        log.info("{}PC点击 取消打印，机器返回的结果是：{}", acnum, resDto.toString());

        //通知前端
        protocol.noticeFrontService.notice(6, "1".equals(resDto.getRes()) ? "操作成功" : "操作失败");
        //返回
        byte[] data = new byte[]{(byte) 0x01};
        return getWriteContent(data);
    }
}
