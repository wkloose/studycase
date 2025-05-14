public class Tiket {
    private Penumpang penumpang;
    private String kursi, naik, turun;
    private int harga;

    public Tiket(Penumpang p, String kursi, String naik, String turun, int harga) {
        this.penumpang = p;
        this.kursi = kursi;
        this.naik = naik;
        this.turun = turun;
        this.harga = harga;
    }

    public String getKursi() { return kursi; }
    public int getHarga() { return harga; }
    public Penumpang getPenumpang() { return penumpang; }
    public String getNaik() { return naik; }
    public String getTurun() { return turun; }

    public String toDataString() {
        return penumpang.toDataString() + ";" + kursi + ";" + naik + ";" + turun + ";" + harga;
    }

    public static Tiket fromString(String data) {
        String[] d = data.split(";");
        Penumpang p = new Penumpang(d[0], d[1], d[2]);
        return new Tiket(p, d[3], d[4], d[5], Integer.parseInt(d[6]));
    }
}