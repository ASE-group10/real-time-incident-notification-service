package nl.ase_wayfinding.real_time_incident_notification_service.beans;

import io.pyroscope.javaagent.PyroscopeAgent;
import io.pyroscope.javaagent.config.Config;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

class PyroscopeBeanTest {

    // Utility to set private fields via reflection.
    private void setPrivateField(Object target, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testInitStartsPyroscopeAgent() {
        PyroscopeBean pyroscopeBean = new PyroscopeBean();
        setPrivateField(pyroscopeBean, "activeProfile", "dev");
        setPrivateField(pyroscopeBean, "applicationName", "TestApp");
        setPrivateField(pyroscopeBean, "pyroscopeServerAddress", "http://pyroscope-server");
        setPrivateField(pyroscopeBean, "pyroscopeServerAuthUser", "user");
        setPrivateField(pyroscopeBean, "pyroscopeServerAuthPassword", "password");

        try (MockedStatic<PyroscopeAgent> mockedAgent = Mockito.mockStatic(PyroscopeAgent.class)) {
            pyroscopeBean.init();
            mockedAgent.verify(() -> PyroscopeAgent.start(any(Config.class)), times(1));
        }
    }
}
