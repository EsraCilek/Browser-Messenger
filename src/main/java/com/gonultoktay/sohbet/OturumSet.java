package com.gonultoktay.sohbet;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Component
public class OturumSet {

    private Set<WebSocketSession> oturum = new HashSet<>();

    public void add(WebSocketSession session) {
        oturum.add(session);
    }

    public void remove(WebSocketSession session) {
        oturum.remove(session);
    }

    public Set<WebSocketSession> getList() {
        return oturum;
    }

    /**
     * Aktif kullanicilara kullanicisi listesini yenilemesi icin komut gonderir.
     */
    public void kullaniciListYenileGonder() {
        Gson gson = new GsonBuilder().create();
        for (WebSocketSession webSocketSession : oturum) {
            try {
                Mesaj mesaj = new Mesaj(Mesaj.YENILE);
                TextMessage message = new TextMessage(gson.toJson(mesaj));
                webSocketSession.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
