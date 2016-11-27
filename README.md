# Export-Entity
Export Java entities to various formats like Excel, CSV etc. Framework should be extensible easily for new formats.

CSV and EXCEL support is already available: 
-----------------------

	public static void main(final String[] args) throws Exception {
		final Employee e = new Employee();
		e.name = "Abhishek";
		e.address = "Indore";
		e.age = 28;

		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		FlatDataExportService<Employee> service = new SeparatorBasedExportService<>("|", Employee.class, outputStream);
		service.exportHeader();
		service.export(e);
		System.out.println(outputStream.toString());
		service.release();

		service = new Excel2003ExportService<>(Employee.class, new FileOutputStream("text.xlsx"));
		service.exportHeader();
		service.export(e);
		service.release();
	}

	public static class Employee {

		@Attribute(headerLabel = "Name", order = 0)
		String	name;

		@Attribute(headerLabel = "Address", order = 1)
		String	address;

		@Attribute(headerLabel = "Age", order = 2, formatter = AgeFormatter.class)
		int		age;
	}

	public static class AgeFormatter implements AttributeFormatter {
		@Override
		public String format(final Object object) {
			return object.toString() + " Years";
		}
	}

**Output:**

 Name|Address|Age  
 Abhishek|Indore|28 Years



