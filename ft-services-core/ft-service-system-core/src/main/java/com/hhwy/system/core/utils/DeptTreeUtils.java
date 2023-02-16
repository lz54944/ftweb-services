package com.hhwy.system.core.utils;

import com.hhwy.system.api.domain.SysDept;
import java.util.ArrayList;
import java.util.List;

public class DeptTreeUtils {
    private List<SysDept> deptCommon;
    private List<SysDept> list = new ArrayList<>();

    public List<SysDept> deptList(List<SysDept> dept){
        this.deptCommon = dept;
        for (SysDept x : dept) {
            if(x.getParentId() == null || x.getParentId() == 0L){
                x.setChildren(menuChild(x.getDeptId()));
                list.add(x);
            }
        }
        return list;
    }

    private List<SysDept> menuChild(Long id) {
        List<SysDept> lists = new ArrayList<>();
        for (SysDept x : deptCommon) {
            if (id.equals(x.getParentId())) {
                x.setChildren(menuChild(x.getDeptId()));
                lists.add(x);
            }
        }
        return lists;
    }
}
