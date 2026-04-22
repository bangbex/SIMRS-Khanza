/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fungsi;

import AESsecurity.EnkripsiAES;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author khanzasoft
 */
public class koneksiDB {
    private static Connection connection = null;
    private static final Properties prop = new Properties();
    private static final MysqlDataSource dataSource = new MysqlDataSource();

    private static int loginAttempt = 0; 
    
    public static Connection condb() {
        try {
            if (connection == null || connection.isClosed()) {
                // Cek apakah sudah melebihi batas 3x
                if (loginAttempt >= 3) {
                    JOptionPane.showMessageDialog(null, "Gagal koneksi database . mencoba lagi...");
                    System.exit(0); // Exit program
                }

                String modeAplikasi = getVal("MODE", false);
                String host = "", port = "", db = "", user = "", pas = "";
                switch(modeAplikasi){
                    case "PROD":
                        host = getVal("HOST", false); 
                        port = getVal("PORT", false);
                        db   = getVal("DATABASE", false);
                        user = getVal("USER", false);
                        pas  = getVal("PAS", false);
                        break;
                    default: 
                        host = getVal("HOSTFORDEVELOPMENT", false);
                        port = getVal("PORTFORDEVELOPMENT", false);
                        db   = getVal("DATABASEFORDEVELOPMENT", false);
                        user = getVal("USERFORDEVELOPMENT", false);
                        pas  = getVal("PASFORDEVELOPMENT", false);
                        break;
                
                }

                dataSource.setURL("jdbc:mysql://" + host + ":" + port + "/" + db + 
                                  "?zeroDateTimeBehavior=convertToNull&autoReconnect=true&useCompression=true");
                dataSource.setUser(user);
                dataSource.setPassword(pas);
                
                // Tambahkan timeout agar tidak menunggu terlalu lama saat DB offline
                dataSource.setLoginTimeout(5);
                
                connection = dataSource.getConnection();
                
                // Jika berhasil sampai sini, reset counter ke 0
                loginAttempt = 0;
                 System.out.println(
                    "  Koneksi Berhasil. Sorry bro loading, silahkan baca dulu.... \n\n"+
                    "	Software ini adalah Software Menejemen Rumah Sakit/Klinik/\n" +
                    "  Puskesmas yang  gratis dan boleh digunakan siapa saja tanpa dikenai \n" +
                    "  biaya apapun. Dilarang keras memperjualbelikan/mengambil \n" +
                    "  keuntungan dari Software ini dalam bentuk apapun tanpa seijin pembuat \n" +
                    "  software (Khanza.Soft Media). Bagi yang sengaja memperjualbelikan/\n"+
                    "  mengambil keuntangan dari softaware ini tanpa ijin, kami  sumpahi sial\n"+
                    "  1000 turunan, miskin sampai 500 turunan.\n"+
                    "                                                                           \n"+
                    "  #    ____  ___  __  __  ____   ____    _  __ _                              \n" +
                    "  #   / ___||_ _||  \\/  ||  _ \\ / ___|  | |/ /| |__    __ _  _ __   ____ __ _ \n" +
                    "  #   \\___ \\ | | | |\\/| || |_) |\\___ \\  | ' / | '_ \\  / _` || '_ \\ |_  // _` |\n" +
                    "  #    ___) || | | |  | ||  _ <  ___) | | . \\ | | | || (_| || | | | / /| (_| |\n" +
                    "  #   |____/|___||_|  |_||_| \\_\\|____/  |_|\\_\\|_| |_| \\__,_||_| |_|/___|\\__,_|\n" +
                    "  #                                                                           \n"+
                    "                                                                           \n"+
                    "  Licensi yang dianut di software ini https://en.wikipedia.org/wiki/Aladdin_Free_Public_License \n"+
                    "  Informasi dan panduan bisa dicek di halaman https://github.com/mas-elkhanza/SIMRS-Khanza/wiki \n"+
                    "  Bagi yang ingin berdonasi untuk pengembangan aplikasi ini bisa ke BSI 1015369872 atas nama Windiarto\n"+
                    "                                                                           "
                );  
                 
                System.out.println("==========================================");
                System.out.println("   SIMRS RSANDINI Version 1.1.1 Released ");
                System.out.println("          Powered by Khanza ");
                System.out.println("=========================================="); 
                System.out.println("\nConnecting to >> "+host+"["+db+"]:"+port+"\nConnection successful.");
            }
        } catch (Exception e) {
                loginAttempt++; // Tambah angka percobaan jika gagal
                System.out.println("Percobaan ke-" + loginAttempt + " Gagal: \n" + e.getMessage());

            if (loginAttempt >= 3) {
                JOptionPane.showMessageDialog(null, "Koneksi Gagal 3x: \n" + e.getMessage() + "\nAplikasi Keluar.");
                System.exit(0);
            } else {
                JOptionPane.showMessageDialog(null, "Koneksi Gagal (Percobaan " + loginAttempt + "/3). Periksa Jaringan/Database.");
            }
        }
        return connection;
    }
    
    
    // 1. Muat file XML hanya SEKALI saat class dipanggil
    static {
        try (FileInputStream fis = new FileInputStream("setting/database.xml")) {
            prop.loadFromXML(fis);
        } catch (Exception e) {
            System.err.println("Gagal memuat file setting/database.xml: " + e.getMessage());
        }
    }

