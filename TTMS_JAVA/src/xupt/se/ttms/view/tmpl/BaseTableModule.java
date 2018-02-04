package xupt.se.ttms.view.tmpl;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class BaseTableModule extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	@SuppressWarnings("rawtypes")
	Vector<Vector> rows;// 定义行
	Vector<String> colums;// 定义列

	public BaseTableModule(String[] params, @SuppressWarnings("rawtypes") Vector<Vector> vector) {
		// 初始化列
		this.colums = new Vector<String>();
		for (String colum : params) {
			colums.add(colum);
		}

		// 初始化行
		this.rows = vector;
	}

	@Override
	public String getColumnName(int column) {
		return this.colums.get(column);
	}

	@Override
	public int getColumnCount() {
		return this.colums.size();
	}

	@Override
	public int getRowCount() {
		return this.rows.size();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return ((Vector) rows.get(rowIndex)).get(columnIndex);
	}
	
	public boolean addTableItem() {
		return true;
	}
}
