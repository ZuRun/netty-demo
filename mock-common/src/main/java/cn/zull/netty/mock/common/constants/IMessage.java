package cn.zull.netty.mock.common.constants;

/**
 * @author zurun
 * @date 2018/3/20 23:46:11
 */
public interface IMessage {

    /**
     * 获取异常码
     *
     * @return
     */
    Integer getErrCode();

    /**
     * 获取异常描述信息
     *
     * @return
     */
    String getErrMsg();
}
