package com.hhwy.activiti.core.service.impl;

import com.hhwy.activiti.core.domain.dto.HistoryDataDTO;
import com.hhwy.activiti.core.domain.dto.HistoryFormDataDTO;
import com.hhwy.activiti.core.domain.po.ActiFormData;
import com.hhwy.activiti.core.service.IActFormHistoryDataService;
import com.hhwy.activiti.core.service.IActiFormDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <br>描 述： ActFormHistoryDataServiceImpl
 * <br>创 建 人：Jinzhaoqiang
 * <br>创建时间：2021/7/28 15:24
 * <br>修改备注：无
 * <br>版本：1.0.0
 */
@Service
public class ActFormHistoryDataServiceImpl implements IActFormHistoryDataService {
    @Autowired
    private IActiFormDataService actWorkflowFormDataService;


    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<HistoryDataDTO> historyDataShow(String businessKey) {
        List<HistoryDataDTO> returnHistoryFromDataDTOS = new ArrayList<>();
        List<ActiFormData> actiFormData = actWorkflowFormDataService.selectActiFormDataByBusinessKey(businessKey);
        Map<String, List<ActiFormData>> collect = actiFormData.stream().collect(Collectors.groupingBy(ActiFormData::getTaskNodeName));
        collect.entrySet().forEach(
                entry -> {
                    HistoryDataDTO returnHistoryFromDataDTO = new HistoryDataDTO();
                    returnHistoryFromDataDTO.setTaskNodeName(entry.getValue().get(0).getTaskNodeName());
                    returnHistoryFromDataDTO.setCreateName(entry.getValue().get(0).getCreateNickName());
                    returnHistoryFromDataDTO.setCreatedDate(sdf.format(entry.getValue().get(0).getCreateTime()));
                    returnHistoryFromDataDTO.setFormHistoryDataDTO(entry.getValue().stream().map(awfd -> new HistoryFormDataDTO(awfd.getControlName(), awfd.getControlValue())).collect(Collectors.toList()));
                    returnHistoryFromDataDTOS.add(returnHistoryFromDataDTO);
                }
        );
        List<HistoryDataDTO> collect1 = returnHistoryFromDataDTOS.stream().sorted((x, y) -> x.getCreatedDate().compareTo(y.getCreatedDate())).collect(Collectors.toList());

        return collect1;
    }


}
