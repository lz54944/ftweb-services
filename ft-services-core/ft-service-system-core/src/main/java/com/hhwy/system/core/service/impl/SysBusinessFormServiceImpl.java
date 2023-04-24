package com.hhwy.system.core.service.impl;

import com.hhwy.common.core.text.Convert;
import com.hhwy.common.core.utils.StringUtils;
import com.hhwy.common.core.web.domain.AjaxResult;
import com.hhwy.system.core.domain.SysBusinessForm;
import com.hhwy.system.core.domain.SysBusinessFormItem;
import com.hhwy.system.core.domain.vo.TableAll;
import com.hhwy.system.core.mapper.SysBusinessFormItemMapper;
import com.hhwy.system.core.mapper.SysBusinessFormMapper;
import com.hhwy.system.core.service.ISysBusinessFormService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * 业务流程单Service业务层处理
 * 
 * @author hanchuanchuan
 * @date 2021-09-16
 */
@Service
public class SysBusinessFormServiceImpl implements ISysBusinessFormService {
    @Resource
    private SysBusinessFormMapper sysBusinessFormMapper;

    @Resource
    private SysBusinessFormItemMapper sysBusinessFormItemMapper;

    /**
     * 查询业务流程单
     * 
     * @param id 业务流程单ID
     * @return 业务流程单
     */
    @Override
    public SysBusinessForm selectSysBusinessFormById(Long id) {
        return sysBusinessFormMapper.selectSysBusinessFormById(id);
    }

    /**
     * 查询业务流程单列表
     * 
     * @param sysBusinessForm 业务流程单
     * @return 业务流程单
     */
    @Override
    public List<SysBusinessForm> selectSysBusinessFormList(SysBusinessForm sysBusinessForm) {
        return sysBusinessFormMapper.selectSysBusinessFormList(sysBusinessForm);
    }

    /**
     * 新增业务流程单
     * 
     * @param sysBusinessForm 业务流程单
     * @return 结果
     */
    @Override
    @Transactional
    public AjaxResult insertSysBusinessForm(SysBusinessForm sysBusinessForm) {
        AjaxResult res = null;
        //添加uuid
        String flowFormId = UUID.randomUUID().toString().replace("-","");
        sysBusinessForm.setFlowFormId(flowFormId);
//        AddBaseInfoUtil<BaseEntity> baseEntityAddBaseInfoUtil = new AddBaseInfoUtil<>();
//        baseEntityAddBaseInfoUtil.add(sysBusinessForm);
        //判断子表是否有值
        if (sysBusinessForm.getSysBusinessFormItemList() != null){
            List<SysBusinessFormItem> sysBusinessFormItemList = sysBusinessForm.getSysBusinessFormItemList();
            Iterator<SysBusinessFormItem> iterator = sysBusinessFormItemList.iterator();
            while(iterator.hasNext()){
                SysBusinessFormItem l = iterator.next();
                String str = checkDbParam(l,sysBusinessFormItemList);
                if(!StringUtils.isBlank(str)){
                    return AjaxResult.error("[新增行]错误信息",str);
                }else{
                    sysBusinessFormItemMapper.insertSysBusinessFormItem(l);
                }
            }

        }
        String querySql = getQuerySql(sysBusinessForm);
        //生成sql语句
        sysBusinessForm.setQuerySql(querySql);
        int i = sysBusinessFormMapper.insertSysBusinessForm(sysBusinessForm);
        if(1 == i){
            res = AjaxResult.success(sysBusinessForm);
        }else{
            res = AjaxResult.success("添加失败!");
        }
        return res;
    }

