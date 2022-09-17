package rcs;

public class Temporary {
    public static void main(String[] args) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 3; i++) {
            sb.append((char) (0x4e00 + (Math.random() * (0x9fa5 - 0x4e00 + 1))));
        }
        System.out.println();
    }
}
