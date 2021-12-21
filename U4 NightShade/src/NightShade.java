
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/*
 * Polymorphism
 */
/**
 *
 * @author Longhui Zhang
 */
public class NightShade extends Game {

    private int prevBoatRoom;
    private int currRoomNum;
    private Room currRoom;
    private int numItemsInInventory;
    private int numGlueIngredients;
    private boolean trollFainted;
    private String verb;
    private String noun;
    private String completeNoun;
    protected String[] allNouns;
    protected int[] allNounIndex;
    private final int ITEM_USED = -4;
    private final int MAX_ROOM_ITEMS = 4;
    private final int NOT_IN_GAME = 0;
    private final int NUM_ROOMS = 30;

    public NightShade() {
        System.out.println("Nightshade is a Text adventure game\n===================");
        //System.out.println(player);
        init();
    }

    protected Player setUpPlayer() {
        //System.out.println("nightsahde setupplayer");
        System.out.println("Enter the name of the adventurer who will help denny in his Nightshade quest");
        Player p = super.setUpPlayer();
        return p;
    }

    private void init() {

        System.out.println("............................");
        loadItems();

        System.out.println("setting up NightShade");
        player = setUpPlayer();
        addRooms();
        //will be altered later
        //codedItems = new Item[1];
        //codedItems[0] = new Item("Key", 1);

        trollFainted = false;
        numGlueIngredients = 0;
        prevBoatRoom = 12;
        currRoomNum = 5;

        numItemsInInventory = 0;
        currRoom = rooms[currRoomNum];

        updateGame();
        processInput();
        //System.out.println("CoddedItems: " + getItems(1));
    }

    private void addRooms() {
        System.out.println("...................");
        System.out.println("room info");
        prevBoatRoom = 12;
        File file = new File("src/RoomData.txt");
        //may remove this later as we know we need 30 elements 0 - 30
        //ignoreing the roomNums we dont need
        try {
            Scanner in = new Scanner(file);
            int numLines = 0;
            while (in.hasNextLine()) {
                in.nextLine();
                numLines++;
                //System.out.println(numLines);
            }
            rooms = new Room[31];
            in.close();

            in = new Scanner(file);
            for (int i = 0; i < numLines / 2; i++) {

                int location = in.nextInt();
                String name = in.next();
                String description = in.nextLine();

                int n = in.nextInt();
                int s = in.nextInt();
                int e = in.nextInt();
                int w = in.nextInt();
                int up = in.nextInt();
                int down = in.nextInt();

                rooms[location] = new Room(name, description, location, n, s, e, w, up, down);
                //System.out.println(rooms[location] + " " +  rooms[location].getLocation());

            }

        } catch (FileNotFoundException e) {
            System.out.println("Rooms file not found");
        }

    }

    private void loadItems() {

        File file = new File("src/ItemsEnum.txt");
        try {

            Scanner in = new Scanner(file);

            int numLines = 0;
            while (in.hasNextLine()) {
                in.nextLine();
                numLines++;

            }
            allItems = new String[numLines];
            in.close();

            in = new Scanner(file);
            numLines = 0;
            while (in.hasNextLine()) {
                allItems[numLines] = in.nextLine();
                numLines++;

            }

            in.close();

        } catch (FileNotFoundException e) {
            // happens if file isn't found
            System.out.println("Items File not found");

        }

        file = new File("src/codedItems.txt");
        try {
            Scanner in = new Scanner(file);
            int numLines = 0;

            while (in.hasNextLine()) {
                in.nextLine();
                numLines++;
                // System.out.println("test numLines" + numLines);
            }
            codedItems = new Item[numLines];
            in.close();
            in = new Scanner(file);
            for (int i = 0; i < numLines; i++) {

                String name = in.next();
                int location = in.nextInt();
                codedItems[i] = new Item(name, location);
                //System.out.println(codedItems[i]);

            }

            in.close();

        } catch (FileNotFoundException e) {

            System.out.println("Items File not Found");
        }

        file = new File("src/nounNames.txt");
        try {
            Scanner in = new Scanner(file);
            int numLines = 0;
            while (in.hasNextLine()) {
                in.nextLine();
                numLines++;
            }
            allNouns = new String[numLines];
            allNounIndex = new int[numLines];
            in.close();
            in = new Scanner(file);

            for (int i = 0; i < numLines; i++) {
                String noun = in.next();
                int indexValue = in.nextInt();
                allNouns[i] = new String(noun);
                allNounIndex[i] = indexValue;
                // System.out.println(allNouns[i] + " " + allNounIndex[i]);
            }

        } catch (FileNotFoundException e) {
            System.out.println("nounNames file not found");
        }
    }

