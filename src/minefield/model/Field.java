package minefield.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import minefield.model.enums.FieldEvent;
import minefield.model.interfaces.FieldObserver;

public class Field {

	private final int row;
	private final int column;

	private boolean opened = false;
	private boolean mined = false;
	private boolean marked = false;

	private List<Field> neighbors = new ArrayList<>();

	private Set<FieldObserver> observers = new HashSet<>();

	Field(int row, int column) {
		this.row = row;
		this.column = column;
	}

	public void addObserver(FieldObserver observer) {
		observers.add(observer);
	}

	public void removeObserver(FieldObserver observer) {
        observers.remove(observer);
    }

	private void notifyObservers(FieldEvent event) {
    	for (FieldObserver observer : observers) {
        	observer.eventOccurred(this, event);
        }
    }

	boolean addNeighbor(Field neighbor) {
		boolean differentRow = row != neighbor.row;
		boolean differentColumn = column != neighbor.column;
		boolean diagonal = differentRow && differentColumn;

		int rowDelta = Math.abs(row - neighbor.row);
		int columnDelta = Math.abs(column - neighbor.column);
		int generalDelta = rowDelta + columnDelta;

		if (generalDelta == 1 && !diagonal) {
			neighbors.add(neighbor);
			return true;
		} else if (generalDelta == 2 && diagonal) {
			neighbors.add(neighbor);
			return true;
		} else {
			return false;
		}
	}

	public void toggleMark() {
		if (!opened) {
			marked = !marked;

			if (marked) {
				notifyObservers(FieldEvent.MARK);
			} else {
				notifyObservers(FieldEvent.UNMARK);
			}
		}
	}

	public boolean open() {

		if (!opened && !marked) {

			if (mined) {
				notifyObservers(FieldEvent.EXPLODE);
				return true;
			}

			setOpened(true);
			
			if (isSafeNeighborhood()) {
				neighbors.forEach(n -> n.open());
			}

			return true;
		} else {
			return false;
		}
	}

	public boolean isSafeNeighborhood() {
		return neighbors.stream().noneMatch(n -> n.mined);
	}

	void mine() {
		mined = true;
	}

	public boolean isMined() {
		return mined;
	}

	public boolean isMarked() {
		return marked;
	}

	void setOpened(boolean opened) {
		this.opened = opened;

		if (opened) {
			notifyObservers(FieldEvent.OPEN);
		}
	}

	public boolean isOpened() {
		return opened;
	}

	public boolean isClosed() {
		return !isOpened();
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	boolean isObjectiveAchieved() {
		boolean revealed = !mined && opened;
		boolean protectedField = mined && marked;
		return revealed || protectedField;
	}

	public int minesInNeighborhood() {
		return (int) neighbors.stream().filter(n -> n.mined).count();
	}

	void restart() {
		opened = false;
		mined = false;
		marked = false;
		notifyObservers(FieldEvent.RESTART);
	}
}
