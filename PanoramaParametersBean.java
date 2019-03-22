package ch.epfl.alpano.gui;

import java.util.EnumMap;
import java.util.Map;

import static javafx.application.Platform.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import static ch.epfl.alpano.gui.UserParameter.*;

/**
 * Panorama parameters beans
 * 
 * @author Haitham Hammami (257479)
 * @author Makki Fourati (247746)
 */
public class PanoramaParametersBean {

	private ObjectProperty<PanoramaUserParameters> parameters;
	private Map<UserParameter, ObjectProperty<Integer>> individualParameters;

	/**
	 * constructs a PanoramaParametersBean by using PanoramaUserParameters as
	 * its property values
	 * 
	 * @param parameters
	 */
	public PanoramaParametersBean(PanoramaUserParameters parameters) {
		this.parameters = new SimpleObjectProperty<>(parameters);
		individualParameters = new EnumMap<>(UserParameter.class);

		for (UserParameter u : UserParameter.values()) {
			individualParameters.put(u, new SimpleObjectProperty<>(parameters.get(u)));

			individualParameters.get(u).addListener((prop, oldV, newV) -> runLater(this::synchronizeParameters));
		}

	}

	private void synchronizeParameters() {
		PanoramaUserParameters parameters = new PanoramaUserParameters(observerLongitudeProperty().get(),
				observerLatitudeProperty().get(), observerElevationProperty().get(), centerAzimuthProperty().get(),
				horizontalFieldOfViewProperty().get(), maxDistanceProperty().get(), widthProperty().get(),
				heightProperty().get(), superSamplingExponentProperty().get());
		this.parameters.set(parameters);

		for (UserParameter u : UserParameter.values()) {
			individualParameters.get(u).set(parameters.get(u));

		}
	}

	ReadOnlyObjectProperty<PanoramaUserParameters> parametersProperty() {
		return parameters;
	}

	ObjectProperty<Integer> observerLongitudeProperty() {
		return individualParameters.get(OBSERVER_LONGITUDE);
	}

	ObjectProperty<Integer> observerLatitudeProperty() {
		return individualParameters.get(OBSERVER_LATITUDE);
	}

	ObjectProperty<Integer> observerElevationProperty() {
		return individualParameters.get(OBSERVER_ELEVATION);
	}

	ObjectProperty<Integer> centerAzimuthProperty() {
		return individualParameters.get(CENTER_AZIMUTH);
	}

	ObjectProperty<Integer> horizontalFieldOfViewProperty() {
		return individualParameters.get(HORIZONTAL_FIELD_OF_VIEW);
	}

	ObjectProperty<Integer> maxDistanceProperty() {
		return individualParameters.get(MAX_DISTANCE);
	}

	ObjectProperty<Integer> widthProperty() {
		return individualParameters.get(WIDTH);
	}

	ObjectProperty<Integer> heightProperty() {
		return individualParameters.get(HEIGHT);
	}

	ObjectProperty<Integer> superSamplingExponentProperty() {
		return individualParameters.get(SUPER_SAMPLING_EXPONENT);
	}

}
