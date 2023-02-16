package com.hhwy.demo.controller;

import com.hhwy.activiti.api.RemoteActivitiService;
import com.hhwy.activiti.api.domain.ActiBusinessInfo;
import com.hhwy.common.core.domain.R;
import com.hhwy.common.core.utils.UUIDUtils;
import com.hhwy.common.core.utils.file.FileUtils;
import com.hhwy.common.core.web.controller.BaseController;
import com.hhwy.common.core.web.domain.AjaxResult;
import com.hhwy.common.datasource.utils.DataSourceUtils;
import com.hhwy.common.security.service.TokenService;
import com.hhwy.demo.mapper.DemoMapper;
import com.hhwy.demo.service.IDemoService;
import com.hhwy.demo.test.service.TestServiceImpl;
import com.hhwy.file.api.domain.SysFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.Map;

/**
 * 参数配置 信息操作处理
 *
 * @author hhwy
 */
@RestController
@RequestMapping("/test")
public class DemoController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(DemoController.class);

    public DemoController() {
    }

    @Resource
    private IDemoService demoService;

    @Autowired
    private DemoMapper demoMapper;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RemoteActivitiService remoteActivitiService;

    private String template;
    /**
     * 获取参数配置列表
     */
    @GetMapping("/one")
    public AjaxResult one(String code) throws Exception{
        String resource = "/sql/dbInit.sql";
        InputStream resourceAsStream = DemoController.class.getResourceAsStream(resource);
        String sql = FileUtils.readFile(resourceAsStream);
        demoMapper.cs(sql.replace('\r', ' ').replace('\n', ' '));
        return AjaxResult.success("操作成功",tokenService.getLoginUser().getUsername());
    }

    @GetMapping("/two/{tenantKey}/{tableName}/{businessId}")
    public AjaxResult two(@PathVariable String tenantKey, @PathVariable String tableName, @PathVariable String businessId) {
        ActiBusinessInfo data = remoteActivitiService.getInfo(tenantKey, tableName, businessId).getData();
        return AjaxResult.success(data);
    }

    /**
     * 流程任务监听器接口
     */
    @PostMapping("taskFilter")
    public AjaxResult taskFilter(@RequestBody Map<String,Object> delegateTask) {
       /* int i = 0;
        int j = 1;
        System.err.println(j/i);*/
        return AjaxResult.success();
    }
    @PostMapping("executionFilter")
    public AjaxResult executionFilter(@RequestBody Map<String,Object> delegateExecution) {
        return AjaxResult.success();
    }

    @PostMapping("template")
    public AjaxResult saveTemplate(@RequestBody Map<String,String> content) throws Exception{
        String templateParam = content.get("template");
        template = "<template><div>"+templateParam.substring(10, templateParam.length() - 11)+"</div></template>";//template包裹div
        String fileName = UUIDUtils.getID()+".vue";
        String path = "D:\\ft\\uploadPath\\vue\\"+ fileName;
        File file = new File(path);
        if (!file.exists()) {
            file.createNewFile();
        }
        //文件的续写
        FileWriter fw = new FileWriter(file,true);
        fw.write(template);
        fw.close();

        SysFile sysFile = new SysFile();
        sysFile.setName(fileName);
        sysFile.setUrl("http://192.168.1.66:9300/statics/vue/"+fileName);

        return AjaxResult.success(sysFile);
    }

    @GetMapping("test")
    public Boolean test(){
        return demoService.test();
    }
}
