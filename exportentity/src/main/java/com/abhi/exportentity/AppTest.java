package com.abhi.exportentity;

import com.abhi.exportentity.api.*;
import com.abhi.exportentity.impl.Excel2003ExportService;
import com.abhi.exportentity.impl.SeparatorBasedExportService;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AppTest {

    public static void main(final String[] args) throws Exception {
        final Employee e = new Employee();
        e.name = "Abhishek";
        e.address.city = "Pune";
        e.address.street = "Airport Road";
        e.address.builingNumber = 96;
        e.age = 28;
        e.phoneNumbers.add("123456");
        e.phoneNumbers.add("294872894");
        e.phoneNumbers.add("5757657");
        e.phoneNumbers.add("57576sdfs57");
        e.phoneNumbersMap = null;

        final Employee e2 = new Employee();
        e2.name = "Soni";
        e2.address = null;
        e2.age = 23;
        e2.phoneNumbers = null;
        e2.phoneNumbersMap.put("PhoneNumber1", "21313321");
        e2.phoneNumbersMap.put("PhoneNumber2", "4564645");
        e2.phoneNumbersMap.put("PhoneNumber3", "754465464");


        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        FlatDataExportService<Employee> service = new SeparatorBasedExportService<>("|", Employee.class, outputStream);
        service.exportHeader(new ArrayList<>(e2.phoneNumbersMap.keySet()), true);
        service.export(e);
        service.export(e2);
        System.out.println(outputStream.toString());
        service.release();

        service = new Excel2003ExportService<>(Employee.class, new FileOutputStream("text.xlsx"));
        service.exportHeader(new ArrayList<>(e2.phoneNumbersMap.keySet()), true);
        service.export(e);
        service.export(e2);
        service.release();
    }

    public static class Employee {

        @Attribute(headerLabel = "Name", order = 0)
        String name;

        @Attribute(headerLabel = "Address", order = 1)
        @ComplexAtrribute
        Address address = new Address();

        @Attribute(headerLabel = "Age", order = 2, formatter = AgeFormatter.class)
        int age;

        @Attribute(headerLabel = "PhoneNumber", order = 3)
        @CollectionAttribute
        List<String> phoneNumbers = new ArrayList<>();

        @Attribute(headerLabel = "PhoneNumber", order = 4)
        @CollectionAttribute
        Map<String, String> phoneNumbersMap = new LinkedHashMap<>();
    }

    public static class Address {
        @Attribute(headerLabel = "City", order = 0)
        String city;

        @Attribute(headerLabel = "Street", order = 1)
        String street;

        @Attribute(headerLabel = "Building Number", order = 2)
        int builingNumber;
    }

    public static class AgeFormatter implements AttributeFormatter {
        @Override
        public String format(final Object object) {
            return object.toString() + " Years";
        }
    }

}
