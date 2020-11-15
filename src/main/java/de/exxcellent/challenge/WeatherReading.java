package de.exxcellent.challenge;

public class WeatherReading implements SpreadItem {

	private int day;
	private float max;
	private float min;

	public WeatherReading(int day, float max, float min) {
		this.day = day;
		this.max = max;
		this.min = min;
	}

	public static WeatherReading fromStrings(String dayOfMonth, String maxTemp, String minTemp) throws NumberFormatException {
		int day = Integer.parseInt(dayOfMonth);
		float max = Float.parseFloat(maxTemp);
		float min = Float.parseFloat(minTemp);
		return new WeatherReading(day, max, min);
	}

	@Override
	public String name() {
		return Integer.toString(day);
	}

	@Override
	public float min() {
		return min;
	}

	@Override
	public float max() {
		return max;
	}
}
