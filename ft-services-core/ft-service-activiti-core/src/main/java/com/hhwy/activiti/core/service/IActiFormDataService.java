package com.hhwy.activiti.core.service;

import com.hhwy.activiti.core.domain.po.ActiFormData;

import java.util.List;

/**
 * <br>描 述： 动态单Service接口
 * <br>创 建 人：Jinzhaoqiang
 * <br>创建时间：2021/7/28 15:23
 * <br>修改备注：无
 * <br>版本：1.0.0
 */
public interface IActiFormDataService {
    /**
     * 查询动态单
     *
     * @param id 动态单ID
     * @return 动态单
     */
    ActiFormData selectActiFormDataById(Long id);

    List<ActiFormData> selectActiFormDataByBusinessKey(String businessKey);

    /**
     * 查询动态单列表
     *
     * @param ActiFormData 动态单
     * @return 动态单集合
     */
    List<ActiFormData> selectActiFormDataList(ActiFormData ActiFormData);

    /**
     * 新增动态单
     *
     * @param ActiFormData 动态单
     * @return 结果
     */
    int insertActiFormData(ActiFormData ActiFormData);

    /**
     * 新增动态单集合
     *
     * @param actiFormData 动态表单集合
     * @return
     */
    int insertActiFormDatas(List<ActiFormData> actiFormData);

    /**
     * 修改动态单
     *
     * @param ActiFormData 动态单
     * @return 结果
     */
    int updateActiFormData(ActiFormData ActiFormData);

    /**
     * 批量删除动态单
     *
     * @param ids 需要删除的动态单ID
     * @return 结果
     */
    int deleteActiFormDataByIds(Long[] ids);

    /**
     * 删除动态单信息
     *
     * @param id 动态单ID
     * @return 结果
     */
    int deleteActiFormDataById(Long id);
}
