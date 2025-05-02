import java.util.*;
import java.util.stream.Collectors;

public class FruitingPlant {
    private final Map<Criteria,Object> plantCriteria;
    private double minPrice;
    private double maxPrice;


    /**
     * constructor used to create user's dream plant (not real plant)
     * @param plantCriteria a mapping of criteria to values, e.g. category: Pome
     * @param minPrice the lowest adoption fee the user is interested in
     * @param maxPrice the highest adoption fee the user is willing to pay
     */
    public FruitingPlant(Map<Criteria,Object> plantCriteria,double minPrice, double maxPrice) {
        this.plantCriteria = new HashMap<>(plantCriteria);
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }
    /**
     * constructor used to create real plant's dream plant features
     * @param plantCriteria a mapping of criteria to values, e.g. category: vine
     */
    public FruitingPlant(Map<Criteria,Object> plantCriteria) {

        this.plantCriteria = new HashMap<>(plantCriteria);
    }


    /**
     *  access all the key os the criteria
     * @return all plant criteria
     */
    public Map<Criteria, Object> getPlantCriteria() {
        return plantCriteria;
    }

    /**
     * access all the key-value pairs, e.g. breed: jack russell, sex: female, etc.
     * @return the entire mapping of criteria and their values
     */
    public Map<Criteria, Object> getAllPlantCriteriaAndValues() {
        return new HashMap<>(plantCriteria);
    }


    /**
     * @param key a criteria, e.g. category
     * @return the value at a specified criteria, e.g. vine
     */
    public Object getValueAtCriteria(Criteria key){
        return getAllPlantCriteriaAndValues().get(key);
    }


    /**
     *
     * @return minPrice  the minimum price customer is willing to pay
     */
    public double getMinPrice() {
        return minPrice;
    }

    /**
     * @param minPrice  the minimum price customer is willing to pay
     */
    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    /**
     @return maxnPrice  the maximum price customer is willing to pay
     */
    public double getMaxPrice() {
        return maxPrice;
    }

    /**
     * @param maxPrice  the maximum price customer is willing to pay
     */
    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }

    /**
     * method to return a description of generic Fruiting Plant features
     * @return a formatted String description of the fruiting plant's dream features
     */
    public String getFruitingPlantDescription(Map<Criteria,Object> criteria){
        StringBuilder description= new StringBuilder();
        for(Criteria key: criteria.keySet()) description.append("\n").append(key).append(": ").append(getValueAtCriteria(key));
        return description.toString();
    }


    /**
     * method to compare two DreamPet objects against each other for compatibility
     * @param plantCriteria an imaginary fruiting plant representing the user's criteria
     * @return true if matches, false if not
     */
    public boolean compareFruitingPlant(FruitingPlant plantCriteria) {
        for (Criteria key : plantCriteria.getAllPlantCriteriaAndValues().keySet()) {
            if (key == Criteria.POT_SIZE_TO_PRICE) continue;
            Object thisValue = getValueAtCriteria(key);
            Object otherValue = plantCriteria.getValueAtCriteria(key);


            if (key == Criteria.TYPE) {

                if (otherValue.equals("Skip")) {
                    continue;
                }

                    if (!thisValue.equals(otherValue)) return false;

            }

            if (key == Criteria.TRAINING_SYSTEM){
                if (otherValue.equals("Skip")) {
                    continue;
                }

                    if (!thisValue.equals(otherValue)) return false;

            }


//            if (key == Criteria.DECIDUOUS && otherValue.equals(Choice.NA)) continue;

            if (key == Criteria.DWARF) {
                if (otherValue.equals(Choice.NA)) continue;
                if (!thisValue.equals(otherValue)) return false;
            }

            if (key == Criteria.DECIDUOUS) {

                if (otherValue.equals(Choice.NA)) continue;


                if (!thisValue.equals(otherValue)) return false;
            }

            if (key == Criteria.RECOMMENDED_POLLINATORS) {

                Set<String> thisPollinators = (Set<String>) thisValue;
                Set<String> otherPollinators = (Set<String>) otherValue;
                if (otherPollinators.contains("Skip") || otherPollinators.isEmpty()) {
                    continue;
                }

                thisPollinators = thisPollinators.stream().map(String::trim).collect(Collectors.toSet());

                otherPollinators = otherPollinators.stream().map(String::trim).collect(Collectors.toSet());


                if (Collections.disjoint(thisPollinators, otherPollinators)) {
                    return false;
                }
                continue;
            }

            // Handle POT_SIZE
            if (key == Criteria.POT_SIZE) {

                List<Integer> thisPotSizes = (List<Integer>) thisValue;
                if(thisPotSizes.contains("Skip")) continue;
                Integer otherPotSize = (Integer) otherValue;
                if (!thisPotSizes.contains(otherPotSize)) {
                    return false;
                }
                continue;
            }

            if (!thisValue.equals(otherValue)) {
                return false;
            }
        }
        return true;
    }

}