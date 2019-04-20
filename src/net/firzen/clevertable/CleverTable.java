package net.firzen.clevertable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CleverTable {
	private List<Class<?>> types = new ArrayList<Class<?>>();
	private List<String> headers = new ArrayList<String>();
	private List<List<Comparable<?>>> data = new ArrayList<List<Comparable<?>>>();
	private List<Printer> printers = new ArrayList<Printer>();
	
	// -------------------------------------------------------------------------	
	// Getters and setters
	// -------------------------------------------------------------------------
	
	public void removeAll() {
		data = new ArrayList<List<Comparable<?>>>();
	}
	
	public List<Printer> getPrinters() {
		return printers;
	}
	
	public void setPrinter(String columnHeader, Printer printer) {
		setPrinter(getColumnIndexByHeader(columnHeader), printer);
	}
	
	public void setPrinter(int column, Printer printer) {
		printers.set(column, printer);
	}

	public void setPrinters(List<Printer> printers) {
		this.printers = printers;
	}
	
	public void setPrinters(Printer ... printers) {
		setPrinters(getModificableList(printers));
	}
	
	public void addRow(List<Comparable<?>> row) {
		if(isRightRow(row)) {
			data.add(row);
		}
	}

	public void addRow(Comparable<?> ... row) {
		List<Comparable<?>> r = getModificableList(row); 
		if(isRightRow(r)) {
			data.add(r);
		}
	}
	
	public List<String> getHeaders() {
		return headers;
	}

	public void setHeaders(List<String> headers) {
		this.headers = headers;
		int size = headers.size();
		for(int i = 0; i < size; i++) {
			printers.add(null);
		}
	}

	public void setHeaders(String ... headers) {
		setHeaders(getModificableList(headers));
	}
	
	public List<Class<?>> getTypes() {
		return types;
	}

	public void setTypes(List<Class<?>> types) {
		this.types = types;
	}

	public void setTypes(Class<?> ... types) {
		this.types = getModifiableList(types);
	}
	
	public int getColumnCount() {
		return headers.size();
	}
	
	public int getRowCount() {
		return data.size();
	}
	
	public List<Comparable<?>> getColumn(String columnHeader) {
		return getColumn(getColumnIndexByHeader(columnHeader));
	}
	
	public List<Comparable<?>> getColumn(int index) {
		List<Comparable<?>> output = new ArrayList<Comparable<?>>();
		for(List<Comparable<?>> row : data) {
			output.add(row.get(index));
		}
		return output;
	}
	
	public List<Comparable<?>> getRow(int index) {
		return data.get(index);
	}
	
	public Comparable<?> getCell(int column, int row) {
		return getRow(row).get(column);
	}

	public void setRow(int index, Comparable<?> ... row) {
		setRow(index, getModificableList(row));
	}
	
	public void setRow(int index, List<Comparable<?>> row) {
		if(isRightRow(row)) {
			data.set(index, row);
		}
	}

	public void setCell(int column, int row, Comparable<?> cell) {
		if(types.get(column).equals(cell.getClass())) {
			getRow(row).set(column, cell);
		}
	}

	public void setColumn(String columnHeader, Comparable<?> ... column) {
		setColumn(columnHeader, getModificableList(column));
	}

	public void setColumn(String columnHeader, List<Comparable<?>> column) {
		setColumn(getColumnIndexByHeader(columnHeader), column);
	}
	
	public void setColumn(int index, Comparable<?> ... column) {
		setColumn(index, getModificableList(column));
	}

	public void setColumn(int index, List<Comparable<?>> column) {
		if(isRightColumn(column, index)) {
			int size = this.getRowCount();
			for(int i = 0; i < size; i++) {
				setCell(index, i, column.get(i));
			}
		}
	}
	
	public void insertRow(int index, Comparable<?> ... row) {
		List<Comparable<?>> list = getModificableList(row);
		if(isRightRow(list)) {
			data.add(index, list);
		}
	}
	
	public void insertRow(int index, List<Comparable<?>> row) {
		if(isRightRow(row)) {
			data.add(index, row);
		}
	}

	public void addColumn(String columnHeader, Comparable<?> ... column) {
		insertColumn(getColumnCount(), columnHeader, column);
	}
	
	public void addColumn(String columnHeader, List<Comparable<?>> column) {
		insertColumn(getColumnCount(), columnHeader, column);		
	}
	
	public void insertColumn(int index, String columnHeader,
			Comparable<?> ... column) {
		insertColumn(index, columnHeader, getModificableList(column));
	}

	public void insertColumn(int index, String columnHeader,
			List<Comparable<?>> column) {
		if(isSameType(column)) {
			addHeader(index, columnHeader, column.get(0).getClass());
			int size = this.getRowCount();
			for(int i = 0; i < size; i++) {
				getRow(i).add(index, column.get(i));
			}
		}
	}
	
	public void deleteRow(int index) {
		data.remove(index);
	}
	
	public void deleteColumn(int column) {
		headers.remove(column);
		types.remove(column);
		printers.remove(column);
		int size = this.getRowCount();
		for(int i = 0; i < size; i++) {
			getRow(i).remove(column);
		}
	}
	
	public void deleteColumn(String columnHeader) {
		deleteColumn(getColumnIndexByHeader(columnHeader));
	}
	
	// -------------------------------------------------------------------------
	// Sorting methods
	// -------------------------------------------------------------------------
	
	public void sortByColumn(String columnHeader) {
		sortByColumn(getColumnIndexByHeader(columnHeader), true);
	}
	
	public void sortByColumn(String columnHeader, boolean descending) {
		sortByColumn(getColumnIndexByHeader(columnHeader), descending);
	}
	
	public void sortByColumn(final int index) {
		sortByColumn(index, true);
	}
	
	@SuppressWarnings("unchecked")
	public void sortByColumn(final int index, boolean descending) {
		Comparator<List<Comparable<?>>> cmp;
		if(descending) {
			cmp = new Comparator<List<Comparable<?>>>() {
				public int compare(List<Comparable<?>> o1, List<Comparable<?>> o2) {
					return ((Comparable<Object>)o2.get(index)).compareTo((Comparable<Object>)o1.get(index));
				}
			};
		}
		else {
			cmp = new Comparator<List<Comparable<?>>>() {
				public int compare(List<Comparable<?>> o1, List<Comparable<?>> o2) {
					return ((Comparable<Object>)o1.get(index)).compareTo((Comparable<Object>)o2.get(index));
				}
			};
		}
		
		Collections.sort(data, cmp);
	}
	
	// -------------------------------------------------------------------------
	// Private methods
	// -------------------------------------------------------------------------

	private List<Class<?>> getModifiableList(Class<?>[] array) {
		List<Class<?>> output = new ArrayList<Class<?>>();
		int size = array.length;
		for(int i = 0; i < size; i++) {
			output.add(array[i]);
		}
		return output;
	}
	
	private List<Printer> getModificableList(Printer[] array) {
		List<Printer> output = new ArrayList<Printer>();
		int size = array.length;
		for(int i = 0; i < size; i++) {
			output.add(array[i]);
		}
		return output;
	}

	private List<String> getModificableList(String[] array) {
		List<String> output = new ArrayList<String>();
		int size = array.length;
		for(int i = 0; i < size; i++) {
			output.add(array[i]);
		}
		return output;
	}
	
	private List<Comparable<?>> getModificableList(Comparable<?>[] array) {
		List<Comparable<?>> output = new ArrayList<Comparable<?>>();
		int size = array.length;
		for(int i = 0; i < size; i++) {
			output.add(array[i]);
		}
		return output;
	}
	
	private void addHeader(int index, String name, Class<?> type) {
		headers.add(index, name);
		types.add(index, type);
		printers.add(index, null);
	}
	
	private boolean isSameType(List<Comparable<?>> list) {
		if(list != null && list.size() > 0) {
			Class<?> type = list.get(0).getClass();
			for(Comparable<?> el : list) {
				if(!el.getClass().equals(type)) {
					return false;
				}
			}
			return true;
		}
		else {
			return false;
		}
	}
	
	private int getColumnIndexByHeader(String columnHeader) {
		int i = 0;
		for(String header : headers) {
			if(header.equals(columnHeader)) {
				return i;
			}
			i++;
		}
		return -1;
	}
	
	private boolean isRightRow(List<Comparable<?>> row) {
		int size = this.getColumnCount();
		if(row.size() == size) {
			for(int i = 0; i < size; i++) {
				if(row.get(i) == null || !row.get(i).getClass().equals(types.get(i))) {
					return false;
				}
			}
		}
		return true;
	}
	
	private boolean isRightColumn(List<Comparable<?>> column, int forIndex) {
		if(column.size() == getRowCount()) {
			Class<?> columnType = this.getColumnType(forIndex);
			for(Comparable<?> cell : column) {
				if(!cell.getClass().equals(columnType)) {
					return false;
				}
			}
			return true;
		}
		else {
			return false;
		}
	}
	
	// -------------------------------------------------------------------------
	// Other methods
	// -------------------------------------------------------------------------

	public Class<?> getColumnType(int index) {
		return types.get(index);
	}

	public Class<?> getColumnType(String columnHeader) {
		return types.get(getColumnIndexByHeader(columnHeader));
	}
	
	// -------------------------------------------------------------------------
	// Data export
	// -------------------------------------------------------------------------
	
	private List<String> rowToStrings(List<Comparable<?>> row) {
		List<String> output = new ArrayList<String>();
		int column = 0;
		for(Comparable<?> cell : row) {
			Printer printer = printers.get(column);

			if(printer == null) {
				output.add(cell.toString());
			}
			else {
				output.add(printer.printAs(cell));
			}
			column++;
		}
		return output;
	}
	
	public List<List<String>> getData() {
		List<List<String>> output = new ArrayList<List<String>>();
		for(List<Comparable<?>> row : data) {
			output.add(rowToStrings(row));
		}
		return output;
	}
	
	// -------------------------------------------------------------------------
	// Eye candy visualisation
	// -------------------------------------------------------------------------
	
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		
		final String columnSeparator = " | ";
		
		for(String header : headers) {
			buffer.append(getCellString(header, getColumnWidth(header)));
			buffer.append(columnSeparator);
		}
		
		buffer.append("\n");
		for(int i = 0; i < this.getColumnCount(); i++) {
			for(int j = 0; j < getColumnWidth(i) + columnSeparator.length(); j++) {
				buffer.append("-");
			}
		}
		buffer.append("\n");
		
		List<List<String>> data = getData();

		for(List<String> row : data) {
			int i = 0;
			for(String cell : row) {
				buffer.append(getCellString(cell, getColumnWidth(i)));
				buffer.append(columnSeparator);
				i++;
			}
			buffer.append("\n");
		}
		
		return buffer.toString();
	}
	
	private String getCellString(String text, int length) {
		StringBuilder buffer = new StringBuilder();
		buffer.append(text);
		
		int l = length - text.length();
		for(int i = 0; i < l; i++) {
			buffer.append(" ");
		}
		
		return buffer.toString(); 
	}

	private int getColumnWidth(int columnIndex) {
		int max = this.getHeaders().get(columnIndex).length();
		
		for(Comparable<?> cell : this.getColumn(columnIndex)) {
			if(cell.toString().length() > max) {
				max = cell.toString().length();
			}
		}
		
		return max;
	}
	
	private int getColumnWidth(String columnHeader) {
		return getColumnWidth(getColumnIndexByHeader(columnHeader));
	}
}
