package service.repository;

import service.model.Follow;
import service.model.Ingredient;
import service.model.Recipe;
import service.model.User;

import java.util.ArrayList;
import java.util.List;

public class DataStore {
    private List<User> users = new ArrayList<>();
    private List<Recipe> recipes = new ArrayList<>();
    private List<Follow> follows = new ArrayList<>(); // can be added in User

    // singleton pattern
    private static final DataStore INSTANCE = new DataStore();
    public static DataStore getInstance() {
        return INSTANCE;
    }

    private DataStore() {
        generateDummyData();

    }

    public List<User> getUsers(){
        return users;
    }

    public User getUser(int id){
        for (User user: users) {
            if(user.getId() == id) {
                return user;
            }
        }

        return null;
    }

    public boolean addUser(User user){
        if(isUserOnFile(user) != null){ // user with this email already exists
            return false;
        }
        else {
            users.add(user);
            return true;
        }
    }

    public boolean updateUser(int id, User user){
        for (User u: users){
            if(u.getId() == id){
                //System.out.println(user.getId() + " = " + id);
                int index = users.indexOf(u); // Get index of user

                // Use same id
                user.setId(id);
                User.decreaseIdSeeder();

                users.set(index, user); // update user

                return true;
            }
        }

        return false;
    }

    public boolean deleteUser(int id){
        for (User user: users){
            if(user.getId() == id){
                users.remove(user);
                return true;
            }
        }

        return false;
    }

    public User isUserOnFile(User user){
        for (User u: users) {
            if(u.getEmail().equals(user.getEmail())){
                return u;
            }
        }

        return null;
    }



    // Recipe
    public boolean addRecipe(Recipe recipe){
        recipes.add(recipe);
        return true;
    }

    public List<Recipe> getRecipes(){
        return this.recipes;
    }

    public Recipe getRecipe(int id){
        for(Recipe recipe: recipes){
            if(recipe.getId() == id){
                return recipe;
            }
        }

        return null;
    }

    public boolean updateRecipe(int id, Recipe recipe){
        Recipe foundRecipe = getRecipe(id);

        if(foundRecipe != null){
            int index = recipes.indexOf(foundRecipe);

            recipe.setId(id);
            recipes.set(index, recipe);

            Recipe.decreaseIdSeeder();

            return true;
        }

        return false;
    }

    public boolean deleteRecipe(int id){
        Recipe recipe = getRecipe(id);

        if(recipe != null){
            recipes.remove(recipe);
            return true;
        }

        return false;
    }

    public List<Recipe> getRecipesBy(String ingredient){
        List<Recipe> foundRecipes = new ArrayList<>();
        for (Recipe recipe: recipes){
            for (Ingredient i: recipe.getIngredients()){
                if(i.getName().equals(ingredient)){
                    foundRecipes.add(recipe);
                }
            }
        }

        return foundRecipes;
    }

    public List<Recipe> getUserRecipes(int id){
        List<Recipe> foundRecipes = new ArrayList<>();
        for (Recipe recipe: recipes){
            if(recipe.getUserId() == id){
                foundRecipes.add(recipe);
            }
        }

        return foundRecipes;
    }


    /*------------------------------------------------------------------------Follow-----------------------------------------------------------------------------*/
    public void follow(Follow follow) {
        // check if already followed
        if(!isFollowOnFile(follow)) {
            follows.add(follow);
        }
    }

    public void unFollow(Follow follow) {
        if(follows.contains(follow)) {
            follows.remove(follow);
        }
    }

    public boolean isFollowOnFile(Follow follow) {
        for (Follow followItem: follows) {
            if(followItem.getFollowee().equals(follow.getFollowee()) && followItem.getFollower().equals(follow.getFollower())) {
                return true;
            }
        }

        return false;
    }





