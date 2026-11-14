package org.rudy.chat.ui.Charts.view_data;

import android.graphics.Paint;

import org.rudy.chat.ui.Charts.BaseChartView;
import org.rudy.chat.ui.Charts.data.ChartData;

public class StackLinearViewData extends LineViewData {

    public StackLinearViewData(ChartData.Line line) {
        super(line, false);
        paint.setStyle(Paint.Style.FILL);
        if (BaseChartView.USE_LINES) {
            paint.setAntiAlias(false);
        }
    }
}