    private void updateGame() {

        System.out.println("");
        System.out.println("---------------------");
        currRoom = rooms[currRoomNum];

        setRoomData(currRoomNum);

        System.out.println(currRoom);
        System.out.println("-------------");
        showVisibleItems(currRoomNum);
        System.out.println("---------------");
        player.showInventory();
        

        processInput();
        

    }

    private void processInput() {
        Scanner in = new Scanner(System.in);
        System.out.println("---------------------");
        System.out.println("? Help");
        System.out.println("! Exit");
        System.out.println("Enter a direction(N S E W U D)");

        String input = in.nextLine();

        //if the string is only length 1, it is a direction or a cry for help
        try {
            if (input.length() == 1) {

                if (input.equals("!")) {
                    System.out.println("Wakey wakey!");
                    System.exit(0);

                } else if (input.equals("?")) {
                    displayHelp();
                } //else move in the requested direction
                else {
                    movePlayer(input);
                }
            } else { //assume there is 2 words here

                in = new Scanner(input);
                verb = in.next().trim().toUpperCase();
                if (verb.equals("GO")) {
                    verb = "GO ";
                }
                
                noun = in.nextLine().trim().toUpperCase();
                

                verb = verb.substring(0, 3);
                completeNoun = noun.toString();
                noun = noun.substring(0, 3);
                //System.out.println("Verb " + verb + " Noun " + noun);
                processVerb();

            }
        } catch (Exception e) {
            System.out.println("needs both verb and noun");
            processInput();
        }
    }

    private void movePlayer(String dir) {
        int direction = getDirection(dir);
        if (direction != -1) {

            if (currRoom.getExit(direction) > 0) {
                currRoomNum = currRoom.getExit(direction);

            } else {

                System.out.println("\ndennny cant move that way");

            }
        } else {
            System.out.println("\nDenny doesn't understand");
        }
        updateGame();

    }

    private void displayHelp() {
        System.out.println("Help is not comeing anytime soon");
        updateGame();
        
    }

    private void processVerb() {
        //System.out.println("Verb: " + verb + " noun " + noun);
        switch (verb) {
            case "LOO":
            case "EXA":
                doExamine();
                break;
            case "GLU":
                doGlue();
                break;
            case "MOV":
                doMove();
                break;
            case "TAK":
            case "GET":
                doGet();
                break;
            case "GIV":
            case "PUT":
            case "DRO":
                doDrop();
                break;
            case "TAL":
                doTalk();
                break;
            case "DRI":
                doDrink();
                break;
            case "CLI":
                doClimb();
                break;
            case "SIN":
                doSing();
                break;
            case "ENT":
            case "GO":
            case "GO ":
                doGo();
                break;
            case "SAY":
                doSay();
                break;
            case "SMA":
            case "HIT":
                doHit();
                break;

            default:
                System.out.println("Denny doesnt' know how to do that.");
                updateGame();
        }
    }

