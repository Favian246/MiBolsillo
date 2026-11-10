package com.mibolsillo.app;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;
import android.view.ViewGroup;

public class WidgetConfigureActivity extends Activity {
  @Override public void onCreate(Bundle state){super.onCreate(state);
    int id=getIntent().getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,AppWidgetManager.INVALID_APPWIDGET_ID);
    Intent canceled=new Intent();canceled.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,id);setResult(RESULT_CANCELED,canceled);
    LinearLayout root=new LinearLayout(this);root.setOrientation(1);root.setPadding(34,55,34,40);
    TextView title=new TextView(this);title.setText("¿Qué cuenta quieres mostrar en el widget?");title.setTextSize(22);root.addView(title);
    for(String account:new String[]{"personal","negocio"}){Button b=new Button(this);b.setText(account.equals("personal")?"👤 Personal":"🏪 Negocio");
      root.addView(b,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
      b.setOnClickListener(v->{if(id==AppWidgetManager.INVALID_APPWIDGET_ID)return;
        getSharedPreferences("widget_summary",MODE_PRIVATE).edit().putString("account_"+id,account).apply();
        BalanceWidget.updateAll(this);
        Intent ok=new Intent();ok.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,id);setResult(RESULT_OK,ok);finish();});
    }
    setContentView(root);
  }
}
