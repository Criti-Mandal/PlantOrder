
public enum Category {

    POME, VINE, CITRUS, STONE_FRUIT;

    /**
     * @return a prettified version of the relevant enum constant
     */
    public String toString() {
        return switch (this) {
            case POME -> "Pome";
            case VINE -> "Vine";
            case CITRUS -> "Citrus";
            case STONE_FRUIT -> "Stone Fruit";
        };
    }
}
