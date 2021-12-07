package id.ac.umn.jonathanchristian.uas_cstudio_if570D_myhabits;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class Detilactivity extends DialogFragment {
    String nama_habit, date, time, desc, key, pilih;
    FirebaseAuth mAuth;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    public Detilactivity(String nama_habit, String desc, String date, String time,
                         String key, String pilih) {
        this.nama_habit = nama_habit;
        this.desc = desc;
        this.date= date;
        this.time = time;
        this.key = key;
        this.pilih = pilih;
    }

    TextView tvlabel;
    EditText etnamahabit;
    EditText etDescriptionhabit;
    AppCompatButton varbtnDate;
    AppCompatButton varbtnTime;

    TextView vartvDatehabit;
    TextView vartvTimehabit;

    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat timeFormatter;

    //    EditText etDatehabit;
//    EditText etTimehabit;
    Button btnAdd;
    Button btnbatal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_detilactivity,
                container, false);

        tvlabel = (TextView) view.findViewById(R.id.tvlabel);
        etnamahabit = (EditText) view.findViewById(R.id.etnamahabit);
        etDescriptionhabit= (EditText) view.findViewById(R.id.etDescriptionhabit);
        varbtnDate=(AppCompatButton) view.findViewById(R.id.btnDate);
        varbtnTime=(AppCompatButton) view.findViewById(R.id.btnTime);
        dateFormatter=new SimpleDateFormat("dd--MM--yyyy", Locale.US);
        timeFormatter=new SimpleDateFormat("HH:mm:ss", Locale.US);

        varbtnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePopUp();
            }
        });

        varbtnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePopUp();
            }
        });



        vartvDatehabit=(TextView) view.findViewById(R.id.tvDatehabit);
        vartvTimehabit=(TextView) view.findViewById(R.id.tvTimehabit);

//        etDatehabit = (EditText) view.findViewById(R.id.etDatehabit);
//        etTimehabit = (EditText)view.findViewById(R.id.etTimehabit);
        btnAdd = (Button) view.findViewById(R.id.btnAdd);
        btnbatal = (Button) view.findViewById(R.id.btnbatal);

        etnamahabit.setText(nama_habit);
        etDescriptionhabit.setText(desc);
//        etDatehabit.setText(date);
//        etTimehabit .setText(time);
        vartvDatehabit.setText(date);
        vartvTimehabit.setText(time);

        btnAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String nama_habit = etnamahabit.getText().toString();
                String desc = etDescriptionhabit.getText().toString();
//                String date = etDatehabit.getText().toString();
//                String time = etTimehabit.getText().toString();
                String date=vartvDatehabit.getText().toString();
                String time=vartvTimehabit.getText().toString();

                if (TextUtils.isEmpty(nama_habit)) {
                    input((EditText) etnamahabit, "nama habit");
                } else if (TextUtils.isEmpty(desc)) {
                    input((EditText) etDescriptionhabit, "deskripsi habit");
//                } else if (TextUtils.isEmpty(date)) {
//                    input((EditText) etDatehabit, "tanggal habit");
//                } else if (TextUtils.isEmpty(time)) {
//                    input((EditText) etTimehabit, "waktu mulai habit");
                } else {
                    if(pilih.equals("Tambah")) {
                        database.child(FirebaseAuth.getInstance()
                                .getCurrentUser().getUid()).push().setValue(new
                                Habitku(nama_habit, desc, date, time))
                                .addOnSuccessListener(new OnSuccessListener() {

                                    @Override

                                    public void onSuccess(Object o) {

                                        Toast.makeText(view.getContext(), "Data Habit Tersimpan", Toast.LENGTH_SHORT).show();

                                        dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener()

                        {

                            @Override

                            public void onFailure(@NonNull Exception e)

                            {

                                Toast.makeText(view.getContext(), "Data Habit Gagal Tersimpan", Toast.LENGTH_SHORT)
                                        .show();

                            }
                        });

                    } else if(pilih.equals("Ubah")) {
                        database.child(FirebaseAuth.getInstance()
                                .getCurrentUser().getUid()).child(key).setValue(new
                                Habitku(nama_habit, desc, date, time))
                                .addOnSuccessListener(new OnSuccessListener() {

                                    @Override

                                    public void onSuccess(Object o) {

                                        Toast.makeText(view.getContext(), "Data Habit Berhasil Diupdate", Toast.LENGTH_SHORT)
                                                .show();
                                        dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener()

                        {

                            @Override
                            public void onFailure(@NonNull Exception e)

                            {

                                Toast.makeText(view.getContext(), "Data Habit Gagal Diupdate", Toast.LENGTH_SHORT)
                                        .show();

                            }
                        });
                    }
                }
            }
        });
        btnbatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });
        return view;
    }

    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if(dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.
                    MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        if(pilih.equals("Tambah")) {
            tvlabel.setText("FORM INPUT HABIT");
        } else if(pilih.equals("Ubah")) {
            tvlabel.setText("FORM EDIT HABIT");
        }
    }

    private void input(EditText txt, String s) {
        txt.setError(s+" harus diisi");
        txt.requestFocus();
    }

    public void closeFragment() {
        getActivity().getFragmentManager().popBackStack();
    }

    private void datePopUp(){
        Calendar newCalendar=Calendar.getInstance();
        datePickerDialog=new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                Calendar today=Calendar.getInstance();
                Calendar newDate=Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                //apabila vartvdatehabit<current.time,
                //print("pilih tanggal yang valid (tgl skrg atau kedepan)");
                if(newDate.before(today)){
                    Toast.makeText(getActivity().getApplicationContext(), "Pilih tanggal yang valid (tgl hari ini atau berikutnya)", Toast.LENGTH_SHORT).show();
                }
                else{
                    vartvDatehabit.setText(dateFormatter.format(newDate.getTime()));
                }
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void timePopUp()
    {
        Calendar calendar=Calendar.getInstance();
        timePickerDialog=new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                Calendar timenow=Calendar.getInstance();
                Calendar newtime=Calendar.getInstance();
                newtime.set(Calendar.HOUR_OF_DAY,hour);
                newtime.set(Calendar.MINUTE, minute);
                if(newtime.getTimeInMillis()<=timenow.getTimeInMillis()){
                    Toast.makeText(getActivity().getApplicationContext(),"Pilih jam yang valid (disarankan jam kedepan untuk menjalankan habit kedepan)", Toast.LENGTH_SHORT).show();
                }
                else{
                    vartvTimehabit.setText(timeFormatter.format(newtime.getTime()));
                }
            }
        },calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                DateFormat.is24HourFormat(getActivity()));
        timePickerDialog.show();
    }
}