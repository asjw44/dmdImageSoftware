package sample.Util;

import sample.Model.Canvas;

public interface CoreController {

    Canvas getStartState();

    int getStartNumber();

    Iterator getIterator();

    Canvas getEndState();

}
