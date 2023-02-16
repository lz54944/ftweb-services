package com.hhwy.system.core.utils;

import com.hhwy.system.api.domain.SysMenu;
import java.util.ArrayList;
import java.util.List;

public class MenuTreeUtils {
    private List<SysMenu> menuCommon;
    private List<SysMenu> list = new ArrayList<>();

    public List<SysMenu> menuList(List<SysMenu> menu){
        this.menuCommon = menu;
        for (SysMenu x : menu) {
            if(x.getParentId() == null || x.getParentId() == 0L){
                x.setChildren(menuChild(x.getMenuId()));
                list.add(x);
            }
        }
        return list;
    }

    private List<SysMenu> menuChild(Long id) {
        List<SysMenu> lists = new ArrayList<>();
        for (SysMenu x : menuCommon) {
            if (id.equals(x.getParentId())) {
                x.setChildren(menuChild(x.getMenuId()));
                lists.add(x);
            }
        }
        return lists;
    }
}
