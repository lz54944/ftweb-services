package com.hhwy.system.core.controller;

import com.hhwy.common.core.utils.StringUtils;
import com.hhwy.common.core.web.controller.BaseController;
import com.hhwy.common.core.web.domain.AjaxResult;
import com.hhwy.common.core.web.page.TableDataInfo;
import com.hhwy.system.core.domain.SysDeptFuncFlow;
import com.hhwy.system.core.domain.vo.FuncFlowQueryVo;
import com.hhwy.system.core.service.ISysDeptFuncFlowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门关联流程图Controller
 * 
 * @author 韩
 * @date 2023-02-14
 */
@Api(tags = "部门关联流程图")
@RestController
@RequestMapping("/funcFlow")
public class SysDeptFuncFlowController extends BaseController {

    @Autowired
    private ISysDeptFuncFlowService sysDeptFuncFlowService;

    /**
     * 查询部门关联流程图列表
     */
    @GetMapping("/list")
    public TableDataInfo list(SysDeptFuncFlow sysDeptFuncFlow) {
        startPage();
        List<SysDeptFuncFlow> list = sysDeptFuncFlowService.selectSysDeptFuncFlowList(sysDeptFuncFlow);
        return getDataTable(list);
    }

    /**
     * 根据功能模块编码和部门id获取流程图集合
     * @param queryVo
     * @return
     */
    @GetMapping("getFuncFlowVoListByModelAndDeptId")
    public AjaxResult getFuncFlowVoListByModelAndDeptId(FuncFlowQueryVo queryVo){
        if(StringUtils.isBlank(queryVo.getFunModel())){
            throw new RuntimeException("功能模块标识不能为空！");
        }
        if(queryVo.getDeptId() == null){
            throw new RuntimeException("部门id不能为空！");
        }
        return AjaxResult.success(sysDeptFuncFlowService.getFuncFlowVoListByModelAndDeptId(queryVo));
    }

    /**
     * 根据部门id查询部门关联流程图列表
     * @param queryVo
     * @return
     */
    @ApiOperation("根据部门id查询部门关联流程图列表")
    @GetMapping("getFlowListByDeptId")
    public AjaxResult getFlowListByDeptId(FuncFlowQueryVo queryVo){
        if(queryVo.getDeptId() == null){
            throw new RuntimeException("部门id不能为空！");
        }
        startPage();
        List<SysDeptFuncFlow> list = sysDeptFuncFlowService.getFlowListByDeptId(queryVo);
        return getDataTableAjaxResult(list);
    }

    /**
     * 获取部门关联流程图详细信息
     */
    @GetMapping(value = "get/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(sysDeptFuncFlowService.selectSysDeptFuncFlowById(id));
    }

    /**
     * 新增部门关联流程图
     */
    @ApiOperation("新增部门关联流程图")
    @PostMapping("add")
    public AjaxResult add(@RequestBody SysDeptFuncFlow sysDeptFuncFlow) {
        return toAjax(sysDeptFuncFlowService.insertSysDeptFuncFlow(sysDeptFuncFlow));
    }

    /**
     * 修改部门关联流程图
     */
    @ApiOperation("修改部门关联流程图")
    @PutMapping("edit")
    public AjaxResult edit(@RequestBody SysDeptFuncFlow sysDeptFuncFlow) {
        return toAjax(sysDeptFuncFlowService.updateSysDeptFuncFlow(sysDeptFuncFlow));
    }

    /**
     * 删除部门关联流程图
     */
	@DeleteMapping("remove/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(sysDeptFuncFlowService.deleteSysDeptFuncFlowByIds(ids));
    }
}
