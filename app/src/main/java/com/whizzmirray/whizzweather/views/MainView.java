package com.whizzmirray.whizzweather.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.whizzmirray.whizzweather.MainActivity;
import com.whizzmirray.whizzweather.R;
import com.whizzmirray.whizzweather.models.Weather;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;

/**
 * Created by Whizz Mirray on 02/24/2018.
 */

public class MainView {
    private Activity mA;
    private TextView locationText,dateText,tempText,weatherInfoText;
    private ImageView weatherImage;
    private LineChart lineChart;
    private SimpleDateFormat dateFormat,hourFormat;
    private LineDataSet dataSet;
    public MainView(Activity mA){
        this.mA = mA;
        init();
    }

    public void init(){
        dateFormat = new SimpleDateFormat("E, d MMM HH:mm", Locale.getDefault());
        hourFormat = new SimpleDateFormat("HH:mm",Locale.getDefault());
        locationText    = mA.findViewById(R.id.locationText);
        dateText        = mA.findViewById(R.id.dateText);
        tempText        = mA.findViewById(R.id.tempText);
        weatherInfoText = mA.findViewById(R.id.weatherInfoText);
        weatherImage    = mA.findViewById(R.id.weatherImage);
        lineChart       = mA.findViewById(R.id.chart);

        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawLabels(false);
        lineChart.getAxisLeft().setDrawAxisLine(false);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setDrawBorders(false);
        lineChart.setVerticalScrollBarEnabled(true);

    }
    private void updateGraph(List<Entry> entries){
        dataSet = new LineDataSet(entries,"");
        dataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return (int)value + " C";
            }
        });
        dataSet.setDrawVerticalHighlightIndicator(false);
        dataSet.setDrawValues(true);
        dataSet.setDrawHighlightIndicators(false);
        dataSet.setDrawFilled(false);
        dataSet.setColor(mA.getColor(R.color.colorPrimaryDark));
        dataSet.setCircleColor(mA.getColor(R.color.colorPrimaryDark));
        dataSet.setLineWidth(5);
        dataSet.setCircleRadius(5);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.disableDashedLine();
        dataSet.setValueTextSize(13);

        LineData lineData = new LineData(dataSet);

        lineChart.setData(lineData);
        lineChart.setVisibleXRangeMaximum(3);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setAxisLineColor(R.color.colorPrimaryDark);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.invalidate();
        lineChart.animateX(500, Easing.EasingOption.EaseInBack);
    }
    /**
     * Updates the main view with the new weather info
     * @param weathers
     */
    public void update(final Weather[] weathers){
        if(weathers == null) return;
        Weather mainWeather = weathers[weathers.length-1];
        if(mainWeather == null) return;
        locationText.setText(String.format("%s, %s", mainWeather.getLocation().getCity(), mainWeather.getLocation().getCountry()));

        weatherImage.setImageDrawable(getDrawable(mainWeather.getIcon()));
        dateText.setText(dateFormat.format(mainWeather.getDt()));
        tempText.setText(String.format("%s %s°", (int)mainWeather.getTemperature().getTempCurrent(),"C"));//todo change unit system
        weatherInfoText.setText(mainWeather.getDescription());
        Log.d(TAG,"Update Finished at " + mainWeather.getLocation().getCity());

        List<Entry> entries = new ArrayList<Entry>();
        String[] xV = new String[8];
        for(int i = 0; i < 8; i++){
            xV[i] = hourFormat.format(weathers[i].getDt());
            entries.add(new Entry(i, (int) weathers[i].getTemperature().getTempCurrent()));
        }
        updateXAxis(xV);
        updateGraph(entries);
    }

    private Drawable getDrawable(String icon) {
        String iconName = mA.getString(mA.getResources().getIdentifier(new StringBuilder(icon).reverse().toString(),"string",mA.getPackageName()));
        int resourceId = mA.getResources().getIdentifier(iconName, "drawable", mA.getPackageName());
        return mA.getDrawable(resourceId);
    }
    private void updateXAxis(final String[] xV){
        XAxis xAxis = lineChart.getXAxis();
        IAxisValueFormatter formatter = new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xV[(int) value];
            }

            // we don't draw numbers, so no decimal digits needed
        };
        xAxis.setValueFormatter(formatter);
    }

}
