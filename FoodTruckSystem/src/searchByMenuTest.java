import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;

class searchByMenuTest {

	@Test
	void test() {
		searchByMenu test = new searchByMenu();
		try {
			int outTruckId = test.searchMenu();
			assertEquals(1, outTruckId);
		} catch (SQLException e) {
		} catch (ClassNotFoundException e) {
		}
	}

}
