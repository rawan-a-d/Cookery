import org.junit.Test;
import service.model.User;
import service.repository.DataStore;
import static org.junit.jupiter.api.Assertions.*;


public class UserTest {
	private final DataStore dataStore = DataStore.getInstance();

	@Test
	public void createUser(){
		User user = new User("Omar", "omar@gmail.com", "1234");

		// create user
		dataStore.addUser(user);

		// retrieve user
		User retrievedUser = dataStore.getUser(user.getId());

		assertSame(user, retrievedUser);
	}

	@Test
	public void getUser(){
		User user = dataStore.getUser(1);

		User expectedUser = new User("Ranim", "ranim@gmail.com", "12345");

		assertEquals(expectedUser.getName(), user.getName());
		assertEquals(expectedUser.getEmail(), user.getEmail());
		assertEquals(expectedUser.getPassword(), user.getPassword());
	}


	@Test
	public void deleteUser(){
		// delete user
		boolean isUserDeleted = dataStore.deleteUser(2);

		// retrieve user
		User user = dataStore.getUser(2);

		assertTrue(isUserDeleted && user == null);
	}

	@Test
	public void updateUser(){

		User updatedUser = new User("Denys", "denys@gmail.com", "1234");

		// delete user
		dataStore.updateUser(2, updatedUser);

		// retrieve user
		User user = dataStore.getUser(2);

		assertSame(updatedUser, user);
	}

	// Check constructor


	// When working with db
	// Create a user -> try to retrieve it
	// Read a user
	// Delete a user -> try to retrieve it
	// Update a user -> try to retrieve it
}
