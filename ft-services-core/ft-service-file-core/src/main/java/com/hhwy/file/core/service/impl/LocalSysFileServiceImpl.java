package com.hhwy.file.core.service.impl;

import com.hhwy.common.core.utils.StringUtils;
import com.hhwy.common.core.utils.YamlUtil;
import com.hhwy.common.core.utils.file.FileUploadUtils;
import com.hhwy.file.api.domain.FileParam;
import com.hhwy.file.api.domain.SysFile;
import com.hhwy.file.core.service.ISysFileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.util.List;

/**
 * 本地文件存储
 *
 * @author hhwy
 */
@Service
public class LocalSysFileServiceImpl implements ISysFileService {
    /**
     * 资源映射路径 前缀
     */
    @Value("${file.prefix}")
    public String localFilePrefix;

    /**
     * 域名或本机访问地址
     */
    @Value("${file.domain:}")
    public String domain;

    /**
     * 上传文件存储在本地的根路径
     */
    @Value("${file.path}")
    private String localFilePath;

    @Override
    public SysFile uploadFile(MultipartFile file, FileParam fileParam) throws Exception {
        return null;
    }

    @Override
    public List<SysFile> uploadFiles(MultipartFile[] files, FileParam fileParam) {
        return null;
    }

    /**
     * 本地文件上传接口
     *
     * @param file 上传的文件
     * @return 访问地址
     * @throws Exception
     */
    @Override
    public String uploadFile(MultipartFile file) throws Exception {
        String name = FileUploadUtils.upload(localFilePath, file);
        String url = domain + localFilePrefix + name;
        return url;
    }

    @PostConstruct
    public void initDomain() throws Exception{
        if (StringUtils.isEmpty(domain)) {
            String ip = InetAddress.getLocalHost().getHostAddress();
            String port = StringUtils.valueOf(YamlUtil.getProperty("server.port"));
            String path = StringUtils.valueOf(YamlUtil.getProperty("server.servlet.context-path"));
            domain = "http://" + ip + ":" + port + path;
        }
    }
}
