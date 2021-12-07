package id.ac.umn.jonathanchristian.uas_cstudio_if570D_myhabits;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
        private  List<Habitku> listHabit;
        private Activity activity;
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        public RecyclerAdapter(List<Habitku> listHabit, Activity activity) {
            this.listHabit = listHabit;
            this.activity = activity;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
        int viewType) {
            LayoutInflater inflater = LayoutInflater.
                    from(parent.getContext());
            View itemView = inflater.inflate(R.layout.hbt_item_layout, parent,
                    false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerAdapter.MyViewHolder holder, int position) {
            Habitku hbt= listHabit.get(position);
            holder.tvnamahabit.setText(hbt.getNama_habit());
            holder.tvdeschabit.setText("Deskripsi: "+hbt.getDesc());
            holder.tvdatehabit.setText("Tanggal: "+hbt.getDate());
            holder.tvtimehabit.setText("Waktu: "+hbt.getTime());
            holder.deleteitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.

                            Builder(activity);

                    builder.setPositiveButton("Ya", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface,
                                            int i) {
                            database.child(FirebaseAuth.getInstance()
                                    .getCurrentUser().getUid()).child(hbt.getKey())
                                    .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(activity, "Berhasil dihapus", Toast.LENGTH_SHORT).show();
                                        }
                            }).addOnFailureListener(new OnFailureListener()

                            {

                                @Override
                                public void onFailure(@NonNull Exception e)

                                {

                                    Toast.makeText(activity, "Gagal dihapus", Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    }).setNegativeButton("Tidak", new DialogInterface
                            .OnClickListener() {
                        @Override

                        public void onClick(DialogInterface dialog, int
                                which) {

                            dialog.dismiss();
                        }
                    }).setMessage("Apakah anda yakin ingin menghapus Habit ini?");
                            builder.show();
                }
            });

            holder.relapse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //reset tanggal
                    DateFormat dateformatter=new SimpleDateFormat("dd--MM--yyyy");
                    Calendar cal =Calendar.getInstance();
                    //settext ke tanggal sekarang;
                    holder.tvdatehabit.setText("Tanggal: "+dateformatter.format(cal.getTime()));
                    //reset jam
                    DateFormat timeformatter=new SimpleDateFormat("HH:mm:ss");
                    Calendar caltime=Calendar.getInstance();
                    //settext ke jam sekarang
                    holder.tvtimehabit.setText("Waktu: "+timeformatter.format(caltime.getTime()));
                }
            });


            holder.cardView.setOnLongClickListener(new View
                    .OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    FragmentManager manager = ((AppCompatActivity)activity).getSupportFragmentManager();
                    Detilactivity detilactivity= new Detilactivity(
                            hbt.getNama_habit(),
                            hbt.getDesc(),
                            hbt.getDate(),
                            hbt.getTime(),
                            hbt.getKey(),
                            "Ubah");

                    detilactivity.show(manager,"form");
                    return true;
                }
            });
        }

        @Override
        public int getItemCount() {
            return listHabit.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvnamahabit;
            TextView tvdeschabit;
            TextView tvdatehabit;
            TextView tvtimehabit;
            CardView cardView;
            ImageView deleteitem;
            ImageView relapse;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                tvnamahabit = itemView.findViewById(R.id.tvnamahabit);
                tvdeschabit = itemView.findViewById(R.id.tvdeschabit);
                tvdatehabit = itemView.findViewById(R.id.tvdatehabit);
                tvtimehabit = itemView.findViewById(R.id.tvtimehabit);
                cardView = itemView.findViewById(R.id.cardView);
                deleteitem = itemView.findViewById(R.id.deleteitem);
                relapse=itemView.findViewById(R.id.relapse);
            }
        }

}
