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
//    
//    public SectionNewsBox(Hashtable<String, String> localeMap){
//        this.localeMap = localeMap;
//    }
    
    private List<ArticleDTO> articleDTOs;
    
    private String pageId;
    
    private String dataUrl;
    
    public NewsSectionForm(String title, String pageId, String dataUrl,
            Form previousForm){
        this.setTitle(title);
        this.pageId = pageId;
        this.dataUrl = dataUrl;
        setBackCommand(new Command("Back") {
            public void actionPerformed(ActionEvent ev) {
                previousForm.showBack();
            } 
        });
        setScrollableY(true);
    }
    
    public void load() throws Exception{  
        
        Container sectionBox = getContentPane();
        
        //TODO - ,ight need to move this referesh code from here
        sectionBox.addPullToRefresh(new Runnable() {
            @Override
            public void run() {
            }
        });
        
        for(ArticleDTO articleDTO : articleDTOs){
            articlesMap.put(articleDTO.getId(), articleDTO);
            Container singleArticleBoxNews = createNewsBoxContainer(articleDTO);
            sectionBox.add(singleArticleBoxNews);                
        }
        
        this.revalidate();
        this.show();
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
        private Form form;
        
        public ArticleAction(String id){
            this.id = id;
            this.form = form;
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            ArticleDTO articleDTO = articlesMap.get(id);
            try {
                    ArticleForm articleForm = new ArticleForm();
                    form.getContentPane().removeAll();
                    form.getContentPane().add(createArticleBox(articleDTO));
                    form.revalidate();
                } catch (Exception ex) {
                    System.out.println("ERROR: " + RestConsumer.class.getName());
                    ex.printStackTrace();
                    
                }
                form.revalidate();
        }
        
        private Container createArticleBox(/*Hashtable<String, String> localeMap, */
            ArticleDTO articleDTO) throws Exception{

            final Container articleBox = new Container(new BoxLayout(BoxLayout.Y_AXIS));
            articleBox.setUIID("newsBox");
            articleBox.setScrollableY(true);
            articleBox.addPullToRefresh(new Runnable() {
                @Override
                public void run() {
                }
            });

            TextArea titleTA = new TextArea();
            titleTA.setUIID("titleLabel");
            String title = util.Util.parseHtmlSpecialTags(articleDTO.getTitle());
            titleTA.setText(title);
            titleTA.setEditable(false);      


            DefaultXMLViewKit kit = new DefaultXMLViewKit();

            XMLView contentViewer = new XMLView(UIManager.initFirstTheme("/theme"));
            kit.install(contentViewer);
    //        content.load("http://dev.weblite.ca/demo-xmlview.xml", new Callback<Element>() {

            String content = articleDTO.getContent();
            content = Util.parseContentElement(content);
            content = util.Util.parseHtmlSpecialTags(content);


            contentViewer.loadXmlAsString(content, new Callback<Element>() {

                public void onSucess(Element value) {
                    articleBox.revalidate();
                }

                public void onError(Object sender, Throwable err, int errorCode, String errorMessage) {                
                }            
            });

            Container x = new Container(new BoxLayout(BoxLayout.X_AXIS));
            articleBox.add(util.Util.getImageContainer(null, articleDTO, 1, 2));
            articleBox.add(titleTA);
            articleBox.add(contentViewer);
            articleBox.revalidate();

            return articleBox;
        }
    }
    
    //load form data
    private void loadArticles(String url){
        
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
    
    
}
