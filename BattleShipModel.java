import java.util.HashMap;
import java.util.*;
import javax.swing.JPanel;

public class BattleShipModel{

    //Scale for the BattleShip grid
    private static int scale = 10;
    //Starts in placing boat mode
    private static boolean placingboats = true;

    //How many boats do each player get
    private static int player1BoatCount = 5;
    private static int player2BoatCount = 5;

    //How many hits do players have
    private static int player1Hits = 0;
    private static int player2Hits = 0;
    //Win Varables...
    private static boolean win = false;
    private static int winningPlayer = 0;

    //Remeber what spot we are for each player
    private static int boatnum1 = 1;
    private static int boatnum2 = 1;

    //Remeber what locations for boats to spawn.  
    private static int p1set1[] = new int[2];
    private static int p1set2[] = new int[2];
    private static int p1set3[] = new int[2];

    private static int p2set1[] = new int[2];
    private static int p2set2[] = new int[2];
    private static int p2set3[] = new int[2];


    // MAP HAS FOUR STATES; W = water, B = boat, H = miss; S = sunk boat / hit boat
    private static HashMap<Integer, HashMap<Integer, String>> map1 = new HashMap<>();
    private static HashMap<Integer, HashMap<Integer, String>> map2 = new HashMap<>();

    //Creates a blank ocean for our playing field. 
    public static void createGameGrid(){
        //Creates a nested Hashmap for both players, of int, int, string. 
        for(int i = 0; i < scale; i++){
            HashMap<Integer, String> insideMap1 = new HashMap<>();
            HashMap<Integer, String> insideMap2 = new HashMap<>();
            for(int j = 0; j < scale; j++){ 
                 //SET ALL VALUES TO WATER
                 insideMap1.put(j, "W");
                 insideMap2.put(j, "W");
              } 
            map1.put(i, insideMap1);
            map2.put(i, insideMap2);
        }   

    }

    //Gets a players map 
    public static HashMap<Integer, HashMap<Integer, String>> getMap(int i){
        if(i == 1){
            //Player 1
            return map1;
        } else{
            //Player 2
            return map2;
        }
    }


    public int getScale(){
        return scale;
    }

    //Return True if placing boats
    //Return False if not placing boats
    public boolean getMode(){
        return placingboats;
    }

    //Returns 0 if no winner yet, 1 if player 1, and 2 if player 2.
    public int getWinnter(){
        return winningPlayer;
    }

