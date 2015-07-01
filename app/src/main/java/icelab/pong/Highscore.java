package icelab.pong;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class Highscore extends Activity {
    private Button buttonAgain;
    private Button buttonSms;
    private Button buttonShare;

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

        // Set handler for the buttons
        setButtonAgain((Button) findViewById(R.id.button_again));
        getButtonAgain().setOnClickListener(getButtonAgain_handler());

        setButtonSms((Button) findViewById(R.id.button_sms));
        getButtonSms().setOnClickListener(getButtonSms_handler());

        setButtonShare((Button) findViewById(R.id.button_share));
        getButtonShare().setOnClickListener(getButtonShare_handler());
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

    private View.OnClickListener buttonSms_handler = new View.OnClickListener() {
        public void onClick(View v) {
            sendScore(v.getContext(), 1);
        }
    };

    private View.OnClickListener buttonShare_handler = new View.OnClickListener() {
        public void onClick(View v) {
            sendScore(v.getContext(), 2);
        }
    };

    public void sendScore(Context context, int type){
        // Type 1 SMS, 2 Share on social media
        String message = "Hi dude, i just hit " + getIntent().getExtras().getInt("SCORE") + " on KLPong !";
        if (type == 1) {
            EditText editPhone = (EditText) findViewById(R.id.phone);
            String phoneNo = String.valueOf(editPhone.getText());
            Log.d("phoneNo", String.valueOf(phoneNo));
            Log.d("message", String.valueOf(message));
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNo, null, message, null, null);
                Toast.makeText(context, "SMS sent.", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(context, "SMS failed, please try again.", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
        else if (type == 2) {
            Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "KLPong High Score");
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);

            context.startActivity(Intent.createChooser(shareIntent, "Sending KLPong score"));
        }
    }

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
                TextView tvName = (TextView) findViewById(R.id.name);
                tvName.setText(input.getText().toString());
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

    public Button getButtonSms() {
        return buttonSms;
    }

    public void setButtonSms(Button buttonSms) {
        this.buttonSms = buttonSms;
    }

    public View.OnClickListener getButtonSms_handler() {
        return buttonSms_handler;
    }

    public Button getButtonShare() {
        return buttonShare;
    }

    public void setButtonShare(Button buttonShare) {
        this.buttonShare = buttonShare;
    }

    public View.OnClickListener getButtonShare_handler() {
        return buttonShare_handler;
    }

    public void setButtonAgain_handler(View.OnClickListener buttonAgain_handler) {
        this.buttonAgain_handler = buttonAgain_handler;
    }

    public void setButtonSms_handler(View.OnClickListener buttonSms_handler) {
        this.buttonSms_handler = buttonSms_handler;
    }

    public void setButtonShare_handler(View.OnClickListener buttonShare_handler) {
        this.buttonShare_handler = buttonShare_handler;
    }
}
