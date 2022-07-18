package com.czhao.test.jdk14;

/**
 * JEP 358: Helpful NullPointerExceptions
 *
 * @author zhaochun
 */
public class TestHelpfulNullPointerExceptions {
    public static void main(String[] args) {
        Department department = new Department(null);
        Employee employee = new Employee(department);
        System.out.println(employee.department().company().name());
    }

    private record Company(String name) {
    }

    private record Department(Company company) {
    }

    private record Employee(Department department) {
    }
}
