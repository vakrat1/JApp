/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myapp;

import com.codename1.components.SpanLabel;
import com.codename1.components.xmlview.DefaultXMLViewKit;
import com.codename1.components.xmlview.XMLView;
import com.codename1.io.services.ImageDownloadService;
import com.codename1.l10n.L10NManager;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.Font;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.TextArea;
import com.codename1.ui.URLImage;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.Layout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;
import com.codename1.util.Callback;
import com.codename1.xml.Element;
import dto.ArticleDTO;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.StringTokenizer;

/**
 *
 * @author yaniv
 */
public class NewsBoxContainer extends Container{
    
    public NewsBoxContainer(){
        super();
    }
    
    
    public NewsBoxContainer(Layout layout){
        super(layout);
    }
    
    public NewsBoxContainer createNewsBoxContainer(/*Hashtable<String, String> localeMap, */
            ArticleDTO articleDTO) throws Exception{
        Font myFont = null;
//        if(Font.isTrueTypeFileSupported()) { 
//            myFont = Font.createTrueTypeFont("Bsthebrew", "Bsthebrew.ttf"); 
//            myFont = myFont.derive(15, Font.STYLE_PLAIN); 
//            // do something with the font 
//        } 
        
        String local = L10NManager.getInstance().getLanguage();
        
        ArticleAction articleAction = new ArticleAction();
        
        
        
        
        NewsBoxContainer newsBox = new NewsBoxContainer(new BoxLayout(BoxLayout.Y_AXIS));
        newsBox.setUIID("newsBox");
        
//        Container contentPane = new Container(new BoxLayout(BoxLayout.X_AXIS));
        
//        contentPane.add(getImageContainer(articleAction, articleDTO));
        
//        Container titleCont = new Container();
//        TextArea title = new TextArea(localeMap.get("title"));
        TextArea title = new TextArea(articleDTO.getTitle());
        title.setEditable(false);
        title.addPointerReleasedListener(articleAction);
//        title.setRTL(true);
//        title.getUnselectedStyle().setFont(myFont);
        title.setUIID("titleLabel");
//        titleCont.add(BorderLayout.EAST, title);
//        contentPane.add(title);
        
        DefaultXMLViewKit kit = new DefaultXMLViewKit();
        
        XMLView content = new XMLView(UIManager.initFirstTheme("/theme"));
        kit.install(content);
//        content.load("http://dev.weblite.ca/demo-xmlview.xml", new Callback<Element>() {
        
        String xmlViewContent = parseContentElement(articleDTO.getContent());
        
        content.loadXmlAsString(xmlViewContent, new Callback<Element>() {

            public void onSucess(Element value) {
                newsBox.revalidate();
            }

            public void onError(Object sender, Throwable err, int errorCode, String errorMessage) {
                
            }
            
        });
        
        
//        TextArea content = new TextArea();
//        content.setEditable(false);
//        subtitle.setGrowByContent(true);        
//        subtitle.setGrowLimit(3);
//        subtitle.setRTL(true);
//        subtitle.getTextUnselectedStyle().setAlignment(Component.RIGHT);
//        subtitle.setTextBlockAlign(Component.RIGHT);
//        subtitle.getStyle().setAlignment(Component.RIGHT);
//        subtitle.getAllStyles().setFont(myFont);
//        content.setUIID("SubtitleTextArea");
//        content.setUIID("Label");
//        content.setColumns(20);
//        content.setText(articleDTO.getContent());
        
//        subtitle.setText(localeMap.get("subtitle"));
//        content.addPointerReleasedListener(articleAction);

//        subtitle.setRTL(true);
//        contentPane.add(content);
                
        
        newsBox.add(title);
        newsBox.add(getImageContainer(articleAction, articleDTO));
        newsBox.add(content);
        newsBox.revalidate();
//        newsContainer.add(contentPane);
//        newsContainer.add(getImageContainer());
//        newsContainer.setHeight(5);
        
        
//        form.add(newsBox);
//        form.setHeight(5);
//        form.getStyle().setAlignment(Component.RIGHT);
//        form.repaint();
//        form.revalidate();
//        return form;
        return newsBox;
    }
    
    
    private String parseContentElement(String content){
        String parsedContent = "<?xml version=\"1.0\"?>\n" +
                                "<doc>\n" +
                                "<body>\n";
        //first part ends with <p>
        String _content = content;
        int index = _content.indexOf("<p>");
        String paragraph = _content.substring(0, index);
        parsedContent += "<p>" + paragraph +"</p>\n";
        
        _content = _content.substring(index);        

        //from second line and on        
        boolean endOfData = false;
        while (!endOfData){
            index = _content.indexOf("</p>");
            if (index == -1){
                endOfData = true;
                parsedContent += "<p>" + removeStylingContent(_content) + "</p>\n";
                continue;
            }
            paragraph = _content.substring(0, index+4);
            _content = _content.substring(index+4);
            System.out.println(paragraph);
            System.out.println(_content);
            
            if (paragraph.startsWith("\r\n<p><img")){
                paragraph = paragraph.substring(5, paragraph.indexOf("</p>"));
                
                String paragraph1 = paragraph.substring(0, paragraph.indexOf("images"));
                String paragraph2 = paragraph.substring(paragraph.indexOf("images")+6);
                
                paragraph = paragraph1 + "http://www.ashdod10.co.il/images" + 
                        paragraph2;
                
                
                
               
            }
            paragraph = removeStylingContent(paragraph);
            parsedContent += "<p>"+paragraph +"</p>\n";
            
//            parsedContent += "<p>" + paragraph;
//            if (paragraph.endsWith("</p>") == false){
//                parsedContent += "</p>";
//            }
//            parsedContent += "\n";
        }
        
//        StringTokenizer st = new StringTokenizer(content, "<p>");
//        while (st.hasMoreElements()) {
//            parsedContent += "<p>" + (String)st.nextElement() + "</p>\n";
//        }
        
        
        parsedContent += "</body>\n" +
                        "</doc>";
        
        return parsedContent;
    }
    
