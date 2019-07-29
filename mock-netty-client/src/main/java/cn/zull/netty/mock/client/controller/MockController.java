package cn.zull.netty.mock.client.controller;

import cn.zull.netty.mock.client.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zurun
 * @date 2019-07-19 21:13:36
 */
@RestController
@RequestMapping("admin")
public class MockController {
    @Autowired
    AdminService adminService;

    @GetMapping("new/{size}")
    public void connect(@PathVariable int size) {
        adminService.newConnect(size);
    }
}
