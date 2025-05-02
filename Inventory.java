import java.util.*;

public class Inventory {
    private final List<Plant> inventory = new ArrayList<>();

    /**
     * method to add a plant object to the database (inventory)
     * @param plant a Plant object
     */
    public void addItem(Plant plant){
        this.inventory.add(plant);
    }

    /**
     * a method to return a set of all plant pome/vine/ in the dataset (no duplicates)
     * @param category a String representing the category of a plant that customer want
     * @return Set</string> of available category
     */

    public Set<String> getAllTypes( Category category) {
        Set<String> allTypes = new LinkedHashSet<>();
        for (Plant p : inventory) {
            if (category.equals(p.fruitingPlant().getValueAtCriteria(Criteria.CATEGORY)))
                allTypes.add((String) p.fruitingPlant().getValueAtCriteria(Criteria.TYPE));
        }

        allTypes.add("Skip");
        return allTypes;
    }



    public Set<String> getAllTrainingSystem( Category category) {
        Set<String> allTrainingSystems = new LinkedHashSet<>();
        for (Plant p : inventory) {
            if (category.equals(p.fruitingPlant().getValueAtCriteria(Criteria.CATEGORY)))
                allTrainingSystems.add((String) p.fruitingPlant().getValueAtCriteria(Criteria.TRAINING_SYSTEM));
        }

        allTrainingSystems.add("Skip");
        return allTrainingSystems;
    }

    public Set<String> getAllPollinators(Category category, String type){
        Set<String> allPollinators = new LinkedHashSet<>();
        for (Plant p : inventory) {
            if (category.equals(p.fruitingPlant().getValueAtCriteria(Criteria.CATEGORY)) ) {
                Set<String> plantPollinators = (Set<String>) p.fruitingPlant().getValueAtCriteria(Criteria.RECOMMENDED_POLLINATORS);
                allPollinators.addAll(plantPollinators);
            }
        }

        allPollinators.add("Skip");
        return allPollinators;
    }

    public Set<String> getAllPotSize(Category category, String type){
        Set<String> allPotSizes = new LinkedHashSet<>();
        for (Plant p : inventory) {
            if (category.equals(p.fruitingPlant().getValueAtCriteria(Criteria.CATEGORY)))
                if (p.fruitingPlant().getValueAtCriteria(Criteria.POT_SIZE) instanceof List<?>) {
                    for (Object size : (List<?>) p.fruitingPlant().getValueAtCriteria(Criteria.POT_SIZE)) {
                        allPotSizes.add(size.toString());
                    }
                }
        }
        return allPotSizes;
    }

    /**
     * returns a collection of Plant objects that meet all the user's requirements
     * @param plantCriteria a Plant object representing a user's preferred Plant
     * @return a Plant object
     */
    public List<Plant> findMatch(FruitingPlant plantCriteria){
        List<Plant> compatiblePlants = new ArrayList<>();
        for(Plant p: this.inventory){
            System.out.println("the p tag." + p.fruitingPlant().getFruitingPlantDescription(p.fruitingPlant().getAllPlantCriteriaAndValues()));
            System.out.println("User Criteria: " + plantCriteria.getFruitingPlantDescription(plantCriteria.getAllPlantCriteriaAndValues()));

            if(!p.fruitingPlant().compareFruitingPlant(plantCriteria)) continue;
            Integer userPotSize = (Integer) plantCriteria.getValueAtCriteria(Criteria.POT_SIZE);
            Map<Integer, Float> potSizeToPrice = (Map<Integer, Float>) p.fruitingPlant().getValueAtCriteria(Criteria.POT_SIZE_TO_PRICE);
            if (potSizeToPrice.containsKey(userPotSize)) {
                float price = potSizeToPrice.get(userPotSize);
                if (price >= plantCriteria.getMinPrice() && price <= plantCriteria.getMaxPrice()) {
                    compatiblePlants.add(p);
                }
            }
        }
        return compatiblePlants;
    }


}
