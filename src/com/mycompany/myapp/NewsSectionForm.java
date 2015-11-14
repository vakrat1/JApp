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
import util.DataBuilder;
import util.Util;

/**
 *
 * @author yaniv
 */
public class NewsSectionForm extends Form implements DataDependedForm{
    
    private Map<String, ArticleDTO> articlesMap = new HashMap<>();
    
    private Map<String, ArticleForm> articleFormsMap = new HashMap<>();
    
    private List<ArticleDTO> articleDTOs = new ArrayList<>();
    
    private String pageId;
    
    private String dataUrl;
    
    private String componentType;
            
    private String categoryId;
    
    private String limit;
    
    private String page;
    
    private Form previousForm;
    
    public NewsSectionForm(String title, String pageId, String componentType, 
            String categoryId, String limit, String page, Form previousForm){
        this.setTitle(title);
        this.pageId = pageId;
        this.componentType = componentType;
        this.categoryId = categoryId;
        this.limit = limit;
        this.page = page;
        this.previousForm = previousForm;
    }
    
    
    
    public void init() throws Exception{  
        
        this.dataUrl = "http://ashdod10.co.il/get/" + 
                            componentType + "/items?cats=" +
                             categoryId + "&limit="+ limit;
        
        util.Util.setFormTitle(this);
        
        Container sectionBox = getContentPane();
        
        //TODO - might need to move this referesh code from here
        sectionBox.addPullToRefresh(new Runnable() {
            @Override
            public void run() {
            }
        });        
        if(previousForm != null){
            setBackCommand(new Command("Back") {
                public void actionPerformed(ActionEvent ev) {
                    previousForm.showBack();
                } 
            });
            setScrollableY(true);
        }        
        DataBuilder.downloadArticles(dataUrl, this);
    }
    
    //this is a callback method that is being invoked after data is being 
    //downloaded from the server
    public void postDataDownload(Object data){
        articleDTOs = (List<ArticleDTO>)data;
        
        for(ArticleDTO articleDTO : articleDTOs){
            articlesMap.put(articleDTO.getId(), articleDTO);
            Container singleArticleBoxNews;
            NewsSectionForm.ArticleAction articleAction = new NewsSectionForm.ArticleAction(articleDTO.getId());
            try {
                singleArticleBoxNews = DataBuilder.createNewsBoxContainer(articleDTO, articleAction);
                NewsSectionForm.this.add(singleArticleBoxNews);                
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        NewsSectionForm.this.revalidate();
        NewsSectionForm.this.show();
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
    
    
    public Map<String, ArticleDTO> getArticlesMap() {
        return articlesMap;
    }

    public void setArticlesMap(Map<String, ArticleDTO> articlesMap) {
        this.articlesMap = articlesMap;
    }

    public Map<String, ArticleForm> getArticleFormsMap() {
        return articleFormsMap;
    }

    public void setArticleFormsMap(Map<String, ArticleForm> articleFormsMap) {
        this.articleFormsMap = articleFormsMap;
    }

    public List<ArticleDTO> getArticleDTOs() {
        return articleDTOs;
    }

    public void setArticleDTOs(List<ArticleDTO> articleDTOs) {
        this.articleDTOs = articleDTOs;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getDataUrl() {
        return dataUrl;
    }

    public void setDataUrl(String dataUrl) {
        this.dataUrl = dataUrl;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public Form getPreviousForm() {
        return previousForm;
    }

    public void setPreviousForm(Form previousForm) {
        this.previousForm = previousForm;
    }

     
    
}
