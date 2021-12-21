/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author 303426
 */
public class Translate {

    private String text;

    public Translate(String encoded) {

        setText(encoded);
       // System.out.println(text);
    }

    public void setText(String code) {
        String result = "";
       
        for (int i = 0; i < code.length(); i++) {
            char asciiValue = code.charAt(i);
            asciiValue -=1;

            result += Character.toString(asciiValue);
        }
        
        this.text = result;
    }
    
    public String getText(){
        return this.text;
    }
}
