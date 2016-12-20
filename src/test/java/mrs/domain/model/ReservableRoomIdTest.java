package mrs.domain.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.Test;

public class ReservableRoomIdTest {
	@Test
	public void valueOf() throws Exception {
		{
			ReservableRoomId id = ReservableRoomId.valueOf("1_2016-12-20");
			assertThat(id).isNotNull();
			assertThat(id.getRoomId()).isEqualTo(1);
			assertThat(id.getReservedDate()).isEqualTo(LocalDate.of(2016, 12, 20));
		}
		{
			ReservableRoomId id = ReservableRoomId
					.valueOf("http://localhost:8081/v1/reservableRooms/1_2016-12-20");
			assertThat(id).isNotNull();
			assertThat(id.getRoomId()).isEqualTo(1);
			assertThat(id.getReservedDate()).isEqualTo(LocalDate.of(2016, 12, 20));
		}
		{
			ReservableRoomId id = ReservableRoomId
					.valueOf("reservableRooms/1_2016-12-20");
			assertThat(id).isNull();
		}
	}

}