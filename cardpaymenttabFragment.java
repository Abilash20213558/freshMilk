package Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.madp.CardInfo;


import com.example.madp.MainActivity5;
import com.example.madp.MainActivity6;
import com.example.madp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class cardpaymenttabFragment extends Fragment {

    EditText cardnumber,expirydate, cvv,nic;
    Button continueBtn;
    CheckBox checkBox;
    DatabaseReference cardReference;



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.cardpaymenttab_fragment, container, false);

        cardnumber = root.findViewById(R.id.cardnumber);
        expirydate = root.findViewById(R.id.date);
        cvv = root.findViewById(R.id.cvv);
        continueBtn = root.findViewById(R.id.continuebtn);
        TextView amount;
        nic = root.findViewById(R.id.nicnumber);
        checkBox =root.findViewById(R.id.checkBox);

        cardReference = FirebaseDatabase.getInstance().getReference().child("cardInfo");

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox.isChecked()) {
                    String dnic = nic.getText().toString();
                    String dcardnumber = cardnumber.getText().toString();
                    String ddate = expirydate.getText().toString();
                    String dcvv = cvv.getText().toString();

                    CardInfo cardinfo = new CardInfo(dnic, dcardnumber, ddate, dcvv);

                    cardReference.push().setValue(cardinfo);
                    Toast.makeText(getActivity(),"Card Details are Inserted!!!",Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getActivity(), MainActivity5.class);
                    startActivity(intent);
                }else{
                    Intent intentt = new Intent(getActivity(), MainActivity5.class);
                    startActivity(intentt);
                }

            }
        });



        return root;
    }

}
