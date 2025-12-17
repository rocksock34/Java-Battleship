import javax.swing.*;
import java.awt.*;
import java.util.*;

public class BattleShipView extends JPanel{
    //Model ref
    private static BattleShipModel model;
    //Scale ref
    private static int scale;
    //Players interactive Ui Grids
    private static HashMap<Integer, HashMap<Integer, JButton>> buttonMap1 = new HashMap<>();
    private static HashMap<Integer, HashMap<Integer, JButton>> buttonMap2 = new HashMap<>();
    
    //Game starts with player 1's turn 
    private static int playerTurn = 1; 
    //players GUI's
    private static JFrame play1Frame;
    private static JFrame play2Frame;
    //Extra GUI's
    private static JFrame stJPanel = new JFrame();
    private static JFrame winnerJPanel = new JFrame();
    //private static HashMap<Integer, HashMap<Integer, JPanel>> enemyBoxMap1 = new HashMap<>();
    //private static HashMap<Integer, HashMap<Integer, JPanel>> enemyBoxMap2 = new HashMap<>();

    //Create's the game view
    public static void createGameView(BattleShipModel mod){
        //TESTING WINNING SCREEN
        //winningScreen(2);
        
        model = mod;
        scale = model.getScale();
        //GUI 

        JFrame player1Frame = new JFrame("Battle Ship Player 1");
        JFrame player2Frame = new JFrame("Player Ship Player 2");

        player1Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        player2Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        player1Frame.setSize(600, 600);
        player2Frame.setSize(600, 600);

        player1Frame.setLayout(new BorderLayout(model.getScale(), model.getScale()));
        player2Frame.setLayout(new BorderLayout(model.getScale(), model.getScale()));

        JPanel grid1 = new JPanel();
        JPanel grid2 = new JPanel();

        grid1.setLayout(new GridLayout(scale, scale, 2, 2));
        grid2.setLayout(new GridLayout(scale, scale, 2, 2));

        createButtonLayout(buttonMap1, grid1, 1);
        createButtonLayout(buttonMap2, grid2, 2);

        paintGameGrid(buttonMap1, 1);
        paintGameGrid(buttonMap2, 2);

        player1Frame.add(grid1, BorderLayout.CENTER);
        player2Frame.add(grid2, BorderLayout.CENTER);

        JPanel leftInfo1 = new JPanel();
        JPanel leftInfo2 = new JPanel();

        leftInfo1.setLayout(new BoxLayout(leftInfo1, BoxLayout.Y_AXIS));
        leftInfo2.setLayout(new BoxLayout(leftInfo2, BoxLayout.Y_AXIS));

        player1Frame.add(leftInfo1, BorderLayout.WEST);
        player2Frame.add(leftInfo2, BorderLayout.WEST);

        player1Frame.setVisible(true);
        player2Frame.setVisible(true);

        play1Frame = player1Frame;
        play2Frame = player2Frame;
        
    }

    //Creates the Start GUI
    public static void startMenu(BattleShipModel mod){
        
        stJPanel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        stJPanel.setSize(600, 600);

        JButton starButton = new JButton("Start");
        Font biggerFont = new Font(starButton.getFont().getFontName(), Font.PLAIN, 50);
        starButton.setFont(biggerFont);
        starButton.addActionListener(e -> placeBoatsMenu(mod));
        
        stJPanel.add(starButton);

        //stJPanel.setLayout(new BoxLayout(stJPanel, BoxLayout.Y_AXIS));
        
        stJPanel.setVisible(true);

    }
    //Function for the start button, spawns in the game boards and alows boats to be placed
    public static void placeBoatsMenu(BattleShipModel mod){
        stJPanel.setVisible(false);
        createGameView(mod);
    }

