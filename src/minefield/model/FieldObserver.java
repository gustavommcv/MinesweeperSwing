package minefield.model;

import minefield.model.enums.FieldEvent;

@FunctionalInterface
public interface FieldObserver {
    
    void eventOccurred(Field field, FieldEvent event);
}
