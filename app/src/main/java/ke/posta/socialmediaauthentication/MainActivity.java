package ke.posta.socialmediaauthentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookRequestError;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.internal.ImageRequest;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TAG";
    CallbackManager callbackManager = CallbackManager.Factory.create();
    LoginButton loginButton;

    FacebookRequestError facebookRequestError;

    private static final String EMAIL = "email";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends","user_gender"));
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            // UiZZyM02H04t10z3LmJBuKwiTnY=
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.d(TAG, "onSuccess: "+loginResult.toString());
                Log.d(TAG, "onSuccess: "+loginResult.getAccessToken().getUserId());
                Log.d(TAG, "onSuccess: "+loginResult.getAccessToken().getPermissions()); // [openid, public_profile, email]


                /**
                 * 2022-02-03 15:24:19.400 3008-3008/ke.posta.mediaauthentication
                 * D/TAG: onSuccess: LoginResult(
                 * accessToken={AccessToken token:ACCESS_TOKEN_REMOVED permissions:[openid, public_profile, email]},
                 * authenticationToken=null,
                 * recentlyGrantedPermissions=[openid, public_profile, email],
                 * recentlyDeniedPermissions=[])
                 * **/

                Log.d(TAG, "onSuccess: token"+loginResult.getAccessToken().getToken());
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {

                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                Log.d(TAG, "onCompleted: "+response.toString());
                                Log.d(TAG, "onCompleted: "+object.toString());
                                // Application code
                                try {
                                    Log.d(TAG, "onCompleted: "+object.get("user_gender"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Log.d(TAG, "onCompleted: "+ response.getError());
                                Log.d(TAG, "onCompleted: "+response.getJSONArray());
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                // App code
                Log.d(TAG, "onCancel: "+"cancelled");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.d(TAG, "onError: "+exception.toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: "+data.toString());
        Log.d(TAG, "onActivityResult: "+data.getDataString());
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * marykonyango@marykonyango-HP-Laptop-14-cf2xxx:~$ keytool -exportcert -alias media.jks -keystore /home/marykonyango/AndroidStudioProjects/SocialMediaAuthentication | openssl sha1 -binary | openssl base64
     * UiZZyM02H04t10z3LmJBuKwiTnY=
     */

}