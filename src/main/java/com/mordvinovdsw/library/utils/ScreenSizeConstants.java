package com.mordvinovdsw.library.utils;

import javafx.stage.Screen;
import javafx.stage.Stage;

public class ScreenSizeConstants {
    public static final double[] MainControllerSize = new double[]{1500, 1000};
    public static final double[] SupportControllerSize = new double[]{1000, 600};

    public static void adjustStageSize(Stage stage, double[] size) {
        Screen screen = Screen.getPrimary();
        double maxWidth = screen.getVisualBounds().getWidth();
        double maxHeight = screen.getVisualBounds().getHeight();
        double adjustedWidth = Math.min(size[0], maxWidth);
        double adjustedHeight = Math.min(size[1], maxHeight);
        stage.setWidth(adjustedWidth);
        stage.setHeight(adjustedHeight);
        stage.centerOnScreen();
    }
}


