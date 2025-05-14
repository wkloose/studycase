//public class Penumpang {
//    private String nama, nik, hp;
//
//    public Penumpang(String nama, String nik, String hp) {
//        this.nama = nama;
//        this.nik = nik;
//        this.hp = hp;
//    }
//
//    public String getNama() { return nama; }
//    public String getNik() { return nik; }
//    public String getHp() { return hp; }
//
//    public String toDataString() {
//        return nama + ";" + nik + ";" + hp;
//    }
//
//    public static Penumpang fromString(String s) {
//        String[] parts = s.split(";");
//        return new Penumpang(parts[0], parts[1], parts[2]);
//    }
//}


public class Penumpang {
    private String nama, nik, hp;
    private String naik, turun; // Added boarding and destination

    public Penumpang(String nama, String nik, String hp) {
        this.nama = nama;
        this.nik = nik;
        this.hp = hp;
    }

    public String getNama() { return nama; }
    public String getNik() { return nik; }
    public String getHp() { return hp; }

    // New getters and setters for boarding and destination
    public String getNaik() { return naik; }
    public void setNaik(String naik) { this.naik = naik; }
    public String getTurun() { return turun; }
    public void setTurun(String turun) { this.turun = turun; }

    public String toDataString() {
        return nama + ";" + nik + ";" + hp;
    }

    public static Penumpang fromString(String s) {
        String[] parts = s.split(";");
        return new Penumpang(parts[0], parts[1], parts[2]);
    }
}