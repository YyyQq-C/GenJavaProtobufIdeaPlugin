package com.plugin.entitiy;

/**
 * @author YongQc
 * .
 * @date 2019/12/25 19:33
 *
 * ProtoFileEntity :
 */
public class ProtoFileEntity{
    private String name;
    private String path;
    private String fullPath;

    public ProtoFileEntity(String name, String path) {
        this.name = name;
        this.fullPath = path;
        this.path = fullPath.substring(0, fullPath.lastIndexOf("\\"));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFullPath() {
        return fullPath;
    }

    @Override
    public String toString() {
        return name;
    }
}
