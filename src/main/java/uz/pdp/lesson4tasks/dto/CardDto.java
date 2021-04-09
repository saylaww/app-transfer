package uz.pdp.lesson4tasks.dto;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class CardDto {

    private Date expiredDate;
    private boolean isActive;
    private String number;
    private String username;
    private UUID userId;
}
