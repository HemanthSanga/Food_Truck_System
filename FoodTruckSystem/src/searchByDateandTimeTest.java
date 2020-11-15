import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;

class searchByDateandTimeTest {

	@Test
	void test() {
		searchByDateAndTime test = new searchByDateAndTime();
		try {
			int outTruckId = test.searchTime();
			assertEquals(1, outTruckId);
		} catch (SQLException e) {
		} catch (ClassNotFoundException e) {
		}
	}

}
