package mrs;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import mrs.domain.model.ReservableRoom;
import mrs.domain.model.ReservableRoomId;
import mrs.domain.model.Reservation;

public class Fixutures {
	public static String accessToken_makiExampleCom() {
		return "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsib2F1dGgyLXJlc291cmNlIl0sInVzZXJfaWQiOiIwMDAwMDAwMC0wMDAwLTAwMDAtMDAwMC0wMDAwMDAwMDAwMDAiLCJ1c2VyX25hbWUiOiJtYWtpQGV4YW1wbGUuY29tIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIl0sImV4cCI6MTQ4MjQzMzI4OCwiZ2l2ZW5fbmFtZSI6IlRvc2hpYWtpIiwiZGlzcGxheV9uYW1lIjoiTWFraSBUb3NoaWFraSIsImZhbWlseV9uYW1lIjoiTWFraSIsImF1dGhvcml0aWVzIjpbIlJPTEVfQURNSU4iLCJST0xFX1VTRVIiXSwianRpIjoiM2NjZGYxZGYtM2M1OC00YWZiLTlmMmMtNGU5MDM0ZDRkYzZjIiwiY2xpZW50X2lkIjoiMDAwMDAwMDAtMDAwMC0wMDAwLTAwMDAtMDAwMDAwMDAwMDAxIn0.eDA8uploygzedH_hmWrJP9Lj_GpzNFVPASuVBdsrqiankPtO51VLi_SNt765LDAaJPGIpu7LihZzOnM4-boGI9LePoROs9eJpTUZl0WgiSxzrWS03nNu2nOmvOEUJ6PjczzIMX9Q29NL1lKBnj-OLql2GKTH4RGFFhRbM9F-TOMgbuJkzJpvzRS3NRlUjb2SriRHayYa4bpvJJlwVhvXtf-6IihJ4I9bEjsQ3q8AQxSLzKOpS00tI9I2lqe8YjGpK1bHBIECt57e5O6JtT_E532N8e5_8O1HHQ9lmm714r6hddJTkxle5qx_zEXF7D70VRX0fj2GfBnKFTmFnTDpLg";
	}

	public static ReservableRoom reservableRoom_1_2016_12_20() {
		ReservableRoom reservableRoom = new ReservableRoom(
				ReservableRoomId.valueOf("1_2016-12-20"));
		return reservableRoom;
	}

	public static List<Reservation> reservations_2016_12_20() {
		Reservation r1 = new Reservation();
		r1.setUserId("maki@example.com");
		r1.setReservableRoom(reservableRoom_1_2016_12_20());
		r1.setStartTime(LocalTime.of(9, 0));
		r1.setEndTime(LocalTime.of(12, 0));
		return Collections.singletonList(r1);
	}
}
