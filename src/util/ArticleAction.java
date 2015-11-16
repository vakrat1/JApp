/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import com.codename1.ui.Form;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.mycompany.myapp.ArticleForm;
import com.mycompany.myapp.NewsSectionForm;
import dto.ArticleDTO;
import java.util.Map;
import rest.RestConsumer;

/**
 *
 * @author CHAYON
 */
public class ArticleAction implements ActionListener{
        
//    private String id;
    private ArticleDTO articleDTO;
    private Form previousForm;
    private ArticleForm articleForm;

    public ArticleAction(ArticleDTO articleDTO, Form previousForm){
//        this.id = id;
        this.articleDTO = articleDTO;
        this.previousForm = previousForm;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        try {            
            if (articleForm == null){
                articleForm = new ArticleForm(previousForm);
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