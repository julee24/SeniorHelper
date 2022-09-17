package com.yejija.myapplication;


import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BubbleEntry;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;


public abstract class MyValueFormatter implements IAxisValueFormatter, IValueFormatter {


    @Override
    @Deprecated
    public String getFormattedValue(float value, AxisBase axis) {
        return getFormattedValue(value);
    }

    @Override
    @Deprecated
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return getFormattedValue(value);
    }

    public String getFormattedValue(float value) {
        return String.valueOf(value);
    }

    public String getAxisLabel(float value, AxisBase axis) {
        return getFormattedValue(value);
    }

    public String getBarLabel(BarEntry barEntry) {
        return getFormattedValue(barEntry.getY());
    }

    public String getBarStackedLabel(float value, BarEntry stackedEntry) {
        return getFormattedValue(value);
    }

    public String getPointLabel(Entry entry) {
        return getFormattedValue(entry.getY());
    }

    public String getPieLabel(float value, PieEntry pieEntry) {
        return getFormattedValue(value);
    }

    public String getRadarLabel(RadarEntry radarEntry) {
        return getFormattedValue(radarEntry.getY());
    }

    public String getBubbleLabel(BubbleEntry bubbleEntry) {
        return getFormattedValue(bubbleEntry.getSize());
    }

    public String getCandleLabel(CandleEntry candleEntry) {
        return getFormattedValue(candleEntry.getHigh());
    }

}
