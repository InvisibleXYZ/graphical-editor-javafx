import javafx.application.Application;
import javafx.stage.Stage;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;


public class app extends Application {
    Visual visual;
    @Override
    public void start(Stage stage){
        visual = new Visual();
        stage.setScene(visual.createScene());
        stage.show();
        stage.setTitle("Advanced Graphical Editor");
    }

    public static void main(String[] args) {
        launch(args);
    }
}

class Visual {
    private final ObservableList<Logic.Figure> figureList;
    private Pane drawPane;
    private TableView<Logic.Figure> tableView;
    private final SimpleIntegerProperty currentFigure;

    public Visual(){
        figureList = FXCollections.observableArrayList();
        currentFigure = new SimpleIntegerProperty(-1);
    }

    public Scene createScene(){

        drawPane = new Pane();
        drawPane.setBorder(new Border(new BorderStroke(
                Color.GRAY,
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                BorderWidths.DEFAULT
        )));
        drawPane.setPrefSize(600, 600);

        Rectangle clip = new Rectangle(drawPane.getPrefWidth(), drawPane.getPrefHeight());
        clip.widthProperty().bind(drawPane.widthProperty());
        clip.heightProperty().bind(drawPane.heightProperty());
        drawPane.setLayoutX(0);
        drawPane.setLayoutY(0);
        drawPane.setClip(clip);

        BorderPane borderPane = new BorderPane();
        borderPane.setPrefSize(1000, 700);

        tableView = new TableView<>();
        tableView.setPrefSize(250, 300);
        tableView.getSelectionModel().getSelectedIndices().addListener((ListChangeListener<? super Integer>) change -> {
            if(tableView.getSelectionModel().getSelectedIndices().size() > 0) {
                currentFigure.set(tableView.getSelectionModel().getSelectedIndices().get(0));
            }
            else {
                currentFigure.set(-1);
            }
        });

        TableColumn<Logic.Figure, String> numberColumn = new TableColumn<>("Layer");
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("layerNum"));

        TableColumn<Logic.Figure, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setPrefWidth(tableView.getPrefWidth()*0.3);

        TableColumn<Logic.Figure, Object> shapeColumn = new TableColumn<>("Shape");
        shapeColumn.setCellValueFactory(new PropertyValueFactory<>("shape"));
        shapeColumn.setPrefWidth(tableView.getPrefWidth()*0.3);
        shapeColumn.setStyle("-fx-alignment: CENTER");

        TableColumn<Logic.Figure, Object> colorColumn = new TableColumn<>("Color");
        colorColumn.setCellValueFactory(new PropertyValueFactory<>("colorObject"));
        colorColumn.setPrefWidth(tableView.getPrefWidth()*0.3);
        colorColumn.setStyle("-fx-alignment: CENTER");

        tableView.getColumns().addAll(numberColumn, nameColumn, shapeColumn, colorColumn);

        Label nameLabel = new Label("Name");

        TextField nameField = new TextField();

        Label colorLabel = new Label("Color");

        ColorPicker colorPicker = new ColorPicker();

        Label colorStrokeLabel = new Label("Color of stroke");

        ColorPicker colorStrokePicker = new ColorPicker();

        Label fillLabel = new Label("Fill");

        ToggleButton fillToggle = new ToggleButton();

        Label widthLabel = new Label("Width");

        Slider widthSlider = createSizeSlider();
        Label widthValue = createSizeValue(widthSlider);

        Label heightLabel = new Label("Height");

        Slider heightSlider = createSizeSlider();
        Label heightValue = createSizeValue(heightSlider);

        Label xLabel = new Label("X");

        Slider xSlider = new Slider();
        xSlider.setShowTickMarks(true);
        xSlider.setMin(0);
        xSlider.setMax(drawPane.getPrefWidth());

        Label xValue = new Label(""+xSlider.getValue());
        xValue.textProperty().bind(Bindings.format("%.0f",xSlider.valueProperty()));

        Label yLabel = new Label("Y");

        Slider ySlider = new Slider();
        ySlider.setShowTickMarks(true);
        ySlider.setMin(0);
        ySlider.setMax(drawPane.getHeight());

        Label yValue = new Label(""+ySlider.getValue());
        yValue.textProperty().bind(Bindings.format("%.0f",ySlider.valueProperty()));

        Label rotateLabel = new Label("Angle");

        Slider rotateSlider = new Slider();
        rotateSlider.setShowTickMarks(true);
        rotateSlider.setMin(0);
        rotateSlider.setMax(360);

        Label rotateValue = new Label(""+rotateSlider.getValue());
        rotateValue.textProperty().bind(Bindings.format("%.0f",rotateSlider.valueProperty()));

        Label numAngleLabel = new Label("Number of angles");


        ChoiceBox<Integer> numAngleChoice = new ChoiceBox<>(FXCollections.observableArrayList(3, 4, 5, 6));

