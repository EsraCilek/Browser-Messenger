package com.gonultoktay.sohbet;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Mesaj {

    public static Integer YENILE = 100;
    public static Integer KABUL_SORGU = 300;
    public static Integer KABUL_EVET = 301;
    public static Integer KABUL_HAYIR = 302;
    public static Integer METIN = 400;

    private Integer tip;
    private String kimden;
    private String kime;
    private String mesaj;

    public Mesaj(Integer tip) {
        this.tip = tip;
    }

    public Mesaj(Integer tip, String kimden, String kime, String mesaj) {
        this.tip = tip;
        this.kimden = kimden;
        this.kime = kime;
        this.mesaj = mesaj;
    }

    public Integer getTip() {
        return tip;
    }

    public void setTip(Integer tip) {
        this.tip = tip;
    }

    public String getKimden() {
        return kimden;
    }

    public void setKimden(String kimden) {
        this.kimden = kimden;
    }

    public String getKime() {
        return kime;
    }

    public void setKime(String kime) {
        this.kime = kime;
    }

    public String getMesaj() {
        return mesaj;
    }

    public void setMesaj(String mesaj) {
        this.mesaj = mesaj;
    }
}
