package com.ablue.template.controller;

import com.ablue.template.model.request.BaseTemplateRequest;
import com.ablue.template.service.TemplateService;
import com.ablue.template.model.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 模板处理控制器
 * 提供模板处理、下载、预览和Excel解析功能
 */
@RestController
@RequestMapping("/api")
@Tag(name = "模板处理接口", description = "提供模板的处理、下载、预览和Excel解析功能")
public class TemplateController {

    @Autowired
    private TemplateService templateService;

    /**
     * 处理模板
     */
    @Operation(summary = "处理模板", description = "根据请求类型和数据处理模板")
    @PostMapping("/process")
    public BaseResponse<Map<String, String>> processTemplate(
            @Parameter(description = "模板请求") @RequestBody BaseTemplateRequest<Object> request) throws Exception {
        Map<String, String> result = templateService.processTemplate(request.getType(), request.getData());
        return BaseResponse.success(result);
    }

    /**
     * 下载模板
     */
    @Operation(summary = "下载模板", description = "下载处理后的模板文件")
    @PostMapping("/download")
    public ResponseEntity<byte[]> downloadTemplate(
            @Parameter(description = "模板请求") @RequestBody BaseTemplateRequest<Object> request) throws Exception {
        byte[] content = templateService.downloadTemplate(request.getType(), request.getData());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", request.getType() + "-template.zip");
        return ResponseEntity.ok()
                .headers(headers)
                .body(content);
    }

    /**
     * 解析Excel
     */
    @Operation(summary = "解析Excel", description = "解析上传的Excel文件")
    @PostMapping("/parse/{type}")
    public BaseResponse<Object> parseExcel(
            @Parameter(description = "模板类型") @PathVariable String type,
            @Parameter(description = "Excel文件") @RequestParam("file") MultipartFile file) throws Exception {
        Object data = templateService.parseExcel(type, file.getBytes());
        return BaseResponse.success(data);
    }
}
