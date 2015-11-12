/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myapp;

import com.codename1.components.WebBrowser;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkManager;
import com.codename1.ui.BrowserComponent;
import com.codename1.ui.Command;
import com.codename1.ui.Form;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import dto.MenuDTO;
import dto.MenuItemDTO;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.web.WebView;
import parser.AppStructureParser;
import rest.RestConsumer;

/**
 *
 * @author CHAYON
 */
public class MainNewsForm extends Form{
    
    private Map<String, NewsSectionForm> newsSectionFormsMap = new HashMap<>();
    
    List<MenuDTO> menuDTOs = new ArrayList<>();
    
    public MainNewsForm() throws IOException{
        menuDTOs = AppStructureParser.getInstance().getMenuDTOList();
    }
    
    public void init() throws IOException{
        
        util.Util.setFormTitle(this);
//        setBackCommand(new Command("Back") {
//                        public void actionPerformed(ActionEvent ev) {
//                            mainNewsForm.showBack();
//                        } 
//                    });
        setScrollable(false);                    
        setLayout(new BorderLayout());
        
        WebBrowser webBrowser = new WebBrowser();
        webBrowser.setURL("http://ads.ashdod10.co.il/components/com_adagency/ijoomla_ad_agency_zone.php?zid=105");
        add(BorderLayout.SOUTH, webBrowser);
        
        buildCommands();
        
        revalidate();
        show();

        //use REST API to build dynamic menu
//        loadAppMenu();            
    }
    
    
    
//    private void loadAppMenu(){
//        
//        ConnectionRequest req = new ConnectionRequest(){                        
//
//            protected void readResponse(InputStream input) throws IOException {
//                
//                InputStreamReader reader = new InputStreamReader(input, "UTF-8");
//                JSONParser parser = new JSONParser();
//                Map<String, Object> response = parser.parseJSON(reader);
//                List<Map<String, Object>> items = (List)response.get("items");
//                
//                for (Map<String, Object> item : items){
//                    String menuId = getMenuId((String)item.get("link"));
//                    if (menuId == null){
//                        continue;
//                    }
//                    String title = (String)item.get("title");
//                    
//                    MenuDTO menuDTO = new MenuDTO(item);
//                    menuDTOs.add(menuDTO);
//                }
//                try {
//                    MainNewsForm.this.buildCommands();
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//            }
//        };
//
//        req.setPost(false);
//        req.setHttpMethod("GET");
//        req.setUrl("http://ashdod10.co.il/get/menu/items?menutype=mainmenu");                       
//
//        NetworkManager.getInstance().addToQueue(req);
//    }
    
    public void buildCommands() {
        if (menuDTOs.size() == 0){
            throw new RuntimeException("Failed to retrieve application menu");
        }
        
        MenuDTO menuDTO = menuDTOs.get(0);
        for (final MenuItemDTO menuItemDTO : menuDTO.getMenuItemDTOList()){
            
            Command cmd = new Command(menuItemDTO.getMenuItemName()){
                public void actionPerformed(ActionEvent evt) { 
                    
                    String pageId = menuItemDTO.getMenuItemId();
                    String dataUrl = "http://ashdod10.co.il/get/" + 
                            menuItemDTO.getComponentType() + "/items?cats=" +
                            menuItemDTO.getComponentId() + "&limit=10";
                    
                    NewsSectionForm newsSectionForm = newsSectionFormsMap.get(pageId);
                    if (newsSectionForm == null){
                        newsSectionForm = new NewsSectionForm(menuItemDTO.getMenuItemName(), 
                                menuItemDTO.getMenuItemId(), 
                                dataUrl, MainNewsForm.this);
                        try {
                            newsSectionForm.init();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        newsSectionFormsMap.put(pageId, newsSectionForm);
                    }
                    newsSectionForm.revalidate();
                    newsSectionForm.show();
                }
            };
            addCommand(cmd);
        }
        revalidate();
    }
    
    
    
    private static String getMenuId(String link){
        int i = link.indexOf("&id=");
        if (i > -1){
            return link.substring(i+4);
        }
        return null;
    }
    
}
