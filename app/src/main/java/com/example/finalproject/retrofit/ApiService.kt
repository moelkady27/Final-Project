package com.example.finalproject.retrofit

import com.example.finalproject.ui.complete_register.models.CompleteSignUpResponse
import com.example.finalproject.ui.complete_register.models.LocationResponse
import com.example.finalproject.ui.setting.models.LogOutResponse
import com.example.finalproject.ui.complete_register.models.UploadPhotoResponse
import com.example.finalproject.ui.complete_register.request.CompleteSignUpRequest
import com.example.finalproject.ui.password.models.ChangePasswordResponse
import com.example.finalproject.ui.password.models.ForgotPasswordResponse
import com.example.finalproject.ui.password.models.LogOutAllResponse
import com.example.finalproject.ui.password.models.ResendCodeForgetResponse
import com.example.finalproject.ui.password.models.ResetPasswordResponse
import com.example.finalproject.ui.password.models.VerificationCodeForgetPasswordResponse
import com.example.finalproject.ui.password.request.ChangePasswordRequest
import com.example.finalproject.ui.password.request.ForgotPasswordRequest
import com.example.finalproject.ui.password.request.ResetPasswordRequest
import com.example.finalproject.ui.password.request.VerificationCodeForgetPasswordRequest
import com.example.finalproject.ui.profile.models.EditProfileResponse
import com.example.finalproject.ui.profile.request.EditProfileRequest
import com.example.finalproject.ui.register.models.ResendCodeResponse
import com.example.finalproject.ui.register.models.SignInResponse
import com.example.finalproject.ui.register.models.SignUpResponse
import com.example.finalproject.ui.register.models.VerificationCodeSignUpResponse
import com.example.finalproject.ui.register.request.SignInRequest
import com.example.finalproject.ui.register.request.SignUpRequest
import com.example.finalproject.ui.register.request.VerificationCodeSignUpRequest
import com.example.finalproject.ui.setting.models.DeleteAccountResponse
import com.example.finalproject.ui.setting.request.DeleteAccountRequest
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("api/v1/auth/signup")
    fun signup(
        @Body req: SignUpRequest
    ) : Call<SignUpResponse>

    @POST("api/v1/auth/login")
    fun login(
        @Body req: SignInRequest
    ) : Call<SignInResponse>

    @POST("api/v1/auth/verification/{id}")
    fun verifyAccount(
        @Header("Authorization") token: String,
        @Path("id") userId: String,
        @Body request: VerificationCodeSignUpRequest
    ): Call<VerificationCodeSignUpResponse>

    @GET("/api/v1/auth/resend-code/{id}")
    fun resendCode(
        @Header("Authorization") token: String,
        @Path("id") userId: String
    ): Call<ResendCodeResponse>

    @POST("api/v1/auth/complete-signup")
    fun complete(
        @Header("Authorization") token: String,
        @Body req: CompleteSignUpRequest
    ) : Call<CompleteSignUpResponse>

    @Multipart
    @POST("api/v1/auth/upload-image")
    fun uploadImage(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part
    ): Call<UploadPhotoResponse>

    @GET("api/v1/auth/logout")
    fun logout(
        @Header("Authorization") token: String,
    ): Call<LogOutResponse>

    @GET("api/v1/auth/logout-all")
    fun logoutAll(
        @Header("Authorization") token: String,
    ): Call<LogOutAllResponse>

    @POST("api/v1/auth/location")
    fun location(
        @Header("Authorization") token: String,
        @Query("longitude") longitude: Double,
        @Query("latitude") latitude: Double
    ) : Call<LocationResponse>

    @PATCH("api/v1/auth/change-password")
    fun changePass(
        @Header("Authorization") token: String,
        @Body req: ChangePasswordRequest
    ) : Call<ChangePasswordResponse>

    @POST("api/v1/auth/forgot-pass")
    fun forgotPassword(
        @Body req: ForgotPasswordRequest
    ): Call<ForgotPasswordResponse>

    @POST("api/v1/auth/verify-pass-otp/{email}")
    fun verificationCodeForgetPass(
        @Body req: VerificationCodeForgetPasswordRequest,
        @Path("email") email: String
    ): Call<VerificationCodeForgetPasswordResponse>

    @POST("api/v1/auth/resend-pass-otp/{email}")
    fun resendCodeForget(
        @Path("email") email: String
    ): Call<ResendCodeForgetResponse>

    @PATCH("api/v1/auth/reset-pass/{email}")
    fun resetPassword(
        @Path("email") email: String,
        @Body req: ResetPasswordRequest
    ): Call<ResetPasswordResponse>

    @HTTP(method = "DELETE", path = "api/v1/auth/delete-user", hasBody = true)
    fun deleteAccount(
        @Header("Authorization") token: String,
        @Body req: DeleteAccountRequest
    ): Call<DeleteAccountResponse>

    @PATCH("api/v1/auth/update-user")
    fun editProfile(
        @Header("Authorization") token: String,
        @Body req: EditProfileRequest
    ): Call<EditProfileResponse>

}