    private String removeStylingContent(String content){
        System.out.println("removeStylingContent():: content BEFORE" + content);
        
        String _content = content;
        if (content.startsWith("\r\n")){
            _content = content.substring(2);
        }
        if (content.endsWith("\r\n")){
            _content = _content.substring(0,_content.length()-2);
        }
        
        int startP = _content.indexOf("<p>");
        int endP = _content.indexOf("<p>");
        if (startP > -1 && endP > -1){
            _content = _content.substring(_content.indexOf("<p>")+3, 
                _content.indexOf("</p>"));
        }
        
        int index1 = _content.indexOf("<");
        if (index1 >= 0){
            int index2 = _content.indexOf(">");            
            String tag = _content.substring(index1+1, index2);
            
            int startTagIndex = _content.indexOf("<"+tag+">");
            int endTagIndex = _content.indexOf("</"+tag+">");
            String part1 = _content.substring(0,startTagIndex);
            if (endTagIndex == -1){
                String part2 = _content.substring(startTagIndex+2+tag.length());
                _content = part1 + part2;
            }
            else{            
                String part2 = _content.substring(startTagIndex+2+tag.length(),endTagIndex);
                String part3 = _content.substring(endTagIndex+3+tag.length());
                _content = part1 + part2 + part3;
            }
                    
            System.out.println("removeStylingContent():: content AFTER" + content);
            
            _content = removeStylingContent(_content);
        }
        
        return _content;
    }
    
    
    
    private Container getImageContainer(ActionListener articalAction, 
            ArticleDTO articleDTO) throws IOException{
        
        Container imageContainer = new Container();        
        InputStream in = Display.getInstance().getResourceAsStream(Form.class, 
                "/news_placeholder.jpg");
        String imageUrl = articleDTO.getImgURL();
        String imageUrl1 = imageUrl.substring(0, imageUrl.indexOf("images"));
        String imageUrl2 = imageUrl.substring(imageUrl.indexOf("images"));
        imageUrl = imageUrl1 +"/"+imageUrl2;
        String imageName = imageUrl.substring(imageUrl.indexOf("_")+1);
        Image img = EncodedImage.create(in);
        img = img.scaledWidth(Display.getInstance().getDisplayWidth());
//        URLImage image = URLImage.createToStorage(img, 
//                imageName, imageUrl, URLImage.RESIZE_SCALE_TO_FILL);        
        
        Label label = new Label(img);
        label.setUIID("ImageBox");
        imageContainer.add(label);
        
        
        ImageDownloadService.createImageToStorage(imageUrl, label, imageName, 
                new Dimension(Display.getInstance().getDisplayWidth(), Display.getInstance().getDisplayHeight()/2));
        
//                "http://www.ashdod10.co.il/images/cache/1ad9088df2ee93b3f6cc0567d35b0a7c_w350_h263_cp_sc.jpg",
                
//        image.fetch();
        
        label.addPointerReleasedListener(articalAction);
        
        return imageContainer;
        
    }
    
    
    class ArticleAction implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent evt) {
            Dialog.show("Notification", "callback is called", "OK", "Cancel");
        }
        
    }
    
}

