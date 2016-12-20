package mrs.domain.service.reservation;

import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import mrs.domain.model.Reservation;

@Component
@Transactional
@RepositoryEventHandler(Reservation.class)
public class ReservationEventHandler {
	private final ReservationService reservationService;

	public ReservationEventHandler(ReservationService reservationService) {
		this.reservationService = reservationService;
	}

	@HandleBeforeCreate
	public void beforeReserve(Reservation reservation) {
		// delegate
		reservationService.checkReservation(reservation, ReservationService.CheckMode.CHECK_FOR_UPDATE);
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		reservation.setUserId(userId);
	}

	// USERロールの場合は予約者がログインユーザーの場合に取り消し可能
	// ADMINロールの場合は全予約取り消し可能
	@HandleBeforeDelete
	@PreAuthorize("hasRole('ADMIN') or #reservation.user.userId == principal.user.userId")
	public void beforeCancel(@P("reservation") Reservation reservation) {
	}
}
