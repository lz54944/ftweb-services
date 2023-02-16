package com.hhwy.system.core.service.impl;

import com.hhwy.common.core.constant.Constants;
import com.hhwy.common.core.constant.UserConstants;
import com.hhwy.common.core.utils.StringUtils;
import com.hhwy.common.security.service.TokenService;
import com.hhwy.system.api.domain.SysMenu;
import com.hhwy.system.api.domain.SysRole;
import com.hhwy.system.api.domain.SysTenant;
import com.hhwy.system.api.domain.SysUser;
import com.hhwy.system.core.domain.vo.MetaVo;
import com.hhwy.system.core.domain.vo.RouterVo;
import com.hhwy.system.core.domain.vo.TreeSelect;
import com.hhwy.system.core.mapper.SysMenuMapper;
import com.hhwy.system.core.mapper.SysRoleMapper;
import com.hhwy.system.core.mapper.SysRoleMenuMapper;
import com.hhwy.system.core.service.ISysConfigService;
import com.hhwy.system.core.service.ISysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单 业务层处理
 *
 * @author hhwy
 */
@Service
public class SysMenuServiceImpl implements ISysMenuService {

    @Autowired
    private SysMenuMapper menuMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysRoleMenuMapper roleMenuMapper;

    @Autowired
    private ISysConfigService sysConfigService;

    @Autowired
    private TokenService tokenService;
    /**
     * 根据用户查询系统菜单列表
     *
     * @param sysUser 用户
     * @return 菜单列表
     */
    @Override
    public List<SysMenu> selectMenuList(SysUser sysUser) {
        return selectMenuList(new SysMenu(), sysUser);
    }

    /**
     * 查询系统菜单列表
     *
     * @param menu 菜单信息
     * @return 菜单列表
     */
    @Override
    public List<SysMenu> selectMenuList(SysMenu menu, SysUser sysUser) {
        List<SysMenu> menuList;
        // 管理员显示所有菜单信息
        if (sysUser.isAdmin()) {
            menuList = menuMapper.selectMenuList(menu,tokenService.getTenantKeyListForMenu());
        } else {
            menu.getParams().put("userId", sysUser.getUserId());
            menuList = menuMapper.selectMenuListByUserId(menu, tokenService.getTenantKey(), tokenService.getTenantKeyListForMenu());
        }
        return menuList;
    }

    /*public void setTenantInfo(SysMenu menu){
        menu.setTenantKey(tokenService.getTenantKey());
        Map<String, Object> params = menu.getParams();
        if(params == null){
            params = new HashMap<>();
        }
        params.put("tenantMenuStatus",tokenService.getTenant().getMenuStatus());
        menu.setParams(params);
    }*/