    private String checkDbParam(SysBusinessFormItem param, List<SysBusinessFormItem> sysBusinessFormItemList) {
        //表名称
        String colName = param.getColName();
        //字段编码
        String colCode = param.getColCode();
        //字段类型
        String colType = param.getColType();
        //条件字段别名
        String dbWhereParam = param.getDbWhereParam();
        String checkStr = "";
//        if("varchar".equals(colType) || "char".equals(colType)){
//            if(dbWhereParam.startsWith("var")){
//                checkStr = "["+colCode+"]当前字段类型必须以[var]字符为前缀";
//            }else{
//                int checkNum = sysBusinessFormItemMapper.checkDbWhereParam(colName, dbWhereParam);
//                if(1 == checkNum){
//                    checkStr ="["+colCode+"]条件字段别名已存在,请检查并修改!";
//                }
//            }
//        }else if("bigint".equals(colType) || "int".equals(colType) || "decimal".equals(colType) ){
//            if(dbWhereParam.startsWith("num")){
//                checkStr = "["+colCode+"]当前字段类型必须以[num]字符为前缀";
//            }else{
//                int checkNum = sysBusinessFormItemMapper.checkDbWhereParam(colName,dbWhereParam);
//                if(1 == checkNum){
//                    checkStr = "["+colCode+"]条件字段别名已存在,请检查并修改!";
//                }
//            }
//        }else{
//            checkStr = "目前只支持字段类型为[varchar,char,bigint,int,decimal]";
//        }
        Iterator<SysBusinessFormItem> iterator = sysBusinessFormItemList.iterator();
        while (iterator.hasNext()){
            SysBusinessFormItem next = iterator.next();
            Long id = next.getId();
            Long id1 = param.getId();
            if(!id.equals(id1)){
                if(!StringUtils.isBlank(dbWhereParam) && !StringUtils.isBlank(next.getDbWhereParam())){
                    if(dbWhereParam.equals(next.getDbWhereParam())){
                        checkStr = "["+colCode+"]条件字段别名["+dbWhereParam+"]已存在,请检查并修改!";
                    }
                }
            }
        }

        return checkStr;
    }

