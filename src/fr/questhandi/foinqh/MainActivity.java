package fr.questhandi.foinqh;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

public class MainActivity extends Activity {

	private FrameLayout mTargetView;
	private FrameLayout mContentView;
	
	private CustomViewCallback mCustomViewCallback;
	private View mCustomView;
	private MyChromeClient mClient;
		
	private ListView list = null;
	private DrawerLayout menuLayout= null;
	private ActionBarDrawerToggle menuToggle;
	private WebView webView;
	
	private ArrayAdapter<String> adapterList;
	private ArrayList<String> elements;
	
	private String title = Utils.TITLE_ACCUEIL;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mContentView = (FrameLayout) findViewById(R.id.main_content);
		mTargetView = (FrameLayout)findViewById(R.id.target_view);

		list = (ListView) findViewById(R.id.menu_elements);
		list.setBackgroundResource(R.drawable.fondetgauche);
		
		menuLayout = (DrawerLayout) findViewById(R.id.menu_layout);
		
		webView = (WebView) findViewById(R.id.webView);
		mClient = new MyChromeClient();
		WebSettings webSetting = webView.getSettings();
		webSetting.setJavaScriptEnabled(true);
		webView.setWebChromeClient(mClient);
		webView.setWebViewClient(new WebViewClient(){
		    public boolean shouldOverrideUrlLoading(WebView view, String url)
		    {
		        if (url.startsWith("afdlr-qh-video"))
		        {
		            try
		            {
		            	int pos = url.indexOf("url=");
		            	String video_url = url.substring(pos + 4);
		            	System.out.println(video_url);
						String extension = MimeTypeMap.getFileExtensionFromUrl(video_url);
						String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
						Intent mediaIntent = new Intent(Intent.ACTION_VIEW);
						mediaIntent.setDataAndType(Uri.parse(video_url), mimeType);
						startActivity(mediaIntent);

		            }
		            catch (Exception e)
		            {
		            }

		            return (true);
		        }
		        return false;
		    }
		});
			
		menuLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		
		elements = new ArrayList<String>();
		elements.add("Live");
		
		adapterList = new ArrayAdapter<String>(MainActivity.this,R.layout.drawer_item,elements);
		adapterList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		list.setAdapter(adapterList);
		list.setOnItemClickListener(new DrawerItemClickListener());
		
