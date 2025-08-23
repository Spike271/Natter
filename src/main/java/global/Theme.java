package global;

public enum Theme
{
    LIGHT_MODE(0, "LIGHT_MODE"),
    DARK_MODE(1, "DARK_MODE");

    public final int value;
    public final String name;

    Theme(int value, String name)
    {
        this.value = value;
        this.name = name;
    }
}
