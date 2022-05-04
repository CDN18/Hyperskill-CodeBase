class Predicate {
    public static final TernaryIntPredicate ALL_DIFFERENT = (a, b, c) ->
            a != b && a != c && b != c;

    @FunctionalInterface
    public interface TernaryIntPredicate {
        // Write a method here
        boolean test(int a, int b, int c);
    }
}