        GridPane propertiesGrid = new GridPane();
        propertiesGrid.setHgap(5);
        propertiesGrid.setVgap(3);
        propertiesGrid.add(nameLabel, 1, 1);
        propertiesGrid.add(nameField, 2, 1);
        propertiesGrid.add(colorLabel, 1, 2);
        propertiesGrid.add(colorPicker, 2, 2);
        propertiesGrid.add(colorStrokeLabel, 1, 3);
        propertiesGrid.add(colorStrokePicker, 2, 3);
        propertiesGrid.add(fillLabel, 1, 4);
        propertiesGrid.add(fillToggle, 2, 4);
        propertiesGrid.add(widthLabel, 1, 5);
        propertiesGrid.add(widthSlider, 2, 5);
        propertiesGrid.add(widthValue, 3, 5);
        propertiesGrid.add(heightLabel, 1, 6);
        propertiesGrid.add(heightSlider, 2, 6);
        propertiesGrid.add(heightValue, 3, 6);
        propertiesGrid.add(xLabel, 1, 7);
        propertiesGrid.add(xSlider, 2, 7);
        propertiesGrid.add(xValue, 3, 7);
        propertiesGrid.add(yLabel, 1, 8);
        propertiesGrid.add(ySlider, 2, 8);
        propertiesGrid.add(yValue, 3, 8);
        propertiesGrid.add(rotateLabel, 1, 9);
        propertiesGrid.add(rotateSlider, 2, 9);
        propertiesGrid.add(rotateValue, 3, 9);
        propertiesGrid.add(numAngleLabel, 1, 10);
        propertiesGrid.add(numAngleChoice, 2, 10);

        for(Node e : propertiesGrid.getChildren()){
            e.setDisable(true);
        }

        VBox listPropertiesVBox = new VBox();
        listPropertiesVBox.getChildren().addAll(tableView, propertiesGrid);
        listPropertiesVBox.setPrefSize(300, 200);

        Menu addMenu = new Menu("Add");

        MenuItem rectangleItem = new MenuItem("Rectangle");
        rectangleItem.setGraphic(new Rectangle(15, 15));
        rectangleItem.setOnAction(new Logic.ClickEvent(Logic.figureEnum.RECTANGLE, drawPane, figureList, tableView));

        MenuItem circleItem = new MenuItem("Ellipse");
        circleItem.setGraphic(new Ellipse(10, 10));
        circleItem.setOnAction(new Logic.ClickEvent(Logic.figureEnum.ELLIPSE, drawPane, figureList, tableView));

        MenuItem polygonItem = new MenuItem("Polygon");
        polygonItem.setGraphic(new Polygon(
                0, 0,
                10, -15,
                20, 0
        ));
        polygonItem.setOnAction(new Logic.ClickEvent(Logic.figureEnum.POLYGON, drawPane, figureList, tableView));

        MenuItem starItem = new MenuItem("Star");
        starItem.setGraphic(new Polygon(
                0, 0,
                6, 0,
                7.5, -7.5,
                9, 0,
                15, 0,
                10, 2.5,
                12.5, 10,
                7.5, 5,
                2.5, 10,
                5, 2.5
        ));
        starItem.setOnAction(new Logic.ClickEvent(Logic.figureEnum.STAR, drawPane, figureList, tableView));

        MenuItem sandClockItem = new MenuItem("Sand Clock");
        sandClockItem.setGraphic(new Polygon(
                0, 0,
                10, 0,
                0, 10,
                10, 10
        ));
        sandClockItem.setOnAction(new Logic.ClickEvent(Logic.figureEnum.SAND_CLOCK, drawPane, figureList, tableView));

        MenuItem arrowItem = new MenuItem("Arrow");
        arrowItem.setGraphic(new Polygon(
                0, 0,
                12, 0,
                12, -4,
                20, 2,
                12, 8,
                12, 4,
                0, 4
        ));
        arrowItem.setOnAction(new Logic.ClickEvent(Logic.figureEnum.ARROW, drawPane, figureList, tableView));

        Menu removeMenu = new Menu("Remove");

        MenuItem removeCurrentItem = new MenuItem("Remove current");
        removeCurrentItem.setOnAction(actionEvent -> {
            if(currentFigure.get() != -1) {
                drawPane.getChildren().remove(drawPane.getChildren().get(currentFigure.get()));
                figureList.remove(figureList.get(currentFigure.get()));
                tableView.setItems(figureList);
                currentFigure.set(tableView.getSelectionModel().getSelectedIndex());
            }
        });

        MenuItem removeAllItem = new MenuItem("Remove all");
        removeAllItem.setOnAction(actionEvent -> {
            drawPane.getChildren().removeAll(drawPane.getChildren());
            figureList.removeAll(figureList);
            tableView.setItems(figureList);
            currentFigure.set(-1);
        });

        addMenu.getItems().addAll(rectangleItem, circleItem, polygonItem, starItem, sandClockItem, arrowItem);
        removeMenu.getItems().addAll(removeCurrentItem, removeAllItem);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(addMenu, removeMenu);

        Button moveLeftButton = new Button("Move left");
        moveLeftButton.setOnAction(actionEvent -> xSlider.setValue(xSlider.getValue()-10));

        Button moveRightButton = new Button("Move right");
        moveRightButton.setOnAction(actionEvent -> xSlider.setValue(xSlider.getValue()+10));

        Button moveUpButton = new Button("Move up");
        moveUpButton.setOnAction(actionEvent -> ySlider.setValue(ySlider.getValue()-10));

        Button moveDownButton = new Button("Move down");
        moveDownButton.setOnAction(actionEvent -> ySlider.setValue(ySlider.getValue()+10));

