package com.plugin.config;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;

/**
 * @author YongQc
 * .
 * @date 2019/12/25 19:41
 *
 * SysConfig :
 */
public  class SysConfig {
    public static final String PROTOC_PATH = "GoogleProtobufTool.protocPath";
    public static final String FILE_PATH = "GoogleProtobufTool.protoFilePath";
    public static final String OUT_PATH = "GoogleProtobufTool.outPath";

    public static String getValue(Project project, String name){
        PropertiesComponent props = null;
        if (name.equals(PROTOC_PATH)){
            props = PropertiesComponent.getInstance();
        }else {
            props = PropertiesComponent.getInstance(project);
        }

        return props.getValue(name);
    }
}
