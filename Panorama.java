 package ch.epfl.alpano;

import java.util.Arrays;
import java.util.Objects;

public final class Panorama {

	private final PanoramaParameters parameters;
	private final float[] distances, longitudes, latitudes, elevations, slopes;

	private Panorama(PanoramaParameters parameters, float[] distances, float[] longitudes, float[] latitudes,
			float[] elevations, float[] slopes) {
		
		this.parameters = parameters;
		
		this.distances = distances;
		this.longitudes = longitudes;
		this.latitudes = latitudes;
		this.elevations = elevations;
		this.slopes = slopes;
	}
	
	public PanoramaParameters parameters(){
		return parameters;
	}
	
	public float distanceAt(int x, int y){
		if(!parameters().isValidSampleIndex(x, y)){
			throw new IndexOutOfBoundsException();
		}
		return distances[parameters().linearSampleIndex(x, y)];
	}
	
	public float distanceAt(int x, int y, float d){
		if(!parameters().isValidSampleIndex(x, y)){
			return d;
		}else{
			return distances[parameters().linearSampleIndex(x, y)];
		}
	}
	
	public float longitudeAt(int x, int y){
		if(!parameters().isValidSampleIndex(x, y)){
			throw new IndexOutOfBoundsException();
		}
		return longitudes[parameters().linearSampleIndex(x, y)];
	}
	
	public float latitudeAt(int x, int y){
		if(!parameters().isValidSampleIndex(x, y)){
			throw new IndexOutOfBoundsException();
		}
		return latitudes[parameters().linearSampleIndex(x, y)];
	}
	
	public float elevationAt(int x, int y){
		if(!parameters().isValidSampleIndex(x, y)){
			throw new IndexOutOfBoundsException();
		}
		return elevations[parameters().linearSampleIndex(x, y)];
	}
	
	public float slopeAt(int x, int y){
		if(!parameters().isValidSampleIndex(x, y)){
			throw new IndexOutOfBoundsException();
		}
		return slopes[parameters().linearSampleIndex(x, y)];
	}
	
	public final static class Builder {
		private final PanoramaParameters parameters;
		private final int nbSamples;
		private final float[] distances, longitudes, latitudes, elevations, slopes ;
		private static boolean check = false;

		public Builder(PanoramaParameters parameters){
			Objects.requireNonNull(parameters);
			this.parameters = parameters;
			this.nbSamples = parameters.height() * parameters.width();
			
			this.distances = new float[nbSamples];
			Arrays.fill(distances, Float.POSITIVE_INFINITY);
			this.longitudes = new float[nbSamples];
			Arrays.fill(longitudes, 0);
			this.latitudes = new float[nbSamples];
			Arrays.fill(latitudes, 0);
			this.elevations = new float[nbSamples];
			Arrays.fill(elevations, 0);
			this.slopes = new float[nbSamples];
			Arrays.fill(slopes, 0);
			
		}
		
		public Builder setDistanceAt(int x, int y, float distance){
			if(check){
				throw new IllegalStateException();
			}
			this.distances[parameters.linearSampleIndex(x, y)] = distance;
			return this;
		}
		public Builder setLongitudeAt(int x, int y, float longitude){
			if(check){
				throw new IllegalStateException();
			}
			this.longitudes[parameters.linearSampleIndex(x, y)] = longitude;
			return this;
		}
		public Builder setLatitudeAt(int x, int y, float latitude){
			if(check){
				throw new IllegalStateException();
			}
			this.latitudes[parameters.linearSampleIndex(x, y)] = latitude;
			return this;
		}
		public Builder setElevationAt(int x, int y, float elevation){
			if(check){
				throw new IllegalStateException();
			}
			this.elevations[parameters.linearSampleIndex(x, y)] = elevation;
			return this;
		}
		public Builder setSlopeAt(int x, int y, float slope){
			if(check){
				throw new IllegalStateException();
			}
			this.slopes[parameters.linearSampleIndex(x, y)] = slope;
			return this;
		}
		public Panorama build(){
			if(check){
				throw new IllegalStateException();
			}
			check = true;
			return new Panorama(this.parameters, this.distances, this.longitudes, this.latitudes, this.elevations, this.slopes);
		}
	}
	

}
