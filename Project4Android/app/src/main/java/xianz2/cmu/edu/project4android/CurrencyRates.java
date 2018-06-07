package xianz2.cmu.edu.project4android;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class CurrencyRates extends AppCompatActivity {

    private static RadioGroup readioG;
    private static RadioButton radioButton1,radioButton2,radioButton3,radioButton4,radioButton5;
    private static Button submitButton;
    private static String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final CurrencyRates cm = this;

        /*
         * Find the "submit" button, and add a listener to it
         */
        submitButton = (Button) findViewById(R.id.submit);
        readioG = (RadioGroup) findViewById(R.id.checkR);
        radioButton1 = (RadioButton) findViewById(R.id.USD);
        radioButton2 = (RadioButton) findViewById(R.id.INR);
        radioButton3 = (RadioButton) findViewById(R.id.AUD);
        radioButton4 = (RadioButton) findViewById(R.id.CAD);
        radioButton5 = (RadioButton) findViewById(R.id.CNY);


        // get radio group value http://www.imooc.com/article/2598
        readioG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (radioButton1.getId() == checkedId) { text = radioButton1.getText().toString(); }
                else if (radioButton2.getId() == checkedId) { text = radioButton2.getText().toString(); }
                else if (radioButton3.getId() == checkedId) { text = radioButton3.getText().toString(); }
                else if (radioButton4.getId() == checkedId) { text = radioButton4.getText().toString(); }
                else if (radioButton5.getId() == checkedId) { text = radioButton5.getText().toString(); }
            }
        });

        System.out.println(text);

        // Add a listener to the send button
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View viewParam) {
                GetRates gp = new GetRates();
                gp.search(text, cm); // Done asynchronously in another thread.
            }
        });


    }

    /*
     * This is called by the Getrates object when the picture is ready.
     */
    public void ratesReady(String rates) {


        TextView showWords = (TextView)findViewById(R.id.show);
        if (rates != null) {
            showWords.setText("EUR exchange rate to " + text + ": " + rates);
        } else {
            showWords.setText("Please choose a currency!");

        }
    }
}




