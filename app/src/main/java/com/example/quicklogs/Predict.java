// the predict page with the daily savings graph and saving details
package com.example.quicklogs;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Predict extends Fragment {

  Database database;
  LineChart dailySavings;
    TextView resultview;
    String month, year;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_predict, container, false);
        dailySavings = view.findViewById(R.id.savings_chart);
        resultview = view.findViewById(R.id.resultview);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar = Calendar.getInstance();
        String date = dateFormat.format(calendar.getTime());
        Database database;
        database = new Database(getContext());
        float savings = database.getTodaySavings(date);
        float newrasavings=database.getNewRequiredSavings(savings);
        resultview.setText("Today's Savings: $"+savings+"\n"+"Need to save $"+newrasavings+" to reach your goal!");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat monthYearFormat = new SimpleDateFormat("MM-yyyy");

        Calendar calendar2 = Calendar.getInstance();
        month = monthYearFormat.format(calendar2.getTime()).split("-")[0];
        year = monthYearFormat.format(calendar2.getTime()).split("-")[1];

        final List<Expense> expenses = database.getExpensesForMonth(month, year);

        if (expenses.size() == 0) {

            dailySavings.setVisibility(View.INVISIBLE);

        } else {

            dailySavings.setVisibility(View.VISIBLE);
            constructGraph();

        }



        dailySavings.getLegend().setEnabled(false);
        dailySavings.setDrawBorders(false);
        dailySavings.getDescription().setEnabled(false);
        dailySavings.setTouchEnabled(false);
        dailySavings.getXAxis().setTextSize(14);
        dailySavings.getAxisLeft().setTextSize(14);
        dailySavings.getXAxis().setDrawGridLines(false);
        dailySavings.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        dailySavings.getXAxis().setGranularity(1.0f);
        dailySavings.getAxisLeft().setGranularity(10.0f);
        dailySavings.getAxisLeft().setAxisLineWidth(2.0f);
        dailySavings.getXAxis().setAxisLineWidth(2.0f);
        dailySavings.getAxisRight().setDrawGridLines(false);
        dailySavings.getAxisRight().setEnabled(false);
        dailySavings.getAxisLeft().setDrawGridLines(false);

        constructGraph();


        return view;
    }
    private void constructGraph() {
        int firstDate, lastDate;
        database = new Database(getContext());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat monthYearFormat = new SimpleDateFormat("MM-yyyy");
        Calendar calendar2 = Calendar.getInstance();
        month = monthYearFormat.format(calendar2.getTime()).split("-")[0];
        year = monthYearFormat.format(calendar2.getTime()).split("-")[1];
        ArrayList<Entry> data = new ArrayList<>();
        JSONObject graphData = database.getGraphData(month, year);
        LineData lineData = null;

        try {
            lastDate = Integer.parseInt(graphData.getString("lastDate").split("-")[0]);
            firstDate = Integer.parseInt(graphData.getString("firstDate").split("-")[0]);

            dailySavings.getAxisRight().setAxisMaximum(graphData.getInt("totalIncome") + 50);

            @SuppressLint("UseSparseArrays") final HashMap<Integer, Integer> xAxisValues = new HashMap<>();

            if(lastDate != 0 && firstDate != 0){
                for (int i = firstDate; i<=lastDate; i++) {
                    data.add(new Entry(i, (database.getMaxExpense() - database.getExpensesForDate(i, month, year))));
                    xAxisValues.put(i, i);
                }
                LineDataSet lineDataSet = new LineDataSet(data, "");
                lineDataSet.setCircleColor(Color.parseColor("#3F51B5"));
                lineDataSet.setCircleHoleRadius(3.0f);
                lineDataSet.setLineWidth(2.0f);
                lineDataSet.setDrawFilled(true);
                lineDataSet.setColor(Color.parseColor("#3F51B5"));
                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                dataSets.add(lineDataSet);

                lineData = new LineData(dataSets);
                lineData.setValueTextSize(14);
                dailySavings.getXAxis().setValueFormatter(new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        return String.valueOf(xAxisValues.get(((int)value)));
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            dailySavings.setData(lineData);
            dailySavings.animateXY(300, 300);
        }
    }
}
