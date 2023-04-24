package com.hhwy.system.core.controller;

import com.hhwy.common.core.utils.SecurityUtils;
import com.hhwy.common.core.utils.StringUtils;
import com.hhwy.common.core.utils.poi.ExcelUtils;
import com.hhwy.common.core.web.controller.BaseController;
import com.hhwy.common.core.web.domain.AjaxResult;
import com.hhwy.common.log.annotation.Log;
import com.hhwy.common.log.enums.BusinessType;
import com.hhwy.common.security.annotation.PreAuthorize;
import com.hhwy.system.core.domain.SysDictData;
import com.hhwy.system.core.service.ISysDictDataService;
import com.hhwy.system.core.service.ISysDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 数据字典信息
 *
 * @author hhwy
 */
@RestController
@RequestMapping("/dict/data")
public class SysDictDataController extends BaseController {
    @Autowired
    private ISysDictDataService dictDataService;

    @Autowired
    private ISysDictTypeService dictTypeService;

    @PreAuthorize(hasPermi = "system:dict:query")
    @GetMapping("/list")
    public AjaxResult list(SysDictData dictData) {
        startPage();
        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
        return getDataTableAjaxResult(list);
    }

    @Log(title = "字典数据", businessType = BusinessType.EXPORT)
    @PreAuthorize(hasPermi = "system:dict:export")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysDictData dictData) throws IOException {
        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
        ExcelUtils<SysDictData> util = new ExcelUtils<SysDictData>(SysDictData.class);
        util.exportExcel(response, list, "字典数据");
    }

    /**
     * 查询字典数据详细
     */
    @PreAuthorize(hasPermi = "system:dict:query")
    @GetMapping(value = "/{dictDataId}")
    public AjaxResult getInfo(@PathVariable Long dictDataId) {
        return AjaxResult.success(dictDataService.selectDictDataById(dictDataId));
    }

    /**
     * 根据字典类型查询字典数据信息
     */
    @GetMapping(value = "/type/{dictType}")
//    @PreAuthorize(hasPermi = "system:dict:query")
    public AjaxResult dictType(@PathVariable String dictType) {
        List<SysDictData> data = dictTypeService.selectDictDataByType(dictType);
        if (StringUtils.isNull(data)) {
            data = new ArrayList<>();
        }
        return AjaxResult.success(data);
    }

    /**
     * 根据字典类型查询字典数据信息
     */
    @GetMapping(value = "/types")
//    @PreAuthorize(hasPermi = "system:dict:query")
    public AjaxResult dictTypes(String dictTypes) {

        Map<String, List<SysDictData>> maplist= new HashMap<String, List<SysDictData>>();

        if (StringUtils.isNotEmpty(dictTypes)) {
            String[] split = dictTypes.split(",");
            Arrays.stream(split).forEach(dictType -> {
                List<SysDictData> data = dictTypeService.selectDictDataByType(dictType);
                if (StringUtils.isNull(data)) {
                    data = new ArrayList<>();
                }
                maplist.put(dictType, data);
            });
        }

        return AjaxResult.success(maplist);
    }

    /**
     * 新增字典类型
     */
    @PreAuthorize(hasPermi = "system:dict:add")
    @Log(title = "字典数据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysDictData dict) {
        dict.setCreateUser(SecurityUtils.getUserName());
        return toAjax(dictDataService.insertDictData(dict));
    }

    /**
     * 修改保存字典类型
     */
    @PreAuthorize(hasPermi = "system:dict:edit")
    @Log(title = "字典数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysDictData dict) {
        dict.setUpdateUser(SecurityUtils.getUserName());
        return toAjax(dictDataService.updateDictData(dict));
    }

    /**
     * 删除字典类型
     */
    @PreAuthorize(hasPermi = "system:dict:remove")
    @Log(title = "字典类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{dictDataIds}")
    public AjaxResult remove(@PathVariable Long[] dictDataIds) {
        dictDataService.deleteDictDataByIds(dictDataIds);
        return success();
    }

    /**
     * 新增代码
     * 根据字典类型查询字典集合
     * @param dictType
     * @return
     */
    @GetMapping("getDictDateByDictType")
    public AjaxResult getDictDateByDictType(String dictType){
        if (StringUtils.isBlank(dictType)) {
            throw new RuntimeException("字典类型不能为空！");
        }
        return AjaxResult.success(dictDataService.getDictDateByDictType(dictType));
    }
}
