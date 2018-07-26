# Dev Tools
**[Export Entity](#exportEntity)**<br>
**[Performance Metrics Generator](#perfMetrics)**<br>
   
<a name="exportEntity"/>

## Export Entity
Export Java entities to various formats like Excel, CSV etc. Framework should be extensible easily for new formats.

#### CSV and EXCEL support is already available: 
-----------------------

	public static void main(final String[] args) throws Exception {
		final Employee e = new Employee();
		e.name = "Abhishek";
		e.address.city = "Pune";
		e.address.street = "Airport Road";
		e.address.builingNumber = 96;
		e.age = 28;

		final Employee e2 = new Employee();
		e2.name = "Soni";
		e2.address = null;
		e2.age = 23;

		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		FlatDataExportService<Employee> service = new SeparatorBasedExportService<>("|", Employee.class, outputStream);
		service.exportHeader();
		service.export(e);
		service.export(e2);
		System.out.println(outputStream.toString());
		service.release();

		service = new Excel2003ExportService<>(Employee.class, new FileOutputStream("text.xlsx"));
		service.exportHeader();
		service.export(e);
		service.export(e2);
		service.release();
	}

	public static class Employee {

		@Attribute(headerLabel = "Name", order = 0)
		String	name;

		@Attribute(headerLabel = "Address", order = 1)
		@ComplexAttribute
		Address	address	= new Address();

		@Attribute(headerLabel = "Age", order = 2, formatter = AgeFormatter.class)
		int		age;
	}

	public static class Address {
		@Attribute(headerLabel = "City", order = 0)
		String	city;

		@Attribute(headerLabel = "Street", order = 1)
		String	street;

		@Attribute(headerLabel = "Building Number", order = 2)
		int		builingNumber;
	}

	public static class AgeFormatter implements AttributeFormatter {
		@Override
		public String format(final Object object) {
			return object.toString() + " Years";
		}
	}

#### Output:
Name|City|Street|Building Number|Age  
Abhishek|Pune|Airport Road|96|28 Years  
Soni||||23 Years

<a name="perfMetrics"/>

## Performance Metrics Generator
Generic tool to generate performance metrics from any application logs. User can define template in tool to describe all internal activity steps which needs to be part of performance metrics.

#### Assumptions:
Application logs will have start and end logs for all internal activity steps with timestamp.

#### Note: Still in development 
