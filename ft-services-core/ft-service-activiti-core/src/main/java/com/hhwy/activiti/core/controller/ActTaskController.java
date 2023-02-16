package com.hhwy.activiti.core.controller;

import com.github.pagehelper.Page;
import com.hhwy.activiti.core.domain.dto.ActTaskDTO;
import com.hhwy.activiti.core.domain.dto.ActWorkflowFormDataDTO;
import com.hhwy.activiti.core.domain.po.ActiTaskConfig;
import com.hhwy.activiti.core.domain.po.ActiTodoTaskHistory;
import com.hhwy.activiti.core.service.IActTaskService;
import com.hhwy.common.core.utils.StringUtils;
import com.hhwy.common.core.web.controller.BaseController;
import com.hhwy.common.core.web.domain.AjaxResult;
import com.hhwy.common.core.web.page.PageDomain;
import com.hhwy.common.core.web.page.TableDataInfo;
import com.hhwy.common.core.web.page.TableSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <br>创 建 人：Jinzhaoqiang
 * <br>创建时间：2021/7/12 16:04
 * <br>备注：无
 */
@RestController
@RequestMapping("/act/task")
public class ActTaskController extends BaseController {

    @Autowired
    private IActTaskService actTaskService;

     /**
        * <br>方法描述：流程提交
        * <br>接收参数: 如果是新发起流程 则： businessId  processKey  nextNodeId  nextTaskAssignee 参数不能为空
        * <br>如果是流程正常流转 则： instanceId  currentTaskId  nextNodeId  nextTaskAssignee 参数不能为空
        * <br>创 建 人：Jinzhaoqiang
        * <br>创建时间：2021/8/2 14:38
        * <br>备注：无
        */
    @PutMapping("/submit")
    public AjaxResult submit(@RequestBody Map<String, Object> param) {
        String currentTaskId = (String) param.get("currentTaskId");
        String nextNodeId = (String)param.get("nextNodeId");
        String nextTaskAssignee = (String)param.get("nextTaskAssignee");
        String businessId = (String)param.get("businessId");
        String instanceId = (String)param.get("instanceId");
        String comment = (String)param.get("comment");
        Map<String,Object> variables = (Map<String,Object>)param.get("variables");
        if(variables==null){
            variables = new HashMap<>();
        }
        if(StringUtils.isEmpty(instanceId)){//新发起流程
            String processKey = (String) param.get("processKey");
            String businessTableName = (String)param.get("businessTableName");
            Assert.notNull(businessTableName,"businessTableName不能为空！");
            Assert.notNull(businessId,"businessId不能为空！");
            variables.put("businessId",businessId);
            variables.put("businessTableName",businessTableName);
            actTaskService.firstNodeSubmit(businessId, processKey, nextNodeId, nextTaskAssignee, comment, variables);
        }else {
            actTaskService.submit(instanceId, currentTaskId, nextNodeId, nextTaskAssignee, comment, variables);
        }
        return AjaxResult.success();
    }

    @PostMapping("/nextNodes")
    public AjaxResult nextNodes(@RequestBody Map<String, Object> param) {
        String processKey = (String)param.get("processKey");
        String taskId = (String)param.get("taskId");
        Map<String,Object> variables = (Map<String, Object>) param.get("variables");
        List<ActiTaskConfig> tasks = actTaskService.nextNodes(processKey, taskId, variables);
        return AjaxResult.success(tasks);
    }

    @GetMapping("/receivers")
    public AjaxResult receivers(String processKey, String instanceId, String taskNodeId) {
        return actTaskService.receivers(processKey, instanceId, taskNodeId);
    }

    /**
     * <br>方法描述：获取所有完成节点
     * <br>创 建 人：Jinzhaoqiang
     * <br>创建时间：2021/8/3 16:17
     * <br>备注：无
     */
    @GetMapping("/completeNodes/{instanceId}")
    public AjaxResult completeNodes(@PathVariable("instanceId") String instanceId) {
        List<ActiTaskConfig> tasks = actTaskService.completeNodes(instanceId);
        return AjaxResult.success(tasks);
    }

    /**
     * <br>方法描述：退回上一步
     * <br>instanceId  currentTaskId  nextNodeId  nextTaskAssignee 参数不能为空
     * <br>创 建 人：Jinzhaoqiang
     * <br>创建时间：2021/8/2 14:38
     * <br>备注：无
     */
    @PutMapping("/back")
    public AjaxResult back(@RequestBody Map<String, Object> param) throws Exception {
        String taskId = (String)param.get("taskId");
        String comment = (String)param.get("comment");
        Map<String,Object> variables = (Map<String,Object>)param.get("variables");
        if(variables==null){
            variables = new HashMap<>();
        }
        actTaskService.back(taskId,comment,variables);
        return AjaxResult.success();
    }

