public class CreateCharacter {
    public static void main(String[] args) {
        Character player1 = new Character("Link","dwarf","barbarian");
        System.out.println(player1.toString());
        player1.changeLevel(3);
        player1.getHealed(2);
        System.out.println(player1.toString());
        player1.getHealed(1000);
        System.out.println(player1.toString());
        player1.takeDamage(1000);
    }
}
