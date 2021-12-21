
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author 303426
 */
public class Game {

    protected Player player;
    protected String[] allItems;
    protected Item[] codedItems;
    protected Room[] rooms;
    public final int IN_INVENTORY = -1;
    

    //protected means accessible to all subclasses but no other classes
    //accessable from nightshade
    public static void main(String[] args) {

        Game nightshade = new NightShade();
                   
            

    }

    public Item getItems(int location){      
        return codedItems[0];
    }
    
    protected Player setUpPlayer() {
        Scanner in = new Scanner(System.in);

        String name = in.nextLine();
        return new Player(name, "game player", -1);

    }



    public Game() {
       
               
        
    }
    
    public void showVisibleItems(int currRoom){
        String visibleItems = "";
        //take coded items
        //read it
       Translate t;
       String item;
        
        for (int i = 0; i < codedItems.length; i++) {
            
            if (Math.abs(codedItems[i].getLocation()) == currRoom){
                t = new Translate(codedItems[i].getName());
                //visibleItems += codedItems[i].getName() + " ";
                visibleItems += t.getText() + " ";
            }
        }
        if (visibleItems.length() == 0){
            System.out.println("Nothing to see here");
        }
        else{
         System.out.println(visibleItems);   
        }           
        
    }
    

    
    
}
