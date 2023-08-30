package minefield.view;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;

import minefield.model.Field;
import minefield.model.enums.FieldEvent;
import minefield.model.interfaces.FieldObserver;

public class FieldButton extends JButton implements FieldObserver, MouseListener {
    
    private final Color DEFAULT_BACK_GROUND = new Color(184, 184, 184);
    private final Color MARKED_BACK_GROUND = new Color(8, 179, 247);
    private final Color EXPLOSION_BACK_GROUND = new Color(189, 66, 68);
    private final Color TEXT_GREEN = new Color(0, 100, 0);

    private Field field;

    public FieldButton(Field field) {
        this.field = field;
        setBackground(DEFAULT_BACK_GROUND);
        setOpaque(true);
        setBorder(BorderFactory.createBevelBorder(0));

        addMouseListener(this);
        field.addObserver(this);
    }

    @Override
    public void eventOccurred(Field field, FieldEvent event) {
        switch (event) {
            case OPEN:
                applyOpenStyle();
                break;

            case MARK:
                applyMarkStyle();
                break;

            case EXPLODE:
                applyExplodeStyle();
                break;

            default:
                applyDefaultStyle();
                break;
        }
    }

    private void applyDefaultStyle() {
        setBackground(DEFAULT_BACK_GROUND);
        setText("");
    }

    private void applyExplodeStyle() {
        setBackground(EXPLOSION_BACK_GROUND);
        setForeground(Color.WHITE);
        setText("X");
    }

    private void applyMarkStyle() {
        setBackground(MARKED_BACK_GROUND);
        setForeground(Color.BLACK);
        setText("M");
    }

    private void applyOpenStyle() {

        setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        if(field.isMined()) {
            setBackground(EXPLOSION_BACK_GROUND);
            return;
        }
        
        setBackground(DEFAULT_BACK_GROUND);

        switch (field.minesInNeighborhood()) {
            case 1:
                setForeground(TEXT_GREEN);
                break;
        
            case 2:
                setForeground(Color.BLUE);
                break;
            
            case 3:
                setForeground(Color.YELLOW);
                break;

            case 4:
            case 5:
            case 6:
                setForeground(Color.RED);
                break;

            default:
                setForeground(Color.PINK);
        }

        String value = !field.isSafeNeighborhood() ? field.minesInNeighborhood()+ "" : "";

        setText(value);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == 1) {
            field.open();
        } else {
            field.toggleMark();
        }
    }
    
    public void mousePressed(MouseEvent e) { }
    public void mouseClicked(MouseEvent e) { }
    public void mouseEntered(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }
}
