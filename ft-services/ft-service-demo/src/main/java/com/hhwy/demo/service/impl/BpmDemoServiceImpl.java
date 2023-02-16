package com.hhwy.demo.service.impl;

import java.util.List;
import com.hhwy.common.core.utils.DateUtils;
import com.hhwy.demo.domain.BpmDemo;
import com.hhwy.demo.mapper.BpmDemoMapper;
import com.hhwy.demo.service.IBpmDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hhwy.common.core.text.Convert;

/**
 * 流程测试Service业务层处理
 *
 * @author jzq
 * @date 2021-10-18
 */
@Service
public class BpmDemoServiceImpl implements IBpmDemoService {
    @Autowired
    private BpmDemoMapper bpmDemoMapper;

    /**
     * 查询流程测试
     *
     * @param id 流程测试ID
     * @return 流程测试
     */
    @Override
    public BpmDemo selectBpmDemoById(Long id) {
        return bpmDemoMapper.selectBpmDemoById(id);
    }

    /**
     * 查询流程测试列表
     *
     * @param bpmDemo 流程测试
     * @return 流程测试
     */
    @Override
    public List<BpmDemo> selectBpmDemoList(BpmDemo bpmDemo) {
        return bpmDemoMapper.selectBpmDemoList(bpmDemo);
    }

    /**
     * 新增流程测试
     *
     * @param bpmDemo 流程测试
     * @return 结果
     */
    @Override
    public int insertBpmDemo(BpmDemo bpmDemo) {
        bpmDemo.setCreateTime(DateUtils.getNowDate());
        return bpmDemoMapper.insertBpmDemo(bpmDemo);
    }

    /**
     * 修改流程测试
     *
     * @param bpmDemo 流程测试
     * @return 结果
     */
    @Override
    public int updateBpmDemo(BpmDemo bpmDemo) {
        bpmDemo.setUpdateTime(DateUtils.getNowDate());
        return bpmDemoMapper.updateBpmDemo(bpmDemo);
    }

    /**
     * 删除流程测试对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteBpmDemoByIds(String ids) {
        return bpmDemoMapper.deleteBpmDemoByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除流程测试信息
     *
     * @param id 流程测试ID
     * @return 结果
     */
    @Override
    public int deleteBpmDemoById(Long id) {
        return bpmDemoMapper.deleteBpmDemoById(id);
    }
}
