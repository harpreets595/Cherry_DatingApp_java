package com.example.project.ui.accounts;

import androidx.fragment.app.Fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.CherryApplication;
import com.example.project.MainActivity;
import com.example.project.R;
import com.example.project.models.Gender;
import com.example.project.models.Profile;
import com.example.project.networking.HttpRequest;
import com.example.project.networking.HttpRequestTask;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

/**
 * A placeholder fragment containing a simple view.
 */
public class ProfileCreationActivityFragment extends Fragment {

    // Used to hash passwords.
    private String hashPassword(String password) {

        // http://stackoverflow.com/questions/3103652/hash-string-via-sha-256-in-java
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(password.getBytes());
            byte[] digest = md.digest();
            return String.format("%040x", new BigInteger(1, digest));
        } catch (NoSuchAlgorithmException e1) {
            // should not happen
        }
        return null;
    }

    private CherryApplication application;
    public ProfileCreationActivityFragment() {
    }

    private void CreateProfile(Profile profile) {
        application = (CherryApplication) getActivity().getApplication();
        HttpRequest req = new HttpRequest(application.host + "/user", HttpRequest.Method.POST);
        req.setRequestBody("application/json", profile.format(application));
        HttpRequestTask task = new HttpRequestTask();
        task.setOnErrorListener(error -> {
        });
        task.setOnResponseListener(response -> {

            String path = response.getHeaders().get("Location").get(0);
            String uuid = path.replace(application.host+"/user/", "");
            application.currentUserUuid=uuid;
            application.currentUser.uuid=uuid;



        });
        task.execute(req);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_profile_creation, container, false);

        //Create the profile
        Profile temp = new Profile();

        //Get all elements
        ImageButton exit = root.findViewById(R.id.undo_create);
        ImageButton create = root.findViewById(R.id.profile_create);
        TextView firstname= root.findViewById(R.id.firstname);
        TextView lastname = root.findViewById(R.id.lastname);
        TextView username = root.findViewById(R.id.username);
        TextView password = root.findViewById(R.id.password);
        TextView email = root.findViewById(R.id.email);
        TextView introduction =root.findViewById(R.id.Introduction);
        Button date_picker=root.findViewById(R.id.dob_picker);
        RadioGroup gender = root.findViewById(R.id.gender_selection);
        RadioButton female= root.findViewById(R.id.female);
        RadioButton male = root.findViewById(R.id.male);
        RadioButton other = root.findViewById(R.id.other);
        //Listener for date picker
        date_picker.setOnClickListener(view -> {
       Calendar c= Calendar.getInstance();
       int day= c.get(Calendar.DAY_OF_MONTH);
       int month = c.get(Calendar.MONTH);
       int year = c.get(Calendar.YEAR);

            DatePickerDialog date_picker_dialog = new DatePickerDialog(getContext(), (datePicker, y, m, d) -> {
                Calendar bd = Calendar.getInstance();
                bd.set(y,m,d);
                temp.birthday =bd.getTime();
                temp.age =Calendar.getInstance().get(Calendar.YEAR)-bd.get(Calendar.YEAR);
                if (Calendar.getInstance().get(Calendar.DAY_OF_YEAR) < bd.get(Calendar.DAY_OF_YEAR)){
                    temp.age =temp.age--;
                }
            }, year, month, day);
            date_picker_dialog.show();
        });


        //Listener for exit
        exit.setOnClickListener(view -> {
        getActivity().finish();
        });
        //Listener for create
        create.setOnClickListener(view -> {
        //Make sure everything is filled out
            if(firstname.getText().toString().equals("") || lastname.getText().toString().equals("") || username.getText().toString().equals("") ||
                    password.getText().toString().equals("") || email.getText().toString().equals("") || introduction.getText().toString().equals("")
                    || gender.getCheckedRadioButtonId()==-1 || temp.birthday ==null) {

                Toast.makeText(getContext(), "Please fill out all fields before creating an account", Toast.LENGTH_SHORT).show();
            }else if (temp.age <18){
                Toast.makeText(getContext(), "You cannot be under the age of 18 to register for a dating app", Toast.LENGTH_SHORT).show();
            }
            else{
                //Create account
                Profile account = new Profile();
                account.firstname =firstname.getText().toString();
                account.lastname =lastname.getText().toString();
                account.username =username.getText().toString();
                account.introduction =introduction.getText().toString();
                account.password =hashPassword(password.getText().toString());
                account.birthday =temp.birthday;
                account.age =temp.age;
                account.email=email.getText().toString();
                account.astrologicalSign =account.getAstrologicalSign(temp.birthday);
                //Get radio button value
                if(gender.getCheckedRadioButtonId()==female.getId()){
                    account.gender =Gender.Female;
                }else if(gender.getCheckedRadioButtonId()==male.getId()){
                    account.gender =Gender.Male;
                }else{
                    account.gender =Gender.Other;
                }

                account.image =null;

                CreateProfile(account);
                CherryApplication app = (CherryApplication) getActivity().getApplication();
                app.currentUser=account;
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }

        });
        return root;
    }



}
