package ee.mihkel.rendipood.model;

import lombok.Data;

import java.util.Date;

@Data
public class ErrorMessage {
    private String message;
    private int status;
    private Date timestamp;
}