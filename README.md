# Template Generate

基于 FreeMarker 的代码生成工具，支持自定义模板和 Excel 数据导入，可以快速生成各类代码文件。

[![GitHub stars](https://img.shields.io/github/stars/Abluee/template-generate.svg)](https://github.com/Abluee/template-generate/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/Abluee/template-generate.svg)](https://github.com/Abluee/template-generate/network/members)
[![GitHub issues](https://img.shields.io/github/issues/Abluee/template-generate.svg)](https://github.com/Abluee/template-generate/issues)
[![GitHub license](https://img.shields.io/github/license/Abluee/template-generate.svg)](https://github.com/Abluee/template-generate/blob/master/LICENSE)

## 特性

- 🚀 基于 FreeMarker 模板引擎，支持强大的模板语法
- 📝 支持 Excel 数据导入，快速批量生成代码
- 🎨 灵活的模板处理器扩展机制
- 📦 支持多种模板类型（如卡片模板等）
- 🔌 提供 RESTful API 接口，方便集成
- 📚 集成 Swagger 文档，接口调试方便
- 📊 AOP 日志记录，运行状态一目了然

## 快速开始

### 环境要求

- JDK 1.8+
- Maven 3.6+
- Spring Boot 2.7.0

### 安装

```bash
# 克隆项目
git clone https://github.com/Abluee/template-generate.git

# 进入项目目录
cd template-generate

# 编译打包
mvn clean package

# 运行
java -jar target/template-generate-1.0-SNAPSHOT.jar
```

### 使用示例

1. 准备模板文件
```freemarker
// templates/card/example.ftl
public class ${className} {
    private String ${field};
    
    public String get${field?cap_first}() {
        return ${field};
    }
    
    public void set${field?cap_first}(String ${field}) {
        this.${field} = ${field};
    }
}
```

2. 准备数据（Excel格式）
```
className | field
User      | name
Role      | code
```

3. 调用接口生成代码
```bash
curl -X POST http://localhost:8080/api/template/generate \
  -H "Content-Type: multipart/form-data" \
  -F "file=@data.xlsx" \
  -F "type=card"
```

## 项目结构

```
src/main/
├── java/com/nbcb/template/
│   ├── aspect/          # AOP切面
│   ├── controller/      # API控制器
│   ├── core/           # 核心接口
│   ├── model/          # 数据模型
│   └── processor/      # 模板处理器
└── resources/
    ├── templates/      # FreeMarker模板
    └── application.yml # 配置文件
```

## 模板处理器

### 现有处理器

- CardTemplateProcessor: 卡片模板处理器，支持从 Excel 导入数据生成代码

### 自定义处理器

创建新的处理器只需继承 `AbstractTemplateProcessor` 并实现必要方法：

```java
@Component
public class CustomProcessor extends AbstractTemplateProcessor<CustomData> {
    @Override
    public String getType() {
        return "custom";
    }
    
    @Override
    protected CustomData processRawData(List<Map<Integer, String>> rawData) {
        // 实现数据处理逻辑
        return customData;
    }
}
```

## API 文档

启动应用后访问：`http://localhost:8080/swagger-ui.html`

主要接口：
- POST `/api/template/generate`: 生成代码
- GET `/api/template/types`: 获取支持的模板类型

## 配置说明

配置文件：`application.yml`

```yaml
spring:
  freemarker:
    template-loader-path: classpath:/templates/
    suffix: .ftl
    cache: false
```

## 贡献指南

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交改动 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 提交 Pull Request

## 作者

👤 **Abluee**

* GitHub: [@Abluee](https://github.com/Abluee)

## 开源协议

本项目使用 MIT 协议 - 详见 [LICENSE](LICENSE)
