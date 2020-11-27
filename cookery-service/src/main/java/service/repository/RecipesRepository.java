package service.repository;

import service.model.DTO.RecipeDTO;
import service.model.Ingredient;
import service.model.Recipe;
import service.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecipesRepository extends JDBCRepository {
    public Recipe getRecipe(int id) throws CookeryDatabaseException {
        Connection connection = super.getDatabaseConnection();

        String sql = "SELECT * FROM recipe WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if(!resultSet.next()) {
                connection.close();
                throw new CookeryDatabaseException("User with user id " + id + " cannot be found");
            }
            else {
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                String image = resultSet.getString("image");
                int userId = resultSet.getInt("user_id");

                connection.close();

                // GET INGREDIENTS
                List<Ingredient> ingredients = getIngredients(id);

                return new Recipe(id, name, image, description, ingredients, userId);
            }
        }
        catch (SQLException throwable) {
            throw new CookeryDatabaseException("Cannot read recipe from the database", throwable);
        }
    }

    public List<Recipe> getRecipes() throws CookeryDatabaseException {
        List<Recipe> recipes = new ArrayList<>();

        Connection connection = super.getDatabaseConnection();
        String  sql = "SELECT `recipe`.*, `ingredient`.`id` AS `ingredient_id`, `ingredient`.`ingredient`, `ingredient`.`amount` " +
                "FROM `recipe` " +
                "LEFT JOIN `ingredient` ON `ingredient`.`recipe_id` = `recipe`.`id`" +
                "ORDER BY recipe.id";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();

            int lastId = -1;
            List<Ingredient> ingredients = new ArrayList<>();
            String name = null;
            String description = null;
            String image = null;
            int userId = -1;
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

                    ingredients = new ArrayList<>();
                }

                // Gather ingredients
                int ingredientId = resultSet.getInt("ingredient_id");
                String ingredientName = resultSet.getString("ingredient");
                int ingredientAmount = resultSet.getInt("amount");

                Ingredient ingredient = new Ingredient(ingredientId, ingredientName, ingredientAmount);

                // Add ingredient to recipes
                if(recipe != null) {
                    recipe.addIngredient(ingredient);
                }
            }

            connection.commit();
            connection.close();
        }
        catch (SQLException throwable) {
            throw new CookeryDatabaseException("Cannot read users from the database.", throwable);
        }
        return recipes;
    }

    public List<Recipe> getRecipes(int userId) throws CookeryDatabaseException {
        List<Recipe> recipes = new ArrayList<>();

        Connection connection = super.getDatabaseConnection();
        String sql = "SELECT * from recipe WHERE user_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);

            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                String image = resultSet.getString("image");

                // GET INGREDIENTS
                List<Ingredient> ingredients = getIngredients(id);

                Recipe recipe = new Recipe(id, name, image, description, ingredients, userId);
                recipes.add(recipe);
            }

            connection.commit();
            connection.close();
        }
        catch (SQLException throwable) {
            throw new CookeryDatabaseException("Cannot read users from the database.", throwable);
        }
        return recipes;

    }

    // int id, String name, String image, User user, boolean isFavourite
    public List<RecipeDTO> getRecipesDTO(int userId) throws CookeryDatabaseException {
        List<RecipeDTO> recipes = new ArrayList<>();

        Connection connection = super.getDatabaseConnection();
        String sql = "SELECT recipe.id AS recipeId, recipe.name AS recipeName, recipe.image AS recipeImage, " +
                "user.id AS userId, user.name AS userName, " +
                "ufr.id " +
                "FROM recipe " +
                "LEFT JOIN USER ON recipe.user_id = user.id " +
                "LEFT JOIN user_favourite_recipe ufr ON recipe.id = ufr.recipe_id ";
        if(userId >= 0) {
            sql += "AND ufr.user_id = ?";
        }

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            if(userId >= 0) {
                statement.setInt(1, userId);
            }
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {
                int id = resultSet.getInt("recipeId");
                String name = resultSet.getString("recipeName");
                String image = resultSet.getString("recipeImage");
                String userName = resultSet.getString("userName");
                int favouriteId = resultSet.getInt("id");

                boolean isFavourite = false;
                if(favouriteId > 0) {
                    isFavourite = true;
                }

                User user = new User(userId, userName);

                RecipeDTO recipe = new RecipeDTO(id, name, image, user, favouriteId, isFavourite);
                recipes.add(recipe);
            }

            connection.commit();
            connection.close();
        }
        catch (SQLException throwable) {
            throw new CookeryDatabaseException("Cannot read users from the database.", throwable);
        }
        return recipes;

    }

    public List<RecipeDTO> getFavouritesDTO(int userId) throws CookeryDatabaseException {
        List<RecipeDTO> recipes = new ArrayList<>();

        Connection connection = super.getDatabaseConnection();
        String sql = "SELECT recipe.id AS recipeId, recipe.name AS recipeName, recipe.image AS recipeImage, " +
                "user.id AS userId, user.name AS userName, " +
                "ufr.id " +
                "FROM recipe " +
                "INNER JOIN USER ON recipe.user_id = user.id " +
                "INNER JOIN user_favourite_recipe ufr ON recipe.id = ufr.recipe_id ";
        if(userId >= 0) {
            sql += "AND ufr.user_id = ?";
        }

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            if(userId >= 0) {
                statement.setInt(1, userId);
            }
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {
                int id = resultSet.getInt("recipeId");
                String name = resultSet.getString("recipeName");
                String image = resultSet.getString("recipeImage");
                String userName = resultSet.getString("userName");
                int favouriteId = resultSet.getInt("id");

                boolean isFavourite = false;
                if(favouriteId > 0) {
                    isFavourite = true;
                }

                User user = new User(userId, userName);

                RecipeDTO recipe = new RecipeDTO(id, name, image, user, favouriteId, isFavourite);
                recipes.add(recipe);
            }

            connection.commit();
            connection.close();
        }
        catch (SQLException throwable) {
            throw new CookeryDatabaseException("Cannot read users from the database.", throwable);
        }
        return recipes;

    }

    public List<Recipe> getRecipes(String ingredient) throws CookeryDatabaseException {
        List<Recipe> recipes = new ArrayList<>();

        Connection connection = super.getDatabaseConnection();

        String sql = "SELECT * FROM ingredient WHERE ingredinet = ?";

        try  {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, ingredient);

            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {
                int recipeId = resultSet.getInt("recipe_id");

                Recipe recipe = getRecipe(recipeId);

                recipes.add(recipe);
            }

            connection.commit();
            connection.close();
        }
        catch (SQLException throwable) {
            throw new CookeryDatabaseException("Cannot read users from the database.", throwable);
        }
        return recipes;

    }

