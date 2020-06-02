//******************************************************************************
// 版权所有(c)，科大国创，保留所有权利。
//******************************************************************************

package com.czhao.test.jdk11.db.po;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 *
 * 本类自动生成，对应的表为[tb_employee].
 */
@lombok.Getter
@lombok.Setter
@lombok.ToString
public class TbEmployee {
    /**
     * id (tb_employee.id).
     */
    private Integer id;

    /**
     * 员工姓名 (tb_employee.employee_name).
     */
    private String employeeName;

    /**
     * 员工性别(0:女;1:男) (tb_employee.employee_sex).
     */
    private Byte employeeSex;

    /**
     * 员工入职年月日 (tb_employee.employee_entry_ymd).
     */
    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date employeeEntryYmd;

    /**
     * 员工级别(0:初级;1:中级;2:高级) (tb_employee.employee_level).
     */
    private Byte employeeLevel;

    /**
     * 员工所属组织 (tb_employee.employee_org_id).
     */
    private Integer employeeOrgId;
}