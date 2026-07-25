/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fungsi;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Custom table cell renderer untuk mewarnai baris tabel kasir rawat jalan
 * berdasarkan status pasien dan status pembayaran.
 * 
 * @author Owner
 */
public class WarnaTableKasirRalan extends DefaultTableCellRenderer {

    // --- Konstanta warna untuk berbagai kondisi ---
    private static final Color COLOR_ROW_EVEN = new Color(255, 255, 255);
    private static final Color COLOR_ROW_ODD  = new Color(255, 244, 244);
    private static final Color COLOR_TEXT_DEFAULT = new Color(50, 50, 50);
    
    // Warna untuk status "Sudah" dilayani tapi belum bayar 
    private static final Color COLOR_STATUS_SUDAH_BG = new Color(200, 0, 0);
    private static final Color COLOR_STATUS_SUDAH_FG = new Color(255, 230, 230);
    
    // Warna untuk status "Batal"
    private static final Color COLOR_STATUS_BATAL_BG = new Color(110, 110, 110);
    private static final Color COLOR_STATUS_BATAL_FG = new Color(255, 255, 255);
    
    // Warna untuk status terminal ("Dirujuk", "Meninggal", "Pulang Paksa")
    private static final Color COLOR_STATUS_TERMINAL_BG = new Color(152, 152, 156);
    private static final Color COLOR_STATUS_TERMINAL_FG = new Color(245, 245, 255);
    
    // Warna untuk status "Dirawat"
    private static final Color COLOR_STATUS_DIRAWAT_BG = new Color(119, 221, 119);
    private static final Color COLOR_STATUS_DIRAWAT_FG = new Color(245, 255, 245);
    
    // Warna untuk status "Sudah Bayar" tetapi status pelayanan "Belum Dilayani"
    private static final Color COLOR_STATUS_BELUM_BG = new Color(255, 172, 28);
    private static final Color COLOR_STATUS_BELUM_FG = new Color(0, 0, 0);
    
    
    // Warna untuk status pembayaran "Sudah Bayar" dan "Sudah Dilayani" (prioritas tertinggi)
    private static final Color COLOR_STATUS_BAYAR_BG = new Color(0, 167, 95);
    private static final Color COLOR_STATUS_BAYAR_FG = new Color(0, 0, 0);

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        Component component = super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);
        
        // 1. Set warna dasar bergantian untuk baris genap/ganjil
        if (row % 2 == 1) {
            component.setBackground(COLOR_ROW_ODD);
        } else {
            component.setBackground(COLOR_ROW_EVEN);
        }
        component.setForeground(COLOR_TEXT_DEFAULT);
        
        // 2. Terapkan warna berdasarkan status pelayanan (kolom indeks 10)
        String statusPelayanan = table.getValueAt(row, 10).toString();
        applyStatusPelayananColor(component, statusPelayanan);
        
        // 3. Terapkan warna berdasarkan status pembayaran (kolom indeks 15)
        //    Status pembayaran memiliki prioritas tertinggi
        String statusBayar = table.getValueAt(row, 15).toString();
        if ("Sudah Bayar".equals(statusBayar)&& "Sudah".equals(statusPelayanan) ) {
            component.setBackground(COLOR_STATUS_BAYAR_BG);
            component.setForeground(COLOR_STATUS_BAYAR_FG);
        }
        if ("Sudah Bayar".equals(statusBayar)&& "Belum".equals(statusPelayanan) ) {
            component.setBackground(COLOR_STATUS_BELUM_BG);
            component.setForeground(COLOR_STATUS_BELUM_FG);
        }
       
        
        return component;
    }
    
    /**
     * Mengatur warna komponen berdasarkan status pelayanan pasien.
     * 
     * @param component komponen yang akan diwarnai
     * @param status    status pelayanan (dari kolom ke-10)
     */
    private void applyStatusPelayananColor(Component component, String status) {
        switch (status) {
            case "Sudah":
                component.setBackground(COLOR_STATUS_SUDAH_BG);
                component.setForeground(COLOR_STATUS_SUDAH_FG);
                break;
            case "Batal":
                component.setBackground(COLOR_STATUS_BATAL_BG);
                component.setForeground(COLOR_STATUS_BATAL_FG);
                break;
            case "Dirujuk":
            case "Meninggal":
            case "Pulang Paksa":
                component.setBackground(COLOR_STATUS_TERMINAL_BG);
                component.setForeground(COLOR_STATUS_TERMINAL_FG);
                break;
            case "Dirawat":
                component.setBackground(COLOR_STATUS_DIRAWAT_BG);
                component.setForeground(COLOR_STATUS_DIRAWAT_FG);
                break;
            default:
                // Biarkan warna dasar (tidak ada perubahan tambahan)
                break;
        }
    }
}