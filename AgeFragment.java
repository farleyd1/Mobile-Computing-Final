package com.example.farleyd1.musicians;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

/**
 * Created by farleyd1 on 12/7/2017.
 */

public class AgeFragment extends Fragment {
    private Context mContext;
    private Activity mActivity;
    JSONArray myJSON_array;

    ImageView ivComposer;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        final View myView = inflater.inflate(R.layout.age_fragment, container, false);

        mContext = getContext();
        mActivity = getActivity();

        final Spinner spComposer = (Spinner) myView.findViewById(R.id.spComposer);
        final EditText etYear = (EditText) myView.findViewById(R.id.etYear);
        final Button btnCalc = (Button) myView.findViewById(R.id.btnCalc);
        final CheckBox checkBox = (CheckBox) myView.findViewById(R.id.checkBox);
        ivComposer = (ImageView) myView.findViewById(R.id.ivComposer);
        final TextView tvAge = (TextView) myView.findViewById(R.id.tvAge);

        try{
            InputStream is = getResources().openRawResource(R.raw.composer);
            byte[] buffer = new byte[is.available()];
            while (is.read(buffer) != -1);

            String myText = new String(buffer);
            JSONObject myJSON_Object = new JSONObject(myText);
            myJSON_array = myJSON_Object.getJSONArray("composers");

            String [] composer_names = new String[myJSON_array.length()];

            for(int i = 0; i < myJSON_array.length(); i++){
                try{
                    JSONObject aJSON_element = myJSON_array.getJSONObject(i);
                    String decName = aJSON_element.getString("composerName");
                    composer_names[i] = decName;
                }catch (JSONException e)
                {
                    Toast.makeText(getContext(), "Dude, you have to know what the JSON looks like to parse it", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            ArrayAdapter<String> aaComposer = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, composer_names);
            aaComposer.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spComposer.setAdapter(aaComposer);

            btnCalc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int year = Integer.parseInt(etYear.getText().toString());
                    int id = spComposer.getSelectedItemPosition();

                    try{
                        String portrait = myJSON_array.getJSONObject(id).getString("portraitLink");
                        int compYear = Integer.parseInt(myJSON_array.getJSONObject(id).getString("composerYear"));
                        if(compYear > year){
                            Toast.makeText(getContext(), "You picked an invalid year", Toast.LENGTH_LONG).show();
                        }else{
                            int age = year - compYear;
                            tvAge.setText("He would be " + age + " years old.");
                        }

                        if(checkBox.isChecked()){
                            loadImageFromUrl(portrait);
                        }

                    }catch(JSONException e){

                    }

                }
            });
        }catch(Exception e){
            Toast.makeText(getContext(), "Problem with file", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        return myView;
    }
    private void loadImageFromUrl(String url){
        Picasso.with(getContext()).load(url).placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(ivComposer, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });
    }
}