    public void doTalk() {
        if (currRoomNum == 13 && (noun.equals("ELF") || noun.equals("FAM"))
                && codedItems[ItemsEnum.Items.WATERSHOES.ordinal()].getLocation() == NOT_IN_GAME) {
            System.out.println("They show Denny something.");

            codedItems[ItemsEnum.Items.WATERSHOES.ordinal()].setLocation(currRoomNum);
        } else if (currRoomNum == 13 && (noun.equals("ELF") || noun.equals("FAM"))
                && codedItems[ItemsEnum.Items.WATERSHOES.ordinal()].getLocation() == IN_INVENTORY) {
            System.out.println("They think Denny should leave.");

        } else if (currRoomNum == 26 && noun.equals("GIA")
                && codedItems[ItemsEnum.Items.HUGEHAMMER.ordinal()].getLocation() == NOT_IN_GAME) {
            System.out.println("He offers Denny something");
            codedItems[ItemsEnum.Items.HUGEHAMMER.ordinal()].setLocation(currRoomNum);

        } else if (currRoomNum == 17 && noun.equals("DWA")
                && codedItems[ItemsEnum.Items.TUNNEL.ordinal()].getLocation() == NOT_IN_GAME) {

            System.out.println("He points to something");
            codedItems[ItemsEnum.Items.TUNNEL.ordinal()].setLocation(-currRoomNum);
        } else if (currRoomNum == 26 && noun.equals("GIA") || currRoomNum == 17 && noun.equals("DWA")) {
            System.out.println("He has nothing else to say.");
        } else if (currRoomNum == 30 && (noun.equals("HAG") || noun.equals("OLD"))) {
            System.out.println("She pinched Denny");
            doDennyWakes("The hag pinched Denny!");
        } else {
            System.out.println("Denny can't talk to that.");
        }

        updateGame();
    }

    public void doDrink() {
        System.out.println("");
        if (noun.equals("POT") && codedItems[ItemsEnum.Items.POTION.ordinal()].getLocation() == IN_INVENTORY) {
            System.out.println("Strength surges into Denny's body.");
            codedItems[ItemsEnum.Items.POTION.ordinal()].setLocation(ITEM_USED);
        } else {
            System.out.println("Denny can't drink that.");
        }

        updateGame();
    }

    public void doClimb() {
        System.out.println("");
        if (currRoomNum == 11 && noun.equals("TRE")) {
            currRoomNum = 28;
            System.out.println("Denny climbs the tree.");
        } else {
            System.out.println("Denny won't climb that.");
        }
        updateGame();
    }

    public void doSing() {
        System.out.println("");
        if ((noun.equals("SHE") || noun.equals("MUS"))
                && codedItems[ItemsEnum.Items.SHEETMUSIC.ordinal()].getLocation() == IN_INVENTORY) {
            if (currRoomNum == 29) {
                if (prevBoatRoom == 17) {
//        Exits(UP) = 12;
                    prevBoatRoom = 12;
                    setBoatRoom(prevBoatRoom);

                } else {
//        Exits(UP) = 17
                    prevBoatRoom = 17;
                    setBoatRoom(prevBoatRoom);

                    codedItems[ItemsEnum.Items.BOAT.ordinal()].setLocation(-prevBoatRoom);

                    System.out.println("The boat glides across the pond.");
                }
            } else {
                System.out.println("While Denny sings, the boat begins to rock.");
            }
        }

        updateGame();
    }

    public void doSay() {
        System.out.println("");
        if (codedItems[ItemsEnum.Items.FANCYBOX.ordinal()].getLocation() == IN_INVENTORY
                && codedItems[ItemsEnum.Items.POTION.ordinal()].getLocation() == NOT_IN_GAME
                && codedItems[ItemsEnum.Items.BLUESCROLL.ordinal()].getLocation() == IN_INVENTORY
                && completeNoun.equals("PRESTO")) {

            System.out.println("The box opens, and the scroll vanishes.");
            codedItems[ItemsEnum.Items.POTION.ordinal()].setLocation(currRoomNum);
            codedItems[ItemsEnum.Items.BLUESCROLL.ordinal()].setLocation(ITEM_USED);

        } else if (currRoomNum == 21 && codedItems[ItemsEnum.Items.REDSCROLL.ordinal()].getLocation() == IN_INVENTORY
                && codedItems[ItemsEnum.Items.GLASSDOOR.ordinal()].getLocation() == NOT_IN_GAME
                && completeNoun.equals("SESAME")) {
            codedItems[ItemsEnum.Items.GLASSDOOR.ordinal()].setLocation(-currRoomNum);
            codedItems[ItemsEnum.Items.REDSCROLL.ordinal()].setLocation(ITEM_USED);
            System.out.println("A glass door appears!");

        } else {
            System.out.println("Denny says " + completeNoun);
        }

        updateGame();
    }

