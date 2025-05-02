import java.text.DecimalFormat;
import java.util.Map;

/**
 * @param productCode     the plant's unique identified number - unique alpha numeric code
 * @param productName     the Plant's name
 * @param description     the Plant's description
 * @param FruitingPlant    a Plant's 'dream' properties
 */
public record Plant(String productCode, String productName, String description, FruitingPlant fruitingPlant) {

    /**
     * toString to return a description of a Plant, including all its unique and generic features
     * @return String
     */
    public String toString(Map<Criteria,Object> plantCriteria) {
        DecimalFormat df = new DecimalFormat("0.00");
        return  this.productName() + " (" + this.productCode + " ) "+ this.fruitingPlant().getFruitingPlantDescription(plantCriteria);
    }

}