    // 2. Fungsi pembantu untuk mengambil & dekripsi data
    private static String getVal(String key, boolean decrypt) {
        String value = prop.getProperty(key, "");
        return (decrypt && !value.isEmpty()) ? EnkripsiAES.decrypt(value) : value;
    }

	
    // 3. Getter yang jauh lebih ringkas
    public static String HOST()        { return getVal("HOST", true); }
    public static String DATABASE()    { return getVal("DATABASE", true); }
    public static String PORT()        { return getVal("PORT", true); }
    public static String USER()        { return getVal("USER", true); }
    public static String CARICEPAT()   { return getVal("CARICEPAT", false); }
    public static String HYBRIDWEB()   { return getVal("HYBRIDWEB", false); }
    public static String PORTWEB()     { return getVal("PORTWEB", false); }
    public static String HOSTHYBRIDWEB(){ return getVal("HOSTHYBRIDWEB", false); }
    public static String USERHYBRIDWEB(){ return getVal("USERHYBRIDWEB", false); }
    public static String PASHYBRIDWEB() { return getVal("PASHYBRIDWEB", false); }
    public static String ANTRIAN()               { return getVal("ANTRIAN", false); }
    public static String ALARMAPOTEK()           { return getVal("ALARMAPOTEK", false); }
    public static String FORMALARMAPOTEK()       { return getVal("FORMALARMAPOTEK", false); }
    public static String ALARMLAB()              { return getVal("ALARMLAB", false); }
    public static String FORMALARMLAB()          { return getVal("FORMALARMLAB", false); }
    public static String ALARMRADIOLOGI()        { return getVal("ALARMRADIOLOGI", false); }
    public static String FORMALARMRADIOLOGI()    { return getVal("FORMALARMRADIOLOGI", false); }
    public static String ALARMRSISRUTE()         { return getVal("ALARMRSISRUTE", false); }
    public static String ALARMBOOKINGPERIKSA()   { return getVal("ALARMBOOKINGPERIKSA", false); }
    public static String ALARMPERMINTAANRANAP()  { return getVal("ALARMPERMINTAANRANAP", false); }
    public static String ALARMPENGADUANPASIEN()  { return getVal("ALARMPENGADUANPASIEN", false); }
    public static String MENUTRANSPARAN()        { return getVal("MENUTRANSPARAN", false); }
    public static String TANGGALMUNDUR()        { return getVal("TANGGALMUNDUR", false); }
    
