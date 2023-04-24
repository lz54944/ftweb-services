package com.hhwy.system.core.controller;

import com.alibaba.fastjson.JSON;
import com.hhwy.common.core.utils.StringUtils;
import com.hhwy.common.core.web.controller.BaseController;
import com.hhwy.common.core.web.domain.AjaxResult;
import com.hhwy.common.log.annotation.Log;
import com.hhwy.common.log.enums.BusinessType;
import com.hhwy.system.core.domain.SysBusinessForm;
import com.hhwy.system.core.domain.SysBusinessFormItem;
import com.hhwy.system.core.service.ISysBusinessFormItemService;
import com.hhwy.system.core.service.ISysBusinessFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 业务流程单Controller
 * 
 * @author hanchuanchuan
 * @date 2021-09-16
 */
@Controller
@RequestMapping("/sysBusinessForm")
public class SysBusinessFormController extends BaseController {
    private String prefix = "sysBusinessForm";

    @Autowired
    private ISysBusinessFormService sysBusinessFormService;

    @Autowired
    private ISysBusinessFormItemService sysBusinessFormItemService;

    //@RequiresPermissions("sysFlowForm:sysFlowForm:view")
    @GetMapping()
    public String sysFlowForm() {
        return prefix + "/sysFlowForm";
    }

    /**
     * 修改保存业务流程单
     */
    //@RequiresPermissions("sysFlowForm:sysFlowForm:edit")
    @Log(title = "业务流程单", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@RequestBody SysBusinessForm sysBusinessForm) {
        return toAjax(sysBusinessFormService.updateSysBusinessForm(sysBusinessForm));
    }

    /**
     * 删除业务流程单
     */
    //@RequiresPermissions("sysFlowForm:sysFlowForm:remove")
    @Log(title = "业务流程单", businessType = BusinessType.DELETE)
    @GetMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String flowFormId) {
        return toAjax(sysBusinessFormService.deleteSysBusinessFormByIds(flowFormId));
    }

    /**
     * 根据功能模块查询数据库表
     * @return
     */
    @GetMapping("/databaseGetTable")
    @ResponseBody
    public AjaxResult databaseGetTable(String module){
        if ( module == null || StringUtils.isEmpty(module)){
            return AjaxResult.error("功能模块不得为空");
        }
       List<SysBusinessForm> sysBusinessFormList = sysBusinessFormService.selectDataBaseGetTable(module);
       return AjaxResult.success("查询成功",sysBusinessFormList);
    }

    /**
     * 查询PC端业务流程表单配置列表
     * @return
     */
    @GetMapping("/configurationList")
    @ResponseBody
    public AjaxResult configurationList(String module,String formCode,String tableName){
     List<SysBusinessForm>  sysBusinessFormList = sysBusinessFormService.selectConfigurationList(module,formCode,tableName);
     return AjaxResult.success("查询业务流程表单配置列表成功",sysBusinessFormList);
    }

    /**
     * 业务流程表单表插入修改
     * @return
     */
    @PostMapping("/databaseAddAndUpdate")
    @ResponseBody
    public AjaxResult databaseAddAndUpdate(@RequestBody SysBusinessForm sysBusinessForm){
        if (sysBusinessForm.getFlowFormId() == null || StringUtils.isEmpty(sysBusinessForm.getFlowFormId())){
            return sysBusinessFormService.insertSysBusinessForm(sysBusinessForm);
        }else {
            return sysBusinessFormService.updateSysBusinessFormEr(sysBusinessForm);
        }
    }

    /**g
     * 生成数据
     * @return
     */
    @GetMapping("/generateData")
    @ResponseBody
    public AjaxResult generateData(String flowFormId,String tableDbCode,String module){
//        if (tableDbCode == null || module == null){
//            return AjaxResult.error("功能模块或数据库表不得为空");
//        }
//        List<SysBusinessFormItem> sysBusinessFormItems = sysBusinessFormItemService.selectOmsList(flowFormId);
//        if (sysBusinessFormItems != null){
//            return AjaxResult.error("数据库表单已经生成过了");
//        }
        List<SysBusinessFormItem> sysBusinessFormItem = sysBusinessFormItemService.selectOmsSysFlowGenerateData(flowFormId,tableDbCode,module);
        return AjaxResult.success("生成数据成功",sysBusinessFormItem);
    }

    /**
     * 移动端获取信息
     * @return
     */
