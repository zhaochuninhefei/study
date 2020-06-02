//******************************************************************************
// 版权所有(c)，科大国创，保留所有权利。
//******************************************************************************

package com.czhao.test.jdk11.db.dao;

import com.czhao.test.jdk11.db.po.TbOrg;
import com.czhao.test.jdk11.db.po.TbOrgExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbOrgMapper {
    /**
     * 根据Example条件查询件数.
     *
     * @param example TbOrgExample
     * @return long
     */
    long countByExample(TbOrgExample example);

    /**
     * 根据Example条件删除数据.
     *
     * @param example TbOrgExample
     * @return int
     */
    int deleteByExample(TbOrgExample example);

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
     * @param record TbOrg
     * @return int
     */
    int insert(TbOrg record);

    /**
     * 插入数据（仅仅插入已设值项目）.
     *
     * @param record TbOrg
     * @return int
     */
    int insertSelective(TbOrg record);

    /**
     * 根据Example条件查询数据.
     *
     * @param example TbOrgExample
     * @return java.util.List<com.czhao.test.jdk11.db.po.TbOrg>
     */
    List<TbOrg> selectByExample(TbOrgExample example);

    /**
     * 根据主键获取一条数据.
     *
     * @param id Integer
     * @return com.czhao.test.jdk11.db.po.TbOrg
     */
    TbOrg selectByPrimaryKey(Integer id);

    /**
     * 根据Example条件来更新数据（仅仅更新已设值项目）.
     *
     * @param record TbOrg
     * @param example TbOrgExample
     * @return int
     */
    int updateByExampleSelective(@Param("record") TbOrg record, @Param("example") TbOrgExample example);

    /**
     * 根据Example条件来更新数据.
     *
     * @param record TbOrg
     * @param example TbOrgExample
     * @return int
     */
    int updateByExample(@Param("record") TbOrg record, @Param("example") TbOrgExample example);

    /**
     * 根据主键更新数据（仅仅更新已设值项目）.
     *
     * @param record TbOrg
     * @return int
     */
    int updateByPrimaryKeySelective(TbOrg record);

    /**
     * 根据主键来更新数据库记录.
     *
     * @param record TbOrg
     * @return int
     */
    int updateByPrimaryKey(TbOrg record);
}