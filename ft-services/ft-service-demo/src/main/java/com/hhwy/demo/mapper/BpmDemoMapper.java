package com.hhwy.demo.mapper;

import com.hhwy.demo.domain.BpmDemo;
import java.util.List;

/**
 * 流程测试Mapper接口
 *
 * @author jzq
 * @date 2021-10-18
 */
public interface BpmDemoMapper {
    /**
     * 查询流程测试
     *
     * @param id 流程测试ID
     * @return 流程测试
     */
    BpmDemo selectBpmDemoById(Long id);

    /**
     * 查询流程测试列表
     *
     * @param bpmDemo 流程测试
     * @return 流程测试集合
     */
    List<BpmDemo> selectBpmDemoList(BpmDemo bpmDemo);

    /**
     * 新增流程测试
     *
     * @param bpmDemo 流程测试
     * @return 结果
     */
    int insertBpmDemo(BpmDemo bpmDemo);

    /**
     * 修改流程测试
     *
     * @param bpmDemo 流程测试
     * @return 结果
     */
    int updateBpmDemo(BpmDemo bpmDemo);

    /**
     * 删除流程测试
     *
     * @param id 流程测试ID
     * @return 结果
     */
    int deleteBpmDemoById(Long id);

    /**
     * 批量删除流程测试
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteBpmDemoByIds(String[] ids);
}
