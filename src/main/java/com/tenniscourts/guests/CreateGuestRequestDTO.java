package com.tenniscourts.guests;

import lombok.*;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Data
public class CreateGuestRequestDTO {

    @NotNull
    private String name;

    private String email;

    @NotNull
    private String phoneNumber;
}
