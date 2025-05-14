public class Trayek {
    public static int getTarif(String asal, String tujuan) {
        asal = asal.toLowerCase();
        tujuan = tujuan.toLowerCase();

        int tarif = 0;

        // Wilangan
        if (asal.equals("wilangan") && tujuan.equals("ngawi")) tarif = 35000;
        else if (asal.equals("wilangan") && tujuan.equals("gendingan")) tarif = 45000;
        else if (asal.equals("wilangan") && tujuan.equals("solo")) tarif = 55000;
        else if (asal.equals("wilangan") && tujuan.equals("kartosuro")) tarif = 60000;
        else if (asal.equals("wilangan") && tujuan.equals("jogja")) tarif = 70000;
        else if (asal.equals("wilangan") && tujuan.equals("magelang")) tarif = 80000;

            // Ngawi
        else if (asal.equals("ngawi") && tujuan.equals("wilangan")) tarif = 35000;
        else if (asal.equals("ngawi") && tujuan.equals("gendingan")) tarif = 25000;
        else if (asal.equals("ngawi") && tujuan.equals("solo")) tarif = 30000;
        else if (asal.equals("ngawi") && tujuan.equals("kartosuro")) tarif = 40000;
        else if (asal.equals("ngawi") && tujuan.equals("jogja")) tarif = 45000;
        else if (asal.equals("ngawi") && tujuan.equals("magelang")) tarif = 60000;

            // Gendingan
        else if (asal.equals("gendingan") && tujuan.equals("wilangan")) tarif = 45000;
        else if (asal.equals("gendingan") && tujuan.equals("ngawi")) tarif = 25000;
        else if (asal.equals("gendingan") && tujuan.equals("solo")) tarif = 25000;
        else if (asal.equals("gendingan") && tujuan.equals("kartosuro")) tarif = 35000;
        else if (asal.equals("gendingan") && tujuan.equals("jogja")) tarif = 40000;
        else if (asal.equals("gendingan") && tujuan.equals("magelang")) tarif = 55000;

            // Solo
        else if (asal.equals("solo") && tujuan.equals("wilangan")) tarif = 55000;
        else if (asal.equals("solo") && tujuan.equals("ngawi")) tarif = 30000;
        else if (asal.equals("solo") && tujuan.equals("gendingan")) tarif = 25000;
        else if (asal.equals("solo") && tujuan.equals("kartosuro")) tarif = 15000;
        else if (asal.equals("solo") && tujuan.equals("jogja")) tarif = 15000;
        else if (asal.equals("solo") && tujuan.equals("magelang")) tarif = 30000;

            // Kartosuro
        else if (asal.equals("kartosuro") && tujuan.equals("wilangan")) tarif = 60000;
        else if (asal.equals("kartosuro") && tujuan.equals("ngawi")) tarif = 40000;
        else if (asal.equals("kartosuro") && tujuan.equals("gendingan")) tarif = 35000;
        else if (asal.equals("kartosuro") && tujuan.equals("solo")) tarif = 15000;
        else if (asal.equals("kartosuro") && tujuan.equals("jogja")) tarif = 15000;
        else if (asal.equals("kartosuro") && tujuan.equals("magelang")) tarif = 30000;

            // Jogja
        else if (asal.equals("jogja") && tujuan.equals("wilangan")) tarif = 70000;
        else if (asal.equals("jogja") && tujuan.equals("ngawi")) tarif = 45000;
        else if (asal.equals("jogja") && tujuan.equals("gendingan")) tarif = 40000;
        else if (asal.equals("jogja") && tujuan.equals("solo")) tarif = 15000;
        else if (asal.equals("jogja") && tujuan.equals("kartosuro")) tarif = 15000;
        else if (asal.equals("jogja") && tujuan.equals("magelang")) tarif = 15000;

            // Magelang
        else if (asal.equals("magelang") && tujuan.equals("wilangan")) tarif = 85000;
        else if (asal.equals("magelang") && tujuan.equals("ngawi")) tarif = 60000;
        else if (asal.equals("magelang") && tujuan.equals("gendingan")) tarif = 55000;
        else if (asal.equals("magelang") && tujuan.equals("solo")) tarif = 30000;
        else if (asal.equals("magelang") && tujuan.equals("kartosuro")) tarif = 30000;
        else if (asal.equals("magelang") && tujuan.equals("jogja")) tarif = 15000;

        // Rute tidak ditemukan
        return tarif;
    }
}
