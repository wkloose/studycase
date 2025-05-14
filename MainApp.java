import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MainApp extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    private JPanel panelKursi;
    private JPanel panelForm;

    private List<String> selectedKursiList = new ArrayList<>();
    private JButton[] btnKursi;
    private String[] kursiList;

    private JTextField tfNama, tfNIK, tfHP;
    private JComboBox<String> cbNaik, cbTurun;
    private JLabel lblKursi;

    private Penumpang[] passengers;
    private Tiket[] tiketList = new Tiket[100];
    private int tiketCount = 0;
    private final String FILE = "data_tiket.txt";

    public MainApp() {
        setTitle("Pemesanan Tiket Bus");
        setSize(480, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        kursiList = generateKursi();
        loadData();

        initPanelForm();
        initPanelKursi();

        mainPanel.add(panelForm, "FORM");
        mainPanel.add(panelKursi, "KURSI");

        add(mainPanel);
        cardLayout.show(mainPanel, "FORM");
    }

    private void initPanelForm() {
        panelForm = new JPanel(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(7, 2, 5, 5));
        tfNama = new JTextField(); tfNIK = new JTextField(); tfHP = new JTextField();
        lblKursi = new JLabel("Kursi Terpilih: -");

        String[] kota = {"Wilangan", "Ngawi", "Gendingan", "Solo", "Kartosuro", "Jogja", "Magelang"};
        cbNaik = new JComboBox<>(kota);
        cbTurun = new JComboBox<>(kota);

        form.add(new JLabel("Nama")); form.add(tfNama);
        form.add(new JLabel("NIK")); form.add(tfNIK);
        form.add(new JLabel("No HP")); form.add(tfHP);
        form.add(new JLabel("Naik Dari")); form.add(cbNaik);
        form.add(new JLabel("Turun Di")); form.add(cbTurun);

        JButton btnLanjut = new JButton("Lanjut Pilih Kursi");
        btnLanjut.addActionListener(e -> {
            String nama = tfNama.getText();
            String nik = tfNIK.getText();
            String hp = tfHP.getText();
            String naik = (String) cbNaik.getSelectedItem();
            String turun = (String) cbTurun.getSelectedItem();

            // Validasi data kosong
            if (nama.isEmpty() || nik.isEmpty() || hp.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Lengkapi semua data!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validasi nama tidak boleh mengandung angka
            if (containsDigit(nama)) {
                JOptionPane.showMessageDialog(this, "Nama tidak boleh mengandung angka!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validasi NIK harus angka
            if (!isNumeric(nik)) {
                JOptionPane.showMessageDialog(this, "NIK hanya boleh berisi angka!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validasi No HP harus angka
            if (!isNumeric(hp)) {
                JOptionPane.showMessageDialog(this, "No HP hanya boleh berisi angka!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (naik.equals(turun)) {
                JOptionPane.showMessageDialog(this, "Asal dan tujuan tidak boleh sama!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int tarif = Trayek.getTarif(naik.toLowerCase(), turun.toLowerCase());
            if (tarif == 0) {
                JOptionPane.showMessageDialog(this, "Rute tidak tersedia!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Penumpang p = new Penumpang(nama, nik, hp);
            p.setNaik(naik);
            p.setTurun(turun);
            passengers = new Penumpang[1];
            passengers[0] = p;

            selectedKursiList.clear();
            updateKursiStatus(naik, turun);

            lblKursi.setText("Kursi Terpilih: -");
            cardLayout.show(mainPanel, "KURSI");
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnLanjut);

        panelForm.add(form, BorderLayout.CENTER);
        panelForm.add(buttonPanel, BorderLayout.SOUTH);
    }

    // Update status kursi berdasarkan trayek yang ingin dipesan
    private void updateKursiStatus(String naik, String turun) {
        for (int i = 0; i < kursiList.length; i++) {
            String kursi = kursiList[i];
            if (isKursiTersediaUntukTrayek(kursi, naik, turun)) {
                btnKursi[i].setBackground(Color.GREEN);
            } else {
                btnKursi[i].setBackground(Color.RED);
            }
        }
    }

    private void initPanelKursi() {
        panelKursi = new JPanel(new BorderLayout());

        JPanel busPanel = new JPanel(new BorderLayout(10, 10));
        busPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel doorDriverPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        JPanel kontroljamanPanel = new JPanel(new GridLayout(2, 1));

        JPanel panelPintuDepan = new JPanel(new FlowLayout(FlowLayout.LEFT, 1, 7));
        JButton btnPintuDepan = new JButton();
        btnPintuDepan.setPreferredSize(new Dimension(5, 30));
        btnPintuDepan.setEnabled(false);
        btnPintuDepan.setBackground(Color.LIGHT_GRAY);
        btnPintuDepan.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel lblPintuDepan = new JLabel("Pintu Depan");
        panelPintuDepan.add(btnPintuDepan);
        panelPintuDepan.add(lblPintuDepan);

        JPanel panelKondektur = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelKondektur.setPreferredSize(new Dimension(120, 30));
        JLabel lblKondektur = new JLabel("KONDEKTUR");
        lblKondektur.setFont(new Font("Arial", Font.BOLD, 10));
        lblKondektur.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panelKondektur.add(lblKondektur);

        JPanel driverPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 30));
        JButton btnDriver = new JButton("DRIVER");
        btnDriver.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        btnDriver.setEnabled(false);
        btnDriver.setPreferredSize(new Dimension(100, 30));
        driverPanel.add(btnDriver);

        doorDriverPanel.add(kontroljamanPanel);
        doorDriverPanel.add(driverPanel);
        kontroljamanPanel.add(panelPintuDepan);
        kontroljamanPanel.add(panelKondektur);

        topPanel.add(doorDriverPanel, BorderLayout.NORTH);

        JPanel seatsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        JPanel leftSeats = new JPanel(new GridLayout(11, 2, 5, 5));
        JPanel rightSeats = new JPanel(new GridLayout(11, 2, 5, 5));

        btnKursi = new JButton[kursiList.length];
        for (int i = 0; i < 18; i++) {
            String kursi = kursiList[i];
            JButton btn = createSeatButton(kursi);
            leftSeats.add(btn);
            btnKursi[i] = btn;
        }

        JPanel panelPintuBelakang = new JPanel(new FlowLayout(FlowLayout.LEFT, 1, 0));
        JButton btnPintuBelakang = new JButton();
        btnPintuBelakang.setPreferredSize(new Dimension(5, 30));
        btnPintuBelakang.setEnabled(false);
        btnPintuBelakang.setBackground(Color.LIGHT_GRAY);
        btnPintuBelakang.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel lblPintuBelakang = new JLabel("Pintu Belakang");
        panelPintuBelakang.add(btnPintuBelakang);
        panelPintuBelakang.add(lblPintuBelakang);

        leftSeats.add(panelPintuBelakang);
        leftSeats.add(new JLabel(""));

        JButton btnToilet = new JButton("TOILET");
        btnToilet.setEnabled(false);
        btnToilet.setPreferredSize(new Dimension(100, 30));
        btnToilet.setBackground(Color.PINK);
        btnToilet.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        leftSeats.add(btnToilet);
        leftSeats.add(new JLabel(""));

        for (int i = 18; i < 40; i++) {
            String kursi = kursiList[i];
            JButton btn = createSeatButton(kursi);
            rightSeats.add(btn);
            btnKursi[i] = btn;
        }

        seatsPanel.add(leftSeats);
        seatsPanel.add(rightSeats);

        // Panel info di bawah kursi
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.add(new JLabel("Keterangan: "));

        JPanel colorInfo1 = new JPanel();
        colorInfo1.setBackground(Color.GREEN);
        colorInfo1.setPreferredSize(new Dimension(20, 20));
        infoPanel.add(colorInfo1);
        infoPanel.add(new JLabel("Tersedia"));

        JPanel colorInfo2 = new JPanel();
        colorInfo2.setBackground(Color.RED);
        colorInfo2.setPreferredSize(new Dimension(20, 20));
        infoPanel.add(colorInfo2);
        infoPanel.add(new JLabel("Terisi/Tidak Tersedia"));

        JPanel colorInfo3 = new JPanel();
        colorInfo3.setBackground(Color.YELLOW);
        colorInfo3.setPreferredSize(new Dimension(20, 20));
        infoPanel.add(colorInfo3);
        infoPanel.add(new JLabel("Terpilih"));

        JButton btnKembali = new JButton("Kembali");
        btnKembali.addActionListener(e -> {
            selectedKursiList.clear();
            cardLayout.show(mainPanel, "FORM");
        });

        JButton btnPesan = new JButton("Pesan Tiket");
        btnPesan.addActionListener(e -> {
            if (selectedKursiList.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Pilih setidaknya satu kursi!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Penumpang p = passengers[0];
            String naik = p.getNaik();
            String turun = p.getTurun();
            int tarif = Trayek.getTarif(naik.toLowerCase(), turun.toLowerCase());
            int total = 0;

            for (String kursi : selectedKursiList) {
                if (!isKursiTersediaUntukTrayek(kursi, naik, turun)) {
                    JOptionPane.showMessageDialog(this,
                            "Kursi " + kursi + " tidak tersedia untuk trayek " + naik + " - " + turun,
                            "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                Tiket t = new Tiket(p, kursi, naik, turun, tarif);
                tiketList[tiketCount++] = t;
                saveTiket(t);
                total += tarif;
            }

            JOptionPane.showMessageDialog(this,
                    "Tiket berhasil dipesan untuk " + p.getNama() + "!\n" +
                            "Jumlah Kursi: " + selectedKursiList.size() + "\n" +
                            "Trayek: " + naik + " → " + turun + "\n" +
                            "Total Harga: Rp " + total);

            clearForm();
            selectedKursiList.clear();
            cardLayout.show(mainPanel, "FORM");
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(btnKembali);
        buttonPanel.add(btnPesan);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(infoPanel, BorderLayout.NORTH);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);

        busPanel.add(topPanel, BorderLayout.NORTH);
        busPanel.add(seatsPanel, BorderLayout.CENTER);

        panelKursi.add(new JLabel("Pilih Kursi", SwingConstants.CENTER), BorderLayout.NORTH);
        panelKursi.add(busPanel, BorderLayout.CENTER);
        panelKursi.add(southPanel, BorderLayout.SOUTH);
    }

    private JButton createSeatButton(String kursi) {
        JButton btn = new JButton(kursi);
        btn.setPreferredSize(new Dimension(60, 30));
        btn.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Warna kursi akan diupdate saat memilih trayek
        btn.setBackground(Color.GREEN);

        btn.addActionListener(e -> {
            if (btn.getBackground() == Color.RED) {
                // Tambahkan detail informasi kursi yang sudah terisi
                String info = getKursiInfo(kursi);
                JOptionPane.showMessageDialog(this,
                        "Kursi " + kursi + " sudah terisi atau tidak tersedia:\n" + info,
                        "Info Kursi", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            if (btn.getBackground() == Color.GREEN) {
                btn.setBackground(Color.YELLOW);
                selectedKursiList.add(kursi);
            } else if (btn.getBackground() == Color.YELLOW) {
                btn.setBackground(Color.GREEN);
                selectedKursiList.remove(kursi);
            }
            updateLabelKursi();
        });

        return btn;
    }

    // Mendapatkan informasi detail kursi untuk ditampilkan
    private String getKursiInfo(String kursi) {
        StringBuilder info = new StringBuilder();
        boolean kursiFound = false;

        for (int i = 0; i < tiketCount; i++) {
            if (tiketList[i].getKursi().equals(kursi)) {
                Tiket t = tiketList[i];
                String penumpangInfo = "Penumpang: " + t.getPenumpang().getNama() + "\n";
                String ruteTiket = "Rute: " + t.getNaik() + " - " + t.getTurun();

                info.append(penumpangInfo).append(ruteTiket);
                kursiFound = true;
                break;
            }
        }

        // Jika kursi terisi karena konflik trayek
        if (!kursiFound) {
            Penumpang p = passengers[0];
            info.append("Konflik dengan trayek: ")
                    .append(p.getNaik())
                    .append(" - ")
                    .append(p.getTurun());
        }

        return info.toString();
    }

    private String[] generateKursi() {
        String[] list = new String[40];
        int idx = 0;
        for (int i = 1; i <= 18; i++) list[idx++] = "A" + i;
        for (int i = 1; i <= 22; i++) list[idx++] = "B" + i;
        return list;
    }

    private void clearForm() {
        tfNama.setText("");
        tfNIK.setText("");
        tfHP.setText("");
    }

    private int jumlahIndex(String kursi) {
        for (int i = 0; i < kursiList.length; i++) {
            if (kursiList[i].equals(kursi)) return i;
        }
        return -1;
    }

    /**
     * Cek apakah trayek yang ingin dipesan overlap dengan
     * trayek yang sudah ada pada kursi tersebut
     */
    private boolean isKursiTersediaUntukTrayek(String kursi, String naik, String turun) {
        naik = naik.toLowerCase();
        turun = turun.toLowerCase();

        // Mengkonversi nama kota menjadi nilai numerik untuk membandingkan posisi
        int naikPos = getKotaPosition(naik);
        int turunPos = getKotaPosition(turun);

        // Cek apakah arah perjalanan dari barat ke timur atau timur ke barat
        boolean isPerjalananBaratKeTimur = naikPos < turunPos;

        // Cek semua tiket yang sudah ada
        for (int i = 0; i < tiketCount; i++) {
            Tiket t = tiketList[i];
            if (t.getKursi().equals(kursi)) {
                int tNaikPos = getKotaPosition(t.getNaik().toLowerCase());
                int tTurunPos = getKotaPosition(t.getTurun().toLowerCase());

                // Cek arah perjalanan tiket yang sudah ada
                boolean isTiketBaratKeTimur = tNaikPos < tTurunPos;

                // Jika arah perjalanan berbeda, maka tidak ada konflik
                // Contoh: Ngawi → Solo vs Solo → Ngawi
                if (isPerjalananBaratKeTimur != isTiketBaratKeTimur) {
                    continue; // Tidak ada konflik jika arah berbeda
                }

                // Jika arah perjalanan sama, cek overlap trayek

                // Verifikasi perjalanan dengan arah yang sama

                // Kasus 1: Naik di tengah-tengah perjalanan yang ada
                if (naikPos > tNaikPos && naikPos < tTurunPos) return false;

                // Kasus 2: Turun di tengah-tengah perjalanan yang ada
                if (turunPos > tNaikPos && turunPos < tTurunPos) return false;

                // Kasus 3: Perjalanan baru mencakup perjalanan yang ada
                if (naikPos <= tNaikPos && turunPos >= tTurunPos) return false;

                // Kasus 4: Perjalanan yang ada mencakup perjalanan baru
                if (naikPos >= tNaikPos && turunPos <= tTurunPos) return false;
            }
        }

        return true;
    }

    /**
     * Mengkonversi nama kota menjadi posisi numerik
     * untuk mengecek overlap pada rute
     */
    private int getKotaPosition(String kota) {
        // Urutan kota dari barat ke timur untuk logika pengecekan overlap
        String[] urutanKota = {"wilangan", "ngawi", "gendingan", "solo", "kartosuro", "jogja", "magelang"};

        for (int i = 0; i < urutanKota.length; i++) {
            if (urutanKota[i].equals(kota)) {
                return i;
            }
        }
        return -1;
    }

    private void saveTiket(Tiket t) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE, true))) {
            writer.write(t.toDataString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadData() {
        File f = new File(FILE);
        if (!f.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    Tiket t = Tiket.fromString(line);
                    tiketList[tiketCount++] = t;
                } catch (Exception e) {
                    System.err.println("Data rusak: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading data file: " + e.getMessage());
        }
    }

    private void updateLabelKursi() {
        if (selectedKursiList.isEmpty()) {
            lblKursi.setText("Kursi Terpilih: -");
        } else {
            lblKursi.setText("Kursi Terpilih: " + String.join(", ", selectedKursiList));
        }
    }
    private boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Memeriksa apakah string mengandung angka
     */
    private boolean containsDigit(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        for (char c : str.toCharArray()) {
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainApp().setVisible(true));
    }
}