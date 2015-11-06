/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myapp;

import com.codename1.components.SpanLabel;
import com.codename1.components.xmlview.DefaultXMLViewKit;
import com.codename1.components.xmlview.XMLView;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkManager;
import com.codename1.l10n.L10NManager;
import com.codename1.ui.Command;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.Font;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.TextArea;
import com.codename1.ui.URLImage;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;
import com.codename1.util.Callback;
import com.codename1.xml.Element;
import dto.ArticleDTO;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import rest.RestConsumer;
import util.Util;

/**
 *
 * @author yaniv
 */
public class NewsSectionForm extends Form{
    
    private Map<String, ArticleDTO> articlesMap = new HashMap<>();
    
    private Map<String, ArticleForm> articleFormsMap = new HashMap<>();
    
    private List<ArticleDTO> articleDTOs = new ArrayList<>();
    
    private String pageId;
    
    private String dataUrl;
    
    private Form previousForm;
    
    public NewsSectionForm(String title, String pageId, String dataUrl,
            Form previousForm){
        this.setTitle(title);
        this.pageId = pageId;
        this.dataUrl = dataUrl;
        this.previousForm = previousForm;
    }
    
    public void init() throws Exception{  
        
        util.Util.setFormTitle(this);
        
        Container sectionBox = getContentPane();
        
        //TODO - might need to move this referesh code from here
        sectionBox.addPullToRefresh(new Runnable() {
            @Override
            public void run() {
            }
        });        
        setBackCommand(new Command("Back") {
            public void actionPerformed(ActionEvent ev) {
                previousForm.showBack();
            } 
        });
        setScrollableY(true);
        loadArticles();        
    }
    
    private Container createNewsBoxContainer(ArticleDTO articleDTO) throws Exception{
                
        ArticleAction articleAction = new ArticleAction(articleDTO.getId());
        
        final Container newsBox = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        newsBox.setUIID("newsBox");        
        
        TextArea titleTA = new TextArea();
        titleTA.setUIID("titleLabel");
        String title = util.Util.parseHtmlSpecialTags(articleDTO.getTitle());
        titleTA.setText(title);
        titleTA.setEditable(false);
        titleTA.addPointerReleasedListener(articleAction);        
        
        
        DefaultXMLViewKit kit = new DefaultXMLViewKit();
        
        XMLView contentViewer = new XMLView(UIManager.initFirstTheme("/theme"));
        kit.install(contentViewer);
//        content.load("http://dev.weblite.ca/demo-xmlview.xml", new Callback<Element>() {
        
        //process only the first raw of the content of the Article
        String content = articleDTO.getContent();
        content = content.substring(0, content.indexOf("<p>"));
        content = util.Util.parseHtmlSpecialTags(content);
        content = Util.addHeader() + "<p>" + content + "</p>" + Util.addFooter();        
                
        contentViewer.loadXmlAsString(content, new Callback<Element>() {

            public void onSucess(Element value) {
                newsBox.revalidate();
            }

            public void onError(Object sender, Throwable err, int errorCode, String errorMessage) {                
            }            
        });
                
        Container x = new Container(new BoxLayout(BoxLayout.X_AXIS));
        x.add(util.Util.getImageContainer(articleAction, articleDTO, 2, 4));
        x.add(titleTA);
        newsBox.add(x);
        newsBox.add(contentViewer);
        newsBox.revalidate();
        
        return newsBox;
    }
    
    
    
    class ArticleAction implements ActionListener{
        
        private String id;
        
        public ArticleAction(String id){
            this.id = id;
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            ArticleDTO articleDTO = articlesMap.get(id);
            try {
                ArticleForm articleForm = articleFormsMap.get(id);
                if (articleForm == null){
                    articleForm = new ArticleForm(NewsSectionForm.this);
                    articleForm.init();
                    articleForm.createArticleBox(articleDTO);
                }
                articleForm.revalidate();
                articleForm.show();;
            } catch (Exception ex) {
                System.out.println("ERROR: " + RestConsumer.class.getName());
                ex.printStackTrace();

            }
        }
    }
    
    //load form data
    private void loadArticles(){
        
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
                
                for(ArticleDTO articleDTO : articleDTOs){
                    articlesMap.put(articleDTO.getId(), articleDTO);
                    Container singleArticleBoxNews;
                    try {
                        singleArticleBoxNews = createNewsBoxContainer(articleDTO);
                        NewsSectionForm.this.add(singleArticleBoxNews);                
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                NewsSectionForm.this.revalidate();
                NewsSectionForm.this.show();
            }
        };

        req.setPost(false);
        req.setHttpMethod("GET");
        req.setUrl(dataUrl);//"http://ashdod10.co.il/get/k2/items?cats=3&limit=10");                       

        NetworkManager.getInstance().addToQueue(req);
    }
    
    
}
