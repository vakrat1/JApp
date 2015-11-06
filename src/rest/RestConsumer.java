/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkManager;
import com.codename1.ui.Command;
import com.codename1.ui.Container;
import com.codename1.ui.Form;
import com.codename1.ui.events.ActionEvent;
import com.mycompany.myapp.MainNewsForm;
import com.mycompany.myapp.NewsSectionForm;
import dto.ArticleDTO;
import dto.MenuItemDTO;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 *
 * @author CHAYON
 */
public class RestConsumer {
    
    public static void loadSectionArticles(String url, final Form form, String pageId){
        
        List<ArticleDTO> articleDTOs = new ArrayList<ArticleDTO>();
    
        ConnectionRequest req = new ConnectionRequest(){                        

            protected void readResponse(InputStream input) throws IOException {
//                createAndSetArticleSection(input, form, pageId);
                InputStreamReader reader = new InputStreamReader(input, "UTF-8");
                JSONParser parser = new JSONParser();
                Map<String, Object> response = parser.parseJSON(reader);
                List<Map<String, Object>> items = (List)response.get("items");
                for (Map<String, Object> item : items){
//                    String title = util.Util.parseHtmlSpecialTags((String)item.get("title"));
//                    String content= util.Util.parseContentElement((String)item.get("content"));
//                    content = util.Util.parseHtmlSpecialTags(content);
                    String id = (String)item.get("id");
                    String title = (String)item.get("title");
                    String content= (String)item.get("content");                    
                    String created= (String)item.get("created");
                    String imgUrl = (String)((Map)item.get("images")).get("imageLarge");
                    
                    ArticleDTO articleDTO = new ArticleDTO(id, title, content, imgUrl);
                    articleDTOs.add(articleDTO);
                }
            }
        };

        req.setPost(false);
        req.setHttpMethod("GET");
        req.setUrl(url);//"http://ashdod10.co.il/get/k2/items?cats=3&limit=10");                       

        NetworkManager.getInstance().addToQueue(req);
    }
    
    
    public static void loadAppMenu(MainNewsForm form){
        
        List<MenuItemDTO> menuItemDTOs = new ArrayList<>();
    
        ConnectionRequest req = new ConnectionRequest(){                        

            protected void readResponse(InputStream input) throws IOException {
                
                InputStreamReader reader = new InputStreamReader(input, "UTF-8");
                JSONParser parser = new JSONParser();
                Map<String, Object> response = parser.parseJSON(reader);
                List<Map<String, Object>> items = (List)response.get("items");
                
                for (Map<String, Object> item : items){
                    String menuId = getMenuId((String)item.get("link"));
                    if (menuId == null){
                        continue;
                    }
                    String title = (String)item.get("title");
                    
                    MenuItemDTO menuItemDTO = new MenuItemDTO(menuId, title);
                    menuItemDTOs.add(menuItemDTO);
                }                
                form.buildCommands(menuItemDTOs);
            }
        };

        req.setPost(false);
        req.setHttpMethod("GET");
        req.setUrl("http://ashdod10.co.il/get/menu/items?menutype=mainmenu");                       

        NetworkManager.getInstance().addToQueue(req);
    }
    
    private static String getMenuId(String link){
        int i = link.indexOf("&id=");
        if (i > -1){
            return link.substring(i+4);
        }
        return null;
    }
    
    
    private static void createAndSetArticleSection(InputStream input, Form form,
            String pageId) throws UnsupportedEncodingException, IOException{
        
                try {
                    NewsSectionForm sectionNewsBox = 
                            new NewsSectionForm(articleDTOs, pageId);
                    form.getContentPane().removeAll();
                    form.getContentPane().add(sectionNewsBox.createNewsBoxContainer(form));
                } catch (Exception ex) {
                    System.out.println("ERROR: " + RestConsumer.class.getName());
                    ex.printStackTrace();
                    
                }
                form.revalidate();
    }
    
}
