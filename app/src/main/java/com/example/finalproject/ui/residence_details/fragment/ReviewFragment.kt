package com.example.finalproject.ui.residence_details.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.retrofit.RetrofitClient
import com.example.finalproject.storage.AppReferences
import com.example.finalproject.storage.BaseActivity
import com.example.finalproject.ui.residence_details.adapter.ReviewAdapter
import com.example.finalproject.ui.residence_details.factory.GetReviewsFactory
import com.example.finalproject.ui.residence_details.factory.LikeReviewFactory
import com.example.finalproject.ui.residence_details.factory.RemoveLikeFactory
import com.example.finalproject.ui.residence_details.factory.RemoveUnLikeFactory
import com.example.finalproject.ui.residence_details.factory.UnLikeReviewFactory
import com.example.finalproject.ui.residence_details.models.Review
import com.example.finalproject.ui.residence_details.repository.GetReviewsRepository
import com.example.finalproject.ui.residence_details.repository.LikeReviewRepository
import com.example.finalproject.ui.residence_details.repository.RemoveLikeRepository
import com.example.finalproject.ui.residence_details.repository.RemoveUnLikeRepository
import com.example.finalproject.ui.residence_details.repository.UnLikeReviewRepository
import com.example.finalproject.ui.residence_details.viewModel.GetReviewsViewModel
import com.example.finalproject.ui.residence_details.viewModel.LikeReviewViewModel
import com.example.finalproject.ui.residence_details.viewModel.RemoveLikeViewModel
import com.example.finalproject.ui.residence_details.viewModel.RemoveUnLikeViewModel
import com.example.finalproject.ui.residence_details.viewModel.UnLikeReviewViewModel
import org.json.JSONException
import org.json.JSONObject

class ReviewFragment : Fragment() {

    private lateinit var baseActivity: BaseActivity

    private lateinit var recyclerView: RecyclerView

    private lateinit var reviewAdapter: ReviewAdapter

    private lateinit var getReviewsViewModel: GetReviewsViewModel

    private lateinit var likeReviewViewModel: LikeReviewViewModel

    private lateinit var removeLikeViewModel: RemoveLikeViewModel

    private lateinit var unLikeReviewViewModel: UnLikeReviewViewModel

    private lateinit var removeUnLikeViewModel: RemoveUnLikeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_review, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        baseActivity = BaseActivity()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        initView()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initView() {
                                    /* Get-Reviews */

        val getReviewsRepository = GetReviewsRepository(RetrofitClient.instance)
        val factory = GetReviewsFactory(getReviewsRepository)
        getReviewsViewModel = ViewModelProvider(
            this, factory
        )[GetReviewsViewModel::class.java]

        getReviews()
                                    /* Like-Review */

        val likeReviewRepository = LikeReviewRepository(RetrofitClient.instance)
        val likeReviewFactory = LikeReviewFactory(likeReviewRepository)
        likeReviewViewModel = ViewModelProvider(
            this, likeReviewFactory
        )[LikeReviewViewModel::class.java]

                                    /* Remove-Like */

        val removeLikeRepository = RemoveLikeRepository(RetrofitClient.instance)
        val removeLikeFactory = RemoveLikeFactory(removeLikeRepository)
        removeLikeViewModel = ViewModelProvider(
            this, removeLikeFactory
        )[RemoveLikeViewModel::class.java]

                                    /* UnLike-Review */

        val unLikeReviewRepository = UnLikeReviewRepository(RetrofitClient.instance)
        val unLikeReviewFactory = UnLikeReviewFactory(unLikeReviewRepository)
        unLikeReviewViewModel = ViewModelProvider(
            this, unLikeReviewFactory
        )[UnLikeReviewViewModel::class.java]

                                    /* Remove-UnLike */

        val removeUnLikeRepository = RemoveUnLikeRepository(RetrofitClient.instance)
        val removeUnLikeFactory = RemoveUnLikeFactory(removeUnLikeRepository)
        removeUnLikeViewModel = ViewModelProvider(
            this, removeUnLikeFactory
        )[RemoveUnLikeViewModel::class.java]
    }

