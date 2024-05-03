package library;

public class Item {
    private int itemID;
    private int availableCopies;
    private int totalCopies;

    public Item()
    {
        itemID = 0;
        availableCopies = 0;
        totalCopies = 0;
    }

    public int getItemID()
    {
        return itemID;
    }

    public int getAvailableCopies()
    {
        return availableCopies;
    }

    public int getTotalCopies()
    {
        return totalCopies;
    }

    public void setItemID(int itemID)
    {
        this.itemID = itemID;
    }

    public void setAvailableCopies(int availableCopies)
    {
        this.availableCopies = availableCopies;
    }

    public void setTotalCopies(int totalCopies)
    {
        this.totalCopies = totalCopies;
    }
}
