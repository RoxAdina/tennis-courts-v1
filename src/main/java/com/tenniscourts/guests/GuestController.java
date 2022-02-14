package com.tenniscourts.guests;

import com.tenniscourts.config.BaseRestController;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/tennis-courts/guest")
@AllArgsConstructor
public class GuestController extends BaseRestController {

    @Autowired
    private final GuestService guestService;

    @PostMapping
    public ResponseEntity<Void> addGuest(@RequestBody CreateGuestRequestDTO createGuestRequestDTO) {
        return ResponseEntity.created(locationByEntity(guestService.addGuest(createGuestRequestDTO).getId())).build();
    }

    @PutMapping(path = "{guestId}")
    public ResponseEntity<GuestDTO> updateGuest(@PathVariable("guestId") Long guestId, @RequestBody UpdateGuestRequestDTO updateGuestRequestDTO) {
        return ResponseEntity.ok(guestService.updateGuest(guestId, updateGuestRequestDTO));
    }

    @DeleteMapping(path = "{guestId}")
    public ResponseEntity<GuestDTO> deleteGuest(@PathVariable("guestId") Long guestId) {
        return ResponseEntity.ok(guestService.deleteGuest(guestId));
    }

    @GetMapping(path = "{guestId}")
    public ResponseEntity<GuestDTO> findGuestById(@PathVariable("guestId") Long guestId) {
        return ResponseEntity.ok(guestService.findGuestById(guestId));
    }

    @GetMapping
    public ResponseEntity<List<GuestDTO>> findGuestsByName(@RequestParam String guestName) {
        return ResponseEntity.ok(guestService.findGuestsByName(guestName));
    }

    @GetMapping(path = "/list")
    public ResponseEntity<List<GuestDTO>> getAllGuests() {
        return ResponseEntity.ok(guestService.getAllGuests());
    }
}
