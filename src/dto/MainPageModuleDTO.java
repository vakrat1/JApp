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
        private String category;
        
        private String featured;
        
        private String limit;
        
        private String page;
        
        public CustomModuleParams(Map<String, Object> mainPageModuleParams){
            this.category = (String)mainPageModuleParams.get("category");
            this.featured = (String)mainPageModuleParams.get("featured");
            this.limit = (String)mainPageModuleParams.get("limit");
            this.page = (String)mainPageModuleParams.get("page");
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
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
    
    
    
    
    //"type": "k2",
//      "position": "above/below/left/right/custom",
//      "name": "עמוד הבית- חדשות",
//      "disply": "none/all/custom/customexpect",
//      "custom display menus": [
//        "2000.1"
//      ],
//      "custom params": {
//        "category": "114",
//        "featured": "yes/no/include",
//        "limit": "3",
//        "page": "1"
//      },
    
}
