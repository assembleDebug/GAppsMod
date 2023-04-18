package com.jacopomii.googledialermod.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.google.android.material.materialswitch.MaterialSwitch;
import com.jacopomii.googledialermod.R;
import com.jacopomii.googledialermod.databinding.SwitchCardBinding;

/**
 * A card that contains a text and a switch on a single line.
 * The text will be rendered in a separate TextView from the switch to prevent accidentally clicking on the text from triggering the switch.
 */
public class SwitchCardView extends LinearLayout {
    final SwitchCardBinding binding;

    public SwitchCardView(Context context) {
        super(context);

        binding = SwitchCardBinding.inflate(LayoutInflater.from(context), this, true);
    }

    public SwitchCardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        binding = SwitchCardBinding.inflate(LayoutInflater.from(context), this, true);

        final TypedArray xmlAttrs = context.obtainStyledAttributes(attrs, R.styleable.SwitchCardView);
        final String text = xmlAttrs.getString(R.styleable.SwitchCardView_text);
        final boolean enabled = xmlAttrs.getBoolean(R.styleable.SwitchCardView_enabled, true);
        xmlAttrs.recycle();

        binding.switchCardTextview.setText(text);
        binding.switchCardSwitch.setEnabled(enabled);
    }

    public MaterialSwitch getSwitch() {
        return binding.switchCardSwitch;
    }
}