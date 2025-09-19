//******************************************************************************
// 版权所有(c)，科大国创，保留所有权利。
//******************************************************************************

package com.czhao.test.jdk11.db.dao;

import com.czhao.test.jdk11.db.po.TbEmployee;
import com.czhao.test.jdk11.db.po.TbEmployeeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbEmployeeMapper {
    /**
     * 根据Example条件查询件数.
     *
     * @param example TbEmployeeExample
     * @return long
     */
    long countByExample(TbEmployeeExample example);

    /**
     * 根据Example条件删除数据.
     *
     * @param example TbEmployeeExample
     * @return int
     */
    int deleteByExample(TbEmployeeExample example);

    /**
     * 根据主键删除数据.
     *
     * @param id Integer
     * @return int
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 插入数据.
     *
     * @param record TbEmployee
     * @return int
     */
    int insert(TbEmployee record);

    /**
     * 插入数据（仅仅插入已设值项目）.
     *
     * @param record TbEmployee
     * @return int
     */
    int insertSelective(TbEmployee record);

    /**
     * 根据Example条件查询数据.
     *
     * @param example TbEmployeeExample
     * @return java.util.List<com.czhao.test.jdk11.db.po.TbEmployee>
     */
    List<TbEmployee> selectByExample(TbEmployeeExample example);

    /**
     * 根据主键获取一条数据.
     *
     * @param id Integer
     * @return com.czhao.test.jdk11.db.po.TbEmployee
     */
    TbEmployee selectByPrimaryKey(Integer id);

    /**
     * 根据Example条件来更新数据（仅仅更新已设值项目）.
     *
     * @param record TbEmployee
     * @param example TbEmployeeExample
     * @return int
     */
    int updateByExampleSelective(@Param("record") TbEmployee record, @Param("example") TbEmployeeExample example);

    /**
     * 根据Example条件来更新数据.
     *
     * @param record TbEmployee
     * @param example TbEmployeeExample
     * @return int
     */
    int updateByExample(@Param("record") TbEmployee record, @Param("example") TbEmployeeExample example);

    /**
     * 根据主键更新数据（仅仅更新已设值项目）.
     *
     * @param record TbEmployee
     * @return int
     */
    int updateByPrimaryKeySelective(TbEmployee record);

    /**
     * 根据主键来更新数据库记录.
     *
     * @param record TbEmployee
     * @return int
     */
    int updateByPrimaryKey(TbEmployee record);
}