package com.example.projektjava.model;

public class Printer<T> {
    T printThing;

    public T getPrintThing() {
        return printThing;
    }

    public void setPrintThing(T printerThing) {
        this.printThing = printerThing;
    }

    public Printer(T printThing) {
        this.printThing = printThing;
    }

    public void printIt(){
        System.out.println(printThing);
    }
}
