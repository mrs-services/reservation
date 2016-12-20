package mrs.domain.repository.room;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import mrs.domain.model.ReservableRoom;
import mrs.domain.model.ReservableRoomId;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

public interface ReservableRoomRepository
		extends JpaRepository<ReservableRoom, ReservableRoomId> {
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@RestResource(exported = false)
	ReservableRoom findOneForUpdateByReservableRoomId(ReservableRoomId reservableRoomId);

	@RestResource(rel = "findByReservedDate", path = "findByReservedDate")
	List<ReservableRoom> findByReservableRoomId_reservedDateOrderByReservableRoomId_roomIdAsc(
			@Param("reservedDate") LocalDate reservedDate);
}
