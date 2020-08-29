import csv.CountriesToExport;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CsvSearchExportTester {

    CountriesToExport export;

    @Before
    public void setUp()
    {
        export = new CountriesToExport();
    }

    @Test
    public void bigExporters() {

        List<String> res = export.bigExporters("$999,999,999,999");
        List<String> actual = Arrays.asList("China", "European Union", "Germany", "United States");
        assertEquals("amount of exporters", 4, res.size());
        assertEquals(actual, res);
    }

    @Test
    public void amountExport() {

        int amount = export.numberOfExporters( "gold");
        assertEquals("amount of exporters", 30, amount);
    }

    @Test
    public void exportersTwoProducts() {

        List<String> res = export.listExportersTwoProducts("cotton", "flowers");
        List<String> actual = Arrays.asList("Uganda", "Zambia");
        assertEquals("amount of exporters", 2, res.size());
        assertEquals(actual, res);
    }

}
