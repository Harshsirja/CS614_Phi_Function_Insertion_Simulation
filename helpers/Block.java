package helpers;

import java.util.ArrayList;
import java.util.HashSet;

public class Block {
    String name;
    ArrayList<Instruction> instructions = new ArrayList<>();
    HashSet<Block> successors = new HashSet<>();
    HashSet<Block> predecessors = new HashSet<>();
    HashSet<Block> definedVariables = new HashSet<>();
    HashSet<String> phiFunctions = new HashSet<>();

    public Block(String name) {
        this.name = name;
    }

    public void addSuccessor(Block succ) {
        this.successors.add(succ);
        succ.predecessors.add(this);
    }

    @Override
    public String toString() {
        return name;
    }

    public void addInstruction(String left, ArrayList<String> right) {
        instructions.add(new Instruction(left, right));
    }
}
