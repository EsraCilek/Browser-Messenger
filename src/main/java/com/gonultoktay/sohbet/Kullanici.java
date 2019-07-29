package com.gonultoktay.sohbet;


import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class Kullanici {

    private String adi;
    private String sifre;
    private boolean cevrimici = false;

    private String ip;

    private Date pingDate;
    private WebSocketSession ws;
    private HttpSession httpSession;

    private Set<String> izinList = new HashSet<>();

    public Kullanici(String adi, String sifre, String ip, boolean cevrimici) {
        this.adi = adi;
        this.sifre = sifre;
        this.ip = ip;
        this.cevrimici = cevrimici;
    }

    public String getAdi() {
        return adi;
    }

    public void setAdi(String adi) {
        this.adi = adi;
    }

    public String getSifre() {
        return sifre;
    }

    public void setSifre(String sifre) {
        this.sifre = sifre;
    }

    public boolean isCevrimici() {
        return cevrimici;
    }

    public void setCevrimici(boolean cevrimici) {
        this.cevrimici = cevrimici;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getPingDate() { return pingDate; }

    public void setPingDate(Date pingDate) {
        this.pingDate = pingDate;
    }

    public WebSocketSession getWs() {
        return ws;
    }

    public void setWs(WebSocketSession ws) {
        this.ws = ws;
    }

    public HttpSession getHttpSession() {
        return httpSession;
    }

    public void setHttpSession(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    public Set<String> getIzinList() {
        return izinList;
    }

    public void setIzinList(Set<String> izinList) {
        this.izinList = izinList;
    }
}
