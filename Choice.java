public enum Choice {
    YES, NO, NA;

    /**
     * @return a prettified version of the relevant enum constant*/
    public String toString() {
       return switch (this) {
            case YES ->"Yes";
            case NO -> "No";
            case NA-> "I don't mind";
        };
    }
}
