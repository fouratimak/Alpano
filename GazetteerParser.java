package ch.epfl.alpano.summit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.alpano.GeoPoint;

public final class GazetteerParser {

	private GazetteerParser() {
	}

	public static List<Summit> readSummitsFrom(File file) throws IOException {
		List<Summit> summitList = new ArrayList<>();
		try (BufferedReader f = new BufferedReader(
				new InputStreamReader(new FileInputStream(file), StandardCharsets.US_ASCII))) {
			String line = f.readLine();
			
			while (line != null) {
				summitList.add(summit(line));
				line = f.readLine();
			}
		}catch(IndexOutOfBoundsException | NumberFormatException e){
			throw new IOException();
		}
		return Collections.unmodifiableList(summitList);
	}

	private static double angleConverter(String angle) {
		String[] dms = angle.split(":");

		return Math
				.toRadians(Integer.parseInt(dms[0]) + Integer.parseInt(dms[1]) / 60.0 + Integer.parseInt(dms[2]) / 3600.0);

	}

	private static Summit summit(String line) {
		String name = line.substring(36);
		double longitude = angleConverter(line.substring(1, 9).trim());
		double latitude = angleConverter(line.substring(10, 18).trim());
		int elevation = Integer.parseInt(line.substring(20, 24).trim());
		return new Summit(name, new GeoPoint(longitude, latitude), elevation);
	}

}