    /**
     * <br>方法描述：流程任意节点退回
     * <br>instanceId  currentTaskId  nextNodeId  nextTaskAssignee 参数不能为空
     * <br>创 建 人：Jinzhaoqiang
     * <br>创建时间：2021/8/2 14:38
     * <br>备注：无
     */
    @PutMapping("/spBack")
    public AjaxResult spBack(@RequestBody Map<String, Object> param) {
        String currentTaskId = (String) param.get("currentTaskId");
        String nextNodeId = (String) param.get("nextNodeId");
        String nextTaskAssignee = (String) param.get("nextTaskAssignee");
        String instanceId = (String) param.get("instanceId");
        String comment = (String) param.get("comment");
        Map<String,Object> variables = (Map<String,Object>)param.get("variables");
        if(variables==null){
            variables = new HashMap<>();
        }
        actTaskService.spBack(instanceId, currentTaskId, nextNodeId, nextTaskAssignee, comment, variables);
        return AjaxResult.success();
    }

    /**
     * <br>方法描述：撤回
     * <br>创 建 人：Jinzhaoqiang
     * <br>创建时间：2021/8/3 16:17
     * <br>备注：无
     */
    @PutMapping("/recall/{taskId}")
    public AjaxResult  recall(@PathVariable("taskId") String taskId) {
        actTaskService.recall(taskId);
        return AjaxResult.success();
    }

     /**
        * <br>方法描述：重新指派
        * <br>创 建 人：Jinzhaoqiang
        * <br>创建时间：2021/8/10 10:12
        * <br>备注：无
        */
    @PutMapping("/reassign")
    public AjaxResult  reassign(@RequestBody Map<String, String> param) {
        String taskId = param.get("taskId");
        String assignee = param.get("assignee");
        actTaskService.reassign(taskId,assignee);
        return AjaxResult.success();
    }

    @PutMapping("/suspended/{instanceId}")
    public AjaxResult suspended(@PathVariable("instanceId") String instanceId){
        actTaskService.suspended(instanceId);
        return AjaxResult.success();
    }

    @PutMapping("/activate/{instanceId}")
    public AjaxResult activate(@PathVariable("instanceId") String instanceId){
        actTaskService.activate(instanceId);
        return AjaxResult.success();
    }

     /**
        * <br>方法描述：删除流程实例
        * <br>创 建 人：Jinzhaoqiang
        * <br>创建时间：2021/8/5 18:58
        * <br>备注：无
        */
    @DeleteMapping("/instanceId/{instanceId}")
    public AjaxResult deleteProcessInstance(@PathVariable("instanceId") String instanceId){
        actTaskService.deleteProcessInstance(instanceId,"No Reason");
        return AjaxResult.success();
    }

     /**
        * <br>方法描述：获取流程处理列表
        * <br>创 建 人：Jinzhaoqiang
        * <br>创建时间：2021/8/13 16:23
        * <br>备注：无
        */
    @GetMapping("/taskTraces/{instanceId}")
    public AjaxResult getTaskTraces(@PathVariable("instanceId") String instanceId){
        List<ActiTodoTaskHistory> list = actTaskService.getTaskTraces(instanceId);
        return AjaxResult.success(list);
    }







    //获取我的代办任务
    @GetMapping(value = "/list")
    public TableDataInfo getTasks() {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Page<ActTaskDTO> hashMaps = actTaskService.selectProcessDefinitionList(pageDomain);
        return getDataTable(hashMaps);
    }

    //渲染表单
    @GetMapping(value = "/formDataShow/{taskID}")
    public AjaxResult formDataShow(@PathVariable("taskID") String taskID) {
        return AjaxResult.success(actTaskService.formDataShow(taskID));
    }

    //保存表单
    @PostMapping(value = "/formDataSave/{taskID}")
    public AjaxResult formDataSave(@PathVariable("taskID") String taskID,
                                   @RequestBody List<ActWorkflowFormDataDTO> formData) throws ParseException {
        return toAjax(actTaskService.formDataSave(taskID, formData));
    }


   /* @PostMapping("/startProcess")
    @ResponseBody
    public AjaxResult startProcess(HttpServletRequest request, @RequestBody Map<String, String> param) {
        String processKey = param.get("processKey");
        String businessId = param.get("businessId");
        actTaskService.startProcess(request, processKey, businessId);
        return AjaxResult.success();
    }

    @PutMapping("/complete")
    @ResponseBody
    public AjaxResult complete(HttpServletRequest request, @RequestBody Map<String, String> param) {
        String taskId = param.get("taskId");
        actTaskService.complete(request, taskId);
        return AjaxResult.success();
    }

    @PutMapping("/firstSubmit")
    @ResponseBody
    public AjaxResult firstSubmit(HttpServletRequest request, @RequestBody Map<String, String> param) {
        String businessId = param.get("businessId");
        String processKey = param.get("processKey");
        String nextNodeId = param.get("nextNodeId");
        String nextTaskAssignee = param.get("nextTaskAssignee");
        actTaskService.firstSubmit(request, businessId, processKey, nextNodeId, nextTaskAssignee);
        return AjaxResult.success();
    }*/

}
