public class BattleShipGameTesting {
    private static BattleShipModel model = new BattleShipModel();
    

    public static void main(String[] args) {
        //Create the Empty Water filled maps.
        model.createGameGrid();
        final BattleShipView view = new BattleShipView(); 
        //Starts the game.
        view.startMenu(model);

    }

}