    public void doHit() {
        System.out.println("");

        if (currRoomNum == 24 && noun.equals("BOU")
                && codedItems[ItemsEnum.Items.HUGEHAMMER.ordinal()].getLocation() == IN_INVENTORY
                && codedItems[ItemsEnum.Items.CRYSTALS.ordinal()].getLocation() == NOT_IN_GAME) {
            System.out.println("When the hammer hits, a boulder crumbles.");
            codedItems[ItemsEnum.Items.CRYSTALS.ordinal()].setLocation(currRoomNum);
            codedItems[ItemsEnum.Items.BITSOFROCK.ordinal()].setLocation(currRoomNum);
        } else {
            System.out.println("Denny doesn't want to hit that.");
        }

        updateGame();
    }

    public void doGo() {
        if (currRoomNum == 5 && noun.equals("BED")) {
            System.out.println("Zzzzzzzzzzzzzzzzz.");

        } else if (currRoomNum == 15 && noun.equals("HUT")) {
            currRoomNum = 16;
            System.out.println("Denny enters the hut.");

        } else if (currRoomNum == 6 && (noun.equals("GLO") || noun.equals("DOO"))
                && codedItems[ItemsEnum.Items.GLOWINGDOOR.ordinal()].getLocation() == -currRoomNum) {

            currRoomNum = 8;

            System.out.println("Denny goes through the door.");
        } else if (currRoomNum == 17 && noun.equals("TUN")
                && codedItems[ItemsEnum.Items.TUNNEL.ordinal()].getLocation() == -currRoomNum) {

            currRoomNum = 18;

            System.out.println("Denny goes through the tunnel.");
        } else if (codedItems[ItemsEnum.Items.BOAT.ordinal()].getLocation() == -currRoomNum && noun.equals("BOA")) {

            currRoomNum = 29;

            System.out.println("Denny gets into the boat.");
        } else if (currRoomNum == 10 && (noun.equals("WOO") || noun.equals("DOO"))
                && codedItems[ItemsEnum.Items.WOODENDOOR.ordinal()].getLocation() == -currRoomNum) {

            currRoomNum = 13;

            System.out.println("Denny goes through the wooden door.");
        } else if ((currRoomNum == 11 || currRoomNum == 15) && noun.equals("SWA")) {

            if (codedItems[ItemsEnum.Items.WATERSHOES.ordinal()].getLocation() == IN_INVENTORY) {

                currRoomNum = 14;
                System.out.println("Denny uses the water shoes to cross the swamp.");
            } else {
                System.out.println("Denny sinks into the swamp.");

                doDennyWakes("Denny sinks into the swamp.");
            }
        } else if (currRoomNum == 18 && noun.equals("BRA")) {
            System.out.println("Denny's falling into empty blackness!");
            doDennyWakes("Denny's falling into blackness!");

        } else if (currRoomNum == 18 && noun.equals("SIL")) {
            currRoomNum = 19;
            System.out.println("Denny goes through the silver door.");

        } else if (currRoomNum == 18 && noun.equals("GOL")) {
            currRoomNum = 21;
            System.out.println("Denny goes through the gold door.");

        } else if (currRoomNum == 21 && noun.equals("GLA")
                && codedItems[ItemsEnum.Items.GLASSDOOR.ordinal()].getLocation() == -currRoomNum) {
            if (codedItems[ItemsEnum.Items.BOOK.ordinal()].getLocation() == IN_INVENTORY
                    || codedItems[ItemsEnum.Items.BOOK.ordinal()].getLocation() == 5
                    || codedItems[ItemsEnum.Items.BOOK.ordinal()].getLocation() == 6
                    && codedItems[ItemsEnum.Items.JUGWITHGLUE.ordinal()].getLocation() == IN_INVENTORY
                    && codedItems[ItemsEnum.Items.PAGE.ordinal()].getLocation() == IN_INVENTORY) {

                currRoomNum = 5;
                System.out.println("Denny goes through the glass door.");
            } else {
                System.out.println("Denny has failed his mission.");
                doDennyWakes("");
            }
        } else if (noun.equals("DOO")) {
            System.out.println("Which door?");
        } else {
            System.out.println("Denny doesn 't want to go there.");
        }

        updateGame();
    }