    //Creates all the buttons for the game boards
    public static void createButtonLayout(HashMap<Integer, HashMap<Integer, JButton>> mapB, JPanel pan, int player){

        for(int i = 0; i < scale; i++){
            HashMap<Integer, JButton> insideMap = new HashMap<>();
            for(int j = 0; j < scale; j++){ 
                 
                 insideMap.put(j, new JButton());

                 int x = i; int y = j;
                 insideMap.get(j).addActionListener(e -> dropBomb(x, y, player));

                 pan.add(insideMap.get(j)); 

              } 
            mapB.put(i, insideMap);
        }  
    }

    //The Main "repaint" function, reads each button and compares it with the string value then updates colors. 
    public static void paintGameGrid(HashMap<Integer, HashMap<Integer, JButton>> mapB, int player){
        int scale = model.getScale();
        HashMap<Integer, HashMap<Integer, String>> map = model.getMap(player);
        //System.out.print("\n");
        for(int i = 0; i < scale; i++){
            for(int j = 0; j < scale; j++){ 
                 String let = map.get(i).get(j);
                 //System.out.print(let + " ");
                 mapB.get(i).get(j).setBackground(buttonUpdate(let));
              } 
            //System.out.print("\n");
        }

    }
    //Allows for buttons to change color based on a string letter input. 
    public static Color buttonUpdate(String let){
        //OCEAN
        if(let.equals("W")){
            return Color.BLUE;
            //HIT BOAT
        } else if (let.equals("H")){
            return Color.WHITE;
           
        } else if (let.equals("B")){
            if(model.getMode()){
                return Color.GREEN;
            } else {
                return Color.BLUE;
            }
        } else if (let.equals("S")){
             return Color.RED;
        }
        //ANY THING ELSE
        return Color.ORANGE;
    }

    //This is the function that gets called by all of the game's grid buttons
    public static void dropBomb(int x, int y, int player){
        //getMode is true if we are placing ships
        if(model.getMode()){
            System.out.println("DEBUG: Place at " + (x + 1) + " " + (y + 1) + " On player " + player + "'s board");
            model.remeberBoat(player, x, y);
            
                paintGameGrid(buttonMap1, 1);
                paintGameGrid(buttonMap2, 2);
            //When we transition from placing mode into guessing mode.
            if(!model.getMode()){
                toggleView();
            }
            

        } else {
            //If we are in placing mode
            System.out.println("DEBUG: hit at " + (x + 1) + " " + (y + 1) + " On player " + player + "'s board");
            //Checks if we hit a boat or water...
            boolean hit = model.checkForBoat(x, y, player);
            
                paintGameGrid(buttonMap1, 1);
                paintGameGrid(buttonMap2, 2);
                //Check for a winner in case
                if(model.getWinnter() == 1){
                    winningScreen(1);
                } else if (model.getWinnter() == 2){
                    winningScreen(2);
                }
            //If we are in guessing mode, and didn't hit a ship, then we switch views.
            if(!model.getMode() && !hit){
                toggleView();
            }
        }
       
    }
    //Turn one player's screen on, turn the others off.
    private static void toggleView(){
        if(playerTurn==1){
            play1Frame.setVisible(true);
            play2Frame.setVisible(false);
            playerTurn++;
        } else if(playerTurn==2){
            play2Frame.setVisible(true);
            play1Frame.setVisible(false);
            playerTurn=1;
        }
    }
    //Winning Screen UI
    private static void winningScreen(int player){

        winnerJPanel.setTitle("Player: " + player + " Has Won");
        winnerJPanel.setSize(500, 200);
        JTextArea winningtext = new JTextArea("Player: " + player + " Has won the game.");
        winningtext.setEditable(false); 
        Font biggerFont = new Font(winningtext.getFont().getFontName(), Font.PLAIN, 32);
        winningtext.setFont(biggerFont);
        winnerJPanel.add(winningtext);
        //JButton reset = new JButton("Reset");
        //ADD RESET FUNCTIONALITY
        //reset.addActionListener(e -> resetAndClear());
        //winnerJPanel.add(reset);
        winnerJPanel.setVisible(true);
        play2Frame.setVisible(false);
        play1Frame.setVisible(false);


    }


    
}
