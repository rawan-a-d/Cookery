package service.model;

public class Follow {
	private int id;
	private static int idSeeder = 0;
	private User follower;
	private User followee;

	public Follow() {
		this.id = idSeeder;
		idSeeder++;
	}

	public Follow(User follower, User followee) {
		this.id = idSeeder;
		idSeeder++;
		this.follower = follower;
		this.followee = followee;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getFollower() {
		return follower;
	}

	public void setFollower(User follower) {
		this.follower = follower;
	}

	public User getFollowee() {
		return followee;
	}

	public void setFollowee(User followee) {
		this.followee = followee;
	}

	public static void decreaseIdSeeder() {
		idSeeder--;
	}
}