    // API VClaim / BPJS
    public static String URLAPIBPJS()           { return getVal("URLAPIBPJS", false); }
    public static String SECRETKEYAPIBPJS()     { return getVal("SECRETKEYAPIBPJS", true); }
    public static String CONSIDAPIBPJS()       { return getVal("CONSIDAPIBPJS", true); }
    public static String USERKEYAPIBPJS()       { return getVal("USERKEYAPIBPJS", true); }

    // API Aplicare
    public static String URLAPIAPLICARE()       { return getVal("URLAPIAPLICARE", false); }
    public static String SECRETKEYAPIAPLICARE() { return getVal("SECRETKEYAPIAPLICARE", true); }
    public static String CONSIDAPIAPLICARE()   { return getVal("CONSIDAPIAPLICARE", true); }
    public static String USERKEYAPIAPLICARE()   { return getVal("USERKEYAPIAPLICARE", true); }

    // API Mobile JKN
    public static String URLAPIMOBILEJKN()      { return getVal("URLAPIMOBILEJKN", false); }
    public static String SECRETKEYAPIMOBILEJKN(){ return getVal("SECRETKEYAPIMOBILEJKN", true); }
    public static String CONSIDAPIMOBILEJKN()   { return getVal("CONSIDAPIMOBILEJKN", true); }
    public static String USERKEYAPIMOBILEJKN()  { return getVal("USERKEYAPIMOBILEJKN", true); }

    // API Apotek BPJS
    public static String URLAPIAPOTEKBPJS()     { return getVal("URLAPIAPOTEKBPJS", false); }
    public static String SECRETKEYAPIAPOTEKBPJS(){ return getVal("SECRETKEYAPIAPOTEKBPJS", true); }
    public static String CONSIDAPIAPOTEKBPJS()  { return getVal("CONSIDAPIAPOTEKBPJS", true); }
    public static String USERKEYAPIAPOTEKBPJS() { return getVal("USERKEYAPIAPOTEKBPJS", true); }
	public static String JADIKANPIUTANGAPOTEKBPJS() { return getVal("JADIKANPIUTANGAPOTEKBPJS", false); }
	
    // API PCare
    public static String URLAPIPCARE()          { return getVal("URLAPIPCARE", false); }
    public static String SECRETKEYAPIPCARE()    { return getVal("SECRETKEYAPIPCARE", true); }
    public static String CONSIDAPIPCARE()       { return getVal("CONSIDAPIPCARE", true); }
    public static String USERKEYAPIPCARE()      { return getVal("USERKEYAPIPCARE", true); }
    public static String USERPCARE()            { return getVal("USERPCARE", true); }
    public static String PASSPCARE()            { return getVal("PASSPCARE", true); }
    public static String DIVREGPCARE()          { return getVal("DIVREGPCARE", false); }
    public static String KACABPCARE()           { return getVal("KACABPCARE", false); }
	
	// API  MOBILEJKNFKTP
	public static String URLMOBILEJKNFKTP()           { return getVal("URLMOBILEJKNFKTP", false); }
	public static String SECRETKEYMOBILEJKNFKTP()           { return getVal("SECRETKEYMOBILEJKNFKTP", true); }
	public static String CONSIDMOBILEJKNFKTP()           { return getVal("CONSIDMOBILEJKNFKTP", true); }
	public static String USERKEYMOBILEJKNFKTP()           { return getVal("USERKEYMOBILEJKNFKTP", true); }
	public static String PASSMOBILEJKNFKTP()           { return getVal("PASSMOBILEJKNFKTP", true); }
	public static String USERMOBILEJKNFKTP()           { return getVal("USERMOBILEJKNFKTP", true); }
	public static String ADDANTRIANAPIMOBILEJKNFKTP()           { return getVal("ADDANTRIANAPIMOBILEJKNFKTP", false); }

    // API Sisrute & SIRS
    public static String URLAPISISRUTE()        { return getVal("URLAPISISRUTE", false); }
    public static String IDSISRUTE()            { return getVal("IDSISRUTE", true); }
    public static String PASSSISRUTE()          { return getVal("PASSSISRUTE", true); }
 
