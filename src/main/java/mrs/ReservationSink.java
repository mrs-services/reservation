package mrs;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface ReservationSink {
	String RESERVE_INPUT = "reserve-input";
	String CANCEL_INPUT = "cancel-input";

	@Input(ReservationSink.RESERVE_INPUT)
	SubscribableChannel reserveInput();

	@Input(ReservationSink.CANCEL_INPUT)
	SubscribableChannel cancelInput();
}
