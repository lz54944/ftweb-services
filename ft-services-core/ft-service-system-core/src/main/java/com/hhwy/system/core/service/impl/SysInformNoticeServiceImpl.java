package com.hhwy.system.core.service.impl;

import com.hhwy.common.core.utils.DateUtils;
import com.hhwy.common.security.service.TokenService;
import com.hhwy.system.api.domain.SysUser;
import com.hhwy.system.core.constant.NoticeStatus;
import com.hhwy.system.core.constant.NoticeType;
import com.hhwy.system.core.constant.ReadStatus;
import com.hhwy.system.core.domain.SysInformNotice;
import com.hhwy.system.core.domain.SysNoticeRecipient;
import com.hhwy.system.core.domain.vo.InformNotice;
import com.hhwy.system.core.domain.vo.InformNoticeQueryVo;
import com.hhwy.system.core.domain.vo.InformNoticeVo;
import com.hhwy.system.core.mapper.SysInformNoticeMapper;
import com.hhwy.system.core.mapper.SysNoticeRecipientMapper;
import com.hhwy.system.core.service.ISysInformNoticeService;
import io.seata.common.util.CollectionUtils;
import io.seata.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 通知公告Service业务层处理
 * 
 * @author 韩
 * @date 2022-12-23
 */
@Service
public class SysInformNoticeServiceImpl implements ISysInformNoticeService {

    @Autowired
    private SysInformNoticeMapper sysInformNoticeMapper;

    @Autowired
    private SysNoticeRecipientMapper sysNoticeRecipientMapper;

    @Autowired
    private TokenService tokenService;

    /**
     * 查询通知公告
     * 
     * @param noticeId 通知公告主键
     * @return 通知公告
     */
    @Override
    public SysInformNotice selectSysInformNoticeByNoticeId(Long noticeId) {
        SysInformNotice notice = sysInformNoticeMapper.selectSysInformNoticeByNoticeId(noticeId);
        List<SysNoticeRecipient> recipientList = sysNoticeRecipientMapper.getSysNoticeRecipientListByNoticeId(noticeId);
        notice.setRecipientList(recipientList);
        return notice;
    }

    /**
     * 查询通知公告列表
     * 
     * @param queryVo 通知公告
     * @return 通知公告
     */
    @Override
    public List<SysInformNotice> selectSysInformNoticeList(InformNoticeQueryVo queryVo) {

        SysUser sysUser = tokenService.getSysUser();
        queryVo.setTenantKey(sysUser.getTenantKey());
        queryVo.setUserId(sysUser.getUserId());

        return sysInformNoticeMapper.selectSysInformNoticeList(queryVo);
    }

    /**
     * 新增通知公告
     *
     * @param sysInformNotice 通知公告
     * @return
     */
    @Override
    @Transactional
    public int insertSysInformNotice(SysInformNotice sysInformNotice) {
        sysInformNotice.setCreateTime(DateUtils.getNowDate());
        sysInformNotice.setUpdateTime(DateUtils.getNowDate());

        sysInformNotice.setTenantKey(tokenService.getTenantKey());

        int result = sysInformNoticeMapper.insertSysInformNotice(sysInformNotice);

        if(!NoticeType.ANNOUNCEMENT.equals(sysInformNotice.getNoticeType())){
            List<SysNoticeRecipient> recipientList = sysInformNotice.getRecipientList();
            if(CollectionUtils.isNotEmpty(recipientList)){
                Long noticeId = sysInformNotice.getNoticeId();
                for (SysNoticeRecipient recipient : recipientList) {
                    recipient.setNoticeId(noticeId);
                    recipient.setReadStatus(ReadStatus.UNREAD);
                }
                sysNoticeRecipientMapper.insertSysNoticeRecipientList(recipientList);
            }
        }

        return result;
    }

