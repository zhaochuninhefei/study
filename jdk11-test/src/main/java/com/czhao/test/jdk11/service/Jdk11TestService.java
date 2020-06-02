package com.czhao.test.jdk11.service;

import com.czhao.test.jdk11.db.dao.TbEmployeeMapper;
import com.czhao.test.jdk11.db.dao.TbOrgMapper;
import com.czhao.test.jdk11.db.po.TbEmployee;
import com.czhao.test.jdk11.db.po.TbEmployeeExample;
import com.czhao.test.jdk11.db.po.TbOrg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhaochun
 */
@Service
@Slf4j
public class Jdk11TestService {
    @Autowired
    private TbOrgMapper tbOrgMapper;
    @Autowired
    private TbEmployeeMapper tbEmployeeMapper;

    public int addOrg(TbOrg org) {
        int result = tbOrgMapper.insert(org);
        log.info("addOrg result is {}", result);
        return result;
    }

    public int addEmployee(TbEmployee employee) {
        int result = tbEmployeeMapper.insert(employee);
        return result;
    }

    public TbEmployee getEmployeeByName(String name) {
        TbEmployeeExample employeeExample = new TbEmployeeExample();
        employeeExample.createCriteria().andEmployeeNameEqualTo(name);
        List<TbEmployee> employeeList = tbEmployeeMapper.selectByExample(employeeExample);
        if (employeeList == null || employeeList.size() == 0) {
            throw new RuntimeException("not found!");
        }
        return employeeList.get(0);
    }

    public List<TbOrg> getAllOrgs() {
        return tbOrgMapper.selectByExample(null);
    }

    public List<TbEmployee> getAllEmployees() {
        return tbEmployeeMapper.selectByExample(null);
    }
}
