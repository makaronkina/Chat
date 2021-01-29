package externalHomework;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.stream.Stream;

public class DemoArrayTest {
    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowRuntimeExeptionWhenEmptyArrayPassed(int[] values) {
        Assertions.assertThrows(RuntimeException.class, () -> DemoAray.separateArray(values));
    }

    @ParameterizedTest
    @MethodSource("arrayParametersProvider")
    void shouldThrowRuntimeExeptionWhenNoFourFoud(int a, int b) {
        Assertions.assertThrows(RuntimeException.class, () -> DemoAray.separateArray(new int[]{a, b}));
    }

    private static Stream<Arguments> arrayParametersProvider() {
        return Stream.of(
                Arguments.arguments(1, 2),
                Arguments.arguments(3, 5),
                Arguments.arguments(6, 44)
        );
    }

    @Test
    void shouldReturnNewArrayAfterLastFour() {
        Assertions.assertArrayEquals(new int[]{1, 7}, DemoAray.separateArray(new int[]{1, 2, 4, 4, 2, 3, 4, 1, 7}));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowRuntimeExeptionWhenEmptyArrayPassedInCheckArray(Integer[] values) {
        Assertions.assertThrows(RuntimeException.class, () -> DemoAray.checkArray(values));
    }

    @Test
    void shouldReturnTrueWhenNoFourOrOneFound() {
        Assertions.assertTrue(DemoAray.checkArray(new Integer[]{4, 4, 1}));
    }

    @ParameterizedTest
    @MethodSource("checkArrayParametersProvider")
    void shouldReturnFalseWhenNoFourOrOneFoud(Integer a, Integer b, Integer c) {
        Assertions.assertFalse(DemoAray.checkArray(new Integer[] {a, b, c}));
    }

    private static Stream<Arguments> checkArrayParametersProvider() {
        return Stream.of(
                Arguments.arguments(4, 4, 4),
                Arguments.arguments(1, 1, 1),
                Arguments.arguments(41, 14, 44),
                Arguments.arguments(5, 23, 11)
        );
    }
}
