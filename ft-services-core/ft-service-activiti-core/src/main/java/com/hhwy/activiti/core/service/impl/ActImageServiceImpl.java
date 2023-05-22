//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.hhwy.activiti.core.service.impl;

import com.hhwy.activiti.core.image.CustomProcessDiagramGenerator;
import com.hhwy.activiti.core.mapper.ActivitiMapper;
import com.hhwy.activiti.core.service.IActImageService;
import com.hhwy.common.core.utils.StringUtils;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.Execution;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGSVGElement;

@Service
@Transactional(
        rollbackFor = {Exception.class}
)
public class ActImageServiceImpl implements IActImageService {
    @Autowired
    RepositoryService repositoryService;
    @Autowired
    ActHistoryServiceImpl actHistoryServiceImpl;
    @Autowired
    ActProcessDefinitionServiceImpl actProcessDefinitionServiceImpl;
    @Autowired
    ActivitiMapper activitiMapper;

    public ActImageServiceImpl() {
    }

    public String getFlowImgByProcInstId(String procInstId) throws Exception {
        List<HistoricActivityInstance> historicActivityInstanceList = new ArrayList();
        List<String> emptyRunningActivityIdList = new ArrayList();
        InputStream initialStream = this.getFlowImgInputStreamByProcInstId(procInstId, historicActivityInstanceList, emptyRunningActivityIdList);
        byte[] buffer = new byte[initialStream.available()];
        initialStream.read(buffer);
        String result = new String(buffer);
        result = result.substring(result.indexOf("<svg"));
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(parser);
        SVGDocument svgDocument = factory.createSVGDocument("http://www.w3.org/2000/svg", new ByteArrayInputStream(result.getBytes("UTF-8")));
        Iterator var10 = historicActivityInstanceList.iterator();

        while(var10.hasNext()) {
            HistoricActivityInstance historicActivityInstance = (HistoricActivityInstance)var10.next();
            String activityId = historicActivityInstance.getActivityId();
            svgDocument.getElementById(activityId).setAttribute("onclick", "showHistoryTaskDetail(this)");
        }

        var10 = emptyRunningActivityIdList.iterator();

        String svg;
        while(var10.hasNext()) {
            svg = (String)var10.next();
            svgDocument.getElementById(svg).setAttribute("onclick", "showRunningTaskDetail(this)");
        }

        SVGSVGElement rootElement = svgDocument.getRootElement();
        svg = convertElemToSVG(rootElement);
        return svg;
    }

    public static String convertElemToSVG(Element element) {
        TransformerFactory transFactory = TransformerFactory.newInstance();
        Transformer transformer = null;

        try {
            transformer = transFactory.newTransformer();
        } catch (TransformerConfigurationException var6) {
            var6.printStackTrace();
        }

        StringWriter buffer = new StringWriter();
        transformer.setOutputProperty("omit-xml-declaration", "yes");

        try {
            transformer.transform(new DOMSource(element), new StreamResult(buffer));
        } catch (TransformerException var5) {
            var5.printStackTrace();
        }

        String elementStr = buffer.toString();
        return elementStr;
    }

    public InputStream getFlowImgInputStreamByProcInstId(String procInstId, List<HistoricActivityInstance> emptyHistoricActivityInstanceList, List<String> emptyRunningActivityIdList) throws Exception {
        if (StringUtils.isEmpty(procInstId)) {
            throw new Exception("[异常]-传入的参数procInstId为空！");
        } else {
            InputStream imageStream = null;

            InputStream var15;
            try {
                HistoricProcessInstance historicProcessInstance = this.actHistoryServiceImpl.getHistoricProcessInstance(procInstId);
                List<HistoricActivityInstance> historicActivityInstanceList = this.actHistoryServiceImpl.getHistoricActivityInstancesAsc(procInstId);
                List<String> recallTaskIdList = this.activitiMapper.getRecallTaskIds(procInstId);
                historicActivityInstanceList = (List)historicActivityInstanceList.stream().filter((p) -> {
                    return !recallTaskIdList.contains(p.getTaskId());
                }).collect(Collectors.toList());
                emptyHistoricActivityInstanceList.addAll(historicActivityInstanceList);
                List<String> highLightedActivityIdList = (List)historicActivityInstanceList.stream().map(HistoricActivityInstance::getActivityId).collect(Collectors.toList());
                List<Execution> runningActivityInstanceList = this.actProcessDefinitionServiceImpl.getRunningActivityInstance(procInstId);
                List<String> runningActivityIdList = new ArrayList();
                Iterator var11 = runningActivityInstanceList.iterator();

                while(var11.hasNext()) {
                    Execution execution = (Execution)var11.next();
                    if (!StringUtils.isEmpty(execution.getActivityId())) {
                        runningActivityIdList.add(execution.getActivityId());
                    }
                }

                emptyRunningActivityIdList.addAll(runningActivityIdList);
                CustomProcessDiagramGenerator processDiagramGenerator = new CustomProcessDiagramGenerator();
                BpmnModel bpmnModel = this.repositoryService.getBpmnModel(historicProcessInstance.getProcessDefinitionId());
                List<String> highLightedFlowsIds = this.getHighLightedFlowsByIncomingFlows(bpmnModel, historicActivityInstanceList);
                List<String> runningActivityFlowsIds = this.getRunningActivityFlowsIds(bpmnModel, runningActivityIdList, historicActivityInstanceList);
//                imageStream = processDiagramGenerator.generateDiagramCustom(bpmnModel, highLightedActivityIdList, runningActivityIdList, highLightedFlowsIds, runningActivityFlowsIds, "宋体", "微软雅黑", "黑体");
                imageStream = processDiagramGenerator.generateDiagramCustom(bpmnModel, highLightedActivityIdList, runningActivityIdList, highLightedFlowsIds, runningActivityFlowsIds, "Courier New", "Courier New", "Courier New");
                var15 = imageStream;
            } catch (Exception var19) {
                throw new Exception("通过流程实例ID" + procInstId + "获取流程图时出现异常！", var19);
            } finally {
                if (imageStream != null) {
                    imageStream.close();
                }

            }

            return var15;
        }
    }

