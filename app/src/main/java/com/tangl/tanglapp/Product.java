package com.tangl.tanglapp;

abstract class Product {
    private final Float type; // will change
    private final String name;//
    private final String desc;
    private final String instructions;



    /**
     * general class to represent products that would be used in the routine
     * @param type the product type will help determine the order that it is displayed in a routine
     * @param name product name in the string.xml file
     * @param desc link to string.xml value a description for the product
     * @param instructions how to use the product
     * */
    public Product(Float type, String name, String desc, String instructions){

        this.type = type;
        this.name = name;
        this.desc = desc;
        this.instructions = instructions;

    }


    Float getType() {
        return type;
    }

    String getName() {
        return name;
    }

    String getDesc() {
        return desc;
    }

    String getInstructions() {
        return instructions;
    }

}
