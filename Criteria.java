public enum Criteria {

    CATEGORY, TYPE, DWARF, DECIDUOUS, TRAINING_SYSTEM, RECOMMENDED_POLLINATORS, POT_SIZE, POT_SIZE_TO_PRICE;

    /**
     * @return a prettified version of the relevant enum constant
     */
        public String toString() {
            return switch (this) {
                case CATEGORY-> "Category";
                case TYPE -> "Type";
                case DWARF -> "Dwarf";
                case DECIDUOUS -> "Deciduous";
                case TRAINING_SYSTEM -> "Training System";
                case RECOMMENDED_POLLINATORS -> "Recommended Pollinators";
                case POT_SIZE-> "Pot Sizes";
                case POT_SIZE_TO_PRICE -> "Pot Size to Price Mapping";
            };
        }
}
