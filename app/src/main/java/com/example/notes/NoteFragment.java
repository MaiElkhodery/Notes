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

import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.notes.databinding.FragmentNoteBinding;
import com.example.notes.databinding.NoteToolbarBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;

import yuku.ambilwarna.AmbilWarnaDialog;

public class NoteFragment extends Fragment {

    private FragmentNoteBinding binding;
    private NoteToolbarBinding toolbarBinding;
    private static SetNoteFragmentListener listener;
    String title;
    String description;
    int background;
    AppCompatImageButton backIcon;
    AppCompatImageButton changeBackgroundIcon;
    AppCompatImageButton settingsIcon;
    AppCompatImageButton saveIcon;
    EditText title_editText;
    EditText description_editText;
    View fragmentNoteRoot;
    View toolbarNoteRoot;
    int defaultColor;


    private static final String NOTE_TITLE ="title";
    private static final String NOTE_DATE ="date";
    private static final String NOTE_DESCRIPTION ="description";
    private static final String NOTE_BACKGROUND ="background";

    private String noteTitle;
    private String noteDescription;
    private Date noteDate;
    private int noteBackground;

    public interface SetNoteFragmentListener{
        void onSaveButtonClick(String title, int background,String description);
    }

    public static NoteFragment newInstance(SetNoteFragmentListener clickListener) {
        listener=clickListener;
        NoteFragment fragment = new NoteFragment();
        return fragment;
    }
    public static NoteFragment openFragment(SetNoteFragmentListener clickListener,
                                            String titleParams,int backgroundParams, String descriptionParams ) {
        listener=clickListener;
        Bundle bundle = new Bundle();
        bundle.putInt(NOTE_BACKGROUND,backgroundParams);
        bundle.putString(NOTE_TITLE,titleParams);
        bundle.putString(NOTE_DESCRIPTION,descriptionParams);
        NoteFragment fragment = new NoteFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            noteTitle=getArguments().getString(NOTE_TITLE);
            noteDescription=getArguments().getString(NOTE_DESCRIPTION);
            noteBackground=getArguments().getInt(NOTE_BACKGROUND);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNoteBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        toolbarBinding = binding.noteToolbar;
        if(getArguments() != null){
            title_editText = binding.titleEditText;
            title_editText.setText(noteTitle);
            description_editText=binding.descriptionEditText;
            description_editText.setText(noteDescription);
            view.setBackgroundColor(noteBackground);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onClickBack();
        onClickColorPicker();
        onClickSave();
    }

    public void onClickBack(){
        backIcon = toolbarBinding.backIcon;
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("back","onclickBack");
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer,RecyclerviewFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
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
                    listener.onSaveButtonClick(title, background, description);
                }
            }
        });
    }
}