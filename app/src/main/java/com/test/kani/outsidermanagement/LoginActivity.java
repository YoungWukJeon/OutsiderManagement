package com.test.kani.outsidermanagement;

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

import java.util.HashMap;

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
//    private View mProgressView;
//    private View mLoginFormView;

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
                    attemptLogin();
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

//        mLoginFormView = findViewById(R.id.login_form);
//        mProgressView = findViewById(R.id.login_progress);
    }

    private void populateAutoComplete()
    {
        if (!mayRequestContacts())
        {
            return;
        }

//        getLoaderManager().initLoader(0, null, this);
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
//        if (mAuthTask != null)
//        {
//            return;
//        }

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
//            showProgress(true);
//            mAuthTask = new UserLoginTask(id, password);
//            mAuthTask.execute((Void) null);

                this.setFireStoreCallbackListener(new FireStoreCallbackListener()
                {
                    final int ID_NOT_EXISTED = 0;
                    final int PASSWORD_NOT_MATCHED = 1;
                    final int TASK_FAILURE = 2;

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

                        if (obj == null)
                            occurError(ID_NOT_EXISTED);
                        else
                        {
                            MainActivity.myInfoMap = (HashMap<String, Object>) obj;

                            if (password.equals(MainActivity.myInfoMap.get("password").toString().trim()))
                            {
                                MainActivity.myInfoMap.put("id", id);
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            }
                            else
                            {
                                occurError(PASSWORD_NOT_MATCHED);
                                MainActivity.myInfoMap = null;
                            }
                        }
                    }
                });

                if( this.loadingDialog == null )
                    this.loadingDialog = new LoadingDialog(this);

                this.loadingDialog.show("Login");
                FireStoreConnectionPool.getInstance().select(fireStoreCallbackListener, "outsider", "member", "user", id);
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
        return password.length() > 4;
    }
//
//    /**
//     * Shows the progress UI and hides the login form.
//     */
//    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
//    private void showProgress(final boolean show)
//    {
//        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
//        // for very easy animations. If available, use these APIs to fade-in
//        // the progress spinner.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
//        {
//            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
//
//            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
//                    show ? 0 : 1).setListener(new AnimatorListenerAdapter()
//            {
//                @Override
//                public void onAnimationEnd(Animator animation)
//                {
//                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//                }
//            });
//
//            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//            mProgressView.animate().setDuration(shortAnimTime).alpha(
//                    show ? 1 : 0).setListener(new AnimatorListenerAdapter()
//            {
//                @Override
//                public void onAnimationEnd(Animator animation)
//                {
//                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//                }
//            });
//        }
//        else
//        {
//            // The ViewPropertyAnimator APIs are not available, so simply show
//            // and hide the relevant UI components.
//            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//        }
//    }
//
//    @Override
//    public Loader<Cursor> onCreateLoader(int i, Bundle bundle)
//    {
//        return new CursorLoader(this,
//                // Retrieve data rows for the device user's 'profile' contact.
//                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
//                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,
//
//                // Select only email addresses.
//                ContactsContract.Contacts.Data.MIMETYPE +
//                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
//                .CONTENT_ITEM_TYPE},
//
//                // Show primary email addresses first. Note that there won't be
//                // a primary email address if the user hasn't specified one.
//                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
//    }
//
//    @Override
//    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor)
//    {
//        List<String> emails = new ArrayList<>();
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast())
//        {
//            emails.add(cursor.getString(ProfileQuery.ADDRESS));
//            cursor.moveToNext();
//        }
//
//        addEmailsToAutoComplete(emails);
//    }
//
//    @Override
//    public void onLoaderReset(Loader<Cursor> cursorLoader)
//    {
//
//    }
//
//    private void addEmailsToAutoComplete(List<String> emailAddressCollection)
//    {
//        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
//        ArrayAdapter<String> adapter =
//                new ArrayAdapter<>(LoginActivity.this,
//                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);
//
//        idAutoCompleteTextView.setAdapter(adapter);
//    }
//
//
//    private interface ProfileQuery
//    {
//        String[] PROJECTION = {
//                ContactsContract.CommonDataKinds.Email.ADDRESS,
//                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
//        };
//
//        int ADDRESS = 0;
//        int IS_PRIMARY = 1;
//    }
//
//    /**
//     * Represents an asynchronous login/registration task used to authenticate
//     * the user.
//     */
//    public class UserLoginTask extends AsyncTask<Void, Void, Boolean>
//    {
//
//        private final String mId;
//        private final String mPassword;
//
//        UserLoginTask(String id, String password)
//        {
//            mId = id;
//            mPassword = password;
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... params)
//        {
//            // TODO: attempt authentication against a network service.
//
//            try
//            {
//                // Simulate network access.
//                Thread.sleep(2000);
//            }
//            catch (InterruptedException e)
//            {
//                return false;
//            }
//
//            for (String credential : DUMMY_CREDENTIALS)
//            {
//                String[] pieces = credential.split(":");
//                if (pieces[0].equals(mId))
//                {
//                    // Account exists, return true if the password matches.
//                    return pieces[1].equals(mPassword);
//                }
//            }
//
//            // TODO: register the new account here.
//            return true;
//        }
//
//        @Override
//        protected void onPostExecute(final Boolean success)
//        {
//            mAuthTask = null;
//            showProgress(false);
//
//            if (success)
//            {
//                finish();
//            }
//            else
//            {
//                passwordEditText.setError(getString(R.string.error_incorrect_password));
//                passwordEditText.requestFocus();
//            }
//        }
//
//        @Override
//        protected void onCancelled()
//        {
//            mAuthTask = null;
//            showProgress(false);
//        }
//    }

}

