/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author CHAYON
 */
public class MenuDTO {
    
    private String menuId;
    
    private String menuType;
    
    private String menuName;
    
    List<MenuItemDTO> menuItemDTOList = new ArrayList<MenuItemDTO>();
    
    public MenuDTO(Map<String, Object> menu){
//        //        "id": "1",
//          "menutype": "mainmenu",
//          "title": "Main Menu",
//     
        this.menuId = (String)menu.get("id");
        this.menuType = (String)menu.get("menutype");
        this.menuName = (String)menu.get("name");
        List<Map<String, Object>> menuItems = (List<Map<String, Object>>)menu.get("menuitems");
        for (Iterator<Map<String, Object>> iterator = menuItems.iterator(); iterator.hasNext();) {
            Map<String, Object> menuItem = iterator.next();
                MenuItemDTO menuItemDTO = new MenuItemDTO(menuItem);
                menuItemDTOList.add(menuItemDTO);
        }
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public List<MenuItemDTO> getMenuItemDTOList() {
        return menuItemDTOList;
    }

    public void setMenuItemDTOList(List<MenuItemDTO> menuItemDTOList) {
        this.menuItemDTOList = menuItemDTOList;
    }

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }
}
