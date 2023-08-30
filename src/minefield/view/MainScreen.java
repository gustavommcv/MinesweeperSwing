package minefield.view;

import javax.swing.JFrame;

import minefield.model.Board;

public class MainScreen extends JFrame {

    public MainScreen() {

        Board board = new Board(16, 30, 50);
        add(new BoardPanel(board));

        setTitle("MineField");
        setSize(677, 423);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setVisible(true);
    }
    
    public static void main(String[] args) {
        new MainScreen();
    }
}
