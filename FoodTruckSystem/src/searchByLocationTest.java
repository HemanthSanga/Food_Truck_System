import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;

class searchByLocationTest {

	@Test
	void test() {
		searchByLocation test = new searchByLocation();
		try {
			int outTruckId = test.searchLocation();
			assertEquals(1, outTruckId);
		} catch (SQLException e) {
		} catch (ClassNotFoundException e) {
		}
	}
}
