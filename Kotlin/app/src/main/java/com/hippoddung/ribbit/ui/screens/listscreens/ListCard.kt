package com.hippoddung.ribbit.ui.screens.listscreens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.hippoddung.ribbit.network.bodys.RibbitListItem
import com.hippoddung.ribbit.ui.RibbitScreen
import com.hippoddung.ribbit.ui.viewmodel.GetCardViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ListCard(
    listItem: RibbitListItem,
    getCardViewModel: GetCardViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.Bottom,
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(start = 20.dp, top = 0.dp, end = 20.dp, bottom = 0.dp)
            .clickable {
                Log.d("HippoLog, UserCard", "UserCardClick")
                listItem.id.let {
                    getCardViewModel.getPostIdPost(postId = it)
                }
                navController.navigate(RibbitScreen.PostIdScreen.name)
            }
    ) {
        Icon(imageVector = Icons.Default.Comment, contentDescription = "RibbitPost")
        Row(
            modifier = modifier.padding(start = 12.dp,bottom = 4.dp)
        ) {
            listItem.listName?.let {
                Text(
                    text = it,
                    fontSize = 14.sp,
                    modifier = modifier.padding(start = 4.dp, end = 4.dp),
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            Log.d("HippoLog, UserCard", "$listItem")
            listItem.user?.fullName?.let {
                Text(
                    text = "@$it",
                    fontSize = 14.sp,
                    modifier = modifier.padding(start = 4.dp, end = 4.dp),
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
    }
}