    private void doGet() {
       // System.out.println("getting");
        int itemNum = getItemNumber();
        if (itemNum == -1) {
            System.out.println("no such item");
        } else if (currRoomNum != Math.abs(codedItems[itemNum].getLocation())) {
            System.out.println("That Item is not here");
        } else if (codedItems[itemNum].getLocation() == -currRoomNum) {
            System.out.println("Denny can't pick that up");
        } else if (currRoomNum == 26 && (noun.equals("HAM"))
                || noun.equals("HUG") && codedItems[ItemsEnum.Items.POTION.ordinal()].getLocation() != ITEM_USED) {
            System.out.println("The hammer is too heavy     plebian");

        } else {
            codedItems[itemNum].setLocation(IN_INVENTORY);
            player.addToInventory(codedItems[itemNum]);
            System.out.println("denny picked it up");
        }
        updateGame();

    }

    public void doExamine() {
        // System.out.println(currRoomNum == codedItems[ItemsEnum.Items.BOOK.ordinal()].getLocation());
        // System.out.println(noun);
        if (noun.equals("BOO"))//&& codedItems[ItemsEnum.Items.BOOK.ordinal()].getLocation() == currRoomNum)
        {
            System.out.println("yes");
            System.out.println("\nIt looks interesting.\n");
        } else if (noun.equals("GIA") && codedItems[ItemsEnum.Items.GIANT.ordinal()].getLocation() == -currRoomNum) {
            System.out.println("\nHe's carrying an huge hammer.\n");
        } else if (noun.equals("BOA") && codedItems[ItemsEnum.Items.BOAT.ordinal()].getLocation() == -currRoomNum) {
            System.out.println("\nYep, it's definitely a boat.\n");
        } else if (noun.equals("MIR")
                && codedItems[ItemsEnum.Items.MIRROR.ordinal()].getLocation() == IN_INVENTORY) {
            System.out.println("\nSome writing says UGLIES DON'T USE THIS.\n");
        } else if (noun.equals("BOO")
                && codedItems[ItemsEnum.Items.BOOK.ordinal()].getLocation() == IN_INVENTORY) {
            System.out.println("\nThe last page is missing.\n");
        } else if (noun.equals("TRE") && currRoomNum == 10
                && codedItems[ItemsEnum.Items.WOODENDOOR.ordinal()].getLocation() == NOT_IN_GAME) {
            System.out.println("\nThere's a door in it.\n");
            codedItems[ItemsEnum.Items.WOODENDOOR.ordinal()].setLocation(-currRoomNum);
        } else if (currRoomNum == 11 && noun.equals("TRE")) {
            System.out.println("\nAn arrow points up.\n");
        } else if (currRoomNum == 23 && noun.equals("STR")
                && codedItems[ItemsEnum.Items.ALGAE.ordinal()].getLocation() == NOT_IN_GAME) {
            System.out.println("\nDenny sees something!\n");
            codedItems[ItemsEnum.Items.ALGAE.ordinal()].setLocation(currRoomNum);
        } else if (currRoomNum == 21 && (noun.equals("GLA") || noun.equals("DOO"))
                && codedItems[ItemsEnum.Items.GLASSDOOR.ordinal()].getLocation() == -currRoomNum) {
            System.out.println("\nDenny sees his bedroom!\n");
        } else if (currRoomNum == 19 && (noun.equals("TRO") || noun.equals("FAI"))
                && codedItems[ItemsEnum.Items.FAINTEDTROLL.ordinal()].getLocation() == -currRoomNum
                && codedItems[ItemsEnum.Items.PAGE.ordinal()].getLocation() == NOT_IN_GAME) {
            System.out.println("\nHe has a BOOK page!\n");
            codedItems[ItemsEnum.Items.PAGE.ordinal()].setLocation(currRoomNum);
        } else if ((noun.equals("FAN") || noun.equals("BOX"))
                && codedItems[ItemsEnum.Items.FANCYBOX.ordinal()].getLocation() == IN_INVENTORY) {
            System.out.println("\nIt says: Use blue scroll\n");
        } else if (noun.equals("BLU")
                && codedItems[ItemsEnum.Items.BLUESCROLL.ordinal()].getLocation() == IN_INVENTORY) {
            System.out.println("\nIt says: Asy rpseot\n");
        } else if ((noun.equals("SHE") || noun.equals("MUS"))
                && codedItems[ItemsEnum.Items.SHEETMUSIC.ordinal()].getLocation() == IN_INVENTORY) {
            System.out.println("\nIt's a very moving piece.\n");
        } else if ((noun.equals("REC") || noun.equals("CAR"))
                && codedItems[ItemsEnum.Items.RECIPECARD.ordinal()].getLocation() == IN_INVENTORY) {
            System.out.println("\nGlue: mud, algae, crystal.\n");
        } else if (noun.equals("RED")
                && codedItems[ItemsEnum.Items.REDSCROLL.ordinal()].getLocation() == IN_INVENTORY) {
            System.out.println("\nIt says: Asy mseesa\n");
        } else if (currRoomNum == 21 && noun.equals("SIG")) {
            System.out.println("\nIt says: Ali Baba was  here.\n");
        } else if (currRoomNum == 19 && noun.equals("TRO")
                && codedItems[ItemsEnum.Items.TROLL.ordinal()].getLocation() == -currRoomNum) {
            System.out.println("\nHe's frighteningly ugly.\n");
        } else if (noun.equals("PAG") && codedItems[ItemsEnum.Items.PAGE.ordinal()].getLocation() == IN_INVENTORY) {
            System.out.println("\nIt's from the BOOK.\n");
        } else if (noun.equals("POT") && codedItems[ItemsEnum.Items.POTION.ordinal()].getLocation() == IN_INVENTORY) {
            System.out.println("\nIt says: For strength.\n");
        } else {
            System.out.println("\nDenny sees nothing of value.");
        }

        updateGame();
    }

