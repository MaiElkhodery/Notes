package com.example.notes;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
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

import yuku.ambilwarna.AmbilWarnaDialog;

public class NoteFragment extends Fragment {

    private FragmentNoteBinding binding;
    private NoteToolbarBinding toolbarBinding;
    private static SetNoteFragmentListener listener;
    String title;
    String description;
    AppCompatImageButton backIcon;
    AppCompatImageButton changeBackgroundIcon;
    AppCompatImageButton settingsIcon;
    AppCompatImageButton saveIcon;
    View fragmentNoteRoot;
    View toolbarNoteRoot;
    int defaultColor;

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
        onClickBack();
        onClickColorPicker();
        onClickSave();
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

                getActivity().getSupportFragmentManager().popBackStack(MainActivity.RECYCLER_FRAG_TAG,0);
            }
        });
    }
    public void onClickColorPicker(){

        changeBackgroundIcon=toolbarBinding.backgroundIcon;
        changeBackgroundIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("color","change color clicked");
                fragmentNoteRoot = binding.getRoot();
                toolbarNoteRoot = binding.getRoot();
                defaultColor = 0;
                openColorPicker();
            }
        });
    }
    public void openColorPicker(){
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(getContext(), defaultColor,
                new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                defaultColor=color;
                fragmentNoteRoot.setBackgroundColor(color);
                toolbarNoteRoot.setBackgroundColor(color);
            }
        });
        colorPicker.show();
    }
    public void onClickSave(){
        saveIcon = toolbarBinding.saveNoteIcon;
        saveIcon.setOnClickListener(new View.OnClickListener() {
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
    }
}