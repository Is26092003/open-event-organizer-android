package com.eventyay.organizer.core.auth.signup;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eventyay.organizer.R;
import com.eventyay.organizer.common.Function;
import com.eventyay.organizer.common.mvp.view.BaseFragment;
import com.eventyay.organizer.core.auth.SharedViewModel;
import com.eventyay.organizer.core.auth.login.LoginFragment;
import com.eventyay.organizer.data.ContextUtils;
import com.eventyay.organizer.databinding.SignUpFragmentBinding;
import com.eventyay.organizer.ui.ViewUtils;
import com.eventyay.organizer.utils.ValidateUtils;

import javax.inject.Inject;

import br.com.ilhasoft.support.validation.Validator;

import static com.eventyay.organizer.ui.ViewUtils.showView;

public class SignUpFragment extends BaseFragment implements SignUpView {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    ContextUtils utilModel;

    private SignUpFragmentBinding binding;
    private Validator validator;
    private SharedViewModel sharedViewModel;
    private SignUpViewModel signUpViewModel;

    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.sign_up_fragment, container, false);
        validator = new Validator(binding);
        sharedViewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        sharedViewModel.getEmail().observe(this, email -> binding.getUser().setEmail(email));
        signUpViewModel = ViewModelProviders.of(this, viewModelFactory).get(SignUpViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        binding.setUser(signUpViewModel.getUser());

        validate(binding.emailLayout, ValidateUtils::validateEmail, getResources().getString(R.string.email_validation_error));
        signUpViewModel.getProgress().observe(this, this::showProgress);
        signUpViewModel.getSuccess().observe(this, this::onSuccess);
        signUpViewModel.getError().observe(this, this::showError);

        binding.btnSignUp.setOnClickListener(view -> {
            if (!validator.validate())
                return;

            String password = binding.password.getText().toString();
            String confirmPassword = binding.confirmPassword.getText().toString();
            if (!(signUpViewModel.arePasswordsEqual(password, confirmPassword))) {
                return;
            }

            String url = binding.url.baseUrl.getText().toString().trim();
            signUpViewModel.setBaseUrl(url, binding.url.overrideUrl.isChecked());
            signUpViewModel.signUp();
        });
        binding.loginLink.setOnClickListener(view -> openLoginPage());
    }

    @Override
    public void validate(TextInputLayout textInputLayout, Function<String, Boolean> validationReference, String errorResponse) {
        textInputLayout.getEditText().addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Nothing here
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (validationReference.apply(charSequence.toString())) {
                    textInputLayout.setError(null);
                    textInputLayout.setErrorEnabled(false);
                } else {
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError(errorResponse);
                }
                if (TextUtils.isEmpty(charSequence)) {
                    textInputLayout.setError(null);
                    textInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Nothing here
            }
        });
    }

    private void openLoginPage() {
        sharedViewModel.setEmail(binding.getUser().getEmail());
        getFragmentManager().beginTransaction()
            .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            .replace(R.id.fragment_container, new LoginFragment())
            .commit();
    }

    @Override
    public void showError(String error) {
        ViewUtils.showSnackbar(binding.getRoot(), error);
    }

    @Override
    public void onSuccess(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        openLoginPage();
    }

    @Override
    public void showProgress(boolean show) {
        showView(binding.progressBar, show);
    }

    @Override
    protected int getTitle() {
        return R.string.sign_up;
    }
}