package com.hhwy.system.core.service.impl;

import com.hhwy.common.core.utils.StringUtils;
import com.hhwy.common.security.service.TokenService;
import com.hhwy.system.core.domain.SysDeptFuncFlow;
import com.hhwy.system.core.domain.vo.FuncFlowQueryVo;
import com.hhwy.system.core.domain.vo.FuncFlowVo;
import com.hhwy.system.core.mapper.SysDeptFuncFlowMapper;
import com.hhwy.system.core.service.ISysDeptFuncFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 部门关联流程图Service业务层处理
 * 
 * @author 韩
 * @date 2023-02-14
 */
@Service
public class SysDeptFuncFlowServiceImpl implements ISysDeptFuncFlowService {

    @Autowired
    private SysDeptFuncFlowMapper sysDeptFuncFlowMapper;

    @Autowired
    private TokenService tokenService;

    /**
     * 查询部门关联流程图
     * 
     * @param id 部门关联流程图主键
     * @return 部门关联流程图
     */
    @Override
    public SysDeptFuncFlow selectSysDeptFuncFlowById(Long id) {
        return sysDeptFuncFlowMapper.selectSysDeptFuncFlowById(id);
    }

    /**
     * 查询部门关联流程图列表
     * 
     * @param sysDeptFuncFlow 部门关联流程图
     * @return 部门关联流程图
     */
    @Override
    public List<SysDeptFuncFlow> selectSysDeptFuncFlowList(SysDeptFuncFlow sysDeptFuncFlow) {
        return sysDeptFuncFlowMapper.selectSysDeptFuncFlowList(sysDeptFuncFlow);
    }

    /**
     * 根据部门id查询部门关联流程图列表
     * @param queryVo
     * @return
     */
    @Override
    public List<SysDeptFuncFlow> getFlowListByDeptId(FuncFlowQueryVo queryVo) {
        queryVo.setTenantKey(tokenService.getTenantKey());
        return sysDeptFuncFlowMapper.getFlowListByDeptId(queryVo);
    }

    /**
     * 新增部门关联流程图
     * 
     * @param sysDeptFuncFlow 部门关联流程图
     * @return 结果
     */
    @Override
    public int insertSysDeptFuncFlow(SysDeptFuncFlow sysDeptFuncFlow) {
        //判断是否已存在
        judgeIfExist(sysDeptFuncFlow);
        sysDeptFuncFlow.setTenantKey(tokenService.getTenantKey());
        return sysDeptFuncFlowMapper.insertSysDeptFuncFlow(sysDeptFuncFlow);
    }

    /**
     * 修改部门关联流程图
     * 
     * @param sysDeptFuncFlow 部门关联流程图
     * @return 结果
     */
    @Override
    public int updateSysDeptFuncFlow(SysDeptFuncFlow sysDeptFuncFlow) {
        //判断是否已存在
        judgeIfExist(sysDeptFuncFlow);
        return sysDeptFuncFlowMapper.updateSysDeptFuncFlow(sysDeptFuncFlow);
    }

    /**
     * 判断是否存在重复的数据
     * @param ipmSysDeptFuncFlow
     */
    public void judgeIfExist(SysDeptFuncFlow ipmSysDeptFuncFlow) {
        Long id = ipmSysDeptFuncFlow.getId();
        Long deptId = ipmSysDeptFuncFlow.getDeptId();
        String funModel = ipmSysDeptFuncFlow.getFunModel();
        boolean existFlag = this.ifExistFuncFlowByModelAndDeptId(id, funModel, deptId);
        if(existFlag){
            throw new RuntimeException("同一部门下功能模块不能重复！");
        }
    }

    /**
     * 根据部门id和功能模块标识查询是否存在数据
     * @param id
     * @param funModel
     * @param deptId
     * @return
     */
    public boolean ifExistFuncFlowByModelAndDeptId(Long id, String funModel ,Long deptId){
        int count = sysDeptFuncFlowMapper.selectFuncFlowCountByModelAndDeptId(id,funModel,deptId,tokenService.getTenantKey());
        return count > 0;
    }

    /**
     * 批量删除部门关联流程图
     * 
     * @param ids 需要删除的部门关联流程图主键
     * @return 结果
     */
    @Override
    public int deleteSysDeptFuncFlowByIds(Long[] ids) {
        return sysDeptFuncFlowMapper.deleteSysDeptFuncFlowByIds(ids);
    }

    /**
     * 删除部门关联流程图信息
     * 
     * @param id 部门关联流程图主键
     * @return 结果
     */
    @Override
    public int deleteSysDeptFuncFlowById(Long id) {
        return sysDeptFuncFlowMapper.deleteSysDeptFuncFlowById(id);
    }

    /**
     * 根据功能模块编码和部门id获取流程图集合
     * @param queryVo
     * @return
     */
    @Override
    public List<FuncFlowVo> getFuncFlowVoListByModelAndDeptId(FuncFlowQueryVo queryVo) {
        queryVo.setTenantKey(tokenService.getTenantKey());
        List<FuncFlowVo> funcFlowVoList = new ArrayList<>();
        SysDeptFuncFlow funcFlow = sysDeptFuncFlowMapper.getFuncFlowByModelAndDeptId(queryVo);
        if(funcFlow == null){
            return null;
        }
        String actCode = funcFlow.getActCode();
        String actName = funcFlow.getActName();
        String[] actCodeArray = StringUtils.split(actCode, ",");
        String[] actNameArray = StringUtils.split(actName, ",");
        if(actCodeArray == null){
            return null;
        }
        for (int i = 0; i < actCodeArray.length; i++) {
            FuncFlowVo funcFlowVo = new FuncFlowVo();
            funcFlowVo.setFunModel(queryVo.getFunModel());
            funcFlowVo.setDeptId(queryVo.getDeptId());
            funcFlowVo.setActCode(actCodeArray[i]);
            if(actNameArray != null && i < actNameArray.length){
                String processActName = actNameArray[i];
                if(StringUtils.isNotBlank(processActName)){
                    funcFlowVo.setActName(processActName);
                }else{
                    funcFlowVo.setActName(actCodeArray[i]);
                }
            }else{
                funcFlowVo.setActName(actCodeArray[i]);
            }
            funcFlowVoList.add(funcFlowVo);
        }
        return funcFlowVoList;
    }
}
