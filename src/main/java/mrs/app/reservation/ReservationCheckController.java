package mrs.app.reservation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import mrs.domain.model.Reservation;
import mrs.domain.service.reservation.ReservationService;

@RestController
public class ReservationCheckController {
	private final ReservationService reservationService;

	public ReservationCheckController(ReservationService reservationService) {
		this.reservationService = reservationService;
	}

	@PostMapping(path = "v1/reservations/check")
	@ResponseStatus(HttpStatus.OK)
	void check(@RequestBody Reservation reservation) {
		reservationService.checkReservation(reservation,
				ReservationService.CheckMode.CHECK_ONLY);
	}
}
