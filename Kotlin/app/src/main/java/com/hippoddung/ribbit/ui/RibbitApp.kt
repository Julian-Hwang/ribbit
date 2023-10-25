@file:OptIn(ExperimentalMaterial3Api::class)

package com.hippoddung.ribbit.ui

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hippoddung.ribbit.R
import com.hippoddung.ribbit.ui.screens.HomeScreen
import com.hippoddung.ribbit.ui.screens.ProfileScreen
import com.hippoddung.ribbit.ui.screens.TwitCreateScreen
import com.hippoddung.ribbit.ui.screens.authscreens.LoginScreen
import com.hippoddung.ribbit.ui.screens.authscreens.LogoutScreen
import com.hippoddung.ribbit.ui.screens.authscreens.SignUpScreen
import com.hippoddung.ribbit.ui.screens.statescreens.ErrorScreen
import com.hippoddung.ribbit.ui.screens.statescreens.LoadingScreen
import com.hippoddung.ribbit.ui.viewmodel.AuthUiState
import com.hippoddung.ribbit.ui.viewmodel.AuthViewModel
import com.hippoddung.ribbit.ui.viewmodel.HomeViewModel
import com.hippoddung.ribbit.ui.viewmodel.TokenViewModel
import com.hippoddung.ribbit.ui.viewmodel.TwitsCreateViewModel

enum class RibbitScreen(@StringRes val title: Int) {
    HomeScreen(title = R.string.home_screen),
    LoginScreen(title = R.string.login_screen),
    LogoutScreen(title = R.string.logout_screen),
    ProfileScreen(title = R.string.profile_screen),
    SignUpScreen(title = R.string.sign_up_screen),
    TwitCreateScreen(title = R.string.twit_create_screen),
    PickImageScreen(title = R.string.pick_image_screen),
    LoadingScreen(title = R.string.loading_screen),
    ErrorScreen(title = R.string.error_screen),
}

@Composable
fun RibbitApp(
    homeViewModel: HomeViewModel,
    authViewModel: AuthViewModel,
    tokenViewModel: TokenViewModel,
    twitsCreateViewModel: TwitsCreateViewModel
) {
    when (authViewModel.authUiState) {
        is AuthUiState.Login -> {
            RibbitScreen(homeViewModel, authViewModel, tokenViewModel, twitsCreateViewModel)
            Log.d("HippoLog, RibbitApp", "Login")
            Log.d("HippoLog, RibbitApp","${homeViewModel.homeUiState}")
        }
        is AuthUiState.Logout -> {
            AuthScreen(authViewModel)
            Log.d("HippoLog, RibbitApp", "Logout")
        }
    }
}

@Composable
fun RibbitScreen(
    homeViewModel: HomeViewModel,
    authViewModel: AuthViewModel,
    tokenViewModel: TokenViewModel,
    twitsCreateViewModel: TwitsCreateViewModel
) {
//    val backStackEntry by navController.currentBackStackEntryAsState()
//    val currentScreen = RibbitScreen.valueOf(backStackEntry?.destination?.route ?: RibbitScreen.SignUpScreen.name)
    val navController: NavHostController = rememberNavController()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { HippoTopAppBar(homeViewModel = homeViewModel,scrollBehavior = scrollBehavior, navController = navController) }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            NavHost(
                navController = navController,
                startDestination = RibbitScreen.HomeScreen.name,
                modifier = Modifier
            ) {
                composable(route = RibbitScreen.HomeScreen.name) {
//                    homeViewModel.getRibbitPosts() // recompositon시 계속 실행됨. 여기 함수를 두면 안 됨.
//                    Log.d("HippoLog, RibbitApp", "${homeViewModel.homeUiState}")
                    HomeScreen(
                        navController = navController,
                        homeViewModel = homeViewModel
                    )
                }
                composable(route = RibbitScreen.ProfileScreen.name) {
                    ProfileScreen()
                }
                composable(route = RibbitScreen.TwitCreateScreen.name) {
                    TwitCreateScreen(twitsCreateViewModel = twitsCreateViewModel, homeViewModel = homeViewModel,navController = navController)
                }
                composable(route = RibbitScreen.LogoutScreen.name) {
                    LogoutScreen(authViewModel = authViewModel, tokenViewModel = tokenViewModel)
                }
                composable(route = RibbitScreen.LoadingScreen.name) {
                    LoadingScreen()
                }
                composable(route = RibbitScreen.ErrorScreen.name) {
                    ErrorScreen()
                }
            }
        }
    }
}

