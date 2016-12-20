package mrs.domain.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Embeddable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Embeddable
public class ReservableRoomId implements Serializable {
	private static final Pattern ID_PATTERN = Pattern
			.compile("(http://.+/)?([0-9]+)_([0-9]{4}-[0-9]{2}-[0-9]{2})$");
	private static final Logger log = LoggerFactory.getLogger(ReservableRoomId.class);
	private Integer roomId;

	private LocalDate reservedDate;

	public ReservableRoomId(Integer roomId, LocalDate reservedDate) {
		this.roomId = roomId;
		this.reservedDate = reservedDate;
	}

	public ReservableRoomId() {

	}

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public LocalDate getReservedDate() {
		return reservedDate;
	}

	public void setReservedDate(LocalDate reservedDate) {
		this.reservedDate = reservedDate;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("");
		sb.append(roomId).append("_").append(reservedDate);
		return sb.toString();
	}

	public static ReservableRoomId valueOf(String source) {
		log.info("ReservableRoomId valueOf({}), ", source);
		if (source == null)
			return null;
		Matcher matcher = ID_PATTERN.matcher(source);
		if (matcher.matches()) {
			Integer roomId = Integer.valueOf(matcher.group(2));
			LocalDate reservedDate = LocalDate.parse(matcher.group(3),
					DateTimeFormatter.ISO_LOCAL_DATE);
			ReservableRoomId id = new ReservableRoomId(roomId, reservedDate);
			log.info("converted -> {}", id);
			return id;
		}
		else {
			log.warn("convert failed -> {}", source);
			return null;
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		ReservableRoomId that = (ReservableRoomId) o;

		if (reservedDate != null ? !reservedDate.equals(that.reservedDate)
				: that.reservedDate != null)
			return false;
		if (roomId != null ? !roomId.equals(that.roomId) : that.roomId != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = roomId != null ? roomId.hashCode() : 0;
		result = 31 * result + (reservedDate != null ? reservedDate.hashCode() : 0);
		return result;
	}
}
