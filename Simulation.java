import java.util.ArrayList;

import helpers.*;

public class Simulation {
    public static void main(String[] args) {
        Block B1 = new Block("B1");
        Block B2 = new Block("B2");
        Block B3 = new Block("B3");
        Block B4 = new Block("B4");
        Block B5 = new Block("B5");
        Block B6 = new Block("B6");
        Block B7 = new Block("B7");
        Block STOP = new Block("STOP");

        B1.addSuccessor(B2);
        B2.addSuccessor(B3);
        B2.addSuccessor(B4);
        B3.addSuccessor(B5);
        B3.addSuccessor(B6);
        B4.addSuccessor(STOP);
        B5.addSuccessor(B7);
        B6.addSuccessor(B7);
        B7.addSuccessor(B2);

        ArrayList<String> right = new ArrayList<>();
        right.add("0");
        B1.addInstruction("i", right);
        right = new ArrayList<>();
        right.add("n");
        B1.addInstruction(null, right);
        right = new ArrayList<>();
        right.add("1");
        B2.addInstruction("n", right);
        right = new ArrayList<>();
        right.add("n");
        B3.addInstruction(null, right);
        right = new ArrayList<>();
        right.add("i");
        B4.addInstruction(null, right);
        right = new ArrayList<>();
        right.add("n");
        right.add("2");
        B5.addInstruction("n", right);
        right = new ArrayList<>();
        right.add("3");
        right.add("n");
        right.add("1");
        B6.addInstruction("n", right);
        right = new ArrayList<>();
        right.add("i");
        right.add("1");
        B7.addInstruction("i", right);

        CFG cfg = new CFG(B1);
        cfg.addBlock(B1);
        cfg.addBlock(B2);
        cfg.addBlock(B3);
        cfg.addBlock(B4);
        cfg.addBlock(B5);
        cfg.addBlock(B6);
        cfg.addBlock(B7);
        cfg.addBlock(STOP);

        cfg.computeDominators();
        cfg.computeImmediateDominators();
        cfg.computeDominance();
        cfg.computeDominanceFrontiers();
        cfg.computeIteratedDominanceFrontier();
        cfg.computeVariableDefine();
        cfg.phiInsertion();

        cfg.printDominators();
        cfg.printImmediateDominators();
        cfg.printDominance();
        cfg.printDominanceFrontiers();
        cfg.printIteratedDominanceFrontiers();
        cfg.printVarDefine();
        cfg.printPhiFunctions();
    }
}
