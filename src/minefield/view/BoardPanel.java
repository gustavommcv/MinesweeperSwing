package minefield.view;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import minefield.model.Board;

import java.awt.GridLayout;

public class BoardPanel extends JPanel {

    public BoardPanel(Board board) {
        setLayout(new GridLayout(board.getRows(), board.getColumns()));

        board.forEachField(f -> add(new FieldButton(f)));
        
        board.addObserver(e -> {
            SwingUtilities.invokeLater(() -> {
                if(e.isWon()) {
                    JOptionPane.showMessageDialog(this, "Won!");
                } else {
                    JOptionPane.showMessageDialog(this, "Game Over!");
                }

                board.restart();
            });
        });
    }
}
