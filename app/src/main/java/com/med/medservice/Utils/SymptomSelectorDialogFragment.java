package com.med.medservice.Utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.med.medservice.R;

import java.util.ArrayList;

public class SymptomSelectorDialogFragment extends DialogFragment {

    public interface onMultiChoiceListener{
        void onPositiveButtonClicked(String[] list, ArrayList<String> selectedSymptoms);
        void onNegativeButtonClicked();
    }

    onMultiChoiceListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener= (onMultiChoiceListener) context;
        } catch (Exception e) {
              throw new ClassCastException(getActivity().toString()+"onMultiChoiceListener must implemented");

        }

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
      //  return super.onCreateDialog(savedInstanceState);

        final ArrayList<String> selectedSymptoms = new ArrayList<>();



        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final String[] list = getActivity().getResources().getStringArray(R.array.symptoms_list);

        builder.setTitle("Select your symptoms")
                .setMultiChoiceItems(list, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if (b){
                            selectedSymptoms.add(list[i]);

                        }else {
                            selectedSymptoms.remove(list[i]);

                        }
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mListener.onPositiveButtonClicked(list, selectedSymptoms);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mListener.onNegativeButtonClicked();
                    }
                });

        return builder.create();


    }
}
