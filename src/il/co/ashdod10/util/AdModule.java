/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package il.co.ashdod10.util;

import com.codename1.components.WebBrowser;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkManager;
import com.codename1.ui.Display;
import com.codename1.ui.events.BrowserNavigationCallback;
import com.codename1.ui.geom.Dimension;
import il.co.ashdod10.dto.ArticleDTO;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author CHAYON
 */
public class AdModule {
    
//    private static AdModule adModule = null;
//    
//    
//    private AdModule(){}
//    
//    public synchronized static AdModule getInstance(){
//        if (adModule == null){
//            adModule = new AdModule();            
//        }
//        return adModule;
//    }
    
    public static WebBrowser getAdModule(){
        
        Map<String, Object> adJson = getAdJson();
        
        String bannerImage = (String)adJson.get("bannerImage");
        String bannerUrl = (String)adJson.get("bannerUrl");        
        
        
        WebBrowser webBrowser = new WebBrowser();
        webBrowser.setPreferredSize(new Dimension(Display.getInstance().getDisplayWidth(), 
                Display.getInstance().getDisplayHeight()/7));
//        webBrowser.setWidth(Display.getInstance().getDisplayWidth());
//        webBrowser.setHeight(Display.getInstance().getDisplayHeight()/7);
        //webBrowser.setURL("http://ads.ashdod10.co.il/components/com_adagency/ijoomla_ad_agency_zone.php?zid=105");
        //webBrowser.setURL("http://ads.ashdod10.co.il/images/stories/ad_agency/2/1446991534.gif");
        webBrowser.setPage("<html>"
                + "<body style=\"margin:0; padding:0\">"
                + "<div class=\"banner_marging\" style=\"margin:0; padding:0\">"
                + "<a href=\""+ bannerUrl +"\">"
                + "<img src=\"" + bannerImage +"\"></a>"
                //+ "<a href=\"http://ads.ashdod10.co.il/index.php?option=com_adagency&controller=adagencyAds&task=click&cid=3&bid=132&aid=2\">"
                //+ "<img src=\"http://ads.ashdod10.co.il/images/stories/ad_agency/2/1446991534.gif\"></a>"
                + "</div>"
                + "</body>"
                + "</html>", null);
        webBrowser.setBrowserNavigationCallback(new BrowserNavigationCallback() {
            @Override
            public boolean shouldNavigate(String url) {
                Display.getInstance().execute(url);
                return true;
            }
        });
        
        return webBrowser;
    }
    
    private static Map<String, Object> getAdJson(){
        
        final Map<String, Object> response = new HashMap<String,Object>();
        
        ConnectionRequest req = new ConnectionRequest(){
            
            protected void readResponse(InputStream input) throws IOException {
//                createAndSetArticleSection(input, form, pageId);
                InputStreamReader reader = new InputStreamReader(input, "UTF-8");
                JSONParser parser = new JSONParser();
                response.putAll(parser.parseJSON(reader));
            }
        };
        

        req.setPost(false);
        req.setHttpMethod("GET");
        req.setUrl("http://ashdod10.co.il/get/remoteads/getRemoteAd?zid=105&remoteURI=ads.ashdod10.co.il");
       
        NetworkManager.getInstance().addToQueueAndWait(req);  
        
        return response;
    }
    
}
