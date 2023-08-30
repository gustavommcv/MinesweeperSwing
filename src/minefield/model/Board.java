package minefield.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Board {

	private int rows;
	private int columns;
	private int mines;

	private final List<Field> fields = new ArrayList<>();

	public Board(int rows, int columns, int mines) {
		this.rows = rows;
		this.columns = columns;
		this.mines = mines;

		generateFields();
		associateNeighbors();
		randomizeMines();
	}

	public void open(int row, int column) {
		try {
			fields.parallelStream()
				.filter(f -> f.getRow() == row && f.getColumn() == column)
				.findFirst()
				.ifPresent(f -> f.open());
		} catch (Exception e) {
			// FIXME Adjust the implementation of the open method
			fields.forEach(f -> f.setOpened(true));
			throw e;
		}
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
				fields.add(new Field(row, column));
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
}
