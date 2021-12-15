package Tools;

public class Filter extends Tools{

    protected static float [] HSL ;

    public static void calculateHSL()
    {
        HSL=ColorConversion.MHSL(img.RGBA);
    }

}
