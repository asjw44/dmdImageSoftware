package sample.Util;

import sample.Model.Canvas;
import sample.Model.Shapes.AbstractShape;

import java.util.ArrayList;

public class FourCoreIteratorController {

    CoreController topLeft;
    CoreController topRight;
    CoreController bottomLeft;
    CoreController bottomRight;

    public void setTopLeft(CoreController topLeft) {
        this.topLeft = topLeft;
    }

    public void setTopRight(CoreController topRight) {
        this.topRight = topRight;
    }

    public void setBottomLeft(CoreController bottomLeft) {
        this.bottomLeft = bottomLeft;
    }

    public void setBottomRight(CoreController bottomRight) {
        this.bottomRight = bottomRight;
    }

    public void write(){
        if(topLeft == null){
            topLeft = blank();
        }if(topRight == null){
            topRight = blank();
        }if(bottomLeft == null){
            bottomLeft = blank();
        }if(bottomRight == null){
            bottomRight = blank();
        }

        ArrayList<CoreController> coreControllers = new ArrayList<>();
        coreControllers.add(topLeft);
        coreControllers.add(topRight);
        coreControllers.add(bottomLeft);
        coreControllers.add(bottomRight);

        int totalNumCanvases = 0;
        for(CoreController controller : coreControllers){
            int finishCanvasNumber = controller.getStartNumber() + (controller.getIterator() == null ? 0 : controller.getIterator().getCanvases().size());
            if(finishCanvasNumber>totalNumCanvases){
                totalNumCanvases = finishCanvasNumber;
            }
        }



    }



    public static CoreController blank() {
        return new CoreController() {
            @Override
            public Canvas getStartState() {
                return null;
            }

            @Override
            public int getStartNumber() {
                return 0;
            }

            @Override
            public Iterator getIterator() {
                return null;
            }

            @Override
            public Canvas getEndState() {
                return null;
            }
        };
    }

    public static CoreController shape(AbstractShape s, Canvas canvas){
        return new CoreController() {
            @Override
            public Canvas getStartState() {
                Canvas c = new Canvas(canvas.getImageName(), canvas.getWidth(), canvas.getHeight());
                c.addShape(s);
                c.drawShapes();
                return c;
            }

            @Override
            public int getStartNumber() {
                return 0;
            }

            @Override
            public Iterator getIterator() {
                return null;
            }

            @Override
            public Canvas getEndState() {
                return null;
            }
        };
    }

}
