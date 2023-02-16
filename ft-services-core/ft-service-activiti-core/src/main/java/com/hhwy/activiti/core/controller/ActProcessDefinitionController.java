package com.hhwy.activiti.core.controller;

import com.hhwy.activiti.core.domain.dto.ActRunTaskDTO;
import com.hhwy.activiti.core.domain.dto.ProcessDefinitionDTO;
import com.hhwy.activiti.core.service.IActProcessDefinitionService;
import com.hhwy.common.core.web.controller.BaseController;
import com.hhwy.common.core.web.domain.AjaxResult;
import com.hhwy.common.core.web.page.PageDomain;
import com.hhwy.common.core.web.page.TableSupport;
import com.hhwy.common.log.annotation.Log;
import com.hhwy.common.log.enums.BusinessType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <br>创 建 人：Jinzhaoqiang
 * <br>创建时间：2021/7/12 16:03
 * <br>备注：无
 */
@RestController
@RequestMapping("/act/definition")
public class ActProcessDefinitionController extends BaseController {

    @Autowired
    private IActProcessDefinitionService processDefinitionService;

    /**
     * 获取流程定义集合
     *
     * @param processDefinition
     * @return
     */
    @GetMapping(value = "/list")
    public AjaxResult list(ProcessDefinitionDTO processDefinition) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        return getDataTableAjaxResult(processDefinitionService.selectProcessDefinitionList(processDefinition, pageDomain));
    }

    @GetMapping(value = "/instance/list")
    public AjaxResult instanceList(String definitionId) {
        startPage();
        List<ActRunTaskDTO> taskEntities = processDefinitionService.selectInstanceList(definitionId);
        return getDataTableAjaxResult(taskEntities);
    }

    /**
     * @return
     */
    @GetMapping(value = "/getDefinitions/{instanceId}")
    public AjaxResult getDefinitionsByInstanceId(@PathVariable("instanceId") String instanceId) {
        return AjaxResult.success(processDefinitionService.getDefinitionsByInstanceId(instanceId));
    }

    /**
     * 删除流程定义
     *
     * @param deploymentId
     * @return
     */
    @Log(title = "流程定义管理", businessType = BusinessType.DELETE)
    @DeleteMapping(value = "/{defId}/{deploymentId}")
    public AjaxResult delDefinition(@PathVariable("defId") String defId, @PathVariable("deploymentId") String deploymentId) {
        try {
            processDefinitionService.deleteProcessDefinitionById(defId,deploymentId);
        }catch (Exception e){
            return AjaxResult.error("流程存在实例，不允许删除");
        }
        return AjaxResult.success();
    }

    /**
     * 上传并部署流程定义
     *
     * @param file
     * @return
     * @throws IOException
     */
    @Log(title = "流程定义管理", businessType = BusinessType.IMPORT)
    @PostMapping(value = "/uploadStreamAndDeployment")
    public AjaxResult uploadStreamAndDeployment(@RequestParam("file") MultipartFile file) throws IOException {
        processDefinitionService.uploadStreamAndDeployment(file);
        return AjaxResult.success();
    }

    /**
     * 启动挂起流程流程定义
     *
     * @param processDefinition
     * @return
     */
    @Log(title = "流程定义管理", businessType = BusinessType.UPDATE)
    @PostMapping("/suspendOrActiveApply")
    public AjaxResult suspendOrActiveApply(@RequestBody ProcessDefinitionDTO processDefinition) {
        processDefinitionService.suspendOrActiveApply(processDefinition.getId(), processDefinition.getSuspendState());
        return AjaxResult.success();
    }

    /**
     * 上传流程流程定义
     *
     * @param multipartFile
     * @return
     * @throws IOException
     */
    @Log(title = "流程定义管理", businessType = BusinessType.IMPORT)
    @PostMapping(value = "/upload")
    public AjaxResult upload(@RequestParam("processFile") MultipartFile multipartFile) throws IOException {
        if (!multipartFile.isEmpty()) {
            String fileName = processDefinitionService.upload(multipartFile);
            return AjaxResult.success("操作成功", fileName);
        }
        return AjaxResult.error("不允许上传空文件！");
    }


    /**
     * 通过stringBPMN添加流程定义
     *
     * @param stringBPMN
     * @return
     */
    @PostMapping(value = "/addDeploymentByString")
    public AjaxResult addDeploymentByString(@RequestParam("stringBPMN") String stringBPMN) {
        processDefinitionService.addDeploymentByString(stringBPMN);
        return AjaxResult.success();
    }

    /**
     * 获取流程定义XML
     *
     * @param modelId
     */
    @GetMapping(value = "/getDefinitionXML")
    public String getProcessDefineXML(@RequestParam("modelId") String modelId) throws Exception {
        return processDefinitionService.getProcessDefineXML(modelId);
    }

    /**
     * 获取流程定义XML
     *
     * @param response
     * @param deploymentId
     * @param resourceName
     */
    @GetMapping(value = "/downLoad")
    public void downLoad(HttpServletResponse response,
                         @RequestParam("deploymentId") String deploymentId,
                         @RequestParam("resourceName") String resourceName) throws IOException {

        processDefinitionService.downLoad(response, deploymentId, resourceName);
    }

    /**
     * 获取流程图 svg
     *
     * @param modelId 模型id
     * @param deploymentId 部署id
     */
    @GetMapping(value = "/getDefinitionSvg")
    public String getDefinitionSvg(@RequestParam(name = "modelId",required = false) String modelId, @RequestParam(name = "deploymentId",required = false) String deploymentId) throws Exception {
        return processDefinitionService.getDefinitionSvg(modelId,deploymentId);
    }
}
