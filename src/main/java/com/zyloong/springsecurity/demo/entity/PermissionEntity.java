package com.zyloong.springsecurity.demo.entity;

/**
 * @author Zhu YiLong
 * @date 2019/04/23
 */
public class PermissionEntity {
    // 权限id
    private Integer id;
    // 权限文字描述，无逻辑意义
    private String description;
    // 权限url前缀，即Controller的 @RequestMapping注解中的内容，如果有多个，则以逗号分隔
    // 如：@RequestMapping({"test","api"})
    // 则此字段值应该为： "/test,/api"
    private String prefix;
    // 请求方法，GET、POST、PUT、DELETE等，必须是全大写
    private String method;
    // 请求url，即接口上@XxxMapping(value = "/noPermission") 中的value值
    // 如 @PostMapping(value = "/admin")
    // 则此字段值应该为： "/admin"
    private String url;

    public PermissionEntity(Integer id, String description, String prefix, String method, String url) {
        this.id = id;
        this.description = description;
        this.prefix = prefix;
        this.method = method;
        this.url = url;
    }

    public PermissionEntity() {
    }

    public Integer getId() {
        return this.id;
    }

    public String getDescription() {
        return this.description;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public String getMethod() {
        return this.method;
    }

    public String getUrl() {
        return this.url;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}