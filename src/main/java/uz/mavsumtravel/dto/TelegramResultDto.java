package uz.mavsumtravel.mavsumtravel.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class TelegramResultDto {
    private boolean ok;
    private Object result;
}
