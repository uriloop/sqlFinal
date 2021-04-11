package com.company;


import java.util.Scanner;

public class Main {
    static Scanner scan= new Scanner(System.in);
    static Imprimir print= new Imprimir();
    public static void main(String[] args) {
        BaseDeDatos db = BaseDeDatos.get();

        // db.deleteTables();
        db.createTables();
        String comanda="llista";
        while ( true){
            if (comanda.length()==1){
                db.lletres(comanda);
            }else if (comanda.equals("llista")) {
                print.titol("  ─────── CONTACTES ───────","groc");
                db.selectContactes();
            }else if (comanda.equals("busca")){
                db.buscaContacte();
            }else if (comanda.equals("apaga")){
                break;
            }else if (comanda.equals("dracarys")){
                db.deleteTables();
                db.createTables();
                print.titol("Tot s'ha reduït a cendres!!!!!", "vermell");
            }else if (comanda.equals("nou")){
                db.insertContacte();
            }else{
                if (db.existeixContacte(comanda)){
                    db.mostraContacte(comanda);// mostra contacte
                }else{
                    print.titol("Comanda erronea !!!","vermell");
                    print.titol("Les comandes son:","verd");
                    print.titol(" <llista> <busca> <nou> <lletra> <nom+ +cognom> <apaga>","verd");

                }
            }
            print.titol("- Introdueix la comanda -","blanc");
            System.out.println();
            comanda=scan.nextLine();
        }
        print.titol("Fins aviat!","verd");
        System.out.println();


    /*    db.insertEstudiante("si", 2);
        db.insertEstudiante("ola", 4);
        db.insertEstudiante("ios", 6);

        System.out.println("TODOS");

        List<Contacto> contactoList1 = db.selectEstudiantes();

        for(Contacto contacto : contactoList1) {
            System.out.println(contacto.nom + " : " + contacto.nota);
        }




        System.out.println("CON NOTA SUPERIOR A 3");

        List<Contacto> contactoList2 = db.selectEstudiantesConNotaSuperiorA(3);

        for(Contacto contacto : contactoList2) {
            System.out.println(contacto.nom + " : " + contacto.nota);
        }*/
    }
}
