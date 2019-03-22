package ch.epfl.alpano.gui;

/**
 * predefined panoramas
 * 
 * @author Haitham Hammami (257479)
 * @author Makki Fourati (247746)
 */
public interface PredefinedPanoramas {

	static int HEIGHT = 800;
	static int WIDTH = 2500;
	static int MAX_DISTANCE = 300;
	static int SAMPLING = 0;

	public static PanoramaUserParameters NIESEN = new PanoramaUserParameters(76500, 467300, 600, 180, 110, MAX_DISTANCE,
			WIDTH, HEIGHT, SAMPLING);

	public static PanoramaUserParameters ALPES_DU_JURA = new PanoramaUserParameters(68087, 470085, 1380, 162, 27,
			MAX_DISTANCE, WIDTH, HEIGHT, SAMPLING);

	public static PanoramaUserParameters MONT_RACINE = new PanoramaUserParameters(68200, 470200, 1500, 135, 45,
			MAX_DISTANCE, WIDTH, HEIGHT, SAMPLING);

	public static PanoramaUserParameters FINSTERAARHORN = new PanoramaUserParameters(81260, 465374, 4300, 205, 20,
			MAX_DISTANCE, WIDTH, HEIGHT, SAMPLING);

	public static PanoramaUserParameters TOUR_DE_SAUVABELIN = new PanoramaUserParameters(66385, 465353, 700, 135, 100,
			MAX_DISTANCE, WIDTH, HEIGHT, SAMPLING);

	public static PanoramaUserParameters PLAGE_DU_PELICAN = new PanoramaUserParameters(65728, 465132, 380, 135, 60,
			MAX_DISTANCE, WIDTH, HEIGHT, SAMPLING);
}
