package com.test.kani.outsidermanagement.Activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.test.kani.outsidermanagement.Utilitiy.FireStoreCallbackListener;
import com.test.kani.outsidermanagement.Utilitiy.FireStoreConnectionPool;
import com.test.kani.outsidermanagement.Utilitiy.LoadingDialog;
import com.test.kani.outsidermanagement.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity
{
    public FireStoreCallbackListener fireStoreCallbackListener;
    private LoadingDialog loadingDialog;

    public void setFireStoreCallbackListener(FireStoreCallbackListener listener)
    {
        this.fireStoreCallbackListener = listener;
    }


    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
//    private UserLoginTask mAuthTask = null;

    // UI references.
    AutoCompleteTextView idAutoCompleteTextView;
    EditText passwordEditText;
    Button loginBtn, registBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        this.idAutoCompleteTextView = findViewById(R.id.id_autoCompleteTextView);
        populateAutoComplete();

        this.passwordEditText = findViewById(R.id.password_editText);
        this.passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent)
            {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL)
                {
                    // 키보드에서 완료 눌렀을 경우(에뮬에서는 enter 도 포함)
                    return true;
                }
                return false;
            }
        });

        loginBtn = findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                attemptLogin();
            }
        });

        registBtn = findViewById(R.id.regist_btn);
        registBtn.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(LoginActivity.this, RegistActivity.class));
            }
        });
    }

    private void populateAutoComplete()
    {
        if (!mayRequestContacts())
        {
            return;
        }
    }

    private boolean mayRequestContacts()
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
        {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS))
        {
            Snackbar.make(idAutoCompleteTextView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener()
                    {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v)
                        {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        }
        else
        {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        if (requestCode == REQUEST_READ_CONTACTS)
        {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin()
    {
        // Reset errors.
        idAutoCompleteTextView.setError(null);
        passwordEditText.setError(null);

        // Store values at the time of the login attempt.
        final String id = idAutoCompleteTextView.getText().toString();
        final String password = passwordEditText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password))
        {
            passwordEditText.setError(getString(R.string.error_invalid_password));
            focusView = passwordEditText;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(id))
        {
            idAutoCompleteTextView.setError(getString(R.string.error_field_required));
            focusView = idAutoCompleteTextView;
            cancel = true;
        }
        else if (!isIdValid(id))
        {
            idAutoCompleteTextView.setError(getString(R.string.error_invalid_email));
            focusView = idAutoCompleteTextView;
            cancel = true;
        }

        if (cancel)
        {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }
        else
        {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            this.setFireStoreCallbackListener(new FireStoreCallbackListener()
            {
                final int ID_NOT_EXISTED = 0;
                final int PASSWORD_NOT_MATCHED = 1;
                final int TASK_FAILURE = 2;

                boolean flag = false;

                @Override
                public void occurError(int errorCode)
                {
                    switch (errorCode)
                    {
                        case ID_NOT_EXISTED:
                            Log.d("LoginActivity", "This ID is not existed");
                            idAutoCompleteTextView.setError("This ID is not existed");
                            idAutoCompleteTextView.selectAll();
                            idAutoCompleteTextView.requestFocus();
                            break;
                        case PASSWORD_NOT_MATCHED:
                            Log.d("LoginActivity", "Password is not matched");
                            passwordEditText.setError("Password is not matched");
                            passwordEditText.selectAll();
                            passwordEditText.requestFocus();
                            break;
                        case TASK_FAILURE:
                            Log.d("LoginActivity", "Task is not successful");
                            break;
                        default:
                            break;
                    }
                }

                @Override
                public void doNext(boolean isSuccesful, Object obj)
                {
                    if( loadingDialog != null )
                        loadingDialog.dismiss();

                    if( !isSuccesful )
                    {
                        occurError(TASK_FAILURE);
                        return;
                    }

                    if (obj == null && !flag)
                        occurError(ID_NOT_EXISTED);
                    else if( obj == null && flag )
                    {
                        MainActivity.myInfoMap.put("isOutsider", false);

                        Map<String, Object> tempMap = new HashMap<> ();
                        tempMap.put("isOutsider", false);
                        tempMap.put("outsiderType", null);

                        loadingDialog.show("Updating Outsider Flag");

                        FireStoreConnectionPool.getInstance().update(fireStoreCallbackListener, tempMap, "member", id);  // false
                    }
                    else if( obj != null && !flag )
                    {
                        MainActivity.myInfoMap = (HashMap<String, Object>) obj;

                        if (password.equals(MainActivity.myInfoMap.get("password").toString().trim()))
                        {
                            flag = true;
                            MainActivity.myInfoMap.put("id", id);

                            loadingDialog.show("Searching for Outsider History");

                            FireStoreConnectionPool.getInstance().selectLessThanDate(fireStoreCallbackListener,
                                    "outsider", "memberId", id,
                                    "startDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                        }
                        else
                        {
                            occurError(PASSWORD_NOT_MATCHED);
                            MainActivity.myInfoMap = null;
                        }
                    }
                    else if( obj != null && flag && !(obj instanceof Boolean) )
                    {
                        ArrayList<HashMap<String, Object>> tempList = (ArrayList<HashMap<String, Object>>) obj;

                        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

                        Iterator<HashMap<String, Object>> listIter = tempList.iterator();

                        while( listIter.hasNext() )
                        {
                            HashMap<String, Object> map = listIter.next();

                            if (map.get("endDate").toString().compareTo(today) <= 0)
                                listIter.remove();
                        }

                        if( tempList.isEmpty() )        // 현재 날짜 기준으로 출타 중이지 않을 경우
                        {
                            MainActivity.myInfoMap.put("isOutsider", false);
                            Map<String, Object> tempMap = new HashMap<> ();
                            tempMap.put("isOutsider", false);
                            tempMap.put("outsiderType", null);

                            loadingDialog.show("Updating Outsider Flag");

                            FireStoreConnectionPool.getInstance().update(fireStoreCallbackListener, tempMap, "member", id);  // false
                        }
                        else
                        {
                            MainActivity.myInfoMap.put("isOutsider", true);
                            MainActivity.myInfoMap.put("outsiderType", tempList.get(0).get("outsiderType"));
                            Map<String, Object> tempMap = new HashMap<> ();
                            tempMap.put("isOutsider", true);
                            tempMap.put("outsiderType", tempList.get(0).get("outsiderType"));

                            loadingDialog.show("Updating Outsider Flag");

                            FireStoreConnectionPool.getInstance().update(fireStoreCallbackListener, tempMap, "member", id);  // false
                        }
                    }
                    else
                    {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                }
            });


            if( this.loadingDialog == null )
                this.loadingDialog = new LoadingDialog(this);

            this.loadingDialog.show("Login");
            FireStoreConnectionPool.getInstance().selectOne(fireStoreCallbackListener, "member", id);
        }
    }

    private boolean isIdValid(String id)
    {
        //TODO: Replace this with your own logic
//        return email.contains("@");
        return id.contains("-");
    }

    private boolean isPasswordValid(String password)
    {
        //TODO: Replace this with your own logic
        return password.length() > 2;
    }
}

