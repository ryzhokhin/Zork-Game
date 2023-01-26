import java.util.zip.GZIPOutputStream;

public class Game {
    private Room currentRoom;
    private Parser parser;
    private Player player;
    public Game(){
        parser = new Parser();
        player = new Player();
    }
    public static void main(String args[]){
        Game game = new Game();
        game.createRooms();
        game.play();
    }

    private void createRooms(){
        Room riverBank = new Room("short river", "long river", "River Bank");
        Room garden = new Room("short garden","long garden", "Garden");
        Room house = new Room("short house","long house", "House");
        Room cemetery = new Room("short cemetery","long cemetery", "cemetery");
        Room xuinya = new Room("short xuinya", "long xuinya", "xuinya");
        Room craftingTable = new Room("craft", "craft", "craftTable");
        //River exits and invent
        riverBank.setExit("west", garden);

        //Garden exits and invent
        garden.setExit("east", riverBank);
        garden.setExit("northwest", house);
        garden.setExit("southwest",cemetery);
        garden.setExit("west", xuinya);

        Item woods = new Item();
        garden.setItem("woods", woods);

        //House exits and invent
        house.setExit("south", cemetery);
        house.setExit("craftingTable", craftingTable);

        //cemetery exits and invent
        cemetery.setExit("north", house);
        cemetery.setExit("northeast", garden);

        Item keys = new Item();
        cemetery.setItem("keys", keys);

        //Xuinya exits and invert
        xuinya.setExit("south", cemetery);
        xuinya.setExit("east", garden);

        Item rope = new Item();
        Item axe = new Item();
        xuinya.setItem("rope", rope);
        xuinya.setItem("axe", axe);

        //Staring parameters
        currentRoom = riverBank;
       // player.setItem("keys" ,keys);



    }

    public void play(){
        printWelcome();
        boolean finished = false;
        while(!finished){
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("You finished the game");
    }

    private boolean processCommand(Command command){
        boolean wantToQuit = false;

        CommandWord commandWord = command.getCommandWord();

        switch (commandWord){
            case UNKNOWN:
                System.out.println("I don't know what you mean");
                helpHint();
                break;
            case HELP:
                printHelp();
                break;
            case GO:
                goRoom(command);
                break;
            case QUIT:
                wantToQuit = quit(command);
                break;
            case LOOK:
                look(command);
                break;
            case GRAB:
                grab(command);
                break;
            case DROP:
                drop(command);
                break;
            case INVENTORY:
                lookInventory(command);
                break;
            case TEST:
                testing(command);
                break;
    }
        return wantToQuit;
}



    private void look(Command command){
        if(command.hasSecondWord()){
            System.out.println("You can't look at "+command.getSecondWord());
            helpHint();
            return;
        }
        System.out.println(currentRoom.getLongDescription());
        System.out.println(player.getItemString());

    }

    private void goRoom(Command command){
        if(!command.hasSecondWord()){
            System.out.println("Go where?");
            helpHint();
            return;
        }
        String direction = command.getSecondWord();
        Room nextRoom = currentRoom.getExit(direction);


        if(nextRoom == null){
            System.out.println("There is no door");
        }else{
            currentRoom = nextRoom;
            System.out.println(currentRoom.getShortDescription());
        }
    }

    private boolean quit(Command command){
        if(command.hasSecondWord()){
            System.out.println("You can't quit "+ command.getCommandWord());
            helpHint();
            return false;
        }else{
            return true;
        }
    }

    private void grab(Command command){
        if (!command.hasSecondWord()){
            System.out.println("Grab what?");
            helpHint();
            return;
        }
        String wantedItem = command.getSecondWord();
        Item  item =  currentRoom.getItem(wantedItem);
        if(wantedItem==null){
            System.out.println("There is no item");
        }else{
            player.setItem(wantedItem,item);
            System.out.println("Now you will find "+wantedItem+" in your inventory");
        }
    }
    private void drop(Command command){
        if(!command.hasSecondWord()){
            System.out.println("Drop what?");
            helpHint();
            return;
        }
        String wantedItemToDrop = command.getSecondWord();
        Item item = player.getItem(wantedItemToDrop);
        if(wantedItemToDrop==null){
            System.out.println("There is no item that you want to drop");
        }else{
            currentRoom.setItem(wantedItemToDrop,item);
            System.out.println("You dropped "+wantedItemToDrop+" from your inventory");
        }
    }

    private void lookInventory(Command command){
        if(command.hasSecondWord()){
            System.out.println("You can't look in inventory of "+command.getSecondWord());
            System.out.println("Your prompt request is: \"inventory\"");
            helpHint();
            return;
        }

        System.out.println(player.getItemString());
    }
    //testing
    private void testing(Command command){
        System.out.println(player.checkKeys());
    }



    //
    private void printWelcome(){
        System.out.println();
        System.out.println("Welcome to my text adventure game!");
        System.out.println("You will find yourself near the bank of the river!");
        System.out.println("Your task is to escape to the opposite side");
        System.out.println("Type \"help\" if you need assistance");
        System.out.println();
        System.out.println("room description");
        System.out.println(currentRoom.getShortDescription());

    }
    private void printHelp(){
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("You are in the "+currentRoom.getRoomName());
        System.out.println();
        System.out.println("Your command word are:");
        System.out.println(" \"go\" "+"\n"+" \"quit\" " +"\n"+ " \"UNKNOWN\" " +"\n"+ " \"LOOK\" " +"\n"+ " \"GRAB\" " +"\n"+ " \"DROP\" " +"\n"+ " \"INVENTORY\" ");
    }
    private void helpHint(){
        System.out.println("----or----");
        System.out.println("Type: \"Help\" for more info");
    }
}
