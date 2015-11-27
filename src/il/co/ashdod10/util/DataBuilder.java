/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package il.co.ashdod10.util;

import com.codename1.components.InfiniteProgress;
import com.codename1.components.xmlview.DefaultXMLViewKit;
import com.codename1.components.xmlview.XMLView;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkManager;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Label;
import com.codename1.ui.TextArea;
import com.codename1.ui.animations.CommonTransitions;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.util.Callback;
import com.codename1.xml.Element;
import il.co.ashdod10.app.DataDependedForm;
import il.co.ashdod10.dto.ArticleDTO;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author CHAYON
 */
public class DataBuilder {
    
    //load form data
    public static void downloadArticles(String dataUrl, 
            DataDependedForm form, String title){
                
        ConnectionRequest req = new ConnectionRequest(){
            
            List<ArticleDTO> articleDTOs = new ArrayList<>();
            
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
                form.postDataDownload(articleDTOs, title);
            }
        };
        

        req.setPost(false);
        req.setHttpMethod("GET");
        req.setUrl(dataUrl);//"http://ashdod10.co.il/get/k2/items?cats=3&limit=10");   
       
//        showProgressBarDialog(req);
        
        NetworkManager.getInstance().addToQueueAndWait(req);        
    }
    
    private static void showProgressBarDialog(ConnectionRequest req){
         Dialog d = new Dialog();
        d.setDialogUIID("Container");
        d.setLayout(new BorderLayout());
        Container cnt = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        cnt.addComponent(new Label("Loading..."));
        InfiniteProgress ip = new InfiniteProgress();
        cnt.addComponent(ip);
        d.addComponent(BorderLayout.CENTER, cnt);
        d.setTransitionInAnimator(CommonTransitions.createEmpty());
        d.setTransitionOutAnimator(CommonTransitions.createEmpty());
        d.showPacked(BorderLayout.CENTER, false);
 
        req.setDisposeOnCompletion(d);
    }
    
    
    
    public static Container createNewsBoxContainer(ArticleDTO articleDTO,
            ActionListener actionListener) throws Exception{
                
        
        
        final Container newsBox = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        newsBox.setUIID("newsBox");        
        
        TextArea titleTA = new TextArea();
        titleTA.setUIID("titleLabel");
        String title = il.co.ashdod10.util.Util.parseHtmlSpecialTags(articleDTO.getTitle());
        titleTA.setText(title);
        titleTA.setEditable(false);        
        titleTA.addPointerReleasedListener(actionListener);
        //titleTA.setHeight(Display.getInstance().getDisplayHeight() / 4);
        
        
//        DefaultXMLViewKit kit = new DefaultXMLViewKit();
//        
//        XMLView contentViewer = new XMLView(UIManager.initFirstTheme("/theme"));
//        kit.install(contentViewer);
//        content.load("http://dev.weblite.ca/demo-xmlview.xml", new Callback<Element>() {
        
        //process only the first raw of the content of the Article
//        String content = articleDTO.getContent();
//        content = content.substring(0, content.indexOf("<p>"));
//        content = il.co.ashdod10.util.Util.parseHtmlSpecialTags(content);
//        content = Util.addHeader() + "<p>" + content + "</p>" + Util.addFooter();        
//                
//        contentViewer.loadXmlAsString(content, new Callback<Element>() {
//
//            public void onSucess(Element value) {
//                newsBox.revalidate();
//            }
//
//            public void onError(Object sender, Throwable err, int errorCode, String errorMessage) {                
//            }            
//        });
                
        Container x = new Container(new BoxLayout(BoxLayout.X_AXIS));
        x.add(il.co.ashdod10.util.Util.getImageContainer(actionListener, articleDTO, 2, 4));
        x.add(titleTA);
        x.setPreferredH(Display.getInstance().getDisplayHeight() / 4);
        x.setPreferredW(Display.getInstance().getDisplayWidth());
        newsBox.add(x);        
//        newsBox.add(contentViewer);
        newsBox.revalidate();
        
        return newsBox;
    }
    
}
