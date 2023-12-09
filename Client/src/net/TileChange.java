package net;

public class TileChange {
    private int mapIndex, x, y, newTile;

    public TileChange(int mapIndex, int x, int y, int newTile) {
        this.mapIndex = mapIndex;
        this.x = x;
        this.y = y;
        this.newTile = newTile;
    }

    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
    public int getNewTile() { return newTile; }
}