	// API SIRS
    public static String URLAPISIRS()      { return getVal("URLAPISIRS", false); }
    public static String IDSIRS()          { return getVal("IDSIRS", true); }
    public static String PASSSIRS()        { return getVal("PASSSIRS", true); }

    // API Corona / Kemenkes
    public static String URLAPICORONA()    { return getVal("URLAPICORONA", false); }
    public static String IDCORONA()        { return getVal("IDCORONA", true); }
    public static String PASSCORONA()      { return getVal("PASSCORONA", true); }

    // API SITT (Sistem Informasi Tuberkulosis)
    public static String URLAPISITT()      { return getVal("URLAPISITT", false); }
    public static String IDSITT()          { return getVal("IDSITT", true); }
    public static String PASSSITT()        { return getVal("PASSSITT", true); }
    public static String KABUPATENSITT()   { return getVal("KABUPATENSITT", false); }
    
    // Getter dengan pembersihan karakter kutip (')
    public static String KAMARAKTIFRANAP()        { return getVal("KAMARAKTIFRANAP", false).replace("'", ""); }
    public static String DOKTERAKTIFKASIRRALAN()  { return getVal("DOKTERAKTIFKASIRRALAN", false).replace("'", ""); }
    public static String POLIAKTIFKASIRRALAN()    { return getVal("POLIAKTIFKASIRRALAN", false).replace("'", ""); }
    public static String RUANGANAKTIFINVENTARIS() { return getVal("RUANGANAKTIFINVENTARIS", false).replace("'", ""); }

    // Getter biasa
    public static String BASENOREG()              { return getVal("BASENOREG", false); }
    public static String VALIDASIULANGBERIOBAT()  { return getVal("VALIDASIULANGBERIOBAT", false); }
    public static String URUTNOREG()              { return getVal("URUTNOREG", false); }   
    
 // Registrasi & Printer
    public static String JADWALDOKTERDIREGISTRASI() { return getVal("JADWALDOKTERDIREGISTRASI", false); }
    public static String KUNCIDOKTERRANAP() { return getVal("KUNCIDOKTERRANAP", true); }
    public static String IPPRINTERTRACER()         { return getVal("IPPRINTERTRACER", false); }

    // API Inhealth
    public static String URLAPIINHEALTH()          { return getVal("URLAPIINHEALTH", false); }
    public static String TOKENINHEALTH()           { return getVal("TOKENINHEALTH", true); }

    // Pengaturan Farmasi & Billing
    public static String PEMBULATANHARGAOBAT()     { return getVal("PEMBULATANHARGAOBAT", false); }
    public static String AKTIFKANBATCHOBAT()       { return getVal("AKTIFKANBATCHOBAT", false); }
    public static String CETAKRINCIANOBAT()        { return getVal("CETAKRINCIANOBAT", false); }
    public static String AKTIFKANBILLINGPARSIAL()  { return getVal("AKTIFKANBILLINGPARSIAL", false); }
    
// API Dukcapil Jakarta
    public static String URLDUKCAPILJAKARTA()   { return getVal("URLDUKCAPILJAKARTA", false); }
    public static String USERDUKCAPILJAKARTA()  { return getVal("USERDUKCAPILJAKARTA", true); }
    public static String PASSDUKCAPILJAKARTA()  { return getVal("PASSDUKCAPILJAKARTA", true); }
    public static String VAR1DUKCAPILJAKARTA()  { return getVal("VAR1DUKCAPILJAKARTA", false); }
    public static String VAR2DUKCAPILJAKARTA()  { return getVal("VAR2DUKCAPILJAKARTA", false); }

