import org.junit.Test;
import service.model.User;
import service.repository.DataStore;
import static org.junit.jupiter.api.Assertions.*;


public class UserTest {
	@Test
	public void AddUser(){
		DataStore dataStore = new DataStore();

		User user = dataStore.getUser(1);

		User actualUser = new User("Ranim", "ranim@gmail.com", "12345");

		//assertSame(user, actualUser);
		assertEquals(user.getName(), actualUser.getName());
		assertEquals(user.getEmail(), actualUser.getEmail());
		assertEquals(user.getPassword(), actualUser.getPassword());

	}
}
