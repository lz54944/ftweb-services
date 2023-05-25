package com.hhwy.system.core.utils;

import com.hhwy.common.core.utils.StringUtils;
import com.hhwy.system.core.domain.SysDictType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.util.CollectionUtils;

public class DictTypeTreeUtils {
    private List<SysDictType> dictTypeCommon;
    private List<SysDictType> list = new ArrayList<>();
    private List<Long> idList = new ArrayList();

    public DictTypeTreeUtils() {
    }


    public List<SysDictType> dictTypeList(List<SysDictType> dictType){
        this.dictTypeCommon = dictType;
        for (SysDictType x : dictType) {
            idList.add(x.getDictId());
        }
        for (SysDictType x : dictType) {
            if(x.getParentId() != null && x.getParentId() != 0L && idList.contains(x.getParentId())){
            } else {
                x.setChildren(menuChild(x.getDictId()));
                list.add(x);
            }
        }
        return list;
    }

    private List<SysDictType> menuChild(Long id) {
        List<SysDictType> lists = new ArrayList<>();
        for (SysDictType x : dictTypeCommon) {
            if (id.equals(x.getParentId())) {
                x.setChildren(menuChild(x.getDictId()));
                lists.add(x);
            }
        }
        return lists;
    }

    // 通过字典名称实现模糊查询字典树 包含上级节点
    public List<SysDictType> filterTreeByDictName(List<SysDictType> list, String dictName) {
        if(CollectionUtils.isEmpty(list)) {
            return list;
        }

        filterDictName(list, dictName);
        return list;
    }

    public void filterDictName(List<SysDictType> list, String dictName) {
        Iterator<SysDictType> parent = list.iterator();
        while (parent.hasNext()) {
            // 当前节点
            SysDictType sysDictType = parent.next();
            if(StringUtils.isNotBlank(dictName) && !sysDictType.getDictName().contains(dictName)) {
                // 当前节点不包含字典名称，继续遍历下一级
                // 取出下一级节点
                List<SysDictType> children = sysDictType.getChildren();
                // 递归
                if(!CollectionUtils.isEmpty(children)) {
                    filterTreeByDictName(children, dictName);
                }

                // 下一级节点都被移除了，那么父节点也要移除，因为父节点不包含字典名称
                if(CollectionUtils.isEmpty(sysDictType.getChildren())) {
                    parent.remove();
                }
            } else {
                // 当前节点不包含字典名称，继续递归遍历
                // 子节点递归如果不包含字典名称则会进入if分支被删除
                List<SysDictType> children = sysDictType.getChildren();
                // 递归
                if(!CollectionUtils.isEmpty(children)) {
                    filterTreeByDictName(children, dictName);
                }
            }
        }
    }
}
