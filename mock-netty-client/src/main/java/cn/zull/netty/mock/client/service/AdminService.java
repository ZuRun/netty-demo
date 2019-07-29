package cn.zull.netty.mock.client.service;

import cn.zull.netty.mock.client.netty.MockClientFactory;
import cn.zull.netty.mock.common.util.RandomUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author zurun
 * @date 2019-07-19 21:09:02
 */
@Slf4j
@Service
public class AdminService {
    @Value("${cu.zull.netty.mock.server.tcpHost}")
    private String host;

    @Value("${cu.zull.netty.mock.server.nPort}")
    private int nPort;

    @Value("${cu.zull.netty.mock.server.beginPort}")
    private int beginPort;


    @Autowired
    MockClientFactory factory;

    public void newConnect(int size) {
        int maxPort = beginPort + nPort - 1;
        for (int i = 0; i < size; i++) {
            int port = RandomUtils.randomNumber(beginPort, maxPort);
            factory.newClient(host, port);
        }
    }
}