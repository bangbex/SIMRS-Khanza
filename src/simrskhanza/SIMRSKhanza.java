/*
  Dilarang keras memperjualbelikan/mengambil keuntungan dari Software 
  ini dalam bentuk apapun tanpa seijin pembuat software
  (Khanza.Soft Media). Bagi yang sengaja membajak softaware ini ta
  npa ijin, kami sumpahi sial 1000 turunan, miskin sampai 500 turu
  nan. Selalu mendapat kecelakaan sampai 400 turunan. Anak pertama
  nya cacat tidak punya kaki sampai 300 turunan. Susah cari jodoh
  sampai umur 50 tahun sampai 200 turunan. Ya Alloh maafkan kami 
  karena telah berdoa buruk, semua ini kami lakukan karena kami ti
  dak pernah rela karya kami dibajak tanpa ijin.
 */
package simrskhanza;

import javax.swing.JOptionPane;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.LocalDate;
import usu.widget.util.WidgetUtilities;

/**
 *
 * @author khanzasoft
 */
public class SIMRSKhanza {
    
    private static String tanggalBuka = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        WidgetUtilities.invokeLater(() -> {
           frmUtama utama=frmUtama.getInstance();
           utama.isWall();
           utama.setVisible(true);
           
            //deteksi window yang terbuka
           deteksiWindowYangTerbuka(utama);
           
           //cek perubahan tanggal
           cekPerubahanTanggal();          
        }); 
    }
    
    // ---  KODE DETEKSI WINDOW APA SAJA YANG TERBUKA ---
    private static void deteksiWindowYangTerbuka(frmUtama utama){
        
        final String statusAwal = utama.jLabel7.getText();
        final java.awt.Font fontDefault = utama.jLabel7.getFont();
        
        java.awt.Toolkit.getDefaultToolkit().addAWTEventListener(new java.awt.event.AWTEventListener() {
            @Override
            public void eventDispatched(java.awt.AWTEvent event) {
                if (event.getID() == java.awt.event.WindowEvent.WINDOW_ACTIVATED) {
                    Object source = event.getSource();

                    if (source instanceof java.awt.Window) {
                        String fullPath = source.getClass().getName();
                        String statusTambahan = "-[[Package.file :  " + fullPath + "]]-";
                        
                        // Gunakan SwingUtilities agar update UI aman (Thread-Safe)
                        javax.swing.SwingUtilities.invokeLater(() -> {
                            utama.jLabel7.setText(statusAwal + statusTambahan);
                            utama.jLabel7.setFont(fontDefault.deriveFont(java.awt.Font.BOLD));

                        });
                        
                    }
                }
            }
        }, java.awt.AWTEvent.WINDOW_EVENT_MASK);
        // --------------------------------------

    }
    
    private static void cekPerubahanTanggal() {
        // Timer menggunakan Lambda: (e) -> { ... } 
        // Tidak perlu lagi menulis "new ActionListener" atau "@Override" secara manual
        Timer timer = new Timer(1800000, e -> {
            // Cara Modern di Java 15: Menggunakan LocalDate
            String tanggalSekarang = LocalDate.now().toString(); // Output otomatis: yyyy-MM-dd

            if (!tanggalBuka.equals(tanggalSekarang)) {
                ((Timer)e.getSource()).stop();

                int jawab = JOptionPane.showConfirmDialog(null, 
                        "Hari telah berganti. Restart aplikasi?", 
                        "Peringatan", JOptionPane.YES_NO_OPTION);

                if (jawab == JOptionPane.YES_OPTION) {
                    restartAplikasi();
                } else {
                    // Memberi tahu user dengan dialog
                    JOptionPane.showMessageDialog(null, "Peringatan akan diulang setiap 5 menit.");

                    // Ubah interval ke 5 menit
                    ((Timer)e.getSource()).setInitialDelay(300000);
                    ((Timer)e.getSource()).restart();
                }
            }
        });

        timer.start();
    } 
    
    private static void restartAplikasi() {
        // Menutup jendela saat ini dan menjalankan ulang Main Class
        System.exit(0); 
        // Catatan: Di lingkungan produksi, biasanya Anda memanggil script launcher 
        // atau menggunakan ProcessBuilder untuk membuka file .jar kembali.
    }
       
    
}
