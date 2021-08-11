package org.example.pitplayground;

public class MainClass {
    public static String StaticField = "123";

    public static void main(String[] args) throws IllegalAccessException {
        System.out.println("Hello World");
        String FunctionalField = "456";

        if (StaticField.equals("123") && FunctionalField.equals("456")){
            System.out.println("IF STATEMENT PASSED");
        }
        else {
            throw new IllegalAccessException("IF STATEMENT FAILED");
        }

        if (StaticField.replace("1","0").equals("123") && FunctionalField.equals("456")){
            System.out.println("IF STATEMENT PASSED");
        }
        else {
            throw new IllegalAccessException("IF STATEMENT FAILED");
        }

        if (FunctionalField.equals("456")){
            System.out.println("IF STATEMENT PASSED");
        }
        else{
            throw new IllegalAccessException("IF STATEMENT FAILED");
        }
    }
}