//    public static final char UNDERLINE='_';
//    @PostMapping("/mobileTerminalGetProject")
//    @ResponseBody
//    public Map<String,Object> mobileTerminalGetProject(@RequestBody Map<String,Object> params){
//        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
//        Map<String,Object> param = (Map<String,Object>)params.get("param");
//        //根据编码查询flowFormId
//        SysBusinessForm sysBusinessForm = sysBusinessFormService.selectActCode((String) params.get("formCode"));
//        if (sysBusinessForm == null){
//            return AjaxResult.error("表单编码不正确!");
//        }
//        String flowFormId = sysBusinessForm.getFlowFormId();
//        ArrayList<DataValue> dataValues = new ArrayList<>();
//        //查出子表信息
//        List<SysBusinessFormItem> sysBusinessFormItems = sysBusinessFormItemService.selectOmsListery(flowFormId);
//        sysBusinessFormItems.forEach(p->{
//            DataValue dataValue = new DataValue();
//            String colCode = p.getColCode();
//            int length = colCode.length();
//            StringBuilder colCodes = new StringBuilder(length);
//            for (int i = 0; i < length; i++) {
//                char c = colCode.charAt(i);
//                if (c == UNDERLINE){
//                    if (++i<length){
//                        colCodes.append(Character.toUpperCase(colCode.charAt(i)));
//                    }
//                }else {
//                    colCodes.append(c);
//                }
//            }
//        String fieldcode = colCodes.toString();
//        //根据id项目详情信息
//        dataValue.setFieldcode(fieldcode);
//        dataValue.setFieldname(p.getColComm());
//        dataValue.setDictcode(p.getDictCode());
//        dataValues.add(dataValue);
//        });
//        //获取sql语句
//        String querySql = sysBusinessForm.getQuerySql();
//        Set<String> keys = param.keySet();
//        Iterator<String> iterator = keys.iterator();
//        while (iterator.hasNext()) {
//            String next = iterator.next();
//            if (param.get(next) instanceof String) {
//                querySql  = querySql.replace("#{"+next+"}", "'" + param.get(next) + "'");
//
//            } else  {
//                querySql = querySql.replace("#{"+next+"}", param.get(next)+"");
//            }
//       }
//        //存入fieldvalue数据
//            String sql = querySql.substring(6);
//            if (sysBusinessForm.getModule().equals("01")){
//                 Map<String, Object> marketSql = rpcMarketFactory.getMarketSql(sql);
//                 if (marketSql == null){
//                     return AjaxResult.error("未查出对应id的信息");
//                 }
//                 dataValues.forEach(y->{
//                     String fieldcode = y.getFieldcode();
//                     int len = fieldcode.length();
//                     StringBuilder sb = new StringBuilder(len);
//                     for (int i = 0; i < len; i++) {
//                         char c = fieldcode.charAt(i);
//                         if (Character.isUpperCase(c)) {
//                             sb.append(UNDERLINE);
//                             sb.append(Character.toLowerCase(c));
//                         } else {
//                             sb.append(c);
//                         }
//                     }
//                     String s = sb.toString();
//                     y.setFieldvalue(marketSql.get(s)+"");
//                 });
//            }
//            if (sysBusinessForm.getModule().equals("02")){
//                Map<String, Object> marketDevFlow = remoteMerketDevService.getMarketDevFlow(sql);
//                if (marketDevFlow == null){
//                    return AjaxResult.error("未查出对应id的信息");
//                }
//                dataValues.forEach(y-> {
//                    String fieldcode = y.getFieldcode();
//                    int len = fieldcode.length();
//                    StringBuilder sb = new StringBuilder(len);
//                    for (int i = 0; i < len; i++) {
//                        char c = fieldcode.charAt(i);
//                        if (Character.isUpperCase(c)) {
//                            sb.append(UNDERLINE);
//                            sb.append(Character.toLowerCase(c));
//                        } else {
//                            sb.append(c);
//                        }
//                    }
//                    String s = sb.toString();
//                    String valueOf = String.valueOf(marketDevFlow.get(s));
//                    if (StringUtils.equals("1111-11-11",valueOf)){
//                        y.setFieldvalue("/");
//                    }else {
//                        y.setFieldvalue(valueOf);
//                    }
//                });
//            }
//            if (sysBusinessForm.getModule().equals("03")){
//                 Map<String, Object> reportFlow = remoteReportService.getReportFlow(sql);
//                 if (reportFlow == null){
//                     return AjaxResult.error("未查出对应id的信息");
//                 }
//                dataValues.forEach(y-> {
//                    String fieldcode = y.getFieldcode();
//                    int len = fieldcode.length();
//                    StringBuilder sb = new StringBuilder(len);
//                    for (int i = 0; i < len; i++) {
//                        char c = fieldcode.charAt(i);
//                        if (Character.isUpperCase(c)) {
//                            sb.append(UNDERLINE);
//                            sb.append(Character.toLowerCase(c));
//                        } else {
//                            sb.append(c);
//                        }
//                    }
//                    String s = sb.toString();
//                    y.setFieldvalue(reportFlow.get(s) + "");
//                });
//            }
//        //处理子表的子数据查询
//        for (SysBusinessFormItem sysBusinessFormItem : sysBusinessFormItems) {
//            if (StringUtils.isNotEmpty(sysBusinessFormItem.getQuerySql()) && StringUtils.isNotEmpty(sysBusinessFormItem.getSqlParams())){
//                String paramData = "";
//                //获取到sql
//                String querySql1 = sysBusinessFormItem.getQuerySql();
//                logger.info("获取到的sql语句:{}", JSON.toJSONString(querySql1));
//                //获取到参数
//                String sqlParams = sysBusinessFormItem.getSqlParams();
//                logger.info("获取到的sql参数:{}",JSON.toJSONString(sqlParams));
//                for (DataValue dataValue : dataValues) {
//                    if (StringUtils.equals(dataValue.getFieldcode(),sqlParams)){
//                        paramData = dataValue.getFieldvalue();
//                    }
//                }
//                querySql1 = querySql1.replace("#{"+sqlParams+"}","'"+paramData+"'");
//                String sqlEr = querySql1.substring(6);
//                logger.info("最终处理完表数据的sql",sqlEr);
//                if (StringUtils.equals("02",sysBusinessForm.getModule())){
//                  //获取到sql
//                    Map<String, Object> marketDevFlow = remoteMerketDevService.getMarketDevFlow(sqlEr);
//                    if (!ObjectUtils.isEmpty(marketDevFlow)){
//                        logger.info("查询出的sql数据{}",JSON.toJSONString(marketDevFlow));
//                        Set<String> strings = marketDevFlow.keySet();
//                        for (DataValue dataValue : dataValues) {
//                            Iterator<String> iteratorEr = strings.iterator();
//                            while (iteratorEr.hasNext()){
//                                if (iteratorEr.next().equals(dataValue.getFieldcode())){
//                                    dataValue.setFieldvalue(String.valueOf(marketDevFlow.get(dataValue.getFieldcode())));
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        stringObjectHashMap.put("data",dataValues);
//        //查询字典
//        HashMap<String,Map<String,String>> stringGenTableColumnHashMap = new HashMap<>();
//        sysBusinessFormItems.forEach(l->{
//            if (StringUtils.isNotEmpty(l.getDictCode()) || StringUtils.isNotBlank(l.getDictCode())){
//              String dictCode = l.getDictCode();
//              List<GenTableColumn> genTableColumn =  iDictTableColumnService.selectDictTableValue(dictCode);
//              HashMap<String, String> stringStringHashMap = new HashMap<>();
//              genTableColumn.forEach(o->{
//                  String dictValue = o.getDictValue();
//                  String dictLabel = o.getDictLabel();
//                  stringStringHashMap.put(dictValue,dictLabel);
//              });
//              stringGenTableColumnHashMap.put(l.getDictCode(),stringStringHashMap);
//            }
//        });
//        stringObjectHashMap.put("dicts",stringGenTableColumnHashMap);
//        return stringObjectHashMap;
//    }
}
