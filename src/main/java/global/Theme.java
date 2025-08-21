package global;

public enum Theme
{
    LIGHT_MODE(0),
    DARK_MODE(1);

    public final int value;

    Theme(int value)
    {
        this.value = value;
    }
}