//    public List<RecipeDTO> getRecipesDTO(String ingredient) throws CookeryDatabaseException {
//        List<Recipe> recipes = new ArrayList<>();
//
//        Connection connection = super.getDatabaseConnection();
//
//        String sql = "SELECT * FROM ingredient WHERE ingredinet = ?";
//
//        try {
//            PreparedStatement statement = connection.prepareStatement(sql);
//            statement.setString(1, ingredient);
//
//            ResultSet resultSet = statement.executeQuery();
//
//            while(resultSet.next()) {
//                int recipeId = resultSet.getInt("recipe_id");
//
//                Recipe recipe = getRecipe(recipeId);
//
//                recipes.add(recipe);
//            }
//
//            connection.commit();
//            connection.close();
//        }
//        catch (SQLException throwable) {
//            throw new CookeryDatabaseException("Cannot read users from the database.", throwable);
//        }
//        return recipes;
//
//    }

    public boolean updateRecipe(int id, Recipe recipe) throws CookeryDatabaseException, SQLException {
        Connection connection = super.getDatabaseConnection();


        //         java.util.zip.ZipFile zf =
        //             new java.util.zip.ZipFile(zipFileName);
        //        java.io.BufferedWriter writer =
        PreparedStatement stmt = null;

        try (PreparedStatement createStmt = connection.prepareStatement("INSERT INTO ingredient (ingredient, amount, recipe_id) VALUES (?, ?, ?)");
            PreparedStatement updateStmt = connection.prepareStatement("UPDATE ingredient SET ingredient = ?, amount = ? WHERE id = ? && recipe_id = ?");
            PreparedStatement deleteStmt = connection.prepareStatement("DELETE FROM ingredient WHERE id = ? AND recipe_id = ?");) {
            if(recipe != null) {
                // Update recipe
                stmt = connection.prepareStatement("UPDATE recipe SET name = ?, description = ?, image = ? WHERE id = ?");

                stmt.setString(1, recipe.getName());
                stmt.setString(2, recipe.getDescription());
                stmt.setString(3, recipe.getImage());
                stmt.setInt(4, id);

                stmt.executeUpdate();

                // Compare ingredients
                List<Ingredient> ingredients = recipe.getIngredients();
                List<Ingredient> DBIngredients = getIngredients(id);

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
                        System.out.println("DbIngredient Id " + DBIngredient.getId());
                        // Delete ingredient
                        deleteStmt.setInt(1, DBIngredient.getId());
                        deleteStmt.setInt(2, id);

                        System.out.println("Trying to delete " + DBIngredient.getId());

                        deleteStmt.addBatch();
                    }
                }


                for (int i = 0; i < ingredients.size(); i++) {
                    String sql = "SELECT * FROM ingredient WHERE id = ? AND recipe_id = ?";

                    stmt = connection.prepareStatement(sql);
                    stmt.setInt(1, ingredients.get(i).getId());
                    stmt.setInt(2, recipe.getId());

                    ResultSet resultSet = stmt.executeQuery();

                    // new ingredient
                    if(!resultSet.next()) {
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

            connection.close();
            return false;
        }
        catch (SQLException throwable) {
            throw new CookeryDatabaseException("Cannot read users from the database.", throwable);
        }
        finally {
            if(!stmt.isClosed()) {
                stmt.close();
            }
            if(!connection.isClosed()) {
                connection.close();
            }
        }
    }


    public boolean deleteRecipe(int id) throws CookeryDatabaseException, SQLException {
        Connection connection = super.getDatabaseConnection();

        PreparedStatement statement = null;
        try {
            String sql = "DELETE FROM ingredient WHERE recipe_id = ?";

            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            statement.executeUpdate();


            sql = "DELETE FROM recipe WHERE id = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            statement.executeUpdate();

            connection.commit();
            connection.close();

            return true;
        }
        catch (SQLException throwable) {
            throw new CookeryDatabaseException("Cannot read users from the database.", throwable);
        }
        finally {
            if(statement != null) {
                statement.close();
            }
            if(connection != null) {
                connection.close();
            }
        }

    }

    public void createRecipe(Recipe recipe) throws CookeryDatabaseException, SQLException {
        Connection connection = super.getDatabaseConnection();

        String sql = "INSERT INTO recipe (name, description, image, user_id) VALUES (?, ?, ?, ?)";

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
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

                preparedStatement = connection.prepareStatement("INSERT INTO ingredient (ingredient, amount, recipe_id) VALUES (?, ?, ?)");

                for(Ingredient ingredient: ingredients) {
                    preparedStatement.setString(1, ingredient.getIngredient());
                    preparedStatement.setInt(2, ingredient.getAmount());
                    preparedStatement.setInt(3, recipeId);

                    preparedStatement.addBatch();
                    preparedStatement.clearParameters();
                }

                preparedStatement.executeBatch();

                connection.commit();
                preparedStatement.close();
                connection.close();
            }
            else {
                throw new CookeryDatabaseException("Cannot get the id of the new recipe.");
            }

        }
        catch (SQLException throwable) {
            throw new CookeryDatabaseException("Cannot create new recipe.", throwable);
        }
        catch (Exception ex) {
            System.out.println("Exception " + ex);
        }
        finally {
            if(!preparedStatement.isClosed()) {
                preparedStatement.close();
            }
            if(!connection.isClosed()) {
                connection.close();
            }
        }
    }


    private List<Ingredient> getIngredients(int recipeId) throws CookeryDatabaseException {
        Connection connection = super.getDatabaseConnection();

        String sql = "SELECT * FROM ingredient WHERE recipe_id = ?";

        List<Ingredient> ingredients = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, recipeId);
            ResultSet resultSet = statement.executeQuery();


            while(resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("ingredient");
                int amount = resultSet.getInt("amount");

                Ingredient ingredient = new Ingredient(id, name, amount);
                ingredients.add(ingredient);
            }

            connection.commit();
            connection.close();

        }
        catch (SQLException throwable) {
            throw new CookeryDatabaseException("Cannot read recipe from the database", throwable);
        }
        return ingredients;
    }
}
