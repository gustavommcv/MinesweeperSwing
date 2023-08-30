package minefield.view;

import javax.swing.JPanel;

import minefield.model.Board;

import java.awt.GridLayout;

public class BoardPanel extends JPanel {

    public BoardPanel(Board board) {
        setLayout(new GridLayout(board.getRows(), board.getColumns()));

        board.forEachField(f -> add(new FieldButton(f)));
        
        board.addObserver(e -> {
            // TODO Show result on screen
        });
    }
}
