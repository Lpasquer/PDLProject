package pdl.wiki;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

public class Table {
	private TreeMap<Integer, TreeMap<Integer, String>> table;

	public Table() {
		table = new TreeMap<Integer, TreeMap<Integer, String>>();
	}

	public void addValue(int row, int column, int rowspan, int colspan, String value) {
		for (int i = row; i < row + rowspan; i++) {

			TreeMap<Integer, String> col = null;

			if (!table.containsKey(i)) {
				col = new TreeMap<Integer, String>();
				table.put(i, col);
			} else {
				col = table.get(i);
			}

			for (int j = column; j < column + colspan; j++) {

				if (!col.containsKey(j)) {
					col.put(j, value);
				} else {
					int shift = j + 1;
					while (col.containsKey(shift)) {
						shift++;
					}
					column = shift;
					j = shift;
					col.put(shift, value);
				}
			}
		}

	}

	public List<String> getCSVLines() {
		List<String> values = new ArrayList<String>();

		Set<Entry<Integer, TreeMap<Integer, String>>> rows = table.entrySet();
		Iterator<Entry<Integer, TreeMap<Integer, String>>> iteratorRow = rows.iterator();

		while (iteratorRow.hasNext()) {

			Entry<Integer, TreeMap<Integer, String>> row = iteratorRow.next();

			Set<Entry<Integer,String>> cols = row.getValue().entrySet();

			Iterator<Entry<Integer, String>> iteratorCol = cols.iterator();

			StringBuilder sb = new StringBuilder();
			while (iteratorCol.hasNext()) {
				sb.append(iteratorCol.next().getValue());

				if (iteratorCol.hasNext())
					sb.append(';');
			}

			values.add(sb.toString());

		}

		return values;
	}

}