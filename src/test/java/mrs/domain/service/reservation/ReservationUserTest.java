package mrs.domain.service.reservation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class ReservationUserTest {
	@Test
	public void testFromJwtAccessToken() throws Exception {
		String accessToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsib2F1dGgyLXJlc291cmNlIl0sInVzZXJfaWQiOiIwMDAwMDAwMC0wMDAwLTAwMDAtMDAwMC0wMDAwMDAwMDAwMDAiLCJ1c2VyX25hbWUiOiJtYWtpQGV4YW1wbGUuY29tIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIl0sImV4cCI6MTQ4MjQzMzI4OCwiZ2l2ZW5fbmFtZSI6IlRvc2hpYWtpIiwiZGlzcGxheV9uYW1lIjoiTWFraSBUb3NoaWFraSIsImZhbWlseV9uYW1lIjoiTWFraSIsImF1dGhvcml0aWVzIjpbIlJPTEVfQURNSU4iLCJST0xFX1VTRVIiXSwianRpIjoiM2NjZGYxZGYtM2M1OC00YWZiLTlmMmMtNGU5MDM0ZDRkYzZjIiwiY2xpZW50X2lkIjoiMDAwMDAwMDAtMDAwMC0wMDAwLTAwMDAtMDAwMDAwMDAwMDAxIn0.eDA8uploygzedH_hmWrJP9Lj_GpzNFVPASuVBdsrqiankPtO51VLi_SNt765LDAaJPGIpu7LihZzOnM4-boGI9LePoROs9eJpTUZl0WgiSxzrWS03nNu2nOmvOEUJ6PjczzIMX9Q29NL1lKBnj-OLql2GKTH4RGFFhRbM9F-TOMgbuJkzJpvzRS3NRlUjb2SriRHayYa4bpvJJlwVhvXtf-6IihJ4I9bEjsQ3q8AQxSLzKOpS00tI9I2lqe8YjGpK1bHBIECt57e5O6JtT_E532N8e5_8O1HHQ9lmm714r6hddJTkxle5qx_zEXF7D70VRX0fj2GfBnKFTmFnTDpLg";
		ReservationUser user = ReservationUser.fromJwtAccessToken(accessToken);

		assertThat(user.getUserId()).isEqualTo("00000000-0000-0000-0000-000000000000");
		assertThat(user.getUserName()).isEqualTo("maki@example.com");
		assertThat(user.getDisplayName()).isEqualTo("Maki Toshiaki");
		assertThat(user.getClientId()).isEqualTo("00000000-0000-0000-0000-000000000001");
		assertThat(user.getScope()).containsExactly("read", "write");
		assertThat(user.getAuthorities()).containsExactly("ROLE_USER", "ROLE_ADMIN");
		assertThat(user.getExp().toString()).isEqualTo("2016-12-22T19:01:28Z");
	}
}