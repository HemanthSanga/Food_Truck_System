import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;

class searchByNameTest {

	@Test
	void test() {
		searchByName test = new searchByName();
		try {
			int outTruckId = test.searchName();
			assertEquals(1, outTruckId);
		} catch (SQLException e) {
		} catch (ClassNotFoundException e) {
		}
	}
}
