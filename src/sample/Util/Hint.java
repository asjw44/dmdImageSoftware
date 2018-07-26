package sample.Util;

import javafx.scene.control.Tooltip;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.Map;

public class Hint {

    private static final int textSize = 12;
    private static final double show_ms = 5000;          //Default = 5000
    private static final double show_delay_ms = 1000;    //Default = 1000

    public static final String ITERATION_BASE_NAME = "baseName";
    public static final String ITERATION_CANVAS_SIZE = "canvasSize";
    public static final String ITERATION_USE_DMD = "useDMD";
    public static final String ITERATION_STATIC_SHAPES = "staticShapes";

    public static final String ITERATION_SHAPE = "shape";
    public static final String ITERATION_SHAPE_LOC = "shapeLoc";
    public static final String ITERATION_SHAPE_SIZE = "shapeSize";
    public static final String ITERATION_DOUGHNUT = "dRadius";

    public static final String ITERATION_NUM_IMAGES = "numOfImages";
    public static final String ITERATION_TRANSLATE_X = "translateX";
    public static final String ITERATION_TRANSLATE_Y = "translateY";
    public static final String ITERATION_dWIDTH = "dWidth";
    public static final String ITERATION_dHEIGHT = "dHeight";

    public static final String ITERATION_CENTER_SHAPES = "centerShapes";
    public static final String ITERATION_DEPLOY = "deployDMD";
    public static final String ITERATION_DEPLOY_TYPE = "deployType";


    private static final Map<String, String> iteration;

    static {
        iteration = Map.ofEntries(
                Map.entry(ITERATION_BASE_NAME,"Enter the name of the images\n" +
                        "These will appear in the form [baseName]_i\n" +
                        "where i is the iteration number."),
                Map.entry(ITERATION_CANVAS_SIZE,"Enter the size (in pixels) of the image."),
                Map.entry(ITERATION_USE_DMD,"Tick to use the dimensions of the DMD."),
                Map.entry(ITERATION_STATIC_SHAPES, "Add other shapes. These will not be iterated over and as such will appear constant over all images."),

                Map.entry(ITERATION_SHAPE,"Choose the type of shape that you want to apply the iterations to."),
                Map.entry(ITERATION_SHAPE_LOC,"Enter the location that the shape will start at.\n" +
                        "**Please note: this is the top-left co-ordinate for ALL shapes**"),
                Map.entry(ITERATION_SHAPE_SIZE,"Enter the size the shape will initially be."),
                Map.entry(ITERATION_DOUGHNUT,"Enter the difference between the two radii of the inner and outer circle of the doughnut\ni.e outer radius = r\ninner radius = r - dRad."),

                Map.entry(ITERATION_NUM_IMAGES,"Total number of images.\n" +
                        "**Please note: one image contains twenty-four bit-planes."),
                Map.entry(ITERATION_TRANSLATE_X,"Move the shape x pixels to the right - or the to left if the checkbox has been ticked - for each bit-plane iteration."),
                Map.entry(ITERATION_TRANSLATE_Y,"Move the shape x pixels up - or down if the checkbox has been ticked - for each bit-plane iteration."),
                Map.entry(ITERATION_dWIDTH,"Make the shape x pixels wider - or thinner if the checkbox has been ticked - for each bit-plane iteration."),
                Map.entry(ITERATION_dHEIGHT,"Make the shape x pixels taller - or shorter if the checkbox has been ticked - for each bit-plane iteration."),

                Map.entry(ITERATION_CENTER_SHAPES,"Center the shapes so that the middle of the shape in each iteration has the same co-ordinates."),
                Map.entry(ITERATION_DEPLOY,"If ticked, the width of the images will be halved.\n" +
                        "This is required for the DMD to accept the images."),
                Map.entry(ITERATION_DEPLOY_TYPE,"These are the options as to how the image will be halved:\n" +
                        "Start:\t\tEvery other pixel is taken, starting from the first pixel.\n" +
                        "End:\t\t\tEvery other pixel is taken, starting from the second pixel.\n" +
                        "Average:\t\tThe average colour value of the two pixels is taken.")

        );
    }

    public static Tooltip getTooltip(String key){
        Tooltip tooltip = new Tooltip(iteration.get(key));
        tooltip.setShowDelay(new Duration(show_delay_ms));
        tooltip.setShowDuration(new Duration(show_ms));
        tooltip.setFont(new Font(textSize));
        return tooltip;
    }


}
