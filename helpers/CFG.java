package helpers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;

public class CFG {
    Block sourceNode;
    ArrayList<Block> blocks = new ArrayList<>();
    HashMap<Block, HashSet<Block>> dominators = new HashMap<>();
    HashMap<Block, HashSet<Block>> dominates = new HashMap<>();
    HashMap<Block, Block> immediateDominator = new HashMap<>();
    HashMap<Block, HashSet<Block>> dominanceFrontiers = new HashMap<>();
    HashMap<Block, HashSet<Block>> iteratedDominanceFrontiers = new HashMap<>();
    HashMap<String, HashSet<Block>> defBlocks = new HashMap<>();

    public CFG(Block sourceNode) {
        this.sourceNode = sourceNode;
    }

    // public void addAllBlock(ArrayList<Block> blocks) {
    // this.blocks = blocks;
    // }

    public void addBlock(Block block) {
        blocks.add(block);
    }

    public void computeDominators() {
        for (Block block : blocks) {
            dominators.put(block, new HashSet<>(blocks));
        }
        HashSet<Block> startDominators = new HashSet<>();
        startDominators.add(sourceNode);
        dominators.put(sourceNode, startDominators);

        boolean isChanged = true;
        while (isChanged) {
            isChanged = false;
            for (Block block : blocks) {
                if (block != sourceNode) {
                    HashSet<Block> updatedSet = new HashSet<>();
                    updatedSet.add(block);

                    if (!block.predecessors.isEmpty()) {
                        HashSet<Block> intersection = new HashSet<>();
                        for (Block pred : block.predecessors) {
                            intersection.addAll(dominators.get(pred));
                        }
                        // System.out.println("Intersection For Bloc: " + intersection);
                        for (Block pred : block.predecessors) {
                            intersection.retainAll(dominators.get(pred));
                        }
                        updatedSet.addAll(intersection);
                    }

                    if (!dominators.get(block).equals(updatedSet)) {
                        dominators.put(block, updatedSet);
                        isChanged = true;
                    }
                }
            }
        }
        // for (Block block : blocks) {
        // Collections.sort(dominators.get(block));
        // }
    }

    public void computeImmediateDominators() {
        for (Block block : blocks) {
            if (block != sourceNode) {
                HashSet<Block> Candidates = new HashSet<>(dominators.get(block));
                Candidates.remove(block); // Remove itself

                Block immediateDom = null;
                for (Block candidate : Candidates) {
                    boolean isClosest = true;
                    for (Block other : Candidates) {
                        if (other != candidate && dominators.get(other).contains(candidate)) {
                            isClosest = false;
                            break;
                        }
                    }
                    if (isClosest) {
                        immediateDom = candidate;
                        break;
                    }
                }
                immediateDominator.put(block, immediateDom);
            }
        }
    }

    public void computeDominance() {
        for (Block block : blocks) {
            dominates.put(block, new HashSet<>());
        }

        for (Block block : blocks) {
            for (Block d : dominators.get(block)) {
                dominates.get(d).add(block);
            }
        }
    }

    public void computeDominanceFrontiers() {
        for (Block block : blocks) {
            dominanceFrontiers.put(block, new HashSet<>());
        }
        for (Block node : blocks) {
            // dominates.remove(node);
            for (Block b : dominates.get(node)) {
                for (Block s : b.successors) {
                    if (s.equals(node) || !dominators.get(s).contains(node)) {
                        dominanceFrontiers.get(node).add(s);
                    }
                }
            }
        }
    }

    public void computeIteratedDominanceFrontier() {
        for (Block block : blocks) {
            iteratedDominanceFrontiers.put(block, new HashSet<>());
        }

        for (Block block : blocks) {
            HashSet<Block> idf = new HashSet<>();
            HashSet<Block> idf_1 = new HashSet<>(dominanceFrontiers.get(block));

            while (!idf_1.isEmpty()) {
                Block x = idf_1.iterator().next();
                idf_1.remove(x);

                if (!idf.contains(x)) {
                    idf.add(x);
                    idf_1.addAll(dominanceFrontiers.get(x)); // Propagate recursively
                }
            }
            iteratedDominanceFrontiers.put(block, idf);
        }
    }

    public void computeVariableDefine() {
        for (Block block : blocks) {
            for (Instruction instr : block.instructions) {
                if (instr.left != null) {
                    defBlocks.putIfAbsent(instr.left, new HashSet<>());
                    defBlocks.get(instr.left).add(block);
                }
            }
        }
    }

    public void phiInsertion() {
        for (String var : defBlocks.keySet()) {
            if (var == null)
                continue;
            HashSet<Block> res = new HashSet<>(defBlocks.get(var));
            HashSet<Block> insertionPoints = new HashSet<>();
            for (Block b : res) {
                insertionPoints.addAll(iteratedDominanceFrontiers.get(b));
            }
            for (Block b : insertionPoints) {
                b.phiFunctions.add(var);
            }
        }
    }

    public void printDominators() {
        for (Block block : blocks) {
            System.out.println(block + " Dominates by : " + dominators.get(block));
        }
    }

    public void printDominance() {
        for (Block block : blocks) {
            System.out.println(block + " Dominates : " + dominates.get(block));
        }
    }

    public void printImmediateDominators() {
        for (Block block : blocks) {
            System.out.println(block + " Immediate Dominates by : " + immediateDominator.get(block));
        }
    }

    public void printDominanceFrontiers() {
        for (Block block : blocks) {
            System.out.println("Dominance Frontier of " + block + ": " + dominanceFrontiers.get(block));
        }
    }

    public void printIteratedDominanceFrontiers() {
        for (Block block : blocks) {
            System.out
                    .println("Iterated Dominance Frontier of " + block + ": " + iteratedDominanceFrontiers.get(block));
        }
    }

    public void printVarDefine() {
        for (String var : defBlocks.keySet()) {
            System.out.println(var + " defined in " + defBlocks.get(var));
        }
    }

    public void printPhiFunctions() {
        for (Block block : blocks) {
            if (!block.phiFunctions.isEmpty()) {
                System.out.println(block + " : phi(" + block.phiFunctions + ")");
            }
        }
    }

}
