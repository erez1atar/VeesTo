package veesto.com.android.veesto.Record;

/**
 * Created by erez on 14/11/2016.
 */
public enum AudioSourceE
{
    DEFAULT(0), MIC(1), VOICE_DOWNLINK(3) , CAMCORDER(5)
            , VOICE_COMMUNICATION(7), REMOTE_SUBMIX(8), UNPROCESSED(9);

    private final int value;
    private AudioSourceE(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static AudioSourceE fromInteger(int x) {
        switch(x) {
            case 0:
                return DEFAULT;
            case 1:
                return MIC;
            case 3:
                return VOICE_DOWNLINK;
            case 5:
                return CAMCORDER;
            case 7:
                return VOICE_COMMUNICATION;
            case 8:
                return REMOTE_SUBMIX;
            case 9:
                return UNPROCESSED;
            default:
                return null;
        }

    }
}
