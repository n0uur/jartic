package Test;

import Shared.Logger.GameLog;
import Shared.RunLength;

public class EncodingTest {
    public static void main(String[] args) {

        String text = "00001111000011111111111111111111";

        GameLog.warn(RunLength.encode(text));

        GameLog.error(RunLength.decode(RunLength.encode(text)));

    }
}
