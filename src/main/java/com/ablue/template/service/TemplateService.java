package com.ablue.template.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ablue.template.core.TemplateProcessor;
import com.ablue.template.model.CardData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 模板服务
 * 处理模板相关的业务逻辑，包括模板处理和Excel解析
 */
@Service
public class TemplateService {

    @Autowired
    private List<TemplateProcessor<?>> processors;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 处理模板
     * @param type 模板类型
     * @param data 模板数据
     * @return 处理结果，key为文件路径，value为文件内容
     * @throws Exception 处理过程中的异常
     */
    @SuppressWarnings("unchecked")
    public Map<String, String> processTemplate(String type, Object data) throws Exception {
        TemplateProcessor processor = getProcessor(type);
        Object convertedData = convertData(type, data);
        return processor.process(convertedData);
    }

    /**
     * 下载模板
     * @param type 模板类型
     * @param data 模板数据
     * @return 打包后的文件字节数组
     * @throws Exception 处理过程中的异常
     */
    @SuppressWarnings("unchecked")
    public byte[] downloadTemplate(String type, Object data) throws Exception {
        TemplateProcessor processor = getProcessor(type);
        Object convertedData = convertData(type, data);
        return processor.download(convertedData);
    }

    /**
     * 预览模板
     * @param type 模板类型
     * @param data 模板数据
     * @return 预览内容
     * @throws Exception 处理过程中的异常
     */
    @SuppressWarnings("unchecked")
    public String previewTemplate(String type, Object data) throws Exception {
        TemplateProcessor processor = getProcessor(type);
        Object convertedData = convertData(type, data);
        return processor.preview(convertedData);
    }

    /**
     * 解析Excel文件
     * @param type 模板类型
     * @param excelBytes Excel文件字节数组
     * @return 解析后的数据对象
     * @throws Exception 解析过程中的异常
     */
    @SuppressWarnings("unchecked")
    public Object parseExcel(String type, byte[] excelBytes) throws Exception {
        TemplateProcessor processor = getProcessor(type);
        return processor.parseExcel(excelBytes);
    }

    /**
     * 获取模板处理器
     * @param type 模板类型
     * @return 对应的处理器
     * @throws IllegalArgumentException 如果找不到对应的处理器
     */
    private TemplateProcessor<?> getProcessor(String type) {
        return processors.stream()
                .filter(p -> p.getType().equals(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown template type: " + type));
    }

    /**
     * 转换数据为正确的类型
     * @param type 模板类型
     * @param data 原始数据
     * @return 转换后的数据
     */
    private Object convertData(String type, Object data) {
        if (data == null) {
            return null;
        }

        // 如果已经是正确的类型，直接返回
        switch (type) {
            case "card":
                if (data instanceof CardData) {
                    return data;
                }
                return objectMapper.convertValue(data, CardData.class);
            default:
                throw new IllegalArgumentException("Unknown template type: " + type);
        }
    }
}