        Button makeBiggerButton = new Button("Make bigger");
        makeBiggerButton.setOnAction(actionEvent -> {
            widthSlider.setValue(widthSlider.getValue()+10);
            heightSlider.setValue(heightSlider.getValue()+10);
        });

        Button makeSmallerButton = new Button("Make smaller");
        makeSmallerButton.setOnAction(actionEvent -> {
            widthSlider.setValue(widthSlider.getValue()-10);
            heightSlider.setValue(heightSlider.getValue()-10);
        });

        Button rotateRightButton= new Button("Rotate Right");
        rotateRightButton.setOnAction(actionEvent -> rotateSlider.setValue(rotateSlider.getValue() + 10)
        );

        Button rotateLeftButton = new Button("Rotate Left");
        rotateLeftButton.setOnAction(actionEvent -> rotateSlider.setValue(rotateSlider.getValue() - 10)
        );

        ButtonBar.setButtonData(moveLeftButton, ButtonBar.ButtonData.LEFT);
        ButtonBar.setButtonData(moveRightButton, ButtonBar.ButtonData.LEFT);
        ButtonBar.setButtonData(moveUpButton, ButtonBar.ButtonData.LEFT);
        ButtonBar.setButtonData(moveDownButton, ButtonBar.ButtonData.LEFT);
        ButtonBar.setButtonData(makeBiggerButton, ButtonBar.ButtonData.LEFT);
        ButtonBar.setButtonData(makeSmallerButton, ButtonBar.ButtonData.LEFT);
        ButtonBar.setButtonData(rotateRightButton, ButtonBar.ButtonData.LEFT);
        ButtonBar.setButtonData(rotateLeftButton, ButtonBar.ButtonData.LEFT);

        ButtonBar buttonBar = new ButtonBar();
        buttonBar.getButtons().addAll(
                moveLeftButton, moveRightButton, moveUpButton, moveDownButton,
                makeBiggerButton, makeSmallerButton,
                rotateRightButton, rotateLeftButton);

        for(Node button : buttonBar.getButtons()){
            button.setDisable(true);
        }

        VBox selectionVBox = new VBox();
        selectionVBox.getChildren().addAll(menuBar, buttonBar);

        borderPane.setLeft(listPropertiesVBox);
        borderPane.setTop(selectionVBox);
        borderPane.setCenter(drawPane);

        Scene scene = new Scene(borderPane);

        currentFigure.addListener(new Logic.BindListener(figureList, propertiesGrid, buttonBar, drawPane));

        return scene;
    }

    public Slider createSizeSlider(){
        Slider sizeSlider = new Slider();
        sizeSlider.setShowTickMarks(true);
        sizeSlider.setMin(5);
        sizeSlider.setMax(500);

        return sizeSlider;
    }

    public Label createSizeValue(Slider sizeSlider){
        Label sizeValue = new Label(""+sizeSlider.getValue());
        sizeValue.textProperty().bind(Bindings.format("%.0f",sizeSlider.valueProperty()));

        return sizeValue;
    }
}

class Logic {
    public enum figureEnum {
        RECTANGLE, ELLIPSE, STAR, SAND_CLOCK, ARROW, POLYGON
    }

    static class ClickEvent implements EventHandler<ActionEvent> {
        private final Logic.figureEnum figure;
        private final Pane drawPane;
        private final ObservableList<Figure> figureList;
        private final TableView<Figure> tableView;

        public ClickEvent(Logic.figureEnum figure, Pane drawPane, ObservableList<Figure> figureList, TableView<Figure> tableView) {
            this.figure = figure;
            this.drawPane = drawPane;
            this.figureList = figureList;
            this.tableView = tableView;
        }

        @Override
        public void handle(ActionEvent actionEvent) {
            Pane layer = new Pane();
            layer.setPrefSize(drawPane.getWidth(),drawPane.getHeight());
            layer.prefWidthProperty().bind(drawPane.widthProperty());
            layer.prefHeightProperty().bind(drawPane.heightProperty());

            switch (figure) {
                case RECTANGLE -> figureList.add(new Figure(
                        "Rectangle",
                        new Rectangle(15, 15),
                        new Rectangle(50, 15),
                        layer,
                        drawPane.getChildren().size(),
                        Logic.figureEnum.RECTANGLE)
                );
                case ELLIPSE -> figureList.add(new Figure(
                        "Circle",
                        new Ellipse(10, 10),
                        new Ellipse(50, 50),
                        layer,
                        drawPane.getChildren().size(),
                        Logic.figureEnum.ELLIPSE)
                );
                case POLYGON -> figureList.add(new Figure(
                        "Polygon",
                        new Polygon( 0, 0, 10, -15, 20, 0),
                        new Polygon(0, 0, 50, -75, 100, 0),
                        layer,
                        drawPane.getChildren().size(),
                        Logic.figureEnum.POLYGON));
                case STAR -> figureList.add(new Figure(
                        "Star",
                        new Polygon(0, 0, 6, 0, 7.5, -7.5, 9, 0, 15, 0, 10, 2.5, 12.5, 10, 7.5, 5, 2.5, 10, 5, 2.5),
                        new Polygon(0, 0, 24, 0, 30, -30, 36, 0, 60, 0, 40, 10, 50, 40, 30, 20, 10, 40, 20, 10),
                        layer,
                        drawPane.getChildren().size(),
                        Logic.figureEnum.STAR
                ));
                case SAND_CLOCK -> figureList.add(new Figure(
                        "Sand Clock",
                        new Polygon(0, 0, 10, 0, 0, 10, 10, 10),
                        new Polygon(0, 0, 50, 0, 0, 50, 50, 50),
                        layer,
                        drawPane.getChildren().size(),
                        Logic.figureEnum.SAND_CLOCK));
                case ARROW -> figureList.add(new Figure(
                        "Arrow",
                        new Polygon(0, 0, 12, 0, 12, -4, 20, 2, 12, 8, 12, 4, 0, 4),
                        new Polygon(0, 0, 60, 0, 60, -20, 100, 10, 60, 40, 60, 20, 0, 20),
                        layer,
                        drawPane.getChildren().size(),
                        Logic.figureEnum.ARROW));
            }
            tableView.setItems(figureList);

            layer.getChildren().add(figureList.get(figureList.size()-1).getFigure());

            layer.getChildren().get(0).layoutXProperty().bind(figureList.get(figureList.size() - 1).xProperty());
            layer.getChildren().get(0).layoutYProperty().bind(figureList.get(figureList.size() - 1).yProperty());


            drawPane.getChildren().addAll(layer);

            tableView.getSelectionModel().selectLast();

        }
    }

