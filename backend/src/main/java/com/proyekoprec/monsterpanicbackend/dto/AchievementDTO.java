package com.proyekoprec.monsterpanicbackend.dto;

public class AchievementDTO {
    private String name;
    private String description;
    private String icon_class;
    
    // Getter dan Setter
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getIcon_class() { return icon_class; }
    public void setIcon_class(String icon_class) { this.icon_class = icon_class; }
}