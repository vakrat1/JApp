/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package il.co.ashdod10.app;

import com.codename1.components.xmlview.DefaultXMLViewKit;
import com.codename1.components.xmlview.XMLView;
import com.codename1.ui.Command;
import com.codename1.ui.Container;
import com.codename1.ui.Form;
import com.codename1.ui.TextArea;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.util.Callback;
import com.codename1.xml.Element;
import il.co.ashdod10.dto.ArticleDTO;
import java.io.IOException;
import il.co.ashdod10.util.Util;

/**
 *
 * @author CHAYON
 */
public class ArticleForm extends Form{
    
    private Container contentPane;
    
    private Form previousForm;
    
    public ArticleForm(Form previousForm){        
        this.contentPane = getContentPane(); 
        this.previousForm = previousForm;
    }
    
    public void init() throws IOException{        
        contentPane.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        il.co.ashdod10.util.Util.setFormTitle(this);
        setUIID("newsBox");
        setScrollableY(true);
        setBackCommand(new Command("Back") {
            public void actionPerformed(ActionEvent ev) {
                previousForm.showBack();
            } 
        });
        addPullToRefresh(new Runnable() {
            @Override
            public void run() {
            }
        });
    }
    
    
    public void createArticleBox(ArticleDTO articleDTO) throws Exception{

        TextArea titleTA = new TextArea();
        titleTA.setUIID("titleLabel");
        String title = il.co.ashdod10.util.Util.parseHtmlSpecialTags(articleDTO.getTitle());
        titleTA.setText(title);
        titleTA.setEditable(false);      


        DefaultXMLViewKit kit = new DefaultXMLViewKit();

        XMLView contentViewer = new XMLView(UIManager.initFirstTheme("/theme"));
        kit.install(contentViewer);
//        content.load("http://dev.weblite.ca/demo-xmlview.xml", new Callback<Element>() {

        String content = articleDTO.getContent();
        content = Util.parseContentElement(content);
        content = il.co.ashdod10.util.Util.parseHtmlSpecialTags(content);


        contentViewer.loadXmlAsString(content, new Callback<Element>() {

            public void onSucess(Element value) {
                revalidate();
            }

            public void onError(Object sender, Throwable err, int errorCode, String errorMessage) {                
            }            
        });

        Container x = new Container(new BoxLayout(BoxLayout.X_AXIS));
        contentPane.add(il.co.ashdod10.util.Util.getImageContainer(null, articleDTO, 1, 2));
        contentPane.add(titleTA);
        contentPane.add(contentViewer);
        contentPane.revalidate();
    }
    
}
