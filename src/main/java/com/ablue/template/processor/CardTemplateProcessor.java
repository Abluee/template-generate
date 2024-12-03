package com.ablue.template.processor;

import com.ablue.template.model.CardData;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 卡片模板处理器
 * 实现卡片模板的处理和Excel解析功能
 */
@Component
public class CardTemplateProcessor extends AbstractTemplateProcessor<CardData> {

    private static final String TYPE = "card";

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    protected CardData processRawData(List<Map<Integer, String>> rawData) {
        // TODO: 在这里实现自定义的数据处理逻辑
        CardData cardData = new CardData();
        // ... 根据rawData处理数据并设置到cardData对象中
        return cardData;
    }
}
