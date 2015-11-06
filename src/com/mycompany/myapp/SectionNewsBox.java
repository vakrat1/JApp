/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myapp;

import com.codename1.components.SpanLabel;
import com.codename1.components.xmlview.DefaultXMLViewKit;
import com.codename1.components.xmlview.XMLView;
import com.codename1.l10n.L10NManager;
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
public class SectionNewsBox extends Form{
    
    private Map<String, ArticleDTO> articlesMap = new HashMap<>();
//    
//    public SectionNewsBox(Hashtable<String, String> localeMap){
//        this.localeMap = localeMap;
//    }
    
    private List<ArticleDTO> articleDTOs;
    
    private String pageId;
    
    public SectionNewsBox(List<ArticleDTO> articleDTOs, String pageId){
        this.articleDTOs = articleDTOs;
        this.pageId = pageId;
    }
    
    public Container createNewsBoxContainer(Form form) throws Exception{  
        
        Container sectionBox = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        sectionBox.setScrollableY(true);
        sectionBox.addPullToRefresh(new Runnable() {
            @Override
            public void run() {
            }
        });
        
        for(ArticleDTO articleDTO : articleDTOs){
            articlesMap.put(articleDTO.getId(), articleDTO);
            Container singleArticleBoxNews = createNewsBoxContainer(form, articleDTO);
            sectionBox.add(singleArticleBoxNews);                
        }
 
        return sectionBox;
    }
    
    private Container createNewsBoxContainer(Form form,
            ArticleDTO articleDTO) throws Exception{
                
        ArticleAction articleAction = new ArticleAction(articleDTO.getId(), form);
        
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
        
        public ArticleAction(String id, Form form){
            this.id = id;
            this.form = form;
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            ArticleDTO articleDTO = articlesMap.get(id);
            try {
                    SectionNewsBox sectionNewsBox = 
                            new SectionNewsBox(articleDTOs, pageId);
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
    
    
}
