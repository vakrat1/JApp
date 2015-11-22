package il.co.ashdod10;


import com.codename1.push.Push;
import com.codename1.push.PushCallback;
import com.codename1.ui.Command;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.TextArea;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import il.co.ashdod10.app.SplashScreenForm;
import java.io.IOException;
import java.util.Hashtable;
import il.co.ashdod10.parser.AppStructureParser;
import il.co.ashdod10.rest.RestConsumer;

public class Ashdod10 implements PushCallback{
    
    //Server side Push API Key - AIzaSyAEEqlL2T1UfpDmtmpw72Ii1ZZJL543TEs
    
    private String GOOGLE_MESSAGING_API_KEY = "926251658740";//Ashdod10 - Google project
    private static String deviceId1 = "cn1-gcm-APA91bHcn7kdt5DsmXFAP_uj3liLL61ECy7uwHcCAx1Q600Fya1l7T7C6j2of6rDUzKbKo-Ngyq6ZciHdaQUTQjt8n6dgdUh5TGJOC49AY4LxDr4uBJDeznfqmfiqY6rDCtNqKXLULxZ";
//"876789427701";//12 digits taken from Google messaging

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
    
//    TextArea ta;
    
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
        //ta.setText(pushKey);
        //Dialog.show("registeredForPush", "DeviceId: " + pushKey, "OK", "Cancel");
    }
    
    
    public void pushRegistrationError(String error, int errorCode) {
        Dialog.show("ERROR", "registeredForPush():: Device Id is: " + error,
                "OK", "Cancel");
    }
    
//    private void testPush(){
//        ta = new TextArea();
//        Form form = new Form();
//        form.setLayout(new BorderLayout());
//        form.add(BorderLayout.CENTER, ta);
//        form.show();
//
//        Display.getInstance().scheduleBackgroundTask(new Runnable() {
//            public void run() {
//                try {
//                    Hashtable meta = new Hashtable();
//                    meta.put(com.codename1.push.Push.GOOGLE_PUSH_KEY, GOOGLE_MESSAGING_API_KEY);
//                    Display.getInstance().registerPush(meta, false);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }

}
