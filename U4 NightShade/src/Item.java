/*
 * Came from Sams Learn game programming in Visual Basic in 21 days
 */

/**
 *
 * @author Longhui Zhang
 */
public class Item {

    private String name;
    private String description;
    private int location;

    public Item(String name, String description,int location){
        this.name = name;
        this.description = description;
        this.location = location;
    }
    
    public Item(){
        
    }
    public Item(String name, int location){
        this.name = name;
        this.location = location;
        this.description = "";
    }
    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public int getLocation() {
        return this.location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(int location) {
        this.location = location;
    }
    
    public String toString(){
        if (!getDescription().equals("")){           
            return("Item: " + getName() + "\nDescription " + getDescription() + "\nLocation "+ getLocation());
            
        }
        return ("Item: " + getName() + "\nLocation: " + getLocation());
        
    }
}
