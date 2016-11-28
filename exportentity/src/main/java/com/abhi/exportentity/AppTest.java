package com.abhi.exportentity;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

import com.abhi.exportentity.api.Attribute;
import com.abhi.exportentity.api.AttributeFormatter;
import com.abhi.exportentity.api.FlatDataExportService;
import com.abhi.exportentity.impl.Excel2003ExportService;
import com.abhi.exportentity.impl.SeparatorBasedExportService;

public class AppTest {

	public static void main(final String[] args) throws Exception {
		final Employee e = new Employee();
		e.name = "Abhishek";
		e.address = "Indore";
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

}
