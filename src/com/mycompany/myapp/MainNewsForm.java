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
import com.codename1.ui.Button;
import com.codename1.ui.Command;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import dto.ArticleDTO;
import dto.MainPageModuleDTO;
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
import util.ArticleAction;
import util.DataBuilder;
import util.Util;

/**
 *
 * @author CHAYON
 */
public class MainNewsForm extends Form implements DataDependedForm{
    
    private Map<String, NewsSectionForm> newsSectionFormsMap = new HashMap<>();
    
    List<ArticleDTO> articleDTOs = new ArrayList<>();
    
    List<MenuDTO> menuDTOs = new ArrayList<>();
    
    Container centerContainer;
    
    ArticleAction articleAction;
    
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
        
        
        WebBrowser webBrowser = new WebBrowser();
        webBrowser.setPreferredSize(new Dimension(Display.getInstance().getDisplayWidth(),
                        Display.getInstance().getDisplayHeight()/7));
        //webBrowser.setURL("http://ads.ashdod10.co.il/components/com_adagency/ijoomla_ad_agency_zone.php?zid=105");
        webBrowser.setURL("http://ads.ashdod10.co.il/images/stories/ad_agency/2/1446991534.gif");        
        webBrowser.addPointerPressedListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                System.out.println("Action Performed");
                Display.getInstance().execute("http://www.hakeren.co.il");
            }
        });
        setLayout(new BorderLayout());
        add(BorderLayout.SOUTH, webBrowser);
        
        centerContainer = new Container();
        centerContainer.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        centerContainer.setScrollableY(true);
        add(BorderLayout.CENTER, centerContainer);
        
        buildCommands();
        
        buildMainNewsForm();
        
        revalidate();
        show();

        //use REST API to build dynamic menu
//        loadAppMenu();            
    }
    
    Map<String, ArticleDTO> articlesMap = new HashMap<>();
    
    //this is a callback method that is being invoked after data is being 
    //downloaded from the server
    public synchronized void postDataDownload(Object data, String title){
        
        articleDTOs.addAll((List<ArticleDTO>)data);
        
//        for(ArticleDTO articleDTO : (List<ArticleDTO>)data){
//            articlesMap.put(articleDTO.getId(), articleDTO);
//        }
                
        List<ArticleDTO> _data = (List<ArticleDTO>)data;
        
        centerContainer.add(Util.getComponentSeparator(title));
        
        for(ArticleDTO articleDTO : _data){
            try {
                ArticleAction articleAction = new ArticleAction(
                        articleDTO, this);
                Container singleArticleBoxNews = 
                        DataBuilder.createNewsBoxContainer(articleDTO, articleAction);
                centerContainer.add(singleArticleBoxNews);                
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        
//        add(BorderLayout.CENTER, centerContainer);        
//        BoxLayout boxLayout = new BoxLayout(BoxLayout.Y_AXIS);
//        mainContainer.setLayout(boxLayout);
//        
//        mainContainer.setScrollableY(true);
        centerContainer.revalidate();        
        revalidate();
        show();
    }
    
    public void buildMainNewsForm() throws IOException{
//        Container mainContainer = getContentPane();
//        BoxLayout boxLayout = new BoxLayout(BoxLayout.Y_AXIS);
//        mainContainer.setLayout(boxLayout);
//        
//        mainContainer.setScrollableY(true);
        
        MainPageModuleDTO mainPageModuleDTO = 
                AppStructureParser.getInstance().getMainPageModuleDTO();
        
        List<MainPageModuleDTO.CustomModuleParams> customModuleParamsList = 
                mainPageModuleDTO.getCustomModuleParamList();
        
        for (MainPageModuleDTO.CustomModuleParams customModuleParams : customModuleParamsList){
            
            String dataUrl = customModuleParams.getCategoryUrl();
            String title = customModuleParams.getName();
            
            DataBuilder.downloadArticles(dataUrl, this, title);
            
//            NewsSectionForm newsSectionForm = newsSectionFormsMap.get(pageId);
//                                    
//            if (newsSectionForm == null){
//                System.out.println("Failed to find NewsSection with pageId: " + pageId);
//                continue;
//            }
//            
//            String dataUrl = "http://ashdod10.co.il/get/" + 
//                    newsSectionForm.getComponentType() + "/items?cats=" +
//                    newsSectionForm.getCategoryId() + 
//                    "&limit=" + customModuleParams.getLimit();
            
            
            
//            NewsSectionForm newsSectionFormHeadlines = new NewsSectionForm(
//                    newsSectionForm.getTitle(), 
//                    pageId, 
//                    ,
//                    ,
//                    ,
//                    customModuleParams.getPage(),
//                    null);
//            try {
//                newsSectionFormHeadlines.init();
//                mainContainer.add(Util.getComponentSeparator());
//                mainContainer.add(newsSectionFormHeadlines);
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
        }
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
            String pageId = menuItemDTO.getMenuItemId();
            NewsSectionForm newsSectionForm = new NewsSectionForm(
                    menuItemDTO.getMenuItemName(),
                    menuItemDTO.getMenuItemId(), 
                    menuItemDTO.getComponentType(),
                    menuItemDTO.getComponentId(),
                    menuItemDTO.getLimit(), "1",
                    MainNewsForm.this);
            newsSectionFormsMap.put(pageId, newsSectionForm);
            
            
            Command cmd = new Command(menuItemDTO.getMenuItemName()){
                public void actionPerformed(ActionEvent evt) {                    
//                    String dataUrl = "http://ashdod10.co.il/get/" + 
//                            menuItemDTO.getComponentType() + "/items?cats=" +
//                            menuItemDTO.getComponentId() + "&limit="+menuItemDTO.getLimit();                    
                    NewsSectionForm newsSectionForm = newsSectionFormsMap.get(pageId);
                    if (newsSectionForm == null){
                        System.out.println("Failed to retrieve DataForm for "
                                + "command with PageID: " + pageId);
                        return;
                    }
                    try {
                        newsSectionForm.init();
                        newsSectionForm.revalidate();
                        newsSectionForm.show();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
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
