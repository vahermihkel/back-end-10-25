package ee.mihkel.veebipood.model;

import lombok.Data;

import java.util.Date;

@Data // siin sees on @Getter @Setter. Automaatselt on olemas:  @NoArgsConstructor
public class ErrorMessage {
    private String message;
    private int status;
    private Date timestamp;
}