    /* Dummy data */
    private void generateDummyData() {
        User user1 = new User("Rawan", "rawan@gmail.com", "1234");
        User user2 = new User("Ranim", "ranim@gmail.com", "12345");
        User user3 = new User("Robin", "robin@gmail.com", "123456");
        User user4 = new User("anas", "anas@gmail.com", "1234567");
        this.users.add(user1);
        this.users.add(user2);
        this.users.add(user3);
        this.users.add(user4);

        List<Ingredient> recipe1Ingredients = new ArrayList<Ingredient>();
        recipe1Ingredients.add(new Ingredient("mayonnaise", 1));
        recipe1Ingredients.add(new Ingredient("egg", 1));
        recipe1Ingredients.add(new Ingredient("Dijon mustard", 2));
        recipe1Ingredients.add(new Ingredient("Hot sauce", 1));

        List<Ingredient> recipe2Ingredients = new ArrayList<Ingredient>();
        recipe2Ingredients.add(new Ingredient("zucchini", 10));
        recipe2Ingredients.add(new Ingredient("tomato paste", 2));
        recipe2Ingredients.add(new Ingredient(" long-grain rice", 1));
        recipe2Ingredients.add(new Ingredient("onion", 1/2));

        List<Ingredient> recipe3Ingredients = new ArrayList<Ingredient>();
        recipe3Ingredients.add(new Ingredient("minced garlic", 2));
        recipe3Ingredients.add(new Ingredient("brown sugar", 2));
        recipe3Ingredients.add(new Ingredient("soy sauce", 5));
        recipe3Ingredients.add(new Ingredient("onion", 1/4));
        recipe3Ingredients.add(new Ingredient("sesame oil", 2));
        recipe3Ingredients.add(new Ingredient("sesame seeds", 1));

        List<Ingredient> recipe4Ingredients = new ArrayList<Ingredient>();
        recipe4Ingredients.add(new Ingredient("ricotta cheese", 2));
        recipe4Ingredients.add(new Ingredient("egg", 1));
        recipe4Ingredients.add(new Ingredient("Italian seasoning", 2));
        recipe4Ingredients.add(new Ingredient("sausage", 1));
        recipe4Ingredients.add(new Ingredient("jar marinara sauce", 2));


        Recipe recipe1 = new Recipe("Crab Cakes", "img.png", "Step 1\n" +
                "In a medium bowl, whisk together egg, mayonnaise, lemon juice, red pepper flakes, tarragon, and scallions. Gently stir in crabmeat, being careful not to break up meat. Gradually mix in cracker crumbs, adding until desired consistency is achieved.\n" +
                "\n" +
                " Step 2\n" +
                "Heat butter in a skillet over medium heat. Form crab mixture into 4 patties. Place patties in skillet, and cook until golden brown, about 5 to 6 minutes on each side.", 2, recipe1Ingredients);
        Recipe recipe2 = new Recipe("Stuffed zucchini (kousa mahshi)", "img.png", "Cut off the zucchini " +
                "stalks, then slice off the dried tips at the opposite ends without removing too much flesh. " +
                "Carefully hollow out the zucchini from the stalk end by pushing and turning a manakra into the flesh. " +
                "The tool will remove thin fingers of flesh at a time; keep hollowing until you have a generous cavity. " +
                "(Reserve the zucchini flesh for another purpose, such as an omelette.) Fill a bowl with water and add 1 tsp salt. " +
                "Wash the zucchini in the salted water (this helps to keep them firm when cooking), then drain.\n" +
                "\n" +
                "Fill each zucchini with the stuffing, leaving 1 cm free at the top to allow the filling to expand. " +
                "It’s easiest to fill the zucchini by hand, tapping them on the bench every now and then to settle the stuffing down. " +
                "If you have any leftover stuffing, shape it into meatballs.\n" +
                "\n" +
                "Fill a large saucepan with water and add 2 tbsp salt and the tomato paste. Add the stuffed zucchini and any meatballs and bring to the boil. Simmer over low heat for about 1 hour, allowing the sauce to reduce. " +
                "Serve the stuffed zucchini with a little of the sauce and a dollop of yoghurt.", 2, recipe2Ingredients);
        Recipe recipe3 = new Recipe("Skillet Chicken Bulgogi", "img.png", "Step 1\n" +
                "Whisk onion, soy sauce, brown sugar, garlic, sesame oil, sesame seeds, cayenne pepper, salt, and black pepper together in a bowl until marinade is smooth.\n" +
                "\n" +
                " Step 2\n" +
                "Cook and stir chicken and marinade together in a large skillet over medium-high heat until chicken is cooked through, about 15 minutes.", 3, recipe3Ingredients);
        Recipe recipe4 = new Recipe("Lasagna Flatbread\n", "img.png", "Step 1\n" +
                "Preheat oven to 375 degrees F (190 degrees C).\n" +
                "\n" +
                " Step 2\n" +
                "Combine ricotta cheese, 1/2 of the mozzarella cheese, Parmesan cheese, egg, and Italian seasoning in a bowl.\n" +
                "\n" +
                " Step 3\n" +
                "Cook sausage in a skillet over medium heat until no longer pink, 5 to 10 minutes; drain. Stir in marinara sauce.\n" +
                "\n" +
                " Step 4\n" +
                "Spread 1/6 of the cheese mixture evenly on each flatbread; cover with sausage mixture. Top with remaining mozzarella cheese.\n" +
                "\n" +
                " Step 5\n" +
                "Bake in the preheated oven until cheese is melted and bubbly, 10 to 15 minutes.", 1, recipe4Ingredients);

        recipes.add(recipe1);
        recipes.add(recipe2);
        recipes.add(recipe3);
        recipes.add(recipe4);
    }

}