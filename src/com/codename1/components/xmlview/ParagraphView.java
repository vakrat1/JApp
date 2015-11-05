/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.components.xmlview;

import com.codename1.components.SpanLabel;
import com.codename1.ui.Component;
import com.codename1.xml.Element;
import java.util.Iterator;

/**
 *
 * @author shannah
 */
public class ParagraphView implements XMLView.ViewFactory {

    public Component createView(Element el, XMLView context) {
        String text = getText(el);
        SpanLabel label = new SpanLabel(text);
        String uiid = el.getAttribute("uiid");
        if (uiid == null) {
            uiid = "ParagraphView";
        }
        label.setTextUIID(uiid+"Text");
        label.setUIID(uiid);
        
        return label;
    }
    static int counter = 0;
    private void getText(Element root, StringBuilder sb) {
        counter++;
        try{
        if (root.isTextElement()) {
            sb.append(root.getText());
        } else {
            if (root.getNumChildren() > 0){
                Iterator<Element> it = root.iterator();
                while (it.hasNext()) {
                    getText(it.next(), sb);
                }
            }
        }
        }catch (NullPointerException e){
            e.printStackTrace();            
        }
    }
    
    private String getText(Element root) {
        StringBuilder sb = new StringBuilder();
        getText(root, sb);
        return sb.toString();
    }
    
}