    /**
     * 修改业务流程单(作废代码)
     * 
     * @param sysBusinessForm 业务流程单
     * @return 结果
     */
    public static final char UNDERLINE = '_';
    @Override
    public int updateSysBusinessForm(SysBusinessForm sysBusinessForm) {
        //判断集合的值是否改变
       if (sysBusinessForm.getSysBusinessFormItemList() != null){
           List<SysBusinessFormItem> arrayList = new ArrayList<>();
           List<SysBusinessFormItem> sysBusinessFormItemList = sysBusinessForm.getSysBusinessFormItemList();
           sysBusinessFormItemList.forEach(l->{
               if (StringUtils.isNotBlank(l.getFlowFormId()) || l.getFlowFormId() != null) {
                   sysBusinessFormItemMapper.updateSysBusinessFormItem(l);
               }
               l.setFlowFormId(sysBusinessForm.getFlowFormId());
               sysBusinessFormItemMapper.insertSysBusinessFormItem(l);
           });
       }
       //生成sql查询
        if (sysBusinessForm.getSqlParams() != null){
            String querySql=null;
            String sqlParams = sysBusinessForm.getSqlParams();
            //转化成数据库表的形式
            int length = sqlParams.length();
            StringBuilder stringBuilder = new StringBuilder(length);
            for (int i = 0; i < length; i++) {
                char c = sqlParams.charAt(i);
                if (Character.isUpperCase(c)){
                    stringBuilder.append(UNDERLINE);
                    stringBuilder.append(Character.toLowerCase(c));
                }else {
                    stringBuilder.append(c);
                }
            }
            String tableDbCode = sysBusinessForm.getTableDbCode();
            if (tableDbCode.equals("oms_md_project_info")){
                /**
                 * = =不知道怎么想的写了这么垃圾的代码,代码已经优化到下面了。
                 */
            querySql = "select id,project_info_id,area_code,area_name,continent,regional_head,project_code,project_name,owner_unit,business_type,industry_type,financing_method,project_type,source_of_funds,cooperate_mode,bidding_method,DATE_FORMAT(expect_bid_start_time, '%Y-%m-%d %H:%i:%s') expect_bid_start_time,DATE_FORMAT(expect_bid_time, '%Y-%m-%d %H:%i:%s') expect_bid_time,investors,brand_use_situation,cooperation_unit,state_leaders_sign,implement_mode,project_mode,is_do_business,is_government_ifr_project,owner_cooperation_file,is_year_investment_plan,expect_investment_amt,project_second_review,lead_unit,project_filing,third_party_name,is_project_database,report_to_embassies,investment_share_ratio,third_cooperation_project,third_cooperation_country,DATE_FORMAT(expect_contact_sign_time, '%Y-%m-%d %H:%i:%s')expect_contact_sign_time,invest_drive_contract_amt,unit_expect_invest_total_amt,one_belt_one_road,risk_country,editer_iphone_num,project_technical_overview,project_overview,project_tracking_progress,exist_problem,exist_risk_solutions,next_work,task_status,is_stop_tracking,create_user,organ,organ_id,task_user_name,task_user_id,task_node from "+ sysBusinessForm.getTableDbCode() + " where " +stringBuilder.toString() + " ="+"#{"+sqlParams+"} and del_flag = 0";
            }else if (tableDbCode.equals("oms_md_project_info_change" )){

             querySql = "select id,project_info_id,area_code,area_name,continent,regional_head,project_code,project_name,owner_unit,business_type,industry_type,financing_method,project_type,source_of_funds,cooperate_mode,bidding_method,DATE_FORMAT(expect_bid_start_time, '%Y-%m-%d %H:%i:%s') expect_bid_start_time,DATE_FORMAT(expect_bid_time, '%Y-%m-%d %H:%i:%s') expect_bid_time,investors,brand_use_situation,cooperation_unit,state_leaders_sign,implement_mode,project_mode,is_do_business,is_government_ifr_project,owner_cooperation_file,is_year_investment_plan,expect_investment_amt,project_second_review,lead_unit,project_filing,third_party_name,is_project_database,report_to_embassies,investment_share_ratio,third_cooperation_project,third_cooperation_country,DATE_FORMAT(expect_contact_sign_time, '%Y-%m-%d %H:%i:%s')expect_contact_sign_time,invest_drive_contract_amt,unit_expect_invest_total_amt,one_belt_one_road,risk_country,editer_iphone_num,project_technical_overview,project_overview,project_tracking_progress,exist_problem,exist_risk_solutions,next_work,task_status,is_stop_tracking,create_user,organ,organ_id,task_user_name,task_user_id,task_node from "+ sysBusinessForm.getTableDbCode() + " where " +stringBuilder.toString() + " ="+"#{"+sqlParams+"} and del_flag = 0";
            }else if (tableDbCode.equals("oms_md_project_label_review")){

              querySql = "select id,project_info_id,project_code,project_name,DATE_FORMAT(review_date, '%Y-%m-%d %H:%i:%s') review_date,DATE_FORMAT(apply_time, '%Y-%m-%d %H:%i:%s') apply_time,apply_organ_id,apply_organ,del_flag,file_group_id,remark,organ_id,organ,task_status,process_instance_id,task_id,task_node,task_user_id,task_user_name,DATE_FORMAT(create_time, '%Y-%m-%d %H:%i:%s') create_time,create_user,create_org_id,DATE_FORMAT(update_time, '%Y-%m-%d %H:%i:%s') update_time,update_by,update_org_id,update_user,project_leader,reason_applying,instead_send_status from "+ sysBusinessForm.getTableDbCode() + " where " +stringBuilder.toString() + " ="+"#{"+sqlParams+"} and del_flag = 0";
            }else {
                querySql= "select id from "+ sysBusinessForm.getTableDbCode() + " where " +stringBuilder.toString() + " ="+"#{"+sqlParams+"} and del_flag = 0";
            }
            sysBusinessForm.setQuerySql(querySql);
        }
        return  sysBusinessFormMapper.updateSysBusinessForm(sysBusinessForm);
    }

