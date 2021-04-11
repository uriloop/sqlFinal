package com.company;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BaseDeDatos {

    static final String url = "jdbc:sqlite:database.db";
    static BaseDeDatos instance;
    static Connection connection;


    public static BaseDeDatos get(){            //  Conectamos a la base de datos y le pedimos info
        if(instance == null){
            instance = new BaseDeDatos();

            try {
                connection = DriverManager.getConnection(url);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        }
        return instance;
    }


    void deleteTables(){
        try (Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS contacte");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    void createTables(){
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS contacte (nom text,cognom text,telefon text,mail text)");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public boolean existeixContacte(String comanda) {
        // convertir comanda a nom + cognom
        boolean cog=false;
        String nom="";
        String cognom="";
        for (int i = 0; i < comanda.length(); i++) {
            if (!cog){
                if (comanda.charAt(i)==' '){
                    cog=true;
                }else{
                    nom=nom+comanda.charAt(i);
                }
            }else{
                cognom=cognom+comanda.charAt(i);
            }
        }

        String sql = "SELECT nom, cognom FROM contacte WHERE nom LIKE ? and cognom LIKE ?";
        try (PreparedStatement preparedStatement  = connection.prepareStatement(sql)){
            preparedStatement.setString(1, nom);
            preparedStatement.setString(2,cognom);
            ResultSet resultSet  = preparedStatement.executeQuery();
            String nom1 = resultSet.getString("nom");
            String cognom1 = resultSet.getString("cognom");


        } catch (SQLException e) {

            Main.print.titol("no hi ha cap contacte amb aquest nom.","groc");
            return false;
        }
        return true;


    }


    public void mostraContacte(String comanda) {

        // convertir comanda a nom + cognom
        boolean cog=false;
        String nom="";
        String cognom="";
        for (int i = 0; i < comanda.length(); i++) {
            if (!cog){
                if (comanda.charAt(i)==' '){
                    cog=true;
                }else{
                    nom+=comanda.charAt(i);
                }
            }else{
                cognom+=comanda.charAt(i);
            }
        }
        String sql = "SELECT * FROM contacte WHERE nom LIKE ? and cognom LIKE ?";
        Contacto contacte= new Contacto();

        try (PreparedStatement preparedStatement  = connection.prepareStatement(sql)){
            preparedStatement.setString(1, nom);
            preparedStatement.setString(2, cognom);
            ResultSet resultSet  = preparedStatement.executeQuery();
            contacte.nom = resultSet.getString("nom");
            contacte.cognom = resultSet.getString("cognom");
            contacte.telefon = resultSet.getString("telefon");
            contacte.mail = resultSet.getString("mail");


        } catch (SQLException e) {
            Main.print.titol(e.getMessage(),"vermell");


        }

        // imprimir contacte

        Main.print.titol(contacte.nom+" "+contacte.cognom,"verd");
        Main.print.titol(contacte.telefon,"verd");
        Main.print.titol(contacte.mail,"verd");

        Main.print.titol("Per eliminar: <elimina>","vermell");
        Main.print.titol("Per tornar: <anything>","verd");

        if (Main.scan.nextLine().equals("elimina")){
            eliminaContacte(contacte.nom,contacte.cognom);
        }

    }


    private void eliminaContacte(String nom, String cognom) {

        String sql = "DELETE FROM contacte WHERE nom LIKE ? and cognom LIKE ?";
        try (PreparedStatement preparedStatement  = connection.prepareStatement(sql)){
            preparedStatement.setString(1, nom);
            preparedStatement.setString(2, cognom);
            preparedStatement.execute();
            Main.print.titol("Contacte eliminat!","groc");



        } catch (SQLException e) {

            System.out.println("El contacte no existeix!?   error a l'esborrar");
            Main.print.titol("El contacte no existeix!?   error a l'esborrar", "vermell");
        }
    }


    public void selectContactes() {
        String sql = "SELECT nom, cognom FROM contacte order by nom collate nocase";

        List<Contacto> llistaContactes = new ArrayList<>();
        try (PreparedStatement preparedStatement  = connection.prepareStatement(sql)){

            ResultSet resultSet  = preparedStatement.executeQuery();
            while (resultSet.next()) {
                llistaContactes.add(new Contacto(resultSet.getString("nom"), resultSet.getString("cognom")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        if (llistaContactes.isEmpty()){
            Main.print.titol("(No hi ha contactes guardats)","groc");
            System.out.println();
        }
        for (Contacto i : llistaContactes) {
            String str = " - " + i.nom + " " + i.cognom;
            Main.print.titol(str, "verd");

        }

    }


    public void insertContacte() {
        Demana demana= new Demana();

        Main.print.titol("Introdueix el nom:","blanc");
        String nom= demana.obligat(Main.scan.nextLine());
        Main.print.titol("Introdueix el cognom:","blanc");
        String cognom= demana.obligat(Main.scan.nextLine());
        String comand= nom+" "+cognom;

        // comprovem que no existeix
        if (existeixContacte(comand)){
            Main.print.titol("Aquest contacte ja existeix","groc");
            System.out.println();
            mostraContacte(comand);
        }else{
            Main.print.titol("Introdueix un telefon (opcional)", "blanc");
            String telefon = demana.opcional(Main.scan.nextLine());
            Main.print.titol("Introdueix un mail (opcional)","blanc");
            String mail = demana.opcional(Main.scan.nextLine());

            Main.print.titol("El contacte es diu: "+nom+" "+cognom,"verd");
            Main.print.titol("El telefon és: "+telefon,"verd");
            Main.print.titol("El mail és: "+mail,"verd");

            Main.print.titol("Vols guardar-lo?    si/no","blanc");
            if (Main.scan.nextLine().equals("si")){
                // xixa
                String sql = "INSERT INTO contacte (nom,cognom,telefon,mail) VALUES(?,?,?,?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setString(1,nom);  // Element 1 de la taula
                    preparedStatement.setString(2,cognom);     // Element 2 de la taula
                    preparedStatement.setString(3,telefon);     // Element 2 de la taula
                    preparedStatement.setString(4,mail);     // Element 2 de la taula

                    preparedStatement.executeUpdate();                      // update de la info com a nova row de la taula
                    System.out.println();
                    Main.print.titol("Contacte guardat amb exit!","verd");
                } catch (SQLException e) {
                    Main.print.titol("Alguna cosa no ha funcionat...","vermell");
                }

            }
        }
    }

    public void buscaContacte() {

        String busqueda;
        Main.print.titol("Escriu una cadena de caracters a buscar","blanc");
        busqueda=Main.scan.nextLine();
        boolean busquedaDoble=false;
        String nom="";String cognom="";
        for (int i = 0; i < busqueda.length(); i++) {

            if(busqueda.charAt(i)==' '){
                busquedaDoble=true;
            }else if (!busquedaDoble){
                nom+=busqueda.charAt(i);
            }else{
                cognom+=busqueda.charAt(i);
            }
        }
        if (!busquedaDoble){
            busqueda="%"+busqueda+"%";
            String sql = "SELECT nom, cognom FROM contacte WHERE nom LIKE ? collate noaccents OR cognom LIKE ? collate noaccents order by nom";

            List<Contacto> llistaContactes = new ArrayList<>();
            try (PreparedStatement preparedStatement  = connection.prepareStatement(sql)){
                preparedStatement.setString(1,busqueda);
                preparedStatement.setString(2,busqueda);
                ResultSet resultSet  = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    llistaContactes.add(new Contacto(resultSet.getString("nom"), resultSet.getString("cognom")));
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }


            if (llistaContactes.isEmpty()){

                Main.print.titol("(No hi ha contactes amb aquests parametres de búsqueda)","groc");

            }

            for (Contacto i : llistaContactes){
                String str=" - "+i.nom+" "+i.cognom;
                Main.print.titol(str,"verd");

            }
        }else{
            nom="%"+nom+"%";
            cognom="%"+cognom+"%";
            String sql = "SELECT nom, cognom FROM contacte WHERE nom LIKE ? collate noaccents OR cognom LIKE ? collate noaccents order by nom";
            List<Contacto> llistaContactes = new ArrayList<>();
            try (PreparedStatement preparedStatement  = connection.prepareStatement(sql)){
                preparedStatement.setString(1,nom);
                preparedStatement.setString(2,cognom);
                ResultSet resultSet  = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    llistaContactes.add(new Contacto(resultSet.getString("nom"), resultSet.getString("cognom")));
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }


            if (llistaContactes.isEmpty()){

                Main.print.titol("(No hi ha contactes amb aquests parametres de búsqueda)","groc");

            }

            for (Contacto i : llistaContactes){
                String str=" - "+i.nom+" "+i.cognom;
                Main.print.titol(str,"verd");

            }
        }



    }

    public void lletres(String comanda) {
        String abcd="qwertyuiopasdfghjklñzxcvbnmQWERTYUIOPASDFGHJKLÑZXCVBNM";
        String lletra=comanda;
        comanda+="%";
        // comprovem que sigui una lletra
        boolean hoEs=false;
        for (int i = 0; i < abcd.length(); i++) {
            if (abcd.charAt(i)==lletra.charAt(0)){
                hoEs=true;
            }
        }
        lletra=lletra.toUpperCase();
        if(hoEs){
            String sql = "SELECT nom, cognom FROM contacte WHERE nom LIKE ? collate noaccents order by nom";

            List<Contacto> llistaContactes = new ArrayList<>();
            try (PreparedStatement preparedStatement  = connection.prepareStatement(sql)){
                preparedStatement.setString(1,comanda);
                ResultSet resultSet  = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    llistaContactes.add(new Contacto(resultSet.getString("nom"), resultSet.getString("cognom")));
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }


            if (llistaContactes.isEmpty()){

                Main.print.titol("(No hi ha contactes a la lletra \""+lletra+"\")","groc");

            }

            for (Contacto i : llistaContactes){
                String str=" - "+i.nom+" "+i.cognom;
                Main.print.titol(str,"verd");

            }
        }else{
            Main.print.titol("Només es permeten lletres  !!!", "vermell");
        }



    }
}