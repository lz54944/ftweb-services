package com.hhwy.system.core.mapper;

import com.hhwy.system.core.domain.SysDeptFuncFlow;
import com.hhwy.system.core.domain.vo.FuncFlowQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 部门关联流程图Mapper接口
 * 
 * @author 韩
 * @date 2023-02-14
 */
@Repository
public interface SysDeptFuncFlowMapper {
    /**
     * 查询部门关联流程图
     * 
     * @param id 部门关联流程图主键
     * @return 部门关联流程图
     */
    SysDeptFuncFlow selectSysDeptFuncFlowById(Long id);

    /**
     * 查询部门关联流程图列表
     * 
     * @param ipmSysDeptFuncFlow 部门关联流程图
     * @return 部门关联流程图集合
     */
    List<SysDeptFuncFlow> selectSysDeptFuncFlowList(SysDeptFuncFlow ipmSysDeptFuncFlow);

    /**
     * 新增部门关联流程图
     * 
     * @param ipmSysDeptFuncFlow 部门关联流程图
     * @return 结果
     */
    int insertSysDeptFuncFlow(SysDeptFuncFlow ipmSysDeptFuncFlow);

    /**
     * 修改部门关联流程图
     * 
     * @param ipmSysDeptFuncFlow 部门关联流程图
     * @return 结果
     */
    int updateSysDeptFuncFlow(SysDeptFuncFlow ipmSysDeptFuncFlow);

    /**
     * 删除部门关联流程图
     * 
     * @param id 部门关联流程图主键
     * @return 结果
     */
    int deleteSysDeptFuncFlowById(Long id);

    /**
     * 批量删除部门关联流程图
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteSysDeptFuncFlowByIds(Long[] ids);

    /**
     * 根据部门id查询部门关联流程图列表
     * @param queryVo
     * @return
     */
    List<SysDeptFuncFlow> getFlowListByDeptId(FuncFlowQueryVo queryVo);

    /**
     * 根据功能模块编码和部门id获取部门关联流程图
     * @param queryVo
     * @return
     */
    SysDeptFuncFlow getFuncFlowByModelAndDeptId(FuncFlowQueryVo queryVo);

    /**
     * 根据功能模块编码和部门id获取数据数量（修改时排除主键id）
     * @param id
     * @param funModel
     * @param deptId
     * @param tenantKey
     * @return
     */
    int selectFuncFlowCountByModelAndDeptId(@Param("id") Long id, @Param("funModel") String funModel, @Param("deptId") Long deptId,@Param("tenantKey")  String tenantKey);
}
