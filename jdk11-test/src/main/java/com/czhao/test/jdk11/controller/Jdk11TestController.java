package com.czhao.test.jdk11.controller;

import com.czhao.test.jdk11.db.po.TbEmployee;
import com.czhao.test.jdk11.db.po.TbOrg;
import com.czhao.test.jdk11.service.Jdk11TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhaochun
 */
@RestController
@RequestMapping("/jdk11/test")
public class Jdk11TestController {
    @Autowired
    private Jdk11TestService jdk11TestService;

    @GetMapping("/hello")
    public String hello(String name) {
        return "Hello " + name;
    }

    @PostMapping("/helloByPost")
    public String helloPost(@RequestBody String name) {
        return "Hello " + name + " by post!";
    }

    @PostMapping("/org")
    public int addOrg(@RequestBody TbOrg tbOrg) {
        return jdk11TestService.addOrg(tbOrg);
    }

    @PostMapping("/employee")
    public int addEmployee(@RequestBody TbEmployee employee) {
        return jdk11TestService.addEmployee(employee);
    }

    @GetMapping("/employee/{name}")
    public TbEmployee getEmployeeByName(@PathVariable String name) {
        return jdk11TestService.getEmployeeByName(name);
    }
}