    public static class Figure {
        private final SimpleStringProperty name;
        private final SimpleObjectProperty<Shape> shape;
        private final SimpleObjectProperty<Object> colorObject;
        private final SimpleObjectProperty<Color> color;
        private final SimpleObjectProperty<Color> colorStroke;
        private final Shape figure;
        private final SimpleBooleanProperty fill;
        private final SimpleDoubleProperty width, height;
        private final SimpleDoubleProperty x, y;
        private final SimpleDoubleProperty angle;
        private final int layerNum;
        private final Logic.figureEnum figureEnum;

        public Figure(String name, Object shape, Shape f, Pane layer, int layerNum, Logic.figureEnum figureEnum) {
            this.name = new SimpleStringProperty(name);
            this.shape = new SimpleObjectProperty<>((Shape) shape);
            this.colorObject = new SimpleObjectProperty<>(new Rectangle(50, 15));
            this.figure = f;
            this.x =  new SimpleDoubleProperty(layer.getPrefWidth()/2);
            this.y = new SimpleDoubleProperty(layer.getPrefHeight()/2);
            this.figureEnum = figureEnum;
            this.layerNum = layerNum;

            color = new SimpleObjectProperty<>(Color.BLACK);
            colorStroke = new SimpleObjectProperty<>(Color.TRANSPARENT);
            fill = new SimpleBooleanProperty(true);
            width = new SimpleDoubleProperty(50);
            height = new SimpleDoubleProperty(50);
            angle = new SimpleDoubleProperty(0);

            fill.addListener((observableValue, aBoolean, t1) -> {
                if(!fill.get()) {
                    figure.fillProperty().unbind();
                    figure.setFill(Color.TRANSPARENT);

                    ((Rectangle)colorObject.get()).fillProperty().unbind();
                    ((Rectangle)colorObject.get()).setFill(Color.TRANSPARENT);
                }
                else {
                    figure.setFill(color.get());
                    figure.fillProperty().bind(colorProperty());

                    ((Rectangle)colorObject.get()).setFill(color.get());
                    ((Rectangle)colorObject.get()).fillProperty().bind(colorProperty());
                }
            });

            figure.fillProperty().bind(colorProperty());
            figure.strokeProperty().bind(colorStrokeProperty());

            ((Rectangle)(colorObject.get())).fillProperty().bind(colorProperty());
            ((Rectangle)colorObject.get()).strokeProperty().bind(colorStrokeProperty());

            switch (figureEnum) {
                case RECTANGLE -> {
                    ((Rectangle) figure).widthProperty().bind(widthProperty());
                    ((Rectangle) figure).heightProperty().bind(heightProperty());
                }
                case ELLIPSE -> {
                    ((Ellipse) figure).radiusXProperty().bind(widthProperty());
                    ((Ellipse) figure).radiusYProperty().bind(heightProperty());
                }
                case POLYGON -> {
                    width.addListener((observableValue, number, t1) -> {
                        switch (((Polygon) figure).getPoints().size() / 2) {
                            case 3 -> {
                                ((Polygon) figure).getPoints().set(0,   -getWidth());
                                ((Polygon) figure).getPoints().set(2, 10+getWidth()/10);
                                ((Polygon) figure).getPoints().set(4, 20+getWidth());
                            }
                            case 4 -> {
                                ((Polygon) figure).getPoints().set(0,   -getWidth());
                                ((Polygon) figure).getPoints().set(2, 20+getWidth());
                                ((Polygon) figure).getPoints().set(4, 20+getWidth());
                                ((Polygon) figure).getPoints().set(6,   -getWidth());
                            }
                            case 5 -> {
                                ((Polygon) figure).getPoints().set(0, -5-getWidth());
                                ((Polygon) figure).getPoints().set(2, 10-getWidth());
                                ((Polygon) figure).getPoints().set(4, 30+getWidth());
                                ((Polygon) figure).getPoints().set(6, 45+getWidth());
                            }
                            case 6 -> {
                                ((Polygon) figure).getPoints().set(0,    -getWidth());
                                ((Polygon) figure).getPoints().set(2,  10-getWidth()/2);
                                ((Polygon) figure).getPoints().set(4,  30+getWidth()/2);
                                ((Polygon) figure).getPoints().set(6,  40+getWidth());
                                ((Polygon) figure).getPoints().set(8,  30+getWidth()/2);
                                ((Polygon) figure).getPoints().set(10, 10-getWidth()/2);
                            }
                        }

                    });
                    height.addListener((observableValue, number, t1) -> {
                        switch (((Polygon) figure).getPoints().size() / 2) {
                            case 3 -> {
                                ((Polygon) figure).getPoints().set(1, getHeight());
                                ((Polygon) figure).getPoints().set(3, -15-getHeight());
                                ((Polygon) figure).getPoints().set(5, getHeight());
                            }
                            case 4 -> {
                                ((Polygon) figure).getPoints().set(1,   -getHeight());
                                ((Polygon) figure).getPoints().set(3,   -getHeight());
                                ((Polygon) figure).getPoints().set(5, 20+getHeight());
                                ((Polygon) figure).getPoints().set(7, 20+getHeight());
                            }
                            case 5 -> {
                                ((Polygon) figure).getPoints().set(3, -20-getHeight());
                                ((Polygon) figure).getPoints().set(5, -20-getHeight());
                                ((Polygon) figure).getPoints().set(9,  15+getHeight());
                            }
                            case 6 -> {
                                ((Polygon) figure).getPoints().set(3, -15-getHeight());
                                ((Polygon) figure).getPoints().set(5, -15-getHeight());
                                ((Polygon) figure).getPoints().set(9,  15+getHeight());
                                ((Polygon) figure).getPoints().set(11, 15+getHeight());
                            }
                        }
                    });
                }
                case STAR -> {
                    width.addListener((observableValue, number, t1) -> {
                        switch (((Polygon) figure).getPoints().size() / 4) {
                            case 3 -> {
                                ((Polygon) figure).getPoints().set(0, 5 - getWidth() / 4);
                                ((Polygon) figure).getPoints().set(4, 15 + getWidth() / 4);
                                ((Polygon) figure).getPoints().set(6, 40 + getWidth());
                                ((Polygon) figure).getPoints().set(10, -20 - getWidth());
                            }
                            case 4 -> {
                                ((Polygon) figure).getPoints().set(0, -getWidth());
                                ((Polygon) figure).getPoints().set(2, 20 - getWidth() / 4);
                                ((Polygon) figure).getPoints().set(6, 40 + getWidth() / 4);
                                ((Polygon) figure).getPoints().set(8, 60 + getWidth());
                                ((Polygon) figure).getPoints().set(10, 40 + getWidth() / 4);
                                ((Polygon) figure).getPoints().set(14, 20 - getWidth() / 4);
                            }
                            case 5 -> {
                                ((Polygon) figure).getPoints().set(0, -20-getWidth());
                                ((Polygon) figure).getPoints().set(2, 24d - getWidth() / 2);
                                ((Polygon) figure).getPoints().set(4, 30d);
                                ((Polygon) figure).getPoints().set(6, 36d + getWidth() / 2);
                                ((Polygon) figure).getPoints().set(8, 80 + getWidth());
                                ((Polygon) figure).getPoints().set(10, 40d + getWidth() / 2);
                                ((Polygon) figure).getPoints().set(12, 50 + getWidth());
                                ((Polygon) figure).getPoints().set(14, 30d);
                                ((Polygon) figure).getPoints().set(16, 10 - getWidth());
                                ((Polygon) figure).getPoints().set(18, 20d - getWidth() / 2);
                            }
                            case 6 -> {
                                ((Polygon) figure).getPoints().set(0, -getWidth());
                                ((Polygon) figure).getPoints().set(2, 15-getWidth()/4);
                                ((Polygon) figure).getPoints().set(6, 25+getWidth()/4);
                                ((Polygon) figure).getPoints().set(8, 40+getWidth());
                                ((Polygon) figure).getPoints().set(10, 30+getWidth()/2);
                                ((Polygon) figure).getPoints().set(12, 40+getWidth());
                                ((Polygon) figure).getPoints().set(14, 25+getWidth()/4);
                                ((Polygon) figure).getPoints().set(18, 15-getWidth()/4);
                                ((Polygon) figure).getPoints().set(20, -getWidth());
                                ((Polygon) figure).getPoints().set(22, 10-getWidth()/2);
                            }
                        }
                    });
                    height.addListener((observableValue, number, t1) -> {
                        switch (((Polygon) figure).getPoints().size() / 4) {
                            case 3 -> {
                                ((Polygon) figure).getPoints().set(1, -getHeight() / 4);
                                ((Polygon) figure).getPoints().set(3, -30 - getHeight());
                                ((Polygon) figure).getPoints().set(5, -getHeight() / 4);
                                ((Polygon) figure).getPoints().set(7, 20 + getHeight());
                                ((Polygon) figure).getPoints().set(9, 10 + getHeight() / 2);
                                ((Polygon) figure).getPoints().set(11, 20 + getHeight());
                            }
                            case 4 -> {
                                ((Polygon) figure).getPoints().set(3, -10 - getHeight() / 4);
                                ((Polygon) figure).getPoints().set(5, -30 - getHeight());
                                ((Polygon) figure).getPoints().set(7, -10 - getHeight() / 4);
                                ((Polygon) figure).getPoints().set(11, 10 + getHeight() / 4);
                                ((Polygon) figure).getPoints().set(13, 30 + getHeight());
                                ((Polygon) figure).getPoints().set(15, 10 + getHeight() / 4);
                            }
                            case 5 -> {
                                ((Polygon) figure).getPoints().set(3, -getHeight() / 4);
                                ((Polygon) figure).getPoints().set(5, -30 - getHeight());
                                ((Polygon) figure).getPoints().set(7, -getHeight() / 4);
                                ((Polygon) figure).getPoints().set(11, 10 + getHeight() / 4);
                                ((Polygon) figure).getPoints().set(13, 40 + getHeight());
                                ((Polygon) figure).getPoints().set(15, 20 + getHeight() / 2);
                                ((Polygon) figure).getPoints().set(17, 40 + getHeight());
                                ((Polygon) figure).getPoints().set(19, 10 + getHeight() / 4);
                            }
                            case 6 -> {
                                ((Polygon) figure).getPoints().set(1, -getHeight()/2);
                                ((Polygon) figure).getPoints().set(3, -getHeight()/2);
                                ((Polygon) figure).getPoints().set(5, -15-getHeight());
                                ((Polygon) figure).getPoints().set(7, -getHeight()/2);
                                ((Polygon) figure).getPoints().set(9, -getHeight()/2);
                                ((Polygon) figure).getPoints().set(13, 20+getHeight()/2);
                                ((Polygon) figure).getPoints().set(15, 20+getHeight()/2);
                                ((Polygon) figure).getPoints().set(17, 35+getHeight());
                                ((Polygon) figure).getPoints().set(19, 20+getHeight()/2);
                                ((Polygon) figure).getPoints().set(21, 20+getHeight()/2);
                            }
                        }
                    });
                }
                case SAND_CLOCK -> {
                    width.addListener((observableValue, number, t1) -> {
                        ((Polygon) figure).getPoints().set(0, -getWidth());
                        ((Polygon) figure).getPoints().set(2, getWidth());
                        ((Polygon) figure).getPoints().set(4, -getWidth());
                        ((Polygon) figure).getPoints().set(6, getWidth());
                    });
                    height.addListener((observableValue, number, t1) -> {
                        ((Polygon) figure).getPoints().set(1, -getHeight());
                        ((Polygon) figure).getPoints().set(3, -getHeight());
                        ((Polygon) figure).getPoints().set(5, getHeight());
                        ((Polygon) figure).getPoints().set(7, getHeight());
                    });
                }
                case ARROW -> {
                    width.addListener((observableValue, number, t1) -> {
                        ((Polygon) figure).getPoints().set(0, -getWidth());
                        ((Polygon) figure).getPoints().set(2, getWidth() / 2);
                        ((Polygon) figure).getPoints().set(4, getWidth() / 2);
                        ((Polygon) figure).getPoints().set(6, getWidth());
                        ((Polygon) figure).getPoints().set(8, getWidth() / 2);
                        ((Polygon) figure).getPoints().set(10, getWidth() / 2);
                        ((Polygon) figure).getPoints().set(12, -getWidth());
                    });
                    height.addListener((observableValue, number, t1) -> {
                        ((Polygon) figure).getPoints().set(1, -getHeight() / 2);
                        ((Polygon) figure).getPoints().set(3, -getHeight() / 2);
                        ((Polygon) figure).getPoints().set(5, -getHeight());
                        ((Polygon) figure).getPoints().set(7, 0d);
                        ((Polygon) figure).getPoints().set(9, getHeight());
                        ((Polygon) figure).getPoints().set(11, getHeight() / 2);
                        ((Polygon) figure).getPoints().set(13, getHeight() / 2);
                    });
                }
            }

            figure.rotateProperty().bind(angleProperty());
        }

