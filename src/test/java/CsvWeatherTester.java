import csv.Weather;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class CsvWeatherTester {
    Weather weather;

    @Before
    public void setUp()
    {
        weather = new Weather();
    }

    @Test
    public void testMinTemperature() throws IOException {

        String res = weather.getMinTemperatureMultiCSVFile("nc_weather/2012"); //19,4
        assertEquals("19.4", res);
    }

    @Test
    public void testMinHumidity() throws IOException {

        String res = weather.getMinHumidityMultiCSVFile("nc_weather/2015"); //19,4
        assertEquals("14", res);
    }

    @Test
    public void testAverageTemperature() throws IOException {

        double res = weather.getAverageTemperatureInFile(weather.readCSVFile("nc_weather/2012/weather-2012-01-01.csv"));
        double actual = 1210.1/23.0;
        assertEquals("check average t", res, actual, 0.001);
    }

    @Test
    public void testAverageTemperatureWithHighHumidity() throws IOException {

        double res = weather.averageTemperatureWithHighHumidityInFile(weather.readCSVFile("nc_weather/2015/weather-2015-04-08.csv"), 80);
        double actual = 904.6/14.0;
        //double res = averageTemperatureWithHighHumidityInFile(parser, 80);
        assertEquals("check average t", res, actual, 0.001);
    }

}
