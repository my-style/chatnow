package Client;

/*
* Class name: Draw.java
* Author:@ Wing Yu Leung 山东大学软件工程八班 梁咏瑜
* 
* 该类利用javaFX实现画板功能
* 用户可以在画板上用鼠标绘画，重置画板，以及保存画板图片。
* 
*/

import javafx.embed.swing.SwingFXUtils;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import javafx.scene.image.WritableImage;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;



public class Draw {

    @SuppressWarnings("unchecked")
    
	public void start(Stage primaryStage) {

        StackPane root = new StackPane(); //新建面板
        
        Canvas canvas = new Canvas(400, 400 ); //新建一个canvas
        
        final GraphicsContext graphicsContext = 
                canvas.getGraphicsContext2D(); 
        //相当于画布，一个canvas只有一个graphicsContext
        
        final Button resetButton = new Button("RESET");
        final Button saveButton=new Button("SAVE");
        
        saveButton.setOnAction(ActionEvent->{
        	
        	 FileChooser fileChooser=new FileChooser();
        	 
        	 FileChooser.ExtensionFilter extFilter = 
                     new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
             fileChooser.getExtensionFilters().add(extFilter);
            
             File file= fileChooser.showSaveDialog(primaryStage);
             
             if(file!=null){
            	 
            	 try{
            		 
            		 WritableImage writableImage = new WritableImage(400,400);
                     canvas.snapshot(null, writableImage);
                     RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                     ImageIO.write(renderedImage, "png", file);
                     
            	 }catch(IOException e){
            		 e.printStackTrace();
            	 }
            	 
            	 
            	 
             }
             
        });
        
        
        
        resetButton.setOnAction(actionEvent-> {
        	
            graphicsContext.clearRect(1, 1, 500, 
            graphicsContext.getCanvas().getHeight()-2); //清除画布
          
        });
        
        resetButton.setTranslateX(15);
        saveButton.setTranslateX(30);
        saveButton.setTranslateY(5);
        resetButton.setTranslateY(5);

        // 新建颜色选择器 控制颜色选择
        
         final ColorPicker colorPicker=new ColorPicker();
         colorPicker.setValue(Color.CORAL);
         colorPicker.setOnAction((ActionEvent e)->{
        	 
                      graphicsContext.setStroke(colorPicker.getValue()); //设置当前画笔的颜色属性
         });
               
        
        colorPicker.setTranslateX(5);
        colorPicker.setTranslateY(5);

        //控制画笔大小
        
        ChoiceBox sizeChooser = new ChoiceBox(
                FXCollections.observableArrayList(
            "1", "2", "3", "4", "5"
        ));
  
        sizeChooser.getSelectionModel().selectFirst();

        sizeChooser.getSelectionModel()
                .selectedIndexProperty().addListener(
                		
                (ChangeListener)(ov, old, newval) -> {
                 Number idx = (Number)newval;

                 switch(idx.intValue()){
                   case 0: graphicsContext.setLineWidth(1);
                           break;
                   case 1: graphicsContext.setLineWidth(2);
                           break;
                   case 2: graphicsContext.setLineWidth(3);
                           break;
                   case 3: graphicsContext.setLineWidth(4);
                           break;
                   case 4: graphicsContext.setLineWidth(5);
                           break;
                   default: graphicsContext.setLineWidth(1);
                           break;
                 }
                });
        sizeChooser.setTranslateX(7);
        sizeChooser.setTranslateY(5);

        //给鼠标点击设置事件
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED,
                (MouseEvent event) -> {
            graphicsContext.beginPath();
            graphicsContext.moveTo(
                    event.getX(), event.getY() 
                    //把画笔移动到当前鼠标点击位置
            );
            graphicsContext.stroke();
        });

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                (MouseEvent event) -> {
            graphicsContext.lineTo(
                    event.getX(), event.getY()
            ); //开始画线，从画笔的当前位置绘制一条线到指定位置
            graphicsContext.stroke();
        });

        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED,
                (MouseEvent event) -> {
                    // 不作处理
        });

        HBox buttonBox = new HBox();
        
        buttonBox.getChildren().addAll(colorPicker,
                                       sizeChooser,
                                       resetButton,
                                       saveButton);

        initDraw(graphicsContext, canvas.getLayoutX(),
                canvas.getLayoutY());

        BorderPane container = new BorderPane();
        container.setTop(buttonBox);
        container.setCenter(canvas);

        root.getChildren().add(container);
        Scene scene = new Scene(root,
                      400, 450);
        primaryStage.setTitle("Drawing......");
        primaryStage.setScene(scene);
        primaryStage.show();
        
    }
    


    private void initDraw(GraphicsContext gc,
                          double x, double y){
    	
        double canvasWidth = gc.getCanvas().getWidth();
        double canvasHeight = gc.getCanvas().getHeight();


        gc.strokeRect(
                x,             
                y,             
                canvasWidth,   
                canvasHeight); 

    }
}   