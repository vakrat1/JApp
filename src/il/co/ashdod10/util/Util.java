/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package il.co.ashdod10.util;

import com.codename1.io.services.ImageDownloadService;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.geom.Dimension;
import com.codename1.util.StringUtil;
import il.co.ashdod10.dto.ArticleDTO;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author CHAYON
 */
public class Util {
    
    public static Component getComponentSeparator(String title){
        Label sep = new Label(title);
        sep.setUIID("sectionSeperatorTitle");
        return sep;        
    }
    
    public static String parseHtmlSpecialTags(String content){
        String _content = content;
//        StringBuffer cs1 = new StringBuffer();
//        StringBuffer cs2 = new StringBuffer("\n");
////        _content.re
////                "&nbsp;", "\n");
        _content = StringUtil.replaceAll(_content, "&nbsp;", " ");
        _content = StringUtil.replaceAll(_content, "&quot;", "\"");        
        
        return _content;
    }
    
    public static void setFormTitle(Form form) throws IOException{
        InputStream in = Display.getInstance().getResourceAsStream(Form.class, 
                "/ashdod10_1.png");
        Image img = EncodedImage.create(in);
        form.getTitleComponent().setIcon(img);
        form.revalidate();
    }
    
    public static Image getImage(String imageName) throws IOException{
        InputStream in = Display.getInstance().getResourceAsStream(Form.class, 
                imageName);
        return EncodedImage.create(in);
        
    }
    
    public static Container getImageContainer(ActionListener articalAction, 
            ArticleDTO articleDTO, int widthRatio, int heightRatio) throws IOException{
        
        Container imageContainer = new Container();        
        InputStream in = Display.getInstance().getResourceAsStream(Form.class, 
                "/news_placeholder.jpg");
        String imageUrl = articleDTO.getImgURL();
        String imageUrl1 = imageUrl.substring(0, imageUrl.indexOf("images"));
        String imageUrl2 = imageUrl.substring(imageUrl.indexOf("images"));
        imageUrl = imageUrl1 +"/"+imageUrl2;
//        String imageName = imageUrl.substring(imageUrl.indexOf("_")+1);
        String imageName = imageUrl.substring(imageUrl.lastIndexOf("/")+1);
        Image img = EncodedImage.create(in);
        img = img.scaledWidth(Display.getInstance().getDisplayWidth()/widthRatio);
        img = img.scaledHeight(Display.getInstance().getDisplayHeight()/heightRatio);
//        URLImage image = URLImage.createToStorage(img, 
//                imageName, imageUrl, URLImage.RESIZE_SCALE_TO_FILL);        
        
        Label label = new Label(img);
        label.setUIID("ImageBox");
        imageContainer.add(label);
        
        
        ImageDownloadService.createImageToStorage(imageUrl, label, imageName, 
                new Dimension(Display.getInstance().getDisplayWidth()/widthRatio,
                        Display.getInstance().getDisplayHeight()/heightRatio));
        
//                "http://www.ashdod10.co.il/images/cache/1ad9088df2ee93b3f6cc0567d35b0a7c_w350_h263_cp_sc.jpg",
                
        if (articalAction != null){
            label.addPointerReleasedListener(articalAction);
        }
        
        return imageContainer;
        
    }
    
    public static String addHeader(){
        return "<?xml version=\"1.0\"?>\n<doc>\n<body>\n";
    }
    
    public static String addFooter(){
        return "</body>\n</doc>";
    }
    
    public static String parseContentElement(String content){
        count++;
        
        String parsedContent = addHeader();
                
        //first part ends with <p>
        String _content = content;
        int index = _content.indexOf("<p>");
        String paragraph = _content.substring(0, index);
        parsedContent += "<p>" + paragraph +"</p>\n";
        
        _content = _content.substring(index);        

        //from second line and on        
        boolean endOfData = false;
        boolean isImage = false;
        while (!endOfData){
            isImage = false;
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
                
                isImage = true;                
            }
            
            paragraph = removeStylingContent(paragraph);
            if (!isImage){
                parsedContent += "<p>"+paragraph +"</p>\n";
            }
            else{
                 parsedContent += paragraph;
            }
            
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
        
        
        parsedContent += addFooter();
        
        return parsedContent;
    }
     static int count = 0;
    
    private static String removeStylingContent(String content){        
        System.out.println("removeStylingContent():: content BEFORE" + content);
        
        if (content.startsWith("<img") || 
                content.startsWith("<video")){
            return content;
        }
        
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
    
}
