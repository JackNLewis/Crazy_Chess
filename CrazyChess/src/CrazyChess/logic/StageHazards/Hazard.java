package CrazyChess.logic.StageHazards;
/**
 * A class for the Hazard object, which is the main object used
 * for stage hazards
 *
 */
public enum Hazard {
    FROZEN,
    BURN;
/**
 * Returns numerical value for a stage hazzard
 * @param hazard   Hazard enumerator
 * @return         1 if FROZEN, 2 if BURN, -1 if neither one
 */
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
