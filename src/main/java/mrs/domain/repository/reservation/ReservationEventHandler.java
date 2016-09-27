package mrs.domain.repository.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import mrs.domain.model.ReservableRoom;
import mrs.domain.model.ReservableRoomId;
import mrs.domain.model.Reservation;
import mrs.domain.repository.room.ReservableRoomRepository;

@Component
@Transactional
@RepositoryEventHandler(Reservation.class)
public class ReservationEventHandler {
	@Autowired
	ReservationRepository reservationRepository;

	@Autowired
	ReservableRoomRepository reservableRoomRepository;

	@HandleBeforeCreate
	public void beforeReserve(Reservation reservation) {
		if (reservation.getReservableRoom() == null) {
			throw new UnavailableReservationException("入力の日付・部屋の組み合わせは予約できません。");
		}
		ReservableRoomId reservableRoomId = reservation.getReservableRoom()
				.getReservableRoomId();
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		reservation.setUserId(userId);
		// 悲観ロック
		ReservableRoom reservable = reservableRoomRepository
				.findOneForUpdateByReservableRoomId(reservableRoomId);
		if (reservable == null) {
			throw new UnavailableReservationException("入力の日付・部屋の組み合わせを取得できません。");
		}
		// 該当の日付・部屋の全予約情報をReservableRoomテーブルから取得し、重複をチェック
		boolean overlap = reservationRepository
				.findByReservableRoom_ReservableRoomIdOrderByStartTimeAsc(
						reservableRoomId)
				.stream().anyMatch(x -> x.overlap(reservation));
		if (overlap) {
			throw new AlreadyReservedException("入力の時間帯は既に予約済みです。");
		}
	}

	// USERロールの場合は予約者がログインユーザーの場合に取り消し可能
	// ADMINロールの場合は全予約取り消し可能
	@HandleBeforeDelete
	@PreAuthorize("hasRole('ADMIN') or #reservation.user.userId == principal.user.userId")
	public void beforeCancel(@P("reservation") Reservation reservation) {
	}
}
