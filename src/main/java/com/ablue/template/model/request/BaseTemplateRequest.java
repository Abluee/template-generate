package com.ablue.template.model.request;

import lombok.Data;

/**
 * 基础模板请求
 * @param <T> 模板数据类型
 */
@Data
public class BaseTemplateRequest<T> {
    /**
     * 模板类型
     */
    private String type;

    /**
     * 模板数据
     */
    private T data;
}
