package zbl.moonlight.server.protocol;

import org.junit.jupiter.api.Test;
import zbl.moonlight.core.protocol.mdtp.MdtpMethod;

class MdtpMethodTest {
    @Test
    void getMethodNameTest() {
        assert "SET".equals(MdtpMethod.getMethodName((byte) 0x01));
        assert "SYSTEM".equals(MdtpMethod.getMethodName((byte) 0x06));
    }
}