package service.repository;

import service.controller.RecipesController;
import service.model.DTO.RecipeDTO;
import service.model.Ingredient;
import service.model.Recipe;
import service.model.User;

import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class RecipesRepository {
    private final static Logger LOGGER = Logger.getLogger(RecipesController.class.getName());

    //    @Inject
    JDBCRepository jdbcRepository;

    public RecipesRepository() {
        this.jdbcRepository = new JDBCRepository();
    }

    public Recipe getRecipe(int id) throws CookeryDatabaseException, URISyntaxException {

        String  sql = "SELECT `recipe`.*, `ingredient`.`id` AS `ingredient_id`, `ingredient`.`ingredient`, `ingredient`.`amount` " +
                                "FROM `recipe` " +
                                "LEFT JOIN `ingredient` ON `ingredient`.`recipe_id` = `recipe`.`id` " +
                                "WHERE recipe.id = ?";

        Recipe recipe = null;

        try (Connection connection = jdbcRepository.getDatabaseConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if(!resultSet.isBeforeFirst()) {
                connection.close();
                throw new CookeryDatabaseException("Recipe with recipe id " + id + " cannot be found");
            }
            else {
                while(resultSet.next()) {

                    if(recipe == null) {
                        String name = resultSet.getString("name");
                        String description = resultSet.getString("description");
                        String image = resultSet.getString("image");
                        int userId = resultSet.getInt("user_id");

                        recipe = new Recipe(id, name, image, description, userId);
                    }

                    // Gather ingredients
                    int ingredientId = resultSet.getInt("ingredient_id");
                    String ingredientName = resultSet.getString("ingredient");
                    int ingredientAmount = resultSet.getInt("amount");

                    Ingredient ingredient = new Ingredient(ingredientId, ingredientName, ingredientAmount);
                    recipe.addIngredient(ingredient);
                }

                connection.commit();

                return recipe;
            }
        }
        catch (SQLException throwable) {
            throw new CookeryDatabaseException("Cannot read recipe from the database", throwable);
        }
    }

    public List<Recipe> getRecipes() throws CookeryDatabaseException, URISyntaxException {
        List<Recipe> recipes = new ArrayList<>();

        String  sql = "SELECT `recipe`.*, `ingredient`.`id` AS `ingredient_id`, `ingredient`.`ingredient`, `ingredient`.`amount` " +
                "FROM `recipe` " +
                "LEFT JOIN `ingredient` ON `ingredient`.`recipe_id` = `recipe`.`id`" +
                "ORDER BY recipe.id";

        try (Connection connection = jdbcRepository.getDatabaseConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();

            int lastId = -1;
            String name;
            String description;
            String image;
            int userId;
            Recipe recipe = null;

            while(resultSet.next()) {
                int id = resultSet.getInt("id");

                if(id != lastId) { // new recipe
                    // create new recipe
                    name = resultSet.getString("name");
                    description = resultSet.getString("description");
                    image = resultSet.getString("image");
                    userId = resultSet.getInt("user_id");

                    recipe = new Recipe(id, name, image, description, userId);

                    recipes.add(recipe);

                    lastId = id;
                }

                // Gather ingredients
                int ingredientId = resultSet.getInt("ingredient_id");
                String ingredientName = resultSet.getString("ingredient");
                int ingredientAmount = resultSet.getInt("amount");

                Ingredient ingredient = null;
                if(ingredientName != null && ingredientId != 0) {
                    ingredient = new Ingredient(ingredientId, ingredientName, ingredientAmount);
                }

                // Add ingredient to recipe
                if(recipe != null && ingredient != null) {
                    recipe.addIngredient(ingredient);
                }
            }

            connection.commit();
        }
        catch (SQLException throwable) {
            throw new CookeryDatabaseException("Cannot read recipes from the database.", throwable);
        }
        return recipes;
    }

    public List<Recipe> getRecipes(int userId) throws CookeryDatabaseException, URISyntaxException {
        List<Recipe> recipes = new ArrayList<>();

        String sql = "SELECT `recipe`.*, `ingredient`.`id` AS `ingredient_id`, `ingredient`.`ingredient`, `ingredient`.`amount` " +
                "from recipe " +
                "LEFT JOIN `ingredient` ON `ingredient`.`recipe_id` = `recipe`.`id` " +
                "WHERE user_id = ? " +
                "ORDER BY recipe.id";

        try (Connection connection = jdbcRepository.getDatabaseConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if(!resultSet.isBeforeFirst()) {
                connection.close();
                throw new CookeryDatabaseException("User with user id " + userId + " cannot be found");
            }
            else {
                int lastId = -1;
                String name;
                String description;
                String image;
                Recipe recipe = null;

                while(resultSet.next()) {
                    int id = resultSet.getInt("id");

                    if(id != lastId) { // new recipe
                        // create new recipe
                        name = resultSet.getString("name");
                        description = resultSet.getString("description");
                        image = resultSet.getString("image");

                        recipe = new Recipe(id, name, image, description, userId);

                        recipes.add(recipe);

                        lastId = id;
                    }

                    // Gather ingredients
                    int ingredientId = resultSet.getInt("ingredient_id");
                    String ingredientName = resultSet.getString("ingredient");
                    int ingredientAmount = resultSet.getInt("amount");

                    Ingredient ingredient = null;
                    if(ingredientName != null && ingredientId != 0) {
                        ingredient = new Ingredient(ingredientId, ingredientName, ingredientAmount);
                    }

                    // Add ingredient to recipe
                    if(recipe != null && ingredient != null) {
                        recipe.addIngredient(ingredient);
                    }
                }
            }

            connection.commit();
        }
        catch (SQLException throwable) {
            throw new CookeryDatabaseException("Cannot read recipes from the database.", throwable);
        }

        return recipes;

    }

    public List<RecipeDTO> getRecipesDTO(int userId) throws CookeryDatabaseException, URISyntaxException {
        List<RecipeDTO> recipes = new ArrayList<>();

        String sql = "SELECT recipe.id AS recipeId, recipe.name AS recipeName, recipe.image AS recipeImage, " +
                "user.id AS userId, user.name AS userName, " +
                "ufr.id " +
                "FROM recipe " +
                "LEFT JOIN USER ON recipe.user_id = user.id " +
                "LEFT JOIN user_favourite_recipe ufr ON recipe.id = ufr.recipe_id " +
                "AND ufr.user_id = ?";

        try (Connection connection = jdbcRepository.getDatabaseConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);

            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {
                int id = resultSet.getInt("recipeId");
                String name = resultSet.getString("recipeName");
                String image = resultSet.getString("recipeImage");
                String userName = resultSet.getString("userName");
                int favouriteId = resultSet.getInt("id");
                int creatorId = resultSet.getInt("userId");

                boolean isFavourite = false;
                if(favouriteId > 0) {
                    isFavourite = true;
                }

                User user = new User(creatorId, userName);

                RecipeDTO recipe = new RecipeDTO(id, name, image, user, favouriteId, isFavourite);
                recipes.add(recipe);
            }

            connection.commit();
        }
        catch (SQLException throwable) {
            throw new CookeryDatabaseException("Cannot read recipes from the database.", throwable);
        }
        return recipes;

    }

    public List<Recipe> getRecipes(String ingredient) throws CookeryDatabaseException, URISyntaxException {
        List<Recipe> recipes = new ArrayList<>();


        String sql = "SELECT ingredient.*, recipe.name, recipe.description, recipe.image, recipe.user_id FROM ingredient " +
                "LEFT JOIN recipe on recipe.id = ingredient.recipe_id " +
                "WHERE ingredient = ?";

        try (Connection connection = jdbcRepository.getDatabaseConnection();
             PreparedStatement statement = connection.prepareStatement(sql))  {
            statement.setString(1, ingredient);

            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                int recipeId = resultSet.getInt("recipe_id");

                String recipeName = resultSet.getString("name");
                String recipeDescription = resultSet.getString("description");
                String recipeImage = resultSet.getString("image");
                int userId = resultSet.getInt("user_id");

                Recipe recipe = new Recipe(recipeId, recipeName, recipeImage, recipeDescription, userId);

                recipes.add(recipe);
            }

            connection.commit();
        }
        catch (SQLException throwable) {
            throw new CookeryDatabaseException("Cannot read recipes from the database.", throwable);
        }
        return recipes;
    }

    public boolean updateRecipe(int id, Recipe recipe) throws CookeryDatabaseException, SQLException {

        try (Connection connection = jdbcRepository.getDatabaseConnection();
             PreparedStatement stmt = connection.prepareStatement("UPDATE recipe SET name = ?, description = ?, image = ? WHERE id = ?");
            PreparedStatement createStmt = connection.prepareStatement("INSERT INTO ingredient (ingredient, amount, recipe_id) VALUES (?, ?, ?)");
            PreparedStatement updateStmt = connection.prepareStatement("UPDATE ingredient SET ingredient = ?, amount = ? WHERE id = ? && recipe_id = ?");
            PreparedStatement deleteStmt = connection.prepareStatement("DELETE FROM ingredient WHERE id = ? AND recipe_id = ?");
            PreparedStatement getIngredients = connection.prepareStatement("SELECT * FROM ingredient WHERE recipe_id = ?");
            PreparedStatement getIngredientsInRecipe = connection.prepareStatement("SELECT * FROM ingredient WHERE id = ? AND recipe_id = ?")) {
            if(recipe != null) {
                // Update recipe

                stmt.setString(1, recipe.getName());
                stmt.setString(2, recipe.getDescription());
                stmt.setString(3, recipe.getImage());
                stmt.setInt(4, id);

                stmt.executeUpdate();

                // Compare ingredients
                List<Ingredient> ingredients = recipe.getIngredients();
                List<Ingredient> DBIngredients = new ArrayList<>();;

                getIngredients.setInt(1, id);
                ResultSet resultSet = getIngredients.executeQuery();

                while(resultSet.next()) {
                    int ingredientId = resultSet.getInt("id");
                    String name = resultSet.getString("ingredient");
                    int amount = resultSet.getInt("amount");

                    Ingredient ingredient = new Ingredient(ingredientId, name, amount);

                    DBIngredients.add(ingredient);
                }

                // **************************************************** delete and update are similar
                // Check if an ingredient is deleted
                boolean isDeleted = true;
                for (Ingredient DBIngredient: DBIngredients) {
                    isDeleted = true;
                    for (Ingredient ingredient: ingredients) {

                        if(DBIngredient.getId() == ingredient.getId()) { // if same -> exists
                            isDeleted = false;

                            break;
                        }
                    }

                    if(isDeleted) {
                        // Delete ingredient
                        deleteStmt.setInt(1, DBIngredient.getId());
                        deleteStmt.setInt(2, id);

                        deleteStmt.addBatch();
                    }
                }


                for (int i = 0; i < ingredients.size(); i++) {
                    getIngredientsInRecipe.setInt(1, ingredients.get(i).getId());
                    getIngredientsInRecipe.setInt(2, recipe.getId());

                    ResultSet resultSetIngredients = getIngredientsInRecipe.executeQuery();

                    // new ingredient
                    if(!resultSetIngredients.next()) {
                        // Create ingredient
                        createStmt.setString(1, ingredients.get(i).getIngredient());
                        createStmt.setInt(2, ingredients.get(i).getAmount());
                        createStmt.setInt(3, recipe.getId());

                        createStmt.addBatch();
                    }
                    // existing ingredient
                    else {
                        // Update ingredient
                        updateStmt.setString(1, ingredients.get(i).getIngredient());
                        updateStmt.setInt(2, ingredients.get(i).getAmount());
                        updateStmt.setInt(3, ingredients.get(i).getId());
                        updateStmt.setInt(4, recipe.getId());

                        updateStmt.addBatch();
                    }
                }

                updateStmt.executeBatch(); //executing the batch
                createStmt.executeBatch(); //executing the batch
                deleteStmt.executeBatch();

                connection.commit();
                connection.close();

                return true;
            }

            return false;
        }
        catch (SQLException | URISyntaxException throwable) {
            throw new CookeryDatabaseException("Cannot update recipe in the database.", throwable);
        }
    }


    public boolean deleteRecipe(int id) throws CookeryDatabaseException, SQLException, URISyntaxException {
        String sql = "DELETE FROM ingredient WHERE recipe_id = ?";

        try (Connection connection = jdbcRepository.getDatabaseConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             PreparedStatement deleteFavourites = connection.prepareStatement("DELETE FROM user_favourite_recipe WHERE recipe_id = ?");
            PreparedStatement deleteRecipeStatement = connection.prepareStatement("DELETE FROM recipe WHERE id = ?")) {

            statement.setInt(1, id);
            statement.executeUpdate();

            deleteFavourites.setInt(1, id);
            deleteFavourites.executeUpdate();

            deleteRecipeStatement.setInt(1, id);
            int affected = deleteRecipeStatement.executeUpdate();

            connection.commit();

            if(affected <= 0) { // Not success
                throw new CookeryDatabaseException("User was not found");
            }

            return true;
        }
        catch (SQLException throwable) {
            throw new CookeryDatabaseException("Cannot delete recipe from the database.", throwable);
        }
    }

    public boolean createRecipe(Recipe recipe) throws CookeryDatabaseException {

        String sql = "INSERT INTO recipe (name, description, image, user_id) VALUES (?, ?, ?, ?)";

        try (Connection connection = jdbcRepository.getDatabaseConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement preparedStatementIngredient = connection.prepareStatement("INSERT INTO ingredient (ingredient, amount, recipe_id) VALUES (?, ?, ?)");
        ) {
            preparedStatement.setString(1, recipe.getName());
            preparedStatement.setString(2, recipe.getDescription());
            preparedStatement.setString(3, recipe.getImage());
            preparedStatement.setInt(4, recipe.getUserId());
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if(resultSet.next()) {
                int recipeId = resultSet.getInt(1);

                connection.commit();

                List<Ingredient> ingredients = recipe.getIngredients();


                for(Ingredient ingredient: ingredients) {
                    preparedStatementIngredient.setString(1, ingredient.getIngredient());
                    preparedStatementIngredient.setInt(2, ingredient.getAmount());
                    preparedStatementIngredient.setInt(3, recipeId);

                    preparedStatementIngredient.addBatch();
                    preparedStatementIngredient.clearParameters();
                }

                preparedStatementIngredient.executeBatch();

                return true;
            }
            else {
                throw new CookeryDatabaseException("Cannot get the id of the new recipe.");
            }

        }
        catch (SQLException throwable) {
            throw new CookeryDatabaseException("Cannot create new recipe.", throwable);
        }
        catch (Exception ex) {
            LOGGER.info(ex.getMessage()); // Compliant
        }
        return false;
    }


    /*------------------------------------------------ Favourites ------------------------------------------------------*/
    public boolean addFavourite(int userId, RecipeDTO favourite) throws CookeryDatabaseException, SQLException, URISyntaxException {
        String sql = "INSERT INTO user_favourite_recipe (user_id, recipe_id) " +
                "SELECT ?, ? Where not exists( SELECT user_id, recipe_id FROM user_favourite_recipe " +
                "WHERE user_id = ? AND recipe_id = ?)";

        try (Connection connection = jdbcRepository.getDatabaseConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setInt(2, favourite.getId());
            statement.setInt(3, userId);
            statement.setInt(4, favourite.getId());
            int affected = statement.executeUpdate();

            if(affected <= 0) {
                throw new CookeryDatabaseException("Cannot add favourite into the database");
            }

            connection.commit();

            return true;
        } catch (SQLException throwable) {
            throw new CookeryDatabaseException("Cannot add favourite into the database", throwable);
        }
    }

    public boolean removeFavourite(int favouriteId) throws CookeryDatabaseException, SQLException, URISyntaxException {
        String sql = "DELETE FROM user_favourite_recipe WHERE id = ?";

        try (Connection connection = jdbcRepository.getDatabaseConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, favouriteId);

            int affected = statement.executeUpdate();

            connection.commit();

            if(affected <= 0) {
                throw new CookeryDatabaseException("Cannot delete favourite from the database");
            }

            return true;

        } catch (SQLException throwable) {
            throw new CookeryDatabaseException("Cannot delete favourite from the database", throwable);
        }
    }


    public List<Recipe> getFavourites(int userId) throws CookeryDatabaseException, URISyntaxException {
        List<Recipe> recipes = new ArrayList<>();

        String sql = "SELECT recipe.id AS recipeId, recipe.name AS recipeName, recipe.description AS recipeDescription, recipe.image AS recipeImage, recipe.user_id AS creator_id, ufr.* " +
                "FROM user_favourite_recipe AS ufr " +
                "LEFT JOIN recipe ON ufr.recipe_id = recipe.id " +
                "WHERE ufr.user_id = ?";

        try (Connection connection = jdbcRepository.getDatabaseConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("recipeId");
                String name = resultSet.getString("recipeName");
                String description = resultSet.getString("recipeDescription");
                String image = resultSet.getString("recipeImage");
                int creator_id = resultSet.getInt("creator_id");

                Recipe recipe = new Recipe(id, name, image, description, creator_id);
                recipes.add(recipe);
            }

        } catch (SQLException throwable) {
            throw new CookeryDatabaseException("Cannot read favourites from the database.", throwable);
        }

        return recipes;
    }


    public List<RecipeDTO> getFavouritesDTO(int userId) throws CookeryDatabaseException, Exception {
        List<RecipeDTO> recipes = new ArrayList<>();

        String sql = "SELECT recipe.id AS recipeId, recipe.name AS recipeName, recipe.image AS recipeImage, " +
                "       user.id AS userId, user.name AS userName, " +
                "       ufr.id " +
                "FROM  user_favourite_recipe AS ufr " +
                "INNER JOIN recipe ON recipe.id = ufr.recipe_id " +
                "INNER JOIN USER ON recipe.user_id = user.id " +
                "AND ufr.user_id = ? " +
                "ORDER BY recipe.id";

        try (Connection connection = jdbcRepository.getDatabaseConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("recipeId");
                String name = resultSet.getString("recipeName");
                String image = resultSet.getString("recipeImage");
                String userName = resultSet.getString("userName");
                int creatorId = resultSet.getInt("userId");

                User user = new User(creatorId, userName);

                RecipeDTO recipe = new RecipeDTO(id, name, image, user);

                recipes.add(recipe);
            }

        } catch (SQLException throwable) {
            throw new CookeryDatabaseException("Cannot read favourites from the database.", throwable);
        }
        catch (Exception ex) {
            throw new Exception("Cannot read favourites from the database.", ex);
        }

        return recipes;
    }
}
