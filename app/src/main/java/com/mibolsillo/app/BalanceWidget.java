package com.mibolsillo.app;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import org.json.JSONObject;
import java.util.Locale;

public class BalanceWidget extends AppWidgetProvider {
    public static void updateAll(Context c){
        AppWidgetManager am=AppWidgetManager.getInstance(c);
        android.content.ComponentName component=new android.content.ComponentName(c,BalanceWidget.class);
        onUpdateWidgets(c,am,am.getAppWidgetIds(component));
    }
    static void onUpdateWidgets(Context c,AppWidgetManager manager,int[] ids){
        SharedPreferences prefs=c.getSharedPreferences("widget_summary",Context.MODE_PRIVATE);
        JSONObject summary;
        try{summary=new JSONObject(prefs.getString("summary","{}"));}catch(Exception e){summary=new JSONObject();}
        for(int id:ids){
            String account=prefs.getString("account_"+id,"personal");
            JSONObject values=summary.optJSONObject(account);
            String code=values==null?"BOB":values.optString("currency","BOB");
            String symbol="BOB".equals(code)?"Bs":("USD".equals(code)?"US$":("EUR".equals(code)?"€":("BRL".equals(code)?"R$":"AR$")));
            double balance=values==null?0:values.optDouble("balance",0);
            double expense=values==null?0:values.optDouble("expenseToday",0);
            RemoteViews rv=new RemoteViews(c.getPackageName(),R.layout.wallet_widget);
            rv.setTextViewText(R.id.widget_title,"Mi Bolsillo · "+(account.equals("personal")?"Personal":"Negocio"));
            rv.setTextViewText(R.id.widget_balance,symbol+" "+String.format(Locale.US,"%,.2f",balance));
            rv.setTextViewText(R.id.widget_expense,"Gasto de hoy: "+symbol+" "+String.format(Locale.US,"%,.2f",expense));
            Intent i=new Intent(c,MainActivity.class);i.putExtra("quick",true);i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pending=PendingIntent.getActivity(c,10000+id,i,PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);
            rv.setOnClickPendingIntent(R.id.widget_add,pending);
            rv.setOnClickPendingIntent(R.id.widget_root,pending);
            manager.updateAppWidget(id,rv);
        }
    }
    @Override public void onUpdate(Context c,AppWidgetManager m,int[] ids){onUpdateWidgets(c,m,ids);}
    @Override public void onDeleted(Context c,int[] ids){SharedPreferences.Editor e=c.getSharedPreferences("widget_summary",Context.MODE_PRIVATE).edit();for(int id:ids)e.remove("account_"+id);e.apply();}
}
