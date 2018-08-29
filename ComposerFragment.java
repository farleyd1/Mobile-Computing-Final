package com.example.farleyd1.musicians;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

/**
 * Created by farleyd1 on 12/5/2017.
 */

public class ComposerFragment extends Fragment {
    private Context mContext;
    private Activity mActivity;
    JSONArray myJSON_array;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        final View myView = inflater.inflate(R.layout.composer_fragment, container, false);

        mContext = getContext();
        mActivity = getActivity();

        final Spinner spComposer = (Spinner) myView.findViewById(R.id.spComposer);
        final Button btnPlay = (Button) myView.findViewById(R.id.btnPlay);
        final TextView tvComposer = (TextView) myView.findViewById(R.id.tvComposer);

        try{
            InputStream is = getResources().openRawResource(R.raw.composer);
            byte[] buffer = new byte[is.available()];
            while (is.read(buffer) != -1);

            String myText = new String(buffer);
            JSONObject myJSON_Object = new JSONObject(myText);
            myJSON_array = myJSON_Object.getJSONArray("composers");

            String [] song_titles = new String[myJSON_array.length()];

            for(int i = 0; i < myJSON_array.length(); i++){
                try{
                    JSONObject aJSON_element = myJSON_array.getJSONObject(i);
                    String decName = aJSON_element.getString("songTitle");
                    song_titles[i] = decName;
                }catch (JSONException e)
                {
                    Toast.makeText(getContext(), "Dude, you have to know what the JSON looks like to parse it", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            ArrayAdapter<String> aaSong = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, song_titles);
            aaSong.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spComposer.setAdapter(aaSong);

            spComposer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    try{
                        JSONObject song = myJSON_array.getJSONObject(i);
                        String composer = song.getString("composerName");
                        String year = song.getString("yearWritten");

                        tvComposer.setText("Composer: " + composer + "\nYear Composed: " + year);
                    }catch(JSONException e){

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            btnPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id = spComposer.getSelectedItemPosition();
                    //Toast.makeText(getApplicationContext(), ""+id, Toast.LENGTH_LONG).show();

                    try {
                        String ytc = myJSON_array.getJSONObject(id).getString("youtubeCode");
                        //Toast.makeText(getApplicationContext(), ytc, Toast.LENGTH_LONG).show();
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v="+ytc)));
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
}
