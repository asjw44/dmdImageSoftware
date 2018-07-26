package sample.Model;

import sample.Model.Shapes.AbstractShape;

import java.util.ArrayList;

public class SaveData {

    private String baseName;

    private int canvasSizeWidth;
    private int canvasSizeHeight;

    private boolean useDMDCheckbox;

    private ArrayList<AbstractShape> staticShapes = new ArrayList<>();

    private String shape;

    private int shapeLocX;
    private int shapeLocY;
    private int shapeSizeWidth;
    private int shapeSizeHeight;

    private int doughnutOffset;

    private int numOfImages;
    private int translateX;
    private int translateY;
    private int dWidth;
    private int dHeight;

    private boolean translateXCheckbox;
    private boolean translateYCheckbox;
    private boolean dWidthCheckbox;
    private boolean dHeightCheckbox;

    private boolean centerShapes;
    private boolean deployDMDCheckbox;
    private String rescaleTypePicker;

    private String imagePath;

    public SaveData(){}

    public String getId() {
        return "0.1.1";
    }

    public String getBaseName() {
        return baseName;
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    public int getCanvasSizeWidth() {
        return canvasSizeWidth;
    }

    public void setCanvasSizeWidth(int canvasSizeWidth) {
        this.canvasSizeWidth = canvasSizeWidth;
    }

    public int getCanvasSizeHeight() {
        return canvasSizeHeight;
    }

    public void setCanvasSizeHeight(int canvasSizeHeight) {
        this.canvasSizeHeight = canvasSizeHeight;
    }

    public boolean isUseDMDCheckbox() {
        return useDMDCheckbox;
    }

    public void setUseDMDCheckbox(boolean useDMDCheckbox) {
        this.useDMDCheckbox = useDMDCheckbox;
    }

    public ArrayList<AbstractShape> getStaticShapes() {
        return staticShapes;
    }

    public void setStaticShapes(ArrayList<AbstractShape> staticShapes) {
        this.staticShapes = staticShapes;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public int getShapeLocX() {
        return shapeLocX;
    }

    public void setShapeLocX(int shapeLocX) {
        this.shapeLocX = shapeLocX;
    }

    public int getShapeLocY() {
        return shapeLocY;
    }

    public void setShapeLocY(int shapeLocY) {
        this.shapeLocY = shapeLocY;
    }

    public int getShapeSizeWidth() {
        return shapeSizeWidth;
    }

    public void setShapeSizeWidth(int shapeSizeWidth) {
        this.shapeSizeWidth = shapeSizeWidth;
    }

    public int getShapeSizeHeight() {
        return shapeSizeHeight;
    }

    public void setShapeSizeHeight(int shapeSizeHeight) {
        this.shapeSizeHeight = shapeSizeHeight;
    }

    public int getDoughnutOffset() {
        return doughnutOffset;
    }

    public void setDoughnutOffset(int doughnutOffset) {
        this.doughnutOffset = doughnutOffset;
    }

    public int getNumOfImages() {
        return numOfImages;
    }

    public void setNumOfImages(int numOfImages) {
        this.numOfImages = numOfImages;
    }

    public int getTranslateX() {
        return translateX;
    }

    public void setTranslateX(int translateX) {
        this.translateX = translateX;
    }

    public int getTranslateY() {
        return translateY;
    }

    public void setTranslateY(int translateY) {
        this.translateY = translateY;
    }

    public int getdWidth() {
        return dWidth;
    }

    public void setdWidth(int dWidth) {
        this.dWidth = dWidth;
    }

    public int getdHeight() {
        return dHeight;
    }

    public void setdHeight(int dHeight) {
        this.dHeight = dHeight;
    }

    public boolean isTranslateXCheckbox() {
        return translateXCheckbox;
    }

    public void setTranslateXCheckbox(boolean translateXCheckbox) {
        this.translateXCheckbox = translateXCheckbox;
    }

    public boolean isTranslateYCheckbox() {
        return translateYCheckbox;
    }

    public void setTranslateYCheckbox(boolean translateYCheckbox) {
        this.translateYCheckbox = translateYCheckbox;
    }

    public boolean isdWidthCheckbox() {
        return dWidthCheckbox;
    }

    public void setdWidthCheckbox(boolean dWidthCheckbox) {
        this.dWidthCheckbox = dWidthCheckbox;
    }

    public boolean isdHeightCheckbox() {
        return dHeightCheckbox;
    }

    public void setdHeightCheckbox(boolean dHeightCheckbox) {
        this.dHeightCheckbox = dHeightCheckbox;
    }

    public boolean isCenterShapes() {
        return centerShapes;
    }

    public void setCenterShapes(boolean centerShapes) {
        this.centerShapes = centerShapes;
    }

    public boolean isDeployDMDCheckbox() {
        return deployDMDCheckbox;
    }

    public void setDeployDMDCheckbox(boolean deployDMDCheckbox) {
        this.deployDMDCheckbox = deployDMDCheckbox;
    }

    public String getRescaleTypePicker() {
        return rescaleTypePicker;
    }

    public void setRescaleTypePicker(String rescaleTypePicker) {
        this.rescaleTypePicker = rescaleTypePicker;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