		menuToggle = new ActionBarDrawerToggle(this, /* host Activity */
		menuLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(title);
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(title);
			}
		};
		menuLayout.setDrawerListener(menuToggle);
		
		webView.loadUrl(Utils.URL_ACCUEIL);
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
        
        return super.onCreateOptionsMenu(menu);
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if (menuToggle.onOptionsItemSelected(item)) {
			return true;
		}
		
		switch (item.getItemId()) {
		
			case R.id.acceuil:
				//Changement du titre
				title = Utils.TITLE_ACCUEIL;
				getActionBar().setTitle(title);
				
				//Changement du contenu de la liste
				elements = new ArrayList<String>();
				elements.add(Utils.MENU_LIVE);
				
				adapterList.clear();
				adapterList.addAll(elements);
				
				webView.loadUrl(Utils.URL_ACCUEIL);
				
				return true;
			case R.id.assoc_1:
				list.setBackgroundResource(R.drawable.fondetgauche);
				
				//Changement du titre
				title = Utils.TITLE_ASSOAFDLR;
				getActionBar().setTitle(title);
			
				//Changement du contenu de la liste
				elements = new ArrayList<String>();
				elements.add(Utils.MENU_ASSOAFDLR);
				elements.add(Utils.MENU_PROGRAMMATION);
				elements.add(Utils.MENU_ARTISTES);
				elements.add(Utils.MENU_PLAN);
				elements.add(Utils.MENU_LIVE);
				elements.add(Utils.MENU_SITE);
				elements.add(Utils.MENU_BILLETERIE);
			
				adapterList.clear();
				adapterList.addAll(elements);
			
				webView.loadUrl(Utils.URL_ASSOAFDLR);
				return true;
			case R.id.assoc_2:
				list.setBackgroundResource(R.drawable.menuqh);
				
				//Changement du titre
				title = Utils.TITLE_ASSOQH;
				getActionBar().setTitle(title);
			
				//Changement du contenu de la liste
				elements = new ArrayList<String>();
				elements.add("");
				elements.add(Utils.MENU_ASSOQH);
				elements.add(Utils.MENU_ACTIONS);
				elements.add(Utils.MENU_AGENDA);
				elements.add(Utils.MENU_LIVE);
				elements.add(Utils.MENU_SITEQH);
				elements.add("");
			
				adapterList.clear();
				adapterList.addAll(elements);
			
				webView.loadUrl(Utils.URL_ASSOQH);
				return true;
		
			default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if(parent.getItemAtPosition(position).equals(Utils.MENU_LIVE)){
				title = "";
				title = Utils.TITLE_LIVE;
				//WebView Affichage
				webView.loadUrl(Utils.URL_LIVE);		
			}else if(parent.getItemAtPosition(position).equals(Utils.MENU_ASSOAFDLR)){
				title = "";
				title = Utils.TITLE_ASSOAFDLR;
				//WebView Affichage
				webView.loadUrl(Utils.URL_ASSOAFDLR);
			}else if(parent.getItemAtPosition(position).equals(Utils.MENU_PLAN)){
				title = "";
				title = Utils.TITLE_PLAN;
				//WebView Affichage
				webView.loadUrl(Utils.URL_PLAN);
			}else if(parent.getItemAtPosition(position).equals(Utils.MENU_PROGRAMMATION)){
				title = "";
				title = Utils.TITLE_PROGRAMMATION;
				//WebView Affichage
				webView.loadUrl(Utils.URL_PROGRAMMATION);
			}else if(parent.getItemAtPosition(position).equals(Utils.MENU_ARTISTES)){
				title = "";
				title = Utils.TITLE_ARTISTES;
				//WebView Affichage
				webView.loadUrl(Utils.URL_ARTISTES);
			}else if(parent.getItemAtPosition(position).equals(Utils.MENU_SITE)) {
				title = "";
				title = Utils.TITLE_SITE;
				//WebView Affichage
				webView.loadUrl(Utils.URL_SITE);
			}else if(parent.getItemAtPosition(position).equals(Utils.MENU_BILLETERIE)) {
				title = "";
				title = Utils.TITLE_BILLETERIE;
				//WebView Affichage
				webView.loadUrl(Utils.URL_BILLETERIE);
			}else if(parent.getItemAtPosition(position).equals(Utils.MENU_ASSOQH)){
				title = "";
				title = Utils.TITLE_ASSOQH;
				//WebView Affichage
				webView.loadUrl(Utils.URL_ASSOQH);
			}else if(parent.getItemAtPosition(position).equals(Utils.MENU_AGENDA)){
				title = "";
				title = Utils.TITLE_AGENDA;
				//WebView Affichage
				webView.loadUrl(Utils.URL_AGENDA);
			}else if(parent.getItemAtPosition(position).equals(Utils.MENU_ACTIONS)){
				title = "";
				title = Utils.TITLE_ACTIONS;
				//WebView Affichage
				webView.loadUrl(Utils.URL_ACTIONS);
			}else if(parent.getItemAtPosition(position).equals(Utils.MENU_SITEQH)){
				title = "";
				title = Utils.TITLE_SITEQH;
				//WebView Affichage
				webView.loadUrl(Utils.URL_SITEQH);
			}
			menuLayout.closeDrawer(Gravity.START);
		}
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		menuToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		menuToggle.onConfigurationChanged(newConfig);
	}
	
	class MyChromeClient extends WebChromeClient {

		@Override
		public void onShowCustomView(View view, CustomViewCallback callback) {		
		    mCustomViewCallback = callback;
			mTargetView.addView(view);
			mCustomView = view;
			mContentView.setVisibility(View.GONE);
			mTargetView.setVisibility(View.VISIBLE);
			mTargetView.bringToFront();
		}

		@Override
		public void onHideCustomView() {
			if (mCustomView == null)
				return;

			mCustomView.setVisibility(View.GONE);
			mTargetView.removeView(mCustomView);
			mCustomView = null;
			mTargetView.setVisibility(View.GONE);			
			mCustomViewCallback.onCustomViewHidden();			
			mContentView.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	public void onBackPressed(){
		if (mCustomView != null){
			mClient.onHideCustomView();
		}else{
			finish();
		}
	}
}
