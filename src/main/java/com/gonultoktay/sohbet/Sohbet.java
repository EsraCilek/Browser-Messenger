package com.gonultoktay.sohbet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.socket.TextMessage;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class Sohbet {

    @Autowired
    private KullaniciService sohbetService;

    @Autowired
    private OturumSet oturumSet;

    @GetMapping("/test")
    public String merhaba() {
        return "test";
    }

    @GetMapping("/")
    public String giris(HttpServletRequest request) {

        // oturum acmis kullanici kontrolü yapılıyor. varsa sohbete yonlendiriliyor.
        Kullanici oturum = (Kullanici) request.getSession().getAttribute("oturum");
        if (oturum != null) {
            return "redirect:/sohbet";
        }

        return "giris";
    }

    @PostMapping("/")
    public ModelAndView girisYap(HttpServletRequest request) {

        // oturum acmis kullanici kontrolü yapılıyor. kullanici varsa sohbete yonlendiriliyor.
        Kullanici oturum = (Kullanici) request.getSession().getAttribute("oturum");
        if (oturum != null) {
            return new ModelAndView("redirect:/sohbet");
        }


        String kullaniciAdi = request.getParameter("kullaniciAdi");
        String sifre = request.getParameter("sifre");

        Kullanici kullanici = sohbetService.getKullaniciMap().get(kullaniciAdi);

        // kullanici adi bulunamadi veya sifre yanlis
        if (kullanici == null || !kullanici.getSifre().equals(sifre)) {
            ModelAndView view = new ModelAndView("giris");
            view.addObject("hata", true);
            return view;
        }

        kullanici.setCevrimici(true);
        kullanici.setHttpSession(request.getSession());
        kullanici.setPingDate(new Date());
        request.getSession().setAttribute("oturum", kullanici);

        return new ModelAndView("redirect:/sohbet");
    }


    @GetMapping("/yeni")
    public String yeni(HttpServletRequest request) {

        // oturum acmis kullanici kontrolü yapılıyor. oturum acan kullanici varsa sohbet'e yonlendiriliyor.
        Kullanici oturum = (Kullanici) request.getSession().getAttribute("oturum");
        if (oturum != null) {
            return "redirect:/sohbet";
        }

        return "yeni";
    }

    @PostMapping("/yeni")
    public ModelAndView yeniKayit(HttpServletRequest request) {

        // oturum acmis kullanici kontrolü yapılıyor. kullanici varsa sohbete yonlendiriliyor.
        Kullanici oturum = (Kullanici) request.getSession().getAttribute("oturum");
        if (oturum != null) {
            return new ModelAndView("redirect:/sohbet");
        }

        String kullaniciAdi = request.getParameter("kullaniciAdi");
        String sifre = request.getParameter("sifre");
        String sifre2 = request.getParameter("sifre2");

        // kullanici adi bulunamadi veya sifre yanlis
        Kullanici kullanici = sohbetService.getKullaniciMap().get(kullaniciAdi);
        if (kullanici != null) {
            ModelAndView view = new ModelAndView("yeni");
            view.addObject("hata", true);
            return view;
        } else if (!sifre.equals(sifre2)) {
            ModelAndView view = new ModelAndView("yeni");
            view.addObject("sifreHata", true);
            return view;
        }

        // ip adresine aynı modem'den çıkış yapacak olan kullanıcılar için port da ekleniyor.
        String ip = request.getRemoteAddr() + ":" + request.getRemotePort();

        Kullanici yeni = new Kullanici(kullaniciAdi, sifre, ip, true);
        yeni.setPingDate(new Date());
        sohbetService.getKullaniciMap().put(kullaniciAdi, yeni);

        request.getSession().setAttribute("oturum", yeni);

        return new ModelAndView("redirect:/sohbet");
    }


    @GetMapping("/sohbet")
    public ModelAndView sohbet(HttpServletRequest request) {

        // oturum acmis kullanici kontrolü yapılıyor. oturum acan kullanici yoksa girise yonlendiriliyor.
        Kullanici oturum = (Kullanici) request.getSession().getAttribute("oturum");
        if (oturum == null) {
            return new ModelAndView("redirect:/");
        }

        ModelAndView modelAndView = new ModelAndView("sohbet");
        modelAndView.addObject("oturum", oturum);

        return modelAndView;
    }

    @GetMapping("/kullaniciList")
    public ModelAndView getKullaniciList(HttpServletRequest request) {

        // oturum acmis kullanici kontrolü yapılıyor. kullanici yoksa girise yonlendiriliyor.
        Kullanici oturum = (Kullanici) request.getSession().getAttribute("oturum");
        if (oturum == null) {
            return new ModelAndView("bosList");
        }

        ModelAndView view = new ModelAndView("kullaniciList");
        List<Kullanici> kullaniciList = new ArrayList<>(sohbetService.getKullaniciMap().values());

        view.addObject("oturum", oturum);
        view.addObject("kullaniciList", kullaniciList);
        return view;
    }

    @GetMapping("/ping")
    public String ping(HttpServletRequest request) {
        // oturum acmis kullanici kontrolü yapılıyor. kullanici yoksa girise yonlendiriliyor.
        Kullanici oturum = (Kullanici) request.getSession().getAttribute("oturum");
        if (oturum != null) {
            oturum.setPingDate(new Date());
        }

        return "pong";
    }

    @GetMapping("/cikis")
    public String cikis(HttpServletRequest request) {
        // oturum acmis kullanici kontrolü yapılıyor. kullanici yoksa girise yonlendiriliyor.
        Kullanici oturum = (Kullanici) request.getSession().getAttribute("oturum");
        if (oturum != null) {
            try {
                oturum.setCevrimici(false);
                if (oturum.getWs() != null) {
                    oturumSet.remove(oturum.getWs());
                    oturum.getWs().close();
                }
                request.getSession().invalidate();

            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        return "redirect:/";
    }

    @GetMapping("/istek")
    @ResponseStatus(value = HttpStatus.OK)
    public void istek(HttpServletRequest request, Model model) {
        try {
            Kullanici oturum = (Kullanici) request.getSession().getAttribute("oturum");
            if (oturum == null) {
            }

            String istek = request.getParameter("istek");
            Kullanici kullanici = sohbetService.getKullaniciMap().get(istek);

            // sohbet etmek istenilen kullanici yok ise
            if (kullanici == null) {
            }
            // kullanici izin daha once izin vermis ise
            else if (kullanici.getIzinList().contains(oturum.getAdi())) {
            }
            // kullanicinin izin listesinde yok ise
            else {

                Mesaj mesaj = new Mesaj(Mesaj.KABUL_SORGU);
                mesaj.setKimden(oturum.getAdi());
                mesaj.setKime(istek);
                mesaj.setMesaj("Kullanıcı sohbet etmek için izin istiyor.");

                Gson gson = new GsonBuilder().create();
                kullanici.getWs().sendMessage(new TextMessage(gson.toJson(mesaj)));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
