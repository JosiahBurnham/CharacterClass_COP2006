import java.util.HashMap;
import java.util.Map;

public class Character {
    /* demographic variables for standard (dnd)
     * charClass = can only equal classes listed in <placeHolder>
     *race = can only equal "elf", "dwarf", "human", "gnome", "halfling"
     */
    private String name = "";
    private String charClass = "";
    private String race = "";
    /* States will hold our main 8 game stats:
     *      Strength (str), dexterity (dex), constitution (con),
     *      Intelligence (int), Wisdom (wis), Charisma (cha),
     *      maxHP, HP (current HP), maxMP, MP (current MP)
     * Bonuses will hold our 6 bonus stats (all but HP & MP)
     */
    private Map<String, Integer> stats = new HashMap<>();
    private Map<String, Integer> bounuses = new HashMap<>();

    public Character(String name, String race, String charClass){
        /** Create a character given name, race, and class.
         *      Load base stats for all starting characters into stats
         *      Create unique bonus stats for later calculation
         *      Call determineBonuses to calculate bonus stats based on race/class
         *      Build the character at level 1
         *
         * @param name Character Name
         * @param race Character race(elf, dwarf, human, gnome, halfling)
         * @param class Character job class (see determineBonuses)
         */

        this.name = name;
        this.race = race.toLowerCase();
        this.charClass = charClass.toLowerCase();

        stats.put("str",2);
        stats.put("dex",2);
        stats.put("con",3);
        stats.put("wis",3);
        stats.put("int",2);
        stats.put("cha",2);

        bounuses.put("str",0);
        bounuses.put("dex",0);
        bounuses.put("con",0);
        bounuses.put("wis",0);
        bounuses.put("int",0);
        bounuses.put("cha",0);

        determineBonuses();
        buildCharacter(1);
        stats.put("HP", stats.get("maxHP"));
        stats.put("MP", stats.get("maxMP"));
    }

    public void getHealed(int healed){
        /**Adds the amount to be healed to the current HP
         * unless that amount would be over maxHP
         * then it sets HP to max HP
         *
         * @param healed The amount to be added to character HP
         */
        // you do not want to heal more than maxHP
        if((stats.get("HP")+healed) <= stats.get("maxHP")) {
            stats.put("HP", stats.get("HP") + healed);
        }else{
            stats.put("HP", stats.get("maxHP"));
        }
    }

    public void takeDamage(int damage){
        /** Subtracts from the player's health
         *  and checks to see if the player is dead
         *
         * @param damage The damage being applied to the character
         */
        stats.put("HP", stats.get("HP")-damage);
        if(stats.get("HP") <= 0){
            System.out.println(printGameOver());
        }
    }

    public void changeLevel(int level){
        /**Changes the character to the desired level
         *(I am not calling build character here for balance sake)
         *
         * @param level The current level of the character
         */

        // you only want to change the stats of the character if they are above level 1
        if(level > 1) {
            for (String key : stats.keySet()) {
                // this game does not heal on level up
                if(!"HP".equals(key)&&!"MP".equals(key)) {
                    // add what ever the level is * 2 to the stats
                    stats.put(key, stats.get(key) + (level * 2));
                }
            }
        }

    }

    private String printGameOver(){
        /**returns the ending when called in takeDamage()
         * when HP is less than or equal to 0
         *
         * @return ending The ending showed to the player when they have died
         */
        String ending =String.format("You fought Gallantly %s but alas it's the end of your" +
                " journey, you have died.", name);
        return ending;
    }

    private void determineBonuses(){
        /**
         * Determine bonuses based on class & race
         *  Also holds main classes (only class choices) for Map
         */
        // first bonuses based on class
        switch (charClass){
            case "cleric":
                bounuses.put("wis", bounuses.get("wis")+2);
                bounuses.put("cha", bounuses.get("cha")+1);
                bounuses.put("dex", bounuses.get("dex")-1);
                break;
            case "barbarian":
                bounuses.put("str", bounuses.get("str")+3);
                bounuses.put("dex", bounuses.get("dex")+2);
                bounuses.put("int", bounuses.get("int")-2);
                bounuses.put("wis", bounuses.get("wis")-1);
                break;
            case "wizard":
                bounuses.put("str", bounuses.get("str")-1);
                bounuses.put("wis", bounuses.get("wis")+3);
                bounuses.put("int", bounuses.get("int")+5);
                bounuses.put("dex", bounuses.get("dex")-2);
                break;
            case "ranger":
                bounuses.put("str", bounuses.get("str")+1);
                bounuses.put("dex", bounuses.get("dex")+5);
                bounuses.put("con", bounuses.get("con")+2);
                bounuses.put("cha", bounuses.get("cha")-1);
                break;
        }
        // add race bonuses
        switch (race){
            case "halfling": case "half-ling": case "hobbit":
                bounuses.put("str", bounuses.get("str") - 2);
                bounuses.put("cha", bounuses.get("cha") + 2);
                bounuses.put("dex", bounuses.get("dex") + 3);
                break;
            case "dwarf": case "duegar":
                bounuses.put("str", bounuses.get("str")+3);
                bounuses.put("int", bounuses.get("int")-2);
                bounuses.put("con", bounuses.get("con")+3);
                bounuses.put("cha", bounuses.get("cha")-3);
                break;
            case "elf": case "dark elf": case"high elf":
                bounuses.put("str", bounuses.get("str")+1);
                bounuses.put("int", bounuses.get("int")+2);
                bounuses.put("dex", bounuses.get("dex")+5);
                bounuses.put("cha", bounuses.get("cha")-3);
                break;
            case "gnome":
                // I mean, have you seen Gnomeo and Juliet?
                bounuses.put("cha", bounuses.get("cha")+10);
                break;
            case "human": default:
                bounuses.put("str", bounuses.get("str")+2);
                bounuses.put("int", bounuses.get("int")+2);
                bounuses.put("dex", bounuses.get("dex")+1);
                bounuses.put("con", bounuses.get("con")-1);
                break;

        }
    }
    private void buildCharacter(int level){
        /**Build character using bonuses & level
         *
         * @param level character being built from
         */

        for(String key : bounuses.keySet()){
            /**bonuses and stats have the same keys
             * we can loop through both at one
             */

            stats.put(key, level * (stats.get(key)+bounuses.get(key)));
        }
        // calculating HP and MP using con & wis ( if -1 or less treat as 0)
        if (stats.get("con") > 1){
            stats.put("maxHP", stats.get("con")*100);
        }else{
            stats.put("maxHP", 100);
        }
        if (stats.get("wis") > 1){
            stats.put("maxMP", stats.get("wis")*10);
        }else{
            stats.put("maxMP", 10);
        }
    }

    @Override
    public String toString() {
        /**Inherits properties of toString
         *  & Overrides it to print our template
         *  
         * @return desc Description of character based on printf template
         */

        String desc = String.format("%s the %s %s with ", name, race, charClass);
        for(String key : stats.keySet()){
            if (!"maxHP".equals(key) && !"maxMP".equals(key)) {
                desc += String.format("%s of %d ", key, stats.get(key));
            }
        }
        return desc;
    }
}
