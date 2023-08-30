package minefield.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.w3c.dom.events.Event;

import minefield.model.enums.FieldEvent;
import minefield.model.interfaces.FieldObserver;

public class Board implements FieldObserver {

	private int rows;
	private int columns;
	private int mines;

	private final List<Field> fields = new ArrayList<>();

	private List<Consumer<EventResult>> observers = new ArrayList<>();

	public Board(int rows, int columns, int mines) {
		this.rows = rows;
		this.columns = columns;
		this.mines = mines;

		generateFields();
		associateNeighbors();
		randomizeMines();
	}

	public void addObserver(Consumer<EventResult> observer) {
		observers.add(observer);
	}

	public void removeObserver(Consumer<EventResult> observer) {
        observers.remove(observer);
    }

	private void notifyObservers(boolean result) {
    	observers.stream()
			.forEach(o -> o.accept(new EventResult(result)));
    }

	public void open(int row, int column) {
		fields.parallelStream()
			.filter(f -> f.getRow() == row && f.getColumn() == column)
			.findFirst()
			.ifPresent(f -> f.open());
	}
	
	private void showMines() {
		fields.stream()
			.filter(f -> f.isMined())
			.forEach(f -> f.setOpened(isObjectiveAchieved()));
	}

	public void toggleMark(int row, int column) {
		fields.parallelStream()
			.filter(f -> f.getRow() == row && f.getColumn() == column)
			.findFirst()
			.ifPresent(f -> f.toggleMark());
	}

	private void generateFields() {
		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
				Field field = new Field(row, column);
				field.addObserver(this);
				fields.add(field);
			}
		}
	}

	private void associateNeighbors() {
		for (Field f1 : fields) {
			for (Field f2 : fields) {
				f1.addNeighbor(f2);
			}
		}
	}

	private void randomizeMines() {
		long armedMines = 0;
		Predicate<Field> mined = f -> f.isMined();

		do {
			int randomIndex = (int) (Math.random() * fields.size());
			fields.get(randomIndex).mine();
			armedMines = fields.stream().filter(mined).count();
		} while (armedMines < mines);
	}

	public boolean isObjectiveAchieved() {
		return fields.stream().allMatch(f -> f.isObjectiveAchieved());
	}

	public void restart() {
		fields.stream().forEach(f -> f.restart());
		randomizeMines();
	}

	@Override
	public void eventOccurred(Field field, FieldEvent event) {
		if (event == FieldEvent.EXPLODE) {
			showMines();
			notifyObservers(false);
		} else if (isObjectiveAchieved()) {
			notifyObservers(true);
		}
	}
}
