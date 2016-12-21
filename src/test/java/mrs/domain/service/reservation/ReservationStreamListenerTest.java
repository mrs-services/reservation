package mrs.domain.service.reservation;

import static mrs.Fixutures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.verify;

import java.time.LocalTime;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessagingException;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import mrs.ReservationApplication;
import mrs.domain.event.ReserveEvent;
import mrs.domain.model.ReservableRoomId;
import mrs.domain.model.Reservation;
import mrs.domain.repository.reservation.ReservationRepository;
import mrs.domain.repository.room.ReservableRoomRepository;
import mrs.domain.service.notification.NotificationClient;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, properties = {
		"stubrunner.ids-to-service-ids.notification=notification" }, classes = {
				ReservationApplication.class, ReservationStreamListenerTest.StubConf.class })
@AutoConfigureStubRunner(ids = "mrs:notification", workOffline = true)
public class ReservationStreamListenerTest {
	@Autowired
	Sink sink;
	@MockBean
	ReservationRepository reservationRepository;
	@MockBean
	ReservableRoomRepository reservableRoomRepository;
	@SpyBean
	ReservationService reservationService;
	@SpyBean
	NotificationClient notificationClient;
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testReserve() throws Exception {
		ReserveEvent event = new ReserveEvent(
				reservableRoom_1_2016_12_20().getReservableRoomId().toString(),
				LocalTime.of(9, 0), LocalTime.of(10, 0));
		given(reservableRoomRepository.findOneForUpdateByReservableRoomId(
				ReservableRoomId.valueOf(event.getReservableRoomId())))
						.willReturn(reservableRoom_1_2016_12_20());
		sink.input().send(MessageBuilder.withPayload(event)
				.setHeader("accessToken", accessToken_makiExampleCom()).build());

		ArgumentCaptor<Reservation> captor = ArgumentCaptor.forClass(Reservation.class);
		verify(reservationRepository).saveAndFlush(captor.capture());
		assertThat(captor.getValue().getUserId()).isEqualTo("maki@example.com");
		assertThat(captor.getValue().getStartTime()).isEqualTo(LocalTime.of(9, 0));
		assertThat(captor.getValue().getEndTime()).isEqualTo(LocalTime.of(10, 0));
		assertThat(captor.getValue().getReservableRoom())
				.isEqualTo(reservableRoom_1_2016_12_20());
	}

	@Test
	public void testReserve_UnavailableReservation() throws Exception {
		ReserveEvent event = new ReserveEvent(
				reservableRoom_1_2016_12_20().getReservableRoomId().toString(),
				LocalTime.of(9, 0), LocalTime.of(10, 0));
		given(reservableRoomRepository.findOneForUpdateByReservableRoomId(
				ReservableRoomId.valueOf(event.getReservableRoomId()))).willReturn(null);

		sink.input().send(MessageBuilder.withPayload(event)
				.setHeader("accessToken", accessToken_makiExampleCom()).build());

		ArgumentCaptor<NotificationClient.Type> captorForType = ArgumentCaptor
				.forClass(NotificationClient.Type.class);
		ArgumentCaptor<String> captorForMessage = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<String> captorForUserId = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<String> captorForAccessToken = ArgumentCaptor
				.forClass(String.class);
		verify(notificationClient).createNotification(captorForType.capture(),
				captorForMessage.capture(), captorForUserId.capture(),
				captorForAccessToken.capture());

		assertThat(captorForType.getValue()).isEqualTo(NotificationClient.Type.ERROR);
		assertThat(captorForMessage.getValue()).isEqualTo("入力の日付・部屋の組み合わせを取得できません。");
		assertThat(captorForUserId.getValue()).isEqualTo("maki@example.com");
		assertThat(captorForAccessToken.getValue())
				.isEqualTo(accessToken_makiExampleCom());
	}

	@Test
	public void testReserve_AlreadyReserved() throws Exception {
		ReserveEvent event = new ReserveEvent(
				reservableRoom_1_2016_12_20().getReservableRoomId().toString(),
				LocalTime.of(9, 0), LocalTime.of(10, 0));
		given(reservableRoomRepository.findOneForUpdateByReservableRoomId(
				ReservableRoomId.valueOf(event.getReservableRoomId())))
						.willReturn(reservableRoom_1_2016_12_20());
		given(reservationRepository
				.findByReservableRoom_ReservableRoomIdOrderByStartTimeAsc(
						reservableRoom_1_2016_12_20().getReservableRoomId()))
								.willReturn(reservations_2016_12_20());

		sink.input().send(MessageBuilder.withPayload(event)
				.setHeader("accessToken", accessToken_makiExampleCom()).build());

		ArgumentCaptor<NotificationClient.Type> captorForType = ArgumentCaptor
				.forClass(NotificationClient.Type.class);
		ArgumentCaptor<String> captorForMessage = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<String> captorForUserId = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<String> captorForAccessToken = ArgumentCaptor
				.forClass(String.class);
		verify(notificationClient).createNotification(captorForType.capture(),
				captorForMessage.capture(), captorForUserId.capture(),
				captorForAccessToken.capture());

		assertThat(captorForType.getValue()).isEqualTo(NotificationClient.Type.ERROR);
		assertThat(captorForMessage.getValue()).isEqualTo("入力の時間帯は既に予約済みです。");
		assertThat(captorForUserId.getValue()).isEqualTo("maki@example.com");
		assertThat(captorForAccessToken.getValue())
				.isEqualTo(accessToken_makiExampleCom());
	}

	@Test
	public void testReserve_DB_Failure() throws Exception {
		ReserveEvent event = new ReserveEvent(
				reservableRoom_1_2016_12_20().getReservableRoomId().toString(),
				LocalTime.of(9, 0), LocalTime.of(10, 0));
		given(reservableRoomRepository.findOneForUpdateByReservableRoomId(
				ReservableRoomId.valueOf(event.getReservableRoomId())))
						.willReturn(reservableRoom_1_2016_12_20());
		given(reservationRepository.saveAndFlush(anyObject()))
				.willThrow(new DataAccessResourceFailureException("DB Down!"));

		thrown.expect(MessagingException.class);
		thrown.expectCause(allOf(instanceOf(DataAccessException.class),
				hasProperty("message", is("DB Down!"))));
		sink.input().send(MessageBuilder.withPayload(event)
				.setHeader("accessToken", accessToken_makiExampleCom()).build());

		ArgumentCaptor<NotificationClient.Type> captorForType = ArgumentCaptor
				.forClass(NotificationClient.Type.class);
		ArgumentCaptor<String> captorForMessage = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<String> captorForUserId = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<String> captorForAccessToken = ArgumentCaptor
				.forClass(String.class);
		verify(notificationClient).createNotification(captorForType.capture(),
				captorForMessage.capture(), captorForUserId.capture(),
				captorForAccessToken.capture());

		assertThat(captorForType.getValue()).isEqualTo(NotificationClient.Type.WARN);
		assertThat(captorForMessage.getValue())
				.isEqualTo("一部で一部で障害が発生中です。データの反映まで時間がかかる場合があります。");
		assertThat(captorForUserId.getValue()).isEqualTo("maki@example.com");
		assertThat(captorForAccessToken.getValue())
				.isEqualTo(accessToken_makiExampleCom());
	}

	@Configuration
	static class StubConf {
		@Bean
		ObjectPostProcessor postProcessor() {
			return new ObjectPostProcessor() {
				@Override
				public Object postProcess(Object object) {
					return object;
				}
			};
		}

		@Bean
		AuthenticationConfiguration authenticationConfiguration() {
			return new AuthenticationConfiguration();
		}
	}
}