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
    
    private String componentK2Category;
    
    private String featured;
    
    private String limit;
    
    private String page;

    
    public MenuItemDTO(){}
    
    public MenuItemDTO(Map<String, Object> menuItem) {
        this.menuItemId = (String)menuItem.get("id");
        this.menuItemName = (String)menuItem.get("name");
        Map<String, Object> component = (Map<String, Object>)menuItem.get("component");
        String componentType = (String) component.get("type");
        String componentK2Category = (String) component.get("k2_category");
        String featured = (String) component.get("featured");
        String limit = (String) component.get("limit");
        String page = (String) component.get("page");
    }
}
