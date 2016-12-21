package mrs;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import mrs.domain.model.ReservableRoom;
import mrs.domain.model.ReservableRoomId;
import mrs.domain.model.Reservation;

public class Fixutures {
	public static String accessToken_makiExampleCom() {
		return "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsib2F1dGgyLXJlc291cmNlIl0sInVzZXJfaWQiOiIwMDAwMDAwMC0wMDAwLTAwMDAtMDAwMC0wMDAwMDAwMDAwMDAiLCJ1c2VyX25hbWUiOiJtYWtpQGV4YW1wbGUuY29tIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIl0sImV4cCI6MTQ4MjQzMzI4OCwiZ2l2ZW5fbmFtZSI6IlRvc2hpYWtpIiwiZGlzcGxheV9uYW1lIjoiTWFraSBUb3NoaWFraSIsImZhbWlseV9uYW1lIjoiTWFraSIsImF1dGhvcml0aWVzIjpbIlJPTEVfQURNSU4iLCJST0xFX1VTRVIiXSwianRpIjoiM2NjZGYxZGYtM2M1OC00YWZiLTlmMmMtNGU5MDM0ZDRkYzZjIiwiY2xpZW50X2lkIjoiMDAwMDAwMDAtMDAwMC0wMDAwLTAwMDAtMDAwMDAwMDAwMDAxIn0.eDA8uploygzedH_hmWrJP9Lj_GpzNFVPASuVBdsrqiankPtO51VLi_SNt765LDAaJPGIpu7LihZzOnM4-boGI9LePoROs9eJpTUZl0WgiSxzrWS03nNu2nOmvOEUJ6PjczzIMX9Q29NL1lKBnj-OLql2GKTH4RGFFhRbM9F-TOMgbuJkzJpvzRS3NRlUjb2SriRHayYa4bpvJJlwVhvXtf-6IihJ4I9bEjsQ3q8AQxSLzKOpS00tI9I2lqe8YjGpK1bHBIECt57e5O6JtT_E532N8e5_8O1HHQ9lmm714r6hddJTkxle5qx_zEXF7D70VRX0fj2GfBnKFTmFnTDpLg";
	}

	public static String accessToken_demoExampleCom() {
		return "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsib2F1dGgyLXJlc291cmNlIl0sInVzZXJfaWQiOiIwMDAwMDAwMC0wMDAwLTAwMDAtMDAwMC0wMDAwMDAwMDAwMDEiLCJ1c2VyX25hbWUiOiJkZW1vQGV4YW1wbGUuY29tIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIl0sImV4cCI6MTQ4MjQ5Mjg3MywiZ2l2ZW5fbmFtZSI6IlRhcm8iLCJkaXNwbGF5X25hbWUiOiJEZW1vIFRhcm8iLCJmYW1pbHlfbmFtZSI6IkRlbW8iLCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXSwianRpIjoiYWE0MGE5OTUtMjdhMi00OWMzLTgyOTktZWMwZjI3YWE5ZWNiIiwiY2xpZW50X2lkIjoiMDAwMDAwMDAtMDAwMC0wMDAwLTAwMDAtMDAwMDAwMDAwMDAxIn0.dkx04hIddU2wkVbIR77a9ehK9RN34PmymxhqBDO2WwgOrGUSZ9UYSs_qBDKCGtM2HqfkM3ZX9MKJzGvFC87ORFkDpuyJj3CyjWV9TQWotP1R1k4SU475iYQ_ovPK6nJsRc6sc_sqMFveHyr0t14uPnc3Vq-zYg454zrTOb9ei6ayaUOoKsQxM0Pm3ZhdUMdty_Ge91GnIh9fn3ptsRysBbsHs3PgLCkwNNCyeeHdt_IQ-uk2b7KlSqNOWUxNqJ4wuHiu4dbNsrjOzRMOwkP5jsQjqW26MPJW5c1e9flYwsgNEBEgybmDrhw2i7IJl7GplogOVK8VoAvLjXZoi7vyoA";
	}

	public static ReservableRoom reservableRoom_1_2016_12_20() {
		ReservableRoom reservableRoom = new ReservableRoom(
				ReservableRoomId.valueOf("1_2016-12-20"));
		return reservableRoom;
	}

	public static Reservation reservation_1() {
		Reservation r = new Reservation();
		r.setReservationId(1);
		r.setUserId("maki@example.com");
		r.setReservableRoom(reservableRoom_1_2016_12_20());
		r.setStartTime(LocalTime.of(9, 0));
		r.setEndTime(LocalTime.of(12, 0));
		return r;
	}

	public static Reservation reservation_2() {
		Reservation r = new Reservation();
		r.setReservationId(2);
		r.setUserId("demo@example.com");
		r.setReservableRoom(reservableRoom_1_2016_12_20());
		r.setStartTime(LocalTime.of(12, 0));
		r.setEndTime(LocalTime.of(13, 0));
		return r;
	}

	public static List<Reservation> reservations_2016_12_20() {
		return Arrays.asList(reservation_1(), reservation_2());
	}
}
