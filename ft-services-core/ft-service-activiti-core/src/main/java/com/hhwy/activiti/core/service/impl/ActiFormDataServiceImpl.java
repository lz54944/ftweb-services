package com.hhwy.activiti.core.service.impl;

import com.hhwy.activiti.core.domain.po.ActiFormData;
import com.hhwy.activiti.core.mapper.ActiFormDataMapper;
import com.hhwy.activiti.core.service.IActiFormDataService;
import com.hhwy.common.core.utils.DateUtils;
import com.hhwy.common.core.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <br>描 述： 动态单Service业务层处理
 * <br>创 建 人：Jinzhaoqiang
 * <br>创建时间：2021/7/28 15:23
 * <br>修改备注：无
 * <br>版本：1.0.0
 */
@Service
public class ActiFormDataServiceImpl implements IActiFormDataService {
    @Autowired
    private ActiFormDataMapper actiFormDataMapper;

    /**
     * 查询动态单
     *
     * @param id 动态单ID
     * @return 动态单
     */
    @Override
    public ActiFormData selectActiFormDataById(Long id) {
        return actiFormDataMapper.selectActiFormDataById(id);
    }

    @Override
    public List<ActiFormData> selectActiFormDataByBusinessKey(String businessKey) {
        return actiFormDataMapper.selectActiFormDataByBusinessKey(businessKey);
    }

    /**
     * 查询动态单列表
     *
     * @param ActiFormData 动态单
     * @return 动态单
     */
    @Override
    public List<ActiFormData> selectActiFormDataList(ActiFormData ActiFormData) {
        return actiFormDataMapper.selectActiFormDataList(ActiFormData);
    }

    /**
     * 新增动态单
     *
     * @param ActiFormData 动态单
     * @return 结果
     */
    @Override
    public int insertActiFormData(ActiFormData ActiFormData) {
        ActiFormData.setCreateTime(DateUtils.getNowDate());
        return actiFormDataMapper.insertActiFormData(ActiFormData);
    }

    @Override
    public int insertActiFormDatas(List<ActiFormData> actiFormData) {
        return actiFormDataMapper.insertActiFormDatas(SecurityUtils.getUserName(), actiFormData, new Date(), SecurityUtils.getUserName());
    }


    /**
     * 修改动态单
     *
     * @param ActiFormData 动态单
     * @return 结果
     */
    @Override
    public int updateActiFormData(ActiFormData ActiFormData) {
        return actiFormDataMapper.updateActiFormData(ActiFormData);
    }

    /**
     * 批量删除动态单
     *
     * @param ids 需要删除的动态单ID
     * @return 结果
     */
    @Override
    public int deleteActiFormDataByIds(Long[] ids) {
        return actiFormDataMapper.deleteActiFormDataByIds(ids);
    }

    /**
     * 删除动态单信息
     *
     * @param id 动态单ID
     * @return 结果
     */
    @Override
    public int deleteActiFormDataById(Long id) {
        return actiFormDataMapper.deleteActiFormDataById(id);
    }
}
