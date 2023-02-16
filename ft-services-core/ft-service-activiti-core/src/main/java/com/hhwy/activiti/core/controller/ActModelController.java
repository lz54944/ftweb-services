package com.hhwy.activiti.core.controller;

import com.hhwy.activiti.core.domain.dto.ActModelDTO;
import com.hhwy.activiti.core.service.IActModelService;
import com.hhwy.common.core.web.controller.BaseController;
import com.hhwy.common.core.web.domain.AjaxResult;
import com.hhwy.common.log.annotation.Log;
import com.hhwy.common.log.enums.BusinessType;
import com.hhwy.common.security.annotation.PreAuthorize;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.repository.Model;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

/**
 * <br>描 述： 模型处理
 * <br>创 建 人：Jinzhaoqiang
 * <br>创建时间：2021/7/28 15:21
 * <br>修改备注：无
 * <br>版本：1.0.0
 */
@RestController
@RequestMapping("/act/model")
public class ActModelController extends BaseController {

    @Autowired
    IActModelService actModelService;

    /**
     * <br>方法描述：模型列表查询
     * <br>创 建 人：Jinzhaoqiang
     * <br>创建时间：2021/7/14 15:45
     * <br>备注：无
     */
    @GetMapping("/list")
    @PreAuthorize(hasPermi = "act:model:query")
    public AjaxResult list(ActModelDTO actModelDTO) {
        Map<String, Object> result = actModelService.selectModelList(actModelDTO, getPageSize(), getPageNum());
        return AjaxResult.success("查询成功", result);
    }

    /**
     * <br>方法描述：添加模型
     * <br>创 建 人：Jinzhaoqiang
     * <br>创建时间：2021/7/15 10:17
     * <br>备注：无
     */
    @Log(title = "流程模型管理", businessType = BusinessType.INSERT)
    @PostMapping
    @PreAuthorize(hasPermi = "act:model:add")
    public AjaxResult addModel(@RequestBody ActModelDTO actModelDTO) throws Exception {
        return actModelService.addModel(actModelDTO);
    }

    /**
     * <br>方法描述：更新
     * <br>创 建 人：Jinzhaoqiang
     *
     * @param actModelDTO id 模型id
     * @param actModelDTO editorSourceValue 模型文件（json格式数据）
     * @param actModelDTO editorSourceExtraValue 模型生成的图片文件(svg_json)
     *                    <br>创建时间：2021/7/15 9:39
     *                    <br>备注：无
     */
    @Log(title = "模型管理", businessType = BusinessType.UPDATE)
    @PutMapping
    @PreAuthorize(hasPermi = "act:model:edit")
    public AjaxResult updateModel(@RequestBody ActModelDTO actModelDTO) {
        return actModelService.updateModel(actModelDTO);
    }

    /**
     * <br>方法描述：删除，多个id以逗号隔开，即为批量删除
     * <br>创 建 人：Jinzhaoqiang
     * <br>创建时间：2021/7/15 10:11
     * <br>备注：无
     */
    @Log(title = "删除模型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{modelId}")
    @PreAuthorize(hasPermi = "act:model:remove")
    public AjaxResult remove(@PathVariable String modelId) {
        try {
            actModelService.deleteModelByIds(modelId);
        }catch (Exception e){
            return AjaxResult.error("流程已部署，不允许删除");
        }
        return AjaxResult.success();
    }

    /**
     * 发布/部署流程
     *
     * @param modelId 模型id
     */
    @Log(title = "发布流程", businessType = BusinessType.UPDATE)
    @PutMapping("/deploy/{modelId}")
    @PreAuthorize(hasPermi = "act:model:deploy")
    public AjaxResult deploy(@PathVariable("modelId") String modelId) throws Exception{
        return actModelService.deployProcess(modelId);
    }

    @Log(title = "导出指定模型", businessType = BusinessType.EXPORT)
    @GetMapping("/export/{modelId}")
    @PreAuthorize(hasPermi = "act:model:export")
    public void exportToXml(@PathVariable String modelId, HttpServletResponse response) {
        try {
            Model modelData = actModelService.selectModelById(modelId);
            BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
            /*JsonNode editorNode = new ObjectMapper().readTree(actModelService.getModelEditorSource(modelData.getId()));
            BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);
            BpmnXMLConverter xmlConverter = new BpmnXMLConverter();*/
            byte[] bpmnBytes = actModelService.getModelEditorSource(modelData.getId());
            StringBuilder builder = new StringBuilder(new String(bpmnBytes));
            String temp = builder.substring(0, builder.indexOf("\n"));
            if (temp.indexOf("'") > -1) {
                String suffix = builder.substring(builder.indexOf("\n"));
                temp = temp.replaceAll("'", "\"");
                System.out.println(temp + suffix);
                bpmnBytes = (temp + suffix).getBytes("utf-8");
            }
            String filename = modelData.getName();
            if (!filename.endsWith("bpmn20.xml") && !filename.endsWith(".bpmn")) {
                filename = filename + ".bpmn"; //只能 以".bpmn20.xml"或".bpmn" 结尾，不然在部署的时候不会往act_re_procdef插入数据，原因见ParsedDeploymentBuilder中的isBpmnResource函数
            }
            //String filename = bpmnModel.getMainProcess().getName() + ".bpmn"; //只能 以".bpmn20.xml"或".bpmn" 结尾，不然在部署的时候不会往act_re_procdef插入数据，原因见ParsedDeploymentBuilder中的isBpmnResource函数
            response.setHeader("Content-Disposition", "attachment; filename=" + filename);
            response.setContentType("application/octet-stream");
            ByteArrayInputStream in = new ByteArrayInputStream(bpmnBytes);
            IOUtils.copy(in, response.getOutputStream());
            response.flushBuffer();
        } catch (Exception e) {
            throw new ActivitiException("导出model的xml文件失败，模型ID=" + modelId, e);
        }
    }

    @Log(title = "导入指定模型", businessType = BusinessType.IMPORT)
    @PostMapping("/import")
    @PreAuthorize(hasPermi = "act:model:import")
    public AjaxResult importXML(MultipartFile file) throws IOException {
        return actModelService.importModel(file);
    }

}
