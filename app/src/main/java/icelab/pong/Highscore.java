package icelab.pong;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class Highscore extends Activity {
    private Button buttonAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        // Show an input dialog to set the name of player
        askName(this);

        // Get values passed to this intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            String score = String.valueOf(extras.getInt("SCORE"));
            Log.d("score", score);

            TextView tvScore = (TextView) findViewById(R.id.score);
            tvScore.setText(score);
        }

        // Set handler for the button
        setButtonAgain((Button) findViewById(R.id.button_again));
        getButtonAgain().setOnClickListener(getButtonAgain_handler());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_highscore, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private View.OnClickListener buttonAgain_handler = new View.OnClickListener() {
        public void onClick(View v) {
            // Close intent
            finish();
        }
    };

    public void askName(Context context) {
        // Create a input dialog to write name
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("What is your name ?");

        // Set up the input
        final EditText input = new EditText(context);
        // Type of input
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = input.getText().toString();
                TextView tvName = (TextView) findViewById(R.id.name);
                tvName.setText(name);
            }
        });

        // Show dialog
        builder.show();
    }

    public Button getButtonAgain() {
        return buttonAgain;
    }

    public void setButtonAgain(Button buttonAgain) {
        this.buttonAgain = buttonAgain;
    }

    public View.OnClickListener getButtonAgain_handler() {
        return buttonAgain_handler;
    }

}
