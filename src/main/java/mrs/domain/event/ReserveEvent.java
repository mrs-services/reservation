package mrs.domain.event;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ReserveEvent implements DomainEvent {
	public static final String TYPE = "reserve";
	private final String reservableRoomId;
	private final LocalTime startTime;
	private final LocalTime endTime;

	@JsonCreator
	public ReserveEvent(@JsonProperty("reservableRoomId") String reservableRoomId,
			@JsonProperty("startTime") LocalTime startTime,
			@JsonProperty("endTime") LocalTime endTime) {
		this.reservableRoomId = reservableRoomId;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public String getReservableRoomId() {
		return reservableRoomId;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	@Override
	public String toString() {
		return "ReserveEvent{" + "reservableRoomId='" + reservableRoomId + '\''
				+ ", startTime=" + startTime + ", endTime=" + endTime + '}';
	}
}