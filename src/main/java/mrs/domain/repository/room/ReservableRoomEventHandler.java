package mrs.domain.repository.room;

import mrs.domain.model.MeetingRoom;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import mrs.domain.model.ReservableRoom;

@Component
@Transactional
@RepositoryEventHandler(ReservableRoom.class)
public class ReservableRoomEventHandler {

	@HandleBeforeCreate
	public void beforeCreate(ReservableRoom reservableRoom) {
		System.out.println("beforeCreate " + reservableRoom);
		if (reservableRoom.getMeetingRoom() == null
				&& reservableRoom.getReservableRoomId() != null) {
			MeetingRoom room = new MeetingRoom();
			room.setRoomId(reservableRoom.getReservableRoomId().getRoomId());
			reservableRoom.setMeetingRoom(room);
		}
	}
}
