package ch.epfl.alpano.summit;

import java.util.Objects;

import ch.epfl.alpano.GeoPoint;

public final class Summit {

	private final String name;
	private final GeoPoint position;
	private final int elevation;
	
	// verification si position est null
	public Summit(String name, GeoPoint position, int elevation) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(position);
		this.name = name;
		this.position = position;
		this.elevation = elevation;
	}
	
	public String name(){
		return this.name;
	}
	
	public GeoPoint position(){
		return this.position;
	}
	
	public int elevation(){
		return this.elevation;
	}
	
	@Override
	public String toString() {
		return this.name() + " " + this.position().toString() + " " + this.elevation();
	}
}
