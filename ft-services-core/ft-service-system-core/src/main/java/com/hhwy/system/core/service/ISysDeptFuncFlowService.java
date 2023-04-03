package com.hhwy.system.core.service;

import com.hhwy.system.core.domain.SysDeptFuncFlow;
import com.hhwy.system.core.domain.vo.FuncFlowQueryVo;
import com.hhwy.system.core.domain.vo.FuncFlowVo;

import java.util.List;

/**
 * 部门关联流程图Service接口
 * 
 * @author 韩
 * @date 2023-02-14
 */
public interface ISysDeptFuncFlowService {
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
     * @param sysDeptFuncFlow 部门关联流程图
     * @return 部门关联流程图集合
     */
    List<SysDeptFuncFlow> selectSysDeptFuncFlowList(SysDeptFuncFlow sysDeptFuncFlow);

    /**
     * 根据部门id查询部门关联流程图列表
     * @param queryVo
     * @return
     */
    List<SysDeptFuncFlow> getFlowListByDeptId(FuncFlowQueryVo queryVo);

    /**
     * 新增部门关联流程图
     * 
     * @param sysDeptFuncFlow 部门关联流程图
     * @return 结果
     */
    int insertSysDeptFuncFlow(SysDeptFuncFlow sysDeptFuncFlow);

    /**
     * 修改部门关联流程图
     * 
     * @param sysDeptFuncFlow 部门关联流程图
     * @return 结果
     */
    int updateSysDeptFuncFlow(SysDeptFuncFlow sysDeptFuncFlow);

    /**
     * 批量删除部门关联流程图
     * 
     * @param ids 需要删除的部门关联流程图主键集合
     * @return 结果
     */
    int deleteSysDeptFuncFlowByIds(Long[] ids);

    /**
     * 删除部门关联流程图信息
     * 
     * @param id 部门关联流程图主键
     * @return 结果
     */
    int deleteSysDeptFuncFlowById(Long id);

    /**
     * 根据功能模块编码和部门id获取流程图集合
     * @param queryVo
     * @return
     */
    List<FuncFlowVo> getFuncFlowVoListByModelAndDeptId(FuncFlowQueryVo queryVo);
}
