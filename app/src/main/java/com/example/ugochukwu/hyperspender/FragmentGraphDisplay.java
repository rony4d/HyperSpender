package com.example.ugochukwu.hyperspender;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.ugochukwu.hyperspender.data.BudgetDbHelper;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

public class FragmentGraphDisplay extends Fragment {
    BudgetDbHelper budgetDbHelper;
    int eatingOutCount;
    int transportationCount;
    int rentCount;
    int medicalCount;
    int uncategorizedCount;
    int utilitiesCount;
    int shoppingCount;
    int groceriesCount;
    int incomeCount;

    int[] pieChartValues;

    private CategorySeries mSeries = new CategorySeries("");
    private DefaultRenderer mRenderer = new DefaultRenderer();
    private GraphicalView mChartView;

    int[] COLORS = new int[] {Color.BLUE,Color.GREEN,Color.MAGENTA,Color.CYAN, Color.YELLOW, Color.RED, Color.WHITE, Color.CYAN,Color.DKGRAY };
    public static String [] CATEGORY  = new String[]{"Eating Out","Transportation","Rent","Medical","Uncategorized","Utilities","Shopping","Groceries","Income"};

    public FragmentGraphDisplay() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i("Rony", "Real Color "+ String.valueOf(Color.BLUE));
        budgetDbHelper = new BudgetDbHelper(getActivity());
        long month_id = getActivity().getIntent().getLongExtra(FragmentDetailMonthList.EXTRA_MONTH_ID, -1);
        String budgetName = budgetDbHelper.getMonthNameWithId(month_id);
        ((ActivityGraphDisplay)getActivity()).getSupportActionBar().setTitle(budgetName+" Budget Pie Chart");

        View rootView = inflater.inflate(R.layout.fragment_graph, container, false);
          eatingOutCount = budgetDbHelper.getCategoryCount(month_id, "Eating Out");
          transportationCount = budgetDbHelper.getCategoryCount(month_id,"Transportation");
          rentCount= budgetDbHelper.getCategoryCount(month_id, "Rent");
          medicalCount =  budgetDbHelper.getCategoryCount(month_id,"Medical");
          uncategorizedCount =budgetDbHelper.getCategoryCount(month_id, "Uncategorized");
          utilitiesCount = budgetDbHelper.getCategoryCount(month_id,"Utilities");
          shoppingCount = budgetDbHelper.getCategoryCount(month_id,"Shopping");
          groceriesCount =  budgetDbHelper.getCategoryCount(month_id,"Groceries");
          incomeCount =budgetDbHelper.getCategoryCount(month_id,"Income");
         Log.i("Rony", "Income count is " + String.valueOf(incomeCount));
         Log.i("Rony", "rent count is " + String.valueOf(medicalCount));
        pieChartValues = new int[] {eatingOutCount,transportationCount,rentCount,medicalCount,uncategorizedCount,utilitiesCount, shoppingCount,groceriesCount,incomeCount};

        mRenderer.setApplyBackgroundColor(true);
        mRenderer.isInScroll();
        mRenderer.setBackgroundColor(Color.argb(100, 50, 50, 50));
        mRenderer.setChartTitleTextSize(20);
        mRenderer.setLabelsTextSize(15);
        mRenderer.setLegendTextSize(15);
        mRenderer.setMargins(new int[] { 20, 30, 15});// 0,5,5,5,5,5 });
        mRenderer.setZoomButtonsVisible(true);
        mRenderer.setStartAngle(90);

        if (mChartView == null) {
            LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.chart_layout);
            mChartView = ChartFactory.getPieChartView(getActivity(), mSeries, mRenderer);
            mRenderer.setClickEnabled(true);
            mRenderer.setSelectableBuffer(10);
            layout.addView(mChartView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.FILL_PARENT));
        } else {
            mChartView.repaint();
        }

        fillPieChart();
        return rootView;
    }
    //To populate the Pie Chart with values provided in array-PieChartValues
    public void fillPieChart(){

        for(int i=0;i<pieChartValues.length;i++)
        {
            mSeries.add(CATEGORY[i], pieChartValues[i]);
            SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
            renderer.setColor(COLORS[(mSeries.getItemCount() - 1) % COLORS.length]);
            mRenderer.addSeriesRenderer(renderer);
            if (mChartView != null)
                mChartView.repaint();
        }
    }


}