    @Override
    public List<SysMenu> selectMenuBarList(SysMenu menu, SysUser sysUser) {
        List<SysMenu> menuList;
        // 管理员显示所有菜单信息
        if (sysUser.isAdmin()) {
            menuList = menuMapper.selectMenuList(menu,tokenService.getTenantKeyListForMenu());
        } else {
            menu.getParams().put("userId", sysUser.getUserId());
            menuList = menuMapper.selectMenuBarListByUserId(menu, tokenService.getTenantKey(), tokenService.getTenantKeyListForMenu());
        }
        for (SysMenu sysMenu : menuList) {
            String isExternalLink = sysMenu.getIsExternalLink();
            String routerPath = sysMenu.getRouterPath();
            if ("1".equals(isExternalLink) && StringUtils.startsWithAny(routerPath, "{") ) {//是否为外链（0不是外链 1是外链）
                int index = routerPath.indexOf("}");
                String key = routerPath.substring(1,index);
                String uri = routerPath.substring(index+1);
                String host = sysConfigService.selectConfigByKey(key);
                Assert.notNull(host,"请在参数设置中配置外链变量`"+key+"`的值");
                routerPath = host+uri;
                sysMenu.setRouterPath(routerPath);
            }
        }
        return menuList;
    }

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectMenuPermsByUserId(SysUser user) {
        Long userId = user.getUserId();
        SysTenant tenant = user.getTenant();
        String tenantKey = tenant.getTenantKey();

        List<String> tenantKeyList = new ArrayList<>();
        tenantKeyList.add(tenantKey);
        if (!tenantKey.equals(Constants.MASTER_TENANT_KEY) && tenant.getRoleStatus().equals("1")) {
            tenantKeyList.add(Constants.MASTER_TENANT_KEY);
        }

        List<String> perms = menuMapper.selectMenuPermsByUserId(userId, tenantKey, tenantKeyList);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms) {
            if (StringUtils.isNotEmpty(perm)) {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * 获取按钮权限
     *
     * @param userId 用户id
     * @return 按钮权限信息
     */
    @Override
    public Set<String> selectButtonPermissionsByUserId(Long userId) {
        List<String> perms = menuMapper.selectButtonPermissionsByUserId(userId, tokenService.getTenantKey(), tokenService.getTenantKeyListForMenu());
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms) {
            if (StringUtils.isNotEmpty(perm)) {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId 角色ID
     * @return 选中菜单列表
     */
    @Override
    public List<Long> selectMenuIdListByRoleId(Long roleId) {
        SysRole role = roleMapper.selectRoleById(roleId);
        return menuMapper.selectMenuIdListByRoleId(roleId, role.getMenuCheckStrictly(),tokenService.getTenantKey(),tokenService.getTenantKeyListForMenu());
    }

    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    @Override
    public List<RouterVo> buildMenus(List<SysMenu> menus) {
        List<RouterVo> routers = new LinkedList<>();
        for (SysMenu menu : menus) {
            RouterVo router = new RouterVo();
            router.setHidden("1".equals(menu.getVisible()));
            router.setName(getRouteName(menu));
            router.setPath(getRouterPath(menu));
            router.setComponent(getComponent(menu));
            router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), StringUtils.equals("1", menu.getIsCache())));
            List<SysMenu> cMenus = menu.getChildren();
            if (!cMenus.isEmpty() && cMenus.size() > 0 && UserConstants.TYPE_DIR.equals(menu.getMenuType())) {
                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
                router.setChildren(buildMenus(cMenus));
            } else if (isMenuFrame(menu)) {
                router.setMeta(null);
                List<RouterVo> childrenList = new ArrayList<>();
                RouterVo children = new RouterVo();
                children.setPath(menu.getRouterPath());
                children.setComponent(menu.getComponentPath());
                children.setName(StringUtils.capitalize(menu.getRouterPath()));
                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), StringUtils.equals("1", menu.getIsCache())));
                childrenList.add(children);
                router.setChildren(childrenList);
            }
            routers.add(router);
        }
        return routers;
    }

    /**
     * 构建前端所需要树结构
     *
     * @param menus 菜单列表
     * @return 树结构列表
     */
    @Override
    public List<SysMenu> buildMenuTree(List<SysMenu> menus) {
        List<SysMenu> returnList = new ArrayList<>();
        List<Long> tempList = new ArrayList<>();
        for (SysMenu dept : menus) {
            tempList.add(dept.getMenuId());
        }
        for (Iterator<SysMenu> iterator = menus.iterator(); iterator.hasNext(); ) {
            SysMenu menu = iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(menu.getParentId())) {
                recursionFn(menus, menu);
                returnList.add(menu);
            }
        }
        if (returnList.isEmpty()) {
            returnList = menus;
        }
        return returnList;
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param menus 菜单列表
     * @return 下拉树结构列表
     */
    @Override
    public List<TreeSelect> buildMenuTreeSelect(List<SysMenu> menus) {
        List<SysMenu> menuTrees = buildMenuTree(menus);
        return menuTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 根据菜单ID查询信息
     *
     * @param menuId 菜单ID
     * @return 菜单信息
     */
    @Override
    public SysMenu selectMenuById(Long menuId) {
        return menuMapper.selectMenuById(menuId);
    }

    /**
     * 是否存在菜单子节点
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    @Override
    public boolean hasChildByMenuId(Long menuId) {
        int result = menuMapper.hasChildByMenuId(menuId);
        return result > 0 ? true : false;
    }

    /**
     * 查询菜单使用数量
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    @Override
    public boolean checkMenuExistRole(Long menuId) {
        int result = roleMenuMapper.checkMenuExistRole(menuId,tokenService.getTenantKey());
        return result > 0 ? true : false;
    }

    /**
     * 新增保存菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    public int insertMenu(SysMenu menu) {
        menu.setTenantKey(tokenService.getTenantKey());
        return menuMapper.insertMenu(menu);
    }

    /**
     * 修改保存菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    public int updateMenu(SysMenu menu) {
        return menuMapper.updateMenu(menu);
    }

    /**
     * 删除菜单管理信息
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    @Override
    public int deleteMenuById(Long menuId) {
        return menuMapper.deleteMenuById(menuId);
    }

    /**
     * 级联删除菜单管理信息
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    @Override
    @Transactional
    public int cascadeDeleteMenuById(Long menuId) {
        List<SysMenu> sysMenus = menuMapper.selectMenuList(new SysMenu(),tokenService.getTenantKeyListForMenu());
        List<Long> menuIds = getChildMenuIds(sysMenus, menuId);
        menuIds.add(menuId);
        roleMenuMapper.deleteRoleMenuByMenuIds(menuIds,tokenService.getTenantKey());
        return menuMapper.realDeleteMenuByIds(menuIds);
    }

    /**
     * 校验菜单名称是否唯一
     *
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    public String checkMenuNameUnique(SysMenu menu) {
        Long menuId = StringUtils.isNull(menu.getMenuId()) ? -1L : menu.getMenuId();
        SysMenu info = menuMapper.checkMenuNameUnique(menu.getMenuName(), menu.getParentId(), tokenService.getTenantKeyListForMenu());
        if (StringUtils.isNotNull(info) && info.getMenuId().longValue() != menuId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验路由是否唯一
     *
     * @return 结果
     */
    @Override
    public String checkRouterPathUnique(SysMenu menu) {
        if (StringUtils.isBlank(menu.getRouterPath())) {
            return UserConstants.UNIQUE;
        }
        Long menuId = StringUtils.isNull(menu.getMenuId()) ? -1L : menu.getMenuId();
        SysMenu info = menuMapper.checkRouterPathUnique(menu.getRouterPath(), tokenService.getTenantKeyListForMenu());
        if (StringUtils.isNotNull(info) && info.getMenuId().longValue() != menuId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 获取路由名称
     *
     * @param menu 菜单信息
     * @return 路由名称
     */
    public String getRouteName(SysMenu menu) {
        String routerName = StringUtils.capitalize(menu.getRouterPath());
        // 非外链并且是一级目录（类型为目录）
        if (isMenuFrame(menu)) {
            routerName = StringUtils.EMPTY;
        }
        return routerName;
    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(SysMenu menu) {
        String routerPath = menu.getRouterPath();
        // 非外链并且是一级目录（类型为目录）
        if (0 == menu.getParentId().intValue() && UserConstants.TYPE_DIR.equals(menu.getMenuType())
                && UserConstants.NO_FRAME.equals(menu.getIsExternalLink())) {
            routerPath = "/" + menu.getRouterPath();
        }
        // 非外链并且是一级目录（类型为菜单）
        else if (isMenuFrame(menu)) {
            routerPath = "/";
        }
        return routerPath;
    }

    /**
     * 获取组件信息
     *
     * @param menu 菜单信息
     * @return 组件信息
     */
    public String getComponent(SysMenu menu) {
        String component = UserConstants.LAYOUT;
        if (StringUtils.isNotEmpty(menu.getComponentPath()) && !isMenuFrame(menu)) {
            component = menu.getComponentPath();
        } else if (StringUtils.isEmpty(menu.getComponentPath()) && isParentView(menu)) {
            component = UserConstants.PARENT_VIEW;
        }
        return component;
    }

    /**
     * 是否为菜单内部跳转
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isMenuFrame(SysMenu menu) {
        return menu.getParentId().intValue() == 0 && UserConstants.TYPE_MENU.equals(menu.getMenuType())
                && menu.getIsExternalLink().equals(UserConstants.NO_FRAME);
    }

    /**
     * 是否为parent_view组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isParentView(SysMenu menu) {
        return menu.getParentId().intValue() != 0 && UserConstants.TYPE_DIR.equals(menu.getMenuType());
    }

    /**
     * 根据父节点的ID获取所有子节点
     *
     * @param list     分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
    public List<SysMenu> getChildPerms(List<SysMenu> list, Long parentId) {
        List<SysMenu> returnList = new ArrayList<>();
        for (Iterator<SysMenu> iterator = list.iterator(); iterator.hasNext(); ) {
            SysMenu t = iterator.next();
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (t.getParentId() != null && parentId != null && (t.getParentId().longValue() == parentId.longValue())) {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    public List<Long> getChildMenuIds(List<SysMenu> menulist, Long parentId) {
        List<SysMenu> childMenulist = menulist.parallelStream().filter(menu -> menu.getParentId() == null ? false : menu.getParentId().equals(parentId)).collect(Collectors.toList());
        List<Long> menuIdList = childMenulist.parallelStream().map(menu -> menu.getMenuId()).collect(Collectors.toList());
        List<Long> resultList = new ArrayList<>();
        resultList.addAll(menuIdList);
        for (Long menuId : menuIdList) {
            recursionMenuFn(menulist, resultList, menuId);
        }
        return resultList;
    }

    private void recursionMenuFn(List<SysMenu> menulist, List<Long> resultList, Long parentId) {
        List<SysMenu> childMenulist = menulist.parallelStream().filter(menu -> menu.getParentId() == null ? false : menu.getParentId().equals(parentId)).collect(Collectors.toList());
        List<Long> menuIdList = childMenulist.parallelStream().map(menu -> menu.getMenuId()).collect(Collectors.toList());
        resultList.addAll(menuIdList);
        for (Long menuId : menuIdList) {
            recursionMenuFn(menulist, resultList, menuId);
        }
    }

    /**
     * 递归列表
     *
     * @param list
     * @param t
     */
    private void recursionFn(List<SysMenu> list, SysMenu t) {
        // 得到子节点列表
        List<SysMenu> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysMenu tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysMenu> getChildList(List<SysMenu> list, SysMenu t) {
        List<SysMenu> tlist = new ArrayList<>();
        Iterator<SysMenu> it = list.iterator();
        while (it.hasNext()) {
            SysMenu n = it.next();
            if (n.getParentId() != null && t.getMenuId() != null & (n.getParentId().longValue() == t.getMenuId().longValue())) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysMenu> list, SysMenu t) {
        return getChildList(list, t).size() > 0 ? true : false;
    }
}