    private fun initRecyclerView() {
        recyclerView = requireView().findViewById(R.id.recycle_review_details)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        reviewAdapter = ReviewAdapter(mutableListOf(),
            onLikeClicked = { review ->
                likeReview(review)
            },
            onRemoveLikeClicked = { review ->
                removeLike(review)
            },
            onUnLikeClicked = { review ->
                unLikeReview(review)
            },
            onRemoveUnLikeClicked = { review ->
                removeUnLike(review)
            }
        )

        recyclerView.adapter = reviewAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getReviews() {
        val token = AppReferences.getToken(requireContext())
        val residenceId = arguments?.getString("residenceId")

        if (residenceId != null) {
            getReviewsViewModel.getReviews(token, residenceId)

            getReviewsViewModel.getReviewsResponseLiveData.observe(viewLifecycleOwner) { response ->
                baseActivity.hideProgressDialog()
                response?.let {
                    val status = it.status
                    Log.e("status review", status)

                    Log.e("reviews", it.reviews.toString())

                    reviewAdapter.list = it.reviews.toMutableList()
                    reviewAdapter.notifyDataSetChanged()
                }
            }

            getReviewsViewModel.errorLiveData.observe(viewLifecycleOwner) { error ->
                BaseActivity().hideProgressDialog()
                error?.let {
                    try {
                        val errorMessage = JSONObject(error).getString("message")
                        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
                    } catch (e: JSONException) {
                        Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        } else {
            Toast.makeText(requireContext(), "Residence ID is not available", Toast.LENGTH_LONG).show()
        }
    }

    private fun likeReview(review: Review) {
        val token = AppReferences.getToken(requireContext())
        val reviewId = review._id

        likeReviewViewModel.likeReview(token, reviewId)

        likeReviewViewModel.likeReviewLiveData.observe(viewLifecycleOwner) { response ->
            baseActivity.hideProgressDialog()
            response?.let {
                val message = it.message
                Log.e("LikeReview", "LikeMessage: $message")
                Log.e("ReviewLikes", "ReviewLikes: ${it.likes}")
                Log.e("ReviewUnLikes", "ReviewUnLikes: ${it.unLikes}")

                reviewAdapter.updateLikes(reviewId, it.likes, it.unLikes)
            }
        }

        likeReviewViewModel.errorLiveData.observe(viewLifecycleOwner) { error ->
            BaseActivity().hideProgressDialog()
            error?.let {
                try {
                    val errorMessage = JSONObject(error).getString("message")
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
                } catch (e: JSONException) {
                    Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun removeLike(review: Review) {
        val token = AppReferences.getToken(requireContext())
        val reviewId = review._id

        removeLikeViewModel.removeLike(token, reviewId)

        removeLikeViewModel.removeLikeLiveData.observe(viewLifecycleOwner) { response ->
            baseActivity.hideProgressDialog()
            response?.let {
                val message = it.message
                Log.e("RemoveLike", "RemoveMessage: $message")
                Log.e("ReviewLikes", "ReviewLikes: ${it.likes}")
                Log.e("ReviewUnLikes", "ReviewUnLikes: ${it.unLikes}")

                reviewAdapter.updateLikes(reviewId, it.likes, it.unLikes)
            }
        }

        removeLikeViewModel.errorLiveData.observe(viewLifecycleOwner) { error ->
            BaseActivity().hideProgressDialog()
            error?.let {
                try {
                    val errorMessage = JSONObject(error).getString("message")
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
                } catch (e: JSONException) {
                    Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun unLikeReview(review: Review) {
        val token = AppReferences.getToken(requireContext())
        val reviewId = review._id

        unLikeReviewViewModel.unLikeReview(token, reviewId)

        unLikeReviewViewModel.unLikeReviewLiveData.observe(viewLifecycleOwner) { response ->
            baseActivity.hideProgressDialog()
            response?.let {
                val message = it.message
                Log.e("UnLikeReview", "UnLikeMessage: $message")
                Log.e("ReviewLikes", "ReviewLikes: ${it.likes}")
                Log.e("ReviewUnLikes", "ReviewUnLikes: ${it.unLikes}")

                reviewAdapter.updateLikes(reviewId, it.likes, it.unLikes)
            }
        }

        unLikeReviewViewModel.errorLiveData.observe(viewLifecycleOwner) { error ->
            BaseActivity().hideProgressDialog()
            error?.let {
                try {
                    val errorMessage = JSONObject(error).getString("message")
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
                } catch (e: JSONException) {
                    Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun removeUnLike(review: Review) {
        val token = AppReferences.getToken(requireContext())
        val reviewId = review._id

        removeUnLikeViewModel.removeUnLike(token, reviewId)

        removeUnLikeViewModel.removeUnLikeLiveData.observe(viewLifecycleOwner) { response ->
            baseActivity.hideProgressDialog()
            response?.let {
                val message = it.message
                Log.e("RemoveUnLike", "RemoveUnLikeMessage: $message")
                Log.e("ReviewLikes", "ReviewLikes: ${it.likes}")
                Log.e("ReviewUnLikes", "ReviewUnLikes: ${it.unLikes}")

                reviewAdapter.updateLikes(reviewId, it.likes, it.unLikes)
            }
        }

        removeUnLikeViewModel.errorLiveData.observe(viewLifecycleOwner) { error ->
            BaseActivity().hideProgressDialog()
            error?.let {
                try {
                    val errorMessage = JSONObject(error).getString("message")
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
                } catch (e: JSONException) {
                    Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}