        public String getName() {
            return name.get();
        }

        public SimpleStringProperty nameProperty() {
            return name;
        }

        public Object getColor() {
            return color.get();
        }

        public SimpleObjectProperty<Color> colorProperty() {
            return color;
        }

        public Shape getFigure() {
            return figure;
        }

        public boolean isFill() {
            return fill.get();
        }

        public SimpleBooleanProperty fillProperty() {
            return fill;
        }

        public double getWidth() {
            return width.get();
        }

        public SimpleDoubleProperty widthProperty() {
            return width;
        }

        public double getHeight() {
            return height.get();
        }

        public SimpleDoubleProperty heightProperty() {
            return height;
        }

        public double getX() {
            return x.get();
        }

        public SimpleDoubleProperty xProperty() {

            return x;
        }

        public double getY() {
            return y.get();
        }

        public SimpleDoubleProperty yProperty() {

            return y;
        }

        public double getAngle() {
            return angle.get();
        }

        public SimpleDoubleProperty angleProperty() {
            return angle;
        }

        public Logic.figureEnum getFigureEnum() {
            return figureEnum;
        }

        public Object getColorStroke() {
            return colorStroke.get();
        }

        public SimpleObjectProperty<Color> colorStrokeProperty() {
            return colorStroke;
        }

        public void setColor(Color color) {
            this.color.set(color);
        }

