/*  Created on 20/08/17 by Ingen Dynamics Inc.
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.rathore.evernoteapi;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.asyncclient.EvernoteClientFactory;
import com.evernote.client.android.asyncclient.EvernoteHtmlHelper;
import com.evernote.client.android.asyncclient.EvernoteNoteStoreClient;
import com.evernote.client.android.helper.Cat;
import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.thrift.TException;
import com.squareup.okhttp.Response;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.IOException;

/* This class is used to shows the contents of note on webview.*/
public class ViewHtmlActivity extends AppCompatActivity {
    private static final Cat CAT = new Cat("ViewHtmlActivity");

    private static final String KEY_NOTE = "KEY_NOTE";
    private static final String KEY_HTML = "KEY_HTML";
    EditText editText;
    EvernoteNoteStoreClient noteStoreClient;
    com.rathore.evernoteapi.NoteRef noteRef;
    private NoteRef mNoteRef;
    private String mHtml;
    private EvernoteHtmlHelper mEvernoteHtmlHelper;

    public Intent createIntent(Context context, NoteRef note, String html) {

        Intent intent = new Intent(context, ViewHtmlActivity.class);
        intent.putExtra(KEY_NOTE, note);
        intent.putExtra(KEY_HTML, html);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        return intent;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html_view);
        noteRef = getIntent().getParcelableExtra(KEY_NOTE);
        mHtml = getIntent().getStringExtra(KEY_HTML);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        toolbar.setTitleTextColor(Color.WHITE);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolBarTitle);
        mTitle.setText(noteRef.getTitle());

        setSupportActionBar(toolbar);

        if (!isTaskRoot()) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        final WebView webView = (WebView) findViewById(R.id.webView);
        webView.setBackgroundColor(getResources().getColor(R.color.skyblue));
        webView.getSettings().setJavaScriptEnabled(true);


        webView.loadUrl("javascript:document.body.style.color=\"white\";");
        String message = "<font color='white'>" + "<u>" + "text in white" + "<br>" + "<font color='cyan'>" + "<font size='2'>" + " text in blue color " + "</font>";

        if (savedInstanceState == null) {
            String htmlData = "<font color='white'>" + "<font size='20'>" + mHtml + "</font>";

            webView.setWebViewClient(new WebViewClient() {

               // Return WebResourceResponse with CSS markup from a String.
               @SuppressWarnings("deprecation")
                public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                    try {
                        Response response = getEvernoteHtmlHelper().fetchEvernoteUrl(url);
                        WebResourceResponse webResourceResponse = toWebResource(response);
                        if (webResourceResponse != null) {
                            return webResourceResponse;
                        }

                    } catch (Exception e) {
                        CAT.e(e);
                    }

                    return super.shouldInterceptRequest(view, url);
                }
            });

            webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);


            webView.loadDataWithBaseURL(null, htmlData, "text/html", "UTF-8", null);
        }
        webView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("webview", "called");
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected WebResourceResponse toWebResource(Response response) throws IOException {
        if (response == null || !response.isSuccessful()) {
            return null;
        }

        String mimeType = response.header("Content-Type");
        String charset = response.header("charset");
        return new WebResourceResponse(mimeType, charset, response.body().byteStream());
    }

    // Provides helper methods to receive a note as HTML instead of ENML.
    //Makes a GET request to download the note content as HTML.
    protected com.evernote.client.android.asyncclient.EvernoteHtmlHelper getEvernoteHtmlHelper() throws EDAMUserException, EDAMSystemException, EDAMNotFoundException, TException {
        if (mEvernoteHtmlHelper == null) {
            EvernoteClientFactory clientFactory = EvernoteSession.getInstance().getEvernoteClientFactory();

            mEvernoteHtmlHelper = clientFactory.getHtmlHelperDefault();

        }

        return mEvernoteHtmlHelper;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();

    }

    public String parseHtml(String html) {

        String content = new String();
        org.jsoup.nodes.Document document = Jsoup.parse(html);
        Elements divs = document.select("div");

        for (org.jsoup.nodes.Element div : divs) {
            String content1 = div.ownText();
            content = content.concat(content1);
        }

        return content;
    }

}


