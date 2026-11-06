package com.example.assignment1_airtravel;

import org.junit.Test;

import static org.junit.Assert.*;

public class ExampleUnitTest {

    @Test
    public void testExcessBaggageCalculation_EconomySample1() {
        double weight = 25.0;
        double allowance = 20.0;
        double rate = 15.0;

        double excessWeight = (weight > allowance) ? (weight - allowance) : 0.0;
        double excessCharge = excessWeight * rate;
        double serviceTax = excessCharge * 0.08;
        double totalPayable = excessCharge + serviceTax;

        assertEquals(5.0, excessWeight, 0.001);
        assertEquals(75.0, excessCharge, 0.001);
        assertEquals(81.0, totalPayable, 0.001);
    }

    @Test
    public void testExcessBaggageCalculation_EconomySample2() {
        double weight = 18.0;
        double allowance = 20.0;
        double rate = 15.0;

        double excessWeight = (weight > allowance) ? (weight - allowance) : 0.0;
        double excessCharge = excessWeight * rate;
        double serviceTax = excessCharge * 0.08;
        double totalPayable = excessCharge + serviceTax;

        assertEquals(0.0, excessWeight, 0.001);
        assertEquals(0.0, excessCharge, 0.001);
        assertEquals(0.0, totalPayable, 0.001);
    }

    @Test
    public void testExcessBaggageCalculation_BusinessSample1() {
        double weight = 55.0;
        double allowance = 40.0;
        double rate = 12.0;

        double excessWeight = (weight > allowance) ? (weight - allowance) : 0.0;
        double excessCharge = excessWeight * rate;
        double serviceTax = excessCharge * 0.08;
        double totalPayable = excessCharge + serviceTax;

        assertEquals(15.0, excessWeight, 0.001);
        assertEquals(180.0, excessCharge, 0.001);
        assertEquals(194.4, totalPayable, 0.001);
    }

    @Test
    public void testExcessBaggageCalculation_BusinessSample2() {
        double weight = 42.5;
        double allowance = 40.0;
        double rate = 12.0;

        double excessWeight = (weight > allowance) ? (weight - allowance) : 0.0;
        double excessCharge = excessWeight * rate;
        double serviceTax = excessCharge * 0.08;
        double totalPayable = excessCharge + serviceTax;

        assertEquals(2.5, excessWeight, 0.001);
        assertEquals(30.0, excessCharge, 0.001);
        assertEquals(32.4, totalPayable, 0.001);
    }
}