        //Used for TableView
        public Shape getShape() {
            return shape.get();
        }

        public SimpleObjectProperty<Shape> shapeProperty() {
            return shape;
        }

        public Object getColorObject() {
            return colorObject.get();
        }

        public SimpleObjectProperty<Object> colorObjectProperty() {
            return colorObject;
        }

        public int getLayerNum() {
            return layerNum;
        }
    }

    static class BindListener implements ChangeListener<Number>{
        private final ObservableList<Figure> figureList;
        private final GridPane propertiesGrid;
        private final ButtonBar buttonBar;
        private final Pane drawPane;
        private ChangeListener<Integer> changeListener;

        private final TextField nameField;
        private final ColorPicker colorPicker;
        private final ColorPicker colorStrokePicker;
        private final ToggleButton fillToggle;
        private final Slider widthSlider;
        private final Slider heightSlider;
        private final Slider xSlider;
        private final Slider ySlider;
        private final Slider rotateSlider;
        private final Label numAngleLabel;
        private final ChoiceBox<Integer> numAngleChoice;

        public BindListener(ObservableList<Figure> figureList, GridPane propertiesGrid, ButtonBar buttonBar, Pane drawPane) {
            this.figureList = figureList;
            this.propertiesGrid = propertiesGrid;
            this.buttonBar = buttonBar;
            this.drawPane = drawPane;

            nameField =         (TextField)          this.propertiesGrid.getChildren().get(1);
            colorPicker =       (ColorPicker)        this.propertiesGrid.getChildren().get(3);
            colorStrokePicker = (ColorPicker)        this.propertiesGrid.getChildren().get(5);
            fillToggle =        (ToggleButton)       this.propertiesGrid.getChildren().get(7);
            widthSlider =       (Slider)             this.propertiesGrid.getChildren().get(9);
            heightSlider =      (Slider)             this.propertiesGrid.getChildren().get(12);
            xSlider =           (Slider)             this.propertiesGrid.getChildren().get(15);
            ySlider =           (Slider)             this.propertiesGrid.getChildren().get(18);
            rotateSlider =      (Slider)             this.propertiesGrid.getChildren().get(21);
            numAngleLabel =     (Label)              this.propertiesGrid.getChildren().get(23);
            numAngleChoice =    (ChoiceBox<Integer>) this.propertiesGrid.getChildren().get(24);

            this.changeListener = new AngleListener(figureList, numAngleChoice, -1);
        }

