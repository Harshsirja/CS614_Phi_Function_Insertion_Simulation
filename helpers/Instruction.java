package helpers;

import java.util.ArrayList;

public class Instruction {
    String left;
    ArrayList<String> right;

    public Instruction(String left, ArrayList<String> right) {
        this.left = left;
        this.right = right;
    }
}
