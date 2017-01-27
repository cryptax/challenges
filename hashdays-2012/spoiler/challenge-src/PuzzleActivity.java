package com.fortiguard.challenge.hashdays2012.challengeapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PuzzleActivity extends Activity {
    public TextView txtView;
    public Button validateBtn;
    public EditText editTxt;
    public Validate valid;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

	validateBtn = (Button) findViewById(R.id.validateButton);
        editTxt = (EditText) findViewById(R.id.textArea);
        txtView = (TextView) findViewById(R.id.textView1);
	valid = new Validate(this.getApplicationContext());

	validateBtn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) 
            {                
		txtView.setText(Validate.checkSecret(editTxt.getText().toString()));
		editTxt.setEnabled(false);
		validateBtn.setEnabled(false);
            }
        });

    }
}
