package fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



import androidx.fragment.app.Fragment;

import com.example.mad_project20.MainActivity2;
import com.example.mad_project20.R;


import Database.DBHelper;


public class paymenttabFragment extends Fragment {


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.paymenttab_fragment, container, false);

        Button btn = (Button) root.findViewById(R.id.continuebtn);
        EditText cardnum = (EditText) root.findViewById(R.id.cardnumber);
        EditText exdate = (EditText) root.findViewById(R.id.date);
        EditText cvv = (EditText) root.findViewById(R.id.cvv);



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity();

                String s_cardnumber = cardnum.getText().toString();
                String s_exdate = exdate.getText().toString();
                String s_cvv = cvv.getText().toString();
                DBHelper dbHelper = new DBHelper(getActivity());

                if(s_cardnumber.isEmpty() || s_exdate.isEmpty() || s_cvv.isEmpty()){
                    Toast.makeText(getActivity(),"Enter Values",Toast.LENGTH_SHORT).show();
                }else{
                    dbHelper.addInfo(s_cardnumber,s_exdate,s_cvv);
                    Toast.makeText(getActivity(),"Successfully Inserted",Toast.LENGTH_SHORT).show();
                }

                }
        });
        return root;
    }



    private void openActivity() {
        Intent intent = new Intent(getActivity(),MainActivity2.class);
        startActivity(intent);
    }




}
