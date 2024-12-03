package com.ablue.template.core;

import java.util.Map;

/**
 * 模板处理器接口
 * 定义模板处理和Excel解析的通用方法
 * @param <T> 模板数据类型
 */
public interface TemplateProcessor<T> {

    /**
     * 获取支持的模板类型
     * @return 模板类型标识符
     */
    String getType();

    /**
     * 处理模板
     * @param data 模板数据
     * @return 处理结果，key为文件路径，value为文件内容
     * @throws Exception 处理过程中的异常
     */
    Map<String, String> process(T data) throws Exception;

    /**
     * 下载模板
     * @param data 模板数据
     * @return 打包后的文件字节数组
     * @throws Exception 处理过程中的异常
     */
    byte[] download(T data) throws Exception;

    /**
     * 预览模板
     * @param data 模板数据
     * @return 预览内容
     * @throws Exception 处理过程中的异常
     */
    String preview(T data) throws Exception;

    /**
     * 解析Excel文件
     * @param excelBytes Excel文件字节数组
     * @return 解析后的数据对象
     * @throws Exception 解析过程中的异常
     */
    T parseExcel(byte[] excelBytes) throws Exception;
}
