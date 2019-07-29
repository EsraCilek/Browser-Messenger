package com.gonultoktay.sohbet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Date;

@Component
public class WebSocketHandler extends TextWebSocketHandler {


    @Autowired
    private OturumSet oturumSet;

    @Autowired
    private KullaniciService kullaniciService;

    public void afterConnectionEstablished(WebSocketSession session) {
        oturumSet.add(session);
        Kullanici kullanici = (Kullanici) session.getAttributes().get("oturum");
        if (kullanici != null) {
            kullanici.setWs(session);
            kullanici.setPingDate(new Date());
        }
    }

    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        oturumSet.remove(session);
        Kullanici kullanici = (Kullanici) session.getAttributes().get("oturum");
        if (kullanici != null) {
            kullanici.setWs(null);
        }
    }

    protected void handleTextMessage(WebSocketSession session, TextMessage message) {

        try {
            Gson gson = new GsonBuilder().create();
            Mesaj gelen = gson.fromJson(message.getPayload(), Mesaj.class);

            Kullanici kime = kullaniciService.getKullaniciMap().get(gelen.getKime());
            Kullanici kimden = kullaniciService.getKullaniciMap().get(gelen.getKimden());
            if (kime != null && kimden != null) {
                kime.getWs().sendMessage(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
