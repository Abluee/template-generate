package com.ablue.template.processor;

import com.ablue.template.core.TemplateProcessor;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.data.ReadCellData;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 抽象模板处理器
 * 提供通用的模板处理逻辑
 * @param <T> 模板数据类型
 */
public abstract class AbstractTemplateProcessor<T> implements TemplateProcessor<T> {

    @Autowired
    protected Configuration freemarkerConfig;

    @Autowired
    protected ResourceLoader resourceLoader;

    /**
     * 获取模板类型
     * @return 模板类型
     */
    @Override
    public abstract String getType();

    /**
     * 处理Excel原始数据
     * @param rawData Excel原始数据
     * @return 处理后的模板数据对象
     */
    protected abstract T processRawData(List<Map<Integer, String>> rawData);

    /**
     * 解析Excel文件
     * @param excelBytes Excel文件字节数组
     * @return 模板数据对象
     */
    @Override
    public T parseExcel(byte[] excelBytes) throws Exception {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(excelBytes);
        ExcelDataListener listener = new ExcelDataListener();
        
        EasyExcel.read(inputStream)
                .sheet()
                .registerReadListener(listener)
                .doRead();

        List<Map<Integer, String>> rawData = listener.getRawData();
        return processRawData(rawData);
    }

    /**
     * Excel数据读取监听器
     */
    private static class ExcelDataListener extends AnalysisEventListener<Map<Integer, String>> {
        private final List<Map<Integer, String>> rawDataList = new ArrayList<>();

        @Override
        public void invoke(Map<Integer, String> rowData, AnalysisContext context) {
            if (rowData != null && !rowData.isEmpty()) {
                rawDataList.add(rowData);
            }
        }

        @Override
        public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
            // 如果需要处理表头，可以在这里实现
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
            // 解析完成后的操作
        }

        public List<Map<Integer, String>> getRawData() {
            return rawDataList;
        }
    }

    /**
     * 处理模板
     * @param data 模板数据
     * @return 处理结果，key为文件路径，value为处理后的内容
     */
    @Override
    public Map<String, String> process(T data) throws Exception {
        Map<String, String> result = new HashMap<>();
        ResourcePatternResolver resolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        Resource[] resources = resolver.getResources("classpath:templates/" + getType() + "/**/*.ftl");

        for (Resource resource : resources) {
            String templatePath = getTemplatePath(resource);
            String content = processTemplate(templatePath, data);
            String outputPath = templatePath.replace(".ftl", "");
            result.put(outputPath, content);
        }

        return result;
    }

    /**
     * 下载模板
     * @param data 模板数据
     * @return 处理后的文件字节数组
     */
    @Override
    public byte[] download(T data) throws Exception {
        Map<String, String> processedFiles = process(data);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            for (Map.Entry<String, String> entry : processedFiles.entrySet()) {
                ZipEntry zipEntry = new ZipEntry(entry.getKey());
                zos.putNextEntry(zipEntry);
                zos.write(entry.getValue().getBytes());
                zos.closeEntry();
            }
        }
        return baos.toByteArray();
    }

    /**
     * 预览模板
     * @param data 模板数据
     * @return 预览内容
     */
    @Override
    public String preview(T data) throws Exception {
        Map<String, String> processedFiles = process(data);
        StringBuilder preview = new StringBuilder();
        for (Map.Entry<String, String> entry : processedFiles.entrySet()) {
            preview.append("=== ").append(entry.getKey()).append(" ===\n");
            preview.append(entry.getValue()).append("\n\n");
        }
        return preview.toString();
    }

    /**
     * 获取模板路径
     * @param resource 资源
     * @return 模板路径
     */
    protected String getTemplatePath(Resource resource) throws IOException {
        String fullPath = resource.getURI().toString();
        return fullPath.substring(fullPath.indexOf(getType() + "/") + getType().length() + 1);
    }

    /**
     * 处理单个模板
     * @param templatePath 模板路径
     * @param data 模板数据
     * @return 处理后的内容
     */
    protected String processTemplate(String templatePath, T data) throws Exception {
        Template template = freemarkerConfig.getTemplate(getType() + "/" + templatePath);
        StringWriter writer = new StringWriter();
        template.process(data, writer);
        return writer.toString();
    }
}
