package com.med.medservice.Utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.med.medservice.BookAppointmentActivity;
import com.med.medservice.DoctorVideoActivity;
import com.med.medservice.Models.CartItems.CartItemsList;
import com.med.medservice.Models.CartItems.CartPrescriptionAdapter;
import com.med.medservice.PrescriptionActivity;
import com.med.medservice.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OpenPrescribedItems {


    CartDBHelper mydb;

    RecyclerView cartRecycler;
    ArrayList<CartItemsList> cartItemsLists;

    Context context;
    String Activity;
    String session_id;

    ArrayList<DosageList> dosageLists;


    public OpenPrescribedItems(Context mContext, String Activity,String session_id) {

        dosageLists = new ArrayList<DosageList>();

        context = mContext;

        this.Activity = Activity;
        this.session_id = session_id;

        mydb = new CartDBHelper(context);
        if (mydb.numberOfRows() < 1) {
            //  mydb.insertCartItem("1", "2", "Carline", "4", "55", "66", "medicine", "URL");
            //   mydb.insertCartItem("1", "3", "Name4", "5", "66", "77", "medicine", "URL");
        }
        cartItemsLists = mydb.getAllItems();

        if (Activity.equals("DoctorVideoActivity")) {
            cartRecycler = (RecyclerView) ((DoctorVideoActivity) context).findViewById(R.id.cartRecycler);
        } else if (Activity.equals("PrescriptionActivity")) {
            cartRecycler = (RecyclerView) ((PrescriptionActivity) context).findViewById(R.id.cartRecycler);
        }

        // cartRecycler = context.findViewById(R.id.cartRecycler);
        cartRecycler.setLayoutManager(new LinearLayoutManager(context));

        CartPrescriptionAdapter adapter = new CartPrescriptionAdapter(cartItemsLists, context, Activity,
                new CartPrescriptionAdapter.AsyncApiResponse() {
                    @Override
                    public void processFinish(DosageList dosageList) {
                        dosageLists.add(dosageList);
                    }
                });
        cartRecycler.setAdapter(adapter);


    }

   /* public OpenPrescribedItems(Context mContext, String Activity, String session_id) {


    }*/

    public interface AsyncApiResponse {
        void processFinish(ArrayList<PrescriptionIDList> prescriptionIDLists, ArrayList<DosageList> dosageLists);
    }

    public AsyncApiResponse delegate = null;

    public void addPrescribeItems(AsyncApiResponse delegate) {
        this.delegate = delegate;


        mydb = new CartDBHelper(context);
        if (mydb.numberOfRows() < 1) {
            //  mydb.insertCartItem("1", "2", "Carline", "4", "55", "66", "medicine", "URL");
            //   mydb.insertCartItem("1", "3", "Name4", "5", "66", "77", "medicine", "URL");
        }
        final ArrayList<PrescriptionIDList> prescriptionIDLists = new ArrayList<>();
        cartItemsLists = mydb.getAllItems();
        int sizeItems = cartItemsLists.size();
        int i = 0;
        insertPrescriptionOnline(sizeItems, i, prescriptionIDLists);

        //        for (int i = 0; i <= cartItemsLists.size(); i++) {
//
//            if (i < cartItemsLists.size()) {
//                CartItemsList cartItemsList = cartItemsLists.get(i);
//
//                final String medicine_id = cartItemsList.getITEM_ID();
//                final String type = cartItemsList.getTYPE();
//                String quantity = cartItemsList.getQUANTITY();
//
//                final String requestBody = createJson(session_id, medicine_id, type, quantity).toString();
//
//
//                StringRequest stringRequest = new StringRequest(Request.Method.POST, new GlobalUrlApi().getNewBaseUrl() + "addPrescribedMedicines",
//                        new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String response) {
//
//                                Log.d("response_prescription", response);
//
//
//                                try {
//                                    JSONObject jsonObject = new JSONObject(response);
//                                    JSONObject jsonResponse = jsonObject.getJSONObject("Response");
//                                    JSONObject jsonData = jsonResponse.getJSONObject("Data");
//                                    String jsonStatus = jsonResponse.getString("Status");
//
//                                    String prescription_id = jsonData.getString("id");
//
//                                    prescriptionIDLists.add(new PrescriptionIDList(prescription_id, medicine_id, type));
//
//
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                    Toast.makeText(context, "Json Error.", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        },
//                        new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//
//                                AlertDialog.Builder dialog = new AlertDialog.Builder(context, R.style.DialogTheme)
//                                        .setTitle("Warning!")
//                                        .setMessage("Volley Error")
//                                        .setCancelable(false)
//                                        .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialogInterface, int i) {
//
//
//                                            }
//                                        });
//                                dialog.show();
//
//                            }
//                        }) {
//                    @Override
//                    public String getBodyContentType() {
//                        return "application/json; charset=utf-8";
//                    }
//
//                    @Override
//                    public byte[] getBody() throws AuthFailureError {
//                        try {
//                            return requestBody == null ? null : requestBody.getBytes("utf-8");
//                        } catch (UnsupportedEncodingException uee) {
//                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
//                            return null;
//                        }
//                    }
//
//                    @Override
//                    public Map<String, String> getHeaders() throws AuthFailureError {
//                        Map<String, String> headers = new HashMap<String, String>();
//                        String auth = "Bearer " + new SessionManager(context).getToken();
//                        headers.put("Authorization", auth);
//                        return headers;
//                    }
//                };
//
//                stringRequest.setRetryPolicy(new RetryPolicy() {
//                    @Override
//                    public int getCurrentTimeout() {
//                        return 10000;
//                    }
//
//                    @Override
//                    public int getCurrentRetryCount() {
//                        return 0; //retry turn off
//                    }
//
//                    @Override
//                    public void retry(VolleyError error) throws VolleyError {
//
//                    }
//                });
//
//                RequestQueue requestQueue = Volley.newRequestQueue(context);
//                requestQueue.add(stringRequest);
//
//            } else {
//                Log.d("response_prescription", "Prescription Items" + prescriptionIDLists.size());
//                delegate.processFinish(prescriptionIDLists);
//
//            }
//        }


    }

    private void insertPrescriptionOnline(int sizeItems, int i, ArrayList<PrescriptionIDList> prescriptionIDLists) {

        if (i < sizeItems) {
            CartItemsList cartItemsList = cartItemsLists.get(i);
            i++;

            final String medicine_id = cartItemsList.getITEM_ID();
            final String type = cartItemsList.getTYPE();
            String quantity = cartItemsList.getQUANTITY();

            final String requestBody = createJson(session_id, medicine_id, type, quantity).toString();


            int finalI = i;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, new GlobalUrlApi().getNewBaseUrl() + "addPrescribedMedicines",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.d("response_prescription", response);


                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONObject jsonResponse = jsonObject.getJSONObject("Response");
                                JSONObject jsonData = jsonResponse.getJSONObject("Data");
                                String jsonStatus = jsonResponse.getString("Status");

                                String prescription_id = jsonData.getString("id");

                                prescriptionIDLists.add(new PrescriptionIDList(prescription_id, medicine_id, type));
                                insertPrescriptionOnline(sizeItems, finalI, prescriptionIDLists);


                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(context, "Json Error.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            AlertDialog.Builder dialog = new AlertDialog.Builder(context, R.style.DialogTheme)
                                    .setTitle("Warning!")
                                    .setMessage("Volley Error")
                                    .setCancelable(false)
                                    .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {


                                        }
                                    });
                            dialog.show();

                        }
                    }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<String, String>();
                    String auth = "Bearer " + new SessionManager(context).getToken();
                    headers.put("Authorization", auth);
                    return headers;
                }
            };

            stringRequest.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 10000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 0; //retry turn off
                }

                @Override
                public void retry(VolleyError error) throws VolleyError {

                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);

        } else {
            Log.d("response_prescription", "Dosage Items" + dosageLists.size());
            delegate.processFinish(prescriptionIDLists,  dosageLists);

        }

    }

    private JSONObject createJson(String session_id, String medicine_id, String type, String quantity) {


        JSONObject orderJsonObject = new JSONObject();
        try {
            orderJsonObject.put("session_id", session_id);
            orderJsonObject.put("medicine_id", medicine_id);
            orderJsonObject.put("type", type);
            orderJsonObject.put("quantity", quantity);

            ///////

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return orderJsonObject;
    }
}
