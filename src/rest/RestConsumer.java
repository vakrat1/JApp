/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkManager;
import com.codename1.ui.Container;
import com.codename1.ui.Form;
import com.mycompany.myapp.SectionNewsBox;
import dto.ArticleDTO;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    
    public static void testLoadJSONUsingJSONParser(Form form){
    
        ConnectionRequest req = new ConnectionRequest(){                        

            protected void readResponse(InputStream input) throws IOException {
                //super.readResponse(input);
                
                List<ArticleDTO> articleDTOs = new ArrayList<ArticleDTO>();

                InputStreamReader reader = new InputStreamReader(input, "UTF-8");
                JSONParser parser = new JSONParser();
                Map<String, Object> response = parser.parseJSON(reader);
                List<Map<String, Object>> items = (List)response.get("items");
                for (Map<String, Object> item : items){
                    String title = (String)item.get("title");
                    String content= (String)item.get("content");
                    String created= (String)item.get("created");
                    String imgUrl = (String)((Map)item.get("images")).get("imageLarge");
                    
                    ArticleDTO articleDTO = new ArticleDTO(title, content, imgUrl);
                    articleDTOs.add(articleDTO);
                }
                
                try {
                    SectionNewsBox sectionNewsBox = new SectionNewsBox();
                    form.add(sectionNewsBox.createNewsBoxContainer(articleDTOs));
                } catch (Exception ex) {
                    System.out.println("ERROR: " + RestConsumer.class.getName());
                    ex.printStackTrace();
                    
                }
                form.revalidate();
//            

            }

        };

        req.setPost(false);
        req.setHttpMethod("GET");
        req.setUrl("http://ashdod10.co.il/get/k2/items?cats=3&limit=10");                       

        NetworkManager.getInstance().addToQueue(req);
    }
    
}
