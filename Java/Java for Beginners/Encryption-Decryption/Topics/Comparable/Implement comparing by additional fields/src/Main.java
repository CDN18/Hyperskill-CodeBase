class Article implements Comparable<Article> {
    private String title;
    private int size;

    public Article(String title, int size) {
        this.title = title;
        this.size = size;
    }

    public String getTitle() {
        return this.title;
    }

    public int getSize() {
        return this.size;
    }

    @Override
    public int compareTo(Article otherArticle) {
        // add your code here!
        if (this.size == otherArticle.size) {
            return this.title.compareTo(otherArticle.getTitle());
        }
        return this.size - otherArticle.size;
    }
}