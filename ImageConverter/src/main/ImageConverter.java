package main;

import java.io.File;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ImageConverter extends Application{
	
	Stage primaryStage;
	
	String select = "Original";
	
	Scene scene;
	BorderPane bp;
	HBox buttonsbox, imagebox;
	VBox container;
	
	ComboBox<String> combox;
	Button convert, upload;
	Label title, madeby, notes;
	
	Image img;
	ImageView originalimg;
	ImageView convertedimg;
	
	
	
	public static void main(String[] args) {
        launch(args);
    }
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		bp = new BorderPane();
		buttonsbox = new HBox();
		imagebox = new HBox();
		container = new VBox();
		
		combox = new ComboBox<String>();
		convert = new Button("Convert");
		upload = new Button("Upload Image");
		title = new Label("Image Converter!!");
		madeby = new Label("made by: shelley liaunardy -2602169623 | buboot on github");
		notes = new Label("notes : blur doesnt show well in realistic images, and may take a while to load");
		
		originalimg = new ImageView();
		convertedimg = new ImageView();
		
		StackPane originalImageBox = new StackPane(originalimg);
		StackPane convertedImageBox = new StackPane(convertedimg);
		
		imagebox.getChildren().addAll(originalImageBox, convertedImageBox);
		combox.getItems().addAll("Grayscale", "Blur");
		combox.setValue("Convert to..");
		combox.valueProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            select = newValue;
        });
		
		upload.setOnAction(e -> uploadImg(primaryStage));
		
        convert.setOnAction(e -> {
            if (img != null) {
                editImage(select);
            }
        });
		
        buttonsbox.getChildren().addAll(upload, combox, convert);
		
        
        originalImageBox.setPrefSize(300, 300);
        convertedImageBox.setPrefSize(300, 300);
        
        originalimg.setFitWidth(500);
        originalimg.setFitHeight(500);
        originalimg.setPreserveRatio(true); // Maintain the aspect ratio

        convertedimg.setFitWidth(500);
        convertedimg.setFitHeight(500);
        convertedimg.setPreserveRatio(true);
        
		imagebox.setAlignment(Pos.CENTER);
		buttonsbox.setAlignment(Pos.CENTER);
		container.setAlignment(Pos.CENTER);
		
		container.getChildren().addAll(buttonsbox, imagebox, notes);
		
		//style
		bp.setStyle("-fx-background-color: #9ccddc");
		originalImageBox.setStyle("-fx-padding: 10; -fx-border-color: black; -fx-border-width: 2;");
		convertedImageBox.setStyle("-fx-padding: 10; -fx-border-color: black; -fx-border-width: 2;");
		
		title.setStyle("-fx-font-size: 78; -fx-font-weight: bold;");
		madeby.setStyle("-fx-font-size: 21; -fx-font-weight: bold;");
		notes.setStyle("-fx-font-size: 15;");
		
		bp.setCenter(container);
		bp.setTop(title);
		bp.setBottom(madeby);
		BorderPane.setAlignment(title, Pos.CENTER);
		BorderPane.setMargin(title, new Insets(140, 0, 0, 0));
		imagebox.setPadding(new Insets(50));
		
		//SCENE
		scene = new Scene(bp, 900, 600);
		primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setFullScreen(true);
	}
	
	

	
	private void uploadImg(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.bmp"));
        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            img = new Image(file.toURI().toString());
            originalimg.setImage(img);
            convertedimg.setImage(null); 
        }
    }
	
	private void editImage(String effect) {
        if (img == null) return;

        switch (effect) {
            case "Grayscale":
                convertedimg.setImage(Grayscale(img));
                break;
            case "Blur":
                convertedimg.setImage(Blurrr(img));
                break;
            default:
                convertedimg.setImage(img); // No effect
        }
    }
	
	private Image Grayscale(Image image) {
        WritableImage writableImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
        PixelReader reader = image.getPixelReader();
        PixelWriter writer = writableImage.getPixelWriter();

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                javafx.scene.paint.Color color = reader.getColor(x, y);
                double gray = color.getRed() * 0.2989 + color.getGreen() * 0.587 + color.getBlue() * 0.114;
                javafx.scene.paint.Color grayColor = javafx.scene.paint.Color.color(gray, gray, gray);
                writer.setColor(x, y, grayColor);
            }
        }

        return writableImage;
    }

    // Apply blur effect
	private Image Blurrr(Image image) {
	    WritableImage writableImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
	    PixelReader reader = image.getPixelReader();
	    PixelWriter writer = writableImage.getPixelWriter();

	    // Larger 5x5 blur kernel
	    int[][] kernel = {
	        {1, 1, 1, 1, 1},
	        {1, 1, 1, 1, 1},
	        {1, 1, 1, 1, 1},
	        {1, 1, 1, 1, 1},
	        {1, 1, 1, 1, 1}
	    };
	    int kernelWeight = 25; // The sum of all kernel elements (5x5 = 25)

	    int kernelRadius = kernel.length / 2; // Kernel offset for dynamic sizes

	    for (int y = kernelRadius; y < image.getHeight() - kernelRadius; y++) {
	        for (int x = kernelRadius; x < image.getWidth() - kernelRadius; x++) {
	            double red = 0, green = 0, blue = 0;

	            // Apply the kernel
	            for (int ky = -kernelRadius; ky <= kernelRadius; ky++) {
	                for (int kx = -kernelRadius; kx <= kernelRadius; kx++) {
	                    javafx.scene.paint.Color color = reader.getColor(x + kx, y + ky);
	                    red += color.getRed() * kernel[ky + kernelRadius][kx + kernelRadius];
	                    green += color.getGreen() * kernel[ky + kernelRadius][kx + kernelRadius];
	                    blue += color.getBlue() * kernel[ky + kernelRadius][kx + kernelRadius];
	                }
	            }

	            // Normalize the accumulated color values
	            red /= kernelWeight;
	            green /= kernelWeight;
	            blue /= kernelWeight;

	            writer.setColor(x, y, javafx.scene.paint.Color.color(red, green, blue));
	        }
	    }

	    return writableImage;
	}

	

	
	

}
