package com.hhwy.file.core.controller;

import com.hhwy.common.core.domain.R;
import com.hhwy.common.core.utils.StringUtils;
import com.hhwy.common.core.utils.file.FileUtils;
import com.hhwy.common.core.web.domain.AjaxResult;
import com.hhwy.file.api.domain.FileParam;
import com.hhwy.file.core.service.ISysFileService;
import com.hhwy.file.api.domain.SysFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 文件请求处理
 *
 * @author hhwy
 */
@RestController
@RequestMapping("/file")
public class SysFileController {
    private static final Logger log = LoggerFactory.getLogger(SysFileController.class);

    @Autowired
    private ISysFileService sysFileService;

    /**
     * 文件上传请求（带业务）
     */
    @PostMapping("uploadFile")
    public AjaxResult upload(@RequestPart(value = "file") MultipartFile file, @Validated FileParam fileParam) {
        try {
            // 上传并返回访问地址
            SysFile sysFile = sysFileService.uploadFile(file, fileParam);
            return AjaxResult.success(sysFile);
        } catch (Exception e) {
            log.error("上传文件失败", e);
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 文件上传请求（带业务）
     */
    @PostMapping("uploadFiles")
    public AjaxResult uploadFiles(@RequestPart(value = "files") MultipartFile[] files, @Validated FileParam fileParam) {
        try {
            // 上传并返回访问地址
            List<SysFile> sysFiles = sysFileService.uploadFiles(files, fileParam);
            return AjaxResult.success(sysFiles);
        } catch (Exception e) {
            log.error("上传文件失败", e);
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 文件上传请求
     */
    @PostMapping("upload")
    public R<SysFile> upload(MultipartFile file) {
        try {
            // 上传并返回访问地址
            String url = sysFileService.uploadFile(file);
            SysFile sysFile = new SysFile();
            sysFile.setName(FileUtils.getName(url));
            sysFile.setUrl(url);
            return R.ok(sysFile);
        } catch (Exception e) {
            log.error("上传文件失败", e);
            return R.fail(e.getMessage());
        }
    }

    /**
     * 通用下载请求
     *
     * @param fileName 文件名称
     * @param exportFileName 下载的文件名
     */
    @GetMapping("download")
    public void fileDownload(String fileName, String exportFileName, HttpServletResponse response, HttpServletRequest request) {
        try {
            String filePath = "E:\\test\\temp\\" + fileName;

            if(StringUtils.isEmpty(exportFileName)){
                exportFileName = System.currentTimeMillis() +"_"+ fileName.substring(fileName.indexOf("_") + 1);
            }
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition", "attachment;fileName=" + FileUtils.setFileDownloadHeader(request, exportFileName));
            FileUtils.writeBytes(filePath, response.getOutputStream());
            FileUtils.deleteFile(filePath);
        } catch (Exception e) {
            log.error("下载文件失败", e);
        }
    }

}