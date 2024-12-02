package com.nbcb.template.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 卡片模板数据
 */
@Data
@Schema(description = "卡片模板数据")
public class CardData {
    @Schema(description = "卡片标题", required = true, example = "My Card")
    private String title;
    
    @Schema(description = "卡片描述", required = true, example = "This is a sample card")
    private String description;
    
    @Schema(description = "卡片项目列表", required = true, example = "[\"Item 1\", \"Item 2\"]")
    private List<String> items;
}
