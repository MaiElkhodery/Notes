package com.example.notes;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.notes.databinding.FragmentNoteBinding;
import com.example.notes.databinding.NoteToolbarBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NoteFragment extends Fragment {

    private FragmentNoteBinding binding;
    private NoteToolbarBinding toolbarBinding;
    private static SetNoteFragmentListener listener;
    String title;
    String description;
    ImageView backIcon;
    ImageView changeBackground;
    ImageView settingsIcon;

    public static NoteFragment newInstance(SetNoteFragmentListener clickListener) {
        listener=clickListener;
        NoteFragment fragment = new NoteFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNoteBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        toolbarBinding = NoteToolbarBinding.inflate(inflater,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FloatingActionButton saveButton = binding.saveButton;
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = binding.titleEditText.getText().toString();
                description = binding.descriptionEditText.getText().toString();
                Log.d("info",title+" - "+description);
                if(title != null && description != null) {
                    listener.onSaveButtonClick(title, description);
                }
            }
        });
        onClickBack();
    }
    public interface SetNoteFragmentListener{
        void onSaveButtonClick(String title,String description);
    }
    public void onClickBack(){
        Log.d("back","onclickBack");
        backIcon = toolbarBinding.backIcon;
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }
}