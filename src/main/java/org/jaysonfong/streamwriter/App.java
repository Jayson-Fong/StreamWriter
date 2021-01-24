/*
 * The MIT License
 *
 * Copyright 2021 Jayson Fong <contact@jaysonfong.org>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jaysonfong.streamwriter;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;


/**
 * StreamWriter
 * @author Jayson Fong <contact@jaysonfong.org>
 */
public class App extends Application {
    
    private ListView<String> messageView;
    private TextField messageBox;
    private TextField portBox;
        
    @Override
    public void start(Stage primaryStage) {
        // Containers
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10, 10, 10, 10));
        HBox toolbox = new HBox(0b1111);
        
        // Prepare
        this.setMessageView();
        this.setMessageBox();
        this.setPortBox();
        
        // Vertical Box
        root.setTop(this.messageView);
        root.setCenter(toolbox);
        root.setBottom(new Text("Created by Jayson Fong 2021"));
        
        // Toolbox
        toolbox.getChildren().addAll(
            this.messageBox,
            this.portBox,
            this.getSubmitButton()
        );
        toolbox.setPadding(new Insets(10, 0, 10, 0));
        
        Scene scene = new Scene(root, 600, 500);
        
        primaryStage.setTitle("StreamWriter");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void setMessageView() {
        this.messageView = new ListView();
    }
    
    private void addMessages(String... msg) {
        try {
            Socket socket = new Socket("localhost", Integer.valueOf(this.portBox.getText()));
            this.messageView.getItems().addAll(msg);
            OutputStream output = socket.getOutputStream();
            for (String message : msg) {
                output.write(message.getBytes());
            }
            output.close();
            socket.close();
        } catch (IOException | NumberFormatException e) {
            this.messageView.getItems().add("!!! Failed to Send Message !!!");
        }
    }
    
    private void setMessageBox() {
        this.messageBox = new TextField();
        this.messageBox.promptTextProperty().set("Message");
    }
    
    private void setPortBox() {
        this.portBox = new TextField();
        this.portBox.promptTextProperty().set("Port");
    }
    
    private Button getSubmitButton() {
        Button button = new Button("Send");
        
        button.setOnMouseClicked(event -> {
            String message = this.messageBox.getText();
            this.addMessages(message);
        });
        
        return button;
    }

    public static void main(String[] args) {
        launch(args);
    }

}