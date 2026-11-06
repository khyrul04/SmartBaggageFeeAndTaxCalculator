package com.example.assignment1_airtravel;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText etBaggageWeight, etRate;
    private TextView tvExcessWeight, tvExcessCharge, tvTotalPayable;
    private MaterialCardView cardEconomySelect, cardBusinessSelect;
    private boolean isBusinessClass = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup Window Insets for Edge-to-Edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI components
        etBaggageWeight = findViewById(R.id.etBaggageWeight);
        etRate = findViewById(R.id.etRate);
        tvExcessWeight = findViewById(R.id.tvExcessWeight);
        tvExcessCharge = findViewById(R.id.tvExcessCharge);
        tvTotalPayable = findViewById(R.id.tvTotalPayable);
        Button btnCalculate = findViewById(R.id.btnCalculate);
        Button btnReset = findViewById(R.id.btnReset);
        MaterialButton btnBaggageTips = findViewById(R.id.btnBaggageTips);
        MaterialButton btnTaxGuide = findViewById(R.id.btnTaxGuide);
        MaterialButton btnShareBubble = findViewById(R.id.btnShareBubble);
        MaterialButton btnAboutBubble = findViewById(R.id.btnAboutBubble);

        cardEconomySelect = findViewById(R.id.cardEconomySelect);
        cardBusinessSelect = findViewById(R.id.cardBusinessSelect);

        // Interactive Ticket Class selection
        cardEconomySelect.setOnClickListener(v -> setTicketClass(false));
        cardBusinessSelect.setOnClickListener(v -> setTicketClass(true));

        // Initialize default selection to Economy
        setTicketClass(false);

        btnCalculate.setOnClickListener(v -> calculateExcessBaggageFee());
        btnReset.setOnClickListener(v -> resetForm());

        // Helpful Notice Dialogs
        btnBaggageTips.setOnClickListener(v -> showInfoDialog(R.string.dialog_tips_title, R.string.dialog_tips_message));
        btnTaxGuide.setOnClickListener(v -> showInfoDialog(R.string.dialog_guide_title, R.string.dialog_guide_message));

        // Bubble Header Actions
        btnShareBubble.setOnClickListener(v -> shareApp());
        btnAboutBubble.setOnClickListener(v -> openAboutPage());
    }

    private void setTicketClass(boolean business) {
        isBusinessClass = business;
        int skyBlueColor = Color.parseColor("#87A7D0");
        int slateColor = Color.parseColor("#27425D");

        if (business) {
            cardBusinessSelect.setStrokeWidth(dpToPx(2));
            cardBusinessSelect.setStrokeColor(skyBlueColor);
            cardEconomySelect.setStrokeWidth(dpToPx(1));
            cardEconomySelect.setStrokeColor(slateColor);
        } else {
            cardEconomySelect.setStrokeWidth(dpToPx(2));
            cardEconomySelect.setStrokeColor(skyBlueColor);
            cardBusinessSelect.setStrokeWidth(dpToPx(1));
            cardBusinessSelect.setStrokeColor(slateColor);
        }
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    private void showInfoDialog(int titleResId, int messageResId) {
        new AlertDialog.Builder(this)
                .setTitle(titleResId)
                .setMessage(messageResId)
                .setPositiveButton(R.string.dialog_close, (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void calculateExcessBaggageFee() {
        String weightStr = etBaggageWeight.getText() != null ? etBaggageWeight.getText().toString().trim() : "";
        String rateStr = etRate.getText() != null ? etRate.getText().toString().trim() : "";

        if (weightStr.isEmpty()) {
            etBaggageWeight.setError(getString(R.string.error_empty_weight));
            etBaggageWeight.requestFocus();
            Toast.makeText(this, R.string.error_empty_weight, Toast.LENGTH_SHORT).show();
            return;
        }

        if (rateStr.isEmpty()) {
            etRate.setError(getString(R.string.error_empty_rate));
            etRate.requestFocus();
            Toast.makeText(this, R.string.error_empty_rate, Toast.LENGTH_SHORT).show();
            return;
        }

        double baggageWeight;
        double ratePerKg;

        try {
            // Support both dot and comma decimal input (e.g., 25.5 or 25,5)
            baggageWeight = Double.parseDouble(weightStr.replace(',', '.'));
            ratePerKg = Double.parseDouble(rateStr.replace(',', '.'));
        } catch (NumberFormatException e) {
            Toast.makeText(this, R.string.error_invalid_number, Toast.LENGTH_SHORT).show();
            return;
        }

        if (baggageWeight < 0 || ratePerKg < 0) {
            Toast.makeText(this, "Values cannot be negative", Toast.LENGTH_SHORT).show();
            return;
        }

        // Determine allowance based on interactive card selection
        final double allowance = isBusinessClass ? 40.0 : 20.0;

        // Calculation formulas from assignment spec
        double excessWeight = (baggageWeight > allowance) ? (baggageWeight - allowance) : 0.0;
        double excessCharge = excessWeight * ratePerKg;
        double serviceTax = excessCharge * 0.08;
        double totalPayable = excessCharge + serviceTax;

        // Display results using Android getString resource formatting
        tvExcessWeight.setText(getString(R.string.result_excess_weight, excessWeight));
        tvExcessCharge.setText(getString(R.string.result_excess_charge, excessCharge));
        tvTotalPayable.setText(getString(R.string.result_total_payable, totalPayable));

        // Smooth scroll to results card so user instantly sees the output
        ScrollView mainScrollView = findViewById(R.id.main);
        MaterialCardView cardResults = findViewById(R.id.cardResults);
        if (mainScrollView != null && cardResults != null) {
            mainScrollView.post(() -> mainScrollView.smoothScrollTo(0, cardResults.getTop()));
        }
        
        // Feedback toast
        if (excessWeight == 0.0) {
            String className = isBusinessClass ? "Business (40 kg)" : "Economy (20 kg)";
            Toast.makeText(this, "Within free allowance for " + className + "! No excess charge.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Calculation completed!", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetForm() {
        etBaggageWeight.setText("");
        etRate.setText("");
        setTicketClass(false);
        tvExcessWeight.setText(R.string.placeholder_excess_weight);
        tvExcessCharge.setText(R.string.placeholder_excess_charge);
        tvTotalPayable.setText(R.string.placeholder_total_payable);
        etBaggageWeight.requestFocus();
        Toast.makeText(this, "Form reset", Toast.LENGTH_SHORT).show();
    }

    private void shareApp() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_subject));
        shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_body));
        startActivity(Intent.createChooser(shareIntent, "Share Via"));
    }

    private void openAboutPage() {
        Intent intent = new Intent(MainActivity.this, AboutActivity.class);
        startActivity(intent);
    }
}
