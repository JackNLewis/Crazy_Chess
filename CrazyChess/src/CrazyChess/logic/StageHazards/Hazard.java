package CrazyChess.logic.StageHazards;

public enum Hazard {
    FROZEN,
    BURN;

    public static int getValue(Hazard hazard){
        switch (hazard){
            case FROZEN:
                return 1;
            case BURN:
                return 2;
        }
        return -1;
    }
}
