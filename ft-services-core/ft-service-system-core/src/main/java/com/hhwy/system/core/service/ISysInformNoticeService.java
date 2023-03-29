package com.hhwy.system.core.service;

import com.hhwy.system.core.domain.SysInformNotice;
import com.hhwy.system.core.domain.vo.InformNotice;
import com.hhwy.system.core.domain.vo.InformNoticeQueryVo;
import com.hhwy.system.core.domain.vo.InformNoticeVo;
import java.util.List;
import java.util.Map;

/**
 * 通知公告Service接口
 * 
 * @author 韩
 * @date 2022-12-23
 */
public interface ISysInformNoticeService {
    /**
     * 查询通知公告
     * 
     * @param noticeId 通知公告主键
     * @return 通知公告
     */
    SysInformNotice selectSysInformNoticeByNoticeId(Long noticeId);

    /**
     * 查询通知公告列表
     * 
     * @param queryVo 通知公告
     * @return 通知公告集合
     */
    List<SysInformNotice> selectSysInformNoticeList(InformNoticeQueryVo queryVo);

    /**
     * 新增通知公告
     *
     * @param sysInformNotice 通知公告
     * @return
     */
    int insertSysInformNotice(SysInformNotice sysInformNotice);

    /**
     * 修改通知公告
     * 
     * @param sysInformNotice 通知公告
     * @return 结果
     */
    int updateSysInformNotice(SysInformNotice sysInformNotice);

    /**
     * 批量删除通知公告
     * 
     * @param noticeIds 需要删除的通知公告主键集合
     * @return 结果
     */
    int deleteSysInformNoticeByNoticeIds(Long[] noticeIds);

    /**
     * 删除通知公告信息
     * 
     * @param noticeId 通知公告主键
     * @return 结果
     */
    int deleteSysInformNoticeByNoticeId(Long noticeId);

    /**
     * 根据当前登录人以及消息类型查询消息列表
     * @return
     */
    List<InformNoticeVo> list4LoginUser();

    /**
     * 发布消息
     * @param noticeTitle    消息标题
     * @param noticeType     消息类型 （1通知，2公告，3待办）
     * @param noticeContent  消息内容
     * @param recipient      接收人   （key：接收人id，value：接收人名称）
     * @return
     */
    int postMessage(String noticeTitle, String noticeType, String noticeContent, Map<Long, String> recipient);

    /**
     * 查询通知公告列表（根据消息类型）
     * @param queryVo
     * @return
     */
    List<InformNotice> selectListByNoticeType(InformNoticeQueryVo queryVo);

    /**
     * 根据消息id获取消息详情数据并处理读取状态
     * @param noticeId
     * @return
     */
    InformNotice getInfoById(Long noticeId);

    /**
     * 获取当前登录人发布的消息
     * @param queryVo
     * @return
     */
    List<SysInformNotice> getMessage4LoginUserPost(InformNoticeQueryVo queryVo);
}
