package com.hhwy.activiti.core.utils;

import com.hhwy.common.core.domain.R;
import com.hhwy.common.core.utils.StringUtils;
import com.hhwy.common.core.web.domain.AjaxResult;
import com.hhwy.system.api.RemoteUserService;
import com.hhwy.system.api.domain.SysUser;
import com.hhwy.system.api.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <br>描 述： UserService
 * <br>创 建 人：Jinzhaoqiang
 * <br>创建时间：2021/8/13 10:23
 * <br>修改备注：无
 * <br>版本：1.0.0
 */
@Component
public class UserService {

    @Autowired
    private RemoteUserService userService;

    /**
     * 字段描述：key:userName value:nickName
     */
    private Map<String,String> nickNameMap= new HashMap<>();

    public String getNickName(String tenantKey, String userName){
        String nickName = nickNameMap.get(userName);
        if(StringUtils.isEmpty(nickName)){
            R<LoginUser> userInfo = userService.getUserInfo(tenantKey, userName);
            nickName = userInfo.getData().getSysUser().getNickName();
            nickNameMap.put(userName,nickName);
        }
        return nickName;
    }

    /**
     * 通过用户昵称 模糊查询用户信息
     *
     * @return 结果
     */
    public List<SysUser> getUserListByNickName(String nickName){
        List<SysUser> sysUsers = userService.getUserListByNickName(nickName).getData();
        return sysUsers;
    }
}
