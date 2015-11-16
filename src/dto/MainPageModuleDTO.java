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
public class MainPageModuleDTO {
    
    private String type;
    
    private String position;
    
    private String name;
    
    private String display;
    
    private List customDisplayMenus;
    
    private List<CustomModuleParams> customModuleParamList = new ArrayList<>();
    
    public MainPageModuleDTO(Map<String, Object> mainPageModule){
        this.type = (String)mainPageModule.get("type");
        this.position = (String)mainPageModule.get("position");
        this.name = (String)mainPageModule.get("name");
        this.display = (String)mainPageModule.get("display");
        this.customDisplayMenus = (List)mainPageModule.get("custom_display_menus");
        
        List<Map<String, Object>> customParamsList = 
                (List<Map<String, Object>>)mainPageModule.get("custom_params");
        for (Iterator<Map<String, Object>> iterator = customParamsList.iterator(); iterator.hasNext();) {
            Map<String, Object> next = iterator.next();
            CustomModuleParams customModuleParams = new CustomModuleParams(next);
            customModuleParamList.add(customModuleParams);
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public List getCustomDisplayMenus() {
        return customDisplayMenus;
    }

    public void setCustomDisplayMenus(List customDisplayMenus) {
        this.customDisplayMenus = customDisplayMenus;
    }

    public List<CustomModuleParams> getCustomModuleParamList() {
        return customModuleParamList;
    }

    public void setCustomModuleParamList(List<CustomModuleParams> customModuleParamList) {
        this.customModuleParamList = customModuleParamList;
    }
    
        
    
    public static class CustomModuleParams{
        private String categoryUrl;
        
        private String name;
        
        public CustomModuleParams(Map<String, Object> mainPageModuleParams){
            this.categoryUrl = (String)mainPageModuleParams.get("category_url");
            this.name = (String)mainPageModuleParams.get("name");
        }

        public String getCategoryUrl() {
            return categoryUrl;
        }

        public void setCategoryUrl(String categoryUrl) {
            this.categoryUrl = categoryUrl;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
