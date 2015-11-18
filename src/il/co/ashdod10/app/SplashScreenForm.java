/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package il.co.ashdod10.app;

import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.animations.CommonTransitions;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.util.UITimer;
import java.io.IOException;

/**
 *
 * @author yaniv
 */
public class SplashScreenForm {
    
    
    /**
     * The splash screen is relatively bare bones. Its important to have a splash screen for iOS 
     * since the build process generates a screenshot of this screen to speed up perceived performance
     */
    public static void showSplashScreen() throws IOException {
        final Form splash = new Form();
        
        // a border layout places components in the center and the 4 sides.
        // by default it scales the center component so here we configure
        // it to place the component in the actual center
        BorderLayout border = new BorderLayout();
        border.setCenterBehavior(BorderLayout.CENTER_BEHAVIOR_CENTER_ABSOLUTE);
        splash.setLayout(border);
        
        // by default the form's content pane is scrollable on the Y axis
        // we need to disable it here
        splash.setScrollable(false);
//        Label title = new Label("AHSDOD 10");
        
        // The UIID is used to determine the appearance of the component in the theme
//        title.setUIID("SplashTitle");
//        Label subtitle = new Label("By Codename One");
//        subtitle.setUIID("SplashSubTitle");
        
//        splash.addComponent(BorderLayout.NORTH, title);
//        splash.addComponent(BorderLayout.SOUTH, subtitle);

        Label splashLabel = new Label(il.co.ashdod10.util.Util.getImage("/splash-screen_1080X1920.png"));
        splashLabel.setUIID("splashLabel");

        // a layered layout places components one on top of the other in the same dimension, it is
        // useful for transparency but in this case we are using it for an animation
        final Container center = new Container(new LayeredLayout());
        center.addComponent(splashLabel);
        
        splash.addComponent(BorderLayout.CENTER, center);
                
        splash.show();
        splash.setTransitionOutAnimator(CommonTransitions.createCover(
                CommonTransitions.SLIDE_VERTICAL, true, 800));

        // postpone the animation to the next cycle of the EDT to allow the UI to render fully once
        Display.getInstance().callSerially(new Runnable() {
            public void run() {
                // We replace the layout so the cards will be laid out in a line and animate the hierarchy
                // over 2 seconds, this effectively creates the effect of cards spreading out
                center.setLayout(new BoxLayout(BoxLayout.X_AXIS));
                center.setShouldCalcPreferredSize(true);
                splash.getContentPane().animateHierarchy(2000);

                // after showing the animation we wait for 2.5 seconds and then show the game with a nice
                // transition, notice that we use UI timer which is invoked on the Codename One EDT thread!
                new UITimer(new Runnable() {
                    public void run() {
                        try{
                            MainNewsForm mainNewsForm = new MainNewsForm();
                            mainNewsForm.init();            
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }).schedule(2500, false, splash);
            }
        });
    }
    
}