    public List<String> getHighLightedFlows(BpmnModel bpmnModel, List<HistoricActivityInstance> historicActivityInstanceList) {
        List<String> highFlows = new ArrayList();
        List<FlowNode> allHistoricActivityNodeList = new ArrayList();
        List<HistoricActivityInstance> finishedActivityInstancesList = new ArrayList();
        Iterator var6 = historicActivityInstanceList.iterator();

        while(var6.hasNext()) {
            HistoricActivityInstance historicActivityInstance = (HistoricActivityInstance)var6.next();
            FlowNode flowNode = (FlowNode)bpmnModel.getMainProcess().getFlowElement(historicActivityInstance.getActivityId(), true);
            allHistoricActivityNodeList.add(flowNode);
            if (historicActivityInstance.getEndTime() != null) {
                finishedActivityInstancesList.add(historicActivityInstance);
            }
        }

        for(int k = 0; k < finishedActivityInstancesList.size(); ++k) {
            HistoricActivityInstance currentActivityInstance = (HistoricActivityInstance)finishedActivityInstancesList.get(k);
            FlowNode currentFlowNode = (FlowNode)bpmnModel.getMainProcess().getFlowElement(currentActivityInstance.getActivityId(), true);
            List<SequenceFlow> outgoingFlowList = currentFlowNode.getOutgoingFlows();
            if (!"parallelGateway".equals(currentActivityInstance.getActivityType()) && !"inclusiveGateway".equals(currentActivityInstance.getActivityType())) {
                String currentActivityId = currentActivityInstance.getActivityId();
                int size = historicActivityInstanceList.size();
                boolean ifStartFind = false;
                boolean ifFinded = false;

                for(int i = 0; i < size; ++i) {
                    HistoricActivityInstance historicActivityInstance = (HistoricActivityInstance)historicActivityInstanceList.get(i);
                    if (i >= k && historicActivityInstance.getActivityId().equals(currentActivityId)) {
                        ifStartFind = true;
                    } else {
                        if (ifStartFind) {
                            ifFinded = false;
                            Iterator var17 = outgoingFlowList.iterator();

                            while(var17.hasNext()) {
                                SequenceFlow sequenceFlow = (SequenceFlow)var17.next();
                                if (historicActivityInstance.getActivityId().equals(sequenceFlow.getTargetRef())) {
                                    highFlows.add(sequenceFlow.getId());
                                    ifFinded = true;
                                    break;
                                }
                            }
                        }

                        if (ifFinded) {
                            break;
                        }
                    }
                }
            } else {
                Iterator var11 = outgoingFlowList.iterator();

                while(var11.hasNext()) {
                    SequenceFlow outgoingFlow = (SequenceFlow)var11.next();
                    FlowNode targetFlowNode = (FlowNode)bpmnModel.getMainProcess().getFlowElement(outgoingFlow.getTargetRef(), true);
                    if (allHistoricActivityNodeList.contains(targetFlowNode)) {
                        highFlows.add(outgoingFlow.getId());
                    }
                }
            }
        }

        return highFlows;
    }

