package search;

public interface Heuristic {

    static double F = Math.sqrt(2) - 1;

    public static Heuristic MANHATTAN = (dx,dy) -> dx + dy;
    public static Heuristic EUCLIDEAN = (dx,dy) -> Math.sqrt(dx * dx + dy * dy);
    public static Heuristic OCTILE = (dx,dy) -> (dx < dy) ? F * dx + dy : F * dy + dx;
    public static Heuristic CHEBYSHEV = (dx,dy) -> Math.max(dx, dy);

    double cal(int dx,int dy);
}