        @Override
        public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
            numAngleChoice.valueProperty().removeListener(changeListener);
            if(t1.intValue() != -1){
                for (Logic.Figure figure : figureList) {
                    figure.nameProperty().unbind();
                    figure.colorProperty().unbind();
                    figure.colorStrokeProperty().unbind();
                    figure.fillProperty().unbind();
                    figure.widthProperty().unbind();
                    figure.heightProperty().unbind();
                    figure.xProperty().unbind();
                    figure.yProperty().unbind();
                    figure.angleProperty().unbind();
                }

                for(Node e : propertiesGrid.getChildren()){
                    e.setDisable(false);
                }

                numAngleLabel.setDisable(true);
                numAngleChoice.setDisable(true);

                for(Node button : buttonBar.getButtons()){
                    button.setDisable(false);
                }

                nameField.setText(figureList.get(t1.intValue()).getName());
                figureList.get(t1.intValue()).nameProperty().bind(nameField.textProperty());

                colorPicker.setValue((Color) figureList.get(t1.intValue()).getColor());
                figureList.get(t1.intValue()).colorProperty().bind(colorPicker.valueProperty());

                colorStrokePicker.setValue((Color) figureList.get(t1.intValue()).getColorStroke());
                figureList.get(t1.intValue()).colorStrokeProperty().bind(colorStrokePicker.valueProperty());

                fillToggle.setSelected(figureList.get(t1.intValue()).isFill());
                figureList.get(t1.intValue()).fillProperty().bind(fillToggle.selectedProperty());

                widthSlider.setValue(figureList.get(t1.intValue()).getWidth());
                figureList.get(t1.intValue()).widthProperty().bind(widthSlider.valueProperty());

                heightSlider.setValue(figureList.get(t1.intValue()).getHeight());
                figureList.get(t1.intValue()).heightProperty().bind(heightSlider.valueProperty());

                xSlider.maxProperty().bind(drawPane.widthProperty().subtract(figureList.get(t1.intValue()).widthProperty()));

                xSlider.setValue(figureList.get(t1.intValue()).getX());
                figureList.get(t1.intValue()).xProperty().bind(xSlider.valueProperty());

                ySlider.maxProperty().bind(drawPane.heightProperty().subtract(figureList.get(t1.intValue()).heightProperty()));

                ySlider.setValue(figureList.get(t1.intValue()).getY());
                figureList.get(t1.intValue()).yProperty().bind(ySlider.valueProperty());

                rotateSlider.setValue(figureList.get(t1.intValue()).getAngle());
                figureList.get(t1.intValue()).angleProperty().bind(rotateSlider.valueProperty());

                if(figureList.get(t1.intValue()).getFigureEnum() == Logic.figureEnum.POLYGON ||
                        figureList.get(t1.intValue()).getFigureEnum() == Logic.figureEnum.STAR ){
                    numAngleLabel.setDisable(false);
                    numAngleChoice.setDisable(false);

                    switch (figureList.get(t1.intValue()).getFigureEnum()){
                        case POLYGON -> numAngleChoice.setValue(((((Polygon) figureList.get(t1.intValue()).getFigure()).getPoints().size() / 2)));
                        case STAR -> numAngleChoice.setValue(((((Polygon) figureList.get(t1.intValue()).getFigure()).getPoints().size() / 4)));
                    }
                    changeListener = new AngleListener(figureList, numAngleChoice, t1.intValue());
                    numAngleChoice.valueProperty().addListener(changeListener);
                }
            }
            else{
                for(Node e : propertiesGrid.getChildren()){
                    e.setDisable(true);
                }

                for(Node button : buttonBar.getButtons()){
                    button.setDisable(true);
                }
            }
        }
    }

    static class AngleListener implements ChangeListener<Integer>{
        private final ObservableList<Figure> figureList;
        private final ChoiceBox<Integer> numAngleChoice;
        private final SimpleIntegerProperty current;

        public AngleListener(ObservableList<Figure> figureList, ChoiceBox<Integer> numAngleChoice, int current) {
            this.figureList = figureList;
            this.numAngleChoice = numAngleChoice;
            this.current = new SimpleIntegerProperty(current);
        }

        @Override
        public void changed(ObservableValue<? extends Integer> observableValue, Integer integer, Integer t1) {
            ObservableList<Double> tmp = FXCollections.observableArrayList();
            double width = figureList.get(current.get()).getWidth();
            double height = figureList.get(current.get()).getHeight();
            switch (figureList.get(current.get()).getFigureEnum()) {
                case POLYGON :
                    switch (numAngleChoice.getValue()) {
                        case 3 -> tmp.addAll(
                                0d-width, 0d+height, //0
                                10d+width/10,  -15d-height, //1
                                20d+width,    0d+height  //2
                        );
                        case 4 -> tmp.addAll(
                                0d-width, 0d-height, //0
                                20d+width,     0d-height, //1
                                20d+width,    20d+height, //2
                                0d-width,     20d+height  //3
                        );
                        case 5 -> tmp.addAll(
                                -5d-width, 0d,         //0
                                10d-width,    -20d-height, //1
                                30d+width,    -20d-height, //2
                                40d+width,      0d,        //3
                                20d,           15d+height  //4
                        );
                        case 6 -> tmp.addAll(
                                0d-width, 0d,        //0
                                10d-width/2, -15d-height, //1
                                30d+width/2, -15d-height, //2
                                40d+width,     0d,        //3
                                30d+width/2,  15d+height, //4
                                10d-width/2,  15d+height  //5
                        );
                    }
                    break;
                case STAR :
                    switch (numAngleChoice.getValue()) {
                        case 3 -> tmp.addAll(
                                5 - width / 4, - height / 4,  //0
                                10d,          -30d - height,      //1
                                15d + width / 4,   - height / 4,  //2
                                40d + width,   20d + height,      //3
                                10d,           10d + height / 2,  //4
                                -20d - width,  20d + height       //5
                        );
                        case 4 -> tmp.addAll(
                                -20-width,           0d,            //0
                                20d - width / 4, -10d - height / 4,  //1
                                30d, -30d             - height,      //2
                                40d + width / 4, -10d - height / 4,  //3
                                80d + width,          0d,            //4
                                40d + width / 4,  10d + height / 4,  //5
                                30d,              30d + height,      //6
                                20d - width / 4,  10d + height / 4   //7
                        );
                        case 5 -> tmp.addAll(
                                -width,       0d,              //0
                                24d - width / 2, -height / 4,      //1
                                30d,             -height - 30,     //2
                                36d + width / 2, -height / 4,      //3
                                width + 60,       0d,              //4
                                40d + width / 2,  height / 4 + 10, //5
                                width + 50,       height + 40,     //6
                                30d,              height / 2 + 20, //7
                                10 - width,       height + 40,     //8
                                20d - width / 2,  height / 4 + 10  //9
                        );
                        case 6 -> tmp.addAll(
                                0d-width, 0d-height/2, //0
                                15d-width/4,  0d-height/2, //1
                                20d,        -15d-height,   //2
                                25d+width/4,  0d-height/2, //3
                                40d+width,    0d-height/2, //4
                                30d+width/2, 10d,          //5
                                40d+width,   20d+height/2, //6
                                25d+width/4, 20d+height/2, //7
                                20d,         35d+height,   //8
                                15d-width/4, 20d+height/2, //9
                                0d-width,    20d+height/2, //10
                                10d-width/2, 10d           //11
                        );
                    }
                    break;
            }
            ((Polygon) figureList.get(current.get()).getFigure()).getPoints().removeAll(
                    ((Polygon) figureList.get(current.get()).getFigure()).getPoints()
            );
            ((Polygon) figureList.get(current.get()).getFigure()).getPoints().addAll(tmp);

        }
    }
}

