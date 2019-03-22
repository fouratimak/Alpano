package ch.epfl.alpano.gui;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;
import static java.lang.Math.toDegrees;
import ch.epfl.alpano.Azimuth;
import ch.epfl.alpano.Panorama;
import ch.epfl.alpano.PanoramaParameters;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.DiscreteElevationModel;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;
import ch.epfl.alpano.summit.GazetteerParser;
import ch.epfl.alpano.summit.Summit;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 * Creating the graphic interface
 * 
 * @author Haitham Hammami (257479)
 * @author Makki Fourati (247746)
 */
public final class Alpano extends Application {
	private PanoramaParametersBean parametersBean = new PanoramaParametersBean(PredefinedPanoramas.ALPES_DU_JURA);
	private PanoramaComputerBean computerBean;

	final static int TO_KLM = 1000;
	final static int FRAME_SIZE = 80;
	private static final int ZOOM_SCALE = 4;

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// … creating the graphic interface

		List<Summit> summits = summitsLoader(new File("alps.txt"));

		ContinuousElevationModel cDEM = new ContinuousElevationModel(demLoader());
		computerBean = new PanoramaComputerBean(cDEM, summits);
		Pane labelsPane = labelsPane();

		Button observerPositionButton = new Button("observer\n position");
		observerPositionButton.setOnMouseClicked(buttonEvent -> {

			String longitude = String.format((Locale) null, "%.4f",
					parametersBean.observerLongitudeProperty().get() / 10000d);
			String latitude = String.format((Locale) null, "%.4f",
					parametersBean.observerLatitudeProperty().get() / 10000d);
			String qy = "mlat=" + latitude + "mlon=" + longitude;
			String fg = "map=15/" + latitude + "/" + longitude;
			try {
				URI osmURI = new URI("http", "www.openstreetmap.org", "/", qy, fg);
				java.awt.Desktop.getDesktop().browse(osmURI);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		TextArea mousePosition = new TextArea();
		mousePosition.setEditable(false);
		mousePosition.setPrefRowCount(2);

		// creating a zoom for the mouse
		ImageView panoZoom = new ImageView();
		panoZoom.setSmooth(true);
		panoZoom.setFitWidth(FRAME_SIZE);
		panoZoom.setFitHeight(FRAME_SIZE);
		panoZoom.setMouseTransparent(true);
		panoZoom.setManaged(false);
		panoZoom.imageProperty().bind(computerBean.imageProperty());
		panoZoom.setVisible(false);
		panoZoom.setClip(null);
		panoZoom.setEffect(new DropShadow(10, Color.BLACK));

		ImageView panoView = panoView(mousePosition, panoZoom);

		// creating a gridPane
		GridPane paramsGrid = paramsGrid();
		paramsGrid.add(mousePosition, 6, 0, 1, 3);
		paramsGrid.add(observerPositionButton, 7, 0, 1, 3);
		StackPane panoGroup = new StackPane(panoView, labelsPane, panoZoom);
		ScrollPane panoScrollPane = new ScrollPane(panoGroup);
		StackPane updateNotice = updateNotice(
				"Les paramètres du panorama ont changé." + "\nCliquez ici pour mettre le dessin à jour.");

		StackPane panoPane = new StackPane(panoScrollPane, updateNotice);

		BorderPane root = new BorderPane();
		root.setBottom(paramsGrid);
		root.setCenter(panoPane);

		Scene scene = new Scene(root);

		primaryStage.setTitle("Alpano");
		primaryStage.setScene(scene);
		primaryStage.setFullScreen(true);
		primaryStage.show();
	}

	/**
	 * @return the labels Pane that is bound with the summit labels
	 */
	private Pane labelsPane() {
		Pane labelsPane = new Pane();
		labelsPane.setPickOnBounds(false);
		Bindings.bindContent(labelsPane.getChildren(), computerBean.getLabels());
		labelsPane.prefWidthProperty().bind(parametersBean.widthProperty());
		labelsPane.prefHeightProperty().bind(parametersBean.heightProperty());
		return labelsPane;
	}

	/**
	 * @param field
	 * @param stringConverter
	 * @param beanParam
	 * @param prefColumn
	 *            sets the text field preferences using a text formatter
	 */
	private void textFieldPreferences(TextField field, StringConverter<Integer> stringConverter,
			ObjectProperty<Integer> beanParam, int prefColumn) {
		field.setAlignment(Pos.CENTER_RIGHT);
		field.setPrefColumnCount(prefColumn);
		TextFormatter<Integer> formatter = new TextFormatter<>(stringConverter);
		formatter.valueProperty().bindBidirectional(beanParam);
		field.setTextFormatter(formatter);
	}

	/**
	 * @param text
	 * @return creates the update notice when the parameters have changed
	 */
	private StackPane updateNotice(String text) {
		Text updateText = new Text(text);
		updateText.setFont(new Font(40));
		updateText.setTextAlignment(TextAlignment.CENTER);
		StackPane updateNotice = new StackPane(updateText);
		updateNotice.setBackground(
				new Background(new BackgroundFill(Color.hsb(0, 0, 1, 0.9), CornerRadii.EMPTY, Insets.EMPTY)));
		updateNotice.visibleProperty()
				.bind(parametersBean.parametersProperty().isNotEqualTo(computerBean.parametersProperty()));
		updateNotice.setOnMouseClicked(event -> {
			PanoramaUserParameters parameters = parametersBean.parametersProperty().get();
			computerBean.setParameters(parameters);
		});

		return updateNotice;
	}

	/**
	 * @return the parameters grid
	 */
	private GridPane paramsGrid() {
		StringConverter<Integer> fourDecimal = new FixedPointStringConverter(4);
		StringConverter<Integer> zeroDecimal = new FixedPointStringConverter(0);

		Label latitudeL = new Label("Latitude (°):");
		TextField latitudeF = new TextField();
		textFieldPreferences(latitudeF, fourDecimal, parametersBean.observerLatitudeProperty(), 7);

		Label longitudeL = new Label("Longititude (°):");
		TextField longitudeF = new TextField();
		textFieldPreferences(longitudeF, fourDecimal, parametersBean.observerLongitudeProperty(), 7);

		Label altitudeL = new Label("Altitude (m):");
		TextField altitudeF = new TextField();
		textFieldPreferences(altitudeF, zeroDecimal, parametersBean.observerElevationProperty(), 4);

		Label azimuthL = new Label("Azimuth (°):");
		TextField azimuthF = new TextField();
		textFieldPreferences(azimuthF, zeroDecimal, parametersBean.centerAzimuthProperty(), 3);

		Label horizantalFieldOfViewL = new Label("AngleDeVue (°):");
		TextField horizantalFieldOfViewF = new TextField();
		textFieldPreferences(horizantalFieldOfViewF, zeroDecimal, parametersBean.horizontalFieldOfViewProperty(), 3);

		Label maxDistanceL = new Label("Visibilité (km):");
		TextField maxDistanceF = new TextField();
		textFieldPreferences(maxDistanceF, zeroDecimal, parametersBean.maxDistanceProperty(), 3);

		Label widthL = new Label("Largeur (px):");
		TextField widthF = new TextField();
		textFieldPreferences(widthF, zeroDecimal, parametersBean.widthProperty(), 4);

		Label heightL = new Label("Hauteur (px):");
		TextField heightF = new TextField();
		textFieldPreferences(heightF, zeroDecimal, parametersBean.heightProperty(), 4);

		Label superSamplingExponentL = new Label("Suréchantillonnage:");
		ChoiceBox<Integer> superSamplingExponentF = new ChoiceBox<>();
		superSamplingExponentF.getItems().addAll(0, 1, 2);
		StringConverter<Integer> listConverter = new LabeledListStringConverter("non", "2x", "4x");
		superSamplingExponentF.valueProperty().bindBidirectional(parametersBean.superSamplingExponentProperty());
		superSamplingExponentF.setConverter(listConverter);

		GridPane paramsGrid = new GridPane();
		paramsGrid.setAlignment(Pos.CENTER);
		paramsGrid.setHgap(10);
		paramsGrid.setVgap(3);
		paramsGrid.setPadding(new Insets(7, 5, 5, 5));
		paramsGrid.addRow(0, latitudeL, latitudeF, longitudeL, longitudeF, altitudeL, altitudeF);
		paramsGrid.addRow(1, azimuthL, azimuthF, horizantalFieldOfViewL, horizantalFieldOfViewF, maxDistanceL,
				maxDistanceF);
		paramsGrid.addRow(2, widthL, widthF, heightL, heightF, superSamplingExponentL, superSamplingExponentF);
		for (Node n : paramsGrid.getChildren())
			GridPane.setHalignment(n, HPos.RIGHT);
		return paramsGrid;

	}

	/**
	 * @return the elevation model constructed from 8 hgt files
	 */
	@SuppressWarnings("resource")
	private DiscreteElevationModel demLoader() {
		DiscreteElevationModel dem466 = new HgtDiscreteElevationModel(new File("N45E006.hgt"));
		DiscreteElevationModel dem467 = new HgtDiscreteElevationModel(new File("N45E007.hgt"));
		DiscreteElevationModel dem468 = new HgtDiscreteElevationModel(new File("N45E008.hgt"));
		DiscreteElevationModel dem469 = new HgtDiscreteElevationModel(new File("N45E009.hgt"));
		DiscreteElevationModel dem476 = new HgtDiscreteElevationModel(new File("N46E006.hgt"));
		DiscreteElevationModel dem477 = new HgtDiscreteElevationModel(new File("N46E007.hgt"));
		DiscreteElevationModel dem478 = new HgtDiscreteElevationModel(new File("N46E008.hgt"));
		DiscreteElevationModel dem479 = new HgtDiscreteElevationModel(new File("N46E009.hgt"));
		DiscreteElevationModel dem = ((dem466.union(dem476)).union(dem467.union(dem477)))
				.union((dem468.union(dem478)).union(dem469.union(dem479)));
		return dem;

	}

	/**
	 * @param file
	 * @return the list of summit loaded from the file
	 */
	private List<Summit> summitsLoader(File file) {
		try {
			return GazetteerParser.readSummitsFrom(file);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * @param mousePosition
	 * @return the panorama view from the computer bean
	 */
	private ImageView panoView(TextArea mousePosition, ImageView panoZoom) {
		ImageView panoView = new ImageView();
		panoView.fitWidthProperty().bind(parametersBean.widthProperty());
		panoView.imageProperty().bind(computerBean.imageProperty());
		panoView.setPreserveRatio(true);
		panoView.setSmooth(true);
		panoView.setMouseTransparent(false);

		// convertir le curseur en position

		// Mouse moving event
		panoView.setOnMouseMoved(mouseEvent -> {
			Panorama p = computerBean.getPanorama();

			int x = (int) Math.scalb(mouseEvent.getX(), computerBean.getParameters().superSamplingExponent());
			int y = (int) Math.scalb(mouseEvent.getY(), computerBean.getParameters().superSamplingExponent());
			PanoramaParameters parameters = computerBean.getParameters().panoramaParameters();
			double azimuth = parameters.azimuthForX(x);
			String longitude = String.format((Locale) null, "%.4f", toDegrees(p.longitudeAt(x, y)));
			String latitude = String.format((Locale) null, "%.4f", toDegrees(p.latitudeAt(x, y)));
			String latOrientation = (p.latitudeAt(x, y) > 0 ? "°N " : "°S ");
			String longOrientation = (p.longitudeAt(x, y) > 0 ? "°E " : "°W ");
			mousePosition.setText("Position: " + latitude + latOrientation + longitude + longOrientation
					+ "\nDistance: " + String.format((Locale) null, "%.1f", p.distanceAt(x, y) / TO_KLM)
					+ " km\nAltitude: " + Math.round(p.elevationAt(x, y)) + " m\nAzimuth: "
					+ String.format((Locale) null, "%.1f", toDegrees(azimuth)) + "° ("
					+ Azimuth.toOctantString(azimuth, "N", "E", "S", "W") + ")        Elevation: "
					+ String.format((Locale) null, "%.1f", +(toDegrees(parameters.altitudeForY(y)))) + "°");
			Rectangle2D viewportRect = new Rectangle2D(x - FRAME_SIZE / (ZOOM_SCALE * 2),
					y - FRAME_SIZE / (ZOOM_SCALE * 2), FRAME_SIZE / ZOOM_SCALE, FRAME_SIZE / ZOOM_SCALE);
			panoZoom.setVisible(true);
			panoZoom.setViewport(viewportRect);
			panoZoom.relocate(mouseEvent.getX() - FRAME_SIZE, mouseEvent.getY() - FRAME_SIZE);

		}

		);
		// Mouse clicking event
		panoView.setOnMouseClicked(event -> {
			int x = (int) Math.scalb(event.getX(), computerBean.getParameters().superSamplingExponent());
			int y = (int) Math.scalb(event.getY(), computerBean.getParameters().superSamplingExponent());
			String longitude = String.format((Locale) null, "%.4f",
					Math.toDegrees(computerBean.getPanorama().longitudeAt(x, y)));
			String latitude = String.format((Locale) null, "%.4f",
					Math.toDegrees(computerBean.getPanorama().latitudeAt(x, y)));
			String qy = "mlat=" + latitude + "mlon=" + longitude;
			String fg = "map=15/" + latitude + "/" + longitude;
			try {
				URI osmURI = new URI("http", "www.openstreetmap.org", "/", qy, fg);
				java.awt.Desktop.getDesktop().browse(osmURI);
			} catch (URISyntaxException | IOException e) {
				throw new Error(e);
			}
		}

		);

		return panoView;
	}

}