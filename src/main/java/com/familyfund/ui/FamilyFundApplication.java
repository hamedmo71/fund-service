package com.familyfund.ui;

import com.familyfund.infrastructure.configuration.SpringConfig;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.net.URL;

public class FamilyFundApplication extends Application {

    private ApplicationContext springContext;

    @Override
    public void init() {

        springContext =
                new AnnotationConfigApplicationContext(
                        SpringConfig.class
                );
    }

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader();

        URL fxmlLocation =
                getClass()
                        .getResource("/com/familyfund/ui/MainView.fxml");

        loader.setLocation(fxmlLocation);

        loader.setControllerFactory(
                springContext::getBean
        );

        Parent root = loader.load();

        Scene scene = new Scene(root, 800, 600);

        stage.setTitle("Family Fund");

        stage.setScene(scene);

        stage.show();
    }

    @Override
    public void stop() {

        if (springContext instanceof AnnotationConfigApplicationContext context) {
            context.close();
        }
    }

    public static void main(String[] args) {

        launch(args);
    }
}