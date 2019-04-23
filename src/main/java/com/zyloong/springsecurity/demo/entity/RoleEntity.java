package com.zyloong.springsecurity.demo.entity;

/**
 * @author zyloong
 */
public class RoleEntity {
    // 角色id
    private Integer id;
    // 角色名
    private String role;
    // 角色描述
    private String description;

    public RoleEntity(String role, String description) {
        this.role = role;
        this.description = description;
    }

    public RoleEntity(Integer id, String role, String description) {
        this.id = id;
        this.role = role;
        this.description = description;
    }

    public RoleEntity() {
    }

    public Integer getId() {
        return this.id;
    }

    public String getRole() {
        return this.role;
    }

    public String getDescription() {
        return this.description;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
