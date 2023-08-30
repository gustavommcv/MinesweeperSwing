package minefield.model;

import java.util.ArrayList;
import java.util.List;

public class Field {

	private final int row;
	private final int column;

	private boolean opened = false;
	private boolean mined = false;
	private boolean marked = false;

	private List<Field> neighbors = new ArrayList<>();

	Field(int row, int column) {
		this.row = row;
		this.column = column;
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

	void toggleMark() {
		if (!opened) {
			marked = !marked;
		}
	}

	boolean open() {

		if (!opened && !marked) {
			opened = true;

			if (mined) {
				// TODO Implement new version
			}

			if (isSafeNeighborhood()) {
				neighbors.forEach(n -> n.open());
			}

			return true;
		} else {
			return false;
		}
	}

	boolean isSafeNeighborhood() {
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

	long minesInNeighborhood() {
		return neighbors.stream().filter(n -> n.mined).count();
	}

	void restart() {
		opened = false;
		mined = false;
		marked = false;
	}
}
