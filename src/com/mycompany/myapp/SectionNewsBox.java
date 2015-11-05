/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myapp;

import com.codename1.components.SpanLabel;
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
import dto.ArticleDTO;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.List;

/**
 *
 * @author yaniv
 */
public class SectionNewsBox extends Form{
    
//    private Hashtable<String, String> localeMap;
//    
//    public SectionNewsBox(Hashtable<String, String> localeMap){
//        this.localeMap = localeMap;
//    }
    
    public Container createNewsBoxContainer(List<ArticleDTO> articleDTOs) throws Exception{
        Font myFont = null;
//        if(Font.isTrueTypeFileSupported()) { 
//            myFont = Font.createTrueTypeFont("Bsthebrew", "Bsthebrew.ttf"); 
//            myFont = myFont.derive(15, Font.STYLE_PLAIN); 
//            // do something with the font 
//        } 
        
        //get the language
        String local = L10NManager.getInstance().getLanguage();
        
        //define listener
        ArticalAction articalAction = new ArticalAction();        
        
        Container sectionBox = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        sectionBox.setScrollableY(true);
        sectionBox.addPullToRefresh(new Runnable() {

            @Override
            public void run() {
                
                
            }
        });
        
        
        for(ArticleDTO articleDTO : articleDTOs){
            NewsBoxContainer singleArticleBoxNews = 
                    new NewsBoxContainer().createNewsBoxContainer(articleDTO);
            sectionBox.add(singleArticleBoxNews);                
        }
        
        //seperator
//        Label sectionTitle = new Label(); 
//        sectionTitle.setUIID("sectionSeperatorTitle");
//        sectionTitle.setText("Sport");// " + localeMap.get("sport"));
//        sectionBox.add(sectionTitle);
//        
//        
//        for (int i = 0; i < 3; i++) {
//            Container singleArticleBoxNews = 
//                    new SectionNewsForm().createNewsBoxContainer(localeMap);
//            sectionBox.add(singleArticleBoxNews);                
//        }
//        
        return sectionBox;
    }
    
    
    
    
    class ArticalAction implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent evt) {
            Dialog.show("Notification", "callback is called", "OK", "Cancel");
        }
        
    }
    
}
