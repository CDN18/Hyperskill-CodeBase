class BigIntegerConverter {

    /**
     * @param number string representing the number
     * @return BigInteger instance
     */
    public static Number getBigInteger(String number) {
        return new java.math.BigInteger(number); // create BigInteger instance here
    }
}