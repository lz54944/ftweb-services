package com.hhwy.system.core.controller;

import com.hhwy.common.core.web.controller.BaseController;
import com.hhwy.common.core.web.domain.AjaxResult;
import com.hhwy.common.core.web.page.TableDataInfo;
import com.hhwy.common.log.annotation.Log;
import com.hhwy.common.log.enums.BusinessType;
import com.hhwy.system.core.domain.SysBusinessFormItem;
import com.hhwy.system.core.service.ISysBusinessFormItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 业务流程单子Controller
 * 
 * @author hanchuanchuan
 * @date 2021-09-16
 */
@Controller
@RequestMapping("/sysBusinessFormItem")
public class SysBusinessFormItemController extends BaseController {
    private String prefix = "sysBusinessFormItem";

    @Autowired
    private ISysBusinessFormItemService sysBusinessFormItemService;

    //@RequiresPermissions("sysBusinessFormItem:sysBusinessFormItem:view")
    @GetMapping()
    public String sysBusinessFormItem() {
        return prefix + "/sysBusinessFormItem";
    }

    /**
     * 查询业务流程单子列表
     */
    //@RequiresPermissions("sysBusinessFormItem:sysBusinessFormItem:list")
    @GetMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysBusinessFormItem sysBusinessFormItem) {
        startPage();
        List<SysBusinessFormItem> list = sysBusinessFormItemService.selectSysBusinessFormItemList(sysBusinessFormItem);
        return getDataTable(list);
    }



    /**
     * 新增保存业务流程单子
     */
    //@RequiresPermissions("sysBusinessFormItem:sysBusinessFormItem:add")
    @Log(title = "业务流程单子", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@RequestBody SysBusinessFormItem sysBusinessFormItem) {
        return toAjax(sysBusinessFormItemService.insertSysBusinessFormItem(sysBusinessFormItem));
    }
    /**
     * 修改保存业务流程单子
     */
    //@RequiresPermissions("sysBusinessFormItem:sysBusinessFormItem:edit")
    @Log(title = "业务流程单子", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@RequestBody SysBusinessFormItem sysBusinessFormItem) {
        return toAjax(sysBusinessFormItemService.updateSysBusinessFormItem(sysBusinessFormItem));
    }

    /**
     * 删除业务流程单子
     */
    //@RequiresPermissions("sysBusinessFormItem:sysBusinessFormItem:remove")
    @Log(title = "业务流程单子", businessType = BusinessType.DELETE)
    @GetMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String id) {
        List<SysBusinessFormItem> sysBusinessFormItem = sysBusinessFormItemService.selectSysBusinessFormItemByIdList(id);
        sysBusinessFormItem.forEach(l->{
            if (l == null){
                return ;
            }
        });
        int i = sysBusinessFormItemService.deleteSysBusinessFormItemByIds(id);
        return AjaxResult.success("删除成功",i);
    }

    /**
     * 根据业务id查询子表列表
     * @param flowFormId
     * @return
     */
    @GetMapping("/ListById")
    @ResponseBody
    public AjaxResult listById(String flowFormId){
       List<SysBusinessFormItem> sysBusinessFormItems = sysBusinessFormItemService.selectOmsList(flowFormId);
       return AjaxResult.success("查询成功",sysBusinessFormItems);
    }


//    /**
//     * @Description 根据数据库表名code 查询所有 对应条件字段
//     * @Param [colName]
//     * @return com.hhwy.common.core.web.domain.AjaxResult
//     * @Author ZHUZHIJIE
//     * @Date  2022/5/23
//     **/
//    @GetMapping("/queryDbWhereParamListByColName")
//    @ResponseBody
//    public AjaxResult queryDbWhereParamListByColName(@RequestParam("colName") String colName){
//        List<SysBusinessFormItemResult> dbWhereParamList = sysBusinessFormItemService.queryDbWhereParamListByColName(colName);
//        return AjaxResult.success("查询成功",dbWhereParamList);
//    }

}
