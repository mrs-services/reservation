package mrs.domain.service.reservation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.dao.DataAccessException;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.Assert;

import mrs.domain.event.ReserveEvent;
import mrs.domain.model.ReservableRoom;
import mrs.domain.model.ReservableRoomId;
import mrs.domain.model.Reservation;
import mrs.domain.repository.reservation.AlreadyReservedException;
import mrs.domain.repository.reservation.ReservationRepository;
import mrs.domain.repository.reservation.UnavailableReservationException;
import mrs.domain.service.notification.NotificationClient;

@Component
public class ReservationSink {
	private final static Logger log = LoggerFactory.getLogger(ReservationSink.class);
	private final ReservationRepository reservationRepository;
	private final ReservationService reservationService;
	private final NotificationClient notificationClient;

	public ReservationSink(ReservationRepository reservationRepository,
			ReservationService reservationService,
			NotificationClient notificationClient) {
		this.reservationRepository = reservationRepository;
		this.reservationService = reservationService;
		this.notificationClient = notificationClient;
	}

	@StreamListener(Sink.INPUT)
	@Transactional
	public void reserve(Message<ReserveEvent> message) {
		log.info("Handling {}", message);
		String accessToken = message.getHeaders().get("accessToken", String.class);
		Assert.notNull(accessToken, "accessToken must not be null");
		ReservationUser user = ReservationUser.fromJwtAccessToken(accessToken);
		// TODO check scope, authorities
		// TODO check expiry
		ReserveEvent event = message.getPayload();
		Reservation reservation = new Reservation();
		reservation.setStartTime(event.getStartTime());
		reservation.setEndTime(event.getEndTime());
		reservation.setReservableRoom(new ReservableRoom(
				ReservableRoomId.valueOf(event.getReservableRoomId())));
		reservation.setUserId(user.getUserName());
		try {
			reservationService.checkReservation(reservation,
					ReservationService.CheckMode.CHECK_FOR_UPDATE);
		}
		catch (UnavailableReservationException | AlreadyReservedException e) {
			notificationClient.createNotification(NotificationClient.Type.ERROR,
					e.getMessage(), user.getUserName(), accessToken);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return;
		}
		try {
			reservationRepository.saveAndFlush(reservation);
		}
		catch (DataAccessException e) {
			log.warn("DB error", e);
			notificationClient.createNotification(NotificationClient.Type.WARN,
					"一部で障害が発生中です。データの反映まで時間がかかる場合があります。", user.getUserName(),
					accessToken);
			throw e;
		}
	}
}