    /**
     * 修改通知公告
     * 
     * @param sysInformNotice 通知公告
     * @return 结果
     */
    @Override
    @Transactional
    public int updateSysInformNotice(SysInformNotice sysInformNotice) {
        //修改接收人
        //删除旧数据
        Long noticeId = sysInformNotice.getNoticeId();
        sysNoticeRecipientMapper.deleteSysNoticeRecipientByNoticeId(noticeId);
        //插入新的接收人
        if(!NoticeType.ANNOUNCEMENT.equals(sysInformNotice.getNoticeType())){
            List<SysNoticeRecipient> recipientList = sysInformNotice.getRecipientList();
            if(CollectionUtils.isNotEmpty(recipientList)){
                for (SysNoticeRecipient recipient : recipientList) {
                    recipient.setNoticeId(noticeId);
                    recipient.setReadStatus(ReadStatus.UNREAD);
                }
                sysNoticeRecipientMapper.insertSysNoticeRecipientList(recipientList);
            }
        }

        //修改消息
        sysInformNotice.setUpdateTime(DateUtils.getNowDate());
        return sysInformNoticeMapper.updateSysInformNotice(sysInformNotice);
    }

    /**
     * 批量删除通知公告
     * 
     * @param noticeIds 需要删除的通知公告主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteSysInformNoticeByNoticeIds(Long[] noticeIds) {
        //删除接收人
        sysNoticeRecipientMapper.deleteSysNoticeRecipientByNoticeIds(noticeIds);
        return sysInformNoticeMapper.deleteSysInformNoticeByNoticeIds(noticeIds);
    }

    /**
     * 删除通知公告信息
     * 
     * @param noticeId 通知公告主键
     * @return 结果
     */
    @Override
    public int deleteSysInformNoticeByNoticeId(Long noticeId) {
        return sysInformNoticeMapper.deleteSysInformNoticeByNoticeId(noticeId);
    }

    /**
     * 根据当前登录人以及消息类型查询消息列表
     * @return
     */
    @Override
    public List<InformNoticeVo> list4LoginUser() {

        InformNoticeQueryVo queryVo = new InformNoticeQueryVo();
        queryVo.setReadStatus(ReadStatus.UNREAD);//获取读取状态为未读的消息

        SysUser sysUser = tokenService.getSysUser();

        queryVo.setTenantKey(sysUser.getTenantKey());
        queryVo.setUserId(sysUser.getUserId());

        List<InformNoticeVo> informNoticeVoList = new ArrayList<>();

        //获取读取状态为未读的通知
        queryVo.setNoticeType(NoticeType.NOTICE);
        List<InformNotice> noticeList = sysInformNoticeMapper.getNoticeAboutLoginUser(queryVo);
        InformNoticeVo noticeVo = new InformNoticeVo();
        noticeVo.setKey(NoticeType.NOTICE);
        noticeVo.setName(NoticeType.NOTICE_NAME);
        noticeVo.setList(noticeList);
        informNoticeVoList.add(noticeVo);

        //获取未读的公告
        queryVo.setNoticeType(NoticeType.ANNOUNCEMENT);
        List<InformNotice> announcementList = sysInformNoticeMapper.getAllAnnouncement(queryVo);
        for (InformNotice informNotice : announcementList) {
            if(informNotice.getClickClose() == null){
                informNotice.setClickClose(ReadStatus.UNREAD);
            }
        }
        InformNoticeVo announcementVo = new InformNoticeVo();
        announcementVo.setKey(NoticeType.ANNOUNCEMENT);
        announcementVo.setName(NoticeType.ANNOUNCEMENT_NAME);
        announcementVo.setList(announcementList);
        informNoticeVoList.add(announcementVo);

        return informNoticeVoList;
    }

