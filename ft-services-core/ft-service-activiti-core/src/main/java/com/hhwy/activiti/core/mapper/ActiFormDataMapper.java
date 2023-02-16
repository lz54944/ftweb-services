package com.hhwy.activiti.core.mapper;

import com.hhwy.activiti.core.domain.po.ActiFormData;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <br>描 述： 动态单Mapper接口
 * <br>创 建 人：Jinzhaoqiang
 * <br>创建时间：2021/7/28 15:23
 * <br>修改备注：无
 * <br>版本：1.0.0
 */
public interface ActiFormDataMapper {
    /**
     * 查询动态单
     *
     * @param id 动态单ID
     * @return 动态单
     */
    public ActiFormData selectActiFormDataById(Long id);

    /**
     * 查询动态单
     *
     * @param businessKey 动态单ID
     * @return 动态单
     */
    public List<ActiFormData> selectActiFormDataByBusinessKey(String businessKey);

    /**
     * 查询动态单列表
     *
     * @param ActiFormData 动态单
     * @return 动态单集合
     */
    public List<ActiFormData> selectActiFormDataList(ActiFormData ActiFormData);

    /**
     * 新增动态单
     *
     * @param ActiFormData 动态单
     * @return 结果
     */
    public int insertActiFormData(ActiFormData ActiFormData);


    /**
     * 新增动态单
     *
     * @param
     * @return 结果
     */
    public int insertActiFormDatas(@Param("createBy") String createBy, @Param("ActiFormData") List<ActiFormData> ActiFormData, Date date, String createName);


    /**
     * 修改动态单
     *
     * @param ActiFormData 动态单
     * @return 结果
     */
    public int updateActiFormData(ActiFormData ActiFormData);

    /**
     * 删除动态单
     *
     * @param id 动态单ID
     * @return 结果
     */
    public int deleteActiFormDataById(Long id);

    /**
     * 批量删除动态单
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteActiFormDataByIds(Long[] ids);
}
