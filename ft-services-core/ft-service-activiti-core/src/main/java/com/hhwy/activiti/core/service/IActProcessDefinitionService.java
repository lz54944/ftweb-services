package com.hhwy.activiti.core.service;

import com.github.pagehelper.Page;
import com.hhwy.activiti.core.domain.dto.ActRunTaskDTO;
import com.hhwy.activiti.core.domain.dto.DefinitionIdDTO;
import com.hhwy.activiti.core.domain.dto.ProcessDefinitionDTO;
import com.hhwy.common.core.web.page.PageDomain;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <br>描 述： IActProcessDefinitionService
 * <br>创 建 人：Jinzhaoqiang
 * <br>创建时间：2021/7/28 15:01
 * <br>修改备注：无
 * <br>版本：1.0.0
 */
public interface IActProcessDefinitionService {
    /**
     * 获取流程定义集合
     *
     * @param processDefinition
     * @return Page 分页信息
     */
    Page<ProcessDefinitionDTO> selectProcessDefinitionList(ProcessDefinitionDTO processDefinition, PageDomain pageDomain);

    List<ActRunTaskDTO> selectInstanceList(String definitionId);

    DefinitionIdDTO getDefinitionsByInstanceId(String instanceId);

    /**
     * 删除流程定义
     *
     * @param id
     * @return
     */
    int deleteProcessDefinitionById(String defId, String deploymentId);

    /**
     * 上传并部署流程定义
     *
     * @param file
     * @return
     * @throws IOException
     */
    void uploadStreamAndDeployment(MultipartFile file) throws IOException;

    /**
     * 启动挂起流程流程定义
     *
     * @param id           流程定义id
     * @param suspendState 流程状态
     * @return
     */
    void suspendOrActiveApply(String id, Integer suspendState);

    /**
     * 上传流程流程定义
     *
     * @param multipartFile
     * @return
     */
    String upload(MultipartFile multipartFile) throws IOException;

    /**
     * 通过stringBPMN添加流程定义
     *
     * @param stringBPMN
     * @return
     */
    void addDeploymentByString(String stringBPMN);

    /**
     * 获取流程定义XML
     *
     * @param modelId
     */
    String getProcessDefineXML(String modelId) throws Exception;

    void downLoad(HttpServletResponse response, String deploymentId, String resourceName) throws IOException;

    String getDefinitionSvg(String modelId, String deploymentId) throws Exception;

}
