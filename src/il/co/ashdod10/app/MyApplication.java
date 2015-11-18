package il.co.ashdod10.app;


import com.codename1.push.Push;
import com.codename1.push.PushCallback;
import com.codename1.ui.Command;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import java.io.IOException;
import java.util.Hashtable;
import il.co.ashdod10.parser.AppStructureParser;
import il.co.ashdod10.rest.RestConsumer;

public class MyApplication implements PushCallback{
    
    private String GOOGLE_MESSAGING_API_KEY = "876789427701";//12 digits taken from Google messaging

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
    
    public void start()   {        
        try{
            if(current != null){
                current.show();
                return;
            }
            
            AppStructureParser.getInstance();
            
            SplashScreenForm.showSplashScreen();
            
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void stop() {
        current = Display.getInstance().getCurrent();
    }
    
    public void destroy() {
    }
    
    public ActionListener generalMenuCommandAction(){
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                
            }
        };
    }
    
    public void push(String value) {
        Dialog.show("DeviceId", "Push msg is: " + value, "OK", "Cancel");
        //called when user select the push message in the device
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void registeredForPush(String deviceId) {
        String pushKey = Push.getPushKey();        
    }
    
    
    public void pushRegistrationError(String error, int errorCode) {
        Dialog.show("ERROR", "registeredForPush():: Device Id is: " + error,
                "OK", "Cancel");
    }

}