    public void doDennyWakes(String msg) {
        System.out.println("He wakes in bed, his mission incomplete.");
        init();
    }

    private int getNumRoomItems() {
        int num = 0;
        for (Item i : codedItems) {
            if (Math.abs(i.getLocation()) == currRoomNum) {
                num++;
            }
        }
        return num;
    }

    private void doDrop() {
        int numItems = getNumRoomItems();
        int itemNum = getItemNumber();

        if (itemNum == -1) {
            System.out.println("cant drop what doesn't exist");
        } else if (numItems == MAX_ROOM_ITEMS) {

            System.out.println("room is too full");

        } else if (codedItems[itemNum].getLocation() != IN_INVENTORY) {
            System.out.println("Denny doesn't have that");
        } else if (codedItems[ItemsEnum.Items.JUG.ordinal()].getLocation() == IN_INVENTORY
                && (noun.equals("CRY") || noun.equals("MUD") || noun.equals("ALG"))) {
            System.out.println("In the jug...");
            codedItems[itemNum].setLocation(ITEM_USED);
             player.inventory.remove(codedItems[itemNum]);
            numGlueIngredients = numGlueIngredients + 1;

            if (numGlueIngredients == 3) {
                codedItems[ItemsEnum.Items.JUGWITHGLUE.ordinal()].setLocation(IN_INVENTORY);
                codedItems[ItemsEnum.Items.JUG.ordinal()].setLocation(NOT_IN_GAME);
                codedItems[ItemsEnum.Items.TROLL.ordinal()].setLocation(-19);
                System.out.println("Denny made the magical glue!");
            }
        } else if (currRoomNum == 19 && trollFainted == false
                && noun.equals("MIR") && codedItems[ItemsEnum.Items.MIRROR.ordinal()].getLocation() == IN_INVENTORY) {
            System.out.println("The troll saw himself in the mirror and fainted!");
            trollFainted = true;
            codedItems[ItemsEnum.Items.MIRROR.ordinal()].setLocation(currRoomNum);
            codedItems[ItemsEnum.Items.FAINTEDTROLL.ordinal()].setLocation(-currRoomNum);
            codedItems[ItemsEnum.Items.TROLL.ordinal()].setLocation(NOT_IN_GAME);

        } else {

            player.inventory.remove(codedItems[itemNum]);
            codedItems[itemNum].setLocation(currRoomNum);
            System.out.println("denny has dropped that");

        }

        updateGame();
    }

