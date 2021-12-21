
import java.util.ArrayList;

/*
 * NightShade
 */
/**
 *
 * @author 303426
 */
public class Player extends Item {

    protected ArrayList<Item> inventory = new ArrayList<Item>();

    public Player(String n, String d, int l) {
        super(n, d, l); //calls the item constructer with these

    }

    public String toString() {
        return ("Player is in" + getLocation());
    }

    public void addToInventory(Item i) {
        inventory.add(i);

    }

    public void showInventory() {
        if (inventory.size() == 0) {
            System.out.println("Nothing to display");

        } else {
            System.out.println("backPack contents: ");

            for (Item i : inventory) {

                Translate t = new Translate(i.getName());
                System.out.print(t.getText() + " ");

                
            }

            System.out.println("");
        }
       
    }

    
}
