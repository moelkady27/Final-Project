package com.example.finalproject.retrofit

import com.example.finalproject.ui.add_listing.models.CreateResidenceResponse
import com.example.finalproject.ui.add_listing.models.FirstCompleteResponse
import com.example.finalproject.ui.add_listing.models.FourthCompleteResponse
import com.example.finalproject.ui.add_listing.models.SecondCompleteResponse
import com.example.finalproject.ui.add_listing.models.SetLocationResidenceResponse
import com.example.finalproject.ui.add_listing.models.ThirdCompleteResponse
import com.example.finalproject.ui.add_listing.models.UploadPhotoResidenceResponse
import com.example.finalproject.ui.add_listing.request.CreateResidenceRequest
import com.example.finalproject.ui.add_listing.request.FirstCompleteRequest
import com.example.finalproject.ui.add_listing.request.FourthCompleteRequest
import com.example.finalproject.ui.add_listing.request.SecondCompleteRequest
import com.example.finalproject.ui.add_listing.request.ThirdCompleteRequest
import com.example.finalproject.ui.booking.models.GetBookedUsersResponse
import com.example.finalproject.ui.booking.models.MakeBookResponse
import com.example.finalproject.ui.chat.models.ChatListUsersResponse
import com.example.finalproject.ui.chat.models.DeleteMessageResponse
import com.example.finalproject.ui.chat.models.EditMessageResponse
import com.example.finalproject.ui.chat.models.GetConversationResponse
import com.example.finalproject.ui.chat.models.SendMessageResponse
import com.example.finalproject.ui.chat.request.EditMessageRequest
import com.example.finalproject.ui.chat.request.SendMessageRequest
import com.example.finalproject.ui.complete_register.models.CompleteSignUpResponse
import com.example.finalproject.ui.complete_register.models.LocationResponse
import com.example.finalproject.ui.setting.models.LogOutResponse
import com.example.finalproject.ui.complete_register.models.UploadPhotoResponse
import com.example.finalproject.ui.complete_register.request.CompleteSignUpRequest
import com.example.finalproject.ui.favourite.models.AddToFavouritesResponse
import com.example.finalproject.ui.favourite.models.DeleteAllFavouriteResponse
import com.example.finalproject.ui.favourite.models.DeleteFavouriteResponse
import com.example.finalproject.ui.favourite.models.GetAllFavouritesResponse
import com.example.finalproject.ui.home.models.GetAllResidencesResponse
import com.example.finalproject.ui.home.models.GetNearestResidencesResponse
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
import com.example.finalproject.ui.profile.models.ChangeProfileImageResponse
import com.example.finalproject.ui.profile.models.DeleteProfileImageResponse
import com.example.finalproject.ui.profile.models.EditProfileResponse
import com.example.finalproject.ui.profile.models.GetUserResponse
import com.example.finalproject.ui.profile.models.ResidenceResponse
import com.example.finalproject.ui.profile.request.EditProfileRequest
import com.example.finalproject.ui.recommendation.models.GetRecommendedEstatesResponse
import com.example.finalproject.ui.register.models.ResendCodeResponse
import com.example.finalproject.ui.register.models.SignInResponse
import com.example.finalproject.ui.register.models.SignUpResponse
import com.example.finalproject.ui.register.models.VerificationCodeSignUpResponse
import com.example.finalproject.ui.register.request.SignInRequest
import com.example.finalproject.ui.register.request.SignUpRequest
import com.example.finalproject.ui.register.request.VerificationCodeSignUpRequest
import com.example.finalproject.ui.residence_details.models.AddReviewResponse
import com.example.finalproject.ui.residence_details.models.GetReviewsResponse
import com.example.finalproject.ui.residence_details.models.LikeReviewResponse
import com.example.finalproject.ui.residence_details.request.AddReviewRequest
import com.example.finalproject.ui.search.SearchResponse
import com.example.finalproject.ui.setting.models.DeleteAccountResponse
import com.example.finalproject.ui.setting.request.DeleteAccountRequest
import com.example.finalproject.ui.update_listing.models.DeleteResidenceImageResponse
import com.example.finalproject.ui.update_listing.models.GetResidenceResponse
import com.example.finalproject.ui.update_listing.models.UpdateResidenceResponse
import com.example.finalproject.ui.update_listing.request.FirstUpdateRequest
import com.example.finalproject.ui.update_listing.request.FourthUpdateRequest
import com.example.finalproject.ui.update_listing.request.SecondUpdateRequest
import com.example.finalproject.ui.update_listing.request.ThirdUpdateRequest
import com.example.finalproject.ui.update_listing.request.UpdateResidenceRequest
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

    @POST("api/v1/auth/verification")
    fun verifyAccount(
        @Header("Authorization") token: String,
        @Body request: VerificationCodeSignUpRequest
    ): Call<VerificationCodeSignUpResponse>

    @GET("/api/v1/auth/resend-code")
    fun resendCode(
        @Header("Authorization") token: String
    ): Call<ResendCodeResponse>

    @POST("api/v1/auth/complete-signup")
    fun complete(
        @Header("Authorization") token: String,
        @Body req: CompleteSignUpRequest
    ) : Call<CompleteSignUpResponse>

    @Multipart
    @POST("api/v1/user/upload-image")
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

    @POST("api/v1/user/location")
    fun location(
        @Header("Authorization") token: String,
        @Query("longitude") longitude: Double,
        @Query("latitude") latitude: Double
    ) : Call<LocationResponse>

    @PATCH("api/v1/user/change-password")
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

    @HTTP(method = "DELETE", path = "api/v1/user/delete-user", hasBody = true)
    fun deleteAccount(
        @Header("Authorization") token: String,
        @Body req: DeleteAccountRequest
    ): Call<DeleteAccountResponse>

    @PATCH("api/v1/user/update-user")
    fun editProfile(
        @Header("Authorization") token: String,
        @Body req: EditProfileRequest
    ): Call<EditProfileResponse>

    @GET("api/v1/user/get-user")
    fun getProfile(
        @Header("Authorization") token: String,
    ): Call<GetUserResponse>

    @DELETE("api/v1/user/delete-profile-picture")
    fun deleteProfileImage(
        @Header("Authorization") token: String
    ): Call<DeleteProfileImageResponse>

    @Multipart
    @POST("api/v1/user/upload-image")
    fun changeImage(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part
    ): Call<ChangeProfileImageResponse>

    @GET("api/v1/chat/get-conversations")
    fun getChatUsers(
        @Header("Authorization") token: String,
        ): Call<ChatListUsersResponse>

    @POST("api/v1/chat/send/{receiverId}")
    fun sendMessage(
        @Header("Authorization") token: String,
        @Path("receiverId") receiverId: String,
        @Body req: SendMessageRequest
    ): Call<SendMessageResponse>

    @GET("api/v1/chat/get-conversation/{receiverId}")
    fun getConversation(
        @Header("Authorization") token: String,
        @Path("receiverId") receiverId: String,
    ): Call<GetConversationResponse>

    @DELETE("api/v1/chat/delete-message/{messageId}")
    fun deleteMessage(
        @Header("Authorization") token: String,
        @Path("messageId") messageId: String
    ): Call<DeleteMessageResponse>

    @PATCH("api/v1/chat/edit-message/{messageId}")
    fun editMessage(
        @Header("Authorization") token: String,
        @Path("messageId") messageId: String,
        @Body req: EditMessageRequest
    ): Call<EditMessageResponse>

    @Headers("Content-Type: application/json")
    @GET("api/v1/chat/search-users")
    fun search(
        @Header("Authorization") token: String,
        @Query("search") search: String,
    ) : Call<SearchResponse>

    @Multipart
    @POST("api/v1/chat/send/{receiverId}")
    fun sendImage(
        @Header("Authorization") token: String,
        @Path("receiverId") receiverId: String,
        @Part images: List<MultipartBody.Part>
    ): Call<SendMessageResponse>

    @POST("api/v1/residence/location/{residenceId}")
    fun locationResidence(
        @Header("Authorization") token: String,
        @Path("residenceId") residenceId: String,
        @Query("longitude") longitude: Double,
        @Query("latitude") latitude: Double
    ): Call<SetLocationResidenceResponse>

    @Multipart
    @POST("api/v1/residence/upload/{residenceId}")
    fun uploadResidenceImage(
        @Header("Authorization") token: String,
        @Path("residenceId") residenceId: String,
        @Part images: MultipartBody.Part
    ): Call<UploadPhotoResidenceResponse>

    @POST("api/v1/residence/create")
    fun createResidence(
        @Header("Authorization") token: String,
        @Body req: CreateResidenceRequest
    ): Call<CreateResidenceResponse>

    @POST("api/v1/residence/complete/1st/{residenceId}")
    fun firstComplete(
        @Header("Authorization") token: String,
        @Path("residenceId") residenceId: String,
        @Body req: FirstCompleteRequest
    ): Call<FirstCompleteResponse>

    @POST("api/v1/residence/complete/2nd/{residenceId}")
    fun secondComplete(
        @Header("Authorization") token: String,
        @Path("residenceId") residenceId: String,
        @Body req: SecondCompleteRequest
    ): Call<SecondCompleteResponse>

    @POST("api/v1/residence/complete/3rd/{residenceId}")
    fun thirdComplete(
        @Header("Authorization") token: String,
        @Path("residenceId") residenceId: String,
        @Body req: ThirdCompleteRequest
    ): Call<ThirdCompleteResponse>

    @POST("api/v1/residence/complete/4th/{residenceId}")
    fun fourthComplete(
        @Header("Authorization") token: String,
        @Path("residenceId") residenceId: String,
        @Body req: FourthCompleteRequest
    ): Call<FourthCompleteResponse>

    @GET("api/v1/residence/pending")
    fun getPendingResidence(
        @Header("Authorization") token: String,
        @Query("page") page: Int
    ): Call<ResidenceResponse>

    @GET("api/v1/residence/approved")
    fun getApprovedResidence(
        @Header("Authorization") token: String,
        @Query("page") page: Int
    ): Call<ResidenceResponse>

    @GET("api/v1/residence/sold")
    fun getSoldResidence(
        @Header("Authorization") token: String,
        @Query("page") page: Int
    ): Call<ResidenceResponse>

    @GET("api/v1/residence/all")
    fun getFeaturedEstates(
        @Header("Authorization") token: String,
        @Query("page") page: Int
    ): Call<GetAllResidencesResponse>

    @GET("api/v1/user/favorites/add/{residenceId}")
    fun addToFavorites(
        @Header("Authorization") token: String,
        @Path("residenceId") residenceId: String
    ): Call<AddToFavouritesResponse>

    @DELETE("api/v1/user/favorites/delete/{residenceId}")
    fun deleteFavorite(
        @Header("Authorization") token: String,
        @Path("residenceId") residenceId: String
    ): Call<DeleteFavouriteResponse>

    @GET("api/v1/user/favorites")
    fun getFavorites(
        @Header("Authorization") token: String
    ): Call<GetAllFavouritesResponse>

    @DELETE("api/v1/user/favorites/delete")
    fun deleteAllFavorites(
        @Header("Authorization") token: String,
    ): Call<DeleteAllFavouriteResponse>

    @GET("api/v1/residence/nearest")
    fun getNearestEstates(
        @Header("Authorization") token: String,
        @Query("page") page: Int
    ): Call<GetNearestResidencesResponse>

    @GET("api/v1/residence/get/{residenceId}")
    fun getResidence(
        @Header("Authorization") token: String,
        @Path("residenceId") residenceId: String
    ): Call<GetResidenceResponse>

    @DELETE("api/v1/residence/image/{imageId}")
    fun deleteResidenceImage(
        @Header("Authorization") token: String,
        @Path("imageId") imageId: String
    ): Call<DeleteResidenceImageResponse>

    @PATCH("api/v1/residence/update/{residenceId}")
    fun updateResidence(
        @Header("Authorization") token: String,
        @Path("residenceId") residenceId: String,
        @Body req: UpdateResidenceRequest
    ): Call<UpdateResidenceResponse>

    @PATCH("api/v1/residence/update/1st/{residenceId}")
    fun firstUpdate(
        @Body req: FirstUpdateRequest,
        @Header("Authorization") token: String,
        @Path("residenceId") residenceId: String
    ): Call<UpdateResidenceResponse>

    @PATCH("api/v1/residence/update/2nd/{residenceId}")
    fun secondUpdate(
        @Body req: SecondUpdateRequest,
        @Header("Authorization") token: String,
        @Path("residenceId") residenceId: String
    ): Call<UpdateResidenceResponse>

    @PATCH("api/v1/residence/update/3rd/{residenceId}")
    fun thirdUpdate(
        @Body req: ThirdUpdateRequest,
        @Header("Authorization") token: String,
        @Path("residenceId") residenceId: String
    ): Call<UpdateResidenceResponse>

    @PATCH("api/v1/residence/update/4th/{residenceId}")
    fun fourthUpdate(
        @Body req: FourthUpdateRequest,
        @Header("Authorization") token: String,
        @Path("residenceId") residenceId: String
    ): Call<UpdateResidenceResponse>

    @POST("api/v1/review/{residenceId}")
    fun addReview(
        @Body req: AddReviewRequest,
        @Header("Authorization") token: String,
        @Path("residenceId") residenceId: String
    ): Call<AddReviewResponse>

    @GET("api/v1/review/get/{residenceId}")
    fun getReviews(
        @Header("Authorization") token: String,
        @Path("residenceId") residenceId: String
    ): Call<GetReviewsResponse>

    @GET("api/v1/review/like/{reviewId}")
    fun likeReview(
        @Header("Authorization") token: String,
        @Path("reviewId") reviewId: String
    ): Call<LikeReviewResponse>

    @GET("api/v1/review/remove-like/{reviewId}")
    fun removeLike(
        @Header("Authorization") token: String,
        @Path("reviewId") reviewId: String
    ): Call<LikeReviewResponse>

    @GET("api/v1/review/unlike/{reviewId}")
    fun unlikeReview(
        @Header("Authorization") token: String,
        @Path("reviewId") reviewId: String
    ): Call<LikeReviewResponse>

    @GET("api/v1/review/remove-unlike/{reviewId}")
    fun removeUnlike(
        @Header("Authorization") token: String,
        @Path("reviewId") reviewId: String
    ): Call<LikeReviewResponse>

    @GET("api/v1/residence/recommend/{residenceId}")
    fun getRecommendedEstates(
        @Header("Authorization") token: String,
        @Path("residenceId") residenceId: String
    ): Call<GetRecommendedEstatesResponse>

    @GET("api/v1/residence/make-book/{residenceId}")
    fun makeBook(
        @Header("Authorization") token: String,
        @Path("residenceId") residenceId: String
    ): Call<MakeBookResponse>

    @GET("api/v1/residence/users-booked/{residenceId}")
    fun getBookedUsers(
        @Header("Authorization") token: String,
        @Path("residenceId") residenceId: String
    ): Call<GetBookedUsersResponse>
}