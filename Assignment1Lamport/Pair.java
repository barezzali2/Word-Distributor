package WordDistributor.Assignment1Lamport;

class Pair {
    private final String word;
    private final int clock;

    public Pair(String word, int clock) {
        this.word = word;
        this.clock = clock;
    }

    public String getWord() {
        return word;
    }

    public int getClock() {
        return clock;
    }
}
