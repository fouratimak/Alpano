/*
 *	Author:      Haitham Hammami
 *	Date:        17 Mar 2017
 */

package ch.epfl.alpano;

import java.util.Objects;

public final class PanoramaParameters {
    private final int maxDistance, width, observerElevation, height;
    private final GeoPoint observerPosition;
    private final double centerAzimuth, horizontalFieldOfView;
    // central x and y indexes and the angle between two indexes
    private final double centralX,centralY,unitAngle;

    public PanoramaParameters(GeoPoint observerPosition, int observerElevation,
            double centerAzimuth, double horizontalFieldOfView, int maxDistance,
            int width, int height) {
        Objects.requireNonNull(observerPosition);
        Preconditions.checkArgument(Azimuth.isCanonical(centerAzimuth));
        Preconditions.checkArgument(width > 1);
        Preconditions.checkArgument(height > 0);
        Preconditions.checkArgument(maxDistance > 0);
        Preconditions.checkArgument(horizontalFieldOfView > 0
                && horizontalFieldOfView <= Math2.PI2);
        this.centerAzimuth = centerAzimuth;
        this.height = height;
        this.horizontalFieldOfView = horizontalFieldOfView;
        this.maxDistance = maxDistance;
        this.observerElevation = observerElevation;
        this.width = width;
        this.observerPosition = observerPosition;
        centralX=(width-1)/2d;
        centralY=(height-1)/2d;
        unitAngle=horizontalFieldOfView  / (width() - 1);
    }
    public double centerAzimuth(){
        return centerAzimuth;
    }
    public int  width(){
        return width;
    }
    public int height(){
        return height;
    }
    public int observerElevation() {
        return observerElevation;
    }
    public GeoPoint observerPosition() {
        return observerPosition;
    }
    public double horizontalFieldOfView() {
        return horizontalFieldOfView;
    }
    public int maxDistance() {
        return maxDistance;
    }

    public double verticalFieldOfView() {
        return unitAngle * (height() - 1) ;
    }
    
    public double azimuthForX(double x){
        Preconditions.checkArgument(x>=0 && x<=width()-1);
        
        return Azimuth.canonicalize((x-centralX)*unitAngle+ centerAzimuth());
        
    }
    public double xForAzimuth(double a){
        Preconditions.checkArgument(Math.abs(a-centerAzimuth())<=centerAzimuth());
    return    (a-centerAzimuth())/unitAngle+ centralX;
    }
    public double altitudeForY(double y){
        Preconditions.checkArgument(y>=0 && y<=height()-1);
        return (centralY-y)*unitAngle;
    }
    public double yForAltitude(double a){
        Preconditions.checkArgument(Math.abs(a)<=(verticalFieldOfView()/2));
        return    (-a)/unitAngle+centralY;
    }
    boolean isValidSampleIndex(int x, int y){
        return (x>=0 && x<=width()-1 && y>=0 && y<=height()-1);
    }
    int linearSampleIndex(int x, int y){
        Preconditions.checkArgument(isValidSampleIndex(x, y));
        return x +width()*y;
    }
}