    // API Dukcapil Nasional
    public static String URLDUKCAPIL()          { return getVal("URLDUKCAPIL", false); }
    public static String USERDUKCAPIL()         { return getVal("USERDUKCAPIL", true); }
    public static String PASSDUKCAPIL()         { return getVal("PASSDUKCAPIL", true); }
    public static String IPUSERDUKCAPIL()       { return getVal("IPUSERDUKCAPIL", false); }

    
 // Tracking & LICA
    public static String AKTIFKANTRACKSQL()    { return getVal("AKTIFKANTRACKSQL", true); }
    public static String HOSTWSLICA()          { return getVal("HOSTWSLICA", false); }
    public static String KEYWSLICA()           { return getVal("KEYWSLICA", true); }

    // Farmasi & Stok
    public static String DEPOAKTIFOBAT()       { return getVal("DEPOAKTIFOBAT", false).replace("'", ""); }
    public static String STOKKOSONGRESEP()     { 
        String s = getVal("STOKKOSONGRESEP", false);
        return s.isEmpty() ? "no" : s; 
    }
    public static String NOTIFMAKSIMALNOMINALRESEPRAJAL()     { 
        String s = getVal("NOTIFMAKSIMALNOMINALRESEPRAJAL", false);
        return s.isEmpty() ? "no" : s; 
    }
    public static Double MAKSIMALNOMINALRESEPRAJAL()     { 
        String s = getVal("MAKSIMALNOMINALRESEPRAJAL", false);
        if (s.isEmpty()) return 0.0;
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
	
	
	

    // Logika HPP (Harga Pokok Penjualan)
    public static String HPPFARMASI() {
        return getVal("HPPFARMASI", false).equals("h_beli") ? "h_beli" : "dasar";
    }
    public static String HPPTOKO() {
        return getVal("HPPTOKO", false).equals("h_beli") ? "h_beli" : "dasar";
    }

    // API MedQLab
    public static String URLAPIMEDQLAB()       { return getVal("URLAPIMEDQLAB", false); }
    public static String SECRETKEYAPIMEDQLAB() { return getVal("SECRETKEYAPIMEDQLAB", true); }
    public static String CONSIDAPIMEDQLAB()    { return getVal("CONSIDAPIMEDQLAB", true); }
    
 // Carestream & Softmedix
    public static String URLCARESTREAM()          { return getVal("URLCARESTREAM", false); }
    public static String URLAPISOFTMEDIX()        { return getVal("URLAPISOFTMEDIX", false); }
    public static String PRODUCTSOFTMEDIX()       { return getVal("PRODUCTSOFTMEDIX", true); }
    public static String VERSIONSOFTMEDIX()       { return getVal("VERSIONSOFTMEDIX", true); }
    public static String USERIDSOFTMEDIX()        { return getVal("USERIDSOFTMEDIX", true); }
    public static String KEYSOFTMEDIX()           { return getVal("KEYSOFTMEDIX", true); }

    // Resep & BPJS Logic
    public static String RESEPRAJALKEPLAN()       { return getVal("RESEPRAJALKEPLAN", false); }
    public static String DIAGNOSARUJUKANMASUKAPIBPJS() { return getVal("DIAGNOSARUJUKANMASUKAPIBPJS", false); }
    
    // UI/Tampilan
    public static String AKTIFKANWARNARALAN()     { return getVal("AKTIFKANWARNARALAN", false); }
    
 // API SATUSEHAT (Kredensial Terenkripsi)
    public static String CLIENTIDSATUSEHAT()  { return getVal("CLIENTIDSATUSEHAT", true); }
    public static String SECRETKEYSATUSEHAT() { return getVal("SECRETKEYSATUSEHAT", true); }
    public static String IDSATUSEHAT()        { return getVal("IDSATUSEHAT", true); }
    
    // API SATUSEHAT (Endpoints & Wilayah)
    public static String URLAUTHSATUSEHAT()   { return getVal("URLAUTHSATUSEHAT", false); }
    public static String URLFHIRSATUSEHAT()   { return getVal("URLFHIRSATUSEHAT", false); }
    public static String KELURAHANSATUSEHAT() { return getVal("KELURAHANSATUSEHAT", false); }
    public static String KECAMATANSATUSEHAT() { return getVal("KECAMATANSATUSEHAT", false); }
    public static String KABUPATENSATUSEHAT() { return getVal("KABUPATENSATUSEHAT", false); }
    public static String PROPINSISATUSEHAT()  { return getVal("PROPINSISATUSEHAT", false); }
    public static String KODEPOSSATUSEHAT()   { return getVal("KODEPOSSATUSEHAT", false); }    
    
	// API ESIGN
	public static String URLAPIESIGN()   { return getVal("URLAPIESIGN", false); } 
	public static String USERNAMEAPIESIGN()   { return getVal("USERNAMEAPIESIGN", true); } 	
	public static String PASSAPIESIGN()   { return getVal("PASSAPIESIGN", true); } 
	public static String URLAKSESFILEESIGN()   { return getVal("URLAKSESFILEESIGN", false); } 	
	public static String SFTPFILEESIGNHOST()   { return getVal("SFTPFILEESIGNHOST", true); } 
	public static String SFTPFILEESIGNPORT()   { return getVal("SFTPFILEESIGNPORT", true); } 	
	public static String SFTPFILEESIGNUSER()   { return getVal("SFTPFILEESIGNUSER", true); } 
	public static String SFTPFILEESIGNPAS()   { return getVal("SFTPFILEESIGNPAS", true); } 	
	public static String SFTPFILEESIGNFOLDER()   { return getVal("SFTPFILEESIGNFOLDER", true); } 
	
	// API SERTISIGN
	public static String URLAPISERTISIGN()   { return getVal("URLAPISERTISIGN", false); } 	
	public static String APIKEYSERTISIGN()   { return getVal("APIKEYSERTISIGN", true); } 
	public static String URLDOKUMENSERTISIGN()   { return getVal("URLDOKUMENSERTISIGN", false); } 

	// API DUTAPARKING
	public static String HOSTDUTAPARKING()   { return getVal("HOSTDUTAPARKING", true); } 	
	public static String DATABASEDUTAPARKING()   { return getVal("DATABASEDUTAPARKING", true); } 
	public static String PORTDUTAPARKING()   { return getVal("PORTDUTAPARKING", true); } 	
	public static String USERDUTAPARKING()   { return getVal("USERDUTAPARKING", true); } 	
	public static String PASDUTAPARKING()   { return getVal("PASDUTAPARKING", true); } 
	
	// API ICARE
	public static String URLAPIICARE()   { return getVal("URLAPIICARE", false); } 
	public static String SECRETKEYAPIICARE()   { return getVal("SECRETKEYAPIICARE", true); } 
	public static String CONSIDAPIICARE()   { return getVal("CONSIDAPIICARE", true); } 
	public static String USERKEYAPIICARE()   { return getVal("USERKEYAPIICARE", true); } 

	// Antrian
	public static String LOKETANTRIAN() { return getVal("LOKETANTRIAN", false); }

	// Resep
	public static String TAMPILKANCOPYRESEPDOKTERLAIN() { return getVal("TAMPILKANCOPYRESEPDOKTERLAIN", false); }
	public static String AKTIFKANRESEPITERDOKTER() { return getVal("AKTIFKANRESEPITERDOKTER", false); }

	// BPJS tambahan
	public static String JADIKANBOOKINGSURATKONTROL() { return getVal("JADIKANBOOKINGSURATKONTROL", false); }
	public static String JADIKANBOOKINGSURATKONTROLAPIBPJS() { return getVal("JADIKANBOOKINGSURATKONTROLAPIBPJS", false); }
	public static String ADDANTRIANAPIMOBILEJKN() { return getVal("ADDANTRIANAPIMOBILEJKN", false); }
	public static String KODEPPKAPOTEKBPJS() { return getVal("KODEPPKAPOTEKBPJS", true); }

	// Database FUJI
	public static String HOSTFUJI() { return getVal("HOSTFUJI", true); }
	public static String DATABASEFUJI() { return getVal("DATABASEFUJI", true); }
	public static String PORTFUJI() { return getVal("PORTFUJI", true); }
	public static String USERFUJI() { return getVal("USERFUJI", true); }
	public static String PASFUJI() { return getVal("PASFUJI", true); }

	// Database SYSMEX
	public static String HOSTSYSMEX() { return getVal("HOSTSYSMEX", true); }
	public static String DATABASESYSMEX() { return getVal("DATABASESYSMEX", true); }
	public static String PORTSYSMEX() { return getVal("PORTSYSMEX", true); }
	public static String USERSYSMEX() { return getVal("USERSYSMEX", true); }
	public static String PASSYSMEX() { return getVal("PASSYSMEX", true); }

	// Database ELIMS
	public static String HOSTELIMS() { return getVal("HOSTELIMS", true); }
	public static String DATABASEELIMS() { return getVal("DATABASEELIMS", true); }
	public static String PORTELIMS() { return getVal("PORTELIMS", true); }
	public static String USERELIMS() { return getVal("USERELIMS", true); }
	public static String PASELIMS() { return getVal("PASELIMS", true); }

	// Database SMARTLAB
	public static String HOSTSMARTLAB() { return getVal("HOSTSMARTLAB", true); }
	public static String DATABASESMARTLAB() { return getVal("DATABASESMARTLAB", true); }
	public static String PORTSMARTLAB() { return getVal("PORTSMARTLAB", true); }
	public static String USERSMARTLAB() { return getVal("USERSMARTLAB", true); }
	public static String PASSMARTLAB() { return getVal("PASSMARTLAB", true); }

	// Database VANSLAB
	public static String HOSTVANSLAB() { return getVal("HOSTVANSLAB", true); }
	public static String DATABASEVANSLAB() { return getVal("DATABASEVANSLAB", true); }
	public static String PORTVANSLAB() { return getVal("PORTVANSLAB", true); }
	public static String USERVANSLAB() { return getVal("USERVANSLAB", true); }
	public static String PASVANSLAB() { return getVal("PASVANSLAB", true); }

	// Database SLIMS
	public static String HOSTSLIMS() { return getVal("HOSTSLIMS", true); }
	public static String DATABASESLIMS() { return getVal("DATABASESLIMS", true); }
	public static String PORTSLIMS() { return getVal("PORTSLIMS", true); }
	public static String USERSLIMS() { return getVal("USERSLIMS", true); }
	public static String PASSLIMS() { return getVal("PASSLIMS", true); }

	// ORTHANC
	public static String URLORTHANC() { return getVal("URLORTHANC", false); }
	public static String PORTORTHANC() { return getVal("PORTORTHANC", true); }
	public static String USERORTHANC() { return getVal("USERORTHANC", true); }
	public static String PASSORTHANC() { return getVal("PASSORTHANC", true); }

	// SFTP MANDIRI
	public static String SFTPMANDIRIPATHPEMBAYARANPIHAKKETIGA(){ return getVal("SFTPMANDIRIPATHPEMBAYARANPIHAKKETIGA", true); }
	public static String SFTPMANDIRIPATHPEMBAYARANPAJAK(){ return getVal("SFTPMANDIRIPATHPEMBAYARANPAJAK", true); }
	public static String SFTPMANDIRIPATHPEMBAYARANVIRTUALACCOUNT(){ return getVal("SFTPMANDIRIPATHPEMBAYARANVIRTUALACCOUNT", true); }
	public static String SFTPMANDIRIPATHACK(){ return getVal("SFTPMANDIRIPATHACK", true); }
	public static String SFTPMANDIRIPATHMT940(){ return getVal("SFTPMANDIRIPATHMT940", true); }
	public static String SFTPMANDIRIHOST(){ return getVal("SFTPMANDIRIHOST", true); }
	public static String SFTPMANDIRIPORT(){ return getVal("SFTPMANDIRIPORT", true); }
	public static String SFTPMANDIRIUSER(){ return getVal("SFTPMANDIRIUSER", true); }
	public static String SFTPMANDIRIPAS(){ return getVal("SFTPMANDIRIPAS", true); }

}