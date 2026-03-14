package your.domain.minecraft.pecoraGenesis.gene;

import java.util.Objects;

public class Gene {

    private final GeneType left;
    private final GeneType right;

    public Gene(GeneType left, GeneType right) {
        this.left = left;
        this.right = right;
    }
    /*
     文字列から生成
     例: "AT"
     */
    public static Gene fromString(String str) {

        if (str == null || str.length() != 2) {
            return new Gene(GeneType.A, GeneType.A);
        }

        GeneType left = GeneType.fromChar(str.charAt(0));
        GeneType right = GeneType.fromChar(str.charAt(1));

        return new Gene(left, right);
    }
    /*
     bitから生成
     例: 0001 → AT
     */
    public static Gene fromBits(int bits) {

        int leftBits = (bits >> 2) & 0b11;
        int rightBits = bits & 0b11;

        return new Gene(
                GeneType.fromBits(leftBits),
                GeneType.fromBits(rightBits)
        );
    }
    /*
     4bitへ変換
     */
    public int toBits() {
        return (left.getBits() << 2) | right.getBits();
    }
    /*
     文字列へ変換
     */
    @Override
    public String toString() {
        return "" + left.toChar() + right.toChar();
    }
    public GeneType getLeft() {
        return left;
    }
    public GeneType getRight() {
        return right;
    }
    /*
     デフォルトAA
     */
    public static Gene defaultGene() {
        return new Gene(GeneType.A, GeneType.A);
    }
    /*
     equals
     */
    @Override
    public boolean equals(Object obj) {

        if (this == obj) return true;

        if (!(obj instanceof Gene other)) return false;

        return left == other.left && right == other.right;
    }

    @Override
    public int hashCode() {
        return  Objects.hash(left, right);
    }
}
