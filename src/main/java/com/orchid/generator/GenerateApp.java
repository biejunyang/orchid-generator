package com.orchid.generator;

import cn.hutool.core.io.IoUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class GenerateApp {

    private static Configuration configuration=null;
    private static final String baseUrl="E:/orchid";
    private static final String groupId="com.orchid";
    private static final String artifactId="orchid-test";
    private static final String version="1.0.2";
    private static final String basePackage="com.orchid.test";
    private static final String mainClassName="OrchidTestApp";


    static {
        try {
            // step1 创建freeMarker配置实例
            configuration=new Configuration();

            // step2 获取模版路径
            String path=GenerateApp.class.getClassLoader().getResource("templates").getFile();
            configuration.setDirectoryForTemplateLoading(new File(path));
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }


    public static void main(String[] args) throws IOException, TemplateException {
        File baseDir=new File(baseUrl,artifactId);
        if(baseDir.isDirectory() && baseDir.exists()){
            baseDir.delete();
        }
        baseDir.mkdir();
        System.out.println(baseDir.getAbsolutePath());

        File srcDir=new File(baseDir, "src/main/java");
        srcDir.mkdirs();

        File resourceDir=new File(baseDir, "src/main/resources");
        resourceDir.mkdirs();

        File packageDir=new File(srcDir, basePackage.replaceAll("[.]", "/"));
        packageDir.mkdirs();



        File pomXml=new File(baseDir, "pom.xml");
//        InputStream ins=GenerateApp.class.getClassLoader().getResourceAsStream("templates/pom.xml.ftl");
//        IoUtil.copy(ins, new FileOutputStream(pomXml));

        // step3 创建数据模型
        Map<String, Object> data=new HashMap<>();
        data.put("groupId", groupId);
        data.put("artifactId", artifactId);
        data.put("version", version);



        // step4 加载模版文件
        Template template=configuration.getTemplate("pom.xml.ftl");

        // step5 输出数据
        template.process(data, new FileWriter(pomXml));


        File applicatonYml=new File(resourceDir, "application.yml");
//        ins=GenerateApp.class.getClassLoader().getResourceAsStream("templates/application.yml.ftl");
//        IoUtil.copy(ins, new FileOutputStream(applicatonYml));

        template=configuration.getTemplate("application.yml.ftl");
        template.process(data, new FileWriter(applicatonYml));



        File mainJava=new File(packageDir, mainClassName+".java");
        data=new HashMap<>();
        data.put("basePackage", basePackage);
        data.put("mainClassName", mainClassName);
        template=configuration.getTemplate("MainApp.java.ftl");
        template.process(data, new FileWriter(mainJava));


    }




}
