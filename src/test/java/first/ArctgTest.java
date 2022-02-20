package first;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("╯°□°）╯")
class ArctgTest {

    private static final double DELTA = 0.05;

    private Arctg arctg;

    @BeforeAll
    void setUp() {
        arctg = new Arctg();
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/test_data.csv")
    void tableValuesTest(float expected, float x) {
        double actual = arctg.getResult(x);
        assertEquals(expected, actual, DELTA);
    }

    @Test
    void nanTest() {
        double expected = Double.NaN;
        double actual = Double.NaN;
        assertEquals(expected, actual, DELTA);
    }

    @Test
    void positiveInfinityTest() {
        double expected = Double.NaN;;
        double actual = arctg.getResult(Double.POSITIVE_INFINITY);
        assertEquals(expected, actual, DELTA);
    }

    @Test
    void negativeInfinityTest() {
        double expected = Double.NaN;
        double actual = arctg.getResult(Double.NEGATIVE_INFINITY);
        assertEquals(expected, actual, DELTA);
    }
}