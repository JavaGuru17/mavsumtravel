package uz.mavsumtravel.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum Callback {
    BACK_TO_CHANGE_LANG("back-to-change-lang"),


    ;
    private String callback;
    public static final Map<String, Callback> map = new HashMap<>();
    static {
        for (Callback c: Callback.values()) {
            map.put(c.getCallback(),c);
        }
    }

    public static Callback of(String data){
        return map.get(data);
    }
}
