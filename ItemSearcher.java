import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemSearcher {

    private static final String filePath = "./inventory_v2.txt";
    private static final Icon icon = new ImageIcon("./the_greenie_geek.png");
    private static Inventory inventory;
    private static final String appName = "Greenie Geek";


    /**
     * main method used to allow the user to search Pinkman's database of Plant, and place a request
     *
     * @param args none required
     */
    public static void main(String[] args) {
        inventory = loadInventory(filePath);
        FruitingPlant dreamTree = getCustomerCriteria();
        processSearchResults(dreamTree);
        System.exit(0);
    }

     /**
      * method use process the searchh result and submit the request
      * @param dreamTree the matched plant from the data according to customer criteria
      */
    public static void processSearchResults(FruitingPlant dreamTree) {
        List<Plant> matching = inventory.findMatch(dreamTree);
        if (matching.size() > 0) {
            Map<String, Plant> options = new HashMap<>();
            StringBuilder infoToShow = new StringBuilder("Matches found!! The following trees meet your criteria: \n");
            for (Plant match : matching) {
                infoToShow.append(match.toString(match.fruitingPlant().getAllPlantCriteriaAndValues()));
                infoToShow.append("\n\n");
                options.put(match.productName() + " (" + match.productCode() + ")", match);
            }
            String choice = (String) JOptionPane.showInputDialog(null, infoToShow + "\n\nPlease select which item you'd like to order:", appName, JOptionPane.INFORMATION_MESSAGE, icon, options.keySet().toArray(), "");
            if (choice == null) System.exit(0);
            Plant chosenTree = options.get(choice);
            Customer customer = getContactInfo();
            submitOrder(customer, chosenTree, dreamTree);
            JOptionPane.showMessageDialog(null, "Thank you! Your order has been submitted. Please head to your local Greenie Geek to pay and pick up!", appName, JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Unfortunately none of our trees meet your criteria :(" +
                    "\n\tTo exit, click OK.", appName, JOptionPane.INFORMATION_MESSAGE);
        }
    }


    /**
     * method to get Customer to input name, phone num and email, with appropriate input validation
     *
     * @return a Customer object representing the user of the program
     */
    public static Customer getContactInfo() {
        String name;
        do {
            name = (String) JOptionPane.showInputDialog(null, "Please enter your full name (in format firstname surname): ", appName, JOptionPane.QUESTION_MESSAGE, icon, null, null);
            if (name == null) System.exit(0);
        } while (!isValidFullName(name));
        String phoneNumber;
        do {
            phoneNumber = (String) JOptionPane.showInputDialog(null, "Please enter your phone number (10-digit number in the format 0412345678): ", appName, JOptionPane.QUESTION_MESSAGE, icon, null, null);
            if (phoneNumber == null) System.exit(0);
        }
        while (!isValidPhoneNumber(phoneNumber));
        return new Customer(name, phoneNumber);
    }

    /**
     * a very simple regex for full name in Firstname Surname format
     *
     * @param fullName the candidate full name entered by the user
     * @return true if name matches regex/false if not
     */
    public static boolean isValidFullName(String fullName) {
        String regex = "^[A-Z][a-z]+\\s[A-Z][a-zA-Z]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(fullName);
        return matcher.matches();
    }

    /**
     * a regex matcher that ensures that the user's entry starts with a 0 and is followed by 9 digits
     *
     * @param phoneNumber the candidate phone number entered by the user
     * @return true if phone number matches regex/false if not
     */
    public static boolean isValidPhoneNumber(String phoneNumber) {
        Pattern pattern = Pattern.compile("^0\\d{9}$");
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    public static void submitOrder(Customer customer, Plant citrusTree, FruitingPlant dreamCitrusTree) {
        String filePath = customer.name().replace(" ", "_") + "_" + citrusTree.productCode() + ".txt";
        Path path = Path.of(filePath);
        String lineToWrite = "Order details:" +
                "\n\tName: " + customer.name() + " (" + customer.phoneNumber() + ")" +
                "\n\tItem: " + citrusTree.productName() + " (" + citrusTree.productCode() + ")" +
                "\n\tPot size (inch): " + dreamCitrusTree.getValueAtCriteria(Criteria.POT_SIZE);

        try {
            Files.writeString(path, lineToWrite);
        } catch (IOException io) {
            System.out.println("Order could not be placed. \nError message: " + io.getMessage());
            System.exit(0);
        }
    }

    /**
     * the method to load the data from file to the fruitingplant
     * @param filePath
     * @return inventory the data from the file
     */

    public static Inventory loadInventory(String filePath) {
        Inventory inventory = new Inventory();
        Path path = Path.of(filePath);
        List<String> fileContents = null;
        try {
            fileContents = Files.readAllLines(path);
        } catch (IOException io) {
            System.out.println("File could not be found");
            System.exit(0);
        }

        for (int i = 1; i < fileContents.size(); i++) {
            String[] info = fileContents.get(i).split("\\[");


            String[] singularInfo = info[0].split(",");

            String pollinatorsRaw = info[1].replace("]", "");
            String pricesRaw = info[2].replace("],", "");
            String potSizesRaw = info[3].replace("],", "");
            String description = info[4].replace("]", "");

            Category category = null;
            try {
                category = Category.valueOf(singularInfo[0].trim().toUpperCase().replace(" ", "_"));

            } catch (IllegalArgumentException e) {
                System.out.println("Error in file. Category of plant data could not be parsed for plant on line " + (i + 1) + ". Terminating. \nError message: " + e.getMessage());
                System.exit(0);
            }
            String productName = null;
            try {
                productName = singularInfo[1].trim();
            } catch (IllegalArgumentException e) {
                System.out.println("Error in file. Category of plant data could not be parsed for plant on line " + (i + 1) + ". Terminating. \nError message: " + e.getMessage());
                System.exit(0);
            }
            String productCode = null;
            try {
                productCode = singularInfo[2].trim();
            } catch (IllegalArgumentException e) {
                System.out.println("Error in file. Category of plant data could not be parsed for plant on line " + (i + 1) + ". Terminating. \nError message: " + e.getMessage());
                System.exit(0);
            }
            String type = null;
            try {
                type = singularInfo[3].trim();
            } catch (IllegalArgumentException e) {
                System.out.println("Error in file. Type of plant data could not be parsed for plant on line " + (i + 1) + ". Terminating. \nError message: " + e.getMessage());
                System.exit(0);
            }
            Choice dwarf = null;

            try {

                dwarf = Choice.valueOf(singularInfo[4].trim().toUpperCase());

            } catch (IllegalArgumentException e) {
                System.out.println("Error in file. Dwarf of plant data could not be parsed for plant on line " + (i + 1) + ". Terminating. \nError message: " + e.getMessage());
                System.exit(0);
            }
            Choice deciduous = null;
            try {
                deciduous = Choice.valueOf(singularInfo[5].trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Error in file. Deciduoud of plant data could not be parsed for plant on line " + (i + 1) + ". Terminating. \nError message: " + e.getMessage());
                System.exit(0);
            }
            String training_system = null;
            try {
                training_system = singularInfo[6];
            } catch (IllegalArgumentException e) {
                System.out.println("Error in file. Training system of plant data could not be parsed for plant on line " + (i + 1) + ". Terminating. \nError message: " + e.getMessage());
                System.exit(0);
            }

            Map<Integer, Float> potSizeToPrice = new LinkedHashMap<>();

            if (potSizesRaw.length() > 0) {
                String[] optionsPotSizes = potSizesRaw.split(",");
                String[] optionsPrices = pricesRaw.split(",");
                for (int j = 0; j < optionsPrices.length; j++) {
                    int potSize = 0;
                    float price = 0f;
                    try {

                        potSize = Integer.parseInt(optionsPotSizes[j].trim());

                        price = Float.parseFloat(optionsPrices[j].trim());

                    } catch (IllegalArgumentException e) {
                        System.out.println("Error in file. Pot size/price option could not be parsed for item on line " + (i + 1) + ". Terminating. \nError message: " + e.getMessage());
                        System.exit(0);
                    }
                    potSizeToPrice.put(potSize, price);
                }
            }

            Set<String> pollinators = new HashSet<>();
            if (!pollinatorsRaw.isEmpty() && !pollinatorsRaw.equals(",")) {
                try {
                    String[] pollinatorArray = pollinatorsRaw.split(",");
                    for (String p : pollinatorArray) {
                        String trimmed = p.trim();
                        if (!trimmed.isEmpty()) {
                            pollinators.add(trimmed);
                        }
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Error in file. Pot size/price option could not be parsed for item on line " + (i + 1) + ". Terminating. \nError message: " + e.getMessage());
                    System.exit(0);
                }
            }


            Map<Criteria, Object> plantCriteria = new HashMap<>();
            plantCriteria.put(Criteria.CATEGORY, category);
            plantCriteria.put(Criteria.TYPE, type);
            plantCriteria.put(Criteria.DWARF, dwarf);
            plantCriteria.put(Criteria.DECIDUOUS, deciduous);
            plantCriteria.put(Criteria.TRAINING_SYSTEM, training_system);
            plantCriteria.put(Criteria.RECOMMENDED_POLLINATORS, pollinators);
            plantCriteria.put(Criteria.POT_SIZE, new ArrayList<>(potSizeToPrice.keySet()));
            plantCriteria.put(Criteria.POT_SIZE_TO_PRICE, potSizeToPrice);

            FruitingPlant fruitingPlant = new FruitingPlant(plantCriteria);
            Plant plant = new Plant(productCode, productName, description, fruitingPlant);
            inventory.addItem(plant);
        }
        return inventory;
    }

    /**
     * a method to get the user to enter a value range (min - max)
     *
     * @param minMessage the message to the user asking them to input a min value
     * @param maxMessage the message to the user asking them to input a max value
     * @return an int[] array where [0] is min and [1] is max
     */
    public static double[] minMaxValues(String minMessage, String maxMessage) {
        double[] range = {-1, -1};
        while (range[0] < 0) {
            String input = JOptionPane.showInputDialog(null, minMessage, appName, JOptionPane.QUESTION_MESSAGE);
            if (input == null) System.exit(0);
            try {
                range[0] = Double.parseDouble(input);
                if (range[0] < 0)
                    JOptionPane.showMessageDialog(null, "Min. must be >= 0.", appName, JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please try again.", appName, JOptionPane.ERROR_MESSAGE);
            }
        }
        while (range[1] < range[0]) {
            String input = JOptionPane.showInputDialog(null, maxMessage, appName, JOptionPane.QUESTION_MESSAGE);
            if (input == null) System.exit(0);
            try {
                range[1] = Double.parseDouble(input);
                if (range[1] < range[0])
                    JOptionPane.showMessageDialog(null, "Max must be >= " + range[0], appName, JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please try again.", appName, JOptionPane.ERROR_MESSAGE);
            }
        }
        return range;
    }

    /**
     * generates JOptionPanes requesting user input for Pet breed, sex, de-sexed status and age
     *
     * @return a DreamPet object representing the user's desired Pet criteria
     */
    private static FruitingPlant getCustomerCriteria() {
        Category category = (Category) JOptionPane.showInputDialog(null, "Please select the type of plant you'd like to request.", appName, JOptionPane.QUESTION_MESSAGE, icon, Category.values(), Category.CITRUS);
        if (category == null) System.exit(0);
        Map<Criteria, Object> customerFeatures = new HashMap<>();
        String type = (String) JOptionPane.showInputDialog(null, "Please select your preferred type.", appName, JOptionPane.QUESTION_MESSAGE, icon, inventory.getAllTypes(category).toArray(), "");
        if (type == null) System.exit(0);
        if (!category.equals(Category.VINE)) {
            Choice dwarf = (Choice) JOptionPane.showInputDialog(null, "Would you like your Plant to be dwarf or not?:", appName, JOptionPane.QUESTION_MESSAGE, icon, Choice.values(), Choice.YES);
            if (dwarf == null) System.exit(0);
            if(!dwarf.equals(Choice.NA)) customerFeatures.put(Criteria.DWARF, dwarf);
        }
        Choice deciduous = (Choice) JOptionPane.showInputDialog(null, "Please select your preferred deciduous:", appName, JOptionPane.QUESTION_MESSAGE, icon, Choice.values(), Choice.YES);
        if (deciduous == null) System.exit(0);

        customerFeatures.put(Criteria.CATEGORY, category);
        if(!type.equals("Skip")) customerFeatures.put(Criteria.TYPE, type);

        if(!deciduous.equals(Choice.NA)) customerFeatures.put(Criteria.DECIDUOUS, deciduous);
        if (category.equals(Category.VINE)) {
            String training_system = (String) JOptionPane.showInputDialog(null, "Please select from the following training options", appName, JOptionPane.QUESTION_MESSAGE, icon, inventory.getAllTrainingSystem(Category.VINE).toArray(), "");

            if (training_system == null) System.exit(0);
            if (!training_system.equals("Skip")) customerFeatures.put(Criteria.TRAINING_SYSTEM, training_system);
        }

        if (category.equals(Category.POME) || category.equals(Category.STONE_FRUIT)) {

            
            boolean isAnotherPollinator = true;
            Set<String> pollinators = new HashSet<>();
            while (isAnotherPollinator) {

                List<String> pollinatorsOptions = new ArrayList<>(inventory.getAllPollinators(category, type));
              
                String extraValue = (String) JOptionPane.showInputDialog(null, "Please select an pollinators?", appName, JOptionPane.QUESTION_MESSAGE, icon, pollinatorsOptions.toArray(), pollinatorsOptions.getFirst());
                if (extraValue != null && !extraValue.isEmpty()) {
                    if (!extraValue.equals("Skip")) {
                        pollinators.add(extraValue);
                        int option = JOptionPane.showConfirmDialog(null, "Would you like another pollinators?", appName, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
                        if (option == JOptionPane.NO_OPTION) {
                            isAnotherPollinator = false;
                        }
                    } else {
                        isAnotherPollinator = false;
                    }
                } else {
                    System.exit(0);
                }

            }
            customerFeatures.put(Criteria.RECOMMENDED_POLLINATORS, pollinators);
        }

        int potSize = Integer.parseInt((String) JOptionPane.showInputDialog(null, "Pot size (inch)? **min 8 inch", appName, JOptionPane.QUESTION_MESSAGE, icon, inventory.getAllPotSize(category, type).toArray(), null));
        if (potSize < 8) {
            JOptionPane.showMessageDialog(null, "Invalid input. Please enter a positive number greater than 8.", appName, JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        customerFeatures.put(Criteria.POT_SIZE, potSize);
        double[] priceRange = minMaxValues("What is the lowest price you're interested in? ", "What is the max. price you're willing to pay?");


        return new FruitingPlant(customerFeatures, priceRange[0], priceRange[1]);
    }

}