    /**
     * 代码优化
     * @param sysBusinessForm 业务流程单代码测试
     * @return
     */
    @Override
    @Transactional
    public AjaxResult updateSysBusinessFormEr(SysBusinessForm sysBusinessForm) {
        AjaxResult res = null;
        //判断集合的值是否改变
        if (sysBusinessForm.getSysBusinessFormItemList() != null){
            List<SysBusinessFormItem> sysBusinessFormItemList = sysBusinessForm.getSysBusinessFormItemList();
            Iterator<SysBusinessFormItem> iterator = sysBusinessFormItemList.iterator();
            while(iterator.hasNext()){
                SysBusinessFormItem l = iterator.next();
                String str = checkDbParam(l,sysBusinessFormItemList);
                if(!StringUtils.isBlank(str)){
                    return AjaxResult.error("[编辑行]错误信息",str);
                }else{
                    if (StringUtils.isNotBlank(l.getFlowFormId()) || l.getFlowFormId() != null) {
                        sysBusinessFormItemMapper.updateSysBusinessFormItem(l);
                    }else {
                        l.setFlowFormId(sysBusinessForm.getFlowFormId());
                        sysBusinessFormItemMapper.insertSysBusinessFormItem(l);
                    }
                }
            }
        }
        String flowFormId = sysBusinessForm.getFlowFormId();
        SysBusinessForm sysBusinessFormEr = sysBusinessFormMapper.selectOmsSysFlowByFlowForData(flowFormId);
        if (!sysBusinessForm.getQuerySql().equals(sysBusinessFormEr.getQuerySql())){
            String querySql = getQuerySql(sysBusinessForm);
            sysBusinessForm.setQuerySql(querySql);
        }
        //如果sql语句为空的话，自动生成sql语句
        if (StringUtils.isEmpty(sysBusinessForm.getQuerySql())){
            String querySql = getQuerySql(sysBusinessForm);
            //生成sql语句
            sysBusinessForm.setQuerySql(querySql);
        }
        int i = sysBusinessFormMapper.updateSysBusinessForm(sysBusinessForm);
        if (1==i){
            res = AjaxResult.success("编辑成功!");
        }else{
            res = AjaxResult.success("编辑失败!");
        }
        return res;
    }

    /**
     @MethodName: 生成sql语句
     @Author: Mryan
     @Date: 2021/12/10:15:37
     */
    public String getQuerySql(SysBusinessForm sysBusinessForm){
        String querySql = null;
        if (sysBusinessForm.getSqlParams() != null){
            String table = null;
            if ("01".equals(sysBusinessForm.getModule())) {
                table = "oms_market";
            } else if ("02".equals(sysBusinessForm.getModule())) {
                table = "oms_marketDev";
            } else if ("03".equals(sysBusinessForm.getModule())) {
                table = "oms_report";
            }
            List<String> stringArrayList = new ArrayList<>();
            String sqlParams = sysBusinessForm.getSqlParams();
            //转化成数据库表的形式
            int length = sqlParams.length();
            StringBuilder stringBuilder = new StringBuilder(length);
            for (int i = 0; i < length; i++) {
                char c = sqlParams.charAt(i);
                if (Character.isUpperCase(c)){
                    stringBuilder.append(UNDERLINE);
                    stringBuilder.append(Character.toLowerCase(c));
                }else {
                    stringBuilder.append(c);
                }
            }
            //获取数据库表
            String tableDbCode = sysBusinessForm.getTableDbCode();
            List<TableAll> tableAll = sysBusinessFormItemMapper.selectOmsSysTable(tableDbCode, table);
            tableAll.forEach(l->{
                String columnName = l.getColumnName();
                if (l.getDataType().equals("datetime")){
                    columnName = "DATE_FORMAT("+columnName+", '%Y-%m-%d') "+columnName;
                }
                if (l.getDataType().equals("decimal") || l.getDataType().equals("bigint")){
                    columnName = "CAST(ROUND ("+columnName+",2) AS VARCHAR(100)) "+columnName;
                }
                stringArrayList.add(columnName);
            });
            //生成字段格式
            String paramToString = StringUtils.join(stringArrayList.toArray(), ",");
            //生成sql语句
            querySql = "select "+paramToString+" from "+ sysBusinessForm.getTableDbCode() + " where " +stringBuilder.toString() + " ="+"#{"+sqlParams+"} and del_flag = 0";
        }
        return querySql;
    }

    /**
     * 根据功能模块查询数据库表
     * @param module
     * @return
     */
    @Override
    public List<SysBusinessForm> selectDataBaseGetTable(String module) {
        return sysBusinessFormMapper.selectOmsSysFlowGetTable(module);
    }

    /**
     * 查询pc端
     * @return
     */
    @Override
    public List<SysBusinessForm> selectConfigurationList(String module, String formCode, String tableName) {
        return sysBusinessFormMapper.selectConfiggurationList(module,formCode,tableName);
    }

    /**
     * 删除业务流程单对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSysBusinessFormByIds(String ids) {

        return sysBusinessFormMapper.deleteSysBusinessFormByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除业务流程单信息
     * 
     * @param id 业务流程单ID
     * @return 结果
     */
    @Override
    public int deleteSysBusinessFormById(Long id) {
        return sysBusinessFormMapper.deleteSysBusinessFormById(id);
    }

}
