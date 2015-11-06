/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

/**
 *
 * @author CHAYON
 */
public class MenuItemDTO {
    
    private String menuId;
    
    private String title;

    
    public MenuItemDTO(){}
    
    public MenuItemDTO(String menuId, String title) {
        this.menuId = menuId;
        this.title = title;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    
    
}
