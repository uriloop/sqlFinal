package com.company;

public class Imprimir {
    public static final String reset = "\u001B[0m";
    public static final String vermell = "\u001B[31m";
    public static final String blanc = "\u001B[0;80m";
    public static final String verd = "\u001B[32m";
    public static final String negre = "\033[0;90m";
    public static final String groc = "\033[0;33m";
    public static final String grocBold = "\033[0;33m";
    public static final String verdBold = "\033[1;32m";
    int ample=50;
    int alt=20;

    void titol(String text,String color){
        int spacesXcostat= (ample-text.length())/2;
        for (int i = 0; i < spacesXcostat; i++) {
            System.out.print(" ");
        }


        switch (color) {
            case "verd":
                System.out.print(verd+text+reset);
                break;
            case "groc":
                System.out.print(groc+text+reset);
                break;
            case "blanc":
                System.out.print(blanc+text+reset);
                break;
            case "vermell":
                System.out.print(vermell+text+reset);
                break;
            case "verdBold":
                System.out.println(verdBold+text+reset);
        }
        for (int i = 0; i < spacesXcostat; i++) {
            System.out.print(" ");
        }
        System.out.println();

    }



}
