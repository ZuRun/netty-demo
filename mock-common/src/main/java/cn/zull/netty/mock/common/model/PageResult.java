package cn.zull.netty.mock.common.model;

import lombok.Data;

/**
 * @author zurun
 * @date 2019/7/18 15:04:19
 */
@Data
public class PageResult extends Result {
    private int count;

    public static PageResult ok() {
        return new PageResult();
    }

    public PageResult addData(Object data) {
        this.setData(data);
        return this;
    }

    public PageResult addCount(int count) {
        this.setCount(count);
        return this;
    }
}
