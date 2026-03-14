package your.domain.minecraft.pecoraGenesis.gene;

public enum GeneType {

    A(0b00),
    T(0b01),
    G(0b10),
    C(0b11);

    private final int bits;

    GeneType(int bits) {
        this.bits = bits;
    }

    public int getBits() {
        return bits;
    }

    public static GeneType fromBits(int bits) {
        for (GeneType type : values()) {
            if (type.bits == bits) {
                return type;
            }
        }
        return A;
    }

    public static GeneType fromChar(char c) {
        return switch (c) {
            case 'A' -> A;
            case 'T' -> T;
            case 'G' -> G;
            case 'C' -> C;
            default -> A;
        };
    }

    public char toChar() {
        return switch (this) {
            case A -> 'A';
            case T -> 'T';
            case G -> 'G';
            case C -> 'C';
        };
    }
}
