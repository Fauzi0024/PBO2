package crudDB;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.JOptionPane;

public class crudDB {
    
    private String jdbcURL = "jdbc:mysql://localhost:3306/2210020041_pendidikan";
    private String username = "root";
    private String password = "";
    
    private DefaultTableModel Modelnya;
    private TableColumn Kolomnya;
    
    public crudDB(){}
    
    public Connection getKoneksiDB() throws SQLException {
        try {
            Driver mysqldriver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(mysqldriver);
            System.out.println("Koneksi Berhasil");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        
        return DriverManager.getConnection(jdbcURL, username, password);
    }
    
    public boolean DuplicateKey(String NamaTabel, String PrimaryKey, String isiData) {
        boolean hasil = false;
        int jumlah = 0;
        try {
            String SQL = "SELECT * FROM " + NamaTabel + " WHERE " + PrimaryKey + " ='" + isiData + "'";
            Statement perintah = getKoneksiDB().createStatement();
            ResultSet hasilData = perintah.executeQuery(SQL);
            while (hasilData.next()) {
                jumlah++;
            }
            if (jumlah == 1) { hasil = true; } else { hasil = false; }
            
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return hasil;
    }

    public String getFieldTabel(String[] FieldTabelnya) {
        String hasilnya = "";
        int deteksiIndexAkhir = FieldTabelnya.length - 1;
        try {
            for (int i = 0; i < FieldTabelnya.length; i++) {
                if (i == deteksiIndexAkhir) {
                    hasilnya = hasilnya + FieldTabelnya[i];
                } else {
                    hasilnya = hasilnya + FieldTabelnya[i] + ",";
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return "(" + hasilnya + ")";
    }

    public String getIsiTabel(String[] IsiTabelnya) {
        String hasilnya = "";
        int DeteksiIndex = IsiTabelnya.length - 1;
        try {
            for (int i = 0; i < IsiTabelnya.length; i++) {
                if (i == DeteksiIndex) {
                    hasilnya = hasilnya + "'" + IsiTabelnya[i] + "'";
                } else {
                    hasilnya = hasilnya + "'" + IsiTabelnya[i] + "',";
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return "(" + hasilnya + ")";
    }
    
    public void SimpanDinamis(String NamaTabel, String[] Fieldnya, String[] Isinya) {
        try {
            
            int no_artikelIndex = -1;
            for (int i = 0; i < Fieldnya.length; i++) {
                if (Fieldnya[i].equals("No_Artikel")) {
                    no_artikelIndex = i;
                    break;
                }
            }

            if (no_artikelIndex != -1 && Isinya[no_artikelIndex].length() > 5) { // Sesuaikan dengan panjang yang dibutuhkan
                JOptionPane.showMessageDialog(null, "Data no artikel terlalu panjang. Maksimum 5 karakter.");
                return;
            }

            String SQLSave = "INSERT INTO " + NamaTabel + " " + getFieldTabel(Fieldnya) + " VALUES " + getIsiTabel(Isinya);
            Statement perintah = getKoneksiDB().createStatement();
            perintah.executeUpdate(SQLSave);
            perintah.close();
            JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    
    public String getFieldValueEdit(String[] Field, String[] value) {
        String hasil = "";
        int deteksi = Field.length - 1;
        try {
            for (int i = 0; i < Field.length; i++) {
                if (i == deteksi) {
                    hasil = hasil + Field[i] + " ='" + value[i] + "'";
                } else {
                    hasil = hasil + Field[i] + " ='" + value[i] + "',";
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        
        return hasil;
    }
    
    public void UbahDinamis(String NamaTabel, String PrimaryKey, String IsiPrimary, String[] Field, String[] Value) {
        try {
            String SQLUbah = "UPDATE " + NamaTabel + " SET " + getFieldValueEdit(Field, Value) + " WHERE " + PrimaryKey + "='" + IsiPrimary + "'";
            Statement perintah = getKoneksiDB().createStatement();
            perintah.executeUpdate(SQLUbah);
            perintah.close();
            getKoneksiDB().close();
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }
}
