package com.hippoddung.ribbit

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.Observer
import com.hippoddung.ribbit.network.ApiResponse
import com.hippoddung.ribbit.network.bodys.User
import com.hippoddung.ribbit.network.bodys.requestbody.AuthRequest
import com.hippoddung.ribbit.network.bodys.responsebody.AuthResponse
import com.hippoddung.ribbit.ui.RibbitApp
import com.hippoddung.ribbit.ui.theme.RibbitTheme
import com.hippoddung.ribbit.ui.viewmodel.AuthUiState
import com.hippoddung.ribbit.ui.viewmodel.AuthViewModel
import com.hippoddung.ribbit.ui.viewmodel.EmailUiState
import com.hippoddung.ribbit.ui.viewmodel.GetCardViewModel
import com.hippoddung.ribbit.ui.viewmodel.PWUiState
import com.hippoddung.ribbit.ui.viewmodel.PostingViewModel
import com.hippoddung.ribbit.ui.viewmodel.TokenViewModel
import com.hippoddung.ribbit.ui.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

const val BASE_IP =
    "3.36.249.200:8080"
//    "54.180.96.205:8080"
const val BASE_URL =
    "http://$BASE_IP/"
const val WS_URL =
    "ws://$BASE_IP/ws/websocket" // 리액트에서 사용하는 라이브러리가 없어서 뒤에 websocket을 붙여줘야 함.

@AndroidEntryPoint
class MainActivity @Inject constructor() : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()
    private val tokenViewModel: TokenViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private val getCardViewModel: GetCardViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            RibbitTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background    // 백그라운드 컬러가 아님, Theme에서 바꿔줘야 함.
                ) {
                    RibbitApp(
                        getCardViewModel = getCardViewModel,
                        authViewModel = authViewModel,
                        tokenViewModel = tokenViewModel,
                        userViewModel = userViewModel
                    )
                }
            }
        }
        val tokenObserver =
            Observer<String?> { token -> // tokenViewModel 객체 생성시 init 으로 DataStore 에서 token 을 LiveData에 업데이트하여 여기서 바로 감지한다.
                Log.d("HippoLog, MainActivity", "tokenObserver")
                when (token) {
                    null -> {   // 토큰값이 변경되었는데 토큰이 없는 경우라면 로그아웃밖에 상황이 없다.
                        Log.d("HippoLog, MainActivity", "토큰 없는 경우")
                        if ((authViewModel.emailUiState != EmailUiState.Lack) and (authViewModel.pWUiState != PWUiState.Lack)) {
                            Log.d(
                                "HippoLog, MainActivity",
                                "로그인 정보가 있는 경우"
                            ) // 로그인 정보가 없는 경우 따로 처리를 할 필요 없이 로그아웃상태로 두면 된다.
                            val email =
                                (authViewModel.emailUiState as EmailUiState.Exist).email
                            val pW = (authViewModel.pWUiState as PWUiState.Exist).pW
                            authViewModel.login(
                                AuthRequest(email, pW)
                            )
                        }
                        // 저장된 email, pW가 있는 경우 Login 을 시도함.
                        // Login 성공시 아래의 authObserver 에서 토큰을 저장하고, 로그인 정보가 저장됨.
                        // Login 을 실패하더라도 token값이 변화하지 않기 때문에 루프가 돌지는 않을 것 같음.
                    }

                    else -> {   // 토큰값이 변경되었는데 토큰이 있는 경우라면 토큰을 새로 받은 경우밖에 없다. 따라서 서버응답 500에러 처리를 여기서 해서는 안 된다.
                        Log.d("HippoLog, MainActivity", "토큰이 있는 경우")
                        userViewModel.getMyProfile()  // my profile 정보를 불러온다.
                    }
                }
            }
        tokenViewModel.token.observe(this, tokenObserver)

        val authObserver = Observer<ApiResponse<AuthResponse>> { response ->
            Log.d("HippoLog, MainActivity", "authObserver")
            // Update the UI, in this case, a TextView.
            when (response) {
                is ApiResponse.Failure -> {
                    Log.d("HippoLog, MainActivity", "ApiResponse 실패")
                    authViewModel.authUiState = AuthUiState.Logout
                }

                is ApiResponse.Loading -> {
                    Log.d("HippoLog, MainActivity", "ApiResponse 로딩")
                    authViewModel.authUiState = AuthUiState.LoginLoading
                }

                is ApiResponse.Success -> {
                    Log.d("HippoLog, MainActivity", "ApiResponse 성공")
                    tokenViewModel.saveToken(response.data.jwt) // authResponse 를 관찰하여 응답에서 jwt 를 추출하여 저장.
                }
            }
        }
        authViewModel.authResponse.observe(this, authObserver)

        val myProfileObserver = Observer<User?> { myProfile ->    // 여기서 myProfile 정보를 확인한다.
            Log.d("HippoLog, MainActivity", "userObserver")
            when (myProfile) {
                null -> {
                    Log.d("HippoLog, MainActivity", "유저정보 없는 경우")
//                    authViewModel.authUiState = AuthUiState.LoginLoading  // myProfile 정보 갱신은 로그인시 최조 1회이므로 여기서 다시 state를 지정해줄 필요가 없다.
//                    userViewModel.getUserProfile()    // 여기서 myProfile 정보를 불러오면 로그아웃시 myProfile 정보를 지우면서 다시 myProfile 정보를 불러오는 문제가 생김. tokenObserver에서 불러오게 함.
                }

                else -> {
                    Log.d("HippoLog, MainActivity", "유저정보 있는 경우")
                    getCardViewModel.getRibbitPosts()
                    authViewModel.authUiState =
                        AuthUiState.Login   // myProfile 정보가 있으면 최종로그인이 된 것으로 보고 AuthUiState를 로그인으로 바꿔준다.
                }
            }
        }
        userViewModel.myProfile.observe(this, myProfileObserver)

//        val homeObserver = Observer<ApiResponse<List<RibbitPost>>> { response ->
//            // Update the UI, in this case, a TextView.
//            when (response) {
//                is ApiResponse.Failure -> {
//                    if (response.code == 500) {
//                        tokenViewModel.deleteToken()
//                    }
//                    homeViewModel.homeUiState = HomeUiState.Error
//                }
//                is ApiResponse.Loading -> {
//                    homeViewModel.homeUiState = HomeUiState.Loading
//                }
//                is ApiResponse.Success -> {
//                    homeViewModel.homeUiState = HomeUiState.Success(response.data)
//                    Log.d("HippoLog, MainActivity","${homeViewModel.homeUiState}")
//                }
//            }
//        }
//        homeViewModel.homeResponse.observe(this, homeObserver)
    }
}