    /**
     * 发布消息
     * @param noticeTitle    消息标题
     * @param noticeType     消息类型
     * @param noticeContent  消息内容
     * @param recipient      接收人
     * @return
     */
    @Override
    @Transactional
    public int postMessage(String noticeTitle, String noticeType, String noticeContent, Map<Long, String> recipient) {
        if(StringUtils.isBlank(noticeTitle)){
            throw new RuntimeException("消息标题不能为空！");
        }

        SysUser sysUser = tokenService.getSysUser();

        //插入消息
        SysInformNotice notice = new SysInformNotice();
        notice.setNoticeTitle(noticeTitle);
        notice.setNoticeType(noticeType);
        notice.setNoticeContent(noticeContent);
        notice.setStatus(NoticeStatus.NORMAL);
        notice.setSenderId(sysUser.getUserId());
        notice.setSenderName(sysUser.getUserName());
        notice.setCreateTime(DateUtils.getNowDate());
        notice.setUpdateTime(DateUtils.getNowDate());
        notice.setTenantKey(sysUser.getTenantKey());
        int i = sysInformNoticeMapper.insertSysInformNotice(notice);

        //插入消息接收人
        if(NoticeType.NOTICE.equals(noticeType)){
            if(recipient != null && recipient.size() != 0){
                List<SysNoticeRecipient> noticeRecipientList = new ArrayList<>();
                Iterator<Map.Entry<Long, String>> iterator = recipient.entrySet().iterator();
                while (iterator.hasNext()){
                    Map.Entry<Long, String> entry = iterator.next();
                    Long key = entry.getKey();
                    String value = entry.getValue();
                    SysNoticeRecipient noticeRecipient = new SysNoticeRecipient();
                    noticeRecipient.setNoticeId(notice.getNoticeId());
                    noticeRecipient.setReadStatus(ReadStatus.UNREAD);
                    noticeRecipient.setRecipientId(key);
                    noticeRecipient.setRecipientName(value);
                    noticeRecipientList.add(noticeRecipient);
                }
                sysNoticeRecipientMapper.insertSysNoticeRecipientList(noticeRecipientList);
            }
        }

        return i;
    }

    /**
     * 查询通知公告列表（根据消息类型）
     * @param queryVo
     * @return
     */
    @Override
    public List<InformNotice> selectListByNoticeType(InformNoticeQueryVo queryVo) {
        SysUser sysUser = tokenService.getSysUser();

        queryVo.setTenantKey(sysUser.getTenantKey());
        queryVo.setUserId(sysUser.getUserId());

        List<InformNotice> noticeList = new ArrayList<>();
        String noticeType = queryVo.getNoticeType();//消息类型
        if(NoticeType.NOTICE.equals(noticeType)){
            noticeList = sysInformNoticeMapper.getNoticeAboutLoginUser(queryVo);
            return noticeList;
        }

        if(NoticeType.ANNOUNCEMENT.equals(noticeType)){
            noticeList = sysInformNoticeMapper.getAllAnnouncement(queryVo);
            for (InformNotice notice : noticeList) {
                if(notice.getClickClose() == null){
                    notice.setClickClose(ReadStatus.UNREAD);
                }
            }
        }
        return noticeList;
    }

    /**
     * 根据消息id获取消息详情数据并处理读取状态
     * @param noticeId
     * @return
     */
    @Override
    public InformNotice getInfoById(Long noticeId) {
        SysUser sysUser = tokenService.getSysUser();
        Long userId = sysUser.getUserId();//当前登录人用户id（接收人id）
        InformNotice notice = sysInformNoticeMapper.getInfoById(noticeId,userId);
        String userName = sysUser.getUserName();//当前登录人用户名称
        String clickClose = notice.getClickClose();//读取状态
        String noticeType = notice.getType();//消息类型

        if(NoticeType.NOTICE.equals(noticeType)){
            //类型为消息
            if(ReadStatus.UNREAD.equals(clickClose)){
                //未读，修改状态
                sysNoticeRecipientMapper.updateReadStatusByNoticeIdAndRecipientId(noticeId,userId);
            }
        }else if(NoticeType.ANNOUNCEMENT.equals(noticeType)){
            //类型为公告
            if(clickClose == null){
                //未读，插入接收人并设置读取状态为已读
                SysNoticeRecipient noticeRecipient = new SysNoticeRecipient();
                noticeRecipient.setNoticeId(noticeId);
                noticeRecipient.setReadStatus(ReadStatus.READ);
                noticeRecipient.setRecipientId(userId);
                noticeRecipient.setRecipientName(userName);
                sysNoticeRecipientMapper.insertSysNoticeRecipient(noticeRecipient);
            }
        }
        notice.setClickClose(ReadStatus.READ);
        return notice;
    }

    /**
     * 获取当前登录人发布的消息
     * @param queryVo
     * @return
     */
    @Override
    public List<SysInformNotice> getMessage4LoginUserPost(InformNoticeQueryVo queryVo) {
        SysUser sysUser = tokenService.getSysUser();
        queryVo.setTenantKey(sysUser.getTenantKey());
        queryVo.setUserId(sysUser.getUserId());

        List<SysInformNotice> noticeList = sysInformNoticeMapper.getMessage4LoginUserPost(queryVo);
        return noticeList;
    }
}