    public List<String> getHighLightedFlowsByIncomingFlows(BpmnModel bpmnModel, List<HistoricActivityInstance> historicActivityInstanceList) {
        List<String> highFlows = new ArrayList();
        List<FlowNode> allHistoricActivityNodeList = new ArrayList();
        Iterator var5 = historicActivityInstanceList.iterator();

        while(var5.hasNext()) {
            HistoricActivityInstance historicActivityInstance = (HistoricActivityInstance)var5.next();
            FlowNode flowNode = (FlowNode)bpmnModel.getMainProcess().getFlowElement(historicActivityInstance.getActivityId(), true);
            allHistoricActivityNodeList.add(flowNode);
        }

        var5 = allHistoricActivityNodeList.iterator();

        while(var5.hasNext()) {
            FlowNode flowNode = (FlowNode)var5.next();
            List<SequenceFlow> incomingFlows = flowNode.getIncomingFlows();
            Iterator var8 = incomingFlows.iterator();

            while(var8.hasNext()) {
                SequenceFlow sequenceFlow = (SequenceFlow)var8.next();
                if (((List)allHistoricActivityNodeList.stream().map(BaseElement::getId).collect(Collectors.toList())).contains(sequenceFlow.getSourceFlowElement().getId())) {
                    highFlows.add(sequenceFlow.getId());
                }
            }
        }

        return highFlows;
    }

    private List<String> getRunningActivityFlowsIds(BpmnModel bpmnModel, List<String> runningActivityIdList, List<HistoricActivityInstance> historicActivityInstanceList) {
        List<String> runningActivityFlowsIds = new ArrayList();
        List<String> runningActivityIds = new ArrayList(runningActivityIdList);
        if (CollectionUtils.isEmpty(runningActivityIds)) {
            return runningActivityFlowsIds;
        } else {
            for(int i = historicActivityInstanceList.size() - 1; i >= 0; --i) {
                HistoricActivityInstance historicActivityInstance = (HistoricActivityInstance)historicActivityInstanceList.get(i);
                FlowNode flowNode = (FlowNode)bpmnModel.getMainProcess().getFlowElement(historicActivityInstance.getActivityId(), true);
                if (!runningActivityIds.contains(flowNode.getId())) {
                    List<SequenceFlow> outgoingFlowList = flowNode.getOutgoingFlows();
                    Iterator var10 = outgoingFlowList.iterator();

                    while(var10.hasNext()) {
                        SequenceFlow outgoingFlow = (SequenceFlow)var10.next();
                        FlowNode targetFlowNode = (FlowNode)bpmnModel.getMainProcess().getFlowElement(outgoingFlow.getTargetRef(), true);
                        if (runningActivityIds.contains(targetFlowNode.getId())) {
                            runningActivityFlowsIds.add(outgoingFlow.getId());
                            runningActivityIds.remove(targetFlowNode.getId());
                        }
                    }
                }
            }

            return runningActivityFlowsIds;
        }
    }

    public List<String> getRunningActivityFlowIdsByIcommingFlows(BpmnModel bpmnModel, List<String> runningActivityIdList, List<HistoricActivityInstance> historicActivityInstanceList) {
        List<String> runningActivityFlowsIds = new ArrayList();
        List<String> runningActivityIds = new ArrayList(runningActivityIdList);
        if (CollectionUtils.isEmpty(runningActivityIds)) {
            return runningActivityFlowsIds;
        } else {
            List<FlowNode> allHistoricActivityNodeList = new ArrayList();
            Iterator var7 = historicActivityInstanceList.iterator();

            HistoricActivityInstance historicActivityInstance;
            FlowNode flowNode;
            while(var7.hasNext()) {
                historicActivityInstance = (HistoricActivityInstance)var7.next();
                flowNode = (FlowNode)bpmnModel.getMainProcess().getFlowElement(historicActivityInstance.getActivityId(), true);
                allHistoricActivityNodeList.add(flowNode);
            }

            for(int i = historicActivityInstanceList.size() - 1; i >= 0; --i) {
                historicActivityInstance = (HistoricActivityInstance)historicActivityInstanceList.get(i);
                flowNode = (FlowNode)bpmnModel.getMainProcess().getFlowElement(historicActivityInstance.getActivityId(), true);
                if (runningActivityIds.contains(flowNode.getId())) {
                    List<SequenceFlow> incomingFlows = flowNode.getIncomingFlows();
                    Iterator var11 = incomingFlows.iterator();

                    while(var11.hasNext()) {
                        SequenceFlow sequenceFlow = (SequenceFlow)var11.next();
                        if (((List)allHistoricActivityNodeList.stream().map(BaseElement::getId).collect(Collectors.toList())).contains(sequenceFlow.getSourceFlowElement().getId())) {
                            runningActivityFlowsIds.add(sequenceFlow.getId());
                        }
                    }
                }
            }

            return runningActivityFlowsIds;
        }
    }
}
