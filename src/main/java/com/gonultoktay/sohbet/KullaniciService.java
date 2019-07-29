package com.gonultoktay.sohbet;

import lombok.Getter;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Getter
@Service
public class KullaniciService {

    // Key = Kullanici Adi
    private Map<String, Kullanici> kullaniciMap = new HashMap<>();


    @PostConstruct
    public void init() {
        kullaniciMap.put("test", new Kullanici("test", "test", "0.0.0.0", false));
    }

    public Map<String, Kullanici> getKullaniciMap() {
        return kullaniciMap;
    }

    public void setKullaniciMap(Map<String, Kullanici> kullaniciMap) {
        this.kullaniciMap = kullaniciMap;
    }
}
