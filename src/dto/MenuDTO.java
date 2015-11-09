/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author CHAYON
 */
public class MenuDTO {
    
    private String menuId;
    
    private String menuName;
    
    List<MenuItemDTO> menuItemDTO = new ArrayList<MenuItemDTO>();
    
    public MenuDTO(Map<String, Object> menu){
        this.menuId = (String)menu.get("id");
        this.menuName = (String)menu.get("name");
        Map<String, Object> menuItems = (Map<String, Object>)menu.get("menuitems");
        for (Map.Entry<String, Object> entrySet : menuItems.entrySet()) {
            String key = entrySet.getKey();
            Map<String, Object> menuItem = (Map<String, Object>)entrySet.getValue();
            MenuItemDTO menuItemDTO = new MenuItemDTO(menuItem);
            menuDTOList.add(menuDTO);
        }
        
    }
    
}
