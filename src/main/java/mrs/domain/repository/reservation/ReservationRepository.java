package mrs.domain.repository.reservation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mrs.domain.model.ReservableRoomId;
import mrs.domain.model.Reservation;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

	@RestResource(rel = "findByReservableRoomId", path = "findByReservableRoomId")
	List<Reservation> findByReservableRoom_ReservableRoomIdOrderByStartTimeAsc(
			@Param("reservableRoomId") ReservableRoomId reservableRoomId);
}
