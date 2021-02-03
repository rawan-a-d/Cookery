package service.validators;

import cyclops.control.Validated;
import service.model.Recipe;

public class RecipeValidator {
    public static Validated<Error,String> validName(Recipe recipe){
        if(recipe.getName() == null) { // null
            return Validated.invalid(Error.NULL);
        }
        else if(!recipe.getName().matches("^[a-zA-Z\\s]*$")) { // null
            return Validated.invalid(Error.INVALID_NAME);
        }
        else { // valid
            return Validated.valid("Valid name");
        }
    }

    public static Validated<Error,String> validDescription(Recipe recipe){
        if(recipe.getDescription() == null) { // null
            return Validated.invalid(Error.NULL);
        }
        else { // valid
            return Validated.valid("Valid description");
        }
    }


    public static Validated<Error,String> validIngredients(Recipe recipe){
        // no ingredients
        if(recipe.getIngredients() == null || recipe.getIngredients().size() == 0) { // null
            return Validated.invalid(Error.NULL);
        }

        // duplicate
        int counter = 1;
        for(int i = 0; i < recipe.getIngredients().size(); i++) {
            String ingredient = recipe.getIngredients().get(i).getIngredient();
            for(int j = counter; j < recipe.getIngredients().size(); j++) {
                String ingredient2 = recipe.getIngredients().get(j).getIngredient();

                if(ingredient.equals(ingredient2)) {
                    return Validated.invalid(Error.DUPLICATE);
                }
            }
            counter++;
        }

        return Validated.valid("Valid ingredients");
    }
}
