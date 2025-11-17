package com.czhao.test.jdk24;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.stream.Gatherer;
import java.util.stream.Gatherers;
import java.util.stream.Stream;

@SuppressWarnings({"java:S106", "java:S119", "java:S1452", "DuplicatedCode"})
public class Test485StreamGatherer {

    static void main() {
        Test485StreamGatherer me = new Test485StreamGatherer();
        me.doCheckRecentReadings();
        me.testWindowFixed();
        me.testParallel();
    }

    private void testParallel() {
        var number = Stream.generate(() -> ThreadLocalRandom.current().nextInt(10000))
                .limit(1000)                   // Take the first 1000 elements
                .gather(selectOne(Math::max))  // Select the largest value seen
                .parallel()                    // Execute in parallel
                .findFirst();                   // Extract the largest value
        System.out.println(number);
    }


    static <TR> Gatherer<TR, ?, TR> selectOne(BinaryOperator<TR> selector) {

        // Validate input
        Objects.requireNonNull(selector, "selector must not be null");

        // Private state to track information across elements
        class State {
            TR value;            // The current best value
            boolean hasValue;    // true when value holds a valid value
        }

        // Use the `of` factory method to construct a gatherer given a set
        // of functions for `initializer`, `integrator`, `combiner`, and `finisher`
        return Gatherer.of(

                // The initializer creates a new State instance
                State::new,

                // The integrator; in this case we use `ofGreedy` to signal
                // that this integrator will never short-circuit
                Gatherer.Integrator.ofGreedy((state, element, _) -> {
                    if (!state.hasValue) {
                        // The first element, just save it
                        state.value = element;
                        state.hasValue = true;
                    } else {
                        // Select which value of the two to save, and save it
                        state.value = selector.apply(state.value, element);
                    }
                    return true;
                }),

                // The combiner, used during parallel evaluation
                (leftState, rightState) -> {
                    if (!leftState.hasValue) {
                        // If no value on the left, return the right
                        return rightState;
                    } else if (!rightState.hasValue) {
                        // If no value on the right, return the left
                        return leftState;
                    } else {
                        // If both sides have values, select one of them to keep
                        // and store it in the leftState, as that will be returned
                        leftState.value = selector.apply(leftState.value,
                                rightState.value);
                        return leftState;
                    }
                },

                // The finisher
                (state, downstream) -> {
                    // Emit the selected value, if there is one, downstream
                    if (state.hasValue)
                        downstream.push(state.value);
                }

        );
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void testWindowFixed() {
        System.out.println(Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9).gather(Gatherers.windowFixed(3)).toList());
        System.out.println(Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9).gather(new WindowFixed(3)).toList());
        System.out.println(Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9).gather(fixedWindow(3)).toList());
    }

    @SuppressWarnings("DuplicatedCode")
    record WindowFixed<TR>(int windowSize)
            implements Gatherer<TR, ArrayList<TR>, List<TR>> {

        public WindowFixed {
            // Validate input
            if (windowSize < 1)
                throw new IllegalArgumentException("window size must be positive");
        }

        @Override
        public Supplier<ArrayList<TR>> initializer() {
            // Create an ArrayList to hold the current open window
            return () -> new ArrayList<>(windowSize);
        }

        @Override
        public Integrator<ArrayList<TR>, TR, List<TR>> integrator() {
            // The integrator is invoked for each element consumed
            return Gatherer.Integrator.ofGreedy((window, element, downstream) -> {

                // Add the element to the current open window
                window.add(element);

                // Until we reach our desired window size,
                // return true to signal that more elements are desired
                if (window.size() < windowSize)
                    return true;

                // When the window is full, close it by creating a copy
                var result = new ArrayList<>(window);

                // Clear the window so the next can be started
                window.clear();

                // Send the closed window downstream
                return downstream.push(result);

            });
        }

        // The combiner is omitted since this operation is intrinsically sequential,
        // and thus cannot be parallelized

        @Override
        public BiConsumer<ArrayList<TR>, Downstream<? super List<TR>>> finisher() {
            // The finisher runs when there are no more elements to pass from
            // the upstream
            return (window, downstream) -> {
                // If the downstream still accepts more elements and the current
                // open window is non-empty, then send a copy of it downstream
                if (!downstream.isRejecting() && !window.isEmpty()) {
                    downstream.push(new ArrayList<>(window));
                    window.clear();
                }
            };
        }
    }

    /**
     * Gathers elements into fixed-size groups. The last group may contain fewer
     * elements.
     *
     * @param windowSize the maximum size of the groups
     * @param <TR>       the type of elements the returned gatherer consumes and produces
     * @return a new gatherer which groups elements into fixed-size groups
     */
    @SuppressWarnings("SameParameterValue")
    static <TR> Gatherer<TR, ?, List<TR>> fixedWindow(int windowSize) {

        // Validate input
        if (windowSize < 1)
            throw new IllegalArgumentException("window size must be non-zero");

        // This gatherer is inherently order-dependent,
        // so it should not be parallelized
        return Gatherer.ofSequential(

                // The initializer creates an ArrayList which holds the current
                // open window
                () -> new ArrayList<TR>(windowSize),

                // The integrator is invoked for each element consumed
                Gatherer.Integrator.ofGreedy((window, element, downstream) -> {

                    // Add the element to the current open window
                    window.add(element);

                    // Until we reach our desired window size,
                    // return true to signal that more elements are desired
                    if (window.size() < windowSize)
                        return true;

                    // When window is full, close it by creating a copy
                    var result = new ArrayList<>(window);

                    // Clear the window so the next can be started
                    window.clear();

                    // Send the closed window downstream
                    return downstream.push(result);

                }),

                // The combiner is omitted since this operation is intrinsically sequential,
                // and thus cannot be parallelized

                // The finisher runs when there are no more elements to pass from the upstream
                (window, downstream) -> {
                    // If the downstream still accepts more elements and the current
                    // open window is non-empty then send a copy of it downstream
                    if (!downstream.isRejecting() && !window.isEmpty()) {
                        downstream.push(new ArrayList<>(window));
                        window.clear();
                    }
                }

        );
    }

    private void doCheckRecentReadings() {
        var resultByForeach = findSuspiciousByForeach(Reading.loadRecentReadings());
        System.out.println(resultByForeach);

        var resultByGatherer = findSuspiciousByGatherer(Reading.loadRecentReadings());
        System.out.println(resultByGatherer);
    }

    List<List<Reading>> findSuspiciousByGatherer(Stream<Reading> source) {
        return source.gather(Gatherers.windowSliding(2))
                .filter(window -> (window.size() == 2
                        && isSuspicious(window.getFirst(),
                        window.get(1))))
                .toList();
    }

    List<List<Reading>> findSuspiciousByForeach(Stream<Reading> source) {
        var suspicious = new ArrayList<List<Reading>>();
        Reading previous = null;
        boolean hasPrevious = false;
        for (Reading next : source.toList()) {
            if (!hasPrevious) {
                hasPrevious = true;
            } else {
                if (isSuspicious(previous, next))
                    suspicious.add(List.of(previous, next));
            }
            previous = next;
        }
        return suspicious;
    }

    boolean isSuspicious(Reading previous, Reading next) {
        return next.obtainedAt().isBefore(previous.obtainedAt().plusSeconds(5))
                && (next.kelvins() > previous.kelvins() + 30
                || next.kelvins() < previous.kelvins() - 30);
    }

    record Reading(Instant obtainedAt, int kelvins) {

        Reading(String time, int kelvins) {
            this(Instant.parse(time), kelvins);
        }

        static Stream<Reading> loadRecentReadings() {
            // In reality these could be read from a file, a database,
            // a service, or otherwise
            return Stream.of(
                    new Reading("2023-09-21T10:15:30.00Z", 310),
                    new Reading("2023-09-21T10:15:31.00Z", 312),
                    new Reading("2023-09-21T10:15:32.00Z", 350),
                    new Reading("2023-09-21T10:15:33.00Z", 310)
            );
        }

    }
}
