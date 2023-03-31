package com.hhwy.system.core.mapper;

import com.hhwy.system.core.domain.SysInformNotice;
import com.hhwy.system.core.domain.vo.InformNotice;
import com.hhwy.system.core.domain.vo.InformNoticeQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 通知公告Mapper接口
 * 
 * @author 韩
 * @date 2022-12-23
 */
@Repository
public interface SysInformNoticeMapper {
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
     * @return 结果
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
     * 删除通知公告
     * 
     * @param noticeId 通知公告主键
     * @return 结果
     */
    int deleteSysInformNoticeByNoticeId(Long noticeId);

    /**
     * 批量删除通知公告
     * 
     * @param noticeIds 需要删除的数据主键集合
     * @return 结果
     */
    int deleteSysInformNoticeByNoticeIds(Long[] noticeIds);

    /**
     * 根据当前登录人以及消息类型查询消息列表
     * @param queryVo
     * @return
     */
    List<InformNotice> getNoticeAboutLoginUser(InformNoticeQueryVo queryVo);

    /**
     * 获取当前登录人所有未读的公告
     * @param queryVo
     * @return
     */
    List<InformNotice> getAllAnnouncement4LoginUser(InformNoticeQueryVo queryVo);

    /**
     * 获取所有的公告
     * @param queryVo
     * @return
     */
    List<InformNotice> getAllAnnouncement(InformNoticeQueryVo queryVo);

    /**
     * 查询通知公告列表（根据消息类型）
     * @param queryVo
     * @return
     */
    List<InformNotice> selectListByNoticeType(InformNoticeQueryVo queryVo);

    /**
     * 根据消息id获取消息详情数据
     * @param noticeId
     * @param recipientId
     * @return
     */
    InformNotice getInfoById(@Param("noticeId") Long noticeId,@Param("recipientId") Long recipientId);

    /**
     * 获取当前登录人发布的消息
     * @param queryVo
     * @return
     */
    List<SysInformNotice> getMessage4LoginUserPost(InformNoticeQueryVo queryVo);

    /**
     * 关闭结束时间在指定日期之前所有的消息
     * @param date
     */
    void closeOutOfDateNotice(@Param("date") String date);
}
