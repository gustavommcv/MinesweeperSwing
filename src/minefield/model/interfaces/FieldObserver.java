package minefield.model.interfaces;

import minefield.model.Field;
import minefield.model.enums.FieldEvent;

@FunctionalInterface
public interface FieldObserver {
    void eventOccurred(Field field, FieldEvent event);
}
