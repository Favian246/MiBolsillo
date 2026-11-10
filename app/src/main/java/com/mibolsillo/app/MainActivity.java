package com.mibolsillo.app;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebResourceRequest;
import android.widget.Toast;
import org.json.JSONObject;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends Activity {
    private static final int PICK_FILE = 100;
    private static final int SAVE_FILE = 101;
    private static final int SPEECH = 102;
    private WebView web;
    private ValueCallback<Uri[]> fileCallback;
    private byte[] pendingExport;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        web = new WebView(this);
        web.setBackgroundColor(0xFF0B1220);
        setContentView(web);
        WebSettings settings=web.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(false);
        settings.setAllowContentAccess(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(false);
        web.addJavascriptInterface(new NativeBridge(), "AndroidBridge");
        web.setWebViewClient(new WebViewClient() {
            @Override public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return !"appassets.androidplatform.net".equals(request.getUrl().getHost());
            }
        });
        web.setWebChromeClient(new WebChromeClient() {
            @Override public boolean onShowFileChooser(WebView w, ValueCallback<Uri[]> callback, FileChooserParams params) {
                if(fileCallback != null) fileCallback.onReceiveValue(null);
                fileCallback = callback;
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("application/json");
                try { startActivityForResult(intent, PICK_FILE); }
                catch (ActivityNotFoundException err) { fileCallback.onReceiveValue(null); fileCallback=null; return false; }
                return true;
            }
        });
        try (InputStream in = getAssets().open("index.html"); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[8192]; int n;
            while ((n=in.read(buffer)) != -1) out.write(buffer,0,n);
            // HTTPS origin makes localStorage work and keeps files bundled inside the app.
            web.loadDataWithBaseURL("https://appassets.androidplatform.net/", out.toString("UTF-8"), "text/html", "UTF-8", null);
        } catch(Exception e) { Toast.makeText(this, "Error al abrir la app", Toast.LENGTH_LONG).show(); }
    }

    private class NativeBridge {
        @JavascriptInterface public void saveText(String filename, String content, String mime) {
            runOnUiThread(() -> {
                pendingExport = content.getBytes(StandardCharsets.UTF_8);
                Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType(mime.startsWith("text/csv") ? "text/csv" : "application/json");
                intent.putExtra(Intent.EXTRA_TITLE, filename);
                try { startActivityForResult(intent, SAVE_FILE); }
                catch(ActivityNotFoundException e) { Toast.makeText(MainActivity.this,"No hay gestor de archivos",Toast.LENGTH_LONG).show(); }
            });
        }
        @JavascriptInterface public void startDictation() {
            runOnUiThread(() -> {
                Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"es-BO");
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Di tu gasto o ingreso");
                try { startActivityForResult(intent, SPEECH); }
                catch(ActivityNotFoundException e) { Toast.makeText(MainActivity.this,"No hay reconocimiento de voz. Usa el teclado.",Toast.LENGTH_LONG).show(); }
            });
        }
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==PICK_FILE) {
            if(fileCallback!=null) { fileCallback.onReceiveValue(resultCode==RESULT_OK && data!=null ? new Uri[]{data.getData()}:null); fileCallback=null; }
        } else if(requestCode==SAVE_FILE) {
            if(resultCode==RESULT_OK && data!=null && pendingExport!=null) {
                try(OutputStream out=getContentResolver().openOutputStream(data.getData())) {
                    if(out==null) throw new Exception("No output stream");
                    out.write(pendingExport);
                    Toast.makeText(this,"Archivo guardado",Toast.LENGTH_SHORT).show();
                } catch(Exception e) { Toast.makeText(this,"No se pudo guardar",Toast.LENGTH_LONG).show(); }
            }
            pendingExport=null;
        } else if(requestCode==SPEECH && resultCode==RESULT_OK && data!=null) {
            ArrayList<String> results=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if(results!=null && !results.isEmpty()) {
                web.evaluateJavascript("window.receiveAndroidDictation("+JSONObject.quote(results.get(0))+")",null);
            }
        }
    }
    @Override public void onBackPressed() {
        // This is a single-page application; exit rather than following the WebView's internal history.
        super.onBackPressed();
    }
    @Override protected void onDestroy() {
        if(web!=null) { web.destroy(); web=null; }
        super.onDestroy();
    }
}
