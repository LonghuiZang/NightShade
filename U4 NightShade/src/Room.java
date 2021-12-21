
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author 303426
 */
public class Room extends Item {

    private int exits[] = new int[6];
    private ArrayList<String> items = new ArrayList<String>();

    public Room(String name, String description, int roomNumber,
            int north, int south, int east, int west, int up, int down) {
        super(name, description, roomNumber);
        exits[0] = north;
        exits[1] = south;
        exits[2] = east;
        exits[3] = west;
        exits[4] = up;
        exits[5] = down;
    }

    public String getExits() {
        String exits = "";
        String[] exitValues = {"N", "S", "E", "W", "U", "D"};;
        for (int i = 0; i < this.exits.length; i++) {
            if (this.exits[i] > 0) {
                exits += " " + exitValues[i];
            }
        }
        return exits;

    }

    public int getExit(int dir){
        return exits[dir];
    }
    
    public String toString(){
        return getDescription() + "|" + getExits();
    }
}
