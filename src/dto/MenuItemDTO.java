/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.util.Map;

/**
 *
 * @author CHAYON
 */
public class MenuItemDTO {
    
    private String menuItemId;
    
    private String menuItemName;
    
    private String componentType;
    
    private String componentId;
    
    private String featured;
    
    private String limit;
    
    private String page;

    
    public MenuItemDTO(){}
    
    public MenuItemDTO(Map<String, Object> menuItem) {
                                
        this.menuItemId = (String)menuItem.get("id");
        this.menuItemName = (String)menuItem.get("name");
        Map<String, Object> component = (Map<String, Object>)menuItem.get("component");
        this.componentType = (String) component.get("type");
        this.componentId = (String) component.get("category");
        this.featured = (String) component.get("featured");
        this.limit = (String) component.get("limit");
        this.page = (String) component.get("page");
    }

    public String getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(String menuItemId) {
        this.menuItemId = menuItemId;
    }

    public String getMenuItemName() {
        return menuItemName;
    }

    public void setMenuItemName(String menuItemName) {
        this.menuItemName = menuItemName;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public String getFeatured() {
        return featured;
    }

    public void setFeatured(String featured) {
        this.featured = featured;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
    
    
}
