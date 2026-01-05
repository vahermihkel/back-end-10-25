package ee.mihkel.veebipood.model;

import lombok.Data;

@Data
public class PasswordCredentials {
    private Long id;
    private String oldPassword;
    private String newPassword;
}
