package com.hhwy.activiti.core.service;

import com.hhwy.activiti.core.domain.dto.HistoryDataDTO;

import java.util.List;

/**
 * <br>描 述： IActFormHistoryDataService
 * <br>创 建 人：Jinzhaoqiang
 * <br>创建时间：2021/7/28 15:23
 * <br>修改备注：无
 * <br>版本：1.0.0
 */
public interface IActFormHistoryDataService {

    public List<HistoryDataDTO> historyDataShow(String instanceId);
}
