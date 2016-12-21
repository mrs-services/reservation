package mrs.domain.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CancelEvent implements DomainEvent {
	public static final String TYPE = "cancel";
	private final Integer reservationId;

	@JsonCreator
	public CancelEvent(@JsonProperty("reservationId") Integer reservationId) {
		this.reservationId = reservationId;
	}

	public Integer getReservationId() {
		return reservationId;
	}

	@Override
	public String toString() {
		return "CancelEvent{" + "reservationId=" + reservationId + '}';
	}
}