@Composable
fun AuthScreen(
    authViewModel: AuthViewModel
) {
    val navController: NavHostController = rememberNavController()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            NavHost(
                navController = navController,
                startDestination = RibbitScreen.LoginScreen.name,
                modifier = Modifier
            ) {
                composable(route = RibbitScreen.LoginScreen.name) {
                    LoginScreen(navController, authViewModel)
                }
                composable(route = RibbitScreen.SignUpScreen.name) {
                    SignUpScreen(authViewModel)
                }
            }
        }
    }
}

@Composable
fun HippoTopAppBar(
    homeViewModel: HomeViewModel,
    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Column {
        CenterAlignedTopAppBar(
            scrollBehavior = scrollBehavior,
            title = {
                Box {
                    TextButton(onClick = {
                        navController.navigate(RibbitScreen.HomeScreen.name)
                        homeViewModel.getRibbitPosts()
                    }){
                        Text(
                            text = stringResource(R.string.app_name),
                            style = MaterialTheme.typography.headlineSmall,
                        )
                    }
                }
            },
            navigationIcon = { MainDropDownMenu(navController) },
            actions = { ProfileDropDownMenu(navController) },
            modifier = modifier
        )
//        AdBanner() // 불러오는 중 TimeOut이 자주 발생
    }
}

@Composable
fun MainDropDownMenu(navController: NavHostController) {
    var isDropDownMenuExpanded by remember { mutableStateOf(false) }

    OutlinedButton(
        onClick = { isDropDownMenuExpanded = true }
    ) {
        Text(text = "Menu")
    }

    DropdownMenu(
        expanded = isDropDownMenuExpanded,
        onDismissRequest = { isDropDownMenuExpanded = false },
        modifier = Modifier
            .wrapContentSize()
            .padding(4.dp)
    ) {
        DropdownMenuItem(
            onClick = {
                navController.navigate(RibbitScreen.HomeScreen.name)
                isDropDownMenuExpanded = false
            },
            text = {
                Text(
                    text = "Home",
                    color = Color.Blue,
                    fontStyle = FontStyle.Italic,
                    fontSize = 14.sp,
                    style = TextStyle(shadow = Shadow(Color.Black))
                )
            }
        )
        DropdownMenuItem(
            onClick = {
                println("Hello 5")
                isDropDownMenuExpanded = false
            },
            text = {
                Text(
                    text = "Print Hello 5",
                    color = Color.Blue,
                    fontStyle = FontStyle.Italic,
                    fontSize = 14.sp,
                    style = TextStyle(
                        shadow = Shadow(
                            color = Color.Black,
                            offset = Offset(3f, 3f),
                            blurRadius = 3f
                        )
                    )
                )
            }
        )
    }
}

@Composable
fun ProfileDropDownMenu(navController: NavHostController) {
    var isDropDownMenuExpanded by remember { mutableStateOf(false) }

    OutlinedButton(
        onClick = { isDropDownMenuExpanded = true }
    ) {
        Text(text = "Profile")
    }

    DropdownMenu(
        expanded = isDropDownMenuExpanded,
        onDismissRequest = { isDropDownMenuExpanded = false },
        modifier = Modifier
            .wrapContentSize()
            .padding(4.dp)
    ) {
        DropdownMenuItem(
            onClick = {
                navController.navigate(RibbitScreen.ProfileScreen.name)
                isDropDownMenuExpanded = false
            },
            text = {
                Text(
                    text = "My Profile",
                    color = Color.Blue,
                    fontStyle = FontStyle.Italic,
                    fontSize = 14.sp,
                    style = TextStyle(shadow = Shadow(Color.Black))
                )
            }
        )
        DropdownMenuItem(
            onClick = {
                navController.navigate(RibbitScreen.LogoutScreen.name)
                isDropDownMenuExpanded = false
            },
            text = {
                Text(
                    text = "Log out",
                    color = Color.Blue,
                    fontStyle = FontStyle.Italic,
                    fontSize = 14.sp,
                    style = TextStyle(shadow = Shadow(Color.Black))
                )
            }
        )
    }
}

//@Composable
//fun AdBanner(modifier: Modifier = Modifier) {
//    AndroidView(
//        modifier = modifier,
//        factory = { context ->
//            AdView(context).apply {
//                setAdSize(AdSize.FULL_BANNER)
//                // Use test ad unit ID
//                adUnitId = "ca-app-pub-3940256099942544/6300978111"
//            }
//        },
//        update = { adView ->
//            adView.loadAd(AdRequest.Builder().build())
//        }
//    )
//}