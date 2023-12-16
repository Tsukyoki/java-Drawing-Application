// File name: DrawingApplication
// Written by: Kyle Nguyen
// Description: A drawing application that allows users to specify drawing characteristics, choosing the desired shape, stroke color, fill color, hollow or filled, and option to undo/clear


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import java.util.Stack;
import static javafx.application.Application.launch;
import javafx.scene.control.Button;

public class DrawingApplication extends Application {

    // Variables and default parameters
    private double startX, startY; // Initial mouse position
    private String selectedShape = "Circle"; // Default shape selection
    private Color fillColor = Color.BLACK; // Default fill color selection
    private Color strokeColor = Color.BLACK; // Default stroke color selection
    private boolean isFilled = true; // Indicates if shape should be filled or not
    private Color backgroundColor = Color.WHITE; // Default background color
    private double lineWidth = 4.0; // Default line width
    private Stack<Shape> shapesDrawn = new Stack<>(); // Stack to store drawn shapes
    private ComboBox<String> shapeComboBox;


    
    @Override
    public void start(Stage primaryStage) {
        
        
        // Creating layout components
        BorderPane root = new BorderPane();
        VBox controlBox = new VBox(); // Contains controls        
        VBox backgroundColorBox = new VBox();
        VBox penSize = new VBox();
        Pane drawingArea = new Pane(); // Drawing canvas
        TextArea infoTextArea = new TextArea(); // Information display area
        drawingArea.setStyle("-fx-background-color: white;"); // Setting canvas background color


        // GUI components
        
        // Combo box that selects a shape
        ComboBox<String> shapeComboBox = new ComboBox<>();
        shapeComboBox.getItems().addAll("Circle", "Oval", "Rectangle","Square","Line");
        shapeComboBox.setValue(selectedShape);
        
        // Color pickers for stroke and fill colors
        ColorPicker fillPicker = new ColorPicker();
        fillPicker.setValue(fillColor); // Set default fill color
        ColorPicker strokePicker = new ColorPicker();
        strokePicker.setValue(strokeColor); // Set default stroke color
        
        // Checkbox to specify if a shape should be filled
        CheckBox fillCheckBox = new CheckBox("Fill Shape");
        fillCheckBox.setSelected(isFilled); // Set default value
        
        // Radio buttons to determine the Drawing boards's background color
        ToggleGroup backgroundToggleGroup = new ToggleGroup();
        RadioButton whiteRadioButton = new RadioButton("White");
        whiteRadioButton.setToggleGroup(backgroundToggleGroup);
        whiteRadioButton.setSelected(true); // Default background color
        RadioButton lightGrayRadioButton = new RadioButton("Light Gray");
        lightGrayRadioButton.setToggleGroup(backgroundToggleGroup);
        RadioButton lightYellowRadioButton = new RadioButton("Light Yellow");
        lightYellowRadioButton.setToggleGroup(backgroundToggleGroup);
        RadioButton lightBlueRadioButton = new RadioButton("Light Blue");
        lightBlueRadioButton.setToggleGroup(backgroundToggleGroup);
        RadioButton lightGreenRadioButton = new RadioButton("Light Green");
        lightGreenRadioButton.setToggleGroup(backgroundToggleGroup);
        
        // Radio buttons to determine the Pen Size (with the default value being medium 4)
        ToggleGroup lineWidthToggleGroup = new ToggleGroup();
        RadioButton smallWidthRadioButton = new RadioButton("Small (2)");
        smallWidthRadioButton.setToggleGroup(lineWidthToggleGroup);
        smallWidthRadioButton.setUserData(2.0);
        RadioButton mediumWidthRadioButton = new RadioButton("Medium (4)");
        mediumWidthRadioButton.setToggleGroup(lineWidthToggleGroup);
        mediumWidthRadioButton.setUserData(4.0);
        mediumWidthRadioButton.setSelected(true); // Default line width
        RadioButton largeWidthRadioButton = new RadioButton("Large (6)");
        largeWidthRadioButton.setToggleGroup(lineWidthToggleGroup);
        largeWidthRadioButton.setUserData(6.0);
        
        // Buttons to allow the user to undo the previous drawing, clear the canvas, and exit the program
        Button undoButton = new Button("Undo");
        Button clearButton = new Button("Clear");       
        Button exitButton = new Button("Exit");

        
              
        // Events handlers
        
        // Checks the value of whatever option is selected then returns the value
        shapeComboBox.setOnAction(event -> {
            selectedShape = shapeComboBox.getValue();
        });
        fillPicker.setOnAction(event -> {
            fillColor = fillPicker.getValue();
        });
        strokePicker.setOnAction(event -> {
            strokeColor = strokePicker.getValue();
        });
        fillCheckBox.setOnAction(event -> {
            isFilled = fillCheckBox.isSelected();
        });
        
        // If the button is selected, it will set the background color to the selected option
        whiteRadioButton.setOnAction(event -> {
            backgroundColor = Color.WHITE;
            drawingArea.setStyle("-fx-background-color: white;");
        });
        lightGrayRadioButton.setOnAction(event -> {
            backgroundColor = Color.LIGHTGRAY;
            drawingArea.setStyle("-fx-background-color: lightgray;");
        });        
        lightYellowRadioButton.setOnAction(event -> {
            backgroundColor = Color.YELLOW;
            drawingArea.setStyle("-fx-background-color: yellow;");
        });
        lightBlueRadioButton.setOnAction(event -> {
            backgroundColor = Color.LIGHTBLUE;
            drawingArea.setStyle("-fx-background-color: lightblue;");
        });
        lightGreenRadioButton.setOnAction(event -> {
            backgroundColor = Color.LIGHTGREEN;
            drawingArea.setStyle("-fx-background-color: lightgreen;");
        });        
        
        // undo, clear, and exit event handlers
        undoButton.setOnAction(event -> {
            // checks if there is at least one shape
            if (!shapesDrawn.isEmpty()) { 
                // retrives the lists of shapes drawn then removes the most recent shape created
                drawingArea.getChildren().remove(shapesDrawn.pop()); 
            } else { // handle exception if there is nothing to undo
                infoTextArea.appendText("\nNothing to undo");
            }
        });
        // when clear button is pressed, it will clear all the shapes drawn
        clearButton.setOnAction(event -> {
            drawingArea.getChildren().clear();
            shapesDrawn.clear();
        });
        // when exit button is pressed, it will close the program
        exitButton.setOnAction(event -> {
            primaryStage.close();
        });
        
        // Calculate the Shape's area and perimeter
        drawingArea.setOnMouseClicked(event -> {
            // finds the shape at the position clicked; iterates through the "shapesDrawn" to find the shape at the clicked position, then assigns the shape to the shape variable
            Shape shape = null;
            for (Shape s : shapesDrawn) {
                if (s.contains(event.getX(), event.getY())) {
                    shape = s;
                    break;
                }
            }
            // if returned a shape, it will calculate the shape's area and perimeter
            if (shape != null) {
                // default values
                double area = 0.0;
                double perimeter = 0.0;
                // Checks what shape it returned then calculates their respective area/perimeter.
                if (shape instanceof Circle) {
                    Circle circle = (Circle) shape;
                    area = Math.PI * circle.getRadius() * circle.getRadius();
                    perimeter = 2 * Math.PI * circle.getRadius();
                } else if (shape instanceof Ellipse) {
                    Ellipse ellipse = (Ellipse) shape;
                    area = Math.PI * ellipse.getRadiusX() * ellipse.getRadiusY();
                    perimeter = 2 * Math.PI * Math.sqrt((ellipse.getRadiusX() * ellipse.getRadiusX() + ellipse.getRadiusY() * ellipse.getRadiusY()) / 2);
                } else if (shape instanceof Rectangle) {
                    Rectangle rectangle = (Rectangle) shape;
                    area = rectangle.getWidth() * rectangle.getHeight();
                    perimeter = 2 * (rectangle.getWidth() + rectangle.getHeight());
                }
                // displays the calculated area and perimeter of the shape formatted into 2 decimal places
                infoTextArea.appendText("\nShape Area: " + String.format("%.2f", area));
                infoTextArea.appendText("\nShape Perimeter: " + String.format("%.2f", perimeter));
            }
        });
        
        // Mouse position
        drawingArea.setOnMouseMoved(event -> {
            double mouseX = event.getX();
            double mouseY = event.getY();
            infoTextArea.setText("Mouse position = [" + mouseX + ", " + mouseY + "]");
        });
    
        // Taking initial/ending mouse X and Y values to create each shape
        drawingArea.setOnMousePressed(event -> {
            startX = event.getX();
            startY = event.getY();
        });
        drawingArea.setOnMouseReleased(event -> {
            double endX = event.getX();
            double endY = event.getY();
            Shape shape;
            
            // Checks which shape is selected and draws the shape to the canvas
            if (selectedShape.equals("Circle")) {
                double diameter = Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2));
                double centerX = (startX + endX) / 2;
                double centerY = (startY + endY) / 2;
                double radius = diameter / 2;
                // find the radius by using distance formula and then finding the center position of the circle by finding the midpoint of the X and Y position
                if (startX <= 0 || startY <= 0 || endX <= 0 || endY <= 0) {
                    // exception handle for invalid shape circle
                    infoTextArea.appendText("\nInvalid dimensions for Circle");
                    return; 
                }
                shape = new Circle(centerX, centerY, radius);
            } else if (selectedShape.equals("Oval")) {
                double centerX = (startX + endX) / 2;
                double centerY = (startY + endY) / 2;
                double radiusX = Math.abs(endX - startX) / 2;
                double radiusY = Math.abs(endY - startY) / 2;
                
                if (startX <= 0 || startY <= 0 || endX <= 0 || endY <= 0) {
                    // exception handle for invalid oval
                    infoTextArea.appendText("\nInvalid dimensions for Oval");
                    return; 
                }

                shape = new Ellipse(centerX, centerY, radiusX, radiusY);
            } else if (selectedShape.equals("Rectangle")) {
                double width = Math.abs(endX - startX);
                double height = Math.abs(endY - startY);
                if (startX <= 0 || startY <= 0 || endX <= 0 || endY <= 0) {
                    // exception handle for invalid rectangle
                    infoTextArea.appendText("\nInvalid dimensions for rectangle");
                    return; 
                }

                shape = new Rectangle(startX, startY, width, height);
            } else if (selectedShape.equals("Line")) {               
                if (startX <= 0 || startY <= 0 || endX <= 0 || endY <= 0) {
                    // exception handle for invalid line
                    infoTextArea.appendText("\nInvalid dimensions for Line");
                    return; 
                }
                shape = new Line(startX, startY, event.getX(), event.getY());
            } else { // create square 
                double side = Math.min(Math.abs(endX - startX), Math.abs(endY - startY));
                double minX = Math.min(startX, endX);
                double minY = Math.min(startY, endY);
                if (startX <= 0 || startY <= 0 || endX <= 0 || endY <= 0) {
                    // exception handle for invalid shape creation
                    infoTextArea.appendText("Invalid dimensions for square");
                    return; 
                }

                shape = new Rectangle(minX, minY, side, side);
            }
            // checks if isFilled is checked off, if true then it will fill the color, if not it wont fill
            shape.setFill(isFilled ? fillColor : Color.TRANSPARENT);
            // Set stroke color
            shape.setStroke(strokeColor); 
            // Set line width
            shape.setStrokeWidth(lineWidth); 
            // pushes the drawn shape to the top of the stack and then draws the shape into the canvas
            shapesDrawn.push(shape);
            drawingArea.getChildren().add(shape);
        });
        
        // configuring infoTextArea
        infoTextArea.setEditable(false);
        infoTextArea.setPrefHeight(100); 
        
        // adding the GUI
        controlBox.getChildren().addAll(new Label("Shape:"),shapeComboBox,new Label("Fill Color:"), fillPicker, new Label("Stroke Color:"), strokePicker, fillCheckBox, undoButton, clearButton);
        backgroundColorBox.getChildren().addAll(new Label("Background Color:"), whiteRadioButton, lightGrayRadioButton,lightYellowRadioButton,lightBlueRadioButton,lightGreenRadioButton, exitButton);
        penSize.getChildren().addAll(new Label("Pen Size:"), smallWidthRadioButton, mediumWidthRadioButton, largeWidthRadioButton);
        
        // Added the GUI components together
        VBox leftGUI = new VBox();
        leftGUI.getChildren().addAll(controlBox, penSize,backgroundColorBox,infoTextArea);

        // Setting components to the root BorderPane
        root.setCenter(drawingArea);
        root.setLeft(leftGUI);


        // Create the scene and set it in the stage
        Scene scene = new Scene(root, 1800, 900);
        primaryStage.setTitle("Shape Drawing Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
