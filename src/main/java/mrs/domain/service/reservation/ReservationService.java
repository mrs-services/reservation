package mrs.domain.service.reservation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mrs.domain.model.ReservableRoom;
import mrs.domain.model.ReservableRoomId;
import mrs.domain.model.Reservation;
import mrs.domain.repository.reservation.AlreadyReservedException;
import mrs.domain.repository.reservation.ReservationRepository;
import mrs.domain.repository.reservation.UnavailableReservationException;
import mrs.domain.repository.room.ReservableRoomRepository;

@Service
@Transactional
public class ReservationService {
	private final ReservationRepository reservationRepository;
	private final ReservableRoomRepository reservableRoomRepository;
	private final static Logger log = LoggerFactory.getLogger(ReservationService.class);

	public ReservationService(ReservationRepository reservationRepository,
			ReservableRoomRepository reservableRoomRepository) {
		this.reservationRepository = reservationRepository;
		this.reservableRoomRepository = reservableRoomRepository;
	}

	public void checkReservation(Reservation reservation, CheckMode checkMode) {
		log.info("check reservation {}", reservation);
		if (reservation.getReservableRoom() == null) {
			throw new UnavailableReservationException("入力の日付・部屋の組み合わせは予約できません。");
		}
		ReservableRoomId reservableRoomId = reservation.getReservableRoom()
				.getReservableRoomId();
		ReservableRoom reservable = null;
		switch (checkMode) {
		case CHECK_FOR_UPDATE:
			// 悲観ロック
			reservable = reservableRoomRepository
					.findOneForUpdateByReservableRoomId(reservableRoomId);
			break;
		case CHECK_ONLY:
			reservable = reservableRoomRepository.findOne(reservableRoomId);
			break;
		}
		if (reservable == null) {
			throw new UnavailableReservationException("入力の日付・部屋の組み合わせを取得できません。");
		}
		// 該当の日付・部屋の全予約情報をReservableRoomテーブルから取得し、重複をチェック
		boolean overlap = reservationRepository
				.findByReservableRoom_ReservableRoomIdOrderByStartTimeAsc(
						reservableRoomId).stream().peek(System.out::println).anyMatch(x -> x.overlap(reservation));
		if (overlap) {
			throw new AlreadyReservedException("入力の時間帯は既に予約済みです。");
		}
	}

	public enum CheckMode {
		CHECK_FOR_UPDATE, CHECK_ONLY;
	}
}
