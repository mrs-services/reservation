package mrs.domain.service.reservation;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.dao.DataAccessException;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.Assert;

import mrs.ReservationSink;
import mrs.domain.event.CancelEvent;
import mrs.domain.event.ReserveEvent;
import mrs.domain.model.ReservableRoom;
import mrs.domain.model.ReservableRoomId;
import mrs.domain.model.Reservation;
import mrs.domain.repository.reservation.AlreadyReservedException;
import mrs.domain.repository.reservation.ReservationRepository;
import mrs.domain.repository.reservation.UnavailableReservationException;
import mrs.domain.service.notification.NotificationClient;

@Component
@Transactional
public class ReservationStreamListener {
	private final static Logger log = LoggerFactory
			.getLogger(ReservationStreamListener.class);
	private final ReservationRepository reservationRepository;
	private final ReservationService reservationService;
	private final NotificationClient notificationClient;

	public ReservationStreamListener(ReservationRepository reservationRepository,
			ReservationService reservationService,
			NotificationClient notificationClient) {
		this.reservationRepository = reservationRepository;
		this.reservationService = reservationService;
		this.notificationClient = notificationClient;
	}

	@StreamListener(ReservationSink.RESERVE_INPUT)
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

		tryDataAccess(() -> {
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
			reservationRepository.saveAndFlush(reservation);
		}, user.getUserName(), accessToken);
	}

	@StreamListener(ReservationSink.CANCEL_INPUT)
	public void cancel(Message<CancelEvent> message) {
		log.info("Handling {}", message);
		String accessToken = message.getHeaders().get("accessToken", String.class);
		Assert.notNull(accessToken, "accessToken must not be null");
		ReservationUser user = ReservationUser.fromJwtAccessToken(accessToken);
		CancelEvent event = message.getPayload();

		tryDataAccess(() -> {
			Reservation reservation = reservationRepository
					.findOne(event.getReservationId());
			if (reservation == null) {
				return;
			}
			if (!user.getAuthorities().contains("ROLE_ADMIN")
					&& !Objects.equals(user.getUserName(), reservation.getUserId())) {
				notificationClient.createNotification(NotificationClient.Type.ERROR,
						"権限のない操作が行われました。", user.getUserName(), accessToken);
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return;
			}
			reservationRepository.delete(reservation);
		}, user.getUserName(), accessToken);
	}

	void tryDataAccess(VoidFunction function, String userId, String accessToken) {
		try {
			function.apply();
		}
		catch (DataAccessException e) {
			log.warn("DB error", e);
			notificationClient.createNotification(NotificationClient.Type.WARN,
					"一部で障害が発生中です。データの反映まで時間がかかる場合があります。", userId, accessToken);
			throw e;
		}
	}

	@FunctionalInterface
	public interface VoidFunction {
		void apply();
	}
}
