package your.domain.minecraft.pecoraGenesis.trait;

public class Trait {

    private final int drop;
    private final int food;
    private final int breed;
    private final int status;

    public Trait(int drop, int food, int breed, int status) {
        this.drop = drop;
        this.food = food;
        this.breed = breed;
        this.status = status;
    }

    public int getDrop() {
        return drop;
    }

    public int getFood() {
        return food;
    }

    public int getBreed() {
        return breed;
    }

    public int getStatus() {
        return status;
    }
}
