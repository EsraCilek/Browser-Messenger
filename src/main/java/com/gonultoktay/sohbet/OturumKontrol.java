package com.gonultoktay.sohbet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class OturumKontrol {


    @Autowired
    private OturumSet oturumSet;

    @Autowired
    private KullaniciService kullaniciService;

    /**
     * 15 sn'de bir oturum acmis kullanicilari kontrol eder, 15 sn'dir aktif olmayan kullanici varsa oturumlari sonlandirir ve
     * aktif kullanicilara haber verir.
     */
    @Scheduled(fixedRate = 15000)
    public void kontrolEt() {

        List<Kullanici> kullaniciList = new ArrayList<>(kullaniciService.getKullaniciMap().values());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, -15);
        Date onbesSnOncesi = calendar.getTime();

        for (Kullanici kullanici : kullaniciList) {
            if (kullanici.isCevrimici()) {
                try {
                    if (kullanici.getPingDate() == null || onbesSnOncesi.after(kullanici.getPingDate())) {

                        kullanici.setCevrimici(false);
                        oturumSet.remove(kullanici.getWs());


                        if (kullanici.getHttpSession() != null) {
                            kullanici.getHttpSession().invalidate();
                            kullanici.setHttpSession(null);
                        }
                        if (kullanici.getWs() != null) {
                            kullanici.getWs().close();
                            kullanici.setWs(null);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
