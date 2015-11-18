/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package il.co.ashdod10.parser;

import com.codename1.io.JSONParser;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import il.co.ashdod10.dto.MainPageModuleDTO;
import il.co.ashdod10.dto.MenuDTO;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author CHAYON
 */
public class AppStructureParser {
    
    private static AppStructureParser appStructureParser;
    
    private AppStructureParser() throws IOException{
        processJson();
    }
    
    public synchronized static AppStructureParser getInstance() throws IOException{
        if (appStructureParser == null){
            appStructureParser = new AppStructureParser();            
        }
        return appStructureParser;
    }
    
    private String endpoint;
    private String version;
    private String iPhoneSplash;
    private String iPadSplash;
    private String menuType;
    
    private List<MenuDTO> menuDTOList = new ArrayList<MenuDTO>();    
    
    private MainPageModuleDTO mainPageModuleDTO;
    
    
    private void processJson() throws IOException{
        
        Map<String, Object>  response =  getJsonAsMap();
        
        Map<String, Object> application = (Map<String, Object>)response.get("application");
        endpoint = (String)application.get("endpoint");
        version = (String)application.get("version");
        Map<String, Object> splash = (Map<String, Object>)application.get("splash");
        iPhoneSplash = (String)splash.get("iphone");
        iPadSplash = (String)splash.get("ipad");
        menuType = (String)application.get("menutype");
        
        //load menus
        List<Map<String,Object>> menus = (List<Map<String,Object>>)response.get("menus");
   
        for (Map<String,Object> menu : menus) {
            MenuDTO menuDTO = new MenuDTO(menu);
            menuDTOList.add(menuDTO);
        }
        
        //load modules
        Map<String, Object> modules = (Map<String, Object>)response.get("modules");
        for (Map.Entry<String, Object> entrySet : modules.entrySet()) {
            String key = entrySet.getKey();
            Map<String, Object> value = (Map<String, Object>)entrySet.getValue();
            if (key.equals("ads")){
            }
            else if (key.equals("mainpage")){
                mainPageModuleDTO = new MainPageModuleDTO(value);
            }
        }
//                    
//        for (Map.Entry<String, Object> entrySet : menus.entrySet()) {
//            String key = entrySet.getKey();
//            Map<String, Object> menu = (Map<String, Object>)entrySet.getValue();
//            MenuDTO menuDTO = new MenuDTO(menu);
//            menuDTOList.add(menuDTO);
//        }
    }
    
    
    
    public static Map<String, Object>  getJsonAsMap() throws IOException{
        InputStream in = Display.getInstance().getResourceAsStream(Form.class, 
                "/app_structure.json");
        InputStreamReader reader = new InputStreamReader(in);
        JSONParser parser = new JSONParser();
        Map<String, Object> response = parser.parseJSON(reader);
        return response;
    }
    
    
    public static AppStructureParser getAppStructureParser() {
        return appStructureParser;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getVersion() {
        return version;
    }

    public String getiPhoneSplash() {
        return iPhoneSplash;
    }

    public String getiPadSplash() {
        return iPadSplash;
    }

    public String getMenuType() {
        return menuType;
    }

    public List<MenuDTO> getMenuDTOList() {
        return menuDTOList;
    }

    public void setMenuDTOList(List<MenuDTO> menuDTOList) {
        this.menuDTOList = menuDTOList;
    }

    public MainPageModuleDTO getMainPageModuleDTO() {
        return mainPageModuleDTO;
    }

    public void setMainPageModuleDTO(MainPageModuleDTO mainPageModuleDTO) {
        this.mainPageModuleDTO = mainPageModuleDTO;
    }    
}
