package uz.pdp.lesson4tasks.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Response {
    private String message;
    private boolean status;
    private Object object;

    public Response(String message, boolean status) {
        this.message = message;
        this.status = status;
    }
}
