package org.example;
public class BoxDemoApp{
    public static void main(String[] args) {
        Box<Integer> intBox=new Box<>(123);
        Box<Integer> intBox2=new Box<>(234);
        intBox.setValue(222);
//        intBox2.setValue("abs");
        System.out.println(intBox.getValue()+intBox2.getValue());
    }
}