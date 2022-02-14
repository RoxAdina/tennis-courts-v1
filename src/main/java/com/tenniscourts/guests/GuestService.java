package com.tenniscourts.guests;

import com.tenniscourts.exceptions.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GuestService {

    private final GuestRepository guestRepository;

    private final GuestMapper guestMapper;

    public GuestDTO addGuest(CreateGuestRequestDTO createGuestRequestDTO) {

        Guest guest = Guest.builder()
                .name(createGuestRequestDTO.getName())
                .email(createGuestRequestDTO.getEmail())
                .phoneNumber(createGuestRequestDTO.getPhoneNumber())
                .isActive(true)
                .build();
        return guestMapper.map(guestRepository.save(guest));
    }

    public GuestDTO updateGuest(Long guestId, UpdateGuestRequestDTO updateGuestRequestDTO) {

        Guest guest = guestRepository.findById(guestId)
                .orElseThrow(() -> new EntityNotFoundException("Guest not found."));

        if (!isNullOrEmpty(updateGuestRequestDTO.getName())) {
            guest.setName(updateGuestRequestDTO.getName());
        }
        if (!isNullOrEmpty(updateGuestRequestDTO.getEmail())) {
            guest.setEmail(updateGuestRequestDTO.getEmail());
        }
        if (!isNullOrEmpty(updateGuestRequestDTO.getPhoneNumber())) {
            guest.setPhoneNumber(updateGuestRequestDTO.getPhoneNumber());
        }

        return guestMapper.map(guestRepository.save(guest));
    }

    // Keeps guest id for history purposes and removes all personal data
    public GuestDTO deleteGuest(Long guestId) {

        Guest guest = guestRepository.findById(guestId)
                .orElseThrow(() -> new EntityNotFoundException("Guest not found."));

        guest.setName("");
        guest.setEmail("");
        guest.setPhoneNumber("");
        guest.setActive(false);

        return guestMapper.map(guestRepository.save(guest));
    }

    public GuestDTO findGuestById(Long guestId) {
        return guestRepository.findById(guestId).map(guestMapper::map)
                .orElseThrow(() -> new EntityNotFoundException("Guest not found."));
    }

    public List<GuestDTO> findGuestsByName(String guestName) {
        return guestMapper.map(guestRepository.findByNameContainingIgnoreCase(guestName));
    }

    public List<GuestDTO> getAllGuests() {
        return guestMapper.map(guestRepository.findByIsActive(true));
    }

    private boolean isNullOrEmpty(String str) {
        return (str == null || str.trim().isEmpty());
    }
}