    //Checks if a location is a boat, 
    public static boolean checkForBoat(int x, int y, int player){
        
        if(player == 1){
            // W stands for Water, so if the location is water...
            if(map1.get(x).get(y).equals("W")){
                //System.out.println("Debug: replacing player " + player + "'s grid at " + x + " " + y);

                //Replace the Water as a Empty Hit
                map1.get(x).replace(y, "H");
                //Return false if its not a boat, 
                return false;
            // B stands for Boat, so if location is boat...
            } else if(map1.get(x).get(y).equals("B")){
                //System.out.println("Debug: replacing player " + player + "'s grid at " + x + " " + y);

                //Replace the Boat with Sunk location
                map1.get(x).replace(y, "S");
                //If player1Hits gets to 15 player 1 wins
                player1Hits++;

                //System.out.println("DEBUG: hits for " + player + " is " + player1Hits);

                //Check if player 1 has won the game
                hasWon(player);
                //return true if it is a boat.
                return true;
            }    

        } else if(player == 2){
            if(map2.get(x).get(y).equals("W")){
                //TEST
                //System.out.println("Debug: replacing player " + player + "'s grid at " + x + " " + y);

                map2.get(x).replace(y, "H");
                return false;
            } else if(map2.get(x).get(y).equals("B")){
                //TEST
                //System.out.println("Debug: replacing player " + player + "'s grid at " + x + " " + y);

                map2.get(x).replace(y, "S");
                player2Hits++;

                //System.out.println("DEBUG: hits for " + player + " is " + player2Hits);

                hasWon(player);
                return true;
            }   
        }
        
        
        return false;
    }
    // Remeber where player has tried to place boats, boats are three tiles long so we get three locations from player.
    public static void remeberBoat(int player, int x, int y){
        //If player 1
        if (player == 1) {
            //If not already a boat
            if(!map1.get(x).get(y).equals("B")){
                //This tracks which of the three locations to save we are on; saves the location; and updates what location we are on. 
                if(boatnum1 == 1){
                    p1set1[0] = x; p1set1[1] = y; 
                } else if(boatnum1 == 2){
                    p1set2[0] = x; p1set2[1] = y; 
                } else if(boatnum1 == 3){
                    p1set3[0] = x; p1set3[1] = y; 
                    //If we more boats to place, place one.
                    if(player1BoatCount>0){
                        spawnBoat(player);
                    }
                    boatnum1 = 0;
                }
                boatnum1++;
            }
            
        } else if (player == 2) {
            if(!map2.get(x).get(y).equals("B")) {
                if(boatnum2 == 1){
                    p2set1[0] = x; p2set1[1] = y; 
                } else if(boatnum2 == 2){
                    p2set2[0] = x; p2set2[1] = y; 
                } else if(boatnum2 == 3){
                    p2set3[0] = x; p2set3[1] = y; 
                
                    if(player2BoatCount>0){
                        spawnBoat(player);
                    }
                    boatnum2 = 0;
                }
                boatnum2++;
            }
        }
    }
    //Spawn a boat based on the locations we remebered. 
    public static void spawnBoat(int player){
        System.out.println("TRYING TO SPAWN BOAT");
        int numX, numY;
        if (player == 1) {
            //TESTING
            /*
            for(int i : p1set1){
                System.out.print(i);
            }
            System.out.println("\n");
            for(int i : p1set2){
                System.out.print(i);
            }
            System.out.println("\n");
            for(int i : p1set3){
                System.out.print(i);
            }
            System.out.println("\n");
            */

            //LEFT AND RIGHT
            if(p1set1[0] == p1set2[0] && p1set1[0] == p1set3[0]){
            //CHECKING if position 1 and 2 are close
                if(p1set1[1] + 1 == p1set2[1] || p1set1[1] - 1 == p1set2[1]){
                //CHECKing if position 1 and 3 are close
                   if(p1set1[1] + 2 == p1set3[1] || p1set1[1] - 2 == p1set3[1]){
                    //CHECKING if position 2 and 3 are close
                        if(p1set2[1] + 1 == p1set3[1] || p1set2[1] - 1 == p1set3[1]){
                            //Spawn Boat
                            map1.get(p1set1[0]).put(p1set1[1], "B");
                            map1.get(p1set2[0]).put(p1set2[1], "B");
                            map1.get(p1set3[0]).put(p1set3[1], "B");
                            player1BoatCount--;
                        }
                        
                    } 
                }

            //UP AND DOWN
            } else if (p1set1[1] == p1set2[1] && p1set1[1] == p1set3[1]){
                if(p1set1[0] + 1 == p1set2[0] || p1set1[0] - 1 == p1set2[0]){
                   if(p1set1[0] + 2 == p1set3[0] || p1set1[0] - 2 == p1set3[0]){
                        if(p1set2[0] + 1 == p1set3[0] || p1set2[0] - 1 == p1set3[0]){
                            map1.get(p1set1[0]).put(p1set1[1], "B");
                            map1.get(p1set2[0]).put(p1set2[1], "B");
                            map1.get(p1set3[0]).put(p1set3[1], "B");
                            player1BoatCount--;
                        }
                    } 
                }
            }
            
        } else if (player == 2){
            //TESTING
            /* 
            for(int i : p2set1){
                System.out.print(i);
            }
            System.out.println("\n");
            for(int i : p2set2){
                System.out.print(i);
            }
            System.out.println("\n");
            for(int i : p2set3){
                System.out.print(i);
            }
            System.out.println("\n");
            */
            if(p2set1[0] == p2set2[0] && p2set1[0] == p2set3[0]){
                if(p2set1[1] + 1 == p2set2[1] || p2set1[1] - 1 == p2set2[1]){
                   if(p2set1[1] + 2 == p2set3[1] || p2set1[1] - 2 == p2set3[1]){
                        if(p2set2[1] + 1 == p2set3[1] || p2set2[1] - 1 == p2set3[1]){
                            map2.get(p2set1[0]).put(p2set1[1], "B");
                            map2.get(p2set2[0]).put(p2set2[1], "B");
                            map2.get(p2set3[0]).put(p2set3[1], "B");
                            player2BoatCount--;
                        }
                    } 
                }

            //Up and Down
            } else if (p2set1[1] == p2set2[1] && p2set1[1] == p2set3[1]){
                if(p2set1[0] + 1 == p2set2[0] || p2set1[0] - 1 == p2set2[0]){
                   if(p2set1[0] + 2 == p2set3[0] || p2set1[0] - 2 == p2set3[0]){
                        if(p2set2[0] + 1 == p2set3[0] || p2set2[0] - 1 == p2set3[0]){
                            map2.get(p2set1[0]).put(p2set1[1], "B");
                            map2.get(p2set2[0]).put(p2set2[1], "B");
                            map2.get(p2set3[0]).put(p2set3[1], "B");
                            player2BoatCount--;
                        }
                    } 
                }
            }

        }
        //If both players can't place anymore boats move on to guessing mode
        if (player1BoatCount <= 0 && player2BoatCount <= 0) {
            System.out.println("MOVING TO GUESSING MODE");
            placingboats = false;
            exchangeGameBoards();
        }
        
    }
    //Exchange each players boards, 
    //This is used between placing boats
    //and guessing boats, unless you want to find your own ships.
    private static void exchangeGameBoards(){
        System.out.println("CHANGE GAMEBOARDS AND HIDE BOATS");
        for(int i = 0; i < scale; i++){
            for(int j = 0; j < scale; j++){
                String p1Square, p2Square;
                p1Square = map1.get(i).get(j);
                p2Square = map2.get(i).get(j);

                map1.get(i).put(j, p2Square);
                map2.get(i).put(j, p1Square);
            }
        }
    }

    //Checks if a player has won yet.
    private static void hasWon(int player){
        if(player1Hits == 15 || player2Hits == 15){
            System.out.println("\n\n\nWINNING PLAYER IS: " + player + "\n\n\n");
            win = true;
            winningPlayer = player;
        }
    }

    
}