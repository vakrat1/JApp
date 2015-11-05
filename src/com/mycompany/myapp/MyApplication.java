package com.mycompany.myapp;


import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import java.io.IOException;
import java.util.Hashtable;
import rest.RestConsumer;

public class MyApplication {

    private Form current;
    private Resources theme;
    private Hashtable<String, String> localeMap = new Hashtable<>();

    public void init(Object context) {
        theme = UIManager.initFirstTheme("/theme");
        localeMap = theme.getL10N("Localization (L10N) 1", "iw");
        UIManager.getInstance().setBundle(localeMap);
        

        // Pro users - uncomment this code to get crash reports sent to you automatically
        /*Display.getInstance().addEdtErrorHandler(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                evt.consume();
                Log.p("Exception in AppName version " + Display.getInstance().getProperty("AppVersion", "Unknown"));
                Log.p("OS " + Display.getInstance().getPlatformName());
                Log.p("Error " + evt.getSource());
                Log.p("Current Form " + Display.getInstance().getCurrent().getName());
                Log.e((Throwable)evt.getSource());
                Log.sendLog();
            }
        });*/
    }
    
    public void start()  {
        
        if(current != null){
            current.show();
            return;
        }
        try{
            Form mainNewsForm = new Form();
            mainNewsForm.setScrollable(false);            
            BoxLayout boxLayout = new BoxLayout(BoxLayout.Y_AXIS);
            mainNewsForm.setLayout(boxLayout);
            
            
            
            RestConsumer.testLoadJSONUsingJSONParser(mainNewsForm);
            
//            mainNewsForm.add(sectionNews);
            
    //        hi.addComponent(new Label("News"));
            mainNewsForm.revalidate();
            mainNewsForm.show();
        }catch(Exception e){
            
        }
    }

    public void stop() {
        current = Display.getInstance().getCurrent();
    }
    
    public void destroy() {
    }

}
