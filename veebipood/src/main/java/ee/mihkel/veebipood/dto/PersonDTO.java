package ee.mihkel.veebipood.dto;

// DTO --> Data Transfer Object. Entity, aga muudetud kujul.
// Model --> Päringutest kasutatav mudel

import lombok.Data;

@Data
public class PersonDTO {
    private String firstName;
    private String lastName;
}
