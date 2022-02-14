package com.tenniscourts.guests;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Data
public class UpdateGuestRequestDTO {

    private String name;

    private String email;

    private String phoneNumber;
}
