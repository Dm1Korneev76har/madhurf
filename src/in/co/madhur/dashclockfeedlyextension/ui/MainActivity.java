package in.co.madhur.dashclockfeedlyextension.ui;

import com.infospace.android.oauth2.LoginListener;

import in.co.madhur.dashclockfeedlyextension.App;
import in.co.madhur.dashclockfeedlyextension.R;

import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.annotation.TargetApi;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v7.widget.Toolbar;

public class MainActivity extends BaseActivity implements OnBackStackChangedListener, LoginListener

{
    private boolean isActivityActive = true;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //SetupStrictMode();

        super.onCreate(savedInstanceState);


        getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new MainFragment()).commit();

        // Listen for changes in the back stack
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        // Handle when activity is recreated like on orientation Change


        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayUseLogoEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);

            shouldDisplayHomeUp();
        }

        PreferenceManager.setDefaultValues(this, R.xml.settings_layout, false);

    }

    private void SetupStrictMode() {
        if (App.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork() // or
                    // .detectAll()
                    // for
                    // all
                    // detectable
                    // problems
                    .penaltyLog().build());
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setActivityActive(false);
    }

    @Override
    public void onBackStackChanged() {
        shouldDisplayHomeUp();
    }

    public void shouldDisplayHomeUp() {
        // Enable Up button only if there are entries in the back stack
        boolean canback = getSupportFragmentManager().getBackStackEntryCount() > 0;
        getSupportActionBar().setDisplayHomeAsUpEnabled(canback);
    }

    @Override
    public boolean onSupportNavigateUp() {
        // This method is called when the up button is pressed. Just the pop
        // back stack.
        getSupportFragmentManager().popBackStack();
        return true;
    }

    @Override
    public void Login() {
        MainFragment mainFragment = new MainFragment();
        Bundle data = new Bundle();
        data.putBoolean("refresh", true);
        mainFragment.setArguments(data);
        getSupportFragmentManager().beginTransaction().replace(android.R.id.content, mainFragment).commit();
    }

    public boolean isActivityActive() {
        return isActivityActive;
    }

    public void setActivityActive(boolean isActivityActive) {
        this.isActivityActive = isActivityActive;
    }

}
