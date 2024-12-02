package com.nbcb.template.core;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.StringUtils;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractTemplateProcessor<T> implements TemplateProcessor<T> {
    
    @Autowired
    protected Configuration freemarkerConfig;
    
    protected String processTemplate(String templatePath, Object dataModel) throws Exception {
        Template template = freemarkerConfig.getTemplate(templatePath);
        StringWriter writer = new StringWriter();
        template.process(dataModel, writer);
        return writer.toString();
    }
    
    protected Map<String, String> processTemplateFiles(String baseDir, T data) throws Exception {
        Map<String, String> results = new HashMap<>();
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        
        // 递归查找所有ftl文件
        Resource[] resources = resolver.getResources("classpath:templates/" + baseDir + "/**/*.ftl");
        
        for (Resource resource : resources) {
            String path = resource.getURL().getPath();
            // 提取相对路径
            String relativePath = path.substring(path.indexOf(baseDir) + baseDir.length() + 1);
            // 移除.ftl后缀
            String outputPath = relativePath.substring(0, relativePath.length() - 4);
            // 处理模板
            String content = processTemplate(baseDir + "/" + relativePath, data);
            results.put(outputPath, content);
        }
        
        return results;
    }
}
