package com.nbcb.template.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 通用响应类
 * @param <T> 响应数据类型
 */
@Data
@Accessors(chain = true)
@Schema(description = "通用响应类")
public class BaseResponse<T> {
    
    @Schema(description = "响应码", required = true, example = "200")
    private Integer code;
    
    @Schema(description = "响应消息", required = true, example = "success")
    private String message;
    
    @Schema(description = "响应数据")
    private T data;
    
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<T>()
                .setCode(200)
                .setMessage("success")
                .setData(data);
    }
    
    public static <T> BaseResponse<T> error(String message) {
        return new BaseResponse<T>()
                .setCode(500)
                .setMessage(message);
    }
}
