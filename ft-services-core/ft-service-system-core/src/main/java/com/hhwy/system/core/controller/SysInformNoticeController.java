package com.hhwy.system.core.controller;

import com.hhwy.common.core.web.controller.BaseController;
import com.hhwy.common.core.web.domain.AjaxResult;
import com.hhwy.system.core.domain.SysInformNotice;
import com.hhwy.system.core.domain.vo.InformNotice;
import com.hhwy.system.core.domain.vo.InformNoticeQueryVo;
import com.hhwy.system.core.domain.vo.InformNoticeVo;
import com.hhwy.system.core.service.ISysInformNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 通知公告Controller
 * 
 * @author 韩
 * @date 2022-12-23
 */
@RestController
@RequestMapping("/inform/notice")
public class SysInformNoticeController extends BaseController {

    @Autowired
    private ISysInformNoticeService sysInformNoticeService;

    /**
     * 查询通知公告列表
     */
    @GetMapping("/list")
    public AjaxResult list(InformNoticeQueryVo queryVo) {
        startPage();
        List<SysInformNotice> list = sysInformNoticeService.selectSysInformNoticeList(queryVo);
        return getDataTableAjaxResult(list);
    }

    /**
     * 查询通知公告列表（我的消息）（根据消息类型）
     * @param queryVo
     * @return
     */
    @GetMapping("/listByNoticeType")
    public AjaxResult listByNoticeType(InformNoticeQueryVo queryVo) {
        startPage();
        List<InformNotice> list = sysInformNoticeService.selectListByNoticeType(queryVo);
        return getDataTableAjaxResult(list);
    }

    /**
     * 根据当前登录人以及消息类型查询消息列表
     * @return
     */
    @GetMapping("list4LoginUser")
    public AjaxResult list4LoginUser(){
        startPage();
        List<InformNoticeVo> list = sysInformNoticeService.list4LoginUser();
        return AjaxResult.success(list);
    }

    /**
     * 获取通知公告详细信息
     */
    @GetMapping(value = "get/{noticeId}")
    public AjaxResult getInfo(@PathVariable("noticeId") Long noticeId) {
        return AjaxResult.success(sysInformNoticeService.selectSysInformNoticeByNoticeId(noticeId));
    }

    /**
     * 根据消息id获取消息详情数据并处理读取状态
     * @param noticeId
     * @return
     */
    @GetMapping(value = "getById/{noticeId}")
    public AjaxResult getInfoById(@PathVariable("noticeId") Long noticeId) {
        return AjaxResult.success(sysInformNoticeService.getInfoById(noticeId));
    }

    /**
     * 新增通知公告
     */
    @PostMapping("add")
    public AjaxResult add(@RequestBody SysInformNotice sysInformNotice) {
        return toAjax(sysInformNoticeService.insertSysInformNotice(sysInformNotice));
    }

    /**
     * 修改通知公告
     */
    @PostMapping("edit")
    public AjaxResult edit(@RequestBody SysInformNotice sysInformNotice) {
        return toAjax(sysInformNoticeService.updateSysInformNotice(sysInformNotice));
    }

    /**
     * 删除通知公告
     */
	@DeleteMapping("remove/{noticeIds}")
    public AjaxResult remove(@PathVariable Long[] noticeIds) {
        return toAjax(sysInformNoticeService.deleteSysInformNoticeByNoticeIds(noticeIds));
    }

    /**
     * 获取当前登录人发布的消息
     * @param queryVo
     * @return
     */
    @GetMapping("message4LoginUserPost")
    public AjaxResult message4LoginUserPost(InformNoticeQueryVo queryVo){
	    List<SysInformNotice> noticeList = sysInformNoticeService.getMessage4LoginUserPost(queryVo);
	    return getDataTableAjaxResult(noticeList);
    }

    /**
     * 发布消息
     * @param noticeTitle    消息标题
     * @param noticeType     消息类型 （1通知，2公告，3待办）
     * @param noticeContent  消息内容
     * @param recipient      接收人   （key：接收人id，value：接收人名称）
     * @return
     */
    @PostMapping("postMessage")
    public AjaxResult postMessage(String noticeTitle, String noticeType, String noticeContent, @RequestBody Map<Long,String> recipient){
        int i = sysInformNoticeService.postMessage(noticeTitle, noticeType, noticeContent, recipient);
        if(i > 0){
            return AjaxResult.success("发送成功！");
        }else{
            throw new RuntimeException("发送失败！");
        }
    }
}





















