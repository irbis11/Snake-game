import controller.GameController;

public class Main {

    public static void main(String[] args) {
        GameController gameController = new GameController(3, 30, 30);
        gameController.createWindow();
    }

}