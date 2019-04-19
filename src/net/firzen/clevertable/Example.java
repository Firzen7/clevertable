package net.firzen.clevertable;

public class Example {
	public static void main(String[] args) {
		CleverTable table = new CleverTable();
		table.setTypes(Integer.class, String.class, String.class);
		table.setHeaders("Id", "Name", "Surname");
		
		table.addRow(1, "Linus", "Torvalds");
		table.addRow(2, "Bill", "Gates");
		table.addRow(3, "Donald", "Knuth");
		
		System.out.println(table);

		table.sortByColumn("Name", false);

		System.out.println(table);
		
		System.out.println(table.getRow(0));
		System.out.println(table.getColumn(1));
		System.out.println(table.getColumn("Surname"));
		System.out.println(table.getCell(1, 2));
		
		System.out.println("\n" + table.getHeaders());
		System.out.println(table.getData());
		
		System.out.println(table.getTypes() + "\n");
		
		table.setCell(2, 0, "GATES");
		System.out.println(table);
		
		table.addRow(4, "Richard", "Stallman");
		System.out.println(table);
		
		table.addColumn("Nationality", "US", "US", "Finnish", "US");
		System.out.println(table);
		
		table.setPrinter(1, new Printer() {
			@Override
			public String printAs(Object o) {
				return ((String) o).toUpperCase();
			}
		});
		System.out.println(table);
		
		table.deleteRow(0);
		System.out.println(table);
		
		table.deleteColumn(1);
		System.out.println(table);
	}
}
