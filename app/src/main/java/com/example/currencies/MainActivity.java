package com.example.currencies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.currencies.NBU.NBU_API;
import com.example.currencies.NBU.NBU_Data_Adapter;
import com.example.currencies.NBU.NBU_data;
import com.example.currencies.PB.PB_API;
import com.example.currencies.PB.PB_Data_Adapter;
import com.example.currencies.PB.PB_data;
import com.example.currencies.PB.PB_jsondata;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView_pb;
    private RecyclerView recyclerView_nbu;

    private List<PB_data> pb_datas_local = new ArrayList<>();
    private PB_Data_Adapter pb_Adapter;

    private List<NBU_data> nbu_datas_local = new ArrayList<>();
    private NBU_Data_Adapter nbu_Adapter;

    static boolean IS_AUTO_SCROLL = false;
    int index = -1;

    View prevPVView = null;

    final Calendar myCalendar = Calendar.getInstance();

    private String selectedDatePB;
    private String selectedDateNBU_display;
    private String selectedDateNBU;

    TextView pb_date;
    TextView nbu_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView_pb = findViewById(R.id.recyclerview_pb);
        recyclerView_nbu = findViewById(R.id.recyclerview_nbu);


        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        recyclerView_pb.setLayoutManager(linearLayoutManager1);
        pb_Adapter = new PB_Data_Adapter(MainActivity.this, pb_datas_local);
        recyclerView_pb.setAdapter(pb_Adapter);


        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        recyclerView_nbu.setLayoutManager(linearLayoutManager2);
        nbu_Adapter = new NBU_Data_Adapter(MainActivity.this, nbu_datas_local);
        recyclerView_nbu.setAdapter(nbu_Adapter);


        pb_date = findViewById(R.id.pb_date);
        nbu_date = findViewById(R.id.nbu_date);

        Date c = myCalendar.getTime();
        SimpleDateFormat df_pb = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        SimpleDateFormat df_nbu = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        selectedDatePB = df_pb.format(c);
        selectedDateNBU = df_nbu.format(c);
        pb_date.setText(selectedDatePB);
        nbu_date.setText(selectedDatePB);

        getPB();
        getNBU();


        EditText et = findViewById(R.id.text);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String str_ccy = s.toString();
                IS_AUTO_SCROLL = true;
                index = search_CCY_NBU(str_ccy);
                if (index != -1) {
                    recyclerView_nbu.smoothScrollToPosition(index);
                    IS_AUTO_SCROLL = true;
                }
            }
        });


        recyclerView_nbu.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == SCROLL_STATE_IDLE && IS_AUTO_SCROLL && index != -1) {
                    if (prevPVView != null)
                        prevPVView.setBackgroundResource(R.color.white);
                    RecyclerView.ViewHolder v = recyclerView_nbu.findViewHolderForLayoutPosition(index);
                    if (v.itemView != null) {
                        prevPVView = v.itemView;
                        v.itemView.setBackgroundResource(R.color.green);
                    }
                        IS_AUTO_SCROLL = false;


                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        DatePickerDialog.OnDateSetListener datePB = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelPB();
            }

        };

        DatePickerDialog.OnDateSetListener dateNBU = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelNBU();
            }

        };

        pb_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(MainActivity.this, datePB, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        nbu_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(MainActivity.this, dateNBU, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    private void updateLabelPB() {
        String myFormat = "dd.MM.yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        selectedDatePB = sdf.format(myCalendar.getTime());
        pb_date.setText(selectedDatePB);

        pb_datas_local.clear();
        pb_Adapter.notifyDataSetChanged();
        getPB();
    }

    private void updateLabelNBU() {
        String myFormat = "yyyyMMdd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        selectedDateNBU = sdf.format(myCalendar.getTime());

        String myFormat_display = "dd.MM.yyyy";
        SimpleDateFormat sdf_display = new SimpleDateFormat(myFormat_display, Locale.US);
        selectedDateNBU_display = sdf_display.format(myCalendar.getTime());
        nbu_date.setText(selectedDateNBU_display);

        nbu_datas_local.clear();
        nbu_Adapter.notifyDataSetChanged();
        getNBU();
    }


    private int search_CCY_NBU(String pb) {
        IS_AUTO_SCROLL = true;

        if (pb.equals("PLZ"))
            pb = "PLN";
        if (nbu_datas_local != null && nbu_datas_local.size() > 0) {
            for (int i = 0; i < nbu_datas_local.size(); i++) {
                if (nbu_datas_local.get(i).getCc().equals(pb)) {
                    IS_AUTO_SCROLL = true;
                    return i;
                }
            }
        }
        return -1;
    }


    private void getPB() {

        ProgressBar loading_pb = findViewById(R.id.loadind_pb);
        loading_pb.setVisibility(View.VISIBLE);
        PB_API apiInterface = Controller.startPB(PB_API.class);
        Call<PB_jsondata> call = apiInterface.getData(selectedDatePB);
        call.enqueue(new Callback<PB_jsondata>() {
            @Override
            public void onResponse(Call<PB_jsondata> call, Response<PB_jsondata> response) {
                if (response.isSuccessful()) {
                    for (PB_data data : response.body().getExchangeRate()) {
                        if (data.getSaleRate() > 0 && data.getPurchaseRate() > 0) {
                            pb_datas_local.add(data);
                        }
                    }
                    pb_Adapter.notifyDataSetChanged();

                } else {
                    Log.e("error", response.message());
                }

                loading_pb.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<PB_jsondata> call, Throwable t) {
                Log.e("error 2", t.getMessage());
            }
        });
    }

    private void getNBU() {

        ProgressBar loading_nbu = findViewById(R.id.loadind_nbu);
        loading_nbu.setVisibility(View.VISIBLE);
        NBU_API apiInterface = Controller.startNBU(NBU_API.class);
        Call<List<NBU_data>> call = apiInterface.getNBUData(selectedDateNBU);
        call.enqueue(new Callback<List<NBU_data>>() {
            @Override
            public void onResponse(Call<List<NBU_data>> call, Response<List<NBU_data>> response) {
                if (response.isSuccessful()) {
                    for (NBU_data data : response.body()) {
                        nbu_datas_local.add(data);
                    }
                    nbu_Adapter.notifyDataSetChanged();
                } else {
                    Log.e("error", response.message());
                }

                loading_nbu.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<NBU_data>> call, Throwable t) {
                Log.e("error 2", t.getMessage());
            }
        });
    }

}
