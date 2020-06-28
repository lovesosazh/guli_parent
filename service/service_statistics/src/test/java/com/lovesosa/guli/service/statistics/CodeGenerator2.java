package com.lovesosa.guli.service.statistics;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

/**
 * @author lovesosa
 */
public class CodeGenerator2 {

    @Test
    public void codeGenerator() {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor("lovesosa");
        gc.setOpen(false);
        gc.setFileOverride(false);
        gc.setDateType(DateType.ONLY_DATE);
        gc.setSwagger2(true); //实体属性 Swagger2 注解
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3306/guli_statistics?useUnicode=true&useSSL=false&characterEncoding=utf8&serverTimezone=GMT%2B8");
        // dsc.setSchemaName("public");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("denglong");
        dsc.setDbType(DbType.MYSQL);
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName("statistics");
        pc.setParent("com.lovesosa.guli.service");
        pc.setController("controller");
        pc.setEntity("entity");
        pc.setService("service");
        pc.setMapper("mapper");
        mpg.setPackageInfo(pc);

        // 自定义配置
//        InjectionConfig cfg = new InjectionConfig() {
//            @Override
//            public void initMap() {
//                // to do nothing
//            }
//        };

        // 如果模板引擎是 freemarker
//        String templatePath = "/templates/mapper.xml.ftl";
//        // 如果模板引擎是 velocity
//        // String templatePath = "/templates/mapper.xml.vm";
//
//        // 自定义输出配置
//        List<FileOutConfig> focList = new ArrayList<>();
//        // 自定义配置会被优先输出
//        focList.add(new FileOutConfig(templatePath) {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
//                return projectPath + "/src/main/resources/mapper/" + pc.getModuleName()
//                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
//            }
//        });
        /*
        cfg.setFileCreate(new IFileCreate() {
            @Override
            public boolean isCreate(ConfigBuilder configBuilder, FileType fileType, String filePath) {
                // 判断自定义文件夹是否需要创建
                checkDir("调用默认方法创建的目录，自定义目录用");
                if (fileType == FileType.MAPPER) {
                    // 已经生成 mapper 文件判断存在，不想重新生成返回 false
                    return !new File(filePath).exists();
                }
                // 允许生成模板文件
                return true;
            }
        });
        */
//        cfg.setFileOutConfigList(focList);
//        mpg.setCfg(cfg);

        // 配置模板
//        TemplateConfig templateConfig = new TemplateConfig();

        // 配置自定义输出模板
        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        // templateConfig.setEntity("templates/entity2.java");
        // templateConfig.setService();
        // templateConfig.setController();
//
//        templateConfig.setXml(null);
//        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
//        strategy.setSuperEntityClass("你自己的父类实体,没有就不用设置!");
        strategy.setEntityLombokModel(true);
        strategy.setLogicDeleteFieldName("is_deleted");
        strategy.setEntityBooleanColumnRemoveIsPrefix(true);

        TableFill gmtCreate = new TableFill("gmt_create", FieldFill.INSERT);
        TableFill gmtModified = new TableFill("gmt_modified", FieldFill.INSERT_UPDATE);
        ArrayList<TableFill> tableFills = new ArrayList<>();
        tableFills.add(gmtCreate);
        tableFills.add(gmtModified);
        strategy.setTableFillList(tableFills);
        strategy.setRestControllerStyle(true);
        // 公共父类
//        strategy.setSuperControllerClass("你自己的父类控制器,没有就不用设置!");
        // 写于父类中的公共字段
//        strategy.setSuperEntityColumns("id");
//        strategy.setInclude(scanner("表名，多个英文逗号分割").split(","));
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(pc.getModuleName() + "_");
        strategy.setSuperEntityClass("com.lovesosa.guli.service.base.model.BaseEntity");
        strategy.setSuperEntityColumns("id","gmt_create","gmt_modified");
        mpg.setStrategy(strategy);
//        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }
}