    public void doMove() {

        // System.out.println(currRoomNum + " " + noun +codedItems[ItemsEnum.Items.GLOWINGDOOR.ordinal()].getLoc()  );
        if (currRoomNum == 6 && noun.equals("CLO")) {
            if (codedItems[ItemsEnum.Items.GLOWINGDOOR.ordinal()].getLocation() == NOT_IN_GAME) {

                System.out.println("\nThere's a strange doorway!\n");
                codedItems[ItemsEnum.Items.GLOWINGDOOR.ordinal()].setLocation(-currRoomNum);

                // System.out.println(codedItems[ItemsEnum.Items.GLOWINGDOOR.ordinal()].getLoc();
            } else { //wrong room
                System.out.println("\nDenny sees the closet wall.\n");
            }
        } else {
            System.out.println("\nDenny can't move that.\n");
        }
//
        updateGame();
    }

    public void doGlue() {
        if (codedItems[ItemsEnum.Items.JUGWITHGLUE.ordinal()].getLocation() == IN_INVENTORY
                && codedItems[ItemsEnum.Items.BOOK.ordinal()].getLocation() == IN_INVENTORY
                && codedItems[ItemsEnum.Items.PAGE.ordinal()].getLocation() == IN_INVENTORY
                && (noun.equals("BOOK") || noun.equals("PAG"))) {
            if (currRoomNum != 5 && currRoomNum != 6) {
                System.out.println("\nYou can't do that here.");
            } else {
                System.out.println("\nCongratulations! You saved Nightshade.");
                //StartNewGame
            }
        } else {
            System.out.println("\nDenny can't glue that.");
        }

        updateGame();
    }

    public int getItemNumber() {
        for (int i = 0; i < allNounIndex.length; i++) {
            if (allNouns[i].equals(noun)) {
                return allNounIndex[i];
            }
        }
        return -1;
    }

    private int getDirection(String dir) {
        dir = dir.toUpperCase();
        switch (dir) {
            case "N":
                return 0;
            case "S":
                return 1;
            case "E":
                return 2;
            case "W":
                return 3;
            case "U":
                return 4;
            case "D":
                return 5;
        }
        return -1;

    }

    private void setRoomData(int room) {
        switch (room) {
            case 8:
            case 9:
            case 10:
                codedItems[ItemsEnum.Items.TREES.ordinal()].setLocation(-currRoomNum);
                break;

            case 11:
                codedItems[ItemsEnum.Items.TREES.ordinal()].setLocation(-currRoomNum);
                codedItems[ItemsEnum.Items.SWAMP.ordinal()].setLocation(-currRoomNum);
                break;

            case 15:
                codedItems[ItemsEnum.Items.SWAMP.ordinal()].setLocation(-currRoomNum);
                break;

            case 29:
                setBoatRoom(prevBoatRoom);
                break;

        }

    }

    private void setBoatRoom(int pbr) {
        rooms[29] = new Room("in a small boat", "", 29, 0, 0, 0, 0, pbr, 0);
    }

}
